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

import java.util.Map;

import org.databene.commons.Named;
import org.databene.commons.Visitor;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Represents an XML schema.<br/><br/>
 * Created: 16.05.2014 18:30:35
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class Schema extends SchemaElement implements Named {
	
	private Map<String, SimpleType> simpleTypes;
	private Map<String, ComplexType> complexTypes;
	private ComplexMember member;
	
	public Schema() {
		this.simpleTypes = new OrderedNameMap<SimpleType>();
		this.complexTypes = new OrderedNameMap<ComplexType>();
	}
	
	@Override
	public String getName() {
		return (member != null ? member.getName() : null);
	}

	public void addSimpleType(SimpleType simpleType) {
		this.simpleTypes.put(simpleType.getName(), simpleType);
	}

	public void addComplexType(ComplexType complexType) {
		this.complexTypes.put(complexType.getName(), complexType);
	}
	
	public ComplexType getComplexType(String name) {
		return complexTypes.get(name);
	}
	
	public ComplexMember getMember() {
		return member;
	}
	
	public void setMember(ComplexMember member) {
		this.member = member;
	}
	
	public void printContent() {
		System.out.println("Schema" + renderShortDocumentation());
		for (SimpleType simpleType : simpleTypes.values())
			simpleType.printContent("  ");
		for (ComplexType complexType : complexTypes.values())
			complexType.printContent("  ");
		member.printContent("  ");
	}
	
	@Override
	public void accept(Visitor<SchemaElement> visitor) {
		super.accept(visitor);
		for (SimpleType type : simpleTypes.values())
			type.accept(visitor);
		for (ComplexType type : complexTypes.values())
			type.accept(visitor);
		member.accept(visitor);
	}
	
}
