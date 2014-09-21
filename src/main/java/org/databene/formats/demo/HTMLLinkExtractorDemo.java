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

import org.databene.formats.html.parser.DefaultHTMLTokenizer;
import org.databene.formats.html.parser.FilteringHTMLTokenizer;
import org.databene.formats.html.parser.HTMLTokenizer;
import org.databene.formats.html.util.HTMLTokenFilter;
import org.databene.commons.IOUtil;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;

/**
 * This class demonstrates how to use the HTMLTokenizer for extracting all link targets of a web page.<br/>
 * <br/>
 * Created: 16.06.2007 10:07:54
 * @author Volker Bergmann
 */
public class HTMLLinkExtractorDemo {

    public static void main(String[] args) throws IOException, ParseException {
        // Fetch the web page as stream
        Reader reader = IOUtil.getReaderForURI("http://www.yahoo.com");
        // build the filtering iterator structure
        HTMLTokenizer tokenizer = new DefaultHTMLTokenizer(reader);
        tokenizer = new FilteringHTMLTokenizer(tokenizer, new HTMLTokenFilter(HTMLTokenizer.START_TAG, "a"));
        // simply iterate the filter to retrieve all references of the page
        while (tokenizer.nextToken() != HTMLTokenizer.END)
            System.out.println(tokenizer.attributes().get("href"));
        // free resources
        reader.close();
    }
}
