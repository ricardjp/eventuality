package com.arcanix.eventuality.conf;

import com.arcanix.eventuality.Events;
import com.arcanix.eventuality.internal.GroupBuilder;
import com.arcanix.eventuality.internal.InstanceBuilder;
import com.arcanix.eventuality.internal.ListenerBuilder;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public abstract class AbstractEventualityUnit implements EventualityUnit {

	private Events configureEvents;
	
	@Override
	public final void configure(final Events events) {
		if (this.configureEvents != null) {
			throw new IllegalStateException("Module already configured");
		}
		this.configureEvents = events;
		configure();
	}
	
	protected final void declare(final Class<?> event) {
		this.configureEvents.declare(event);
	}
	
	protected final <T> ListenerBuilder<T> on(final Class<T> event) {
		return this.configureEvents.on(event);
	}
	
	protected final <T> GroupBuilder<T> subscribe(final Class<T> subscriber) {
		return this.configureEvents.subscribe(subscriber);
	}
	
	protected final <T> InstanceBuilder<T> subscribe(final T subscriber) {
		return this.configureEvents.subscribe(subscriber);
	}
	
	protected abstract void configure();
	
	
}
