package com.arcanix.eventuality.internal;

import com.arcanix.eventuality.EventDispatcher.Default;
import com.arcanix.eventuality.EventDefaultScope;
import com.arcanix.eventuality.EventScope;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventBuilder<T> {

	private Class<T> event;
	private Class<? extends T> listenerClass;
	private T listenerInstance;
	private EventType eventType;
	private EventBindingType eventBindingType;
	private Class<?> group = Default.class;
	private EventScope scope = EventDefaultScope.INSTANCE;
	
	public Class<T> getEvent() {
		return this.event;
	}
	
	public void setEvent(final Class<T> event) {
		this.event = event;
	}
	
	public Class<? extends T> getListenerClass() {
		return this.listenerClass;
	}
	
	public void setListenerClass(final Class<? extends T> listenerClass) {
		this.listenerClass = listenerClass;
	}
	
	public T getListenerInstance() {
		return this.listenerInstance;
	}
	
	public void setListenerInstance(final T listenerInstance) {
		this.listenerInstance = listenerInstance;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public void setEventType(final EventType eventType) {
		this.eventType = eventType;
	}
	
	public EventBindingType getEventBindingType() {
		return eventBindingType;
	}
	
	public void setEventBindingType(final EventBindingType eventBindingType) {
		this.eventBindingType = eventBindingType;
	}
	
	public Class<?> getGroup() {
		return group;
	}
	
	public void setGroup(final Class<?> group) {
		this.group = group;
	}
	
	public EventScope getScope() {
		return scope;
	}
	
	public void setScope(final EventScope scope) {
		this.scope = scope;
	}
	
}
