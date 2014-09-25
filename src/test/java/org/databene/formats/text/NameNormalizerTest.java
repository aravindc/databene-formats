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
package org.databene.formats.text;

import static org.junit.Assert.*;

import org.databene.formats.text.NameNormalizer;
import org.junit.Test;

/**
 * Tests the {@link NameNormalizer}.<br/><br/>
 * Created at 20.11.2008 19:43:30
 * @since 0.4.6
 * @author Volker Bergmann
 */

public class NameNormalizerTest {
	
	private NameNormalizer normalizer = new NameNormalizer();

	@Test
	public void test() {
		assertEquals("Alice", normalizer.convert("alice"));
		assertEquals("Alice", normalizer.convert("  ALICE  "));
		assertEquals("Alice Frazer", normalizer.convert("  ALICE   FRAZER  "));
	}

}