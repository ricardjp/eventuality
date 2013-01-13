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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 * 
 * @param <T> the type of the event handled by this proxy.
 */
public final class EventProxy<T> implements InvocationHandler {

	private List<EventListenerProvider<T>> providers;
	
	public EventProxy(final List<EventListenerProvider<T>> providers) {
		this.providers = providers;
	}
	
	@Override
	public Object invoke(final Object object, final Method method, final Object[] arguments) throws Throwable {
		for (EventListenerProvider<T> provider : this.providers) {
			method.invoke(provider.get(), arguments);
		}
		return null;
	}
	
}
