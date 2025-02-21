package org.mini.web.servlet;

import org.mini.web.servlet.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
	HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
