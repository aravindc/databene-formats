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
package org.databene.formats.csv;

import static org.junit.Assert.*;

import org.databene.formats.DataContainer;
import org.databene.formats.csv.CSVLineIterator;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Tests the {@link CSVLineIterator}.
 * Created: 29.09.2006 16:15:23
 * @since 0.1
 * @author Volker Bergmann
 */
public class CSVLineIteratorTest {

	@Test
    public void testIgnoringEmptyLines() throws IOException {
        CSVLineIterator iterator = new CSVLineIterator("file://org/databene/formats/csv/names.csv", ',', true);
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] { "Alice", "Bob" },               iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Charly" },                     iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Dieter", "Indiana\r\nJones" }, iterator.next(container).getData()));
        assertNull(iterator.next(container));
        iterator.close();
    }

	@Test
    public void testIncludingEmptyLines() throws IOException {
        CSVLineIterator iterator = new CSVLineIterator("file://org/databene/formats/csv/names.csv");
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] { "Alice", "Bob" },               iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Charly" },                     iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "Dieter", "Indiana\r\nJones" }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[0], iterator.next(container).getData()));
        assertNull(iterator.next(container));
        iterator.close();
    }
	
	@Test
    public void testEmptyRow() throws IOException {
        CSVLineIterator iterator = new CSVLineIterator("string://DATA\r\n\r\nDATA2");
        DataContainer<String[]> container = new DataContainer<String[]>();
        assertTrue(Arrays.equals(new String[] { "DATA" }, iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[0], iterator.next(container).getData()));
        assertTrue(Arrays.equals(new String[] { "DATA2" }, iterator.next(container).getData()));
        assertNull(iterator.next(container));
        iterator.close();
    }
	
	@Test
    public void testEmptyCell() throws IOException {
        CSVLineIterator iterator = new CSVLineIterator("string://name,\"\",,x");
        DataContainer<String[]> container = new DataContainer<String[]>();
        String[] line = iterator.next(container).getData();
		assertTrue(Arrays.equals(new String[] { "name", "", null, "x" }, line));
        assertNull(iterator.next(container));
        iterator.close();
    }
	
}
