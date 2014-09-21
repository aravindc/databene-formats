/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.databene.commons.ArrayUtil;

/**
 * Renders a numerical value applying quantitative Symbols like 5 K for 5000.<br/><br/>
 * Created: 13.12.2012 14:02:39
 * @since 0.5.21
 * @author Volker Bergmann
 */
public class KiloFormatter {
	
	public static final int BASE_1000 = 1000;
	public static final int BASE_1024 = 1024;
	
	public static final int DEFAULT_BASE = BASE_1000;
	public static final String[] SYMBOLS = { "", "K", "M", "G", "T", "E" };
	
	private int base;
	private char decimalSeparator;

	public KiloFormatter(int base) {
		this(base, Locale.getDefault());
	}

	public KiloFormatter(int base, Locale locale) {
		this.base = base;
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
		this.decimalSeparator = symbols.getDecimalSeparator();
	}

	public String format(long value) {
		return convert(value, base);
	}
	
	public String convert(long value, int base) {
		long threshold = 1;
		for (int i = 0; i < SYMBOLS.length; i++) {
			if (value < threshold * base)
				return formatNumber(value, threshold, SYMBOLS[i]);
			threshold *= base;
		}
		return formatNumber(value, threshold / base, ArrayUtil.lastElementOf(SYMBOLS));
	}

	private String formatNumber(long value, long threshold, String symbol) {
		long prefix = value / threshold;
		long postfix = (value - prefix * threshold + threshold / 20) * 10 / threshold;
		if (postfix >= 10) {
			prefix++;
			postfix -= 10;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		if (postfix != 0 && prefix / 10 == 0)
			builder.append(decimalSeparator).append(postfix);
		if (symbol.length() > 0)
			builder.append(' ').append(symbol);
		return builder.toString();
	}
	
}
