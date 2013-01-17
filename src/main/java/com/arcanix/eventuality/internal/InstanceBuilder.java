package com.arcanix.eventuality.internal;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public interface InstanceBuilder<T> {

	void groupedBy(Class<?> group);
	
}
