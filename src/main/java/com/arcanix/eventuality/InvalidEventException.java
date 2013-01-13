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

import java.util.List;

/**
 * Since an invalid event is necessarily due to a programming error,
 * this class extends from {@link RuntimeException}.
 * 
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public class InvalidEventException extends RuntimeException {

	private static final long serialVersionUID = 0;
	
	private final Class<?> event;
	private final List<String> validationErrors;
	private final String message;
	
	public InvalidEventException(final Class<?> event, final List<String> validationErrors) {
		if (event == null) {
			throw new IllegalArgumentException("Event cannot be null");
		}
		if (validationErrors == null) {
			throw new IllegalArgumentException("Validation errors cannot be null");
		}
		if (validationErrors.isEmpty()) {
			throw new IllegalArgumentException("Validation errors cannot be empty");
		}
		
		this.event = event;
		this.validationErrors = validationErrors;
		this.message = buildMessage();
	}
	
	private String buildMessage() {
		StringBuilder messageBuilder = new StringBuilder()
			.append("Event ")
			.append(this.event)
			.append(" is invalid. The following errors were found:");
		
		for (String validationError : this.validationErrors) {
			messageBuilder.append("\n\t* ").append(validationError);
		}
		return messageBuilder.toString();
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
