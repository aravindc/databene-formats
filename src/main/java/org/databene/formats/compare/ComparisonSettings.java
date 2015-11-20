/*
 * Copyright (C) 2013-2014 Volker Bergmann (volker.bergmann@bergmann-it.de).
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

import java.util.HashSet;
import java.util.Set;

import org.databene.commons.ProgrammerError;

/**
 * Defines comparison settings: Which elements to ignore in comparisons 
 * and which differences to tolerate.<br/><br/>
 * Created: 03.06.2014 15:39:25
 * @since 1.2
 * @author Volker Bergmann
 */

public class ComparisonSettings<E> {

	private ComparisonModel<E> model;
	private Set<LocalDiffType> toleratedDiffs;

	public ComparisonSettings(ComparisonModel<E> model) {
		this.model = model;
		this.toleratedDiffs = new HashSet<LocalDiffType>();
	}

	public ComparisonModel<E> getModel() {
		return model;
	}
	
	public void addKeyExpression(String elementName, String keyExpression) {
		if (model instanceof KeyExpressionHolder) {
			((KeyExpressionHolder) model).addKeyExpression(elementName, keyExpression);
		} else {
			throw new ProgrammerError("Class does not support key expressions: " + model.getClass().getSimpleName());
		}
	}

	public Set<LocalDiffType> getToleratedDiffs() {
		return toleratedDiffs;
	}

	public void tolerateDifferentAt(String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(DiffType.DIFFERENT, xPath));
	}

	public void tolerateMissingAt(String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(DiffType.MISSING, xPath));
	}

	public void tolerateUnexpectedAt(String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(DiffType.UNEXPECTED, xPath));
	}

	public void tolerateMovedAt(String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(DiffType.MOVED, xPath));
	}

	public void tolerateAnyDiffAt(String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(null, xPath));
	}

	public void tolerateGenericDiff(DiffType type, String xPath) {
		this.toleratedDiffs.add(new LocalDiffType(type, xPath));
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[model=" + model + ", toleratedDiffs=" + toleratedDiffs + "]";
	}

}
