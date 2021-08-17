package net.bafeimao.examples.spring.beanPostProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ktgu on 15/8/17.
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String path = Main.class.getResource(".").getPath();

        logger.info("path:{}", path);

        AbstractApplicationContext context = new ClassPathXmlApplicationContext(path + "context.xml");
        context.close();
    }
}
