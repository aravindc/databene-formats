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

package org.databene.formats.xslt;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

/**
 * Performs XSL transformations on XML strings and streams.<br/>
 * <br/>
 * Created: 26.01.2007 08:31:09
 */
public class XSLTTransformer {

    private static final Map<String, Transformer> transformers = new HashMap<String, Transformer>();

    private static Transformer getTransformer(String xsltString) throws TransformerConfigurationException {
        Transformer transformer = transformers.get(xsltString);
        if (transformer == null) {
            Source xsltSource = new StreamSource(new StringReader(xsltString));
            transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            transformers.put(xsltString, transformer);
        }
        return transformer;
    }

    public static String transform(String xmlString, String xsltString) throws TransformerException {
        Reader source = new StringReader(xmlString);
        StringWriter writer = new StringWriter();
        transform(source, xsltString, writer);
        return writer.getBuffer().toString();
    }

    public static void transform(Reader reader, String xsltString, Writer writer) throws TransformerException {
        Transformer transformer = getTransformer(xsltString);
        transformer.transform(new StreamSource(reader), new StreamResult(writer));
    }

}
