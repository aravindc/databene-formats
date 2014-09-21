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

import java.util.Collection;
import java.util.Map;

import org.databene.commons.Visitor;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Represents a {@link ComplexType} which composes other ComplexTypes.<br/><br/>
 * Created: 16.05.2014 19:59:51
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class CompositeComplexType extends ComplexType {
	
	private Map<String, ComplexMember> members;
	
	public CompositeComplexType(String name) {
		super(name);
		this.members = new OrderedNameMap<ComplexMember>();
	}
	
	public void addMember(ComplexMember member) {
		this.members.put(member.getName(), member);
	}
	
	public Collection<ComplexMember> getMembers() {
		return members.values();
	}
	
	@Override
	public void printContent(String indent) {
		System.out.println(indent + super.toString());
		indent += "  ";
		for (Attribute attribute : attributes.values())
			attribute.printContent(indent);
		for (ComplexMember member : members.values())
			member.printContent(indent);
	}

	@Override
	public void accept(Visitor<SchemaElement> visitor) {
		super.accept(visitor);
		for (ComplexMember type : members.values())
			type.accept(visitor);
	}

}
