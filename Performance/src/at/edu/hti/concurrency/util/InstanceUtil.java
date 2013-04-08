package at.edu.hti.concurrency.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import at.edu.hti.concurrency.Store;


public class InstanceUtil{
	public static final String STORES_IMPLEMENTATION_PREFIX = "at.edu.hti.concurrency.stores.";

	public static List<Store> returnAvailableStoreImplementations(
			) throws IOException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
	
		
		
		File storeFolder = findPackageRootDir(STORES_IMPLEMENTATION_PREFIX);
		
	
		List<Store> stores = new ArrayList<Store>();
		for (File clazz : storeFolder.listFiles()) {
			String filEName = clazz.getName();
			String className = filEName
					.substring(0, filEName.indexOf(".class"));
			
			Class<?> storeCandidate = Class.forName(STORES_IMPLEMENTATION_PREFIX
					.concat(className));
	
			if (isInstantiable(storeCandidate)) {
				if (Store.class.isAssignableFrom(storeCandidate)) {
					Object newInstance = storeCandidate.newInstance();
					if (newInstance instanceof Store) {
						stores.add((Store) newInstance);
					}
				}
			}
		}
		return stores;
	}

	protected static boolean isInstantiable(Class<?> storeCandidate) {
	
		//TODO find better implementation for this method
		int modifiers = storeCandidate.getModifiers();
		return !Modifier.isAbstract(storeCandidate.getModifiers())
				&& !Modifier.isInterface(modifiers) && Modifier.isPublic(modifiers) ;
	}

	protected static File findPackageRootDir(String searchPackage)
			throws IOException {
		URL url = null;
		Enumeration<URL> resources = ClassLoader.getSystemResources("");
		while (resources.hasMoreElements()) {
			url = (URL) resources.nextElement();
		}
		File storeFolder = new File(new File(url.getPath()),
				searchPackage.replace('.', File.separatorChar));
		return storeFolder;
	}


}
