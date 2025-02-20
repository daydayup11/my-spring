package org.mini.web;

import org.mini.beans.factory.support.BeansException;
import org.mini.web.servlet.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

	private WebApplicationContext webApplicationContext;
	private WebApplicationContext parentApplicationContext;

    private String sContextConfigLocation;
    private List<String> packageNames = new ArrayList<>();
    private Map<String,Object> controllerObjs = new HashMap<>();
    private List<String> controllerNames = new ArrayList<>();
    private Map<String,Class<?>> controllerClasses = new HashMap<>();


	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;

    public DispatcherServlet() {
        super();
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
		System.out.println("DispatcherServlet初始化。。。");
		super.init(config);
		this.parentApplicationContext =
				(WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		sContextConfigLocation = config.getInitParameter("contextConfigLocation");

		URL xmlPath = null;
		try {
			xmlPath = this.getServletContext().getResource(sContextConfigLocation);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

		this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation,this.parentApplicationContext);

		Refresh();
	}

	protected void Refresh() {
		initController();

		initHandlerMappings(this.webApplicationContext);
		initHandlerAdapters(this.webApplicationContext);
		initViewResolvers(this.webApplicationContext);
	}
	protected void initController() {
		this.controllerNames = Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
		for (String controllerName : this.controllerNames) {
			try {
				this.controllerClasses.put(controllerName,Class.forName(controllerName));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				this.controllerObjs.put(controllerName,this.webApplicationContext.getBean(controllerName));
				System.out.println("controller : "+controllerName);
			} catch (BeansException e) {
				e.printStackTrace();
			}
		}

	}
	protected void initHandlerMappings(WebApplicationContext wac) {
		this.handlerMapping = new RequestMappingHandlerMapping(wac);

	}
	protected void initHandlerAdapters(WebApplicationContext wac) {
		this.handlerAdapter = new RequestMappingHandlerAdapter(wac);

	}
	protected void initViewResolvers(WebApplicationContext wac) {

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

		HandlerAdapter ha = this.handlerAdapter;

		ha.handle(processedRequest, response, handlerMethod);
	}
}
