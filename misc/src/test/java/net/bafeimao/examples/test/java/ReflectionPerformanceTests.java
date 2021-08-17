package net.bafeimao.examples.test.java;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/10/21.
 */
public class ReflectionPerformanceTests {

    static final int loop = 10000000;

    /**
     * 测试反射方式调用方法的性能（原生调用方式）
     */
    @Test
    public void test1() {
        Foo foo = new Foo();
        for(int i = 0; i < loop; i++) {
            foo.bar();
        }
    }

    /**
     * 测试反射方式调用方法的性能（Method在循环体内）
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Foo foo = new Foo();

        for(int i = 0; i < loop; i++) {
            Method m = foo.getClass().getDeclaredMethod("bar");
            m.invoke(foo);
        }
    }

    /**
     * 测试反射方式调用方法的性能（将Method查找放到循环体外面的情况）
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void test3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Foo foo = new Foo();

        Method m = foo.getClass().getDeclaredMethod("bar");
        for(int i = 0; i < loop; i++) {
            m.invoke(foo);
        }
    }

    /**
     * 测试反射方式调用方法的性能（原生调用方式）
     */
    @Test
    public void test4() {
        Object object = new Object();
        for(int i = 0; i < loop; i++) {
            object.toString();
        }
    }

    /**
     * 测试反射方式调用方法的性能（Method在循环体内）
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void test5() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object object = new Object();

        for(int i = 0; i < loop; i++) {
            Method m = object.getClass().getDeclaredMethod("toString");
            m.invoke(object);
        }
    }

    /**
     * 测试反射方式调用方法的性能（将Method查找放到循环体外面的情况）
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void test6() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object object = new Object();

        Method m = object.getClass().getDeclaredMethod("toString");
        for(int i = 0; i < loop; i++) {
            m.invoke(object);
        }
    }

    //@Test
    public void test7() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Object object = new Object();
        Class<Object> c = Object.class;

        int loops = 100000;

        long start = System.currentTimeMillis();
        Object s;
        for (int i = 0; i < loops; i++) {
            s = object.toString();
            //System.out.println(s);
        }
        long regularCalls = System.currentTimeMillis() - start;
        java.lang.reflect.Method method = c.getMethod("toString");

        start = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            s = method.invoke(object);
            //System.out.println(s);
        }

        long reflectiveCalls = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            method = c.getMethod("toString");
            s = method.invoke(object);
            //System.out.println(s);
        }

        long reflectiveLookup = System.currentTimeMillis() - start;

        System.out.println(loops + " regular method calls:" + regularCalls
                + " milliseconds.");

        System.out.println(loops + " reflective method calls without lookup:"
                + reflectiveCalls+ " milliseconds.");

        System.out.println(loops + " reflective method calls with lookup:"
                + reflectiveLookup + " milliseconds.");
    }

    class Foo {
        public void bar() {}
    }
}

