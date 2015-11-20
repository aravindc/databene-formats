/*
 * Copyright (C) 2010-2014 Hamburg Sud and the contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.databene.formats.xml.compare;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.databene.commons.xml.XMLUtil;
import org.databene.commons.xml.XPathUtil;
import org.databene.formats.compare.AggregateDiff;
import org.databene.formats.compare.Diff;
import org.databene.formats.compare.DiffFactory;
import org.databene.formats.xml.compare.XMLComparator;
import org.databene.formats.xml.compare.XMLComparisonSettings;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Tests the {@link XmlContentImpl}
 * @author Volker Bergmann */
@SuppressWarnings("javadoc")
public class XMLComparatorTest {

    private static final String RESOURCE_PATH = XMLComparatorTest.class.getPackage().getName().replace('.', '/') + "/";
    
    private static final String SIMPLE_XML_PATH = RESOURCE_PATH + "simple.xml";
	private static final String SIMPLE_XML_ISO_8859_1_PATH = RESOURCE_PATH + "simple-iso-8859-1.xml";

    // TODO test character escaping

    @Test
    public void testDiff_identical() throws Exception {
        Document doc = parseSimpleXmlUtf8();
        assertTrue(new XMLComparator().compare(doc, doc).isEmpty());
    }

    // test encoding -----------------------------------------------------------
    
    @Test
    public void testDiff_unexpectedEncoding() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlIso_8859_1();
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setEncodingRelevant(true);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.different("utf-8", "iso-8859-1", "document encoding", "/"), diff.getDetails().get(0));
    }
    
    @Test
    public void testDiff_unexpectedEncodingTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlIso_8859_1();
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setEncodingRelevant(false);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }
    
    @Test
    public void testDiff_specialElementsIgnored() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = XMLUtil.parse(RESOURCE_PATH + "special.xml");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setProcessingInstructionRelevant(false);
        settings.setCdataRelevant(false);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }
    /*
    @Test
    public void testDiff_unexpectedProcessingInstruction() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = XMLUtil.parse(RESOURCE_PATH + "special.xml");
        ProcessingInstruction pi = null; // TODO retrieve processing instruction
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setProcessingInstructionRelevant(true);
        settings.setCdataRelevant(false);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.unexpected(pi, "processing instruction", "/root"), diff.getDetails().get(0));
    }
    
    @Test
    public void testDiff_missingProcessingInstruction() throws Exception {
    	Document expected = XMLUtil.parse(RESOURCE_PATH + "special.xml");
        Document actual = parseSimpleXmlUtf8();
        ProcessingInstruction pi = null; // TODO retrieve processing instruction
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setProcessingInstructionRelevant(true);
        settings.setCdataRelevant(false);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.missing(pi, "processing instruction", "/root"), diff.getDetails().get(0));
    }
    
    @Test
    public void testDiff_unexpectedCdata() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = XMLUtil.parse(RESOURCE_PATH + "special.xml");
        String cdata = "cdata_text"; 
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setProcessingInstructionRelevant(false);
        settings.setCdataRelevant(true);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.unexpected(cdata, "CDATA", "/root"), diff.getDetails().get(0));
    }
    
    @Test
    public void testDiff_missingCdata() throws Exception {
    	Document expected = XMLUtil.parse(RESOURCE_PATH + "special.xml");
        Document actual = parseSimpleXmlUtf8();
        String cdata = "cdata_text"; 
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.setProcessingInstructionRelevant(false);
        settings.setCdataRelevant(true);
		AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.missing(cdata, "CDATA", "/root"), diff.getDetails().get(0));
    }
    */
    // test attribute diffs ----------------------------------------------------

    @Test
    public void testDiff_otherAttributeVal() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().setAttribute("att", "val2");
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.different("val", "val2", "attribute", "/root/@att"), diff.getDetails().get(0));
    }

    @Test
    public void testDiff_otherAttributeValTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().setAttribute("att", "val2");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateDifferentAt("/root/@att");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    @Test
    public void testDiff_unexpectedAttribute() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().setAttribute("att2", "val2");
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.unexpected("val2", "attribute", "/root/@att2"), diff.getDetails().get(0));
    }

    @Test
    public void testDiff_UnexpectedAttributeTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().setAttribute("att2", "val2");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateUnexpectedAt("/root/@att2");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    @Test
    public void testDiff_missingAttribute() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().removeAttribute("att");
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        assertEquals(DiffFactory.missing("val", "attribute", "/root/@att"), diff.getDetails().get(0));
    }

    @Test
    public void testDiff_missingAttributeTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().removeAttribute("att");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateMissingAt("/root/@att");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    // test element diffs ------------------------------------------------------

    @Test
    public void testDiff_otherElementText() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        node(actual).setTextContent("otherText");
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        Diff<?> expectedDiff = DiffFactory.different("text", "otherText", "element text", "/root/node");
        assertEquals(expectedDiff, diff.getDetails().get(0));
    }

    @Test
    public void testDiff_otherElementTextTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        node(actual).setTextContent("otherText");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateDifferentAt("/root/node");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff: " + diff, diff.isEmpty());
    }

    @Test
    public void testDiff_missingElement() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().removeChild(node(actual));
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        Diff<?> expectedDiff = DiffFactory.missing(node(expected), "list element", "/root/node");
        Diff<?> actualDiff = diff.getDetails().get(0);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    public void testDiff_missingElementTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        actual.getDocumentElement().removeChild(node(actual));
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateMissingAt("/root/node");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff: " + diff, diff.isEmpty());
    }

    @Test
    public void testDiff_additionalElement() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        Element node2 = actual.createElement("node2");
        actual.getDocumentElement().appendChild(node2);
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        Diff<?> expectedDiff = DiffFactory.unexpected(node2, "list element", "/root/node2");
        Diff<?> actualDiff = diff.getDetails().get(0);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    public void testDiff_additionalElementTolerated() throws Exception {
        Document expected = parseSimpleXmlUtf8();
        Document actual = parseSimpleXmlUtf8();
        Element node2 = actual.createElement("node2");
        actual.getDocumentElement().appendChild(node2);
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateUnexpectedAt("/root/node2");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    @Test
    public void testDiff_listElementMoved() throws Exception {
        Document expected = XMLUtil.parse(RESOURCE_PATH + "list_1_alice_2_bob.xml");
        Element alice = XMLUtil.getChildElements(expected.getDocumentElement(), false, "item")[0];
        Document actual = XMLUtil.parse(RESOURCE_PATH + "list_2_bob_1_alice.xml");
        AggregateDiff diff = new XMLComparator().compare(expected, actual);
        assertEquals(1, diff.getDetails().size());
        Diff<?> expectedDiff = DiffFactory.moved(alice, "list element", "/list/item[1]", "/list/item[2]");
        Diff<?> actualDiff = diff.getDetails().get(0);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    public void testDiff_listElementMovedTolerated() throws Exception {
        Document expected = XMLUtil.parse(RESOURCE_PATH + "list_1_alice_2_bob.xml");
        Document actual = XMLUtil.parse(RESOURCE_PATH + "list_2_bob_1_alice.xml");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.tolerateMovedAt("/list/item");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    @Test
    public void testDiff_listMovedContentKeptItemNo() throws Exception {
        Document expected = XMLUtil.parse(RESOURCE_PATH + "list_1_alice_2_bob.xml");
        Document actual = XMLUtil.parse(RESOURCE_PATH + "list_1_bob_2_alice.xml");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.addKeyExpression("item", "@no");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(2, diff.getDetails().size());
        Diff<?> expectedDiff1 = DiffFactory.different("Alice", "Bob", "element text", "/list/item[1]");
        Diff<?> actualDiff1 = diff.getDetails().get(0);
        assertEquals(expectedDiff1, actualDiff1);
        Diff<?> expectedDiff2 = DiffFactory.different("Bob", "Alice", "element text", "/list/item[2]");
        Diff<?> actualDiff2 = diff.getDetails().get(1);
        assertEquals(expectedDiff2, actualDiff2);
    }

    @Test
    public void testDiff_listMovedItemNoKeptContent() throws Exception {
        Document expected = XMLUtil.parse(RESOURCE_PATH + "list_1_alice_2_bob.xml");
        Document actual = XMLUtil.parse(RESOURCE_PATH + "list_2_alice_1_bob.xml");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.addKeyExpression("item", "text()");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertEquals(3, diff.getDetails().size());
        Element alice = XPathUtil.queryElement(expected, "/list/item[position()=1]");
        Diff<?> expectedDiff1 = DiffFactory.moved(alice, "list element", "/list/item[1]", "/list/item[2]");
        assertEquals(expectedDiff1, diff.getDetails().get(0));
        Diff<?> expectedDiff2 = DiffFactory.different("1", "2", "attribute", "/list/item[1]/@no");
        assertEquals(expectedDiff2, diff.getDetails().get(1));
        Diff<?> expectedDiff3 = DiffFactory.different("2", "1", "attribute", "/list/item[2]/@no");
        assertEquals(expectedDiff3, diff.getDetails().get(2));
    }

    @Test
    public void testDiff_listTolerateMovedContentIgnoringItemNo() throws Exception {
        Document expected = XMLUtil.parse(RESOURCE_PATH + "list_1_alice_2_bob.xml");
        Document actual = XMLUtil.parse(RESOURCE_PATH + "list_1_bob_2_alice.xml");
        XMLComparisonSettings settings = new XMLComparisonSettings();
        settings.addKeyExpression("item", "text()");
        settings.tolerateMovedAt("//item");
        settings.tolerateDifferentAt("//item/@no");
        AggregateDiff diff = new XMLComparator(settings).compare(expected, actual);
        assertTrue("Unexpected diff", diff.isEmpty());
    }

    // private helpers ---------------------------------------------------------

    private static Document parseSimpleXmlUtf8() throws IOException {
		return XMLUtil.parse(SIMPLE_XML_PATH);
    }

    private static Document parseSimpleXmlIso_8859_1() throws IOException {
		return XMLUtil.parse(SIMPLE_XML_ISO_8859_1_PATH);
    }

    private static Element node(Document doc) {
        Element rootElement = doc.getDocumentElement();
        return (Element) rootElement.getElementsByTagName("node").item(0);
    }

}
