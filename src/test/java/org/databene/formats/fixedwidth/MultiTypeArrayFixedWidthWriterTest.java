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
import java.util.Calendar;
import java.util.Locale;

import org.databene.commons.ReaderLineIterator;
import org.databene.commons.TimeUtil;
import org.junit.Test;

/**
 * Tests the {@link MultiTypeArrayFixedWidthWriter}.<br/><br/>
 * Created: 13.03.2014 13:10:27
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class MultiTypeArrayFixedWidthWriterTest {
	
	@Test
	public void testDefaultFormats() throws Exception {
		String fileName = "target" + File.separator + getClass().getSimpleName() + ".fcw";
		FileWriter out = new FileWriter(fileName);
		MultiTypeArrayFixedWidthWriter writer = new MultiTypeArrayFixedWidthWriter(out);
		FixedWidthRowTypeDescriptor f1 = FixedWidthUtil.parseArrayColumnsSpec("8,3r0,10,5", "t1", "", Locale.US);
		writer.addRowFormat(f1);
		FixedWidthRowTypeDescriptor f2 = FixedWidthUtil.parseArrayColumnsSpec("6,5r0,15", "t2", "", Locale.US);
		writer.addRowFormat(f2);
		writer.write("t1", "Alice", 23, TimeUtil.date(2014, Calendar.JANUARY, 1), 1.23);
		writer.write("t2", "Bob", 34, TimeUtil.date(2014, Calendar.FEBRUARY, 28));
		writer.close();
		ReaderLineIterator iterator = new ReaderLineIterator(new FileReader(fileName));
		assertTrue(iterator.hasNext());
		assertEquals("Alice   0232014-01-011.23 ", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("Bob   000342014-02-28     ", iterator.next());
		assertFalse(iterator.hasNext());
		iterator.close();
	}
	
}
