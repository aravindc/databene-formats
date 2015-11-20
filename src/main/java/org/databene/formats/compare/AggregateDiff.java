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
package org.databene.formats.compare;

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;

/**
 * Collects the differences between two data structures.<br/><br/>
 * Created: 19.06.2014 15:58:59
 * @since 1.2.2
 * @author Volker Bergmann
 */

public class AggregateDiff {
	
	private Object object1;
	private Object object2;
	private ComparisonSettings<?> comparisonSettings;
	private List<Diff<?>> details;
	
	public AggregateDiff(Object object1, Object object2, ComparisonSettings<?> comparisonSettings) {
		this.object1 = object1;
		this.object2 = object2;
		this.comparisonSettings = comparisonSettings;
		this.details = new ArrayList<Diff<?>>();
	}
	
	public Object getObject1() {
		return object1;
	}
	
	public Object getObject2() {
		return object2;
	}
	
	public ComparisonModel<?> getComparisonModel() {
		return comparisonSettings.getModel();
	}
	
	public ComparisonSettings<?> getComparisonSettings() {
		return comparisonSettings;
	}
	
	public boolean isEmpty() {
		return details.isEmpty();
	}
	
	public int getDetailCount() {
		return details.size();
	}
	
	public Diff<?> getDetail(int index) {
		return this.details.get(index);
	}
	
	public List<Diff<?>> getDetails() {
		return details;
	}
	
	public void add(Diff<?> diff) {
		this.details.add(diff);
	}

	@Override
	public String toString() {
		String LF = SystemInfo.getLineSeparator();
		StringBuilder builder = new StringBuilder("Aggregate diff:");
		if (details.isEmpty()) {
			builder.append(" Empty");
		} else {
			builder.append(LF);
			for (Diff<?> detail : details)
				builder.append("- ").append(StringUtil.normalizeSpace(detail.toString())).append(LF);
		}
		return builder.toString();
	}
	
}
