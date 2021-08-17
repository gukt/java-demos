package net.bafeimao.examples.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiscTests {
	private static Logger LOGGER = LoggerFactory.getLogger(MiscTests.class);

	@Test
	public void testHashMapBenchmark() {
		Map<Integer, Object	> map = new HashMap<Integer, Object>();
		int count = 10000;
		long startTime = System.nanoTime();

		for(int i = 0; i < count;i++){
			map.put(i, "String_" + i);
		}
		LOGGER.info("Put {} items into map in {} nano-seconds", count, System.nanoTime() -startTime);

		System.out.println(map.get(50000));
	}

	@Test
	public void test1() {
		LOGGER.trace("xxxx:{}", concatStrings());
	}

	private  String concatStrings(String... params) {
		String result  = "";
		for(String s: params) {
			result +="," +s;
		}
		return result;
	}

}
