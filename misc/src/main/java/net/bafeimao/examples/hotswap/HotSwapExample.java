package net.bafeimao.examples.hotswap;

import sun.net.spi.nameservice.dns.DNSNameService;

import javax.crypto.Cipher;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/10/19.
 */
public class HotSwapExample {

    public static void main(String[] args) throws Exception {
        HotSwapExample example = new HotSwapExample();

        assert example == null;


        while (true) {
            example.systemLoad();
            example.hotswapLoad();

            TimeUnit.SECONDS.sleep(3);
        }
    }

    // 这里为了测试，每次调用方法的时候都会初始化一个HotSwapClassLoader对象
    // 线上产品只需要在hotswap的时候初始化一个HotSwapClassLoader对象即可
    public void hotswapLoad() throws Exception {
        // 初始化ClassLoader，指定加载根目录及加载的类,这里通过ClassLoader.getSystemResource获取classpath所在的目录(即target\classes目录)
        // 这样我们直接就可以eclipse中直接进行修改Foo，编译->下一次HotSwapClassLoader就直接会加载最新的Foo.class
        HotSwapClassLoader classLoader = new HotSwapClassLoader(ClassLoader.getSystemResource("").toURI());
        classLoader.assignLoadedClazzs(Foo.class.getName());

        // 调用loadClass方法加载类
        Class<?> clazz = classLoader.loadClass(Foo.class.getName());

        // 实例化
        Object foo = clazz.newInstance();
        Method method = foo.getClass().getMethod("say");
        method.invoke(foo);

        // 这样直接调用会抛出java.lang.ClassCastException,因为即使是同一个类文件，如果是由不同的类加载器实例加载的，那么他们的类型就是不同的
        // clazz对象是由HotSwapClassLoader加载的，而foo2的类型生命和转型的Foo类都是由方法所属的类加载（默认是AppClassLoader）加载的，因此是
        // 完全不同的类型，所以会抛出转型异常
        // Foo foo2 = (Foo)clazz.newInstance();
        // foo2.say();

        // 这里通过接口进行调用，并没有抛出ClassCastException.因为HotSwapClassLoader只指定加载了Foo,IFoo接口的加载则会委托给系统类加载器加载
        // 所以转型可成功
        IFoo foo3 = (IFoo) clazz.newInstance();
        foo3.say();
    }

    public void systemLoad() throws Exception {
        System.out.println(Object.class.getClassLoader());
        System.out.println(DNSNameService.class.getClassLoader());
        System.out.println(Foo.class.getClassLoader());

        Foo foo = new Foo();
        foo.say();
    }
}