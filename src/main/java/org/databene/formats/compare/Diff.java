/*
 * Copyright (C) 2011-2014 Volker Bergmann (volker.bergmann@bergmann-it.de).
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

import org.databene.commons.SystemInfo;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 * Represents a difference between the state of two objects.<br/><br/>
 * Created: 21.11.2013 11:29:35
 * @since 1.0
 * @author Volker Bergmann
 */

public class Diff<E> {
	
	private static final String LF = SystemInfo.getLineSeparator();
	
	private E expected;
	private E actual;
	private String objectClassifier;
	private DiffType type;
	private String locator1;
	private String locator2;
	
	Diff(E expected, E actual, String objectClassifier, DiffType type) {
		this(expected, actual, objectClassifier, type, null, null);
	}
	
	Diff(E expected, E actual, String objectClassifier, DiffType type, String locator1, String locator2) {
		this.expected = expected;
		this.actual = actual;
		this.objectClassifier = objectClassifier;
		this.type = type;
		this.locator1 = locator1;
		this.locator2 = locator2;
	}

	public E getExpected() {
		return expected;
	}
	
	public E getActual() {
		return actual;
	}
	
	public String getObjectClassifier() {
		return objectClassifier;
	}
	
	public DiffType getType() {
		return type;
	}
	
	public String getLocator1() {
		return locator1;
	}
	
	public String getLocator2() {
		return locator2;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Diff other = (Diff) obj;
		if (locator1 == null) {
			if (other.locator1 != null)
				return false;
		} else if (!locator1.equals(other.locator1))
			return false;
		if (locator2 == null) {
			if (other.locator2 != null)
				return false;
		} else if (!locator2.equals(other.locator2))
			return false;
		if (objectClassifier == null) {
			if (other.objectClassifier != null)
				return false;
		} else if (!objectClassifier.equals(other.objectClassifier))
			return false;
		if (type != other.type)
			return false;
		if (expected == null) {
			if (other.expected != null)
				return false;
		} else if (!expected.equals(other.expected))
			return false;
		if (actual == null) {
			if (other.actual != null)
				return false;
		} else if (!actual.equals(other.actual))
			return false;
		return true;
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

	private String format(E value) {
		if (value instanceof Element)
			return LF + XMLUtil.format((Element) value).trim() + LF;
		else
			return "'" + String.valueOf(value) + "'";
	}
	
}
