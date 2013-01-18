package com.arcanix.eventuality.internal;

import java.util.ArrayList;
import java.util.List;

import com.arcanix.eventuality.EventDispatcher;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventCollector {

	private final List<Class<?>> events = new ArrayList<>();
	private final List<EventBuilder<?>> builders = new ArrayList<>();
	
	public void addEvent(final Class<?> event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		this.events.add(event);
	}
	
	public void addBuilder(final EventBuilder<?> builder) {
		this.builders.add(builder);
	}
	
	public void configureDispatcher(final EventDispatcher dispatcher) {
		
		// declare events
		for (Class<?> event : this.events) {
			dispatcher.declare(event);
		}
		
		// register event listeners
		for (EventBuilder<?> builder : this.builders) {
			configureEvent(dispatcher, builder);
		}
	}
	
	private <T> void configureEvent(final EventDispatcher dispatcher, final EventBuilder<T> builder) {
		if (builder.getEventType() == EventType.LISTENER) {
			if (builder.getEventBindingType() == EventBindingType.CLASS) {
				dispatcher.addListener(builder.getEvent(), builder.getListenerClass(),
						builder.getScope(), builder.getGroup());
			} else {
				dispatcher.addListener(builder.getEvent(), builder.getListenerInstance(), builder.getGroup());
			}
		} else {
			if (builder.getEventBindingType() == EventBindingType.CLASS) {
				dispatcher.addSubscriber(builder.getListenerClass(), builder.getScope(), builder.getGroup());
			} else {
				dispatcher.addSubscriber(builder.getListenerInstance(), builder.getGroup());
			}
		}
	}
	
}
