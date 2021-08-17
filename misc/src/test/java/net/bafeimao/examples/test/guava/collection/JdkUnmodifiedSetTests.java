package net.bafeimao.examples.test.guava.collection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class JdkUnmodifiedSetTests {
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifiedSet() {
		Set<Integer> set = new HashSet<Integer>(Arrays.asList(1, 2, 3));
		Set<Integer> unmodifiedSet = Collections.unmodifiableSet(set);

		// throws UnsupportedOperationException
		unmodifiedSet.add(4);
	}

	@Test
	public void testUnsatisfiedUnmodifiedSet() {
		// 对于简单构造不可变集合，这里需要两行，不够方便
		Set<Integer> set = new HashSet<Integer>(Arrays.asList(1, 2, 3));
		Set<Integer> unmodifiedSet = Collections.unmodifiableSet(set);

		try {
			unmodifiedSet.add(4);
		} catch (Exception e) {
		}

		Assert.assertEquals(3, unmodifiedSet.size());

		// 对原集合的更改间接的可以操作unmodifiedSet，这是不安全的
		set.add(4); // remove等其他操作一样
		Assert.assertEquals(4, unmodifiedSet.size());
	}
}
