package org.mini.context;

import org.mini.beans.factory.support.BeansException;

public interface ApplicationContextAware {
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}