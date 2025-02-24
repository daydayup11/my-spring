package org.mini.web.method.annotation;


import org.mini.beans.factory.support.BeansException;
import org.mini.converter.HttpMessageConverter;
import org.mini.web.bind.annotation.ResponseBody;
import org.mini.web.context.WebApplicationContext;
import org.mini.web.bind.support.WebBindingInitializer;
import org.mini.web.bind.WebDataBinder;
import org.mini.web.bind.support.WebDataBinderFactory;
import org.mini.web.servlet.HandlerAdapter;
import org.mini.web.method.HandlerMethod;
import org.mini.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
	private WebBindingInitializer webBindingInitializer ;
	private HttpMessageConverter messageConverter ;
	WebApplicationContext wac;

	public WebBindingInitializer getWebBindingInitializer() {
		return webBindingInitializer;
	}

	public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
		this.webBindingInitializer = webBindingInitializer;
	}

	public HttpMessageConverter getMessageConverter() {
		return messageConverter;
	}
	public void setMessageConverter(HttpMessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return handleInternal(request, response, (HandlerMethod) handler);
	}

	private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
										HandlerMethod handler) {
		ModelAndView mv = null;

		try {
			mv = invokeHandlerMethod(request, response, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;

	}
	protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
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

		ModelAndView mav = null;
		Method invocableMethod = handlerMethod.getMethod();
		Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
		Class<?> returnType = invocableMethod.getReturnType();

		if(invocableMethod.isAnnotationPresent(ResponseBody.class)){
			this.messageConverter.write(returnObj, response);
		}
		else if (returnType == void.class) {

		}
		else {
			if (returnObj instanceof ModelAndView) {
				mav = (ModelAndView)returnObj;
			}
			else if(returnObj instanceof String) {
				String sTarget = (String)returnObj;
				mav = new ModelAndView();
				mav.setViewName(sTarget);
			}
		}
		return mav;
	}


}
