package ca.midnight.classloaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ByteArrayClassLoader extends ClassLoader {

	private Map<String, byte[]> classes;

	public ByteArrayClassLoader(HashMap<String, byte[]> classes) {
		this.classes = classes;
	}

	public ByteArrayClassLoader() {
		this(new HashMap<String, byte[]>());
	}
	
	public Class<?> addClass(MClass klass) throws ClassNotFoundException {
		klass.setClassLoader(this);
		return addClass(klass.getClassData());
	}
	
	public Class<?> addClass(String name, byte[] data) throws ClassNotFoundException {
		try {
			if(MClass.isCompressed(data))
				data = MClass.compress(data);
			classes.put(name, data);
			return findClass(name);
		} catch (NoClassDefFoundError e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String error = sw.toString();
			if (error.contains("wrong name")) {
				if (classes.containsKey(name))
					classes.remove(name);
				String correctClass = correctClass(error, data);
				classes.put(correctClass, data);
				return findClass(correctClass);
			}
		}
		return null;
	}
	
	public Class<?> addClass(String name, InputStream is) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int read;
		byte[] buffer = new byte[1024];
		while ((read = is.read(buffer, 0, buffer.length)) != -1)
			bos.write(buffer, 0, read);
		bos.flush();
		bos.close();
		return addClass(name, bos.toByteArray());
	}
	
	public Class<?> addClass(byte[] data) throws ClassNotFoundException {
		return addClass(UUID.randomUUID().toString(), data);
	}

	public Class<?> addClass(InputStream is) throws IOException, ClassNotFoundException {
		return addClass(UUID.randomUUID().toString(), is);
	}

	private String correctClass(String error, byte[] data) {
		String correctClass = error.substring(error.indexOf("wrong name: ") + 12);
		correctClass = correctClass.substring(0, correctClass.indexOf(")"));
		return correctClass.replaceAll("/", ".");
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException {
		if(classes.containsKey(name)) {
			byte[] data = classes.get(name);
			return defineClass(name, data, 0, data.length);
		} else {
			return super.findClass(name);
		}
	}
}