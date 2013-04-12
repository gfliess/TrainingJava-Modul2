package at.edu.hti.concurrency.simplewebserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Simple NIO web server
 * 
 * Initial version taken from:
 * http://forums.bukkit.org/threads/ultra-fast-java-nio-webserver-less-than-350-lines.101080/
 * 
 * @author Martin
 *
 */
public class NIOSimpleWebServer implements Runnable {

  private Charset charset = Charset.forName("UTF-8");
  private CharsetEncoder encoder = charset.newEncoder();
  private Selector nioSelector = Selector.open();
  private ServerSocketChannel nioServerSocketChannel = ServerSocketChannel.open();
  private boolean isRunning = true;

  public static boolean gotSelectedKeys = false;
  protected final static String WORKING_DIR = "/tmp/webserver";

  protected NIOSimpleWebServer(InetSocketAddress ipAddressWithPort) throws IOException {
    nioServerSocketChannel.socket().bind(ipAddressWithPort);
    nioServerSocketChannel.configureBlocking(false);
    nioServerSocketChannel.register(nioSelector, SelectionKey.OP_ACCEPT);
  }

  /**
   * Core run method. This is not a thread safe method, however it is non
   * blocking. If an exception is encountered it will be thrown wrapped in a
   * RuntimeException, and the server will automatically be {@link #shutDown}
   */
  @Override
  public final void run() {
    if (isRunning) {
      try {
        nioSelector.selectNow();
        Iterator<SelectionKey> nioSelectedKeys = nioSelector.selectedKeys().iterator();

        gotSelectedKeys = nioSelectedKeys.hasNext();

        while (nioSelectedKeys.hasNext()) {
          SelectionKey nioSelectedKey = nioSelectedKeys.next();
          nioSelectedKeys.remove();
          if (!nioSelectedKey.isValid()) {
            System.out.println("invalid key from nio selector: " + nioSelectedKey);
            continue;
          }
          try {

            if (nioSelectedKey.isAcceptable()) {

              SocketChannel client = nioServerSocketChannel.accept();
              client.configureBlocking(false);
              client.register(nioSelector, SelectionKey.OP_READ);
              System.out.println("accepted new connection/client: " + client);

            } else if (nioSelectedKey.isReadable()) {

              SocketChannel client = (SocketChannel) nioSelectedKey.channel();
              NIOSimpleHTTPSession httpSession = (NIOSimpleHTTPSession) nioSelectedKey.attachment();
              if (httpSession == null) {
                httpSession = new NIOSimpleHTTPSession(client);
                nioSelectedKey.attach(httpSession);
                System.out.println("created and attached new http session: " + httpSession);
              } else {
                System.out.println("http session already existing: " + httpSession);
              }

              httpSession.readData();

              String line;
              while ((line = httpSession.readLine()) != null) {
                System.out.println("read line from session: " + line);
                if (line.isEmpty()) {
                  HTTPRequest request = new HTTPRequest(httpSession.readLines.toString());
                  httpSession.sendResponse(handle(request));
                  httpSession.close();
                }
              }
            }
          } catch (Exception e) {
            System.out.println("Error handling session/client: " + nioSelectedKey.channel());
            System.out.println("Got Exception: " + e.getMessage());

            if (nioSelectedKey.attachment() instanceof NIOSimpleHTTPSession) {
              ((NIOSimpleHTTPSession) nioSelectedKey.attachment()).close();
            }
          }
        }
      } catch (IOException e) {
        System.out.println("Going to shutdown after caught exception: " + e.getMessage());
        shutdown();
      }
    }
  }

  private HTTPResponse prepareNotFoundResponse(HTTPResponse httpResponse, String fileName) {
    httpResponse.setHttpResponseContent(ByteBuffer
        .wrap(("<html><head><title>404</title></head><body><h1>404 - Not found on server: " + fileName + "</h1></body></html>").getBytes()));
    httpResponse.setHttpResponseCode(404);
    httpResponse.setHttpResponseReason("NOT FOUND");
    return httpResponse;
  }

  protected HTTPResponse handle(HTTPRequest httpRequest) throws IOException {
    HTTPResponse httpResponse = new HTTPResponse();

    String fileName = WORKING_DIR + httpRequest.getLocation();
    System.out.println("trying to read file: " + fileName);

    FileInputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(fileName);
    } catch (IOException e) {
      return prepareNotFoundResponse( httpResponse, fileName );
    }

    FileChannel fileChannel = fileInputStream.getChannel();

    ByteBuffer byteBuffer = ByteBuffer.allocate(fileInputStream.available());

    int numberOfBytesRead = fileChannel.read(byteBuffer);

    fileChannel.close();
    fileInputStream.close();

    System.out.println("got " + numberOfBytesRead + " bytes from file... ");

    byteBuffer.rewind();
    httpResponse.setHttpResponseContent(byteBuffer);

    return httpResponse;
  }

  public final void shutdown() {
    isRunning = false;
    try {
      nioSelector.close();
      nioServerSocketChannel.close();
    } catch (IOException e) {
      System.out.println("Caught IOException during shutdown: " + e.getMessage());
    }
  }

  public final class NIOSimpleHTTPSession {

    private final SocketChannel nioSocketChannel;
    private final ByteBuffer buffer = ByteBuffer.allocate(2048);
    private final StringBuilder readLines = new StringBuilder();
    private int mark = 0;

    public NIOSimpleHTTPSession(SocketChannel nioSocketChannel) {
      this.nioSocketChannel = nioSocketChannel;
    }

    public String readLine() throws IOException {
      StringBuilder sb = new StringBuilder();
      int l = -1;
      while (buffer.hasRemaining()) {
        char c = (char) buffer.get();
        sb.append(c);
        if (c == '\n' && l == '\r') {
          mark = buffer.position();
          readLines.append(sb);
          return sb.substring(0, sb.length() - 2);
        }
        l = c;
      }
      return null;
    }


    /**
     * 
     * @throws IOException
     */
    public void readData() throws IOException {
      buffer.limit(buffer.capacity());
      int read = nioSocketChannel.read(buffer);
      if (read == -1) {
        throw new IOException("End of stream");
      }
      buffer.flip();
      buffer.position(mark);
    }

    private void writeLine(String line) throws IOException {
      nioSocketChannel.write(encoder.encode(CharBuffer.wrap(line + "\r\n")));
    }

    /**
     * 
     * @param httpResponse
     */
    public void sendResponse(HTTPResponse httpResponse) {
      httpResponse.addDefaultHeaders();
      try {
        writeLine(httpResponse.getHttpVersion() + " " + httpResponse.getHttpResponseCode() + " " + httpResponse.getHttpResponseReason());
        for (Map.Entry<String, String> header : httpResponse.getHttpHeaders().entrySet()) {
          writeLine(header.getKey() + ": " + header.getValue());
        }
        writeLine("");
        int numberOfBytesWrote = nioSocketChannel.write(httpResponse.getHttpResponseContent());

        System.out.println("wrote " + numberOfBytesWrote + " bytes to socket ...");
      } catch (IOException e) {
        System.out.println("Caught during sending response: " + e.getMessage());
      }
    }

    public void close() {
      try {
        nioSocketChannel.close();
      } catch (IOException e) {
        System.out.println("Caught during closing nio socket channel: " + e.getMessage());
      }
    }
  }

  public static class HTTPRequest {

    private final String rawRequestData;
    private String httpMethod;
    private String httpLocation;
    private String httpVersion;
    private Map<String, String> httpHeaders = new HashMap<String, String>();

    public HTTPRequest(String raw) {
      this.rawRequestData = raw;
      parse();
    }

    private void parse() {

      // parse first line
      StringTokenizer tokenizer = new StringTokenizer(rawRequestData);
      httpMethod = tokenizer.nextToken().toUpperCase();
      httpLocation = tokenizer.nextToken();
      httpVersion = tokenizer.nextToken();

      // parse the headers
      String[] lines = rawRequestData.split("\r\n");
      for (int i = 1; i < lines.length; i++) {
        String[] keyVal = lines[i].split(":", 2);
        httpHeaders.put(keyVal[0], keyVal[1]);
      }
    }

    public String getMethod() {
      return httpMethod;
    }

    public String getLocation() {
      return httpLocation;
    }

    public String getHead(String key) {
      return httpHeaders.get(key);
    }

    public String getVersion() {
      return httpVersion;
    }

    public String toString() {
      return rawRequestData;
    }

  }

  /**
   * Class for HTTP-Response
   * 
   * @author Martin
   * 
   */
  public static class HTTPResponse {

    private String httpVersion = "HTTP/1.1";
    private int httpResponseCode = 200;
    private String httpResponseReason = "OK";
    private Map<String, String> httpHeaders = new LinkedHashMap<String, String>();
    private ByteBuffer httpResponseContent;

    private void addDefaultHeaders() {
      httpHeaders.put("Date", new Date().toString());
      httpHeaders.put("Server", "Java Simple NIO Webserver");
      httpHeaders.put("Connection", "close");
      httpHeaders.put("Content-Length", Integer.toString(httpResponseContent.capacity()));
    }

    public int getHttpResponseCode() {
      return httpResponseCode;
    }

    public String getHttpResponseReason() {
      return httpResponseReason;
    }

    public String getHttpHeader(String header) {
      return httpHeaders.get(header);
    }

    public Map<String, String> getHttpHeaders() {
      return httpHeaders;
    }

    public ByteBuffer getHttpResponseContent() {
      return httpResponseContent;
    }

    public void setHttpResponseCode(int responseCode) {
      this.httpResponseCode = responseCode;
    }

    public void setHttpResponseReason(String responseReason) {
      this.httpResponseReason = responseReason;
    }

    public void setHttpResponseContent(ByteBuffer httpResponseContent) {
      this.httpResponseContent = httpResponseContent;
    }

    public void setHttpHeader(String key, String value) {
      httpHeaders.put(key, value);
    }

    public String getHttpVersion() {
      return httpVersion;
    }
  }

  /**
   * Main method
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    final int tcpServerPort = 4711;
    final int sleepInterval = 100;

    NIOSimpleWebServer httpServer = new NIOSimpleWebServer(new InetSocketAddress(tcpServerPort));
    System.out.println("running server instance with server tcp port " + tcpServerPort + "...");

    boolean printedNoKeysSelectedNotification = false;

    while (true) {

      httpServer.run(); // not blocking

      if (!gotSelectedKeys) {
        if (!printedNoKeysSelectedNotification) {
          System.out.println("going to sleep for " + sleepInterval + " ms after running server instance ... (not repeating this info until keys get selected)");
          printedNoKeysSelectedNotification = true;
        }
      } else {
        printedNoKeysSelectedNotification = false;
      }

      // *********************************************************************************************
      // now we could do other things here prior to calling "httpServer.run()"
      // again, thanks to NIO :)
      // *********************************************************************************************

      Thread.sleep(sleepInterval);
    }
  }

}