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
package org.databene.formats.script;

import java.io.IOException;

import org.databene.commons.ParseException;

/**
 * Factory class for {@link Script} objects.<br/><br/>
 * @since 0.3.0
 * @author Volker Bergmann
 */
public interface ScriptFactory {
    Script readFile(String uri) throws ParseException, IOException;
    Script parseText(String text) throws ParseException;
}