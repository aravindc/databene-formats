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

package org.databene.formats.xml;

import static org.junit.Assert.*;

import org.databene.commons.CollectionUtil;
import org.databene.formats.xml.AbstractXMLElementParser;
import org.databene.formats.xml.ParseContext;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * Tests the {@link AbstractXMLElementParser}.<br/><br/>
 * Created: 14.12.2012 20:24:54
 * @since 0.6.15
 * @author Volker Bergmann
 */
public class AbstractXMLElementParserTest {
	
	@Test
	public void testRenderUnsupportedAttributesMessage() {
		MyXMLElementParser parser = new MyXMLElementParser();
		StringBuilder message = parser.renderUnsupportedAttributesMessage("att1");
		String expectedMessage = "attribute 'att1' is not supported. The attributes supported by <elem> are: " +
				"req2, req1, opt2, opt1";
		assertEquals(expectedMessage, message.toString());
	}
	
	static class MyXMLElementParser extends AbstractXMLElementParser<Object> {

		public MyXMLElementParser() {
			super("elem", CollectionUtil.toSet("req1", "req2"), CollectionUtil.toSet("opt1", "opt2"));
		}

		@Override
		protected Object doParse(Element element, Object[] parentPath, ParseContext<Object> context) {
			return null;
		}
		
	}
	
}
