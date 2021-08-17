package net.bafeimao.examples;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * Created by ktgu on 15/8/16.
 */
public class SpringDemo1 {

    public static void main(String[] args) throws InterruptedException {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-examples.xml");
        context.close();

        TimeUnit.SECONDS.sleep(3);
      }
}
