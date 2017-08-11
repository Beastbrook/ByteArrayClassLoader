package ca.midnight.classloaders.cryptors;

public class Cryptors {
	
	/**
	 * @deprecated
	 */
	public static BasicCryptor BASIC;
	public static BlowfishCryptor BLOWFISH;
	
	static {
		BASIC = new BasicCryptor();
		BLOWFISH = new BlowfishCryptor();
	}
}
