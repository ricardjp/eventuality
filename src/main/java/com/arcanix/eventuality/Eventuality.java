package com.arcanix.eventuality;

import com.arcanix.eventuality.conf.EventualityUnit;
import com.arcanix.eventuality.internal.EventCollector;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class Eventuality {

	/**
	 * Singleton. Can't instantiate.
	 */
	private Eventuality() {
		throw new AssertionError("Singleton. Can't instantiate");
	}
	
	public static EventDispatcher createEventDispatcher(final EventualityUnit... modules) {
		EventCollector eventCollector = new EventCollector();
		Events events = new Events(eventCollector);
		for (EventualityUnit eventualityModule : modules) {
			eventualityModule.configure(events);
		}
		EventDispatcher eventDispatcher = new EventDispatcher();
		eventCollector.configureDispatcher(eventDispatcher);
		return eventDispatcher;
	}
	
}
