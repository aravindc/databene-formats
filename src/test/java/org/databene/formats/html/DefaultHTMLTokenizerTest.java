/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
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

package org.databene.formats.html;

import org.junit.Test;
import static junit.framework.Assert.*;

import java.io.*;
import java.text.ParseException;
import java.util.Map;
import java.util.HashMap;

import org.databene.commons.SystemInfo;
import org.databene.formats.html.parser.DefaultHTMLTokenizer;
import org.databene.formats.html.parser.HTMLTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests the {@link DefaultHTMLTokenizer}.<br/><br/>
 * Created: 25.01.2007 13:29:54
 * @since 0.1
 * @author Volker Bergmann
 */
public class DefaultHTMLTokenizerTest {

    private Logger logger = LoggerFactory.getLogger(DefaultHTMLTokenizerTest.class);

    private static final TestSetup DOCUMENT_TYPE = new TestSetup(
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">",
            new HT(HTMLTokenizer.DOCUMENT_TYPE, "DOCTYPE"));

    private static final TestSetup TEXT = new TestSetup(
            "1st. some Text!",
            new HT(HTMLTokenizer.TEXT, "1st. some Text!"));

    private static final TestSetup START_TAG = new TestSetup(
            "<x>",
            new HT(HTMLTokenizer.START_TAG, "x"));

    private static final TestSetup END_TAG = new TestSetup(
            "</x>",
            new HT(HTMLTokenizer.END_TAG, "x"));

    private static final TestSetup CLOSED_TAG = new TestSetup(
            "<x y z='0'/>",
            new HT(HTMLTokenizer.CLOSED_TAG, "x", "y", null, "z", "0"));

    private static final TestSetup COMMENT = new TestSetup(
            "<!--ab-c<s>d--ef</s>ghi-->",
            new HT(HTMLTokenizer.COMMENT, "<!--ab-c<s>d--ef</s>ghi-->"));

    private static final TestSetup PROCESSING_INSTRUCTION = new TestSetup(
            "<?xml version = \"1.0\" ?>",
            new HT(HTMLTokenizer.PROCESSING_INSTRUCTION, "xml", "version", "1.0"));

    private static final TestSetup SPACED_CLOSED_TAG = new TestSetup(
            "<x y z = '0' />",
            new HT(HTMLTokenizer.CLOSED_TAG, "x", "y", null, "z", "0"));

    private static final TestSetup TEXT_TAG = new TestSetup(
            "<b>Bold!!!</b>",
            new HT(HTMLTokenizer.START_TAG, "b"),
            new HT(HTMLTokenizer.TEXT, "Bold!!!"),
            new HT(HTMLTokenizer.END_TAG, "b")
    );
    private static final TestSetup TAG_TEXT_COMMENT = new TestSetup(
            "<x><y/>z<!--ab-c<s>d--ef</s>ghi-->z</x>",
            new HT(HTMLTokenizer.START_TAG, "x"),
            new HT(HTMLTokenizer.CLOSED_TAG, "y"),
            new HT(HTMLTokenizer.TEXT, "z"),
            new HT(HTMLTokenizer.COMMENT, "<!--ab-c<s>d--ef</s>ghi-->"),
            new HT(HTMLTokenizer.TEXT, "z"),
            new HT(HTMLTokenizer.END_TAG, "x")
    );

    private static final TestSetup SCRIPT =
        new TestSetup("<a href=javascript:addAbo(53,true,true,true,true,true,true,'GERMAN')>",
            new HT(HTMLTokenizer.START_TAG, "a", "href", "javascript:addAbo(53,true,true,true,true,true,true,'GERMAN')")
        );

    private static final TestSetup COMPARATION_TEXT =
        new TestSetup("a < 3 || a > 5",
            new HT(HTMLTokenizer.TEXT, "a < 3 || a > 5")
        );

    private static final TestSetup TAG_LIKE_TEXT =
        new TestSetup("< a >",
            new HT(HTMLTokenizer.TEXT, "< a >")
        );

    private static String SEP = SystemInfo.getLineSeparator();

    private static final TestSetup MIXED = new TestSetup(
            "<html>" + SEP +
            "\t<?XXX level=\"3\"?>" + SEP +
            "\t<!-- some comment -->" + SEP +
            "</html>",
            new HT(HTMLTokenizer.START_TAG, "html"),
            new HT(HTMLTokenizer.TEXT, SEP + '\t'),
            new HT(HTMLTokenizer.PROCESSING_INSTRUCTION, "XXX", "level", "3"),
            new HT(HTMLTokenizer.TEXT, SEP + '\t'),
            new HT(HTMLTokenizer.COMMENT, "<!-- some comment -->"),
            new HT(HTMLTokenizer.TEXT, SEP),
            new HT(HTMLTokenizer.END_TAG, "html")
    );


    private static final TestSetup[] TESTS = {
        DOCUMENT_TYPE,
        TEXT,
        START_TAG,
        END_TAG,
        CLOSED_TAG,
        COMMENT,
        PROCESSING_INSTRUCTION,

        TAG_LIKE_TEXT,
        COMPARATION_TEXT,
        SPACED_CLOSED_TAG,
        SCRIPT,
        TEXT_TAG,
        TAG_TEXT_COMMENT,
        MIXED
    };

    // testUnprefixed methods ----------------------------------------------------------------------------------------------------

    @Test
    public void testText() throws IOException, ParseException {
        for (TestSetup testString : TESTS)
            checkText(testString);
    }

    @Test
    public void testTokens() throws IOException, ParseException {
        for (TestSetup test : TESTS)
            checkTokens(test);
    }
/*
    public void testCommentText() throws IOException, ParseException {
        Reader source = new StringReader(COMMENT_HTML);
        HTMLReader reader = new HTMLReader(source);
        assertParts(reader,
                new HTMLStartTag("x"),
                new HTMLClosedTag("y"),
                new HTMLText("z"),
                new HTMLComment("ab-c<s>d--ef</s>ghi"),
                new HTMLText("z"),
                new HTMLEndTag("x")
        );
        reader.close();
    }
*/
    // private helpers -------------------------------------------------------------------------------------------------

    public void checkText(TestSetup test) throws IOException, ParseException {
        Reader in = new StringReader(test.html);
        Writer out = new StringWriter();
        HTMLTokenizer tokenizer = new DefaultHTMLTokenizer(in);
        while (tokenizer.nextToken() != HTMLTokenizer.END)
            out.write(tokenizer.text());
        out.close();
        in.close();
        assertEquals(test.html, out.toString());
    }

    public void checkTokens(TestSetup test) throws IOException, ParseException {
        logger.debug("checking: " + test.html);
        Reader in = new StringReader(test.html);
        Writer out = new StringWriter();
        HTMLTokenizer tokenizer = new DefaultHTMLTokenizer(in);
        for (HT token : test.tokens) {
            tokenizer.nextToken();
            assertEquals(token.type, tokenizer.tokenType());
            if (token.type == HTMLTokenizer.TEXT || token.type == HTMLTokenizer.COMMENT)
                assertEquals(token.name, tokenizer.text());
            else
                assertEquals(token.name, tokenizer.name());
            assertEquals(token.attributes, tokenizer.attributes());
        }
        assertEquals("Fond more tokens than expected for: " + test.html, HTMLTokenizer.END, tokenizer.nextToken());
        out.close();
        in.close();
    }
/*
    private void assertParts(HTMLReader reader, HTMLPart ... parts) throws IOException, ParseException {
        for (HTMLPart part : parts) {
            assertNotNull(part);
            assertEquals(part, reader.nextPart());
        }
    }
*/

    private static class TestSetup {
        public String html;
        public HT[] tokens;

        public TestSetup(String html, HT ... tokens) {
            this.html = html;
            this.tokens = tokens;
        }
    }

    private static class HT {

        public int type;
        public String name;
        public Map<String, String> attributes;
//        public String[] attributes;

        public HT(int type, String name, String ... attributes) {
            this.type = type;
            this.name = name;
            this.attributes = buildMap(attributes);
//            this.attributes = attributes;
        }

        private static Map<String, String> buildMap(String[] attributes) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < attributes.length; i += 2) {
                String key = attributes[i];
                String value = null;
                if (i < attributes.length - 1)
                    value = attributes[i + 1];
                map.put(key, value);
            }
            return map;
        }
/* These are never used
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            final HT ht = (HT) o;

            if (type != ht.type)
                return false;
            if (!name.equals(ht.name))
                return false;
            return attributes.equals(ht.attributes);
        }

        public int hashCode() {
            int result;
            result = type;
            result = 29 * result + name.hashCode();
            return result;
        }
*/
    }
}
