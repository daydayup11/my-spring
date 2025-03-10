package org.mini.aop;


import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.BeanFactoryAware;
import org.mini.beans.factory.FactoryBean;
import org.mini.beans.factory.support.BeansException;
import org.mini.util.ClassUtils;

public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {
	private BeanFactory beanFactory;
	private AopProxyFactory aopProxyFactory;
	private String interceptorName;
	private String targetName;
	private Object target;
	private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();
	private Object singletonInstance;
	private PointcutAdvisor advisor;

	public ProxyFactoryBean() {
		this.aopProxyFactory = new DefaultAopProxyFactory();

	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
		this.aopProxyFactory = aopProxyFactory;
	}
	public AopProxyFactory getAopProxyFactory() {
		return this.aopProxyFactory;
	}

	public void setInterceptorName(String interceptorName) {
		this.interceptorName = interceptorName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}

	@Override
	public Object getObject() throws Exception {
		initializeAdvisor();
		return getSingletonInstance();
	}

	private synchronized void initializeAdvisor() {
		Object advice = null;
		try {
			advice = this.beanFactory.getBean(this.interceptorName);
		} catch (BeansException e) {
			e.printStackTrace();
		}

		this.advisor = (PointcutAdvisor) advice;

	}

	private synchronized Object getSingletonInstance() {
		if (this.singletonInstance == null) {
			this.singletonInstance = getProxy(createAopProxy());
		}
		return this.singletonInstance;
	}
	protected AopProxy createAopProxy() {
		return getAopProxyFactory().createAopProxy(target,this.advisor);
	}
	protected Object getProxy(AopProxy aopProxy) {
		return aopProxy.getProxy();
	}

	@Override
	public Class<?> getObjectType() {
		return null;
	}

}
