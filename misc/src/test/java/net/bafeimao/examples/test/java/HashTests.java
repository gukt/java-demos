package net.bafeimao.examples.test.java;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class HashTests {
	@Test
	public void test1() {
		System.out.println(HashUtils.md5AsString("hello"));

	}

	static class HashUtils {
		public static byte[] md5(String word) {
			MessageDigest digest;

			try {
				digest = MessageDigest.getInstance("md5");
				digest.update(word.getBytes());
				return digest.digest();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static String md5AsString(String word) {
			byte[] bytes = md5(word);
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				sb.append(Integer.toHexString(b & 0xff));
			}
			return sb.toString();
		}
	}

}
