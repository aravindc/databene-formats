/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.StringUtil;

/**
 * Behaves like a {@link StringBuilder}, but normalizes all inserted line separators to a default.<br/><br/>
 * Created: 05.04.2010 08:54:42
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class LFNormalizingStringBuilder {

	private final StringBuilder builder;
	private final String lineSeparator;
	
	public LFNormalizingStringBuilder(String lineSeparator) {
	    this.lineSeparator = lineSeparator;
	    builder = new StringBuilder();
    }

    public String getLineSeparator() {
	    return lineSeparator;
    }
    
	public LFNormalizingStringBuilder append(String text) {
		builder.append(StringUtil.normalizeLineSeparators(text, lineSeparator));
		return this;
	}
	
	public LFNormalizingStringBuilder append(char c) {
		if (c != '\r' && c != '\n')
			builder.append(c);
		else
			builder.append(lineSeparator);
		return this;
	}
	
	@Override
	public String toString() {
	    return builder.toString();
	}
	
}
