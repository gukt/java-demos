package net.bafeimao.examples.test.java;

import org.junit.Assert;
import org.junit.Test;
import sun.misc.Launcher;
import sun.net.spi.nameservice.dns.DNSNameService;

/**
 * Created by Administrator on 2015/10/20.
 */
public class ClassLoaderTests {

    @Test
    public void testClassLoader() throws ClassNotFoundException {
        // 输出null，表明是由bootstrap class loader加载Object类的，因为Object类位于/jre/lib/rt.jar包中
        System.out.println(Object.class.getClassLoader());

        // 输出sun.misc.Launcher$AppClassLoader@7b7035c6
        // 因为Foo类并非位于如"/jre/lib"或"/jre/lib/ext"目录下的jar包中，而是位于应用程序的class path路径下，
        // 因此Foo类由Launcher$AppClassLoader来负责加载
        System.out.println(Foo.class.getClassLoader());

        // 输出sun.misc.Launcher$ExtClassLoader@3da997a，
        // 因为DNSNameService类位于/jre/lib/ext/dnsns.jar中，因此它最终被ExtClassLoader所加载
        System.out.println(DNSNameService.class.getClassLoader());
    }

    @Test
    public void testGetSystemClassLoader() {
        // SystemClassLoader:sun.misc.Launcher$AppClassLoader
        System.out.println(ClassLoader.getSystemClassLoader());
    }

    /**
     * Output:
     * net.bafeimao.examples.test.java.ClassLoaderTests$MyClassLoader@359172db
     * sun.misc.Launcher$AppClassLoader@4921a90
     * sun.misc.Launcher$ExtClassLoader@140de648
     * <p/>
     * NOTE：
     * 1. ExtClassLoader的parent是JVM built-in bootstrap class loader，而bootstrap class loader并非是标准的java类，
     * 因此ExtClassLoader上调用getParent()返回的是null
     **/
    @Test
    public void testGetClassLoaderUpstream() {
        ClassLoader cl = new MyClassLoader();
        while (cl != null) {
            System.out.println(cl);
            cl = cl.getParent();
        }
    }

    @Test
    public void test1() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> fooClass = classLoader.loadClass(Foo.class.getName());

        // parent: null，阻止向上委托
        System.out.println(classLoader.getParent());

        // sun.misc.Launcher$AppClassLoader@4921a90
        System.out.println(Foo.class.getClassLoader());

        // sun.misc.Launcher$AppClassLoader@4921a90
        System.out.println(IFoo.class.getClassLoader());

        // Foo和IFoo是由同一个ClassLoader加载的
        Assert.assertEquals(Foo.class.getClassLoader(), IFoo.class.getClassLoader());
    }

    public void testEquality() throws ClassNotFoundException {
        MyClassLoader classLoader = new MyClassLoader();
        Class<?> fooClass1 = classLoader.loadClass(Foo.class.getName());
    }

    static class MyClassLoader extends ClassLoader {

        public MyClassLoader() {
            super(null); // 设置该ClassLoader的parent为null
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            synchronized (getClassLoadingLock(name)) {
                // 检查该类是否已经被加载过
                Class c = findLoadedClass(name);

                if (c == null) {
                    c = findClass(name);
                }

                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }

    }

    class Foo implements IFoo {
        @Override
        public void bar() {
            System.out.println("i am bar!");
        }
    }

    interface IFoo {
        void bar();
    }
}
