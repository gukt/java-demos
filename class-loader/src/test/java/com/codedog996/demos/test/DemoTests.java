package com.codedog996.demos.test;

import com.codedog996.demos.demo2.NetworkClassLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * DemoTests class
 *
 * @author https://github.com/gukt
 */
public class DemoTests {

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

    @Test
    void testDemo2() throws ClassNotFoundException {
        // 打印出当前类的 ClassLoader
        // Output：sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(this.getClass().getClassLoader());

        // 可以利用 Web Server for Chrome 浏览器插件，快速启动一个 Web Server 进行测试
        // 插件地址：https://chrome.google.com/webstore/detail/web-server-for-chrome/ofhbbkphhbklhfoeikjpcbhemlocgigb，
        // 只用在插件的启动页面中点击 "Choose Folder"，然后指向 IDE 编译后的 classes 目录即可
        // 需要注意的是：如果要下载的 class 文件（比如本例中 Foo.class）是在当前项目中准备的，
        // 一定要将 IDE 编译到 class path 中的 class 文件（本例是 Foo.class）删除掉，
        // 否则按照 “Parent Delegate Model”， AppClassLoader 会优先加载它，而轮不到 NetWorkClassLoader 加载
        // 因为 NetWorkClassLoader 的 parent 设置为了 null，默认使用 AppClassLoader 作为它的 parent。
        // 也可以留在当前 class path 中，只要将 NetWorkClassLoader 的 parent 设置为 ExtClassLoader,
        // 具体写法是：
        // public NetworkClassLoader(String url) {
        //      super(getSystemClassLoader().getParent());
        //      this.url = url;
        // }
        String url = "http://127.0.0.1:8887";
        String className = "com.codedog996.demos.demo2.Foo";

        // 创建两个不同的 Class Loader 对象
        NetworkClassLoader classLoader1 = new NetworkClassLoader(url);
        NetworkClassLoader classLoader2 = new NetworkClassLoader(url);
        Assertions.assertNotSame(classLoader1, classLoader2);

        // 分别用两个不同的 ClassLoader 实例去加载 Class
        Class<?> type1 = classLoader1.loadClass(className);
        Class<?> type2 = classLoader2.loadClass(className);
        // 不同 ClassLoader 加载的同名 class（哪怕是 .class 内容也完全相同），Class 是不同的。
        Assertions.assertNotSame(type1, type2);

        // 用 classLoader1 再加载一次 class（实际上这次加载操作并不会正在下载 class 了，内部是从缓存里读了）
        Class<?> type3 = classLoader1.loadClass(className);
        // type1 和 type3 是用同一个 ClassLoader 加载的，因此是相同的
        Assertions.assertSame(type1, type3);
    }
}
