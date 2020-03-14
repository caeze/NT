package test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import console.Log;

/**
 * Test suite class.
 *
 * @author Clemens Strobel
 * @date 2020/02/04
 */
public class TestSuite {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean startTests() {
		boolean success = true;
		try {
			Class[] classes = getClasses(TestSuite.class.getPackage().getName());
			for (Class c : classes) {
				if (!Testable.class.isAssignableFrom(c) || Testable.class.equals(c)) {
					continue;
				}
				Object o = c.getDeclaredConstructor().newInstance();
				Method[] methods = o.getClass().getDeclaredMethods();
				for (Method m : methods) {
					boolean b = (Boolean) m.invoke(o);
					success &= b;
					if (!b) {
						Log.error(TestSuite.class, "Test " + m + " in " + o + " failed!");
					}
				}
			}
		} catch (Exception e) {
			success = false;
			Log.error(TestSuite.class, e.toString());
			Log.error(TestSuite.class, e.getCause().toString());
			for (StackTraceElement t : e.getStackTrace()) {
				Log.error(TestSuite.class, t.toString());
			}
		}
		return success;
	}

	@SuppressWarnings("rawtypes")
	private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	@SuppressWarnings("rawtypes")
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
