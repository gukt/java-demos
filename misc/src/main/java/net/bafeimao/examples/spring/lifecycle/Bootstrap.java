package net.bafeimao.examples.spring.lifecycle;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bootstrap {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("springmvc-samples.xml");
		// context.stop();

		// 让JVM关闭时，自动调用context的close方法
		context.registerShutdownHook();
	}
}
