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

import org.databene.commons.ArrayBuilder;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.StringUtil;
import org.databene.commons.xml.XMLUtil;
import org.databene.commons.xml.XPathUtil;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * XML comparison model based on element names.
 * Created: 03.06.2014 13:46:59
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class DefaultXMLComparisonModel extends AbstractXMLComparisonModel {
	
	private Map<String, String> keyExpressions;
	
	public DefaultXMLComparisonModel() {
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
	public boolean equal(Object o1, Object o2) {
		return equalNodes((Node) o1, (Node) o2);
	}
	
	@Override
	public boolean correspond(Object o1, Object o2) {
		Node n1 = (Node) o1;
		Node n2 = (Node) o2;
		if (n1 instanceof Text && n2 instanceof Text)
			return true;
		if (n1 instanceof Element && n2 instanceof Element) {
			Element e1 = (Element) n1;
			Element e2 = (Element) n2;
			String ln1 = e1.getLocalName();
			String ln2 = e2.getLocalName();
			if (ln1 != null) {
				if (!ln1.equals(ln2))
					return false;
			} else if (ln2 != null) {
				return false;
			}
			String key1 = keyOf(e1);
			if (key1 == null)
				return true;
			return (key1.equals(keyOf(e2)));
		}
		return (n1.getNodeName().equals(n2.getNodeName()));
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
	public String subPath(Object[] array, int index) {
		Node node = (Node) array[index];
		String name = name(node);
		if (name.length() == 0) {
			return "[" + (index + 1) + "]";
		} else {
			int nameOccurrences = 0;
			int indexAmongHomonyms = -1;
			for (int i = 0; i < array.length; i++) {
				if (name((Node) array[i]).equals(name)) {
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
	
	public boolean equalNodes(Node n1, Node n2) {
		// compare node names
		if (n1 instanceof Element && n2 instanceof Element) {
			if (!elementNamesMatch((Element) n1, (Element) n2))
				return false;
		} else if (!n1.getNodeName().equals(n2.getNodeName())) {
			return false;
		}
		
		// compare element attributes
		if (n1 instanceof Element) {
			if (n2 instanceof Element) {
				Element e1 = (Element) n1;
				Element e2 = (Element) n2;
				if (!XMLUtil.getAttributes(e1).equals(XMLUtil.getAttributes(e2)))
					return false;
			} else {
				return false;
			}
		}
		
		// compare child nodes
		Node[] c1 = childNodes(n1);
		Node[] c2 = childNodes(n2);
		if (c1.length != c2.length)
			return false;
		for (int i = 0; i < c1.length; i++)
			if (!equalNodes(c1[i], c2[i]))
				return false;
		return n1.getTextContent().equals(n2.getTextContent());
	}

	@Override
	public Node[] childNodes(Node parent) {
		if (!(parent instanceof Element))
			return new Node[0];
        NodeList childNodes = parent.getChildNodes();
        ArrayBuilder<Node> builder = new ArrayBuilder<Node>(Node.class);
        for (int i = 0; i < childNodes.getLength(); i++) {
        	Node child = childNodes.item(i);
        	if (child instanceof Element) {
        		builder.add(child);
        	} else if (child instanceof ProcessingInstruction) {
        		if (processingInstructionRelevant)
        			builder.add(child);
        	} else if (child instanceof CDATASection) {
        		if (cdataRelevant)
        			builder.add(child);
        		else
        			builder.add(parent.getOwnerDocument().createTextNode(child.getTextContent()));
        	} else if (child instanceof Text) {
        		if (StringUtil.trim(child.getTextContent()).isEmpty()) {
        			if (whitespaceRelevant) // white space
        				builder.add(child);
        		} else {
        			// text with content
        			builder.add(child);
        		}
        	} else if (child instanceof Comment) {
        		if (commentRelevant)
        			builder.add(child);
        	} else {
        		throw new UnsupportedOperationException("Unsupported node type: " + child.getClass().getName());
        	}
        }
        return builder.toArray();
    }

	private boolean elementNamesMatch(Element e1, Element e2) {
		String ln1 = e1.getLocalName();
		String ln2 = e2.getLocalName();
		if (ln1 != null) {
			if (!ln1.equals(ln2))
				return false;
		} else if (ln2 != null) {
			return false;
		}
		if (namespaceRelevant && !NullSafeComparator.equals(e1.getNamespaceURI(), e2.getNamespaceURI()))
			return false;
		return true;
	}
	
	private static String name(Node node) {
		return node.getNodeName();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [keyExpressions=" + keyExpressions + "]";
	}

}
