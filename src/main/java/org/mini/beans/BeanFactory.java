package org.mini.beans;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    void registerBeanDefinition(BeanDefinition beanDefinition);
}