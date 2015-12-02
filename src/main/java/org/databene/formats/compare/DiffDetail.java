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
	
	private Object expected;
	private Object actual;
	private String objectClassifier;
	private DiffDetailType type;
	private String locator1;
	private String locator2;
	private Converter<Object, String> formatter;
	
	public DiffDetail(Object expected, Object actual, String objectClassifier, DiffDetailType type, Converter<Object, String> formatter) {
		this(expected, actual, objectClassifier, type, null, null, formatter);
	}
	
	public DiffDetail(Object expected, Object actual, String objectClassifier, DiffDetailType type, String locator1, String locator2, Converter<Object, String> formatter) {
		this.expected = expected;
		this.actual = actual;
		this.objectClassifier = objectClassifier;
		this.type = type;
		this.locator1 = locator1;
		this.locator2 = locator2;
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
	
	public String getLocator1() {
		return locator1;
	}
	
	public String getLocator2() {
		return locator2;
	}
	
	public void setFormat(Converter<Object, String> formatter) {
		this.formatter = formatter;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locator1 == null) ? 0 : locator1.hashCode());
		result = prime * result + ((locator2 == null) ? 0 : locator2.hashCode());
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
				NullSafeComparator.equals(this.locator1, that.locator1) &&
				NullSafeComparator.equals(this.locator2, that.locator2));
	}

	@Override
	public String toString() {
		switch (type) {
			case DIFFERENT :  return "Different " + objectClassifier + ": expected " + format(expected) + " but found " + format(actual) + (locator2 != null ? " at " + locator2 : "");
			case MISSING :    return "Missing " + objectClassifier + " " + format(expected) + " at " + locator1;
			case UNEXPECTED : return "Unexpected " + objectClassifier + " " + format(actual) + " found at " + locator2;
			case MOVED :      return "Moved " + objectClassifier + " " + format(expected) + " from " + locator1 + " to " + locator2;
			default :         return type + " " + objectClassifier + ", expected " + format(expected) + ", found " + actual + " " + locator1 + " " + locator2;
		}
	}

	protected String format(Object value) {
		return formatter.convert(value);
	}
	
}
