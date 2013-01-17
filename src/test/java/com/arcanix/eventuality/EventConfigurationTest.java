package com.arcanix.eventuality;

import org.junit.Test;

import com.arcanix.eventuality.EventDispatcherTest.ThreadStartEvent;
import com.arcanix.eventuality.EventDispatcherTest.ThreadStopEvent;
import com.arcanix.eventuality.EventDispatcherTest.ThreadSubscriber;
import com.arcanix.eventuality.internal.EventCollector;

// TODO MOCK the dispatcher
// TODO allow event declaration through eventuality
public class EventConfigurationTest {
	
	public interface MyGroup {}
	
	@Test
	public void testConfiguration() {
		
		Eventuality eventuality = new Eventuality() {
			
			@Override
			public void configure(final Events events) {

				events.subscribe(ThreadSubscriber.class).groupedBy(MyGroup.class).in(EventDefaultScope.INSTANCE);
				events.subscribe(new ThreadSubscriber()).groupedBy(MyGroup.class);
				
				events.on(ThreadStartEvent.class).bind(new ThreadSubscriber()).groupedBy(MyGroup.class);
				events.on(ThreadStartEvent.class).bind(ThreadSubscriber.class).in(EventDefaultScope.INSTANCE);
				
			}
		};
		
		EventCollector collector = new EventCollector();
		eventuality.configure(new Events(collector));
		
		EventDispatcher dispatcher = new EventDispatcher();
		dispatcher.declare(ThreadStartEvent.class);
		dispatcher.declare(ThreadStopEvent.class);
		collector.configureDispatcher(dispatcher);
		
	}
	
}
