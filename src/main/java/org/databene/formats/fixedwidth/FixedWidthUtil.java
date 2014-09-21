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

import org.databene.commons.ParseUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.SyntaxError;
import org.databene.commons.format.Alignment;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Provides utility methods for processing flat files.<br/>
 * <br/>
 * Created: 03.09.2007 23:39:57
 * @author Volker Bergmann
 */
public class FixedWidthUtil {
	
    public static FixedWidthRowTypeDescriptor parseBeanColumnsSpec(String properties, String rowTypeName, String nullString, Locale locale) throws ParseException {
        if (properties == null)
            return null;
        String[] propertyFormats = StringUtil.tokenize(properties, ',');
        FixedWidthColumnDescriptor[] columns = new FixedWidthColumnDescriptor[propertyFormats.length];
        for (int i = 0; i < propertyFormats.length; i++) {
            String propertyFormat = propertyFormats[i];
            int lbIndex = propertyFormat.indexOf('[');
            if (lbIndex < 0)
                throw new ConfigurationError("'[' expected in property format descriptor '" + propertyFormat + "'");
            int rbIndex = propertyFormat.indexOf(']');
            if (rbIndex < 0)
                throw new ConfigurationError("']' expected in property format descriptor '" + propertyFormat + "'");
            String propertyName = propertyFormat.substring(0, lbIndex);
            String formatSpec = propertyFormat.substring(lbIndex + 1, rbIndex);
            FixedWidthColumnDescriptor descriptor = parseColumnFormat(formatSpec, nullString, locale);
            descriptor.setName(propertyName);
			columns[i] = descriptor;
        }
        return new FixedWidthRowTypeDescriptor(rowTypeName, columns);
    }
    
    public static FixedWidthRowTypeDescriptor parseArrayColumnsSpec(String columnsSpec, String rowTypeName, String nullString, Locale locale) throws ParseException {
        if (columnsSpec == null)
            return null;
        String[] columnFormats = StringUtil.tokenize(columnsSpec, ',');
        FixedWidthColumnDescriptor[] columns = new FixedWidthColumnDescriptor[columnFormats.length];
        for (int i = 0; i < columnFormats.length; i++)
            columns[i] = parseColumnFormat(columnFormats[i], nullString, locale);
        return new FixedWidthRowTypeDescriptor(rowTypeName, columns);
    }
    
	public static FixedWidthColumnDescriptor parseColumnFormat(String formatSpec, String nullString, Locale locale) throws ParseException {
		switch (formatSpec.charAt(0)) {
			case 'D': return parseDatePattern(formatSpec, nullString, locale);
			case 'N': return parseNumberPattern(formatSpec, nullString, locale);
			default : return parseWidthFormat(formatSpec, nullString);
		}
	}
	
	
	// private helpers -------------------------------------------------------------------------------------------------
	
	private static FixedWidthColumnDescriptor parseDatePattern(String formatSpec, String nullString, Locale locale) {
		if (!formatSpec.startsWith("D"))
			throw new SyntaxError("Illegal date/time pattern", formatSpec);
		String pattern = formatSpec.substring(1);
		Format format = new SimpleDateFormat(pattern, DateFormatSymbols.getInstance(locale));
		return new FixedWidthColumnDescriptor(null, format, nullString);
	}
	
	private static FixedWidthColumnDescriptor parseNumberPattern(String formatSpec, String nullString, Locale locale) {
		if (!formatSpec.startsWith("N"))
			throw new SyntaxError("Illegal number pattern", formatSpec);
		String pattern = formatSpec.substring(1);
		Format format = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
		return new FixedWidthColumnDescriptor(null, format, nullString);
	}
	
	private static FixedWidthColumnDescriptor parseWidthFormat(String formatSpec, String nullString) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        
        // parse width
        int width = (int) ParseUtil.parseNonNegativeInteger(formatSpec, pos);
        
        // parse fractionDigits
        NumberFormat format = null;
        int minFractionDigits = 0;
        int maxFractionDigits = 2;
        if (pos.getIndex() < formatSpec.length() && formatSpec.charAt(pos.getIndex()) == '.') {
            pos.setIndex(pos.getIndex() + 1);
            minFractionDigits = (int) ParseUtil.parseNonNegativeInteger(formatSpec, pos);
            maxFractionDigits = minFractionDigits;
            format = DecimalFormat.getInstance(Locale.US);
            format.setMinimumFractionDigits(minFractionDigits);
            format.setMaximumFractionDigits(maxFractionDigits);
            format.setGroupingUsed(false);
        }
        
        // parse alignment
        Alignment alignment = Alignment.LEFT;
        if (pos.getIndex() < formatSpec.length()) {
            char alignmentCode = formatSpec.charAt(pos.getIndex());
            switch (alignmentCode) {
                case 'l' : alignment = Alignment.LEFT; break;
                case 'r' : alignment = Alignment.RIGHT; break;
                case 'c' : alignment = Alignment.CENTER; break;
                default: throw new ConfigurationError("Illegal alignment code '" + alignmentCode + "' in format descriptor '" + formatSpec + "'");
            }
            pos.setIndex(pos.getIndex() + 1);
        }
        char padChar = ' ';
        if (pos.getIndex() < formatSpec.length()) {
            padChar = formatSpec.charAt(pos.getIndex());
            pos.setIndex(pos.getIndex() + 1);
        }
        if (pos.getIndex() != formatSpec.length())
        	throw new SyntaxError("Illegal column format", formatSpec);
        return new FixedWidthColumnDescriptor(null, format, nullString, width, alignment, padChar);
	}
	
}
