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

package org.databene.formats.xsd;

import org.databene.commons.Visitor;

/**
 * Represents a member of a {@link ComplexType}, having a name, 
 * a type and a permitted cardinality range.<br/><br/>
 * Created: 16.05.2014 20:15:57
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class ComplexMember extends NamedSchemaElement {
	
	private ComplexType type;
	private Integer minCardinality;
	private Integer maxCardinality;
	
	public ComplexMember(String name, ComplexType complexType) {
		super(name);
		this.type = complexType;
	}
	
	public ComplexType getType() {
		return type;
	}
	
	public void setType(ComplexType complexType) {
		this.type = complexType;
	}
	
	public Integer getMinCardinality() {
		return minCardinality;
	}

	public void setMinCardinality(Integer minCardinality) {
		this.minCardinality = minCardinality;
	}

	public Integer getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(Integer maxCardinality) {
		this.maxCardinality = maxCardinality;
	}

	public void printContent(String indent) {
		System.out.println(indent + name + renderShortDocumentation() + ":");
		type.printContent(indent + "  ");
	}

	@Override
	public void accept(Visitor<SchemaElement> visitor) {
		super.accept(visitor);
		type.accept(visitor);
	}

}
