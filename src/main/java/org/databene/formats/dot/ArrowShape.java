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
package org.databene.formats.dot;

/**
 * Arrow type for dot graphs.
 * Created: 24.05.2014 12:18:19
 * @since 0.8.2
 * @author Volker Bergmann
 */

public enum ArrowShape {
	none, normal, empty, dot, odot, 
	diamond, ediamond, box, open, vee, 
	inv, invdot, invodot, tee, invempty, 
	odiamond, crow, obox, halfopen
}
