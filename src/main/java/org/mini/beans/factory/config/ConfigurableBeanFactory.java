package org.mini.beans.factory.config;

import org.mini.beans.factory.support.SingletonBeanRegistry;
import org.mini.beans.BeanFactory;

/**
 * 作用：可配置的BeanFactory，可以添加BeanPostProcessor，可以注册依赖关系
 * 设计模式：模板方法模式
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";

	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	int getBeanPostProcessorCount();

	void registerDependentBean(String beanName, String dependentBeanName);

	String[] getDependentBeans(String beanName);

	String[] getDependenciesForBean(String beanName);

}
