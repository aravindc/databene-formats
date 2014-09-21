/*
 * (c) Copyright 2007-2009 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.text;

import org.databene.commons.StringUtil;
import org.databene.commons.ParseUtil;

import java.text.NumberFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Formats and parses numbers with abbreviations, e.g. 5Mio for 5,000,000.<br/>
 * <br/>
 * Created: 16.05.2005 21:41:17
 */
public class AbbreviatedNumberFormat extends NumberFormat {

	private static final long serialVersionUID = -3938256314974549704L;

	private Object [][] availableScales = new Object[][] {
        {"Mrd", new Double(1E9)},
        {"Mrd.", new Double(1E9)},

        {"Mio", new Double(1E6)},
        {"Mio.", new Double(1E6)},

        {"Tsd", new Double(1E3)},
        {"Tsd.", new Double(1E3)},
        {"T", new Double(1E3)},
    };

    private String defaultScaleId;
    private double defaultScale;
    private NumberFormat snf;

    public AbbreviatedNumberFormat() {
        this(1);
    }

    public AbbreviatedNumberFormat(double scale) {
        this(scale, Locale.getDefault());
    }

    public AbbreviatedNumberFormat(Locale locale) {
        this(1, locale);
    }

    public AbbreviatedNumberFormat(double scale, Locale locale) {
        defaultScale = scale;
        defaultScaleId = "";
        for (int i = 0; i < availableScales.length; i++) {
            if (((Double) availableScales[i][1]).doubleValue() == scale) {
                defaultScaleId = (String)availableScales[i][0];
                break;
            }
        }
        snf = NumberFormat.getInstance(locale);
        snf.setMinimumFractionDigits(2);
        snf.setMaximumFractionDigits(2);
        snf.setGroupingUsed(true);
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        if (!StringUtil.isEmpty(defaultScaleId))
            return formatFixed(number, toAppendTo, pos);
        else
            return formatFree(number, toAppendTo, pos);
    }

    public StringBuffer formatFixed(double number, StringBuffer toAppendTo, FieldPosition pos) {
        snf.format(number/defaultScale, toAppendTo, pos);
        if (!StringUtil.isEmpty(defaultScaleId)) {
            toAppendTo.append(' ');
            toAppendTo.append(defaultScaleId);
        }
        return toAppendTo;
    }

    private StringBuffer formatFree(double number, StringBuffer toAppendTo, FieldPosition pos) {
        String selectedPrefix = "";
        for (int i = 0; i < availableScales.length; i++) {
            double scale = ((Double)availableScales[i][1]).doubleValue();
            if (number >= scale) {
                selectedPrefix = (String) availableScales[i][0];
                number /= scale;
                snf.format(number, toAppendTo, pos);
                toAppendTo.append(' ');
                toAppendTo.append(selectedPrefix);
                return toAppendTo;
            }
        }
        return snf.format(number, toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return format((double) number, toAppendTo, pos);
    }

    @Override
    public Number parse(String source, ParsePosition pos) {
        Number value = snf.parse(StringUtil.trim(source), pos);
        int start = ParseUtil.nextNonWhitespaceIndex(source, pos.getIndex());
        if (start == -1)
            return value;
        for (int i = 0; i < availableScales.length; i++) {
            String prefix = (String) availableScales[i][0];
            if (source.substring(start).startsWith(prefix))  {
                value = new Double(value.doubleValue() * ((Double) availableScales[i][1]).doubleValue());
                pos.setIndex(start + prefix.length());
                break;
            }
        }
        return value;
    }
}
