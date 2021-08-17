package net.bafeimao.examples.test.guava.collection;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class MultisetTests {
	/**
	 * 类似ArrayList，但是Multiset是没有元素顺序的
	 * 在做Key出现次数统计时，内置的功能实现了需要构造类似于Map<String,Integer>才能实现统计的数据结构
	 */
	@Test
	public void tesMultiset() {
		// 允许一个键出现多次，这似乎违背了set集合的契约协定，
		// 但是**Multiset根本就没继承set接口**
		// 该集合可用于统计key出现的次数
		Multiset<String> multiset = HashMultiset.create();

		multiset.add("tom");
		multiset.add("tom");
		multiset.add("jack");

		Assert.assertTrue(multiset.count("tom") == 2);
	}
}
