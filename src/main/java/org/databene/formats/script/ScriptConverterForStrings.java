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
package org.databene.formats.script;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.converter.ThreadSafeConverter;

/**
 * {@link Converter} can recognize and resolve script expressions in strings.
 * @since 0.3.0
 * @author Volker Bergmann
 */
public class ScriptConverterForStrings extends ThreadSafeConverter<String, Object> {

    private Context context;
    
    public ScriptConverterForStrings(Context context) {
    	super(String.class, Object.class);
        this.context = context;
    }

	@Override
	public Object convert(String sourceValue) throws ConversionException {
		if (sourceValue != null)
			return ScriptUtil.evaluate((String) sourceValue, context);
		else
			return null;
	}

}
