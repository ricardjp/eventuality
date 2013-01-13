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

package com.arcanix.eventuality.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
 */
public final class ReflectionUtils {
	
	/**
	 * Utility class, can't instantiate.
	 */
	private ReflectionUtils() {
		throw new AssertionError("Utility class, can't instantiate");
	}
	
	public static Set<Class<?>> getAllInterfaces(final Class<?> clazz) {
		Set<Class<?>> interfaces = new HashSet<>();
		if (clazz == null) {
			return interfaces;
		}
		Class<?> toAnalyze = clazz;
		do {
			for (Class<?> declaredInterface : toAnalyze.getInterfaces()) {
				interfaces.add(declaredInterface);
			}
			toAnalyze = toAnalyze.getSuperclass();
		} while (toAnalyze != null);
		return interfaces;
	}
	
	public static Method[] getUniqueMethods(final Class<?> clazz) {
		if (clazz == null) {
			return new Method[0];
		}
		Map<MethodSignature, Method> methods = new HashMap<>();
		Class<?> toAnalyze = clazz;
		do {
			for (Method method : toAnalyze.getMethods()) {
				methods.put(new MethodSignature(method), method);
			}
			toAnalyze = toAnalyze.getSuperclass();
		} while (toAnalyze != null);
		return methods.values().toArray(new Method[methods.size()]);
	}
	
	/**
	 * @author ricardjp@arcanix.com (Jean-Philippe Ricard)
	 */
	private static final class MethodSignature {
		
		private String name;
		private Class<?>[] parameterTypes;
		
		public MethodSignature(final Method method) {
			if (method == null) {
				throw new IllegalArgumentException("Method cannot be null");
			}
			this.name = method.getName();
			this.parameterTypes = method.getParameterTypes();
		}
		
		public String getName() {
			return name;
		}
		
		public Class<?>[] getParameterTypes() {
			return parameterTypes;
		}
		
		@Override
		public int hashCode() {
			int hash = this.name.hashCode();
			for (Class<?> parameterType : this.parameterTypes) {
				hash = hash * 31 + parameterType.hashCode();
			}
			return hash;
		}
		
		@Override
		public boolean equals(final Object other) {
			if (other == null) {
				return false;
			}
			if (other == this) {
				return true;
			}
			if (!(other instanceof MethodSignature)) {
				return false;
			}
			MethodSignature otherCasted = (MethodSignature) other;
			return this.name.equals(otherCasted.getName())
					&& Arrays.equals(this.parameterTypes, otherCasted.getParameterTypes());
		}
		
	}
	
}
