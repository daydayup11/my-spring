package org.mini.beans.factory;

import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.support.BeansException;

import java.util.Map;


public interface ListableBeanFactory extends BeanFactory {

	boolean containsBeanDefinition(String beanName);

	int getBeanDefinitionCount();

	String[] getBeanDefinitionNames();

	String[] getBeanNamesForType(Class<?> type);

	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

}