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

import java.util.Set;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 *
 * @param <T> type of the underlying event listener
 */
public final class EventDescriptor<T> {

	private final Set<Class<? extends T>> eventClasses;
	private final EventScope eventScope;
	private final EventListenerProvider<T> unscoped;
	
	public EventDescriptor(
			final Set<Class<? extends T>> eventClasses,
			final EventListenerProvider<T> unscoped) {
		
		this(eventClasses, unscoped, EventDefaultScope.INSTANCE);
	}
	
	public EventDescriptor(
			final Set<Class<? extends T>> eventClasses,
			final EventListenerProvider<T> unscoped,
			final EventScope eventScope) {
		
		this.eventClasses = eventClasses;
		this.unscoped = unscoped;
		this.eventScope = eventScope;
	}
	
	public Set<Class<? extends T>> getEventClasses() {
		return this.eventClasses;
	}
	
	public EventListenerProvider<T> getUnscoped() {
		return this.unscoped;
	}
	
	public EventScope getEventScope() {
		return eventScope;
	}
	
	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof EventDescriptor)) {
			return false;
		}
		
		final EventDescriptor<?> otherCasted = (EventDescriptor<?>) other;
		return this.eventClasses.equals(otherCasted.getEventClasses())
				&& this.eventScope.equals(otherCasted.getEventScope());
	}
	
}
