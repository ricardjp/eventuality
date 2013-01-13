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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventValidatorTest {

	public interface EventWithWrongReturnType {
		String onEvent();
	}
	
	@Test
	public void testEventWithWrongReturnType() {
		EventValidator validator = new EventValidator();
		List<String> validationErrors = validator.validateEvent(EventWithWrongReturnType.class);
		assertFalse(validationErrors.isEmpty());
		assertEquals(1, validationErrors.size());
		assertEquals("Event method must return void", validationErrors.get(0));
	}
	
}
