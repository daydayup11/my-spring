package org.mini.beans.factory.config;

import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.support.BeansException;

public interface BeanPostProcessor {
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
	
	void setBeanFactory(BeanFactory beanFactory);

}