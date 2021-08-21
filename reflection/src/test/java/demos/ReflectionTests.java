package demos;

import com.google.common.primitives.Primitives;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;

/**
 * ReflectionTests class
 *
 * @author https://github.com/gukt
 */
public class ReflectionTests {

    @Test
    void testPrimitiveTypes() {
        // 在 java 中， 即使 primitive 类型也有与之对应的 class 对象
        // Primitive 类型一共有以下 9 种
        System.out.println(byte.class);
        System.out.println(short.class);
        System.out.println(char.class);
        System.out.println(int.class);
        System.out.println(long.class);
        System.out.println(double.class);
        System.out.println(float.class);
        System.out.println(boolean.class);
        System.out.println(void.class);

        // 以下借用 Guava 的 Primitives 帮助类测试所有的 Primitive 类型
        for (Class<?> primitiveType : Primitives.allPrimitiveTypes()) {
            System.out.println(primitiveType);
        }
    }

    @Test
    void testArrayReflection() {
        // 通过反射，创建一个 int[]
        // java.lang.reflect.Array 提供了一系列静态方法，动态创建或者访问数组的。
        // Array.newInstance 的第一个参数表示要创建什么类型的数组；第二个参数表示数组的长度
        int[] arr2 = (int[]) Array.newInstance(int.class, 3);
        // 有了数组变量，就可以对其随意设置值了
        arr2[1] = 1;
        Assertions.assertArrayEquals(new int[]{0, 1, 0}, arr2);

        // 也可以通过 java.lang.reflect.Array 提供的 get、set 方法对数组进行反射方式访问
        Array.set(arr2, 2, 2);
        Assertions.assertEquals(2, Array.get(arr2, 2));
    }

    @Test
    void testClassIs() {
        // 有时候，我们需要知道一个 Class 是否代表一个数组类型，可以用 Class 上的实例方法 isArray 来判断
        Assertions.assertFalse(int.class.isArray());
        Assertions.assertTrue(int[].class.isArray());
        // 是否是枚举类型，显然不是
        Assertions.assertFalse(int.class.isEnum());
        // 是否是 primitive 类型，显然是
        Assertions.assertTrue(int.class.isPrimitive());
        // 是否是接口，显然不是
        Assertions.assertFalse(int.class.isInterface());
        // 是否是注解类型，显然不是
        Assertions.assertFalse(int.class.isAnnotation());
        // 判断一个类型是不是成员类（也就是内部类的概念）
        Assertions.assertFalse(int.class.isMemberClass());
        Assertions.assertTrue(Bar.class.isMemberClass());

        // 判断是否是匿名类
        // bar1 是通过正常类型实例化的，所以不是你们类；而 bar2 是匿名类
        // 通过打印出的 Class 也可以看的出来，匿名类名称都是以数字结尾的。
        Bar bar1 = new Bar();
        System.out.println(bar1.getClass()); // Output: class demos.ReflectionTests$Bar
        Assertions.assertFalse(bar1.getClass().isAnonymousClass());
        Bar bar2 = new Bar() {};
        System.out.println(bar2.getClass()); // Output: class demos.ReflectionTests$1
        Assertions.assertTrue(bar2.getClass().isAnonymousClass());

        // 以下是一些比较生僻的判断方法
        // TODO: 2021/8/21

        Assertions.assertFalse(int.class.isLocalClass());
        Assertions.assertFalse(Integer.class.isLocalClass());

        // 此处定义一个类型 Foo，然后检查该类是否为 Local 类
        class Foo {}
        Assertions.assertTrue(Foo.class.isLocalClass());

        // java.lang.Class.isSynthetic(): 如果这个类是一种人工合成的类则返回 true；否则返回false
        //
        Assertions.assertFalse(int.class.isSynthetic());
        Runnable task = () -> {
        };
        Assertions.assertFalse(task.getClass().isAnonymousClass());
        // Assertions.assertFalse(Bar.task.getClass().isAnonymousClass());
        Assertions.assertTrue(task.getClass().isSynthetic());
    }

    @Test
    void testClassForName() throws ClassNotFoundException {
        // Output: int
        System.out.println(int.class);
        // Output: class [I
        // JVM 中，[ 代表数组，I 代表 int 类型
        System.out.println(int[].class);
        // Output: int[]
        System.out.println(int[].class.getSimpleName());

        // Output: class java.lang.String
        System.out.println(String.class);
        // Output: class [Ljava.lang.String;
        System.out.println(String[].class);
        // Output: String[]
        System.out.println(String[].class.getSimpleName());

        // 对于数组类型，该类型输出什么字符串，将这个字符传入 Class.forName(...) 就可以得到对应的数组 Class
        Assertions.assertEquals(int[].class, Class.forName("[I"));
        Assertions.assertEquals(String[].class, Class.forName("[Ljava.lang.String;"));
        // 还有另外一种方法可以得到数组的 Class，就是先通过反射得到一个数组实例，然后在数组实例上调用 getClass() 方法
        // 这么做也蛮有效的，如果你对类名的规则搞的很糊涂的话，可以这么做。
        Assertions.assertEquals(int[].class, Array.newInstance(int.class, 0).getClass());
        Assertions.assertEquals(String[].class, Array.newInstance(String.class, 0).getClass());

        // 但是对于 Primitive 类型，我们是不能通过传入 int 或 I 来得到 Primitive 类型的 Class 的。
        Assertions.assertThrows(ClassNotFoundException.class, () -> Class.forName("int"));
        Assertions.assertThrows(ClassNotFoundException.class, () -> Class.forName("I"));
        // Primitive 类型的 Class 获取就是直接写，比如 int.class, char.class 等。
        // 但是对于有时候需要提供一个公共方法，通过 className 返回对应的 Class，那就只能 if 判断来将 7 种常用的 Primitive 类型都写一遍了。
        Assertions.assertEquals(int.class, getClassByName("int"));

        // Output: void
        System.out.println(void.class);
        // void 没有数组类型
        // System.out.println(void[].class);

        // 原生类型的变量是没有 getClass 方法的，但是原生类型的数组是有 getClass 方法的。
        int n = 1;
        // System.out.println(n.getClass());
        int[] arrInt1 = new int[0];
        // Output: class [I
        System.out.println(arrInt1.getClass());
    }

    private Class<?> getClassByName(String name) throws ClassNotFoundException {
        if ("byte".equalsIgnoreCase(name)) return byte.class;
        if ("short".equalsIgnoreCase(name)) return short.class;
        if ("int".equalsIgnoreCase(name)) return int.class;
        if ("long".equalsIgnoreCase(name)) return long.class;
        if ("double".equalsIgnoreCase(name)) return double.class;
        if ("float".equalsIgnoreCase(name)) return float.class;
        if ("boolean".equalsIgnoreCase(name)) return boolean.class;
        if ("char".equalsIgnoreCase(name)) return char.class;
        if ("void".equalsIgnoreCase(name)) return void.class;
        return Class.forName(name);
    }

    static class Bar {}
}
