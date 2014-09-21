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

/**
 * Represents a regular expression part composed of a SubPattern and a Quantifier.<br/>
 * <br/>
 * Created: 18.08.2006 19:11:31
 */
public class Factor implements RegexPart {
	
    /** The sub pattern */
    private RegexPart atom;
    
    /** The quantifier */
    private Quantifier quantifier;
    
    // constructors ----------------------------------------------------------------------------------------------------
    
    public Factor(RegexPart pattern) {
        this(pattern, 1, 1);
    }
    
    public Factor(RegexPart atom, int minQuantity, Integer maxQuantity) {
        this(atom, new Quantifier(minQuantity, maxQuantity));
    }
    
    public Factor(RegexPart atom, Quantifier quantifier) {
        this.atom = atom;
        this.quantifier = quantifier;
    }
    
    // properties ------------------------------------------------------------------------------------------------------
    
    /** Returns the atom */
    public RegexPart getAtom() {
        return atom;
    }
    
    /** Returns the represented quantifier */
    public Quantifier getQuantifier() {
        return quantifier;
    }
    
	@Override
	public int minLength() {
		return atom.minLength() * quantifier.getMin();
	}
	
	@Override
	public Integer maxLength() {
		Integer maxAtomLength = atom.maxLength();
		Integer maxQuantifier = quantifier.getMax();
		return (maxAtomLength != null && maxQuantifier != null ? maxAtomLength * maxQuantifier : null);
	}
    
    // java.lang.Object overrides --------------------------------------------------------------------------------------
	
    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Factor that = (Factor) o;
        return (this.atom.equals(that.atom) && 
        		that.quantifier.equals(that.quantifier));
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public int hashCode() {
        return 29 * atom.hashCode() + quantifier.hashCode();
    }
    
    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public String toString() {
        return atom.toString() + quantifier.toString();
    }
    
}
