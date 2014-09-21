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
