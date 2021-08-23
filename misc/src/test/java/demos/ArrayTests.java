package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * ArrayTests class
 *
 * @author https://github.com/gukt
 */
public class ArrayTests {

    @Test
    void testArraysCopyOf() {
        // Arrays.copyOf 工具方法用于复制指定的数组到一个新的数组中。
        Object[] arr1 = {1, 2};
        // 指定的 newLength 大于原数组长度，则使用空值填充（此处因为是 Object[]，所以用 null 填充）
        // Arrays.copyOf 还对每个 primitive 类型的数组提供了一个重载方法。根据数组类型不同，空值会不同。
        Object[] arr2 = Arrays.copyOf(arr1, 3);
        // Output: [1, 2, null]
        System.out.println(Arrays.toString(arr2));

        // 指定的 newLength 小于原数组长度，截断。
        Object[] arr3 = Arrays.copyOf(arr1, 1);
        // Output: [1]
        System.out.println(Arrays.toString(arr3));

        // copyOf 总是返回一个新的数组对象
        Object[] arr4 = Arrays.copyOf(arr1, arr1.length);
        Assertions.assertNotEquals(arr1, arr4);
        Assertions.assertArrayEquals(arr1, arr4);

        // TODO: 2021/8/24 测试 copyOf(U[] original, int newLength, Class<? extends T[]> newType)
        // TODO: 2021/8/24 https://www.baeldung.com/java-array-copy#the-arrays-class
        // TODO: 2021/8/24 https://www.baeldung.com/java-generic-array
        // TODO: 2021/8/24 https://www.baeldung.com/java-concatenate-arrays
        // TODO: 2021/8/24 https://stackoverflow.com/questions/12462079/possible-heap-pollution-via-varargs-parameter
        // TODO: 2021/8/24 https://www.techiedelight.com/merge-multiple-arrays-java/
        // TODO: 2021/8/24 https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html
        // TODO: 2021/8/24 https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
    }
}
