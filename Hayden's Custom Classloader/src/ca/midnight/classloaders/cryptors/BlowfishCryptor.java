package ca.midnight.classloaders.cryptors;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class BlowfishCryptor implements ICryptor {

	private String secretKey;

	public BlowfishCryptor(String secretKey) {
		this.secretKey = secretKey;
	}
	
	public BlowfishCryptor() {
		this("MidnightB!u3");
	}
	
	@Override
	public byte[] encrypt(byte[] data) {
		try {
			SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public byte[] decrypt(byte[] data) {
		try {
			SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public BlowfishCryptor setKey(String secretKey) {
		this.secretKey = secretKey;
		return this;
	}
}
