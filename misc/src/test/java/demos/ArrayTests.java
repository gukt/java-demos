package demos;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ArrayTests class
 * <pre>
 * https://stackoverflow.com/questions/12462079/possible-heap-pollution-via-varargs-parameter
 * https://www.techiedelight.com/merge-multiple-arrays-java/
 * https://jaxenter.com/java-performance-tutorial-how-fast-are-the-java-8-streams-118830.html
 * https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
 * https://www.baeldung.com/java-varargs
 * </pre>
 *
 * @author https://github.com/gukt
 * @see <a href="https://github.com/gukt/java-demos/tree/main/reflection">java-demos-reflection</a>
 * @see <a href="https://www.baeldung.com/java-arrays-guide">Arrays in Java: A Reference Guide</a>
 * @see <a href="https://www.baeldung.com/java-concatenate-arrays">Concatenate Two Arrays in Java</a>
 * @see <a href="https://www.baeldung.com/java-generic-array">Creating a Generic Array in Java</a>
 * @see <a href="https://www.baeldung.com/java-array-copy#the-arrays-class">How to Copy an Array in Java</a>
 */
public class ArrayTests {

    @Test
    void test1() {
        int[] arr1 = {1, 2, 3, 4};
        IntStream anotherStream = Arrays.stream(arr1, 1, 3);
    }

    @Test
    void testCreateArray() {
        // 编译期，直接创建，可以以字面量方式创建数组。
        int[] arr11 = new int[2];
        // 字面量方式创建数组
        int[] arr12 = {1, 2};
        Integer[] arr13 = {1, 2};
        Object[] arr14 = {1, 2};
        System.out.printf("%s, %s, %s", Arrays.toString(arr12), Arrays.toString(arr13), Arrays.toString(arr14));

        // 如果类型是运行期决定的，那么就需要借助 java.lang.reflect.Array.newInstance(Class<?> componentType, int length) 创建数组对象
        // 从包名可以看出，用的是反射方式
        int[] arr21 = (int[]) Array.newInstance(int.class, 2);
        boolean[] arr22 = (boolean[]) Array.newInstance(boolean.class, 2);
        Integer[] arr23 = (Integer[]) Array.newInstance(Integer.class, 2);
        Object[] arr24 = (Object[]) Array.newInstance(Object.class, 2);
        System.out.println(Arrays.toString(arr21));
        System.out.println(Arrays.toString(arr22));
        System.out.println(Arrays.toString(arr23));
        System.out.println(Arrays.toString(arr24));

        // 本例提供了一个 newArray 私有方法，用以说明创建“包装类型”数组中可能遇到的问题
        Integer[] arr31 = this.newArray(Integer.class, 2);
        System.out.println(Arrays.toString(arr31));
        // 注意：newArray(..) 方法不能用于返回 primitive 数组，参数也不能是 primitive 数组类型，因为会发生类型转换错误。
        // 以下调用是错误的，因为返回值类型是 Integer[]，Array.newInstance 内部显然是根据参数类型创建了一个 int[]
        // 但是 int[]（实际产生的实例类型） 和 Integer[]（期望返回的类型）是不能转换的
        // 注意：int[] 和 Integer[] 是两个完全不同的类型，不要搞混淆了，另一个容易混淆的地方是：Integer[] 可以赋值给 Number[]，
        // 你可以用 Number[].class.isAssignableFrom(Integer[].class) 方法试试是不是返回 true 。
        // int[] arr3 = this.newArray(int.class,3);
        // int[] 不能转换为 Integer[]， 会报 ClassCastException
        Assertions.assertThrows(ClassCastException.class, () -> {
            Integer[] arr32 = this.newArray(int.class, 3);
            System.out.println(Arrays.toString(arr32));
        });
        // 下面这种调用更不行，因为 newArray 返回的是对象数组，而不是 primitive 数组，所以无论你想怎么强制转换都是徒劳的（编译不过）
        // 简单分析下：newArray 方法内部接受了 int.class 所以创建了一个 int[], 但返回类型是一个对象数组，
        // Java 中 Primitive 数组和对象数组中不能相互转换的，比如 int[] 与 Integer[]； 或 int[] 与 Object[], 这是新手容易混淆的地方。
        // 以下两种尝试都不可行（编译不通过）：
        //      int[] arr33 = this.newArray(int.class, 2); // Error: no instance(s) of type variable(s) T exist so that T[] conforms to int[]
        //      int[] arr33 = (int[]) this.newArray(int.class, 2); // Error: Inconvertible types; cannot cast 'java.lang.Integer[]' to 'int[]'
        // 另一个容易混淆的地方是：Integer[] 可以赋值给 Number[]
        Assertions.assertTrue(Number[].class.isAssignableFrom(Integer[].class));

        // 要想得到 primitive 类型数组，接调用 Array.newInstance(..) 方法
        int[] arr1 = (int[]) Array.newInstance(int.class, 2);
        System.out.println(Arrays.toString(arr1));
    }

    @Test
    void testSystemArrayCopy() {
        // System.arraycopy 方法用以拷贝数组，是一个 native 方法，效率高。
        int[] src = {1, 2, 3, 4};
        int[] dest = new int[2];
        // 从指定的原数组的指定位置开始，复制数组元素，到目标数组的指定位置。
        // 最后一个参数表示要复制多少个元素。最大值是: dest.length - destPos
        //
        System.arraycopy(src, 0, dest, 0, 2);
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            // 最后一个参数值的最大可指定的值是: dest.length - destPos，
            // 如果给大了，会抛出 ArrayIndexOutOfBoundsException，
            // 同时 IDE 也会有相应的警告：Length is always bigger than 'dest.length - destPos' {100}
            System.arraycopy(src, 0, dest, 0, 100);
        });
        // 如果拷贝成功，源数组不受影响，目标数组指定位置开始往后，会被从源数组中拷贝过来的数据填充
        System.out.println(Arrays.toString(src));
        System.out.println(Arrays.toString(dest));

        // 当 src, dest 其中之一不是数组对象，或 src 和 dest 类型不一致时，抛出 ArrayStoreException
        Assertions.assertThrows(ArrayStoreException.class, () -> {
            Object dest1 = new Object();
            // 如果类型不匹配，IDE 也会提示警告：'dest1' is not of an array type
            System.arraycopy(src, 0, dest1, 0, 2);
        });
        // 即使 src 和 dest 一个是 Primitive 数组对象，另一个是其对应的 Wrapper 类型数组对象也视为类型不一致。
        Assertions.assertThrows(ArrayStoreException.class, () -> {
            Integer[] dest2 = new Integer[2];
            // 如果类型不匹配，IDE 也会提示警告：'dest2' is not of an array type
            System.arraycopy(src, 0, dest2, 0, 2);
        });

        /** Arrays.copyOf 提供了很多对 Primitive 数组拷贝的工具方法。内部也是调用 System.arraycopy 实现的，请参考 {@link #testArraysCopyOf()} */
    }

    @Test
    void testArraysCopyOf() {
        // Arrays.copyOf 工具方法用于复制指定的数组到一个新的数组中，返回新数组。
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

        // =====================================================================================
        // Arrays.copyOf 提供了很多对 Primitive 数组拷贝的工具方法。内部也是调用 System.arraycopy 实现的。
        // =====================================================================================
        int[] src = {1, 2, 3, 4};
        int[] dest3 = Arrays.copyOf(src, 2);
        System.out.println(Arrays.toString(dest3));
        /*
         * 另外还提供了对非 Primitive 数组拷贝的方法：
         * {@link Arrays#copyOf(Object[], int)} 和 {@link Arrays#copyOf(Object[], int, Class)}
         * Arrays#copyOf(Object[], int) 内部实现是通过调用 Arrays#copyOf(Object[], int, Class) 的
         * 既然提供了泛型方法，为什么Arrays.copyOf 还提供了对每一个 primitive 数组的重载方法呢，
         * 就是因为 copyOf 方法内部需要构造一个用以返回的数组实例，对于 primitive 数组可以直接 new（比如 new int[newLength]）；
         * 而对象数组需要借助 java.lang.reflect.Array.newInstance 来构造。
         */
        Integer[] src31 = {1, 2};
        Integer[] dest31 = Arrays.copyOf(src31, 2);
        System.out.println(Arrays.toString(dest31));
        // 对于 Arrays#copyOf(Object[], int) 方法，原数组与返回值类型要匹配
        // 如果想以下调用，编译都不会通过（👇🏻 去掉下行注释看看）
        // Integer[] dest32 = Arrays.copyOf(new int[]{1,2}, 2);
        // 想强制转换？对不起，int[] 和 Integer[] 是完全不同的两个概念，不能相互转换（👇🏻 去掉下行注释看看）
        // Integer[] dest32 = (Integer[])Arrays.copyOf(new int[]{1,2}, 2);

        // 下面，重点看看 T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) 的使用场景
        // 通过方法签名可以看出，该方法可以返回一个缩小范围的数组，比如：
        // 如果原数组是 Object[], 可以返回比如 Integer[]
        Integer[] dest41 = Arrays.copyOf(new Object[]{1, 2}, 2, Integer[].class);
        System.out.println(Arrays.toString(dest41));
        // 如果原数组是 Number[], 可以返回比如 Integer[], Long[] 等，因为 Integer[], Long[] 是可以赋值给 Number[] 的
        Assertions.assertTrue(Number[].class.isAssignableFrom(Integer[].class));
        Integer[] dest42 = Arrays.copyOf(new Number[]{1, 2}, 2, Integer[].class);
        System.out.println(Arrays.toString(dest42));

        // 下面的拷贝显然会出错（注意：但是编译能通过，IDE 也不会提示警告），会抛出 ArrayStoreException
        // 因为 Integer 和 Long 并不是继承关系
        Assertions.assertThrows(ArrayStoreException.class, () -> {
            Integer[] dest43 = Arrays.copyOf(new Long[]{1L, 2L}, 2, Integer[].class);
            System.out.println(Arrays.toString(dest43));
        });

        // 总结 'T[] copyOf(T[] original, int newLength)' 和 'T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType)' 的差异：
        // 前者返回值类型必须和 original 相同； 后者可以限定（缩小）数组类型范围。
    }

    @Test
    void testConcatArrays1() {
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
    }

    @Test
    void testConcatArrays2() {
        // 使用一个临时的 List 变量用于保存结果，借助 JDK 1.5 的 Collections.addAll 工具方法，
        // 将需要合并的数组添加进这个临时变量；最后使用 List.toArray 转换为数组输出。
        // 该方法的优点是：简单，容易理解
        // 缺点是：
        // 1. 比较低效，产生了一个 ArrayList 变量，但却没啥用，只是用它在最后调用它的 toArray 方法。
        // 2. 不支持 Primitive 原生类型数组。
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        List<Integer> both = new ArrayList<>(first.length + second.length);
        Collections.addAll(both, first);
        Collections.addAll(both, second);
        // Collections.addAll(both, new int[]{5, 6});
        Integer[] result = both.toArray(new Integer[0]);
        System.out.println(Arrays.toString(result));
    }


    @Test
    void testConcatArrayWithApacheCommons() {
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        System.out.println(Arrays.toString(ArrayUtils.addAll(first, 3, 4)));
        System.out.println(Arrays.toString(ArrayUtils.addAll(first, second)));
    }

    @Test
    void testConcatArrayWithCustomConcat1() {
        Integer[] first = {1, 2};
        Integer[] second = {3, 4};
        System.out.println(Arrays.toString(concatWithArrayCopy(first, second)));
        System.out.println(Arrays.toString(concatWithArrayCopy(first, 4, 5)));
    }

    @Test
    void testConcatArrayWithCustomConcat2() {
        int[] first = {1, 2};
        int[] second = {3, 4};
        System.out.println(Arrays.toString(concatWithCopy2(first, second)));
        // System.out.println(Arrays.toString(concat(first, second, new int[]{4}, new int[]{5})));
        // System.out.println(Arrays.toString(concat(first, new int[]{4, 5})));
    }

    @Test
    public void givenTwoArrays_whenConcatWithCommonsLang_thenGetExpectedResult() {
        // String[] result = ArrayUtils.addAll(strArray1, strArray2);
        // assertThat(result).isEqualTo(expectedStringArray);
        //
        // int[] intResult = ArrayUtils.addAll(intArray1, intArray2);
        // assertThat(intResult).isEqualTo(expectedIntArray);
    }

    @Test
    public void givenTwoStringArrays_whenConcatWithGuava_thenGetExpectedResult() {
        // String[] result = ObjectArrays.concat(strArray1, strArray2, String.class);
        // assertThat(result).isEqualTo(expectedStringArray);
    }

    @Test
    public void givenTwoIntArrays_whenConcatWithGuava_thenGetExpectedResult() {
        // int[] intResult = Ints.concat(intArray1, intArray2);
        // assertThat(intResult).isEqualTo(expectedIntArray);
    }

    // Internal methods

    @SuppressWarnings("unchecked")
    private <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(type, length);
    }

    @SafeVarargs
    static <T> T[] concatWithArrayCopy(T[] a, T... b) {
        T[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    static <T> T[] concatWithStream(T[] array1, T[] array2) {
        return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
                .toArray(size -> (T[]) Array.newInstance(array1.getClass().getComponentType(), size));
    }

    static int[] concatIntArraysWithIntStream(int[] array1, int[] array2) {
        return IntStream.concat(Arrays.stream(array1), Arrays.stream(array2)).toArray();
    }

    static <T> T[] concatWithCollections(T[] a, T[] b) {
        List<T> resultList = new ArrayList<>(a.length + b.length);
        Collections.addAll(resultList, a);
        Collections.addAll(resultList, b);

        @SuppressWarnings("unchecked")
        T[] resultArray = (T[]) Array.newInstance(a.getClass().getComponentType(), 0);
        return resultList.toArray(resultArray);
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    static <T> T concatWithCopy2(T a, T b) {
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

}
