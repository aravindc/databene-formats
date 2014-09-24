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
package org.databene.formats.properties;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.BeanUtil;
import org.databene.commons.bean.BeanToPropertyArrayConverter;
import org.databene.commons.converter.ArrayConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.ToStringConverter;
import org.databene.formats.script.AbstractScript;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;
import org.databene.formats.script.ScriptUtil;
import org.databene.formats.script.ScriptedDocumentWriter;

import java.io.IOException;
import java.io.Writer;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.text.FieldPosition;
import java.beans.PropertyDescriptor;

/**
 * Writes JavaBeans to property files.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 * @author Volker Bergmann
 */
public class BeanPropertiesFileWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanPropertiesFileWriter(Writer out, String ... propertyNames) {
        this(out, null, (Script)null, (Script)null, propertyNames);
    }

    public BeanPropertiesFileWriter(Writer out, String prefixPattern, String headerScriptUrl, String footerScriptUrl,
                                    String ... propertyNames)
            throws IOException {
        this(
            out,
            prefixPattern,
            (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null),
            (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null),
            propertyNames
        );
    }

    public BeanPropertiesFileWriter(Writer out, String prefixPattern, Script headerScript, Script footerScript,
                                    String ... propertyNames) {
        super( out, headerScript, new PartScript(prefixPattern, propertyNames), footerScript);
    }

    public static <T> void persist(T bean, String filename) throws IOException {
        FileWriter out = new FileWriter(filename);
        BeanPropertiesFileWriter<T> writer = new BeanPropertiesFileWriter<T>(out, getPropertyNames(bean.getClass()));
        writer.writeHeader();
        writer.writeElement(bean);
        writer.close();
        out.close();
    }

    private static String[] getPropertyNames(Class<?> beanType) {
        PropertyDescriptor[] descriptors = BeanUtil.getPropertyDescriptors(beanType);
        String[] names = new String[descriptors.length];
        for (int i = 0; i < descriptors.length; i++)
            names[i] = descriptors[i].getName();
        return names;
    }

    // scripts ---------------------------------------------------------------------------------------------------------

    private static class PartScript extends AbstractScript {

        private static final String LINE_SEPARATOR = SystemInfo.getLineSeparator();

        private MessageFormat prefixFormat;
        private String[] propertyNames;
        private Converter<Object, String[]> converter;

        private int elementCount;
        private StringBuffer buffer;
        FieldPosition pos0 = new FieldPosition(0);

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public PartScript(String prefixPattern, String ... propertyNames) {
            this.prefixFormat = (prefixPattern != null ? new MessageFormat(prefixPattern) : null);
            this.propertyNames = propertyNames;
            int length = propertyNames.length;
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++)
                propertyConverters[i] = new ToStringConverter();
            this.converter = new ConverterChain(
                new BeanToPropertyArrayConverter(propertyNames),
                new ArrayConverter(Object.class, String.class, propertyConverters)
            );
            this.elementCount = 0;
            this.buffer = new StringBuffer();
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                elementCount++;
                String[] cells = converter.convert(context.get("part"));
                String prefix = "";
                if (prefixFormat != null) {
                    prefixFormat.format(new Integer[] {elementCount}, buffer, pos0);
                    prefix = buffer.toString();
                    buffer.delete(0, buffer.length());
                }
                for (int i = 0; i < cells.length; i++) {
                    out.write(prefix);
                    out.write(propertyNames[i]);
                    out.write('=');
                    out.write(StringUtil.escape(cells[i]));
                    out.write(LINE_SEPARATOR);
                }
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }

    }
}
