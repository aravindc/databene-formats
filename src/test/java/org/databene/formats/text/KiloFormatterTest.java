/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

import java.util.Locale;

import org.databene.formats.text.KiloFormatter;
import org.junit.Test;

/**
 * Tests the {@link KiloFormatter}.<br/><br/>
 * Created: 13.12.2012 14:17:17
 * @since 0.5.21
 * @author Volker Bergmann
 */
public class KiloFormatterTest {
	
	@Test
	public void testBase1000() {
		KiloFormatter formatter = new KiloFormatter(1000, Locale.US);
		assertEquals("0", formatter.format(0));
		assertEquals("1", formatter.format(1));
		assertEquals("999", formatter.format(999));
		assertEquals("1 K", formatter.format(1000));
		assertEquals("1.1 K", formatter.format(1100));
		assertEquals("1.9 K", formatter.format(1900));
		assertEquals("2 K", formatter.format(1999));
		assertEquals("2 K", formatter.format(2000));
		assertEquals("2 K", formatter.format(2001));
		assertEquals("1 M", formatter.format(1000000));
		assertEquals("1 G", formatter.format(1000000000L));
		assertEquals("1 T", formatter.format(1000000000000L));
		assertEquals("1 E", formatter.format(1000000000000000L));
		assertEquals("1000 E", formatter.format(1000000000000000000L));
	}
	
}
