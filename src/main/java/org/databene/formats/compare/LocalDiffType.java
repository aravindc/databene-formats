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
package org.databene.formats.compare;

import org.databene.commons.ConfigurationError;
import org.databene.commons.NullSafeComparator;
import org.databene.commons.StringUtil;

/**
 * Assigns a DiffType with a locator for special comparison configurations.
 * Created: 08.07.2014 18:14:36
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class LocalDiffType {

	private final String locator;
	private final DiffDetailType type;

	public LocalDiffType(DiffDetailType type, String locator) {
		if (StringUtil.isEmpty(locator) && type == null)
			throw new ConfigurationError("At least one of the argument 'locator' and 'type' must be not empty");
		this.locator = locator;
		this.type = type;
	}

	public String getLocator() {
		return locator;
	}

	public DiffDetailType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int result = (type == null ? 1 : type.hashCode());
		return 31 * result + ((locator == null) ? 0 : locator.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		LocalDiffType that = (LocalDiffType) obj;
		return NullSafeComparator.equals(this.type, that.type) &&
				NullSafeComparator.equals(this.locator, that.locator);
	}

	@Override
	public String toString() {
		return (type != null ? type.toString() : "Any diff") + " for " + (locator != null ? " XPath " + locator : "any location");
	}

}
