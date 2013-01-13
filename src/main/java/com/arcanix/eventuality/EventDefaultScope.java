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

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventDefaultScope implements EventScope {

	/** EventDefaultScope singleton instance. */
	public static final EventScope INSTANCE = new EventDefaultScope();
	
	/**
	 * Should be treated as a singleton, cannot instantiate.
	 */
	private EventDefaultScope() {
		super();
	}
	
	@Override
	public <T> EventListenerProvider<T> scope(
			final EventDescriptor<T> eventDescriptor,
			final EventListenerProvider<T> unscoped) {
		
		return unscoped;
	}
	
}
