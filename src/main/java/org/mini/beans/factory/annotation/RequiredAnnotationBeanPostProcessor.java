package org.mini.beans.factory.annotation;

import org.mini.beans.factory.config.BeanPostProcessor;
import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.support.BeansException;

public class RequiredAnnotationBeanPostProcessor implements BeanPostProcessor {
	private BeanFactory beanFactory;
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("required生效了");
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}


}