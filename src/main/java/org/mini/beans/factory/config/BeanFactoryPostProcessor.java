package org.mini.beans.factory.config;

import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.support.BeansException;

public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}