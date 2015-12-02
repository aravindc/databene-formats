/*
 * Copyright (C) 2011-2014 Volker Bergmann (volker.bergmann@bergmann-it.de).
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
package org.databene.formats.xsd;

import static org.junit.Assert.*;

import org.databene.commons.xml.XMLUtil;
import org.databene.formats.xsd.Schema;
import org.databene.formats.xsd.SchemaParser;
import org.junit.Test;

/**
 * Tests the {@link SchemaParser}.
 * Created: 16.05.2014 18:39:35
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class SchemaParserTest {
	
	private static final String EXPECTED_SCHEMA_DOC = "\nCreated: Exported from EDISIM 6.12.1 10/16/2013 15:43:17.713\nType: UN\n" +
			"VRI: D 03A\nDesc: UN/EDIFACT Draft Messages and Directories Version D.03A - publ. Jun. 2003\n";

	@Test
	public void test() throws Exception {
		Schema schema = new SchemaParser().parse(XMLUtil.parse("org/databene/formats/xsd/D03A_IFTDGN.xsd"));
		schema.printContent();
		assertEquals(EXPECTED_SCHEMA_DOC, schema.getDocumentation());
	}
	
}
