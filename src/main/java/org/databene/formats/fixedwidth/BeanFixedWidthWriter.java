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

package org.databene.formats.fixedwidth;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.SystemInfo;
import org.databene.commons.bean.BeanToPropertyArrayConverter;
import org.databene.commons.converter.ArrayConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.FormatFormatConverter;
import org.databene.commons.converter.ToStringConverter;
import org.databene.formats.script.AbstractScript;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;
import org.databene.formats.script.ScriptUtil;
import org.databene.formats.script.ScriptedDocumentWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes JavaBeans as flat file columns.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 * @author Volker Bergmann
 */
public class BeanFixedWidthWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanFixedWidthWriter(Writer out, FixedWidthColumnDescriptor ... descriptors) {
        this(out, (Script)null, (Script)null, descriptors);
    }

    public BeanFixedWidthWriter(Writer out, String headerScriptUrl, String footerScriptUrl,
                              FixedWidthColumnDescriptor ... descriptors)
            throws IOException {
        this(
            out,
            (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null),
            (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null),
            descriptors
        );
    }

    public BeanFixedWidthWriter(Writer out, Script headerScript, Script footerScript,
                              FixedWidthColumnDescriptor ... descriptors) {
        super( out, headerScript, new BeanFixedWidthScript(descriptors), footerScript);
    }

    // BeanFlatFileScript ----------------------------------------------------------------------------------------------

    private static class BeanFixedWidthScript extends AbstractScript {

        private Converter<Object, String[]> converter;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public BeanFixedWidthScript(FixedWidthColumnDescriptor[] descriptors) {
            int length = descriptors.length;
            String[] propertyNames = new String[length];
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++) {
                FixedWidthColumnDescriptor descriptor = descriptors[i];
                propertyNames[i] = descriptor.getName();
                propertyConverters[i] = new ConverterChain(
                    new ToStringConverter(),
                    new FormatFormatConverter(String.class, 
                            descriptor.getFormat(),
                            true
                            )
                );
            }
            this.converter = new ConverterChain(
                new BeanToPropertyArrayConverter(propertyNames),
                new ArrayConverter(Object.class, String.class, propertyConverters)
            );
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                String[] cells = converter.convert(context.get("part"));
                for (int i = 0; i < cells.length; i++)
                    out.write(cells[i]);
                out.write(SystemInfo.getLineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }
}
