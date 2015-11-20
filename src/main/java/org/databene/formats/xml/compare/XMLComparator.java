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

import java.io.IOException;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.databene.commons.NullSafeComparator;
import org.databene.commons.SystemInfo;
import org.databene.commons.xml.XMLUtil;
import org.databene.formats.compare.AggregateDiff;
import org.databene.formats.compare.ArrayComparator;
import org.databene.formats.compare.ArrayComparisonResult;
import org.databene.formats.compare.Diff;
import org.databene.formats.compare.DiffFactory;
import org.databene.formats.compare.DiffType;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * Compares two XML documents.<br/><br/>
 * Created: 16.11.2015 14:31:12
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class XMLComparator {

	private XMLComparisonSettings settings;

	public XMLComparator() {
		this(new XMLComparisonSettings());
	}
	
	public XMLComparator(XMLComparisonSettings settings) {
		this.settings = settings;
	}
	
	public void assertEquals(Document expected, Document actual) throws XPathExpressionException {
		AggregateDiff diffs = compare(expected, actual);
		if (diffs.getDetailCount() > 0) {
			String LF = SystemInfo.getLineSeparator();
			StringBuilder message = new StringBuilder("Documents do not match. Found " + diffs.getDetailCount() + " difference");
			if (diffs.getDetailCount() > 1)
				message.append('s');
			for (Diff<?> diff : diffs.getDetails())
				message.append(LF).append(diff);
			throw new AssertionError(message);
		}
	}
	
	public AggregateDiff compare(String uriOfExpected, String uriOfActual) throws IOException, XPathExpressionException {
		Document expectedDoc = XMLUtil.parse(uriOfExpected);
		Document actualDoc = XMLUtil.parse(uriOfActual);
		return compare(expectedDoc, actualDoc);
	}
	
	public AggregateDiff compare(Document expectedDocument, Document actualDocument) throws XPathExpressionException {
		NodeSettings nodeSettings = new NodeSettings(settings.getToleratedDiffs(), expectedDocument, actualDocument);
		AggregateDiff diffs = new AggregateDiff(expectedDocument, actualDocument, settings);
		// check encoding
		String expectedEncoding = expectedDocument.getInputEncoding();
		String actualEncoding = actualDocument.getInputEncoding();
		if (!NullSafeComparator.equals(expectedEncoding, actualEncoding) && settings.isEncodingRelevant())
			diffs.add(DiffFactory.different(expectedEncoding, actualEncoding, "document encoding", "/"));
		// check elements
		String rootNodeName = expectedDocument.getDocumentElement().getNodeName();
		return compareElements(expectedDocument.getDocumentElement(),
				actualDocument.getDocumentElement(), nodeSettings, "/" + rootNodeName, diffs);
	}
	
	
	
	// private helpers -------------------------------------------------------------------------------------------------

	AggregateDiff compareElements(Element expected, Element actual, NodeSettings nodeSettings, String parentPath, AggregateDiff diffs) {
		if (nodeSettings.isExcluded(expected)) // if this is an excluded node then return without checking
			return diffs;
		compareElementNames(expected, actual, parentPath, diffs);
		compareAttributes(expected, actual, nodeSettings, parentPath, diffs);
		compareChildElements(expected, actual, nodeSettings, parentPath, diffs);
		return diffs;
	}

	private static void compareElementNames(Element expected, Element actual, String parentPath, AggregateDiff diffs) {
		// assert equal node names
		String elementName = expected.getNodeName();
		expectEqualStrings(elementName, actual.getNodeName(), "Element name", parentPath, diffs);
	}

	private static void compareAttributes(Element expectedElement, Element actualElement, NodeSettings nodeSettings,
			String parentPath, AggregateDiff diffs) {
		// assert equal attributes
		// first check that each expected attribute exists and matches...
		NamedNodeMap expectedAttributes = expectedElement.getAttributes();
		for (int i = 0; i < expectedAttributes.getLength(); i++) {
			Attr expectedAttribute = (Attr) expectedAttributes.item(i);
			if (!nodeSettings.isExcluded(expectedAttribute))
				expectEqualAttribute(expectedAttribute, actualElement, nodeSettings, parentPath, diffs);
		}
		// ...then check that there do not exist additional ones
		NamedNodeMap actualAttributes = actualElement.getAttributes();
		for (int i = 0; i < actualAttributes.getLength(); i++) {
			Attr actualAttribute = (Attr) actualAttributes.item(i);
			Attr expectedAttribute = (Attr) expectedAttributes.getNamedItem(actualAttribute.getNodeName());
			if (expectedAttribute == null && !nodeSettings.isTolerated(DiffType.UNEXPECTED, expectedAttribute, actualAttribute)) {
				diffs.add(DiffFactory.unexpected(actualAttribute.getValue(), "attribute", attributePath(parentPath, actualAttribute)));
			}
		}
	}
	
	private void compareChildElements(Element expected, Element actual, NodeSettings nodeSettings, String parentPath,
			AggregateDiff diffs) {
		Element[] expectedChildElements = XMLUtil.getChildElements(expected);
		Element[] actualChildElements = XMLUtil.getChildElements(actual);
		if (expectedChildElements.length > 0 || actualChildElements.length > 0) {
			// if child elements exist, then compare them
			compareElementArraysExcludingXPaths(expectedChildElements, actualChildElements, nodeSettings, parentPath, diffs);
		} else {
			// ...otherwise compare text content
			//expectEqualStrings(expected.getTextContent(), actual.getTextContent(), "component values", parentPath, diffs);
			String expectedText = expected.getTextContent();
			String actualText = actual.getTextContent();
			String elementTextPath = elementTextPath(parentPath);
			if (!expectedText.equals(actualText) && !nodeSettings.isTolerated(DiffType.DIFFERENT, elementTextPath))
				diffs.add(DiffFactory.different(expectedText, actualText, "element text", elementTextPath));
		}
		Iterator<Diff<?>> iterator = diffs.getDetails().iterator();
		while (iterator.hasNext()) {
			Diff<?> diff = iterator.next();
			if (nodeSettings.isTolerated(diff.getType(), diff.getExpected(), diff.getActual()))
				iterator.remove();
		}
	}

	private void compareElementArraysExcludingXPaths(Element[] expectedElements, Element[] actualElements,
			NodeSettings tolerantNodes, String parentPath, AggregateDiff diffs) {
		ArrayComparisonResult<Element> result = ArrayComparator.compare(expectedElements, actualElements, settings.getModel(), parentPath);
		for (Diff<?> diff : result.getDiffs()) {
			if (diff.getType() == DiffType.DIFFERENT) {
				compareElements((Element) diff.getExpected(), (Element) diff.getActual(), tolerantNodes, String.valueOf(diff.getLocator2()), diffs);
			} else {
				diffs.add(diff);
			}
		}
	}

	private static void expectEqualStrings(String expectedValue, String actualValue, String type, String locator, AggregateDiff diffs) {
		if (!expectedValue.equals(actualValue))
			diffs.add(DiffFactory.different(expectedValue, actualValue, type, locator));
	}

	private static void expectEqualAttribute(Attr expectedAttribute, Element actualElement, NodeSettings nodeSettings, String parentPath, AggregateDiff diffs) {
		String attributeName = expectedAttribute.getName();
		Attr actualAttribute = actualElement.getAttributeNode(attributeName);
		String expectedAttValue = expectedAttribute.getValue();
		if (actualAttribute == null) {
			if (!nodeSettings.isTolerated(DiffType.MISSING, expectedAttribute, null)) {
				diffs.add(DiffFactory.missing(expectedAttValue, "attribute", attributePath(parentPath, expectedAttribute)));
			}
		} else {
			String actualAttValue = actualAttribute.getValue();
			if (!expectedAttValue.equals(actualAttValue) && !nodeSettings.isTolerated(DiffType.DIFFERENT, expectedAttribute, actualAttribute)) {
				diffs.add(DiffFactory.different(expectedAttValue, actualAttValue, "attribute", attributePath(parentPath, actualAttribute)));
			}
		}
	}

	private static String elementTextPath(String elementPath) {
		return elementPath /*+ "/text()" TODO */;
	}

	private static String attributePath(String parentPath, Attr attribute) {
		return parentPath + "/@" + attribute.getName();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + settings + "]";
	}

}
