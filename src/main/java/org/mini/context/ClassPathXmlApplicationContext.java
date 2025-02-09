package org.mini.context;

import org.mini.beans.factory.BeanFactory;
import org.mini.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.mini.beans.factory.config.AutowireCapableBeanFactory;
import org.mini.beans.factory.config.BeanPostProcessor;
import org.mini.beans.factory.support.AbstractBeanFactory;
import org.mini.beans.factory.support.BeansException;
import org.mini.beans.factory.support.SimpleBeanFactory;
import org.mini.beans.factory.xml.XmlBeanDefinitionReader;
import org.mini.core.ClassPathXmlResource;
import org.mini.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher {
    AutowireCapableBeanFactory beanFactory;
    private final List<BeanPostProcessor> beanFactoryPostProcessors =
            new ArrayList<BeanPostProcessor>();
    //context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName,boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        AutowireCapableBeanFactory beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh){
            try {
                refresh();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加BeanFactory后置处理器
     * @param postProcessor
     */
    public void addBeanFactoryPostProcessor(BeanPostProcessor postProcessor) { this.beanFactoryPostProcessors.add(postProcessor); }

    /**
     * 启动容器
     * @throws IllegalStateException
     * @throws BeansException
     */
    public void refresh() throws IllegalStateException, BeansException {
        registerBeanPostProcessor(beanFactory);
        beanFactory.refresh();
    }
    private void registerBeanPostProcessor(AutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }
    public void registerBean(String beanName, Object obj) {
        this.beanFactory.registerBean(beanName, obj);
    }

    //context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {

    }

    public List getBeanFactoryPostProcessors() { return this.beanFactoryPostProcessors; }
}
