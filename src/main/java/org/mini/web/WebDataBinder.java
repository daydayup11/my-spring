package org.mini.web;

import org.mini.beans.AbstractPropertyAccessor;
import org.mini.beans.PropertyEditor;
import org.mini.beans.factory.config.PropertyValues;
import org.mini.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebDataBinder {
	private Object target;
	private Class<?> clz;
	private String objectName;
	AbstractPropertyAccessor propertyAccessor;
	
	public WebDataBinder(Object target) {
		this(target,"");
	}
	public WebDataBinder(Object target, String targetName) {
		this.target = target;
		this.objectName = targetName;
		this.clz = this.target.getClass();
		this.propertyAccessor = new BeanWrapperImpl(this.target);
	}
	
	public void bind(HttpServletRequest request) {
		PropertyValues mpvs = assignParameters(request);
		addBindValues(mpvs, request);
		doBind(mpvs);
	}
	
	private void doBind(PropertyValues mpvs) {
		applyPropertyValues(mpvs);
	}

	protected AbstractPropertyAccessor getPropertyAccessor() {
		return this.propertyAccessor;
	}
	protected void applyPropertyValues(PropertyValues mpvs) {
		getPropertyAccessor().setPropertyValues(mpvs);
	}
	public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
		getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
	}
	
	private PropertyValues assignParameters(HttpServletRequest request) {
		Map<String,Object> map = WebUtils.getParametersStartingWith(request, "");
		return new PropertyValues(map);
	}
	
	protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {
	}

}
