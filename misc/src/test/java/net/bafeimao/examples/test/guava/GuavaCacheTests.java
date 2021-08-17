package net.bafeimao.examples.test.guava;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.bafeimao.examples.test.Person;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.UncheckedExecutionException;

public class GuavaCacheTests {
	static final Logger LOGGER = LoggerFactory.getLogger(GuavaCacheTests.class);

	@Test
	public void testGetAndGetPresent() throws ExecutionException {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				return new Person(key);
			}
		});

		// 该方法不会触发自动加载，如果找不到，返回null
		personCache.getIfPresent(1);
		Assert.assertTrue(personCache.size() == 0);

		// 该方法和上面类似，不同的是这里可以操作多个key
		personCache.getAllPresent(Arrays.asList(1, 3, 5));
		Assert.assertTrue(personCache.size() == 0);

		// 该方法将触发自动加载，但是该方法会抛出一个checked异常ExecutionException（如果自动加载时发生了异常的话）
		personCache.get(1);
		Assert.assertTrue(personCache.size() == 1);

		// 如上所述，该方法只取缓存中存在的条目，也不会出发自动加载，不存在的将不会出现在结果集合中
		ImmutableMap<Integer, Person> persons = personCache.getAllPresent(Arrays.asList(1, 3, 5));
		Assert.assertTrue(persons.size() == 1); // 因为上面的get方法触发了一次自动加载
		Assert.assertTrue(personCache.size() == 1); // 因为所有的get**Present()方法都不触发自动加载，所以仍然为1

		personCache.getAll(Arrays.asList(2, 3));
		Assert.assertTrue(personCache.size() == 3);
	}

	/**
	 * 该测试方法期望获得一个执行异常，该异常时由缓存自动加载器在加载不存在的Cache entry时抛出的checked异常
	 */
	@Test(expected = ExecutionException.class)
	public void testGetWithCheckedException() throws ExecutionException {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				throw new Exception("Cache entry loading error");
			}
		});

		personCache.get(1);
	}

	/**
	 * 该测试方法执行过程中会抛出UncheckedExecutionException,该异常不需要检查
	 * */
	@Test(expected = UncheckedExecutionException.class)
	public void testGetWithUncheckedException() {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				throw new Exception("Cache entry loading error");
			}
		});

		personCache.getUnchecked(1);
	}

	@Test
	public void testGetValueWithValueLoader() throws ExecutionException {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				return new Person(key);
			}
		});

		Person person = personCache.get(1, new Callable<Person>() {
			@Override
			public Person call() throws Exception {
				return new Person(2); // 注意：这里person#id等于2
			}
		});

		Assert.assertEquals(2, person.getId());
	}

	@Test
	public void testCacheAsMap() throws ExecutionException {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				return new Person(key);
			}
		});

		personCache.getAll(Arrays.asList(1, 2));

		ConcurrentMap<Integer, Person> personsMap = personCache.asMap();
		Assert.assertEquals(2, personsMap.size());
	}

	/**
	 * 测试让缓存Entry失效
	 */
	@Test
	public void testInvalidate() {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				return new Person(key);
			}
		});

		personCache.put(1, new Person(1));
		personCache.put(2, new Person(2));
		personCache.put(3, new Person(3));

		// 让指定的key失效
		personCache.invalidate(1);
		Assert.assertEquals(2, personCache.size());

		// 让所有的key都失效
		personCache.invalidateAll();
		Assert.assertEquals(0, personCache.size());
	}

	/**
	 * 测试刷新缓存的条目
	 */
	@Test
	public void testRefreshEntry() {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder()
				// 设置写入后3秒后自动刷新
				.refreshAfterWrite(3, TimeUnit.SECONDS)
				.build(new CacheLoader<Integer, Person>() {
					@Override
					public Person load(Integer key) throws Exception {
						return new Person(key);
					}
				});

		personCache.put(1, new Person(1));
		personCache.put(2, new Person(2));
		personCache.put(3, new Person(3));

		Person person1 = personCache.getIfPresent(1);
		// 刷新指定的key项，这将触发一次自动加载
		personCache.refresh(1);
		Person personRefreshed = personCache.getIfPresent(1);

		// 发生了一次重新加载，所以这里的地址已经发生了改变
		Assert.assertTrue(!person1.equals(personRefreshed));
	}

	@Test
	public void testCleanUpAndInvalidateAll() {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder().build(new CacheLoader<Integer, Person>() {
			@Override
			public Person load(Integer key) throws Exception {
				return new Person(key);
			}
		});

		personCache.put(1, new Person(1));
		personCache.put(2, new Person(2));

		// 执行等待的缓存维护操作,注意：这里不是清空缓存，要清空缓存请使用invalidateAll()
		personCache.cleanUp();
		Assert.assertEquals(2, personCache.size());

		// 执行invalidateAll(),清空缓存
		personCache.invalidateAll();
		Assert.assertEquals(0, personCache.size());
	}

	@Test
	public void testCacheStats() throws ExecutionException {
		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder()
				// 必须要设置打开，否则情况下不开启统计功能（性能考虑）
				.recordStats()
				.build(new CacheLoader<Integer, Person>() {
					@Override
					public Person load(Integer key) throws Exception {
						if (key.equals(4)) {
							throw new Exception("Key#" + key + " was not allowed!");
						}
						return new Person(key);
					}
				});

		try {
			personCache.put(1, new Person(1));
			personCache.put(2, new Person(2));

			personCache.get(3);
			personCache.get(4);

		} catch (Exception ignored) {
		} finally {
			CacheStats cacheStats = personCache.stats();

			// As of get #3, #4
			Assert.assertEquals(2, cacheStats.missCount());

			// 一共调用了两次get操作，执行了两次加载
			Assert.assertEquals(2, cacheStats.loadCount());

			// key=3的项是加载成功的
			Assert.assertEquals(1, cacheStats.loadSuccessCount());

			// key=4的项是故意加载失败的
			Assert.assertEquals(1, cacheStats.loadExceptionCount());
		}
	}

	@Test
	public void testCacheExpiresAndRemovalWithSync() throws ExecutionException, InterruptedException {
		final Thread testThread = Thread.currentThread();

		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder()
				// 指示缓存项在最后访问（包括：创建，更新值）后多长时间后过期
				// 除以下这个方法外，还有一个类似的：.expireAfterWrite(3, TimeUnit.SECONDS);不赘述
				.expireAfterAccess(3, TimeUnit.SECONDS)

				// 添加监听器，用以监听条目被移除
				.removalListener(new RemovalListener<Integer, Person>() {
					@Override
					public void onRemoval(RemovalNotification<Integer, Person> notification) {
						// 该监听器的运行是和测试用例运行在同一个线程中
						Assert.assertEquals(testThread, Thread.currentThread());

						LOGGER.info("Key removing... Key:{}, Cause:{}", notification.getKey(), notification.getCause()
								.toString());
					}
				})
				.build(new CacheLoader<Integer, Person>() {
					@Override
					public Person load(Integer key) throws Exception {
						LOGGER.info("loading entry with key#{}", key);
						return new Person(key);
					}
				});

		// 5秒执行完，其中第7次循环时移除key#0,第9次是移除key#1,key#2,Why?请思考！！！
		for (int i = 0; i < 10; i++) {
			personCache.get(i);
			TimeUnit.MILLISECONDS.sleep(500);
		}

		Assert.assertEquals(7, personCache.size());
	}

	@Test
	public void testRemovalWithAsync() throws ExecutionException, InterruptedException {
		final Thread testThread = Thread.currentThread();

		LoadingCache<Integer, Person> personCache = CacheBuilder.newBuilder()
				.expireAfterAccess(3, TimeUnit.SECONDS)
				// 这里添加一个异步RemovalListener，使监听器运行在和当前线程不同的线程中
				.removalListener(RemovalListeners.asynchronous(new RemovalListener<Integer, Person>() {
					@Override
					public void onRemoval(RemovalNotification<Integer, Person> notification) {
						// 该监听器和测试用例运行在不同的线程中
						Assert.assertNotSame(testThread, Thread.currentThread());

						LOGGER.info("Removing the key # {}", notification.getKey());
					}
				}, Executors.newSingleThreadExecutor()))
				.build(new CacheLoader<Integer, Person>() {
					@Override
					public Person load(Integer key) throws Exception {
						return new Person(key);
					}
				});

		for (int i = 0; i < 10; i++) {
			personCache.get(i);
			TimeUnit.MILLISECONDS.sleep(500);
		}

		Thread.sleep(10 * 1000);

		System.out.println(personCache.size());
	}

	/**
	 * SoftValues就不另外测试了，大体原理相同，指示SoftReference比起WeakReference有更长的生命周期
	 * 
	 * weakKeys的情况也不再另行测试
	 */
	@Test
	public void testWeakValues() {
		Cache<Integer, Person> personCache = CacheBuilder.newBuilder().weakValues().build();

		personCache.put(1, new Person(1, "tom"));
		personCache.put(2, new Person(2, "jack"));

		Assert.assertEquals(2, personCache.size()); // 两条都在

		int i = 0;

		// 这里故意放置一个强引用，如果它一直存在那么缓存值就一直不会被回收
		@SuppressWarnings("unused")
		Person p = personCache.getIfPresent(1);

		// Question:如果将该行往上移动一行会是什么情况？
		System.gc(); // 触发一次GC

		// person#2由于是弱引用值，所以此时应该已回收掉了，
		Assert.assertNull(personCache.getIfPresent(2));

		// 但是person#1有强引用指向它，所以它不会立即回收，直到移除强引用（以下循环10次后移除强引用）
		while (personCache.getIfPresent(1) != null) {
			System.out.println("present");

			// 第十次循环时移除强引用
			if (++i == 10) {
				p = null;
				System.gc(); // 再触发一次GC
			}
		}

		Assert.assertEquals(0, personCache.size());
	}

}
