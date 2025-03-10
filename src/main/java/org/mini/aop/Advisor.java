package org.mini.aop;

public interface Advisor {
	MethodInterceptor getMethodInterceptor();
	void setMethodInterceptor(MethodInterceptor methodInterceptor);
}
