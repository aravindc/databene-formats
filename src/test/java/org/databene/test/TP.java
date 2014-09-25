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
package org.databene.test;

/**
 * JavaBean for testing.<br/><br/>
 * Created: 16.06.2007 06:52:56
 * @author Volker Bergmann
 */
public final class TP {

    private String name;

    public TP() {
        this("Carl");
    }

    public TP(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("static-method")
	public int getAge() { return 48; }
    
}