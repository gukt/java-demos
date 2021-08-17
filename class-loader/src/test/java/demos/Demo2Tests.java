package demos;

import demos.demo2.NetworkClassLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URLClassLoader;
import java.util.Arrays;

/**
 * DemoTests class
 *
 * @author https://github.com/gukt
 */
public class Demo2Tests {

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
