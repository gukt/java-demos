package net.bafeimao.examples.test.guava;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class GuavaHashingTests {

	@Test
	public void test1() {
		String text = "hello";
		HashFunction func = Hashing.murmur3_128();

		func = Hashing.crc32();

		System.out.println(hash(func, text));
	}

	@Test
	public void test2() {
		HashFunction hf = Hashing.md5();
	}

	private String hash(HashFunction hashFunc, String str) {
		return hashFunc.hashString(str, Charsets.UTF_8).toString();
	}

	@Test
	public void testMd5() {
		HashFunction md5Func = Hashing.md5();

		String word = "hello";
		byte[] bytes = word.getBytes();

		String hash1 = md5Func.hashBytes(bytes).toString();
		String hash2 = md5Func.hashString(word, Charsets.UTF_8).toString();

		System.out.println(hash1);
		System.out.println(hash2);

		Assert.assertEquals(hash1, hash2);

	}

}
