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

package org.databene.formats.regex;

import java.util.List;
import java.util.Arrays;

/**
 * Represents an alternative expression part of a regular expression, e.g. '(yes|no)'.<br/>
 * <br/>
 * Created: 17.09.2006 16:14:14
 */
public class Choice implements RegexPart {
	
    /** The alternatives */
    private RegexPart[] alternatives;
    
    
    // constructors ----------------------------------------------------------------------------------------------------
    
    /** Constructor that takes a list of alternative patterns */
    public Choice(List<RegexPart> alternatives) {
    	RegexPart[] ra = new RegexPart[alternatives.size()];
        this.alternatives = alternatives.toArray(ra);
    }
    
    /** Constructor that takes an array of alternative patterns */
    public Choice(RegexPart... alternatives) {
        this.alternatives = alternatives;
    }
    
    
    // properties ------------------------------------------------------------------------------------------------------
    
    /** Returns the alternative patterns */
    public RegexPart[] getAlternatives() {
        return alternatives;
    }
    
    
    // RegexPart interface implementation ------------------------------------------------------------------------------
    
	@Override
	public int minLength() {
		if (this.alternatives.length == 0)
			return 0;
		int min = alternatives[0].minLength();
		for (int i = alternatives.length - 1; i >= 1; i--)
			min = Math.min(min, alternatives[i].minLength());
		return min;
	}

	@Override
	public Integer maxLength() {
		if (this.alternatives.length == 0)
			return 0;
		int max = 0;
		for (RegexPart candidate : alternatives) {
			Integer partMaxLength = candidate.maxLength();
			if (partMaxLength == null)
				return null; // if any option is unlimited, then the whole choice is unlimited
			if (partMaxLength > max)
				max = partMaxLength;
		}
		return max;
	}
	
	
    // java.lang.Object overrides --------------------------------------------------------------------------------------
    
    /** @see java.lang.Object#equals(Object) */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return Arrays.equals(alternatives, ((Choice)o).alternatives);
    }
    
    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return Arrays.hashCode(alternatives);
    }
    
    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("(");
        for (int i = 0; i < alternatives.length; i++) {
            if (i > 0)
                buffer.append('|');
            buffer.append(alternatives[i]);
        }
        buffer.append(')');
        return buffer.toString();
    }
    
}
