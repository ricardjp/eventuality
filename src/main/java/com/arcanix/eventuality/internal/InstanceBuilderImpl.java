package com.arcanix.eventuality.internal;


/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 * @param <T> the type of the subscriber to add.
 */
public final class InstanceBuilderImpl<T> implements InstanceBuilder<T> {

	private final EventBuilder<T> builder;
	
	public InstanceBuilderImpl(final EventBuilder<T> builder) {
		this.builder = builder;
	}
	
	@Override
	public void groupedBy(final Class<?> group) {
		this.builder.setGroup(group);
	}
	
}
