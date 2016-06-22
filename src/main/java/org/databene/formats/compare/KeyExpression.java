/*
 * Copyright (C) 2011-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.databene.formats.compare;

/**
 * Provides a key definition for an object based on a {@link #locator} expression 
 * for the related object and a {@link KeyExpression} to derive a key from the object.
 * The syntax of the locator and the keyExpression depends on the type of the related 
 * object structure and comparator implementation, e.g. XPath expressions for XML. 
 * Created: 13.06.2016 18:31:01
 * @since 1.0.11
 * @author Volker Bergmann
 */

public class KeyExpression {
	
	private final String locator;
	private final String expression;
	
	public KeyExpression(String locator, String expression) {
		this.locator = locator;
		this.expression = expression;
	}
	
	public String getLocator() {
		return locator;
	}
	
	public String getExpression() {
		return expression;
	}
	
	@Override
	public String toString() {
		return locator + " -> " + expression;
	}
	
}
