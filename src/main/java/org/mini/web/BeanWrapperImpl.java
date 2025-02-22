package org.mini.web;

import org.mini.beans.AbstractPropertyAccessor;
import org.mini.beans.PropertyEditor;
import org.mini.beans.PropertyEditorRegistrySupport;
import org.mini.beans.factory.config.PropertyValue;
import org.mini.beans.factory.config.PropertyValues;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends AbstractPropertyAccessor {
	Object wrappedObject;
	Class<?> clz;
	PropertyValues pvs;
	
	public BeanWrapperImpl(Object object) {
		registerDefaultEditors();
		this.wrappedObject = object;
		this.clz = object.getClass();
		System.out.println(this.clz);
	}

	public void setBeanInstance(Object object) {
		this.wrappedObject = object;
	}
	public Object getBeanInstance() {
		return this.wrappedObject;
	}
	public void setPropertyValues(PropertyValues pvs) {
		this.pvs = pvs;
		for (PropertyValue pv : this.pvs.getPropertyValues()) {
			setPropertyValue(pv);
		}
	}
	public void setPropertyValue(PropertyValue pv) {
		BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
		PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
		if (pe == null) {
			pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
		}

		pe.setAsText((String) pv.getValue());

		System.out.println("getClass:"+pe.getClass());
		System.out.println("getValue:"+pe.getValue());
		System.out.println("getAsText:"+pe.getAsText());

		propertyHandler.setValue(pe.getValue());
	}
	class BeanPropertyHandler {
		Method writeMethod = null;
		Method readMethod = null;
		//PropertyEditor pe = null;
		Class<?> propertyClz = null;
		
		public Class<?> getPropertyClz() {
			return propertyClz;
		}

		public BeanPropertyHandler(String propertyName) {
			try {
				Field field = clz.getDeclaredField(propertyName);
				propertyClz = field.getType();
				this.writeMethod = clz.getDeclaredMethod("set"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1),propertyClz);
				this.readMethod = clz.getDeclaredMethod("get"+propertyName.substring(0,1).toUpperCase()+propertyName.substring(1));
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		public Object getValue() {
			Object result = null;
			writeMethod.setAccessible(true);
			
			try {
				result =  readMethod.invoke(wrappedObject);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return result;

		}

		public void setValue(Object value) {
				writeMethod.setAccessible(true);
				try {
					//给属性赋值
					writeMethod.invoke(wrappedObject, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		}

	}

}
