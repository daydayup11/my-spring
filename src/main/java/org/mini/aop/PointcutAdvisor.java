package org.mini.aop;

public interface PointcutAdvisor extends Advisor {
	Pointcut getPointcut();
}