package com.arcanix.eventuality;

import com.arcanix.eventuality.EventDispatcherTest.ThreadStartEvent;
import com.arcanix.eventuality.EventDispatcherTest.ThreadSubscriber;
import com.arcanix.eventuality.internal.EventBindingType;
import com.arcanix.eventuality.internal.EventBuilder;
import com.arcanix.eventuality.internal.EventCollector;
import com.arcanix.eventuality.internal.EventType;
import com.arcanix.eventuality.internal.GroupBuilder;
import com.arcanix.eventuality.internal.InstanceBuilder;
import com.arcanix.eventuality.internal.ListenerBuilder;
import com.arcanix.eventuality.internal.GroupBuilderImpl;
import com.arcanix.eventuality.internal.InstanceBuilderImpl;
import com.arcanix.eventuality.internal.ListenerBuilderImpl;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Events {
	
	private final EventCollector collector;
	
	public Events(final EventCollector collector) {
		if (collector == null) {
			throw new IllegalArgumentException("Collector cannot be null");
		}
		this.collector = collector;
	}
	
	public <T> ListenerBuilder<T> on(final Class<T> event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		EventBuilder<T> builder = new EventBuilder<>();
		builder.setEvent(event);
		builder.setEventType(EventType.LISTENER);
		this.collector.addBuilder(builder);
		return new ListenerBuilderImpl<>(builder);
	}
	
	public <T> GroupBuilder<T> subscribe(Class<T> subscriber) {
		if (subscriber == null) {
			throw new IllegalArgumentException("Subscriber cannot be null");
		}
		EventBuilder<T> builder = new EventBuilder<>();
		builder.setEventType(EventType.SUBSCRIBER);
		builder.setEventBindingType(EventBindingType.CLASS);
		builder.setListenerClass(subscriber);
		this.collector.addBuilder(builder);
		return new GroupBuilderImpl<T>(builder);		
	}
	
	public <T> InstanceBuilder<T> subscribe(final T subscriber) {
		if (subscriber == null) {
			throw new IllegalArgumentException("Subscriber cannot be null");
		}
		EventBuilder<T> builder = new EventBuilder<>();
		builder.setEventType(EventType.SUBSCRIBER);
		builder.setEventBindingType(EventBindingType.INSTANCE);
		builder.setListenerInstance(subscriber);
		this.collector.addBuilder(builder);
		return new InstanceBuilderImpl<T>(builder);
	}
	
}
