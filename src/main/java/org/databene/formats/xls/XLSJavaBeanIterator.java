/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
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

package org.databene.formats.xls;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.databene.commons.Assert;
import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;
import org.databene.commons.context.DefaultContext;
import org.databene.commons.converter.PropertyArray2JavaBeanConverter;
import org.databene.commons.converter.util.ClassProvider;
import org.databene.commons.converter.util.ConstantClassProvider;
import org.databene.commons.converter.util.ReferenceResolver;
import org.databene.formats.DataContainer;
import org.databene.formats.script.ScriptConverterForStrings;
import org.databene.formats.util.ConvertingDataIterator;

/**
 * Iterates XLS sheets and provides each row as JavaBean instance.<br/><br/>
 * Created: 18.09.2014 15:49:49
 * @since 1.0.0
 * @author Volker Bergmann
 */

public class XLSJavaBeanIterator extends ConvertingDataIterator<Object[], Object> {
	
	private String uri;
	private boolean formatted;
	
	public XLSJavaBeanIterator(String uri, String sheetName, boolean formatted, Class<?> beanClass) throws IOException, InvalidFormatException {
		this(uri, sheetName, formatted, new ConstantClassProvider<Object[]>(beanClass));
	}

	public XLSJavaBeanIterator(String uri, String sheetName, boolean formatted, ClassProvider<Object[]> beanClassProvider) 
			throws IOException, InvalidFormatException {
		super(null, null);
		this.uri = uri;
		this.formatted = formatted;
		Converter<String, ?> scriptConverter = new ScriptConverterForStrings(new DefaultContext());
		XLSLineIterator iterator = new XLSLineIterator(uri, sheetName, true, formatted, scriptConverter);
		String[] headers = iterator.getHeaders();
		Assert.notEmpty(headers, "Empty XLS sheet '" + sheetName + "' in document " + uri);
		this.source = iterator;
		this.converter = new PropertyArray2JavaBeanConverter(beanClassProvider, headers, new RefResolver());
	}
	
	public static List<Object> parseAll(String uri, String sheetName, boolean formatted, Class<?> beanClass) 
			throws InvalidFormatException, IOException {
		return parseAll(uri, sheetName, formatted, new ConstantClassProvider<Object[]>(beanClass));
	}

	public static List<Object> parseAll(String uri, String sheetName, boolean formatted, ClassProvider<Object[]> beanClassProvider) 
			throws InvalidFormatException, IOException {
		XLSJavaBeanIterator iterator = new XLSJavaBeanIterator(uri, sheetName, formatted, beanClassProvider);
		List<Object> result = new ArrayList<Object>();
		DataContainer<Object> container = new DataContainer<Object>();
		while (iterator.next(container) != null)
			result.add(container.getData());
		return result;
	}

	public static Class<?> getFeatureComponentType(Class<?> ownerClass, String featureName) {
    	// try JavaBean property
        PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(ownerClass, featureName);
        if (propertyDescriptor != null) {
        	Method readMethod = propertyDescriptor.getReadMethod();
        	Class<?> returnType = readMethod.getReturnType();
			if (Collection.class.isAssignableFrom(returnType)) {
				ParameterizedType genericReturnType = (ParameterizedType) readMethod.getGenericReturnType();
				Type componentType = genericReturnType.getActualTypeArguments()[0];
				return (Class<?>) componentType;
        	} else {
        		return returnType;
        	}
        } else {
        	// try attribute
        	Field field = BeanUtil.getField(ownerClass, featureName);
        	if (field != null) {
    			if (Collection.class.isAssignableFrom(field.getType())) {
	        		ParameterizedType genericReturnType = (ParameterizedType) field.getGenericType();
	    			Type componentType = genericReturnType.getActualTypeArguments()[0];
	    			return (Class<?>) componentType;
    			} else {
    				return field.getType();
    			}
        	} else {
                throw new ConfigurationError("Feature '" + featureName + "' not found in class " + ownerClass.getName());
        	}
        }
	}

	class RefResolver implements ReferenceResolver {
		@Override
		public Object resolveReferences(Object value, Object target, String localFeatureName) {
			if (value instanceof String) {
				String text = (String) value;
				if (text.startsWith("tab:")) {
					String targetSheetName = text.substring("tab:".length());
					try {
						Class<?> targetType = getFeatureComponentType(target.getClass(), localFeatureName);
						return parseAll(uri, targetSheetName, formatted, targetType );
					} catch (Exception e) {
						throw new RuntimeException("Error parsing XLS sheet '" + targetSheetName + "' of " + uri, e);
					}
				}
			}
			return value;
		}
	}

}
