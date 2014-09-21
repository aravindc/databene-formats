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

package org.databene.formats.regex;

import org.databene.commons.CharSet;

/**
 * Represents a constant character as part of a regular expression.<br/><br/>
 * Created: 04.04.2014 18:22:39
 * @since 0.8.0
 * @author Volker Bergmann
 */

public class RegexChar extends RegexCharClass {
	
	private char c;
	private CharSet charSet;

	public RegexChar(char c) {
		this.c = c;
		this.charSet = new CharSet().add(c);
	}

	public char getChar() {
		return c;
	}

	@Override
	public CharSet getCharSet() {
		return charSet;
	}
	
	@Override
	public int hashCode() {
		return c;
	}

	@Override
	public int minLength() {
		return 1;
	}

	@Override
	public Integer maxLength() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RegexChar that = (RegexChar) obj;
		return (this.c == that.c);
	}
	
	@Override
	public String toString() {
		return String.valueOf(c);
	}

}
