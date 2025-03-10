package org.mini.aop;

public class DefaultAdvisor implements Advisor{
	private MethodInterceptor methodInterceptor;

	public DefaultAdvisor() {
	}
	
	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}

	@Override
	public Advice getAdvice() {
		return null;
	}

	public MethodInterceptor getMethodInterceptor() {
		return this.methodInterceptor;
	}

}
