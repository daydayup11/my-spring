package org.mini.web;

import org.mini.beans.factory.support.BeansException;
import org.mini.web.context.support.AnnotationConfigWebApplicationContext;
import org.mini.web.context.WebApplicationContext;
import org.mini.web.context.support.XmlScanComponentHelper;
import org.mini.web.method.HandlerMethod;
import org.mini.web.method.annotation.RequestMappingHandlerAdapter;
import org.mini.web.method.annotation.RequestMappingHandlerMapping;
import org.mini.web.servlet.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
	public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
	public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
	public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
	public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
	public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
	public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
	public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
	private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
	private static final Properties defaultStrategies = null;

	private WebApplicationContext webApplicationContext;
	private WebApplicationContext parentApplicationContext;

	private String sContextConfigLocation;

	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;
	private ViewResolver viewResolver;

    public DispatcherServlet() {
        super();
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
		System.out.println("DispatcherServlet init");
		this.parentApplicationContext =
				(WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

		sContextConfigLocation = config.getInitParameter("contextConfigLocation");

		this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation,this.parentApplicationContext);

		Refresh();
	}

	protected void Refresh() {
		initHandlerMappings(this.webApplicationContext);
		initHandlerAdapters(this.webApplicationContext);
		initViewResolvers(this.webApplicationContext);
	}

	protected void initHandlerMappings(WebApplicationContext wac) {
		try {
			this.handlerMapping = (HandlerMapping) wac.getBean(HANDLER_MAPPING_BEAN_NAME);
		} catch (BeansException e) {
			e.printStackTrace();
		}

	}
	protected void initHandlerAdapters(WebApplicationContext wac) {
		try {
			this.handlerAdapter = (HandlerAdapter) wac.getBean(HANDLER_ADAPTER_BEAN_NAME);
		} catch (BeansException e) {
			e.printStackTrace();
		}

	}
	protected void initViewResolvers(WebApplicationContext wac) {
		try {
			this.viewResolver = (ViewResolver) wac.getBean(VIEW_RESOLVER_BEAN_NAME);
		} catch (BeansException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);

		try {
			doDispatch(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
	}

	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpServletRequest processedRequest = request;
		HandlerMethod handlerMethod = null;

		handlerMethod = this.handlerMapping.getHandler(processedRequest);
		if (handlerMethod == null) {
			return;
		}

		ModelAndView mv = null;
		HandlerAdapter ha = this.handlerAdapter;
		mv = ha.handle(processedRequest, response, handlerMethod);
		render(processedRequest, response, mv);
	}
	protected void render( HttpServletRequest request, HttpServletResponse response,ModelAndView mv) throws Exception {
		if (mv == null) {
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}
		// 获取View
		String sTarget = mv.getViewName();
		Map<String, Object> modelMap = mv.getModel();
		View view = resolveViewName(sTarget, modelMap, request);
		view.render(modelMap, request, response);

	}

	protected View resolveViewName(String viewName, Map<String, Object> model,
								   HttpServletRequest request) throws Exception {
		if (this.viewResolver != null) {
			View view = viewResolver.resolveViewName(viewName);
			if (view != null) {
				return view;
			}
		}
		return null;
	}
}
