/**
 * Copyright (C) 2012 Jean-Philippe Ricard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arcanix.eventuality;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import com.arcanix.eventuality.utils.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventDispatcher {

	public interface Default {
		
		// default event group to use when no one is specified
	}
	
	private static final EventValidator EVENT_VALIDATOR = new EventValidator();
	
	private final Instantiator eventListenerInstantiator;
	private final CopyOnWriteArraySet<Class<?>> declaredEvents = new CopyOnWriteArraySet<>();
	private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>,
		CopyOnWriteArrayList<EventDescriptor<?>>>> listeners = new ConcurrentHashMap<>();
	
	public EventDispatcher() {
		this(new DefaultInstantiator());
	}
	
	public EventDispatcher(final Instantiator eventListenerInstantiator) {
		if (eventListenerInstantiator == null) {
			throw new IllegalArgumentException("Event listener instantiator cannot be null");
		}
		this.eventListenerInstantiator = eventListenerInstantiator;
	}
	
	public void declare(final Class<?> event) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		this.declaredEvents.add(validateEvent(event));
	}
	
	public void suppress(final Class<?> event) {
		this.declaredEvents.remove(event);
	}
	
	public <T> void addListener(final Class<T> event, final Class<? extends T> listener) {
		addListener(event, listener, EventDefaultScope.INSTANCE);
	}
	
	public <T> void addListener(final Class<T> event, final Class<? extends T> listener, final EventScope eventScope) {
		addListener(event, listener, eventScope, Default.class);
	}
	
	public <T> void addListener(
			final Class<T> event,
			final Class<? extends T> listener,
			final EventScope eventScope,
			final Class<?> group) {
		
		if (!isDeclared(event)) {
			throw new EventNotDeclaredException(event);
		}
		Set<Class<? extends T>> eventClasses = new HashSet<>();
		eventClasses.add(event);
		EventDescriptor<T> eventDescriptor = new EventDescriptor<T>(eventClasses, new EventListenerProvider<T>() {
			
			@Override
			public T get() {
				return EventDispatcher.this.eventListenerInstantiator.instantiate(listener);
			}

		}, eventScope);
		addListenerInternal(eventDescriptor, group);
	}
	
	public void addListenerInternal(final EventDescriptor<?> eventDescriptor, final Class<?> group) {
		if (eventDescriptor == null) {
			throw new IllegalArgumentException("Event descriptor cannot be null");
		}
		if (group == null) {
			throw new IllegalArgumentException("Group cannot be null");
		}

		for (Class<?> event : eventDescriptor.getEventClasses()) {
			ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<EventDescriptor<?>>> eventsByType =
					this.listeners.get(event);
			
			if (eventsByType == null) {
				ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<EventDescriptor<?>>> newEventsByType =
						new ConcurrentHashMap<>();
						
				eventsByType = this.listeners.putIfAbsent(event, newEventsByType);
				if (eventsByType == null) {
					eventsByType = newEventsByType;
				}
			}
	
			CopyOnWriteArrayList<EventDescriptor<?>> eventsByGroup = eventsByType.get(group);
			if (eventsByGroup == null) {
				CopyOnWriteArrayList<EventDescriptor<?>> newEventsByGroup = new CopyOnWriteArrayList<>();
				eventsByGroup = eventsByType.putIfAbsent(group, newEventsByGroup);
				if (eventsByGroup == null) {
					eventsByGroup = newEventsByGroup;
				}
			}
			eventsByGroup.add(eventDescriptor);
		}
	}
	
	public <T> void addListener(final Class<T> event, final T listener) {
		addListener(event, listener, Default.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void addListener(final Class<T> event, final T listener, final Class<?> group) {
		if (!isDeclared(event)) {
			throw new EventNotDeclaredException(event);
		}
		
		Set<Class<? extends T>> eventClasses = new HashSet<>();
		eventClasses.add((Class<? extends T>) listener.getClass());
		
		EventDescriptor<T> eventDescriptor = new EventDescriptor<T>(eventClasses, new EventListenerProvider<T>() {
			
			@Override
			public T get() {
				return listener;
			}

		});
		
		addListenerInternal(eventDescriptor, group);
	}
	
	public <T> void addSubscriber(final T subscriber) {
		addSubscriber(subscriber, Default.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void addSubscriber(final T subscriber, final Class<?> group) {
		if (subscriber == null) {
			throw new IllegalArgumentException("Subscriber cannot be null");
		}
		if (group == null) {
			throw new IllegalArgumentException("Group cannot be null");
		}
		
		Set<Class<? extends T>> eventClasses = new HashSet<>();
		
		for (Class<?> declaredInterface : ReflectionUtils.getAllInterfaces(subscriber.getClass())) {
			if (isDeclared(declaredInterface)) {
				eventClasses.add((Class<T>) declaredInterface);
			}
		}
		
		EventDescriptor<T> eventDescriptor = new EventDescriptor<T>(eventClasses, new EventListenerProvider<T>() {
			
			@Override
			public T get() {
				return subscriber;
			}

		});
		
		addListenerInternal(eventDescriptor, group);
	}
	
	public <T> void addSubscriber(final Class<T> subscriber) {
		addSubscriber(subscriber, EventDefaultScope.INSTANCE);
	}
	
	public <T> void addSubscriber(final Class<T> subscriber, final EventScope eventScope) {
		addSubscriber(subscriber, eventScope, Default.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> void addSubscriber(final Class<T> subscriber, final EventScope eventScope, final Class<?> group) {
		if (subscriber == null) {
			throw new IllegalArgumentException("Subscriber cannot be null");
		}
		if (group == null) {
			throw new IllegalArgumentException("Group cannot be null");
		}
		
		EventListenerProvider<T> eventListenerProvider = new EventListenerProvider<T>() {
			
			@Override
			public T get() {
				return EventDispatcher.this.eventListenerInstantiator.instantiate(subscriber);
			}

		};
		
		Set<Class<? extends T>> eventClasses = new HashSet<>();
		
		for (Class<?> declaredInterface : ReflectionUtils.getAllInterfaces(subscriber)) {
			if (isDeclared(declaredInterface)) {
				eventClasses.add((Class<? extends T>) declaredInterface);
			}
		}
		
		EventDescriptor<T> eventDescriptor = new EventDescriptor<T>(
				eventClasses, eventListenerProvider, eventScope);
		addListenerInternal(eventDescriptor, group);
	}
	
	public <T> T dispatch(final Class<T> event) {
		return dispatch(event, Default.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T dispatch(final Class<T> event, final Class<?> group) {
		if (!isDeclared(event)) {
			throw new EventNotDeclaredException(event);
		}

		ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<EventDescriptor<?>>> eventsByType =
				this.listeners.get(event);
		
		List<EventDescriptor<?>> eventsByGroup = null;
		
		if (eventsByType != null) {
			eventsByGroup = eventsByType.get(group);
		}
		
		if (eventsByGroup == null) {
			eventsByGroup = Collections.EMPTY_LIST;
		}
		
		List<EventListenerProvider<T>> providers = new ArrayList<>();
		for (EventDescriptor<?> eventDescriptor : eventsByGroup) {
			EventScope scope = eventDescriptor.getEventScope();
			providers.add(scope.scope(
					(EventDescriptor<T>) eventDescriptor,
					(EventListenerProvider<T>) eventDescriptor.getUnscoped()));
		}
		
		return (T) Proxy.newProxyInstance(
				this.getClass().getClassLoader(),
				new Class<?>[] { event },
				new EventProxy<T>(providers));
	}
	
	public boolean isDeclared(final Class<?> event) {
		return this.declaredEvents.contains(event);
	}
	
	private Class<?> validateEvent(final Class<?> event) {
		List<String> validationErrors = EVENT_VALIDATOR.validateEvent(event);
		if (!validationErrors.isEmpty()) {
			throw new InvalidEventException(event, validationErrors);
		}
		return event;
	}
	
}
