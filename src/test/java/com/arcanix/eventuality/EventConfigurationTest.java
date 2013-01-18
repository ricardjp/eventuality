package com.arcanix.eventuality;

import org.junit.Test;

import com.arcanix.eventuality.EventDispatcherTest.ThreadStartEvent;
import com.arcanix.eventuality.EventDispatcherTest.ThreadStopEvent;
import com.arcanix.eventuality.EventDispatcherTest.ThreadSubscriber;

// TODO MOCK the dispatcher
/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventConfigurationTest {
	
	public interface MyGroup {
	}
	
	@Test
	public void testConfiguration() {
		
		EventualityModule module = new AbstractEventualityModule() {
			
			@Override
			protected void configure() {
				declare(ThreadStartEvent.class);
				declare(ThreadStopEvent.class);
				
				subscribe(ThreadSubscriber.class).groupedBy(MyGroup.class).in(EventDefaultScope.INSTANCE);
				subscribe(new ThreadSubscriber()).groupedBy(MyGroup.class);
				
				on(ThreadStartEvent.class).bind(new ThreadSubscriber()).groupedBy(MyGroup.class);
				on(ThreadStartEvent.class).bind(ThreadSubscriber.class).in(EventDefaultScope.INSTANCE);				
			}
		};
		
		EventDispatcher dispatcher = Eventuality.createEventDispatcher(module);
		dispatcher.dispatch(ThreadStartEvent.class, MyGroup.class).onThreadStart(Thread.currentThread().getId());
		
	}
	
}
