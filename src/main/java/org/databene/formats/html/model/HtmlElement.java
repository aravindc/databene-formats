/*
 * Copyright (C) 2011-2014 Volker Bergmann (volker.bergmann@bergmann-it.de).
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.databene.commons.SystemInfo;
import org.databene.commons.collection.OrderedNameMap;

/**
 * Parent class for HTML element classes.<br/><br/>
 * Created: 06.01.2014 08:13:00
 * @since 0.7.1
 * @author Volker Bergmann
 */

public class HtmlElement<E extends HtmlElement<?>> extends HtmlComponent {
	
	private String tagName;
	protected boolean inline;
	protected OrderedNameMap<String> attributes;
	private List<HtmlComponent> components;
	
	public HtmlElement(String tagName, boolean inline) {
		this.tagName = tagName;
		this.inline = inline;
		this.attributes = new OrderedNameMap<String>();
		this.components = new ArrayList<HtmlComponent>();
	}
	
	public E withClass(String klass) {
		return this.withAttribute("class", klass);
	}

	public E withStyle(String style) {
		return this.withAttribute("style", style);
	}
	
	public E withAlign(String align) {
		return withAttribute("align", align);
	}
	
	public E withValign(String valign) {
		return withAttribute("valign", valign);
	}
	
	@SuppressWarnings("unchecked")
	protected E withAttribute(String attributeName, String attributeValue) {
		attributes.put(attributeName, attributeValue);
		return (E) this;
	}
	
	public E addBreak() {
		return addComponent(HtmlFactory.br());
	}
	
	public E withRawTextContent(String text) {
		return withTextContent(text, false, false);
	}

	public E withTextContent(String text, boolean escape, boolean convertLinefeeds) {
		return this.withComponents(new TextComponent(text, escape, convertLinefeeds));
	}

	@SuppressWarnings("unchecked")
	public E withComponents(HtmlComponent... newComponents) {
		setComponents(newComponents);
		return (E) this;
	}

	public void setComponents(HtmlComponent... newComponents) {
		this.components.clear();
		for (HtmlComponent component : newComponents)
			addComponent(component);
	}

	public E addComponent(String textToAdd) {
		return addComponent(new TextComponent(textToAdd));
	}

	public void addComponents(HtmlComponent... components) {
		for (HtmlComponent component : components)
			addComponent(component);
	}
	
	@SuppressWarnings("unchecked")
	public E addComponent(HtmlComponent componentToAdd) {
		this.components.add(componentToAdd);
		return (E) this;
	}

	public String getTagName() {
		return tagName;
	}
	
	public boolean isInline() {
		return inline;
	}
	
	
	// rendering -------------------------------------------------------------------------------------------------------
	
	protected String formatStartTag() {
		StringBuilder builder = new StringBuilder();
		builder.append('<').append(tagName);
		for (Map.Entry<String, String> att : attributes.entrySet())
			builder.append(' ').append(att.getKey()).append("=\"").append(att.getValue()).append('"');
		builder.append('>');
		if (!inline)
			builder.append(SystemInfo.getLineSeparator());
		return builder.toString();
	}

	protected String formatComponents() {
		StringBuilder builder = new StringBuilder();
		for (HtmlComponent component : this.components)
			builder.append(component);
		return builder.toString();
	}
	
	protected String formatEndTag() {
		StringBuilder builder = new StringBuilder();
		if (!inline)
			builder.append(SystemInfo.getLineSeparator());
		builder.append("</").append(tagName).append('>');
		if (!inline)
			builder.append(SystemInfo.getLineSeparator());
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return formatStartTag() + formatComponents() + formatEndTag();
	}

}
