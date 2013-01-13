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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.arcanix.eventuality.EventDescriptor;
import com.arcanix.eventuality.EventDispatcher;
import com.arcanix.eventuality.EventListenerProvider;
import com.arcanix.eventuality.EventScope;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class EventDispatcherTest {
	
	public static final class MyScope implements EventScope {
		
		private final List<Object> scopedInstancesCreated = new ArrayList<>();
		
		private final ThreadLocal<Map<EventDescriptor<?>, Object>> context = new ThreadLocal<>();
		
		@Override
		@SuppressWarnings("unchecked")
		public <T> EventListenerProvider<T> scope(
				final EventDescriptor<T> eventDescriptor,
				final EventListenerProvider<T> unscoped) {
			
			return new EventListenerProvider<T>() {
				
				@Override
				public T get() {
					Map<EventDescriptor<?>, Object> scopeContext = MyScope.this.context.get();
					if (scopeContext == null) {
						scopeContext = new HashMap<>();
						MyScope.this.context.set(scopeContext);
					}
					if (scopeContext.containsKey(eventDescriptor)) {
						return (T) scopeContext.get(eventDescriptor);
					}
					
					T instance = unscoped.get();
					scopeContext.put(eventDescriptor, instance);
					MyScope.this.scopedInstancesCreated.add(instance);
					return instance;
				}
				
			};
		}
		
		public List<Object> getScopedInstancesCreated() {
			return scopedInstancesCreated;
		}
		
	}
	
	public interface ThreadStartEvent {
		void onThreadStart(long threadId);
	}
	
	public interface ThreadStopEvent {
		void onThreadEnd(long threadId);
	}
	
	public static final class ThreadSubscriber implements ThreadStartEvent, ThreadStopEvent {

		private boolean threadStartEventFired = false;
		private boolean threadStopEventFired = false;
		
		private long threadStartThreadId;
		private long threadStopThreadId;
		
		@Override
		public void onThreadStart(final long threadId) {
			this.threadStartEventFired = true;
			this.threadStartThreadId = threadId;
		}
		
		@Override
		public void onThreadEnd(final long threadId) {
			this.threadStopEventFired = true;
			this.threadStopThreadId = threadId;
		}
		
		public boolean isThreadStartEventFired() {
			return threadStartEventFired;
		}
		
		public boolean isThreadStopEventFired() {
			return threadStopEventFired;
		}
		
		public long getThreadStartThreadId() {
			return this.threadStartThreadId;
		}
		
		public long getThreadStopThreadId() {
			return this.threadStopThreadId;
		}
	}
	
	public static final class SubscriberRunnable implements Runnable {
		
		private final EventDispatcher dispatcher;
		
		public SubscriberRunnable(final EventDispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}
		
		@Override
		public void run() {
			long threadId = Thread.currentThread().getId();
			this.dispatcher.dispatch(ThreadStartEvent.class).onThreadStart(threadId);
			this.dispatcher.dispatch(ThreadStopEvent.class).onThreadEnd(threadId);
		}
	}
	
	@Test
	public void testOutOfScopeSubscriber() {
		EventDispatcher dispatcher = new EventDispatcher();
		dispatcher.declare(ThreadStartEvent.class);
		dispatcher.declare(ThreadStopEvent.class);
		
		ThreadSubscriber threadSubscriber = new ThreadSubscriber();
		dispatcher.addSubscriber(threadSubscriber);
		
		assertFalse(threadSubscriber.isThreadStartEventFired());
		assertFalse(threadSubscriber.isThreadStopEventFired());
		
		long threadId = Thread.currentThread().getId();
		
		dispatcher.dispatch(ThreadStartEvent.class).onThreadStart(threadId);
		
		assertTrue(threadSubscriber.isThreadStartEventFired());
		assertFalse(threadSubscriber.isThreadStopEventFired());
		assertEquals(threadId, threadSubscriber.getThreadStartThreadId());
		
		dispatcher.dispatch(ThreadStopEvent.class).onThreadEnd(threadId);
		
		assertTrue(threadSubscriber.isThreadStartEventFired());
		assertTrue(threadSubscriber.isThreadStopEventFired());
		assertEquals(threadId, threadSubscriber.getThreadStopThreadId());
	}
	
	@Test
	public void testScopedSubscriber() throws InterruptedException {
		EventDispatcher dispatcher = new EventDispatcher();
		dispatcher.declare(ThreadStartEvent.class);
		dispatcher.declare(ThreadStopEvent.class);
		
		MyScope scope = new MyScope();
		
		dispatcher.addSubscriber(ThreadSubscriber.class, scope);
		
		Thread thread = new Thread(new SubscriberRunnable(dispatcher));
		long threadId = thread.getId();
		thread.start();
		thread.join();

		// only one instance of our ThreadSubscriber should have been created
		assertEquals(1, scope.getScopedInstancesCreated().size());
		
		ThreadSubscriber threadSubscriber = (ThreadSubscriber) scope.getScopedInstancesCreated().get(0);
		
		assertTrue(threadSubscriber.isThreadStartEventFired());
		assertTrue(threadSubscriber.isThreadStopEventFired());
		assertEquals(threadId, threadSubscriber.getThreadStartThreadId());
		assertEquals(threadId, threadSubscriber.getThreadStopThreadId());
	}
}
