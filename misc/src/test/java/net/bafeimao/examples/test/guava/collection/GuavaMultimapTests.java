package net.bafeimao.examples.test.guava.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;

public class GuavaMultimapTests {

	Multimap<String, String> teamMembersMap = HashMultimap.create();

	@Before
	public void before() {
		teamMembersMap.put("team1", "tom");
		teamMembersMap.put("team2", "jack");
		teamMembersMap.put("team1", "steven");
		teamMembersMap.put("team1", "jerry");
		teamMembersMap.put("team2", "bob");
	}

	/**
	 * Multimap允许一个键存在多个value
	 */
	@Test
	public void testMultimapGet() {
		Collection<String> members = teamMembersMap.get("team1");
		System.out.println(members); // [tom, steven, jerry]
	}

	/**
	 * Multimap等同于Map<String, Collection<String>>的功能，通过asMap方法就可以得到这个视图
	 */
	@Test
	public void testRemoveFromView() {
		// 视图上可以支持remove操作
		Map<String, Collection<String>> viewMap = teamMembersMap.asMap();
		viewMap.remove("team2");
		Assert.assertEquals(0, teamMembersMap.get("team2").size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testPutFromView() {
		// 视图上不支持put操作
		Map<String, Collection<String>> viewMap = teamMembersMap.asMap();
		viewMap.put("team3", Arrays.asList("ktgu"));
		System.out.println(teamMembersMap);
	}

	@Test
	public void testMultimapWithVariableOps() {
		Multimap<String, Integer> teamRolesMap = HashMultimap.create();

		teamRolesMap.put("team1", 1);
		teamRolesMap.put("team1", 2);
		teamRolesMap.put("team1", 3);
		teamRolesMap.put("team2", 4);

		// {team1=[1, 2, 3], team2=[4]}
		Map<String, Collection<Integer>> viewMap = teamRolesMap.asMap();
		System.out.println(viewMap);

		// [1, 2, 3]
		Collection<Integer> values = teamRolesMap.get("team1");
		System.out.println(values);

		// [team1=1, team1=2, team1=3, team2=4]
		Collection<Entry<String, Integer>> entries = teamRolesMap.entries();
		System.out.println(entries);

		// [team1 x 3, team2]
		Multiset<String> keyMultiset = teamRolesMap.keys();
		Integer count1 = keyMultiset.count("team1"); // 3
		Integer count2 = keyMultiset.count("team2"); // 3
		System.out.println("count1:" + count1 + ", count2:" + count2);

		// [team1, team2]
		Set<String> keys = teamRolesMap.keySet();
		System.out.println(keys);

		// Put single entry
		teamRolesMap.put("team3", 5);

		// Put entries from another multimap
		Multimap<String, Integer> anotherMultimap = HashMultimap.create();
		anotherMultimap.put("team3", 6);
		anotherMultimap.put("team3", 7);
		teamRolesMap.putAll(anotherMultimap);

		// Put entries with single key and multi values
		teamRolesMap.putAll("team4", Arrays.asList(8, 9, 10));

		// [5, 6, 7, 8, 9, 10, 1, 2, 3, 4]
		teamRolesMap.values();

		// Replaces values with specified key
		// Updated: {team3=[5, 6, 7], team4=[80, 100, 90], team1=[1, 2, 3], team2=[4]}
		teamRolesMap.replaceValues("team4", Arrays.asList(80, 90, 100));

		Assert.assertTrue(teamRolesMap.containsKey("team1"));
		Assert.assertTrue(teamRolesMap.containsValue(5));
		Assert.assertTrue(teamRolesMap.containsValue(4));
		Assert.assertTrue(teamRolesMap.containsEntry("team1", 1));
	}

	/**
	 * Guava提供的Multimap有：
	 * ArrayListMultmap, HashMultimap, LinkedHashMultimap, TreeMultimap, ImmutableSetMultimap
	 */
	@Test
	public void testListMultimap() {
		ArrayListMultimap<String, Integer> teamRolesMap = ArrayListMultimap.create();

		teamRolesMap.put("team1", 1);
		teamRolesMap.put("team1", 2);
		teamRolesMap.put("team1", 3);
		teamRolesMap.put("team2", 4);

		// 对于ArrayListMultimap来说，get返回值是有索引位的数组
		List<Integer> members = teamRolesMap.get("team1");
		System.out.println(members);
	}

	@Test
	public void testAsMapEquality() {
		ArrayListMultimap<String, Integer> teamRolesMap = ArrayListMultimap.create();

		Map<String, Collection<Integer>> map1 = teamRolesMap.asMap();
		Map<String, List<Integer>> map2 = Multimaps.asMap(teamRolesMap);

		// 通过asMap返回的视图与Multimaps工具类返回的视图是相等的（但是他们的泛型类型是不一样的）
		Assert.assertEquals(map1, map2);

	}
}
