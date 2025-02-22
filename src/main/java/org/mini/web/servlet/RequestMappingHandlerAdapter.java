package org.mini.web.servlet;


import org.mini.beans.factory.support.BeansException;
import org.mini.web.WebApplicationContext;
import org.mini.web.WebBindingInitializer;
import org.mini.web.WebDataBinder;
import org.mini.web.WebDataBinderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
	private WebBindingInitializer webBindingInitializer = null;
	WebApplicationContext wac;

	public RequestMappingHandlerAdapter(WebApplicationContext wac) {
		this.wac = wac;
		initWebBindingInitializer();
	}
	private void initWebBindingInitializer() {
		try {
			// 尝试从容器中获取名为 "webBindingInitializer" 的 Bean
			this.webBindingInitializer =(WebBindingInitializer) this.wac.getBean("webBindingInitializer");
		} catch (BeansException ex) {
//			// 如果容器中没有该 Bean，则使用默认值或记录日志
//			this.webBindingInitializer = createDefaultWebBindingInitializer();
			ex.printStackTrace();
		}
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler)
			throws Exception {
		try {
			invokeHandlerMethod(request, response, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void invokeHandlerMethod(HttpServletRequest request,
									   HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
		WebDataBinderFactory binderFactory = new WebDataBinderFactory();
		//获得方法参数
		Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
		//需要进行绑定操作的目标
		Object[] methodParamObjs = new Object[methodParameters.length];

		//根据当前参数类型创建实例。
		//使用WebDataBinderFactory创建WebDataBinder对象。
		//调用WebDataBinder的bind方法，将HTTP请求数据绑定到参数对象。
		//将绑定后的参数对象存入数组。
		int i = 0;
		for (Parameter methodParameter : methodParameters) {
			Object methodParamObj = methodParameter.getType().newInstance();
			WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
			webBindingInitializer.initBinder(wdb);
			wdb.bind(request);
			methodParamObjs[i] = methodParamObj;
			i++;
		}

		Method invocableMethod = handlerMethod.getMethod();
		Object returnobj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);

		response.getWriter().append(returnobj.toString());


	}

}
