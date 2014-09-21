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

package org.databene.formats.fixedwidth;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;

import org.databene.commons.ReaderLineIterator;
import org.junit.Test;

/**
 * Tests the {@link MultiTypeBeanFixedWidthWriter}.<br/><br/>
 * Created: 14.03.2014 16:26:52
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class MultiTypeBeanFixedWidthWriterTest {

	@Test
	public void testDefaultFormats() throws Exception {
		String fileName = "target" + File.separator + getClass().getSimpleName() + ".fcw";
		FileWriter out = new FileWriter(fileName);
		MultiTypeBeanFixedWidthWriter writer = new MultiTypeBeanFixedWidthWriter(out);
		writer.addRowFormat("FWPerson", FixedWidthUtil.parseBeanColumnsSpec("name[8],age[3r0],pet.name[7]", "FWPerson", "", Locale.US));
		writer.addRowFormat("FWCity", FixedWidthUtil.parseBeanColumnsSpec("name[18]", "FWCity", "", Locale.US));
		writer.write(new FWPerson("Alice", 23, new FWPet("Miez")));
		writer.write(new FWCity("New York"));
		writer.close();
		ReaderLineIterator iterator = new ReaderLineIterator(new FileReader(fileName));
		assertTrue(iterator.hasNext());
		assertEquals("Alice   023Miez   ", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("New York          ", iterator.next());
		assertFalse(iterator.hasNext());
		iterator.close();
	}
	
}
