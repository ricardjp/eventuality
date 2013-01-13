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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.arcanix.eventuality.utils.ReflectionUtils;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventValidator {

	public List<String> validateEvent(final Class<?> event) {
		List<String> validationErrors = new ArrayList<>();
		if (!event.isInterface()) {
			validationErrors.add("Event must be an interface");
		}

		Method[] methods = ReflectionUtils.getUniqueMethods(event);
		if (methods.length != 1) {
			validationErrors.add("Event must only declare one and only one method");
		} else {
			if (!methods[0].getReturnType().equals(Void.TYPE)) {
				validationErrors.add("Event method must return void");
			}
		}
		return validationErrors;
	}
	
}
