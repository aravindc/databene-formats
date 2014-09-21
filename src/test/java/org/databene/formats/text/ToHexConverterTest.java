/*
 * (c) Copyright 2009-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.text;

import static org.junit.Assert.*;

import org.databene.formats.text.ToHexConverter;
import org.junit.Test;

/**
 * Tests the {@link ToHexConverter}.<br/><br/>
 * Created: 29.10.2009 10:26:04
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class ToHexConverterTest {

	@Test
	public void testLong() {
		check("0", 0L);
		check("1", 1L);
		check("7fffffffffffffff", Long.MAX_VALUE);
		check("8000000000000000", Long.MIN_VALUE);
		check("ffffffffffffffff", -1L);
	}

	@Test
	public void testInt() {
		check("0", 0);
		check("1", 1);
		check("7fffffff", Integer.MAX_VALUE);
		check("80000000", Integer.MIN_VALUE);
		check("ffffffff", -1);
	}

	@Test
	public void testShort() {
		check("0", 0);
		check("1", 1);
		check("7fff", Short.MAX_VALUE);
		check("8000", Short.MIN_VALUE);
		check("ffff", (short) -1);
	}

	@Test
	public void testByte() {
		check("0", 0);
		check("1", 1);
		check("7f", Byte.MAX_VALUE);
		check("80", Byte.MIN_VALUE);
		check("ff", (byte) -1);
	}

	@Test
	public void testChar() {
		check("30", '0');
		check("41", 'A');
		check("0a", '\n');
	}

	@Test
	public void testString() {
		check("41300a", "A0\n");
	}
	
	@Test
	public void testLength() {
		checkLength("ff",   0xff, 2);
		checkLength("00ff", 0xff, 4);
	}

	@Test
	public void testPattern() {
		checkPattern("ffh",  0xff, "{0}h" );
		checkPattern("0xff", 0xff, "0x{0}");
	}

	@Test
	public void testCase() {
		checkUpperCase("FF", 0xff);
	}

	@Test
	public void testCombinedOptions() {
	    assertEquals("0x00FF", new ToHexConverter(true, "0x{0}", 4).convert(0xff));
	}

	private static void check(String expected, Object in) {
	    assertEquals(expected, new ToHexConverter().convert(in));
    }

	private static void checkLength(String expected, Object in, int length) {
	    assertEquals(expected, new ToHexConverter(false, null, length).convert(in));
    }

	private static void checkUpperCase(String expected, Object in) {
	    assertEquals(expected, new ToHexConverter(true).convert(in));
    }

	private static void checkPattern(String expected, Object in, String pattern) {
	    assertEquals(expected, new ToHexConverter(false, pattern).convert(in));
    }

}
