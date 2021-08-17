package net.bafeimao.examples.test.guava.collection;

import org.junit.Test;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

public class GuavaRangeSetTests {

	@Test
	public void test1() {
		// rangeSet:[[1‥10]]
		RangeSet<Integer> rangeSet = TreeRangeSet.create();
		rangeSet.add(Range.closed(1, 10)); // [1,10] ==> 1 <= value <= 10
		System.out.println("rangeSet:" + rangeSet);

		// rangeSet:[[1‥10], [11‥15)]
		rangeSet.add(Range.closedOpen(11, 15)); // [11‥15)
		System.out.println("rangeSet:" + rangeSet);

		// rangeSet:[[1‥10], [11‥15), (15‥20)]
		rangeSet.add(Range.open(15, 20));
		System.out.println("rangeSet:" + rangeSet);

		// rangeSet:[[1‥10], [11‥15), (15‥20)]
		rangeSet.add(Range.openClosed(0, 0)); // ignored
		System.out.println("rangeSet:" + rangeSet);

		// Remove a range
		// rangeSet:[[1‥5], [10‥10], [11‥15), (15‥20)]
		rangeSet.remove(Range.open(5, 10));
		System.out.println("rangeSet:" + rangeSet);

		rangeSet.contains(15); // false
		rangeSet.contains(17); // true
		rangeSet.rangeContaining(17); // (15‥20)
		rangeSet.encloses(Range.closedOpen(18, 20)); // true, 因为[18..20)包含在(15..20)区间内
		rangeSet.encloses(Range.closedOpen(18, 21)); // false

		// 取互补的区间集合
		// [(-∞‥1), (5‥10), (10‥11), [15‥15], [20‥+∞)]
		RangeSet<Integer> complement = rangeSet.complement();
		System.out.println(complement);

		// 添加一个区间，会导致区间合并
		// Before: [[1‥5], [10‥10], [11‥15), (15‥20)]
		// After: [[1‥10], [11‥15), (15‥20)]
		rangeSet.add(Range.open(5, 10));
		System.out.println(rangeSet);
	}

}
