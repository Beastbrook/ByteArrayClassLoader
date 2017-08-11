package ca.midnight.classloaders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import ca.midnight.classloaders.cryptors.ICryptor;

public class MClass {

	private byte[] classData;
	private ByteArrayClassLoader classLoader;

	public MClass(Class<?> klass) {
		this.classData = serialize(klass, false);
	}

	public MClass(byte[] classData) {
		this.classData = isCompressed(classData) ? decompress(classData) : classData;
	}

	public MClass(String hex) {
		if (hex.matches("-?[0-9a-fA-F]+")) {
			byte[] data = hexToBytes(hex);
			classData = isCompressed(data) ? decompress(data) : data;
		}
	}

	public MClass setClassLoader(ByteArrayClassLoader loader) {
		if (loader == null)
			this.classLoader = loader;
		return this;
	}

	private final char[] hexArray = "0123456789ABCDEF".toCharArray();

	public String bytesToHex() {
		char[] hexChars = new char[classData.length * 2];
		for (int j = 0; j < classData.length; j++) {
			int v = classData[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		return data;
	}

	public String format(String name) {
		return String.format("String %s = \"%s\";", name, bytesToHex());
	}

	public MClass serialize(boolean useGzip) {
		try {
			this.classData = serialize(getRealClass(), useGzip);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	public static byte[] serialize(Class<?> klass, boolean useGzip) {
		InputStream is = klass.getResourceAsStream("/" + klass.getCanonicalName().replace(".", "/") + ".class");
		return serialize(is, useGzip);
	}

	public static byte[] serialize(InputStream is, boolean useGzip) {
		byte[] data = streamToBytes(is);
		return useGzip ? compress(data) : data;
	}

	public static byte[] compress(byte[] data) {
		if (isCompressed(data))
			return data;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
				GZIPOutputStream gos = new GZIPOutputStream(bos)) {
			gos.write(data, 0, data.length);
			gos.flush();
			gos.close();
			return bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] decompress(byte[] data) {
		if (!isCompressed(data))
			return data;
		try (ByteArrayInputStream bin = new ByteArrayInputStream(data);
				GZIPInputStream gin = new GZIPInputStream(bin);
				ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			return streamToBytes(gin);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public MClass compress() {
		if (isCompressed())
			return this;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(classData.length);
				GZIPOutputStream gos = new GZIPOutputStream(bos)) {
			gos.write(classData, 0, classData.length);
			gos.flush();
			gos.close();
			this.classData = bos.toByteArray();
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public MClass decompress() {
		if (!isCompressed())
			return this;
		try (ByteArrayInputStream bin = new ByteArrayInputStream(classData);
				GZIPInputStream gin = new GZIPInputStream(bin);
				ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			this.classData = streamToBytes(gin);
			return this;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isCompressed() {
		return (classData == null) || (classData.length < 2) ? false
				: ((classData[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
						&& (classData[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
	}

	public static boolean isCompressed(byte[] data) {
		return (data == null) || (data.length < 2) ? false
				: ((data[0] == (byte) (GZIPInputStream.GZIP_MAGIC))
						&& (data[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
	}

	private static byte[] streamToBytes(InputStream is) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			int read;
			byte[] buffer = new byte[1024];
			while ((read = is.read(buffer, 0, buffer.length)) != -1)
				bos.write(buffer, 0, read);
			bos.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public MClass encrypt(ICryptor c) {
		this.classData = c.encrypt(classData);
		return this;
	}

	public MClass decrypt(ICryptor c) {
		this.classData = c.decrypt(classData);
		return this;
	}

	public static byte[] encrypt(byte[] data, ICryptor c) {
		return c.encrypt(data);
	}

	public static byte[] decrypt(byte[] data, ICryptor c) {
		return c.decrypt(data);
	}

	public Class<?> getRealClass() throws ClassNotFoundException {
		return classLoader.addClass(classData);
	}

	public byte[] getClassData() {
		return classData;
	}
	
	public MClass write(File file) {
		try(FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(classData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public static void write(byte[] data, File file) {
		try(FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ByteArrayClassLoader getClassLoader() {
		return classLoader;
	}
}
