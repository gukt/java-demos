package net.bafeimao.examples.test.guava.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class GuavaTableTests {
	Table<Integer, Integer, String> table = HashBasedTable.create();

	@Before
	public void before() {
		table.put(1, 1, "11");
		table.put(1, 2, "12");
		table.put(2, 1, "21");
		table.put(2, 2, "22");
	}

	@Test
	public void testSimpleTable() {
		String value = table.get(1, 2);
		System.out.println(value);

		// =======================================================

		// {1={1=11, 2=12}, 2={1=21, 2=22}}
		Map<Integer, Map<Integer, String>> map = table.rowMap();
		System.out.println(map);
		// {1=11, 2=12}
		Map<Integer, String> rowDataMap = table.row(1);
		System.out.println(rowDataMap);

		// 不能在视图里调用add添加某个值，会抛出UnsupportedOperationException
		// rowDataMap.values().add("13");

		// 如果需要的话，可以这么添加值
		// {1=11, 2=12} ==> {1=11, 2=12, 3=13}
		rowDataMap.put(rowDataMap.size() + 1, "13");

		// 如果要移除值呢？
		// {1=11, 2=12, 3=13} ==> {1=11, 2=12}
		// 等同于：table.remove(1,3)
		rowDataMap.remove(3);

		// =======================================================

		// {1={1=11, 2=21}, 2={1=12, 2=22}}
		Map<Integer, Map<Integer, String>> columnMap = table.columnMap();
		System.out.println(columnMap);
		// {1=11, 2=21} (rowKey -> value)
		Map<Integer, String> rowColumnValueMap = table.column(1);
		System.out.println(rowColumnValueMap);

		// =======================================================

		// [1, 2]
		Set<Integer> rowKeys = table.rowKeySet();
		System.out.println(rowKeys);
		// [1, 2]
		Set<Integer> keyset = table.columnKeySet();
		System.out.println(keyset);

		// =======================================================

		// [(1,1)=11, (1,2)=12, (2,1)=21, (2,2)=22]
		Set<Cell<Integer, Integer, String>> cells = table.cellSet();
		System.out.println(cells);

		table.contains(1, 3); // false
		table.containsColumn(2); // true

		table.containsRow(3); // false
		table.containsRow(1); // true;

		table.containsValue("12"); // true
		table.containsValue("13"); // false;

		// [11, 12, 21, 22]
		Collection<String> values = table.values();
		System.out.println(values);
	}

	@Test
	public void test1() {
		ArrayTable<String, Integer, Long> table;
	}
}
