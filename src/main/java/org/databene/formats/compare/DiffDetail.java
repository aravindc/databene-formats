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

import org.databene.commons.Converter;
import org.databene.commons.NullSafeComparator;

/**
 * Represents a difference between the state of two objects.
 * Created: 21.11.2013 11:29:35
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class DiffDetail {
	
	protected final Object expected;
	protected final Object actual;
	protected final String objectClassifier;
	protected final DiffDetailType type;
	protected final String locatorOfExpected;
	protected final String locatorOfActual;
	protected final Converter<Object, String> formatter;
	
	public DiffDetail(Object expected, Object actual, String objectClassifier, DiffDetailType type, Converter<Object, String> formatter) {
		this(expected, actual, objectClassifier, type, null, null, formatter);
	}
	
	public DiffDetail(Object expected, Object actual, String objectClassifier, DiffDetailType type, String locatorOfExpected, String locatorOfActual, Converter<Object, String> formatter) {
		this.expected = expected;
		this.actual = actual;
		this.objectClassifier = objectClassifier;
		this.type = type;
		this.locatorOfExpected = locatorOfExpected;
		this.locatorOfActual = locatorOfActual;
		this.formatter = formatter;
	}

	public Object getExpected() {
		return expected;
	}
	
	public Object getActual() {
		return actual;
	}
	
	public String getObjectClassifier() {
		return objectClassifier;
	}
	
	public DiffDetailType getType() {
		return type;
	}
	
	public String getLocatorOfExpected() {
		return locatorOfExpected;
	}
	
	public String getLocatorOfActual() {
		return locatorOfActual;
	}
	
	public Converter<Object, String> getFormatter() {
		return formatter;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locatorOfExpected == null) ? 0 : locatorOfExpected.hashCode());
		result = prime * result + ((locatorOfActual == null) ? 0 : locatorOfActual.hashCode());
		result = prime * result + ((objectClassifier == null) ? 0 : objectClassifier.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((expected == null) ? 0 : expected.hashCode());
		result = prime * result + ((actual == null) ? 0 : actual.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		DiffDetail that = (DiffDetail) obj;
		return (NullSafeComparator.equals(this.expected, that.expected) &&
				NullSafeComparator.equals(this.actual, that.actual) &&
				NullSafeComparator.equals(this.type, that.type) &&
				NullSafeComparator.equals(this.locatorOfExpected, that.locatorOfExpected) &&
				NullSafeComparator.equals(this.locatorOfActual, that.locatorOfActual));
	}

	@Override
	public String toString() {
		switch (type) {
			case DIFFERENT :  return "Different " + objectClassifier + ": expected " + format(expected) + " but found " + format(actual) + (locatorOfActual != null ? " at " + locatorOfActual : "");
			case MISSING :    return "Missing " + objectClassifier + " " + format(expected) + " at " + locatorOfExpected;
			case UNEXPECTED : return "Unexpected " + objectClassifier + " " + format(actual) + " found at " + locatorOfActual;
			case MOVED :      return "Moved " + objectClassifier + " " + format(expected) + " from " + locatorOfExpected + " to " + locatorOfActual;
			default :         return type + " " + objectClassifier + ", expected " + format(expected) + ", found " + actual + " " + locatorOfExpected + " " + locatorOfActual;
		}
	}

	protected String format(Object value) {
		return formatter.convert(value);
	}
	
}
