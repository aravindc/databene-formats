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
 * This class demonstrates how to use the HTMLTokenizer for extracting all link targets of a web page.
 * 
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
