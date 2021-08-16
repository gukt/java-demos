package com.codedog996.demos.demo1;

import sun.misc.Launcher;

/**
 * Hello class
 *
 * @author https://github.com/gukt
 */
public class Hello {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Hello World");

        // 你偷偷的在应用程序 class path 下建了包 java.lang，里面放了一个自定义的类 Object
        // 然后你想通过以下语句得到这个类
        Class<?> type = Class.forName("java.lang.Object");
        System.out.println(type);

        System.out.println(Hello.class);

        // ExtClassLoader 和 AppClassLoader 定义在 Launcher 类中，位于 rt.jar 中
        //
        System.out.println(Launcher.getBootstrapClassPath());

        // sun.boot.class.path 定义了 “启动-类路径”，表明 JVM 启动时会先从哪个路径加载
        // 本机的 $JAVA_HOME: /Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
        // 输出: /Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/classes
        System.out.println(System.getProperty("sun.boot.class.path"));
        // 输出: null
        System.out.println(System.getProperty("java.security.manager"));
        // 如果你需要添加或更改 ExtClassLoader 加载路径，可以通过 '-Djava.ext.dirs' 系统属性来设置。
        // 输出: /Users/ktgu/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java
        System.out.println(System.getProperty("java.ext.dirs"));
        // 在 IDEA 中执行，java.class.path 路径一般为：~/build/classes/java/main: ~/build/resources/main 两个
        // 输出: /Users/ktgu/workspace/projects/900-demos/class-loader/build/classes/java/main:/Users/ktgu/workspace/projects/900-demos/class-loader/build/resources/main
        System.out.println(System.getProperty("java.class.path"));

        // Hello.class 是应用程序代码，位于 java.class.path 指定的目录内，它是由 AppClassLoader 加载的。
        // 输出: sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(Hello.class.getClassLoader());
        // AppClassLoader.parent() 是 ExtClassLoader； ExtClassLoader.parent() 是 null。
        // sun.misc.Launcher$ExtClassLoader@3339ad8e
        System.out.println(Hello.class.getClassLoader().getParent());
        // 输出: null
        System.out.println(Hello.class.getClassLoader().getParent().getParent());
        // int.class 和 String.class 是由 Bootstrap ClassLoader 加载的
        // 他们是定义在 rt.jar 中的（rt 的意思是 runtime）
        // 从 sun.boot.class.path 路径下可以找到 rt.jar，也可以说明它是 Bootstrap ClassLoader 加载的。
        // 输出: null
        System.out.println(int.class.getClassLoader());
        System.out.println(String.class.getClassLoader());

        // Bootstrap ClassLoader 是虚拟机的一部分，用 C 写的，它并不是一个 Java 类，无法在 Java 代码中获取对它的引用。
        // 查看源码我们知道，int, Integer, String 等类是定义在 rt.jar 中的，它们是由 BootStrap class loader 加载的。
        // 所以 int.class.getClassLoader() 返回 null
        System.out.println(Launcher.getLauncher());
        System.out.println(Launcher.getBootstrapClassPath());
    }
}
