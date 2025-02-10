package org.mini.context;


import org.mini.beans.factory.ListableBeanFactory;
import org.mini.beans.factory.config.BeanFactoryPostProcessor;
import org.mini.beans.factory.config.BeanPostProcessor;
import org.mini.beans.factory.config.ConfigurableBeanFactory;
import org.mini.beans.factory.config.ConfigurableListableBeanFactory;
import org.mini.beans.factory.support.BeansException;
import org.mini.core.env.Environment;
import org.mini.core.env.EnvironmentCapable;

public interface ApplicationContext
		extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher{
	String getApplicationName();
	long getStartupDate();
	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
	void setEnvironment(Environment environment);
	Environment getEnvironment();
	void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);
	void refresh() throws BeansException, IllegalStateException;
	void close();
	boolean isActive();

}
