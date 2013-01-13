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
public class EventListenerInstantiationException extends RuntimeException {

	private static final long serialVersionUID = 0;
	
	/**
	 * Constructs a new event listener instantiation exception with null as its detail message.
	 * @see RuntimeException
	 */
	public EventListenerInstantiationException() {
		super();
	}
	
	/**
	 * Constructs a new event listener instantiation exception with the specified detail message.
	 * @param message the detail message.
	 * @see RuntimeException
	 */
	public EventListenerInstantiationException(final String message) {
		super(message);
	}
	
	/**
	 * Constructs a new runtime exception with the specified cause and a detail message
	 * of (cause==null ? null : cause.toString()).
	 * @param cause the cause.
	 * @see RuntimeException
	 */
	public EventListenerInstantiationException(final Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new event listener instantiation exception with the specified detail message and cause.
	 * @param message the detail message.
	 * @param cause the cause.
	 * @see RuntimeException
	 */
	public EventListenerInstantiationException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
