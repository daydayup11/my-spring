package org.mini.web.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingRegistry {
    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String,Object> mappingObjs = new HashMap<>();
    private Map<String,Method> mappingMethods = new HashMap<>();
    
	public List<String> getUrlMappingNames() {
		return urlMappingNames;
	}
	public void setUrlMappingNames(List<String> urlMappingNames) {
		this.urlMappingNames = urlMappingNames;
	}
	public Map<String,Object> getMappingObjs() {
		return mappingObjs;
	}
	public void setMappingObjs(Map<String,Object> mappingObjs) {
		this.mappingObjs = mappingObjs;
	}
	public Map<String,Method> getMappingMethods() {
		return mappingMethods;
	}
	public void setMappingMethods(Map<String,Method> mappingMethods) {
		this.mappingMethods = mappingMethods;
	}

}
