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


/**
 * Creates {@link Diff} objects.<br/><br/>
 * Created: 21.11.2013 12:25:59
 * @since 1.0
 * @author Volker Bergmann
 */

public class DiffFactory {
	
	public static <T> Diff<T> missing(T object, String objectClassifier, String locator) {
		return new Diff<T>(object, null, objectClassifier, DiffType.MISSING, locator, null);
	}
	
	public static <T> Diff<T> unexpected(T object, String objectClassifier, String locator) {
		return new Diff<T>(null, object, objectClassifier, DiffType.UNEXPECTED, null, locator);
	}
	
	public static <T> Diff<T> moved(T object, String objectClassifier, String locator1, String locator2) {
		return new Diff<T>(object, object, objectClassifier, DiffType.MOVED, locator1, locator2);
	}

	public static <T> Diff<T> different(T version1, T version2, String objectClassifier, String locator2) {
		return new Diff<T>(version1, version2, objectClassifier, DiffType.DIFFERENT, null, locator2);
	}
	
}
