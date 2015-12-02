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
		return new DiffDetail(object, null, objectClassifier, DiffDetailType.MISSING, locator, null, formatter);
	}
	
	public DiffDetail unexpected(Object object, String objectClassifier, String locator) {
		return new DiffDetail(null, object, objectClassifier, DiffDetailType.UNEXPECTED, null, locator, formatter);
	}
	
	public DiffDetail moved(Object object, String objectClassifier, String locator1, String locator2) {
		return new DiffDetail(object, object, objectClassifier, DiffDetailType.MOVED, locator1, locator2, formatter);
	}

	public DiffDetail different(Object version1, Object version2, String objectClassifier, String locator2) {
		return new DiffDetail(version1, version2, objectClassifier, DiffDetailType.DIFFERENT, null, locator2, formatter);
	}

	public DiffDetail genericDiff(Object version1, Object version2, String objectClassifier,
			DiffDetailType diffType, String locator1, String locator2) {
		return new DiffDetail(version1, version2, objectClassifier, diffType, locator1, locator2, formatter);
	}
	
}
