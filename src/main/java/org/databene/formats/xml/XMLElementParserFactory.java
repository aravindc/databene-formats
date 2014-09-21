/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.ArrayUtil;
import org.databene.commons.ParseException;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 * Factory method which provides {@link XMLElementParser}s.<br/><br/>
 * Created: 05.12.2010 12:15:57
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class XMLElementParserFactory<E> {
	
	protected List<XMLElementParser<E>> parsers;

	public XMLElementParserFactory() {
		this.parsers = new ArrayList<XMLElementParser<E>>();
	}
	
	public void addParser(XMLElementParser<E> parser) {
		this.parsers.add(parser);
	}

	public XMLElementParser<E> getParser(Element element, E[] parentPath) {
		for (int i = parsers.size() - 1; i >= 0; i--) { // search for parsers in reverse order, to child classes can override parsers of parent classes
			XMLElementParser<E> parser = parsers.get(i);
			if (parser.supports(element, parentPath))
				return parser;
		}
		Object parent = (ArrayUtil.isEmpty(parentPath) ? null : ArrayUtil.lastElementOf(parentPath));
		String message = "Syntax Error: Element '" + element.getNodeName() + 
			"' not supported " + (parent != null ? 
				"in the context of a " + parent.getClass().getSimpleName() :
				"as top level element");
		throw new ParseException(message, XMLUtil.format(element));
	}
	
}
