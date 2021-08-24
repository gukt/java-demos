package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * HeapPollutionTests class
 *
 * <pre>
 * https://www.baeldung.com/java-varargs
 * https://stackoverflow.com/questions/14231037/java-safevarargs-annotation-does-a-standard-or-best-practice-exist
 * </pre>
 * @author https://github.com/gukt
 */
public class HeapPollutionTests {

    @Test
    void testVarargs() {
        class Foo {

            // 可变参数（varargs）是数组，所以把它当成数组就好了。
            // 每个方法只能定义一个 varargs，且只能是最后一个参数。
            void print(String... args) {
                System.out.println(args.getClass());   // Output: class [Ljava.lang.String;
                for (String arg : args) {
                    System.out.println(arg);
                }
            }
        }

        Foo foo = new Foo();
        foo.print("hello", "world");
        String[] arr = new String[]{"hello", "world"};
        foo.print(arr);
    }

    @Test
    void testHeapPollutionWhenUsingParameterizedVarargType() {
        Assertions.assertThrows(ClassCastException.class, () -> {
            System.out.println(heapPollutionDemo1(Arrays.asList("one", "two"), Collections.emptyList()));
        });

        // varargs 安全使用场景:
        // 1. 不要在“隐式创建的数组”中保存任何数据，heapPollutionDemo1 中，我们对数组的第 0 个元素进行了赋值
        // 2. 不要让“隐式创建的数组”逃逸出方法外部
        // 如果我们确定满足上门的条件，则可以使用 @SafeVarargs 消除 IDE 警告
        //
        // 下面演示是如何产生上面第 #2 条所说的 Heap Pollution
        String[] arr = heapPollutionDemo2("hello", "world");
        System.out.println(Arrays.toString(arr));
    }

    // 对于泛型类型的 varargs，编译器会提示如下警告：
    // Warning: Possible heap pollution from parameterized vararg type
    static String heapPollutionDemo1(List<String>... lists) {
        // 如果我们想给数组的第一个元素赋值
        // 下面是将一个 List<Integer> 赋值给 List<String>[] 数组的第一个元素，但这样的复制会被编译器阻止，因为泛型类型不匹配
        // lists[0] = Collections.singletonList(1);
        //
        // 但是，我们知道，List<String>[] 是可以强制转换为 Object[] 数组的。
        // 所以，我们可以迂回一下，就可以对数组的第一个元素赋值成功了
        Object[] arr = lists; // 语义上有问题，但编译器不会提示警告
        arr[0] = Collections.singletonList(1);  // Heap pollution
        // 然后，你还想从 varargs 参数中返回一点什么，比如下面这样
        // 一切看起来挺好，编译器也没发现任何不妥。但此时会发生 ClassCastException 异常，
        // 因为 lists[0].get(0) 的值为 1，而方法返回值类型是 String
        return lists[0].get(0);
    }

    static <T> T[] toArray(T... elements) {
        System.out.println(elements.getClass()); // Output: class [Ljava.lang.Object;
        // 这里啥都没做，只是将 elements 数组原样返回
        return elements;
    }

    static <T> T[] heapPollutionDemo2(T a, T b) {
        System.out.println(a.getClass());
        // 只是简单调用 toArray 方法，将两个参数值作为数组返回。
        // 看起来都没毛病，但实际上这里会抛出 ClassCastException
        // 解释一下流程，看看都发生了什么：
        // 1. 传入 a, b 到 toArray(..) 中，toArray 内部会隐式创建一个数组
        // 2. toArray 内部，由于运行期擦除泛型类型，所以它创建的数组类型为 Object[]
        // 3. toArray 方法返回这个 Object[] 给调用者。
        // 4. 由于该方法返回类型为 T[], 根据传入参数 a, b 的类型为 String，所以编译器试图将 Object[] 转换为 String[]
        // 所以，就发生了 ClassCastException
        return toArray(a, b);
    }
}
