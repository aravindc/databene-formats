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
package org.databene.formats.fixedwidth;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.databene.commons.TimeUtil;
import org.databene.commons.format.Alignment;

import org.junit.Test;

/**
 * Tests the {@link FixedWidthColumnDescriptor}.
 * Created at 05.05.2008 07:18:54
 * @since 0.5.3
 * @author Volker Bergmann
 */
public class FixedWidthColumnDescriptorTest {

	@Test
	public void testEquals() {
		FixedWidthColumnDescriptor d1 = new FixedWidthColumnDescriptor("name", 8, Alignment.LEFT, ' ');
		// simple tests
		assertFalse(d1.equals(null));
		assertFalse(d1.equals(""));
		assertTrue(d1.equals(d1));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name2", 8, Alignment.LEFT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name3", 9, Alignment.LEFT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name4", 8, Alignment.RIGHT, ' ')));
		assertFalse(d1.equals(new FixedWidthColumnDescriptor("name5", 8, Alignment.LEFT, '_')));
	}
	
	@Test
	public void testFormatNumber() throws ParseException {
		DecimalFormat format = new DecimalFormat("00.00", DecimalFormatSymbols.getInstance(Locale.US));
		FixedWidthColumnDescriptor d1 = new FixedWidthColumnDescriptor("num", format, "");
		assertEquals("00.00", d1.format(0.));
		assertEquals("01.50", d1.format(1.5));
		assertEquals("     ", d1.format(null));
	}
	
	@Test
	public void testFormatDate() throws ParseException {
		FixedWidthColumnDescriptor d1 = new FixedWidthColumnDescriptor("date", new SimpleDateFormat("yyyyMMdd"), "");
		assertEquals("19870503", d1.format(TimeUtil.date(1987, 4, 3)));
		assertEquals("        ", d1.format(null));
	}
	
	@Test
	public void testParseDate() throws ParseException {
		FixedWidthColumnDescriptor d1 = new FixedWidthColumnDescriptor("date", new SimpleDateFormat("yyyyMMdd"), "");
		assertEquals(TimeUtil.date(1987, 4, 3), d1.parse("19870503"));
	}
	
}
