/*
 * (c) Copyright 2008-2013 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.script.freemarker;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Locale;

import org.databene.commons.IOUtil;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Creates {@link FreeMarkerScript}s.<br/><br/>
 * Created: 27.01.2008 16:47:21
 * @since 0.3.0
 * @author Volker Bergmann
 */
public class FreeMarkerScriptFactory implements ScriptFactory {

    private Configuration config;
    
    public FreeMarkerScriptFactory() {
        this(Locale.getDefault());
    }

    public FreeMarkerScriptFactory(Locale locale) {
        config = new Configuration();
        config.setClassForTemplateLoading(FreeMarkerScript.class, "/");
        config.setObjectWrapper(new DefaultObjectWrapper());
        config.setNumberFormat("0.##");
        config.setLocale(locale);
    }

    @Override
	public Script parseText(String text) {
        try {
            StringReader reader = new StringReader(text);
            Template template = new Template(text, reader, config);
            return new FreeMarkerScript(template);
        } catch (IOException e) {
            throw new RuntimeException(e); // This is not supposed to happen
        }
    }

    @Override
	public Script readFile(String uri) throws IOException {
    	InputStreamReader reader = new InputStreamReader(IOUtil.getInputStreamForURI(uri), Charset.forName("UTF-8"));
        Template template = new Template(null, reader, config);
        return new FreeMarkerScript(template);
    }

}
