/*
 * (c) Copyright 2009-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.text;

import java.text.MessageFormat;

import org.databene.commons.ConversionException;
import org.databene.commons.Converter;
import org.databene.commons.StringUtil;
import org.databene.commons.converter.ThreadSafeConverter;

/**
 * {@link Converter} that transforms an object into its hexadecimal representation.
 * It works with integral numbers, characters and strings.<br/><br/>
 * Created: 29.10.2009 08:44:53
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class ToHexConverter extends ThreadSafeConverter<Object, String> {
	
	private boolean upperCase;
	private String pattern;
	private int length;

	public ToHexConverter() {
	    this(false);
    }

	public ToHexConverter(boolean upperCase) {
	    this(upperCase, null);
    }

	public ToHexConverter(boolean upperCase, String pattern) {
	    this(upperCase, pattern, -1);
    }

	public ToHexConverter(boolean upperCase, String pattern, int length) {
	    super(Object.class, String.class);
	    this.upperCase = upperCase;
	    this.pattern = pattern;
	    this.length = length;
    }

	@Override
	public String convert(Object sourceValue) throws ConversionException {
		if (sourceValue == null)
			return null;
		Class<?> sourceType = sourceValue.getClass();
		if (sourceType == Long.class)
			return convertLong((Long) sourceValue, upperCase, pattern, length);
		else if (sourceType == Integer.class)
			return convertInt((Integer) sourceValue, upperCase, pattern, length);
		else if (sourceType == Short.class)
			return convertShort((Short) sourceValue, upperCase, pattern, length);
		else if (sourceType == Byte.class)
			return convertByte((Byte) sourceValue, upperCase, pattern, length);
		else if (sourceType == Character.class)
			return convertChar((Character) sourceValue, upperCase, pattern, length);
		else if (sourceType == String.class)
			return convertString((String) sourceValue, upperCase, pattern, length);
		else
			throw new IllegalArgumentException("Can't render '" + sourceType + "' in hex format.");
    }

	public static String convertLong(Long sourceValue, boolean upperCase, String pattern, int length) {
	    String base = Long.toHexString(sourceValue);
	    return postProcess(base, upperCase, pattern, length);
    }

	public static String convertInt(int sourceValue, boolean upperCase, String pattern, int length) {
	    String base = Integer.toHexString(sourceValue);
	    return postProcess(base, upperCase, pattern, length);
    }

	public static String convertShort(short sourceValue, boolean upperCase, String pattern, int length) {
	    String base = Integer.toHexString(sourceValue);
	    if (base.length() == 8)
	    	base = base.substring(4);
	    return postProcess(base, upperCase, pattern, length);
    }

	public static String convertByte(byte sourceValue, boolean upperCase, String pattern, int length) {
	    String base = Integer.toHexString(sourceValue);
	    if (base.length() == 8)
	    	base = base.substring(6);
	    return postProcess(base, upperCase, pattern, length);
    }

	public static String convertChar(Character sourceValue, boolean upperCase, String pattern, int length) {
		String base = convertInt(sourceValue, upperCase, null, 2);
		return postProcess(base, upperCase, pattern, length);
    }

	public static String convertString(String sourceValue, boolean upperCase, String pattern, int length) {
		StringBuilder builder = new StringBuilder(sourceValue.length() * 2);
		for (int i = 0; i < sourceValue.length() ; i++)
			builder.append(convertChar(sourceValue.charAt(i), upperCase, null, 2));
		return postProcess(builder.toString(), upperCase, pattern, length);
    }

	private static String postProcess(String base, boolean upperCase, String pattern, int length) {
	    if (upperCase)
	    	base = base.toUpperCase();
	    if (length > 0)
	    	base = StringUtil.padLeft(base, length, '0');
	    if (pattern != null)
	    	base = MessageFormat.format(pattern, base);
	    return base;
    }

}
