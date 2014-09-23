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

import static org.junit.Assert.*;

import org.databene.formats.html.parser.DefaultHTMLTokenizer;
import org.databene.formats.html.parser.FilteringHTMLTokenizer;
import org.databene.formats.html.parser.HTMLTokenizer;
import org.databene.formats.html.util.HTMLTokenFilter;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;
import java.text.ParseException;

/**
 * Tests the {@link FilteringHTMLTokenizer}.<br/><br/>
 * Created: 16.06.2007 05:53:50
 * @since 0.1
 * @author Volker Bergmann
 */
public class FilteringHTMLTokenizerTest {

    private static final String HTML = "<html><body>Links<ul>" +
            "<li><a href='http://databene.org'>Great Tools</a></li>" +
            "<li><a href='http://bergmann-it.de'>Volker Bergmann</a></li>" +
            "</ul></body></html>";

    @Test
    public void testLinkIteration() throws IOException, ParseException {
        HTMLTokenizer source = new DefaultHTMLTokenizer(new StringReader(HTML));
        HTMLTokenFilter filter = new HTMLTokenFilter(HTMLTokenizer.START_TAG, "a");
        HTMLTokenizer tokenizer = new FilteringHTMLTokenizer(source, filter);

        tokenizer.nextToken();
        assertEquals(HTMLTokenizer.START_TAG, tokenizer.tokenType());
        assertEquals("a", tokenizer.name());
        assertEquals("http://databene.org", tokenizer.attributes().get("href"));

        tokenizer.nextToken();
        assertEquals(HTMLTokenizer.START_TAG, tokenizer.tokenType());
        assertEquals("a", tokenizer.name());
        assertEquals("http://bergmann-it.de", tokenizer.attributes().get("href"));

        assertEquals(HTMLTokenizer.END, tokenizer.nextToken());
    }

}
