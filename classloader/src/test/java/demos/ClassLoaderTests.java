package demos;

import demos.demo2.Foo;
import org.junit.jupiter.api.Test;
import sun.net.spi.nameservice.dns.DNSNameService;

/**
 * ClassLoaderTests class
 *
 * @author https://github.com/gukt
 */
public class ClassLoaderTests {

    @Test
    void testWhichClassLoaderLoaded() {
        // Output: null
        // 因为 Object 类位于 /jre/lib/rt.jar 包中，它是由 Bootstrap ClassLoader 负责加载
        System.out.println(Object.class.getClassLoader());

        // Output: sun.misc.Launcher$AppClassLoader@7b7035c6
        // 因为 Foo类位于应用程序的 class path 路径下，他是由 Launcher$AppClassLoader 负责加载
        System.out.println(Foo.class.getClassLoader());

        // Output: sun.misc.Launcher$ExtClassLoader@3da997a
        // 因为 DNSNameService 类位于 /jre/lib/ext/dnsns.jar 中，它由 ExtClassLoader 负责加载
        System.out.println(DNSNameService.class.getClassLoader());
    }

    @Test
    public void testWhatIsSystemClassLoader() {
        // Output: sun.misc.Launcher$AppClassLoader@73d16e93
        System.out.println(ClassLoader.getSystemClassLoader());
    }

    /**
     * Output:
     * <pre>
     *  sun.misc.Launcher$AppClassLoader@4921a90
     *  sun.misc.Launcher$ExtClassLoader@140de648
     * </pre>
     * <p/> ExtClassLoader 的 parent 是 JVM 内置的 Bootstrap ClassLoader，它并非是一个标准的 Java 类，所以这里 ExtClassLoader.getParent() 返回 null。
     **/
    @Test
    public void testGetClassLoaderUpstream() {
        class Foo {}
        ClassLoader cl = Foo.class.getClassLoader();
        while (cl != null) {
            System.out.println(cl);
            cl = cl.getParent();
        }
    }
}
