package com.arcanix.eventuality.internal;


/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public interface ListenerBuilder<T> {

	InstanceBuilder<T> bind(T listener);
	
	GroupBuilder bind(Class<? extends T> listener);
	
}
