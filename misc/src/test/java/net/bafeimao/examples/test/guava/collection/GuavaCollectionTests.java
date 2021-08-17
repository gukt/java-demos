package net.bafeimao.examples.test.guava.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableSet.Builder;

public class GuavaCollectionTests {
	@SuppressWarnings("deprecation")
	@Test(expected = UnsupportedOperationException.class)
	public void testImmutableSetWithAddOperation() {
		ImmutableSet<String> immutableSet = ImmutableSet.of("RED", "GREEN");
		immutableSet.add("Hello");
	}

	@Test
	public void testImmutableSetWithBuilder() {
		Builder<Integer> builder = ImmutableSet.builder();
		builder.add(1);
		builder.add(2, 3);
		builder.addAll(Arrays.asList(4, 5, 6));
		ImmutableSet<Integer> immutableSet = builder.build();

		Assert.assertEquals(6, immutableSet.size());
	}

	@Test
	public void testSimpleMultiset() {
		List<String> wordList = Arrays.asList("Tom", "Jack", "Tom");
		HashMultiset<String> multiSet = HashMultiset.create();
		multiSet.addAll(wordList);
		Integer count = multiSet.count("Tom");
		Assert.assertTrue(count == 2);
	}

	/**
	 * Multimap常用实现类有：
	 * HashMultimap， LinkedHashMultimap，TreeMultimap, ArrayListMultimap
	 * 
	 * ImmutableListMultimap, ImmutableSetMultimap
	 */
	@Test
	public void testMultimap() {
		// 类似于：Map<String, Set<String>>,但显然这样用更爽
		Multimap<String, String> teamMembersMap = HashMultimap.create();

		teamMembersMap.put("team1", "tom");
		teamMembersMap.put("team2", "jack");
		teamMembersMap.put("team1", "steven");
		teamMembersMap.put("team1", "jerry");
		teamMembersMap.put("team2", "bob");

		Collection<String> members = teamMembersMap.get("team1");
		System.out.println(members); // [tom, steven, jerry]

		ArrayListMultimap<String, String> map2 = ArrayListMultimap.create();
		map2.putAll(teamMembersMap);
		List<String> memberList = map2.get("team1");
		System.out.println(memberList);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBimapWithDuplicatedValue() {
		/**
		 * Bimap要求key，value都唯一，这样才能正反都能查
		 */
		BiMap<String, String> map = HashBiMap.create();
		map.put("tom", "team1");
		map.put("jack", "team1");
	}

	@Test
	public void tes1() {
		// 构造方式1
		ImmutableSet<Integer> set1 = ImmutableSet.copyOf(new Integer[] { 1, 2, 3 });
		ImmutableSet<Integer> set2 = ImmutableSortedSet.of(1, 2);
	}
}
