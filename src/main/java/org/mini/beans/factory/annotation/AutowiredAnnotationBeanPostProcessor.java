package org.mini.beans.factory.annotation;

import org.mini.beans.factory.config.BeanPostProcessor;
import org.mini.beans.BeanFactory;
import org.mini.beans.factory.support.BeansException;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
	private BeanFactory beanFactory;

	/**
	 * 为bean的属性注入实例
	 * @param bean 对象
	 * @param beanName 对象名称
	 * @return 注入后的对象
	 * @throws BeansException
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		Class<?> clazz = bean.getClass();
		// 获取所有的字段，包括私有字段，但不包括父类字段
		Field[] fields = clazz.getDeclaredFields();
		if(fields!=null){
			// 遍历字段，查找是否有Autowired注解
			for(Field field : fields){
				boolean isAutowired = field.isAnnotationPresent(Autowired.class);
				if(isAutowired){
					System.out.println("find Autowired field " + field.getName());
					String fieldName = field.getName();
					// 根据字段名称获取bean
					Object autowiredObj = this.getBeanFactory().getBean(fieldName);
					try {
						// 设置字段可访问，否则无法注入
						field.setAccessible(true);
						field.set(bean, autowiredObj);
						System.out.println("autowire " + fieldName + " for bean " + beanName);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				}
			}
		}
		
		return result;
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