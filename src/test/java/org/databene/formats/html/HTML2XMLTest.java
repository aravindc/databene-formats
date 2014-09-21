/*
 * (c) Copyright 2007-2014 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.Encodings;
import org.databene.commons.SystemInfo;
import org.databene.formats.html.HTML2XML;

/**
 * Tests the {@link HTML2XML} class.<br/><br/>
 * Created: 25.01.2007 17:31:28
 * @since 0.1
 * @author Volker Bergmann
 */
public class HTML2XMLTest {

    private static String SEP = SystemInfo.getLineSeparator();
    private static String XML_HEADER_ROW = 
    	"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + SEP;

    private static final String HTML1 = "<html>" + SEP +
            "\t<?XXX level=\"3\"?>" + SEP +
            "\t<!-- some comment -->" + SEP +
            "\t<tr>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<a></td>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<img src=\"http://databene.org\"></td>" + SEP +
            "\t\t<td>" + SEP +
            "\t\t\t<img></td>" + SEP +
            "\t</tr>" + SEP +
            "</html>";

    private static final String XML1 =
        XML_HEADER_ROW +
        "<html>" + SEP +
        "\t<?XXX level=\"3\"?>" + SEP +
        "\t<!-- some comment -->" + SEP +
        "\t<tr>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<a></a></td>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<img src=\"http://databene.org\"></img></td>" + SEP +
        "\t\t<td>" + SEP +
        "\t\t\t<img></img></td>" + SEP +
        "\t</tr>" + SEP +
        "</html>";

    private static final String HTML2 = 
    	"<a><img src=\"http://databene.org\"><br></a>";

    private static final String XML2 =
        XML_HEADER_ROW +
        "<html><a><img src=\"http://databene.org\"></img><br></br></a></html>";

    private static final String HTML3 = 
    	"R&B";

    private static final String XML3 =
        XML_HEADER_ROW +
        "<html>R&amp;B</html>";
    
    private static final String HTML4 = 
    	"<img src='image.php?s=1&amp;u=1&amp;type=sigpic&amp;dateline=1' a" +
    	"lt='<a href=http://www.xyz.com/image.php?type=pic&amp;userid=1&amp;dateline=1>" +
    	"http://www.xyz.com/image....ine=1</a>' border='0' />";
    
    private static final String XML4 =
        XML_HEADER_ROW +
        "<html><img src=\"image.php?s=1&amp;u=1&amp;type=sigpic&amp;dateline=1\" " +
        "alt=\"&lt;a href=http://www.xyz.com/image.php?type=pic&amp;userid=1&amp;dateline=1&gt;" +
        "http://www.xyz.com/image....ine=1&lt;/a&gt;\" border=\"0\"/></html>";
    
    
    // tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void testNormal() throws Exception {
		check(HTML1, XML1);
    }

    @Test
    public void testMissingHtml() throws Exception {
		check(HTML2, XML2);
    }

    @Test
    public void testAmpersand() throws Exception {
		check(HTML3, XML3);
    }
    
    @Test
    public void testXmlInAttribute() throws Exception {
		check(HTML4, XML4);
    }
    
    // helpers ---------------------------------------------------------------------------------------------------------

	private static void check(String source, String result) throws Exception {
		checkStrings(source, result);
		checkStreams(source, result);
	}

	private static void checkStreams(String source, String result) throws IOException,
			ParseException {
		StringReader in = new StringReader(source);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        HTML2XML.convert(in, out, Encodings.UTF_8);
        in.close();
        out.close();
		assertEquals(result, out.toString());
	}
	
	private static void checkStrings(String source, String result) 
			throws IOException, ParseException {
		assertEquals(result, HTML2XML.convert(source));
	}

}
