package net.bafeimao.examples.hotswap;

/**
 * Created by Administrator on 2015/10/19.
 */
public class Foo implements  IFoo {
    @Override
    public void say() {
        // 打印了类所有的ClassLoader
        System.out.println("Hello,HotSwap.[version 1][ClassLoader-" + getClass().getClassLoader().getClass() + "]");
    }
}
