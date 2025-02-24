package org.mini.web.method.annotation;

import org.mini.beans.factory.support.BeansException;
import org.mini.context.ApplicationContext;
import org.mini.context.ApplicationContextAware;
import org.mini.web.bind.annotation.RequestMapping;
import org.mini.web.context.WebApplicationContext;
import org.mini.web.servlet.HandlerMapping;
import org.mini.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware {
    WebApplicationContext wac;
    private  MappingRegistry mappingRegistry ;

    public RequestMappingHandlerMapping() {
    }
    protected void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.wac.getBeanDefinitionNames();
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                obj = this.wac.getBean(controllerName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
            Method[] methods = clz.getDeclaredMethods();
            if(methods!=null){
                for(Method method : methods){
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping){
                        String methodName = method.getName();
                        String urlmapping = method.getAnnotation(RequestMapping.class).value();
                        this.mappingRegistry.getUrlMappingNames().add(urlmapping);
                        this.mappingRegistry.getMappingObjs().put(urlmapping, obj);
                        this.mappingRegistry.getMappingMethods().put(urlmapping, method);
                    }
                }
            }
        }

    }


    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        if (this.mappingRegistry == null) { //to do initialization
            this.mappingRegistry = new MappingRegistry();
            initMapping();
        }
        String sPath = request.getServletPath();

        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }

        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        HandlerMethod handlerMethod = new HandlerMethod(method, obj);

        return handlerMethod;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.wac = (WebApplicationContext) applicationContext;
    }
}
