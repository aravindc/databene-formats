/*
 * (c) Copyright 2007 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.demo;

import org.databene.formats.html.HTML2XML;
import org.databene.formats.text.SplitStringConverter;
import org.databene.formats.xslt.XSLTTransformer;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.IOUtil;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.text.ParseException;

/**
 * This class demonstrates how to use the HTML2XML utility and the XSLTTransformer
 * for extracting data from a web page.<br/>
 * <br/>
 * Created: 16.06.2007 10:08:41
 * @author Volker Bergmann
 */
public class HTMLTextExtractorDemo {
    private static final String XSLT_FILENAME = "org/databene/formats/demo/HTMLTextExtractorDemo.xsl";

    public static void main(String[] args) throws IOException, ParseException, TransformerException, ConversionException {
        // Fetch the web page as string
        String html = IOUtil.getContentOfURI("http://www.yahoo.com");

        // convert the page content to XML
        String xml = HTML2XML.convert(html);

        // load the XSLT script and execute it on the XML text
        String xslt = IOUtil.getContentOfURI(XSLT_FILENAME);
        String xsltResult = XSLTTransformer.transform(xml, xslt);
        System.out.println("XSLT result: " + xsltResult);

        // split the list by its s separator token |
        Converter<String, String[]> converter = new SplitStringConverter('|');
        String[] headlines = converter.convert(xsltResult);
        System.out.println("Yahoo headlines :");
        System.out.println("- - - - - - - - -");
        for (int i = 0; i < headlines.length; i++)
            System.out.println((i+1) + ". " + headlines[i]);
    }
}
