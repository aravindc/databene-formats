/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.csv;

import static org.junit.Assert.*;

import org.databene.commons.Encodings;
import org.databene.formats.DataContainer;
import org.databene.formats.csv.CSVSingleColumIterator;
import org.databene.formats.util.DataIteratorTestCase;
import org.junit.Test;

/**
 * Tests the {@link CSVSingleColumIterator}.<br/><br/>
 * Created: 14.10.2009 12:06:42
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class CSVSingleColumnIteratorTest extends DataIteratorTestCase {

	private static final String FILENAME = "file://org/databene/formats/csv/persons.csv";

	@Test
	public void testValidColumns() throws Exception {
		CSVSingleColumIterator iterator0 = new CSVSingleColumIterator(FILENAME, 0, ',', true, Encodings.UTF_8);
		try {
			expectNextElements(iterator0, "name", "Alice", "Bob").withNoNext();
		} finally {
			iterator0.close();
		}
		CSVSingleColumIterator iterator1 = new CSVSingleColumIterator(FILENAME, 1, ',', true, Encodings.UTF_8);
		try {
			expectNextElements(iterator1, "age", "23", "34").withNoNext();
		} finally {
			iterator1.close();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNegativeColumn() throws Exception {
		new CSVSingleColumIterator(FILENAME, -1, ',', true, Encodings.UTF_8);
	}
	
	@Test
	public void testNonExistingColumn() throws Exception {
		CSVSingleColumIterator iterator1 = new CSVSingleColumIterator(FILENAME, 2, ',', true, Encodings.UTF_8);
		try {
			assertNull(iterator1.next(new DataContainer<String>()).getData());
		} finally {
			iterator1.close();
		}
	}
	
}
