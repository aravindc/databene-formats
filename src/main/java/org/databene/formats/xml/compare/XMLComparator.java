/*
 * Copyright (C) 2011-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
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

import static org.databene.formats.xml.compare.XMLComparisonModel.ATTRIBUTE;
import static org.databene.formats.xml.compare.XMLComparisonModel.DOCUMENT_ENCODING;
import static org.databene.formats.xml.compare.XMLComparisonModel.ELEMENT_NAME;
import static org.databene.formats.xml.compare.XMLComparisonModel.ELEMENT_NAMESPACE;
import static org.databene.formats.xml.compare.XMLComparisonModel.ELEMENT_TEXT;
import static org.databene.formats.xml.compare.XMLComparisonModel.PROCESSING_INSTRUCTION;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.xpath.XPathExpressionException;

import org.databene.commons.NullSafeComparator;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.converter.XMLNode2StringConverter;
import org.databene.commons.xml.XMLUtil;
import org.databene.formats.compare.AggregateDiff;
import org.databene.formats.compare.ArrayComparator;
import org.databene.formats.compare.ArrayComparisonResult;
import org.databene.formats.compare.DiffDetail;
import org.databene.formats.compare.DiffDetailType;
import org.databene.formats.compare.DiffFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * Compares two XML documents.
 * Created: 16.11.2015 14:31:12
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class XMLComparator {

	private XMLComparisonSettings settings;
	private DiffFactory diffFactory;

	public XMLComparator() {
		this(new XMLComparisonSettings());
	}
	
	public XMLComparator(XMLComparisonSettings settings) {
		this.settings = settings;
		this.diffFactory = new DiffFactory(new XMLNode2StringConverter());
	}
	
	public void assertEquals(Document expected, Document actual) throws XPathExpressionException {
		AggregateDiff diffs = compare(expected, actual);
		if (diffs.getDetailCount() > 0) {
			String LF = SystemInfo.getLineSeparator();
			StringBuilder message = new StringBuilder("Documents do not match. Found " + diffs.getDetailCount() + " difference");
			if (diffs.getDetailCount() > 1)
				message.append('s');
			for (DiffDetail diff : diffs.getDetails())
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
		// prepare comparison
		ComparisonContext context = new ComparisonContext(settings.getToleratedDiffs(), expectedDocument, actualDocument);
		settings.getModel().init(actualDocument, expectedDocument);
		AggregateDiff diffs = new AggregateDiff(expectedDocument, actualDocument, settings);
		
		// check encoding
		String expectedEncoding = expectedDocument.getInputEncoding();
		String actualEncoding = actualDocument.getInputEncoding();
		if (!NullSafeComparator.equals(expectedEncoding, actualEncoding) && settings.isEncodingRelevant())
			diffs.addDetail(diffFactory.different(expectedEncoding, actualEncoding, DOCUMENT_ENCODING, "/"));
		
		// check element tree
		String rootNodeName = expectedDocument.getDocumentElement().getNodeName();
		compareElements(expectedDocument.getDocumentElement(),
				actualDocument.getDocumentElement(), context, "/" + rootNodeName, diffs);
		
		return diffs;
	}
	
	
	
	// private helpers -------------------------------------------------------------------------------------------------

	AggregateDiff compareElements(Element expected, Element actual, ComparisonContext context, String parentPath, AggregateDiff diffs) {
		if (context.isExcluded(expected)) // if this is an excluded node then return without checking
			return diffs;
		compareElementNames(expected, actual, parentPath, diffs);
		compareAttributes(expected, actual, context, parentPath, diffs);
		compareChildNodes(expected, actual, context, parentPath, diffs);
		return diffs;
	}

	private void compareElementNames(Element expected, Element actual, String parentPath, AggregateDiff diffs) {
		// assert equal node names
		String elementName = expected.getLocalName();
		expectEqualStrings(elementName, actual.getLocalName(), ELEMENT_NAME, parentPath, diffs);
		if (settings.isNamespaceRelevant()) {
			String expectedNs = StringUtil.emptyToNull(expected.getNamespaceURI());
			String actualNs = StringUtil.emptyToNull(actual.getNamespaceURI());
			if (!NullSafeComparator.equals(expectedNs, actualNs)) {
				// TODO expectEqualStrings("'" + expectedNs + "'", "'" + actualNs + "'", ELEMENT_NAMESPACE, parentPath, diffs);
				diffs.addDetail(diffFactory.different(nsDescription(expectedNs), nsDescription(actualNs), ELEMENT_NAMESPACE, parentPath));
			}
		}
	}

	private static String nsDescription(String namespace) {
		return (namespace != null ? namespace : "none");
	}

	private void compareAttributes(Element expectedElement, Element actualElement, ComparisonContext context,
			String parentPath, AggregateDiff diffs) {
		// assert equal attributes
		// first check that each expected attribute exists and matches...
		NamedNodeMap expectedAttributes = expectedElement.getAttributes();
		for (int i = 0; i < expectedAttributes.getLength(); i++) {
			Attr expectedAttribute = (Attr) expectedAttributes.item(i);
			if (!isXmlnsAttribute(expectedAttribute) && !context.isExcluded(expectedAttribute))
				expectEqualAttribute(expectedAttribute, actualElement, context, parentPath, diffs);
		}
		// ...then check that there do not exist additional ones
		NamedNodeMap actualAttributes = actualElement.getAttributes();
		for (int i = 0; i < actualAttributes.getLength(); i++) {
			Attr actualAttribute = (Attr) actualAttributes.item(i);
			Attr expectedAttribute = (Attr) expectedAttributes.getNamedItem(actualAttribute.getNodeName());
			if (expectedAttribute == null && !isXmlnsAttribute(actualAttribute) && !context.isTolerated(DiffDetailType.UNEXPECTED, expectedAttribute, actualAttribute)) {
				diffs.addDetail(diffFactory.unexpected(actualAttribute.getValue(), ATTRIBUTE, attributePath(parentPath, actualAttribute)));
			}
		}
	}
	
	private static boolean isXmlnsAttribute(Attr attribute) {
		String name = attribute.getName();
		return "xmlns".equals(name) || name.startsWith("xmlns:");
	}

	private void compareChildNodes(Element expected, Element actual, ComparisonContext context, String parentPath,
			AggregateDiff diffs) {
		Node[] expectedChildNodes = settings.getModel().childNodes(expected);
		Node[] actualChildNodes = settings.getModel().childNodes(actual);
		if (expectedChildNodes.length > 0 || actualChildNodes.length > 0) {
			// if child elements exist, then compare them
			compareNodeArrays(expectedChildNodes, actualChildNodes, context, parentPath, diffs);
		}
		Iterator<DiffDetail> iterator = diffs.getDetails().iterator();
		while (iterator.hasNext()) {
			DiffDetail diff = iterator.next();
			if (context.isTolerated(diff.getType(), diff.getExpected(), diff.getActual()))
				iterator.remove();
		}
	}

	private void compareNodeArrays(Node[] expectedNodes, Node[] actualNodes,
			ComparisonContext context, String parentPath, AggregateDiff diffs) {
		ArrayComparisonResult result = ArrayComparator.compare(expectedNodes, actualNodes, settings.getModel(), parentPath, diffFactory);
		for (DiffDetail diff : result.getDiffs()) {
			if (diff.getType() == DiffDetailType.DIFFERENT && diff.getExpected() instanceof Element && diff.getActual() instanceof Element) {
				// if two elements differ in general, dive deeper in the comparison
				compareElements((Element) diff.getExpected(), (Element) diff.getActual(), context, String.valueOf(diff.getLocatorOfActual()), diffs);
			} else if (diff.getType() == DiffDetailType.DIFFERENT && diff.getExpected() instanceof Text && diff.getActual() instanceof Text) {
				handleTextDiff(diff, diffs, context);
			} else if (diff.getExpected() instanceof ProcessingInstruction || diff.getActual() instanceof ProcessingInstruction) {
				handleProcesingInstructionDiff(diff, diffs, context);
			} else {
				// normal div treatment
				diffs.addDetail(diff);
			}
		}
	}

	private void handleTextDiff(DiffDetail diff, AggregateDiff diffs, ComparisonContext context) {
		// special handling for text nodes
		String locatorOfActual = StringUtil.removeSuffixIfPresent("/#text", diff.getLocatorOfActual());
		if (!context.isTolerated(DiffDetailType.DIFFERENT, locatorOfActual))
			diffs.addDetail(diffFactory.different(diff.getExpected(), diff.getActual(), ELEMENT_TEXT, locatorOfActual));
	}

	private void handleProcesingInstructionDiff(DiffDetail diff, AggregateDiff diffs, ComparisonContext context) {
		// special handling for processing instructions
		ProcessingInstruction expectedPI = (ProcessingInstruction) diff.getExpected();
		ProcessingInstruction actualPI = (ProcessingInstruction) diff.getActual();
		String locatorOfExpected = procIntLocator(StringUtil.removeSuffixIfPresent("/procint", diff.getLocatorOfExpected()), expectedPI);
		String locatorOfActual = procIntLocator(StringUtil.removeSuffixIfPresent("/procint", diff.getLocatorOfActual()), actualPI);
		if (!context.isTolerated(diff.getType(), locatorOfExpected) && !context.isTolerated(diff.getType(), locatorOfActual)) {
			diffs.addDetail(diffFactory.genericDiff(expectedPI, actualPI, PROCESSING_INSTRUCTION, diff.getType(), locatorOfExpected, locatorOfActual));
		}
	}

	private void expectEqualStrings(String expectedValue, String actualValue, String type, String locator, AggregateDiff diffs) {
		if (!NullSafeComparator.equals(expectedValue, actualValue))
			diffs.addDetail(diffFactory.different(expectedValue, actualValue, type, locator));
	}

	private void expectEqualAttribute(Attr expectedAttribute, Element actualElement, ComparisonContext context, String parentPath, AggregateDiff diffs) {
		String attributeName = expectedAttribute.getName();
		Attr actualAttribute = actualElement.getAttributeNode(attributeName);
		String expectedAttValue = expectedAttribute.getValue();
		if (actualAttribute == null) {
			if (!context.isTolerated(DiffDetailType.MISSING, expectedAttribute, null)) {
				diffs.addDetail(diffFactory.missing(expectedAttValue, ATTRIBUTE, attributePath(parentPath, expectedAttribute)));
			}
		} else {
			String actualAttValue = actualAttribute.getValue();
			if (!expectedAttValue.equals(actualAttValue) && !context.isTolerated(DiffDetailType.DIFFERENT, expectedAttribute, actualAttribute)) {
				diffs.addDetail(diffFactory.different(expectedAttValue, actualAttValue, ATTRIBUTE, attributePath(parentPath, actualAttribute)));
			}
		}
	}

	private static String attributePath(String parentPath, Attr attribute) {
		return parentPath + "/@" + attribute.getName();
	}

	private static String procIntLocator(String parentPath, ProcessingInstruction pi) {
		return (parentPath != null && pi != null ? parentPath + "/?" + pi.getTarget() : null);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + settings + "]";
	}

}
