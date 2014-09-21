/*
 * (c) Copyright 2008-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.script;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.converter.ConverterWrapper;

/**
 * {@link Converter} can recognize and resolve script expressions in {@link String} values,
 * forwarding values of other Java type 'as is'.<br/><br/>
 * Created: 07.08.2011 08:27:27
 * @since 0.5.9
 * @author Volker Bergmann
 */
public class ScriptConverterForObjects extends ConverterWrapper<String, Object> 
		implements Converter<Object, Object>{
    
    public ScriptConverterForObjects(Context context) {
    	super(new ScriptConverterForStrings(context));
    }

	@Override
	public Class<Object> getSourceType() {
		return Object.class;
	}

	@Override
	public Class<Object> getTargetType() {
		return Object.class;
	}

    @Override
	public Object convert(Object sourceValue) throws ConversionException {
    	// I might iterate through mixed sets of strings and numbers (e.g. from an XLS file)...
    	if (sourceValue instanceof String) //...so I only apply script evaluation on strings
    		return realConverter.convert((String) sourceValue);
    	else
    		return sourceValue;
    }

}
