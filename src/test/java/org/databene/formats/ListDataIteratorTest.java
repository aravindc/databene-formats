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
package org.databene.formats;

import static org.junit.Assert.*;

import org.databene.formats.util.ListDataIterator;
import org.junit.Test;

/**
 * Testing {@link ListDataIterator}.
 * Created: 08.12.2011 14:42:32
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class ListDataIteratorTest {

	@Test
	public void test() {
		ListDataIterator<Integer> iterator = new ListDataIterator<Integer>(Integer.class, 3, 5);
		DataContainer<Integer> container = new DataContainer<Integer>();
		assertEquals(3, iterator.next(container).getData().intValue());
		assertEquals(5, iterator.next(container).getData().intValue());
		assertNull(iterator.next(container));
		iterator.close();
	}
	
}
