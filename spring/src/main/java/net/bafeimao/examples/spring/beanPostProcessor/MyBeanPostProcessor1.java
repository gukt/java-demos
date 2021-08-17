package net.bafeimao.examples.spring.beanPostProcessor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

public class MyBeanPostProcessor1 implements BeanPostProcessor, Ordered {
    static Logger logger = LoggerFactory.getLogger(MyBeanPostProcessor1.class);

    // 这里定义逻辑在每个Bean实例化过后都会执行
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("已成功实例化对象：{}", beanName);
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("正在实例化对象：{}...", beanName);
        return bean;
    }

    // 数字越小越排在前面，可以为负值，最大值为Integer.MAX_VALUE
    @Override
    public int getOrder() {
        return 1;
    }
}
