package demos;

import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ArrayConcatTests class
 *
 * <pre>
 * https://www.baeldung.com/java-concatenate-arrays
 * https://www.techiedelight.com/merge-multiple-arrays-java/
 * https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html
 * https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
 * </pre>
 *
 * @author https://github.com/gukt
 */
public class ArrayConcatTests {

    @Test
    void testConcatWithCollections() {
        // 使用一个临时的 List 变量用于保存结果，借助 JDK 1.5 的 Collections.addAll 工具方法，
        // 将需要合并的数组添加进这个临时变量；最后使用 List.toArray 转换为数组输出。
        // 该方法的优点是：简单，容易理解
        // 缺点是：
        // 1. 比较低效，产生了一个 ArrayList 变量，但却没啥用，只是用它在最后调用它的 toArray 方法。
        // 2. 不支持 Primitive 原生类型数组。
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        Integer[] both = concatWithCollections(first, second);
        System.out.println(Arrays.toString(both));
    }

    @Test
    void testConcatWithArrayCopy() {
        // 使用 System.arraycopy 这个 native 方法，合并两个数组，
        // 优点是：
        // 1. 高效，首先它是 native 方法，不借助任何外部工具类，也不会创建多余的中间变量。
        // 2. 适用于所有的 Primitive 原始类型数组
        // NOTE:
        // 1. 应该避免涉及 ArrayLists、streams 等解决方案，因为那些方法需要分配临时内存而没有任何用处。
        // 2. 避免对大型数组进行 for 循环，因为它们效率不高。native 方法使用非常快的块复制功能。
        int[] first = {1, 2};
        int[] second = {3, 4};
        int[] dest = new int[first.length + second.length]; // #1
        System.arraycopy(first, 0, dest, 0, first.length); // #2
        System.arraycopy(second, 0, dest, first.length, second.length);
        System.out.println(Arrays.toString(dest));

        // 上面的写法还有个变体，效果和原理与上面的写法是一模一样的（缺点是：稍稍有点没上面那么直观易理解）
        // 这一句顶上面的 #1 和 #2 的两句
        int[] dest2 = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, dest2, first.length, second.length);
        System.out.println(Arrays.toString(dest2));

        // 以上逻辑封装在 concatWithArrayCopy 和 concatWithArrayCopyPro 里
        Integer[] arr3 = {1, 2};
        Integer[] arr4 = {3, 4};
        // concatWithArrayCopy 只能接受对象数组，不能接受 primitive 数组
        System.out.println(Arrays.toString(concatWithArrayCopy(arr3, arr4)));
        // concatWithArrayCopy(first, second); // error

        // 这个版本可以接受 Primitive 数组
        System.out.println(Arrays.toString(concatWithArrayCopyPro(first, second)));
    }

    @Test
    void testConcatWithApacheCommons() {
        // 使用 Apache Commons 库实现数组合并。
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        System.out.println(Arrays.toString(ArrayUtils.addAll(first, 3, 4)));
        System.out.println(Arrays.toString(ArrayUtils.addAll(first, second)));

        int[] arr3 = {1, 2};
        int[] arr4 = {3, 4};
        // ArrayUtils.addAll 除了提供了对象数组的连接，还提供了所有 primitive 数组的重载方法。
        System.out.println(Arrays.toString(ArrayUtils.addAll(arr3, arr4)));
    }

    @Test
    void testConcatWithGuava() {
        Integer[] arr1 = {1, 2};
        Integer[] arr2 = {3, 4};
        Integer[] result = ObjectArrays.concat(arr1, arr2, Integer.class);
        System.out.println(Arrays.toString(result));

        // 如果要合并 Primitive 数组，分别使用: Ints.concat()、 Booleans.concat() ...
        int[] arr3 = {1, 2};
        int[] arr4 = {3, 4};
        System.out.println(Arrays.toString(Ints.concat(arr3, arr4)));
    }

    @Test
    void testConcatWithStream() {
        Integer[] arr1 = {1, 2};
        Integer[] arr2 = {3, 4};
        int[] arr3 = {1, 2};
        int[] arr4 = {3, 4};
        System.out.println(Arrays.toString(concatWithStream(arr1, arr2)));
        System.out.println(Arrays.toString(concatIntArraysWithIntStream(arr3, arr4)));
    }

    // Methods

    // 使用 Collections.addAll 方法，将参数中的元素先添加到 list 中，
    // 然后再借助 list 的 toArray 方法将 list 转换为对象数组返回
    static <T> T[] concatWithCollections(T[] a, T[] b) {
        List<T> list = new ArrayList<>(a.length + b.length);
        Collections.addAll(list, a);
        Collections.addAll(list, b);

        @SuppressWarnings("unchecked")
        T[] resultArray = (T[]) Array.newInstance(a.getClass().getComponentType(), 0);
        return list.toArray(resultArray);
    }

    // 使用 System.arraycopy 合并数组，仅使用与对象数组（也就是 Non-Primitive Arrays）
    @SafeVarargs
    static <T> T[] concatWithArrayCopy(T[] a, T... b) {
        T[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    // 使用 System.arraycopy 合并数组的高级模式，同时适用于 Primitive 数组
    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    static <T> T concatWithArrayCopyPro(T a, T b) {
        if (!a.getClass().isArray() || !b.getClass().isArray()) {
            throw new IllegalArgumentException("Only arrays are accepted.");
        }
        Class<?> compType1 = a.getClass().getComponentType();
        Class<?> compType2 = b.getClass().getComponentType();
        if (!compType1.equals(compType2)) {
            throw new IllegalArgumentException("Two arrays have different types.");
        }
        int len1 = Array.getLength(a);
        int len2 = Array.getLength(b);

        T result = (T) Array.newInstance(compType1, len1 + len2);
        System.arraycopy(a, 0, result, 0, len1);
        System.arraycopy(b, 0, result, len1, len2);
        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> T[] concatWithStream(T[] a, T[] b) {
        return Stream.concat(Arrays.stream(a), Arrays.stream(b))
                .toArray(size -> (T[]) Array.newInstance(a.getClass().getComponentType(), size));
    }

    static int[] concatIntArraysWithIntStream(int[] a, int[] b) {
        return IntStream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();
    }
}
