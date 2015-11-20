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
 * Provides comparison for equality and comparison for objects of a given parameterized type E.<br/><br/>
 * Created: 20.11.2013 17:40:05
 * @since 1.0
 * @author Volker Bergmann
 */

public interface ComparisonModel<E> {
	boolean equal(E o1, E o2);
	boolean correspond(E o1, E o2);
	String subPath(E[] array, int index);
}