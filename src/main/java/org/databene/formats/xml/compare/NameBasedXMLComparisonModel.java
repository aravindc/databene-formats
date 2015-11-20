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
package org.databene.formats.xml.compare;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.databene.commons.xml.XMLUtil;
import org.databene.commons.xml.XPathUtil;
import org.databene.formats.compare.KeyExpressionHolder;
import org.w3c.dom.Element;

/**
 * XML comparison model based on element names.<br/><br/>
 * Created: 03.06.2014 13:46:59
 * @since 1.2
 * @author Volker Bergmann
 */

public class NameBasedXMLComparisonModel implements XMLComparisonModel, KeyExpressionHolder {
	
	private Map<String, String> keyExpressions;
	
	public NameBasedXMLComparisonModel() {
		this.keyExpressions = new HashMap<String, String>();
	}
	
	@Override
	public void addKeyExpression(String elementName, String keyExpression) {
		this.keyExpressions.put(elementName, keyExpression);
	}
	
	public Map<String, String> getKeyExpressions() {
		return keyExpressions;
	}
	
	@Override
	public boolean equal(Element e1, Element e2) {
		return equalElements(e1, e2);
	}
	
	@Override
	public boolean correspond(Element e1, Element e2) {
		if (!e1.getNodeName().equals(e2.getNodeName()))
			return false;
		String key1 = keyOf(e1);
		if (key1 == null)
			return true;
		return (key1.equals(keyOf(e2)));
	}
	
	private String keyOf(Element element) {
		String keyExpression = keyExpressions.get(element.getNodeName());
		if (keyExpression == null)
			return null;
		try {
			return XPathUtil.queryString(element, keyExpression);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Illegal XPath: " + keyExpression);
		}
	}

	@Override
	public String subPath(Element[] array, int index) {
		Element element = array[index];
		String name = name(element);
		if (name.length() == 0) {
			return "[" + (index + 1) + "]";
		} else {
			int nameOccurrences = 0;
			int indexAmongHomonyms = -1;
			for (int i = 0; i < array.length; i++) {
				if (name(array[i]).equals(name)) {
					if (i == index)
						indexAmongHomonyms = i + 1;
					nameOccurrences++;
				}
			}
			String result = "/" + name;
			if (nameOccurrences > 1)
				result += "[" + indexAmongHomonyms + "]";
			return result;
		}
	}
	
	public static boolean equalElements(Element e1, Element e2) {
		if (!e1.getNodeName().equals(e2.getNodeName()))
			return false;
		if (!XMLUtil.getAttributes(e1).equals(XMLUtil.getAttributes(e2)))
			return false;
		Element[] c1 = XMLUtil.getChildElements(e1);
		Element[] c2 = XMLUtil.getChildElements(e2);
		if (c1.length != c2.length)
			return false;
		for (int i = 0; i < c1.length; i++)
			if (!equalElements(c1[i], c2[i]))
				return false;
		return e1.getTextContent().equals(e2.getTextContent());
	}
	
	private static String name(Element element) {
		return element.getNodeName();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [keyExpressions=" + keyExpressions + "]";
	}
	
}
