package demos;

import com.google.common.primitives.Primitives;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * ReflectionTests class
 *
 * @author https://github.com/gukt
 */
public class ReflectionTests {

    @Test
    void testPrimitiveTypes() {
        // 在 java 中， 即使 Primitive 类型也有与之对应的 class 对象
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
    }

    @Test
    void testIsAnonymousClass() {
        // 判断是否是匿名类型
        // bar1 是通过正常类型实例化的，所以不是你们类；而 bar2 是匿名类型
        Bar bar1 = new Bar();
        System.out.println(bar1.getClass()); // Output: class demos.ReflectionTests$Bar
        Assertions.assertFalse(bar1.getClass().isAnonymousClass());
        Bar bar2 = new Bar() {};
        // 通过打印出的 Class 也可以看的出来，匿名类名称都是以数字结尾的。
        System.out.println(bar2.getClass()); // Output: class demos.ReflectionTests$1
        Assertions.assertTrue(bar2.getClass().isAnonymousClass());
        // lambda 表达式表示的变量不是匿名类型
        Runnable task = () -> {
        };
        Assertions.assertFalse(task.getClass().isAnonymousClass());
    }

    @Test
    void testIsLocalClass() {
        // 判断是否是本地类型（local class）
        Assertions.assertFalse(int.class.isLocalClass());
        Assertions.assertFalse(Integer.class.isLocalClass());
        // 此处定义一个类型 Foo，然后检查该类是否为 Local 类型
        class Foo {}
        Assertions.assertTrue(Foo.class.isLocalClass());
    }

    @Test
    void testIsSynthetic() {
        // 判断是否是合成类
        Assertions.assertFalse(Integer.class.isSynthetic());
        // lambda 表达式赋值的变量是一个合成类型
        Runnable task2 = () -> {
        };
        Assertions.assertTrue(task2.getClass().isSynthetic());
    }

    @Test
    void testGetComponentType() {
        // 获取数组的成员类型
        String[] s = new String[0];
        Class<?> cls = s.getClass();
        Class<?> componentType = cls.getComponentType();
        System.out.println(componentType);
    }

    /**
     * 测试方法返回值的泛型参数类型
     */
    @Test
    void testGetTypeArguments() throws NoSuchMethodException, NoSuchFieldException {
        class Person {

            private Map<String, Integer> map1;

            private List<String> names;

            List<String> getNames() {
                return this.names;
            }

            void setNames(List<String> names) {
                this.names = names;
            }
        }

        // 我们经常看到很多文章中提到：Java 泛型是运行期是擦除的，所以无法在运行期获得泛型信息，实际上这种说法不完全正确
        // 在有些情况下是可以获取到泛型信息的，比如：方法的返回值，方法的参数，字段的类型中的泛型信息都可以通过反射获得
        // 获得方法返回值的泛型参数类型
        Method method1 = Person.class.getDeclaredMethod("getNames");
        Type returnType = method1.getGenericReturnType();
        printTypeArgument(returnType);

        // 获得字段的泛型参数类型
        Field field = Person.class.getDeclaredField("map1");
        Type fieldGenericType = field.getGenericType();
        printTypeArgument(fieldGenericType);

        // 获得方法参数的泛型参数类型
        Method method2 = Person.class.getDeclaredMethod("setNames", List.class);
        Type[] genericParameterTypes = method2.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            printTypeArgument(genericParameterType);
        }
    }

    interface Movable<T> {}

    @Test
    void testGetTypeArgumentBySupperClassOrInterface() {
        // 还可以通过 Super class 或 Interface 获得类型参数
        // 子类和父类中都可以写，如：Dog#getTypeArgument、Cat#getTypeArgument2
        // 也可以通过接口获得类型，如下面的 Cat#getTypeArgument3
        abstract class Animal<T> {

            public Class<?> getTypeArgument() {
                Type genericSuperclass = this.getClass().getGenericSuperclass();
                ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                return (Class<?>) typeArguments[0];
            }
        }

        class Dog extends Animal<String> {}
        class Cat extends Animal<Integer> implements Movable<Long> {

            public Class<?> getTypeArgument2() {
                Type genericSuperclass = this.getClass().getGenericSuperclass();
                ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                return (Class<?>) typeArguments[0];
            }

            public Class<?> getTypeArgument3() {
                Type[] genericInterfaces = this.getClass().getGenericInterfaces();
                ParameterizedType parameterizedType = ((ParameterizedType) genericInterfaces[0]);
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                return (Class<?>) typeArguments[0];
            }
        }

        // Output: class java.lang.String
        System.out.println(new Dog().getTypeArgument());

        Cat smellyCat = new Cat();
        // Output: class java.lang.Integer
        System.out.println(smellyCat.getTypeArgument2());
        // Output: class java.lang.Long
        System.out.println(smellyCat.getTypeArgument3());

        // 但是如果一个变量本身也是一个泛型类型，那么就不能通过 Super class 或 Interface 运行时动态获取到类型参数，比如：
        class Bird<T> extends Animal<T> {

            public Class<?> getTypeArgument4() {
                Type genericSuperclass = this.getClass().getGenericSuperclass();
                ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                // 这里会抛出异常：java.lang.ClassCastException:
                // sun.reflect.generics.reflectiveObjects.TypeVariableImpl cannot be cast to java.lang.Class
                // 因为此时 typeArguments[0] 并不是真实的类型，而是 TypeVariableImpl 对象，所以转换出错
                return (Class<?>) typeArguments[0];
            }
        }

        System.out.println(new Bird<String>().getTypeArgument4());
        Assertions.assertThrows(ClassCastException.class, () -> new Bird<String>().getTypeArgument4());
    }

    /**
     * 打印指定类型的泛型类型参数
     *
     * @param type 类型
     */
    private void printTypeArgument(Type type) {
        if (!(type instanceof ParameterizedType)) {
            System.out.println("Type [" + type + "] is not a ParameterizedType");
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        System.out.println("parameterizedType:" + parameterizedType);
        for (Type typeArgument : typeArguments) {
            Class<?> typeArgClass = (Class<?>) typeArgument;
            System.out.println("    typeArgClass = " + typeArgClass);
        }
    }

    // /**
    //  * 测试字段的泛型参数类型
    //  */
    // @Test
    // void testFieldTypeArguments() throws NoSuchFieldException {
    //     Field field = Person.class.getDeclaredField("map1");
    //     Type returnType = field.getGenericType();
    //     if (returnType instanceof ParameterizedType) {
    //         ParameterizedType type = (ParameterizedType) returnType;
    //         Type[] typeArguments = type.getActualTypeArguments();
    //         for (Type typeArgument : typeArguments) {
    //             Class<?> typeArgClass = (Class<?>) typeArgument;
    //             System.out.println("typeArgClass = " + typeArgClass);
    //         }
    //     }
    // }
    //
    // @Test
    // void testMethodArgumentTypeArguments() throws NoSuchMethodException {
    //     Method method = Person.class.getDeclaredMethod("setNames", List.class);
    //     Type[] genericParameterTypes = method.getGenericParameterTypes();
    //     for (Type genericParameterType : genericParameterTypes) {
    //         ParameterizedType type = (ParameterizedType) genericParameterType;
    //         Type[] typeArguments = type.getActualTypeArguments();
    //         for (Type typeArgument : typeArguments) {
    //             Class<?> typeArgClass = (Class<?>) typeArgument;
    //             System.out.println("typeArgClass = " + typeArgClass);
    //         }
    //     }
    // }

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

    @SuppressWarnings("SameParameterValue")
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

    // static class Person {
    //
    //     private Map<String, String> map1;
    //
    //     private List<String> names;
    //
    //     List<String> getNames() {
    //         return this.names;
    //     }
    //
    //     void setNames(List<String> names) {
    //         this.names = names;
    //     }
    // }
}
