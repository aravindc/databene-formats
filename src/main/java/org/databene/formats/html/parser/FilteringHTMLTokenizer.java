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
