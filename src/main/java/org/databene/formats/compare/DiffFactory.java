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
import org.databene.commons.converter.ToStringConverter;

/**
 * Creates {@link DiffDetail} objects.
 * Created: 21.11.2013 12:25:59
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class DiffFactory {
	
	private Converter<Object, String> formatter;
	
	public DiffFactory() {
		this(new ToStringConverter());
	}

	public DiffFactory(Converter<Object, String> formatter) {
		this.formatter = formatter;
	}

	public DiffDetail missing(Object object, String objectClassifier, String locator) {
		return genericDiff(object, null, objectClassifier, DiffDetailType.MISSING, locator, null);
	}
	
	public DiffDetail unexpected(Object object, String objectClassifier, String locator) {
		return genericDiff(null, object, objectClassifier, DiffDetailType.UNEXPECTED, null, locator);
	}
	
	public DiffDetail moved(Object object, String objectClassifier, String locatorOfExpected, String locatorOfActual) {
		return genericDiff(object, object, objectClassifier, DiffDetailType.MOVED, locatorOfExpected, locatorOfActual);
	}

	public DiffDetail different(Object expected, Object actual, String objectClassifier, String locatorOfExpected, String locatorOfActual) {
		return genericDiff(expected, actual, objectClassifier, DiffDetailType.DIFFERENT, locatorOfExpected, locatorOfActual);
	}

	public DiffDetail genericDiff(Object expected, Object actual, String objectClassifier,
			DiffDetailType diffType, String locatorOfExpected, String locatorOfActual) {
		return new DiffDetail(expected, actual, objectClassifier, diffType, locatorOfExpected, locatorOfActual, formatter);
	}
	
}
