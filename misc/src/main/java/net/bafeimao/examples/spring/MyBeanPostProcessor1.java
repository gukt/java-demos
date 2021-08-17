package net.bafeimao.examples.spring;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor1 implements BeanPostProcessor, Ordered {
    static Logger logger = LoggerFactory.getLogger(MyBeanPostProcessor1.class);

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

    @Override
    public int getOrder() {
        return 1;
    }
}
