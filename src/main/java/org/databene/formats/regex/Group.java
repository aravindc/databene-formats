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

package org.databene.formats.regex;

/**
 * Represents a group in a regular expression, e.g. '(abc)'.<br/>
 * <br/>
 */
public class Group implements RegexPart {
	
    /** The regular sub expression */
    private RegexPart regex;
    
    /** Constructor that takes a sub expression */
    public Group(RegexPart regex) {
        this.regex = regex;
    }
    
    /** returns the sub expression */
    public RegexPart getRegex() {
        return regex;
    }
    
    
    // RegexPart interface implementation ------------------------------------------------------------------------------
    
	@Override
	public int minLength() {
		return regex.minLength();
	}

	@Override
	public Integer maxLength() {
		return regex.maxLength();
	}
	
	
    // java.lang.Object overrides --------------------------------------------------------------------------------------
    
    /** @see java.lang.Object#equals(Object) */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        return regex.equals(((Group)o).regex);
    }
    
    /** @see java.lang.Object#equals(Object) */
    @Override
    public int hashCode() {
        return regex.hashCode();
    }
    
    /** @see java.lang.Object#equals(Object) */
    @Override
    public String toString() {
        return "(" + regex + ")";
    }

}
