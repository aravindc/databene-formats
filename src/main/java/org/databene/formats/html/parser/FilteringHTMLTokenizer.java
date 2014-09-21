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

package org.databene.formats.html.parser;

import org.databene.commons.Filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * {@link HTMLTokenizer} proxy that returns only the tokens that match a {@link Filter}.<br/>
 * <br/>
 * Created: 16.06.2007 05:50:50
 * @author Volker Bergmann
 */
public class FilteringHTMLTokenizer implements HTMLTokenizer{

    private HTMLTokenizer source;
    private Filter<HTMLTokenizer> filter;

    public FilteringHTMLTokenizer(HTMLTokenizer source, Filter<HTMLTokenizer> filter) {
        this.source = source;
        this.filter = filter;
    }

    @Override
	public int nextToken() throws IOException, ParseException {
        int token;
        do {
            token = source.nextToken();
        } while (token != -1 && !filter.accept(source));
        return token;
    }

    @Override
	public int tokenType() {
        return source.tokenType();
    }

    @Override
	public String name() {
        return source.name();
    }

    @Override
	public String text() {
        return source.text();
    }

    @Override
	public Map<String, String> attributes() {
        return source.attributes();
    }
    
}
