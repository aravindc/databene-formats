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
package org.databene.formats.xls;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.databene.commons.TimeUtil;
import org.databene.formats.Address;
import org.databene.formats.DataContainer;
import org.databene.formats.PersonWithAddress;
import org.junit.Test;

/**
 * Tests the {@link XLSJavaBeanIterator}.
 * Created: 18.09.2014 18:57:05
 * @since 1.0.0
 * @author Volker Bergmann
 */

public class XLSJavaBeanIteratorTest {

	@Test
	public void testFlat() throws InvalidFormatException, IOException {
		XLSJavaBeanIterator iterator = new XLSJavaBeanIterator("org/databene/formats/xls/person_lines.xls", "persons", true, PersonWithAddress.class);
		DataContainer<Object> wrapper = new DataContainer<Object>();
		assertNotNull(iterator.next(wrapper));
		assertContent("Alice", 23, TimeUtil.date(2011, 0, 1), wrapper);
		assertNotNull(iterator.next(wrapper));
		assertContent("Bob", 34, TimeUtil.date(2011, 0, 2), wrapper);
		assertNull(iterator.next(wrapper));
	}

	@Test
	public void testTrailingNullHeaders() throws InvalidFormatException, IOException {
		XLSJavaBeanIterator iterator = new XLSJavaBeanIterator("org/databene/formats/xls/person_lines_empty_headers_trailing.xls", "persons", true, PersonWithAddress.class);
		DataContainer<Object> wrapper = new DataContainer<Object>();
		assertNotNull(iterator.next(wrapper));
		assertContent("Alice", 23, TimeUtil.date(2011, 0, 1), wrapper);
		assertNotNull(iterator.next(wrapper));
		assertContent("Bob", 34, TimeUtil.date(2011, 0, 2), wrapper);
		assertNull(iterator.next(wrapper));
	}

	@Test
	public void testOneToOneAssociation() throws InvalidFormatException, IOException {
		XLSJavaBeanIterator iterator = new XLSJavaBeanIterator("org/databene/formats/xls/persons_with_address.xls", "persons", true, PersonWithAddress.class);
		DataContainer<Object> wrapper = new DataContainer<Object>();
		assertNotNull(iterator.next(wrapper));
		assertContent("Alice", 23, "London", wrapper);
		assertNotNull(iterator.next(wrapper));
		assertContent("Bob", 34, "New York", wrapper);
		assertNull(iterator.next(wrapper));
	}

	@Test
	public void testOneToManyAssociation() throws InvalidFormatException, IOException {
		XLSJavaBeanIterator iterator = new XLSJavaBeanIterator("org/databene/formats/xls/persons_with_addresses.xls", "persons", true, PersonWithAddress.class);
		DataContainer<Object> wrapper = new DataContainer<Object>();
		assertNotNull(iterator.next(wrapper));
		assertContent("Alice", 23, "London", "Dover", wrapper);
		assertNotNull(iterator.next(wrapper));
		assertContent("Bob", 34, "New York", "Hauppauge", wrapper);
		assertNull(iterator.next(wrapper));
	}


	// private helpers -------------------------------------------------------------------------------------------------

	private static void assertContent(String name, int age, String city, DataContainer<Object> wrapper) {
		Object data = wrapper.getData();
		assertNotNull(data);
		PersonWithAddress person = (PersonWithAddress) data;
		assertEquals(name, person.getName());
		assertEquals(age, person.getAge());
		assertEquals(city, person.getAddress().getCity());
	}

	private static void assertContent(String name, int age, Date date, DataContainer<Object> wrapper) {
		Object data = wrapper.getData();
		assertNotNull(data);
		PersonWithAddress person = (PersonWithAddress) data;
		assertEquals(name, person.getName());
		assertEquals(age, person.getAge());
		assertEquals(date, person.getDate());
	}

	private static void assertContent(String name, int age, String city1, String city2, DataContainer<Object> wrapper) {
		Object data = wrapper.getData();
		assertNotNull(data);
		PersonWithAddress person = (PersonWithAddress) data;
		assertEquals(name, person.getName());
		assertEquals(age, person.getAge());
		List<Address> addresses = person.getAddresses();
		assertEquals(city1, addresses.get(0).getCity());
		assertEquals(city2, addresses.get(1).getCity());
	}

}
