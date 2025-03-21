package org.mini.web.context.support;

import org.mini.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.mini.beans.factory.config.BeanDefinition;
import org.mini.beans.factory.config.BeanFactoryPostProcessor;
import org.mini.beans.factory.config.ConfigurableListableBeanFactory;
import org.mini.beans.factory.support.BeansException;
import org.mini.beans.factory.support.DefaultListableBeanFactory;
import org.mini.context.*;
import org.mini.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//加载controller
public class AnnotationConfigWebApplicationContext
		extends AbstractApplicationContext implements WebApplicationContext {
	private WebApplicationContext parentApplicationContext;
	private ServletContext servletContext;
	DefaultListableBeanFactory beanFactory;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
			new ArrayList<BeanFactoryPostProcessor>();

	public AnnotationConfigWebApplicationContext(String fileName) {
		this(fileName, null);
	}

	public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
		this.parentApplicationContext = parentApplicationContext;
		this.servletContext = this.parentApplicationContext.getServletContext();
		URL xmlPath = null;
		try {
			xmlPath = this.getServletContext().getResource(fileName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
		List<String> controllerNames = scanPackages(packageNames);
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		this.beanFactory = bf;
		this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
		loadBeanDefinitions(controllerNames);

		if (true) {
			try {
				refresh();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (BeansException e) {
				e.printStackTrace();
			}
		}

	}

	public void loadBeanDefinitions(List<String> controllerNames) {
		for (String controller : controllerNames) {
			String beanID=controller;
			String beanClassName=controller;

			BeanDefinition beanDefinition=new BeanDefinition(beanID,beanClassName);

			this.beanFactory.registerBeanDefinition(beanID,beanDefinition);
		}
	}


	private List<String> scanPackages(List<String> packages) {
		List<String> tempControllerNames = new ArrayList<>();
		for (String packageName : packages) {
			tempControllerNames.addAll(scanPackage(packageName));
		}
		return tempControllerNames;
	}

	private List<String> scanPackage(String packageName) {
		List<String> tempControllerNames = new ArrayList<>();
		URL url = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/"));
//		URL url  =this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		for (File file : dir.listFiles()) {
			if(file.isDirectory()){
				scanPackage(packageName+"."+file.getName());
			}else{
				String controllerName = packageName +"." +file.getName().replace(".class", "");
				tempControllerNames.add(controllerName);
			}
		}
		return tempControllerNames;
	}


	public void setParent(WebApplicationContext parentApplicationContext) {
		this.parentApplicationContext = parentApplicationContext;
		this.beanFactory.setParent(this.parentApplicationContext.getBeanFactory());
	}

	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void publishEvent(ApplicationEvent event) {
		this.getApplicationEventPublisher().publishEvent(event);
	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.getApplicationEventPublisher().addApplicationListener(listener);
	}

	@Override
	public void registerListeners() {
		String[] bdNames = this.beanFactory.getBeanDefinitionNames();
		for (String bdName : bdNames) {
			Object bean = null;
			try {
				bean = getBean(bdName);
			} catch (BeansException e1) {
				e1.printStackTrace();
			}

			if (bean instanceof ApplicationListener) {
				this.getApplicationEventPublisher().addApplicationListener((ApplicationListener<?>) bean);
			}
		}
	}

	@Override
	public void initApplicationEventPublisher() {
		ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
		this.setApplicationEventPublisher(aep);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
	}

	@Override
	public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
		this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
	}

	@Override
	public void onRefresh() {
		this.beanFactory.refresh();
	}

	@Override
	public void finishRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
		return this.beanFactory;
	}

}