package org.mini.context;

import java.util.ArrayList;
import java.util.List;

public class SimpleApplicationEventPublisher implements ApplicationEventPublisher{
	// 监听器集合
	List<ApplicationListener> listeners = new ArrayList<>();

	@Override
	public void publishEvent(ApplicationEvent event) {
		// 遍历监听器集合，调用监听器
		for (ApplicationListener listener : listeners) {
			listener.onApplicationEvent(event);			
		}
	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.listeners.add(listener);
	}


}
