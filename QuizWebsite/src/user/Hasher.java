package user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Hasher {
	public static byte[] getSalt() {
		byte[] salt = new byte[16];
		new Random().nextBytes(salt);
		return salt;
	}
	
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	private static int NUM_HASHES = 10;
	public static byte[] hashPassword(String password, byte[] salt) {
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			for (int i = 0; i < NUM_HASHES; i++) {
				md.update(password.getBytes());
				md.update(salt);
			}
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
