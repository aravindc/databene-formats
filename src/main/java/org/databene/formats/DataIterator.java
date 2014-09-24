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
package org.databene.formats;

import java.io.Closeable;

/**
 * Iterates through data. Implementors are expected to be thread-safe.<br/><br/>
 * Created: 24.07.2011 08:49:16
 * @since 0.6.0
 * @author Volker Bergmann
 */
public interface DataIterator<E> extends Closeable {
	
	/** Returns the type of the iterated elements. */
	Class<E> getType();
	
	/** Returns the container with the next data element if available, otherwise null. */
	DataContainer<E> next(DataContainer<E> container);
	
	/** Closes the iterator. */
	@Override
	void close();
	
}
