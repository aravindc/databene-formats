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
package org.databene.formats.csv;

import org.databene.commons.BeanUtil;
import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.bean.ArrayPropertyExtractor;
import org.databene.commons.bean.BeanToFeatureArrayConverter;
import org.databene.commons.converter.ArrayConverter;
import org.databene.commons.converter.ConverterChain;
import org.databene.commons.converter.ToStringConverter;
import org.databene.formats.script.AbstractScript;
import org.databene.formats.script.ConstantScript;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;
import org.databene.formats.script.ScriptedDocumentWriter;

import java.beans.PropertyDescriptor;
import java.io.Writer;
import java.io.IOException;

/**
 * Writes JavaBeans as CSV rows.
 * Created: 06.06.2007 19:35:29
 * @param <E> the type of the objects to write
 * @author Volker Bergmann
 */
public class BeanCSVWriter<E> extends ScriptedDocumentWriter<E> {

    public BeanCSVWriter(Writer out, char separator, Class<E> beanClass) {
        this(out, separator, true, defaultPropertyNames(beanClass));
    }

	public BeanCSVWriter(Writer out, char separator, String ... propertyNames) {
        this(out, separator, true, propertyNames);
    }

    public BeanCSVWriter(Writer out, char separator, boolean headed, String ... propertyNames) {
        this(   out,
                separator,
                (headed ? new ConstantScript(CSVUtil.formatHeaderWithLineFeed(separator, propertyNames)) : null),
                null,
                propertyNames);
    }

    public BeanCSVWriter(Writer out, char separator,
                         Script headerScript, Script footerScript, String ... propertyNames) {
        super(out, headerScript, new BeanCSVScript(propertyNames, separator), footerScript);
    }

    private static <T> String[] defaultPropertyNames(Class<T> beanClass) {
    	PropertyDescriptor[] descriptors = BeanUtil.getPropertyDescriptors(beanClass);
    	return ArrayPropertyExtractor.convert(descriptors, "name", String.class);
	}

    // BeanCSVScript ---------------------------------------------------------------------------------------------------

    private static class BeanCSVScript extends AbstractScript {

        private char separator;
        private Converter<Object, String[]> converter;

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public BeanCSVScript(String[] propertyNames, char separator) {
            this.separator = separator;
            int length = propertyNames.length;
            Converter[] propertyConverters = new Converter[length];
            for (int i = 0; i < length; i++)
                propertyConverters[i] = new ToStringConverter();
            this.converter = new ConverterChain(
                new BeanToFeatureArrayConverter(propertyNames.clone()),
                new ArrayConverter(Object.class, String.class, propertyConverters)
            );
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                String[] cells = converter.convert(context.get("part"));
                CSVUtil.writeRow(out, separator, cells);
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }

    }
}
