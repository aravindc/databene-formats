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
package org.databene.formats.util;

import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Forwards the data of another {@link DataIterator}, swallowing the first elements 
 * provided by its {@link #next(DataContainer)} method.<br/><br/>
 * Created: 18.09.2014 10:01:00
 * @since 1.0
 * @author Volker Bergmann
 */

public class OffsetDataIterator<E> extends DataIteratorProxy<E> {

	public OffsetDataIterator(DataIterator<E> source, int offset) {
		super(source);
		// consume the first 'offset' elements of the source
		DataContainer<E> container = new DataContainer<E>();
		for (int i = 0; i < offset; i++)
			source.next(container);
	}

}
