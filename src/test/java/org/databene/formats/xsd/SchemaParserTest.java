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

package org.databene.formats.xsd;

import static org.junit.Assert.*;

import org.databene.commons.xml.XMLUtil;
import org.databene.formats.xsd.Schema;
import org.databene.formats.xsd.SchemaParser;
import org.junit.Test;

/**
 * Tests the {@link SchemaParser}.<br/><br/>
 * Created: 16.05.2014 18:39:35
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class SchemaParserTest {
	
	@Test
	public void test() throws Exception {
		Schema schema = new SchemaParser().parse(XMLUtil.parse("org/databene/formats/xsd/D03A_IFTDGN.xsd"));
		schema.printContent();
		assertEquals("\nCreated: Exported from EDISIM 6.12.1 10/16/2013 15:43:17.713\nType: UN\nVRI: D 03A\nDesc: UN/EDIFACT Draft Messages and Directories Version D.03A - publ. Jun. 2003\n", schema.getDocumentation());
	}
	
}
