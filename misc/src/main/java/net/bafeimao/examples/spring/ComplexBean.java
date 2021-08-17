package net.bafeimao.examples.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by ktgu on 15/8/16.
 */
//@Component
public class ComplexBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor, InitializingBean, DisposableBean {

    static Logger logger = LoggerFactory.getLogger(ComplexBean.class);

    public ComplexBean() {
        logger.info("Default Constructor()...");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        //logger.info("BeanFactoryAware#setBeanFactory()...");
    }

    @Override
    public void setBeanName(String name) {
        // logger.info("BeanNameAware#setBeanName()...");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //logger.info("ApplicationContextAware#setApplicationContext()...");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        logger.info("BeanPostProcessor#postProcessBeforeInitialization(), bean name:{}...", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        logger.info("BeanPostProcessor#postProcessAfterInitialization...");
        return bean;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("InitializingBean#destroy()...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("InitializingBean#afterPropertiesSet()...");
    }

    @PostConstruct
    public void init1() {
        logger.info("@PostConstructor#init()...");
    }

    @PreDestroy
    public void dispose1() {
        logger.info("@PreDestroy#dispose()...");
    }

    public void init() {
        logger.info("Bean Definition#init()...");
    }

    public void dispose() {
        logger.info("Bean Definition#dispose()...");
    }

}
