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
package org.databene.formats.xml;

import org.w3c.dom.Element;

/**
 * Parent interface for classes that parse XML structures into Java objects.
 * Created: 05.12.2010 10:42:56
 * @param <E> the type of element to provide
 * @since 0.5.4
 * @author Volker Bergmann
 */
public interface XMLElementParser<E> {
	boolean supports(Element element, E[] parentPath);
	E parse(Element element, E[] parentPath, ParseContext<E> context);
}
