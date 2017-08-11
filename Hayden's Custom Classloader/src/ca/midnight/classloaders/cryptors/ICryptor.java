package ca.midnight.classloaders.cryptors;

public interface ICryptor {
	public byte[] encrypt(byte[] data);
	public byte[] decrypt(byte[] data);
}
