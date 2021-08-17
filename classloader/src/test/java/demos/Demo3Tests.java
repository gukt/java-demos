package demos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * Demo3Tests class
 *
 * @author https://github.com/gukt
 */
public class Demo3Tests {

    @Test
    void testGetResource() {
        // 资源文件要放在 resources 目录，如果放在 java 目录中和源代码混在一起，编译会忽略这些非源码文件
        //
        // Class 和 ClassLoader 上都有 getResource 方法
        // Class.getResource 默认相对路径是类所在的包路径；而 ClassLoader.getResource 默认的相对路径是 classpath
        // 比如: 对于 demos/demo3/foo.txt 而言；如果使用
        // file:/Users/ktgu/workspace/projects/900-demos/java-demos/class-loader/out/production/resources/demo3/bar.txt
        URL url11 = getClass().getResource("/demo3/foo.txt");
        // file:/Users/ktgu/workspace/projects/900-demos/java-demos/class-loader/out/production/resources/demo3/bar.txt
        URL url12 = this.getClass().getClassLoader().getResource("demo3/foo.txt");
        Assertions.assertNotNull(url11);
        Assertions.assertNotNull(url12);
        Assertions.assertEquals(url11, url12);

        URL url31 = getClass().getResource("foo.txt");
        URL url32 = getClass().getResource("/demos/demo3/bar.txt");
        System.out.println(url31);
        System.out.println(url32);

        // 返回的是“资源统一定位符”（URL），如果资源不存在，返回 null，而不是抛出异常
        URL url21 = getClass().getResource("not-found");
        URL url22 = getClass().getClassLoader().getResource("not-found");
        Assertions.assertNull(url21);
        Assertions.assertNull(url22);

        // //
        // URL url1 = getClass().getResource("demos/demo3/foo.txt");
        // System.out.println(url1);
        //
        // URL url2 = getClass().getResource("not-exist");
        // Assertions.assertNull(url2);
        //
        // URL fooUrl = getClass().getResource("demos/demo3/foo.txt");
        //
        // URL barUrl1 = getClass().getResource("bar.txt");
        // System.out.println(barUrl1);
        // URL barUrl2 = getClass().getResource("/bar.txt");
        // System.out.println(barUrl2);
        // Assertions.assertEquals(barUrl1, barUrl2);


    }

    @Test
    void testClassPath() {
        // 获得 AppClassLoader 的 classpath
        // 可以通过它的父类的 getURLs() 获取
        System.out.println(Arrays.toString(((URLClassLoader) this.getClass().getClassLoader()).getURLs()));

        // 也可以通过 java.class.path 系统属性获取，为什么是这个环境变量呢，
        // 去看看 AppClassLoader 就知道了，URLClassLoader.getURLs() 的源头就是 java.class.path
        // Output: /Users/ktgu/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.7628.21/IntelliJ IDEA.app/Contents/lib/idea_rt.jar:/Users/ktgu/.m2/repository/org/junit/platform/junit-platform-launcher/1.7.0/junit-platform-launcher-1.7.0.jar:/Users/ktgu/.m2/repository/org/apiguardian/apiguardian-api/1.1.0/apiguardian-api-1.1.0.jar:/Users/ktgu/.m2/repository/org/junit/platform/junit-platform-engine/1.7.0/junit-platform-engine-1.7.0.jar:/Users/ktgu/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/Users/ktgu/.m2/repository/org/junit/platform/junit-platform-commons/1.7.0/junit-platform-commons-1.7.0.jar:/Users/ktgu/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.7628.21/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit5-rt.jar:/Users/ktgu/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/211.7628.21/IntelliJ IDEA.app/Contents/plugins/junit/lib/junit-rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/lib/tools.jar:/Users/ktgu/workspace/projects/900-demos/class-loader/out/test/classes:/Users/ktgu/workspace/projects/900-demos/class-loader/out/production/classes:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.junit.jupiter/junit-jupiter-api/5.7.0/b25f3815c4c1860a73041e733a14a0379d00c4d5/junit-jupiter-api-5.7.0.jar:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.junit.platform/junit-platform-commons/1.7.0/84e309fbf21d857aac079a3c1fffd84284e1114d/junit-platform-commons-1.7.0.jar:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.apiguardian/apiguardian-api/1.1.0/fc9dff4bb36d627bdc553de77e1f17efd790876c/apiguardian-api-1.1.0.jar:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.opentest4j/opentest4j/1.2.0/28c11eb91f9b6d8e200631d46e20a7f407f2a046/opentest4j-1.2.0.jar:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.junit.jupiter/junit-jupiter-engine/5.7.0/d9044d6b45e2232ddd53fa56c15333e43d1749fd/junit-jupiter-engine-5.7.0.jar:/Users/ktgu/.gradle/caches/modules-2/files-2.1/org.junit.platform/junit-platform-engine/1.7.0/eadb73c5074a4ac71061defd00fc176152a4d12c/junit-platform-engine-1.7.0.jar:/Users/ktgu/Library/Caches/JetBrains/IntelliJIdea2021.1/groovyHotSwap/gragent.jar:/Users/ktgu/Library/Caches/JetBrains/IntelliJIdea2021.1/captureAgent/debugger-agent.jar
        // 上面的输出很长，但是能找到这个目录 /Users/ktgu/workspace/projects/900-demos/class-loader/out/production/classes，它是 IDEA 默认的编译目的地
        System.out.println(System.getProperty("java.class.path"));

        // getResource 获取路径的一些用法
        // Output:
        System.out.println(this.getClass().getResource(""));
        // Output:
        System.out.println(this.getClass().getClassLoader().getResource(""));
    }
}
