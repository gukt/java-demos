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
    }
}
