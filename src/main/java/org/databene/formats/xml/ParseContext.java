/*
 * (c) Copyright 2010-2014 by Volker Bergmann. All rights reserved.
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
import java.util.Set;
import java.util.Map.Entry;

import org.databene.commons.ArrayUtil;
import org.databene.commons.Context;
import org.databene.commons.context.DefaultContext;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 * Provides context informations and operations for XML parsing.<br/><br/>
 * Created: 05.12.2010 12:09:54
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class ParseContext<E> implements Context {
	
	protected XMLElementParserFactory<E> factory;
	protected Class<E> pathComponentType;
	private Context context;
	
	public ParseContext(Class<E> pathComponentType) {
		this(pathComponentType, new XMLElementParserFactory<E>());
	}

	public ParseContext(Class<E> pathComponentType, XMLElementParserFactory<E> factory) {
		this.pathComponentType = pathComponentType;
		this.factory = factory;
		this.context = new DefaultContext();
	}

	public void addParser(XMLElementParser<E> parser) {
		factory.addParser(parser);
	}

	public E parseElement(Element element, E[] parentPath) {
		XMLElementParser<E> parser = factory.getParser(element, parentPath);
		return parser.parse(element, parentPath, this);
	}
	
	public List<E> parseChildElementsOf(Element element, E[] currentPath) {
		List<E> result = new ArrayList<E>();
		for (Element childElement : XMLUtil.getChildElements(element))
			result.add(parseChildElement(childElement, currentPath));
		return result;
	}
	
	public E parseChildElement(Element childElement, E[] currentPath) {
		return parseElement(childElement, currentPath);
	}

	@SuppressWarnings("unchecked")
	public E[] createSubPath(E[] parentPath, E currentObject) {
		if (parentPath == null)
			return ArrayUtil.buildObjectArrayOfType(pathComponentType, currentObject);
		else
			return ArrayUtil.append(currentObject, parentPath);
	}

	@Override
	public Object get(String key) {
		return context.get(key);
	}

	@Override
	public void set(String key, Object value) {
		context.set(key, value);
	}

	@Override
	public void remove(String key) {
		context.remove(key);
	}

	@Override
	public boolean contains(String key) {
		return context.contains(key);
	}

	@Override
	public Set<String> keySet() {
		return context.keySet();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return context.entrySet();
	}

}
