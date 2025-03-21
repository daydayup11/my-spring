package org.mini.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.mini.beans.factory.BeanFactoryAware;
import org.mini.beans.factory.FactoryBean;
import org.mini.beans.factory.config.*;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory,BeanDefinitionRegistry{
    protected Map<String, BeanDefinition> beanDefinitionMap=new ConcurrentHashMap<>(256);
    protected List<String> beanDefinitionNames=new ArrayList<>();
	private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    public AbstractBeanFactory() {
    }
    
    public void refresh() {
    	for (String beanName : beanDefinitionNames) {
    		try {
				getBean(beanName);
			} catch (BeansException e) {
				e.printStackTrace();
			}
    	}
    }

    public Object getBean(String beanName) throws BeansException{
        Object singleton = this.getSingleton(beanName);
		//如果一级缓存中不存在
		if (singleton == null) {
			singleton = this.earlySingletonObjects.get(beanName);
			//如果二级缓存中不存在，则创建
			if (singleton == null) {
				System.out.println("get bean null -------------- " + beanName);
				BeanDefinition bd = beanDefinitionMap.get(beanName);
				if (bd != null) {
					singleton=createBean(bd);
					this.registerBean(beanName, singleton);
					if (singleton instanceof BeanFactoryAware) {
						((BeanFactoryAware) singleton).setBeanFactory(this);
					}

					//beanpostprocessor
					//step 1 : postProcessBeforeInitialization
					singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName);

					//step 2 : init-method
					if (bd.getInitMethodName() != null && !bd.getInitMethodName().equals("")) {
						invokeInitMethod(bd, singleton);
					}

					//step 3 : postProcessAfterInitialization
					applyBeanPostProcessorsAfterInitialization(singleton, beanName);

					this.removeSingleton(beanName);
					this.registerBean(beanName, singleton);
				}
				else {
					//如果没有找到
					return null;

				}
			}

		}
		//process Factory Bean
		if (singleton instanceof FactoryBean) {
			System.out.println("factory bean -------------- " + beanName + "----------------"+singleton);
			return this.getObjectForBeanInstance(singleton, beanName);
		}
		else {
			System.out.println("normal bean -------------- " + beanName + "----------------"+singleton);

		}
        return singleton;
    }
	protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
		// Now we have the bean instance, which may be a normal bean or a FactoryBean.
		if (!(beanInstance instanceof FactoryBean)) {
			return beanInstance;
		}

		Object object = null;
		FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
		object = getObjectFromFactoryBean(factory, beanName);
		return object;
	}
    
    private void invokeInitMethod(BeanDefinition bd, Object obj) {
    	Class<?> clz = obj.getClass();
		Method method = null;
		try {
			method = clz.getMethod(bd.getInitMethodName());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		try {
			method.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}    	
    }
    
	@Override
	public boolean containsBean(String name) {
		return containsSingleton(name);
	}

	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);
		
	}

	@Override
	public void registerBeanDefinition(String name, BeanDefinition bd) {
    	this.beanDefinitionMap.put(name,bd);
    	this.beanDefinitionNames.add(name);
    	if (!bd.isLazyInit()) {
        	try {
				getBean(name);
			} catch (BeansException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}

	@Override
	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
		
	}

	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

	@Override
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}

	@Override
	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}

	@Override
	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}

	@Override
	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}
	
	private Object createBean(BeanDefinition bd) {
		Class<?> clz = null;
		Object obj = doCreateBean(bd);
		
		this.earlySingletonObjects.put(bd.getId(), obj);
		
		try {
			clz = Class.forName(bd.getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		populateBean(bd, clz, obj);
		
		return obj;
	}

	private Object doCreateBean(BeanDefinition bd) {
		Class<?> clz = null;
		Object obj = null;
		Constructor<?> con = null;

		try {
    		clz = Class.forName(bd.getClassName());
    		
    		//handle constructor
    		ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
			if (argumentValues != null) {
				if (!argumentValues.isEmpty()) {
					Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
					Object[] paramValues = new Object[argumentValues.getArgumentCount()];
					for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
						ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
						if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
							paramTypes[i] = String.class;
							paramValues[i] = argumentValue.getValue();
						} else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
							paramTypes[i] = Integer.class;
							paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
						} else if ("int".equals(argumentValue.getType())) {
							paramTypes[i] = int.class;
							paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
						} else {
							paramTypes[i] = String.class;
							paramValues[i] = argumentValue.getValue();
						}
					}
					try {
						con = clz.getConstructor(paramTypes);
						obj = con.newInstance(paramValues);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					obj = clz.newInstance();
				}
			}else {
				obj = clz.newInstance();
			}

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());
		
		return obj;

	}
    
	private void populateBean(BeanDefinition bd, Class<?> clz, Object obj) {
		handleProperties(bd, clz, obj);
	}
	
	private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
		//handle properties
		System.out.println("handle properties for bean : " + bd.getId());
		PropertyValues propertyValues = bd.getPropertyValues();
		if (propertyValues != null) {
			if (!propertyValues.isEmpty()) {
				for (int i = 0; i < propertyValues.size(); i++) {
					PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
					String pName = propertyValue.getName();
					String pType = propertyValue.getType();
					Object pValue = propertyValue.getValue();
					boolean isRef = propertyValue.getIsRef();
					Class<?>[] paramTypes = new Class<?>[1];
					Object[] paramValues = new Object[1];
					if (!isRef) {
						if ("String".equals(pType) || "java.lang.String".equals(pType)) {
							paramTypes[0] = String.class;
							paramValues[0] = pValue;
						} else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
							paramTypes[0] = Integer.class;
							paramValues[0] = Integer.valueOf((String)pValue);
						} else if ("int".equals(pType)) {
							paramTypes[0] = int.class;
							paramValues[0] = Integer.valueOf((String)pValue).intValue();
						} else {
							paramTypes[0] = String.class;
							paramValues[0] = pValue;
						}
					} else { //is ref, create the dependent beans
						try {
							paramTypes[0] = Class.forName(pType);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						try {
							paramValues[0] = getBean((String) pValue);

						} catch (BeansException e) {
							e.printStackTrace();
						}
					}

					String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

					Method method = null;
					try {
						method = clz.getMethod(methodName, paramTypes);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					try {
						method.invoke(obj, paramValues);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}


				}
			}
		}else {
			System.out.println("handle properties for bean : " + bd.getId() + " no properties");
		}
		
	}

	abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;

	abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException;

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {

	}

	@Override
	public int getBeanPostProcessorCount() {
		return 0;
	}

	@Override
	public void registerDependentBean(String beanName, String dependentBeanName) {

	}

	@Override
	public String[] getDependentBeans(String beanName) {
		return new String[0];
	}

	@Override
	public String[] getDependenciesForBean(String beanName) {
		return new String[0];
	}
}
