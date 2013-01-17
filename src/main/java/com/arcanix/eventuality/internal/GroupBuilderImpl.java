package com.arcanix.eventuality.internal;

import com.arcanix.eventuality.EventScope;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class GroupBuilderImpl<T> implements GroupBuilder<T> {

	private EventBuilder<T> builder = new EventBuilder<>();
	
	public GroupBuilderImpl(final EventBuilder<T> builder) {
		this.builder = builder;
	}
	
	@Override
	public ScopeBuilder<T> groupedBy(final Class<?> group) {
		this.builder.setGroup(group);
		return this;
	}
	
	@Override
	public void in(final EventScope scope) {
		this.builder.setScope(scope);
	}
	
}
