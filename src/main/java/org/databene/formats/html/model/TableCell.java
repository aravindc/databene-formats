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
package org.databene.formats.html.model;

/**
 * Represents an HTML table cell.
 * Created: 06.01.2014 09:20:56
 * @since 0.7.1
 * @author Volker Bergmann
 */

public class TableCell extends HtmlElement<TableCell>{
	
	public TableCell(String text) {
		this(new TextComponent(text));
	}
	
	public TableCell(HtmlComponent... components) {
		super("td", false);
		withComponents(components);
	}
	
	public TableCell withColspan(int colspan) {
		return this.withAttribute("colspan", String.valueOf(colspan));
	}
	
}
