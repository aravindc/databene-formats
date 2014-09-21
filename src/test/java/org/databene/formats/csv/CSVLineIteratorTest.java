/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
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

import org.databene.formats.DataContainer;
import org.databene.formats.csv.CSVLineIterator;
import org.junit.Test;
import static junit.framework.Assert.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Tests the {@link CSVLineIterator}.<br/><br/>
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
    public void testEmpty() throws IOException {
        CSVLineIterator iterator = new CSVLineIterator("string://name,\"\",,x");
        DataContainer<String[]> container = new DataContainer<String[]>();
        String[] line = iterator.next(container).getData();
		assertTrue(Arrays.equals(new String[] { "name", "", null, "x" }, line));
        assertNull(iterator.next(container));
        iterator.close();
    }
	
}
