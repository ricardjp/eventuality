package com.arcanix.eventuality.internal;

import com.arcanix.eventuality.EventScope;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public interface ScopeBuilder<T> {

	void in(EventScope eventScope);
	
}
