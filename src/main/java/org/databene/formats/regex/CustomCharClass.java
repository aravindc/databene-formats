/*
 * (c) Copyright 2009-2014 by Volker Bergmann. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.CharSet;

/**
 * Represents a custom character class with inclusions and exclusions.<br/>
 * <br/>
 * Created at 20.09.2009 11:18:28
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class CustomCharClass extends RegexCharClass {
	
	private List<RegexCharClass> inclusions;
	private List<RegexCharClass> exclusions;
	
	// constructors ----------------------------------------------------------------------------------------------------
	
	public CustomCharClass() {
		this(null);
	}
	
    public CustomCharClass(List<? extends RegexCharClass> includedSets) {
	    this(includedSets, null);
    }
    
    public CustomCharClass(List<? extends RegexCharClass> includedSets, List<? extends RegexCharClass> excludedSets) {
	    this.inclusions = new ArrayList<RegexCharClass>();
	    if (includedSets != null)
	    	this.inclusions.addAll(includedSets);
	    this.exclusions = new ArrayList<RegexCharClass>();
	    if (excludedSets != null)
	    	this.exclusions.addAll(excludedSets);
    }
    
    
    // interface -------------------------------------------------------------------------------------------------------
    
	@Override
	public CharSet getCharSet() {
		CharSet set = new CharSet();
		for (RegexCharClass inclusion : inclusions)
			set.addAll(inclusion.getCharSet().getSet());
		for (RegexCharClass exclusion : exclusions)
			set.removeAll(exclusion.getCharSet().getSet());
		return set;
	}
	
	public void addInclusion(RegexCharClass set) {
		this.inclusions.add(set);
	}
	
	public void addExclusions(RegexCharClass set) {
		this.exclusions.add(set);
	}
	
	public boolean hasInclusions() {
		return !inclusions.isEmpty();
	}
	
	
	// private helpers -------------------------------------------------------------------------------------------------
	
    private static void appendToString(List<?> objects, StringBuilder builder) {
	    for (Object object : objects)
	    	builder.append(object);
    }
    
    
	// java.lang.Object overrides --------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("[");
		appendToString(inclusions, result);
		if (!exclusions.isEmpty()) {
			result.append('^');
			appendToString(exclusions, result);
		}
		return result.append(']').toString();
	}
	
	@Override
	public int hashCode() {
		return 31 * exclusions.hashCode() + inclusions.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustomCharClass that = (CustomCharClass) obj;
		return (this.inclusions.equals(that.inclusions) && 
				this.exclusions.equals(that.exclusions));
	}

}