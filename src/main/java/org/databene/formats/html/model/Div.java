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

/**
 * Represents a &lt;div&gt; element.<br/><br/>
 * Created: 15.03.2014 07:05:25
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class Div extends HtmlElement<Div> {

	public Div() {
		this(false);
	}

	public Div(boolean inline) {
		super("div", inline);
	}
	
	public Div withTitle(String title) {
		return this.withAttribute("title", title);
	}

}
