package demos;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * PerformanceTests class
 *
 * @author https://github.com/gukt
 */
public class PerformanceTests {

    static int nLoop = 10000000;

    /**
     * Native invoke
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void test1() throws ReflectiveOperationException {
        Object foo = new Object();
        long t0 = System.currentTimeMillis();
        // 直接调用
        for (int i = 0; i < nLoop; i++) {
            foo.toString();
        }
        long t1 = System.currentTimeMillis();
        System.out.println("Invoke1: " + (t1 - t0));

        // 内部获取 Method
        for (int i = 0; i < nLoop; i++) {
            Method m = foo.getClass().getMethod("toString");
            m.invoke(foo);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("Invoke2: " + (t2 - t1));

        // 外部获取 Method
        Method m = foo.getClass().getMethod("toString");
        for (int i = 0; i < nLoop; i++) {
            m.invoke(foo);
        }
        long t3 = System.currentTimeMillis();
        System.out.println("Invoke3: " + (t3 - t2));
    }
}

