package org.mini.beans.factory.support;

import org.mini.beans.factory.config.AbstractAutowireCapableBeanFactory;
import org.mini.beans.factory.config.BeanDefinition;
import org.mini.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {
    ConfigurableListableBeanFactory parentBeanFctory;

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return Arrays.copyOf(this.beanDefinitionNames.toArray(), this.beanDefinitionNames.size(), String[].class);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();

        for (String beanName : this.beanDefinitionNames) {
            boolean matchFound = false;
            BeanDefinition mbd = this.getBeanDefinition(beanName);
            Class<?> classToMatch = mbd.getClass();
            // 判断是否是type的子类或子接口
            if (type.isAssignableFrom(classToMatch)) {
                matchFound = true;
            }
            else {
                matchFound = false;
            }

            if (matchFound) {
                result.add(beanName);
            }
        }
        return (String[]) result.toArray();    }

    /**
     * 根据类型获取bean
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>(beanNames.length);
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }
        return result;
    }

    public void setParent(ConfigurableListableBeanFactory beanFactory) {
        this.parentBeanFctory = beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException{
        // 1. 尝试从当前 BeanFactory 中获取 Bean
        Object result = super.getBean(beanName);
        if (result != null) {
            return result;
        }

        // 2. 如果当前 BeanFactory 中没有找到，尝试从父 BeanFactory 中获取
        if (this.parentBeanFctory != null) {
                result = this.parentBeanFctory.getBean(beanName);
                return result;
        }

        // 3. 如果当前和父 BeanFactory 中都没有找到，抛出 NoSuchBeanDefinitionException
        throw new BeansException("No bean named '" + beanName + "' found in both current and parent BeanFactory");
    }
}
