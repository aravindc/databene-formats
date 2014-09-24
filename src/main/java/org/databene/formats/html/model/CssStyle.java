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
 * Represents an inline CSS stylesheet.<br/><br/>
 * Created: 10.07.2014 13:43:42
 * @since 0.8.4
 * @author Volker Bergmann
 */

public class CssStyle extends HtmlElement<CssStyle> {

	public CssStyle() {
		super("style", false);
		withAttribute("type", "text/css");
	}

}
