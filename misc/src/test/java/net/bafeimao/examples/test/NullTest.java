package net.bafeimao.examples.test;

import org.junit.Test;

import com.google.common.base.Optional;

public class NullTest {
	@Test
	public void testNull() {
		int age = 0;
		System.out.println("user age:" + age);

		long money;
		money = 10L;
		System.out.println("user money" + money);

		String name = null;
		System.out.println("user name:" + name);
	}

	@Test
	public void testNullIsObject() {
		if (null instanceof Object) {
			System.out.println("null属于java.lang.Object类型");
		} else {
			System.out.println("null不属于java.lang.Object类型");
		}
	}

	@Test
	public void testOptional() throws Exception {
		Optional<Integer> possible = Optional.of(6);
		if (possible.isPresent()) {
			System.out.println("possible isPresent:" + possible.isPresent());
			System.out.println("possible value:" + possible.get());
		}
	}
}
