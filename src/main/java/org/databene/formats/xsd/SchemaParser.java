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

import org.databene.commons.StringUtil;
import org.databene.commons.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Parses XML schemas and provides an representation by a {@link Schema} obejct.<br/><br/>
 * Created: 16.05.2014 18:29:01
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class SchemaParser {
	
	public Schema parse(Document document) {
		Element root = document.getDocumentElement();
		return parseSchema(root);
	}

	private Schema parseSchema(Element root) {
		Schema schema = new Schema();
		for (Element child : XMLUtil.getChildElements(root)) {
			String childName = child.getLocalName();
			if ("annotation".equals(childName))
				schema.setDocumentation(parseAnnotationDocumentation(child));
			else if ("simpleType".equals(childName))
				schema.addSimpleType(parseSimpleType(child));
			else if ("complexType".equals(childName))
				schema.addComplexType(parseComplexType(child, schema));
			else if ("element".equals(childName))
				schema.setMember(parseElement(child, schema));
			else
				throw new UnsupportedOperationException("Not a supported child of 'schema': " + childName);
		}
		return schema;
	}

	private static String parseAnnotationDocumentation(Element element) {
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getLocalName();
			if ("documentation".equals(childName))
				return child.getTextContent();
		}
		return null;
	}

	private static SimpleType parseSimpleType(Element element) {
		SimpleType simpleType = new SimpleType(element.getAttribute("name"));
		// TODO parse simple schema type info
		return simpleType;
	}

	private ComplexType parseComplexType(Element element, Schema schema) {
		Element simpleContent = XMLUtil.getChildElement(element, false, false, "simpleContent");
		if (simpleContent != null)
			return parseComplexTypeWithSimpleContent(element);
		Element sequence = XMLUtil.getChildElement(element, false, false, "sequence");
		if (sequence != null)
			return parseComplexTypeWithSequence(element, schema);
		throw new UnsupportedOperationException("Not a supported kind of 'complexType': " + element.getAttribute("name"));
	}

	private ComplexType parseComplexTypeWithSimpleContent(Element element) {
		String name = XMLUtil.getAttribute(element, "name", false);
		PlainComplexType type = new PlainComplexType(name);
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getLocalName();
			if ("simpleContent".equals(childName))
				parseSimpleContent(child, type);
			else if ("attribute".equals(childName))
				type.addAttribute(parseAttribute(child));
			else
				throw new UnsupportedOperationException("Not a supported child of '" + element.getNodeName() + "': " + childName);
		}
		return type;
	}

	private void parseSimpleContent(Element child, PlainComplexType type) {
		// TODO parse simple schema content
	}

	private ComplexType parseComplexTypeWithSequence(Element element, Schema schema) {
		String name = XMLUtil.getAttribute(element, "name", false);
		CompositeComplexType type = new CompositeComplexType(name);
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getLocalName();
			if ("sequence".equals(childName))
				parseSequence(child, type, schema);
			else if ("attribute".equals(childName))
				type.addAttribute(parseAttribute(child));
			else
				throw new UnsupportedOperationException("Not a supported child of '" + element.getNodeName() + "': " + childName);
		}
		return type;
	}

	private void parseSequence(Element element, CompositeComplexType complexType, Schema schema) {
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getLocalName();
			if ("element".equals(childName))
				complexType.addMember(parseElement(child, schema));
			else
				throw new UnsupportedOperationException("Not a supported child of 'element': " + childName);
		}
	}

	private static Attribute parseAttribute(Element child) {
		String name = XMLUtil.getAttribute(child, "name", true);
		return new Attribute(name);
	}

	private ComplexMember parseElement(Element element, Schema schema) {
		String name = element.getAttribute("name");
		ComplexMember member = new ComplexMember(name, null);
		ComplexType type = null;
		for (Element child : XMLUtil.getChildElements(element)) {
			String childName = child.getLocalName();
			if ("complexType".equals(childName))
				type = parseComplexType(child, schema);
			else if ("annotation".equals(childName))
				member.setDocumentation(parseAnnotationDocumentation(child));
			else
				throw new UnsupportedOperationException("Not a supported child of 'element': " + childName);
		}
		if (type == null) {
			String typeName = XMLUtil.getAttribute(element, "type", true);
			type = schema.getComplexType(typeName);
		}
		member.setType(type);
		member.setMinCardinality(parseOccursAttribute(element, "minOccurs"));
		member.setMaxCardinality(parseOccursAttribute(element, "maxOccurs"));
		return member;
	}
	
	private static int parseOccursAttribute(Element element, String name) {
		String stringValue = element.getAttribute(name);
		if (StringUtil.isEmpty(stringValue))
			return 1;
		else if ("unbounded".equals(stringValue))
			return -1;
		else
			return Integer.parseInt(stringValue);
	}
	
}
