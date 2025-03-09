package org.mini.beans.factory;

import org.mini.beans.factory.support.BeansException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    boolean containsBean(String name);
    //void registerBean(String beanName, Object obj);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);
}