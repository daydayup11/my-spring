package org.mini.web.servlet.view;


import org.mini.web.servlet.View;
import org.mini.web.servlet.ViewResolver;

public class InternalResourceViewResolver implements ViewResolver {
	private Class<?> viewClass = null;
	private String viewClassName = "";	
	private String prefix = "";
	private String suffix = "";
	private String contentType;
	
	public InternalResourceViewResolver() {
		if (getViewClass() == null) {
			setViewClass(JstlView.class);			
		}
	}
	
	public void setViewClassName(String viewClassName) {
		this.viewClassName = viewClassName;
		Class<?> clz = null;
		try {
			clz = Class.forName(viewClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		setViewClass(clz);
	}
	
	protected String getViewClassName() {
		return this.viewClassName;
	}
	public void setViewClass(Class<?> viewClass) {
		this.viewClass = viewClass;
	}
	protected Class<?> getViewClass() {
		return this.viewClass;
	}
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}
	protected String getPrefix() {
		return this.prefix;
	}
	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}
	protected String getSuffix() {
		return this.suffix;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	protected String getContentType() {
		return this.contentType;
	}

	@Override
	public View resolveViewName(String viewName) throws Exception {
		return buildView(viewName);
	}

	protected View buildView(String viewName) throws Exception {
		//返回构建好的视图对象。
		Class<?> viewClass = getViewClass();
		//创建视图类的实例。
		View view = (View) viewClass.newInstance();
		//设置视图的URL，由前缀、视图名称和后缀组成。
		view.setUrl(getPrefix() + viewName + getSuffix());
		//获取内容类型并设置到视图对象中。
		String contentType = getContentType();
		view.setContentType(contentType);

		return view;
	}

}
