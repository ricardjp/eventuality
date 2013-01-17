package com.arcanix.eventuality.internal;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class ListenerBuilderImpl<T> implements ListenerBuilder<T> {

	private final EventBuilder<T> builder;
	
	public ListenerBuilderImpl(final EventBuilder<T> builder) {
		this.builder = builder;
	}
	
	@Override
	public GroupBuilder<T> bind(final Class<? extends T> listener) {
		this.builder.setEventBindingType(EventBindingType.CLASS);
		this.builder.setListenerClass(listener);
		return new GroupBuilderImpl<>(this.builder);
	}
	
	@Override
	public InstanceBuilder<T> bind(final T listener) {
		this.builder.setEventBindingType(EventBindingType.INSTANCE);
		this.builder.setListenerInstance(listener);
		return new InstanceBuilderImpl<>(this.builder);
	}
	
}
