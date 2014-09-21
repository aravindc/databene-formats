/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

import java.io.StringWriter;

import org.databene.commons.SystemInfo;
import org.databene.formats.csv.CSVUtil;
import org.junit.Test;

/**
 * Tests the {@link CSVUtil} class.<br/><br/>
 * Created: 16.09.2011 13:17:50
 * @since 0.6.2
 * @author Volker Bergmann
 */
public class CSVUtilTest {

	@Test
	public void testRenderCell() {
		// simple test
		assertEquals("Alice", CSVUtil.renderCell("Alice", ','));
		// test cell with comma
		assertEquals("\"Alice,Bob\"", CSVUtil.renderCell("Alice,Bob", ','));
		// test cell with quotes
		assertEquals("\"\"\"Ha! Ha!\"\" Said the clown\"", CSVUtil.renderCell("\"Ha! Ha!\" Said the clown", ','));
		// test cell with quotes and comma
		assertEquals("\"\"\"One, two, three\"\" and so\"", CSVUtil.renderCell("\"One, two, three\" and so", ','));
	}
	
	@Test
	public void testWriteRow() throws Exception {
		StringWriter out = new StringWriter();
		CSVUtil.writeRow(out, ',', null, "A", "B,C", "D\"E\"F", "G,\"H\",I");
		assertEquals(",A,\"B,C\",\"D\"\"E\"\"F\",\"G,\"\"H\"\",I\"" + SystemInfo.getLineSeparator(), out.toString());
	}
}
