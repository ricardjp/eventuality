package com.arcanix.eventuality.internal;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public interface GroupBuilder<T> extends ScopeBuilder<T> {
	
	ScopeBuilder<T> groupedBy(Class<?> group);
	
}
