/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.formats.xls;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.databene.formats.Address;
import org.databene.formats.DataContainer;
import org.databene.formats.PersonWithAddress;
import org.junit.Test;

/**
 * Tests the {@link XLSJavaBeanIterator}.<br/><br/>
 * Created: 18.09.2014 18:57:05
 * @since 1.0.0
 * @author Volker Bergmann
 */

public class XLSJavaBeanIteratorTest {

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
