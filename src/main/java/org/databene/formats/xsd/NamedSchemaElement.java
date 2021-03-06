/*
 * Copyright (C) 2011-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.databene.formats.xsd;

/**
 * Parent class for {@link SchemaElement}s that carry a name (attribute).
 * Created: 16.05.2014 19:30:31
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class NamedSchemaElement extends SchemaElement {

	protected String name;
	
	public NamedSchemaElement(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return renderNamePrefix() + getClass().getSimpleName() + renderShortDocumentation();
	}
	
	protected String renderNamePrefix() {
		return (name != null ? name + ": " : "");
	}
	
}
