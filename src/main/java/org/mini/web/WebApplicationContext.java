package org.mini.web;

import org.mini.context.ApplicationContext;

import javax.servlet.ServletContext;
//在IOC功能上添加了ServletContext，具备ServletContext的功能
public interface WebApplicationContext extends ApplicationContext {
	String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

	ServletContext getServletContext();
	void setServletContext(ServletContext servletContext);
}