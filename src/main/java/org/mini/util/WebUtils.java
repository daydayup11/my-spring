package org.mini.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class WebUtils {
	public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
		// 创建一个TreeMap，用于存储参数
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			// 如果参数名以指定的前缀开头，则将其添加到TreeMap中
			if (prefix.isEmpty() || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String value = request.getParameter(paramName);

				params.put(unprefixed, value);
			}
		}
		return params;
	}
}