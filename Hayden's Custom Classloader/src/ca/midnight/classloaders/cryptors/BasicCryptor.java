package ca.midnight.classloaders.cryptors;

/**
 * 
 * @deprecated
 *
 */
public class BasicCryptor implements ICryptor {

	@Override
	public byte[] encrypt(byte[] data) {
		byte[] encrypted = new byte[data.length];
		for(int i = 0; i < data.length; i++)
			encrypted[i] = (byte) ~data[i];
		return encrypted;
	}

	@Override
	public byte[] decrypt(byte[] data) {
		return encrypt(data);
	}
}
