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

import org.databene.commons.Assert;
import org.databene.commons.ProgrammerError;

/**
 * Compares arrays of objects using a {@link ComparisonModel}.<br/><br/>
 * Created: 20.11.2013 17:40:38
 * @since 1.0.5
 * @author Volker Bergmann
 */

public class ArrayComparator {
	
	private static final int IDENTICAL = 0;
	private static final int CHANGED   = 1;
	private static final int REMOVED   = 2;
	private static final int ADDED     = 3;
	
	public static ArrayComparisonResult compare(Object[] array1, Object[] array2, ComparisonModel model, String parentLocator, DiffFactory diffFactory) {
		return new ArrayComparator(array1, array2, model, parentLocator, diffFactory).compare();
	}
	
	private String parentLocator;
	private Object[] array1;
	private Object[] array2;
	private ComparisonModel model;
	private Match[] matches1;
	private Match[] matches2;
	private DiffFactory diffFactory;
		
	private ArrayComparator(Object[] array1, Object[] array2, ComparisonModel model, String parentLocator, DiffFactory diffFactory) {
		this.array1 = array1;
		this.array2 = array2;
		this.model = model;
		this.parentLocator = parentLocator;
		this.matches1 = new Match[array1.length];
		this.matches2 = new Match[array2.length];
		this.diffFactory = diffFactory;
	}
	
	private ArrayComparisonResult compare() {
		String objectType = "list element";
		
		// 1. step: match identical elements
		for (int i1 = 0; i1 < array1.length; i1++) {
			Object e1 = array1[i1];
			if (i1 < array2.length && model.equal(e1, array2[i1])) {
				// identical element found at the same index
				matches1[i1] = matches2[i1] = new Match(i1, i1, IDENTICAL);
			} else {
				int i2 = indexOf(e1, array2, matches2);
				if (i2 >= 0) {
					// identical element found at different index
					matches1[i1] = matches2[i2] = new Match(i1, i2, IDENTICAL);
				}
			}
		}
		
		// 2. step: Match similar elements and consider remainders in first array to be missing
		for (int i1 = 0; i1 < array1.length; i1++) {
			if (matches1[i1] == null) {
				Object e1 = array1[i1];
				int i2 = indexOfSimilar(e1, array2, matches2);
				if (i2 >= 0) {
					// store as changed
					matches1[i1] = matches2[i2] = new Match(i1, i2, CHANGED);
				} else {
					// store as removed
					matches1[i1] = new Match(i1, -1, REMOVED);
				}
			}
		}
		
		// 3. step: Consider remainders in second array to be added
		for (int i2 = 0; i2 < array2.length; i2++) {
			if (matches2[i2] == null) {
				// store as added
				matches2[i2] = new Match(-1, i2, ADDED);
			}
		}
		
		// 4. step: assemble comparison result
		ArrayComparisonResult result = new ArrayComparisonResult();
		int i1 = 0;
		int i2 = 0;
		while (i1 < array1.length || i2 < array2.length) {
			Match match1 = (i1 < matches1.length ? matches1[i1] : null);
			Match match2 = (i2 < matches2.length ? matches2[i2] : null);
			if (match1 != null && match1.type == REMOVED) {
				result.add(diffFactory.missing(array1[match1.i1], objectType, locator(array1, match1.i1)));
				match1.consume();
				i1 = nextUnconsumed(matches1, i1); 
			} else if (match2 != null && match2.type == ADDED) {
				result.add(diffFactory.unexpected(array2[match2.i2], objectType, locator(array2, match2.i2)));
				match2.consume();
				i2 = nextUnconsumed(matches2, i2);
			} else {
				Assert.notNull(match1, "match1");
				Assert.notNull(match2, "match2");
				switch (match1.type) {
					case CHANGED: 	if (match1.i1 != i1 || match1.i2 != i2)
										result.add(diffFactory.moved(array1[match1.i1], objectType, locator(array1, match1.i1), locator(array1, match1.i2)));
									result.add(diffFactory.different(array1[match1.i1], array2[match1.i2], objectType, locator(array1, match1.i1))); 
									break;
					case IDENTICAL:	if ((match1.i1 != match1.i2) && (match1.i1 != i1 || match1.i2 != i2))
										result.add(diffFactory.moved(array1[match1.i1], objectType, locator(array1, match1.i1), locator(array1, match1.i2)));
									break;
					default: 		throw new ProgrammerError();
				}
				match1.consume();
				matches2[match1.i2].consume();
				i1 = nextUnconsumed(matches1, i1);
				i2 = nextUnconsumed(matches2, i2);
			}
		}
		return result;
	}

	private static int nextUnconsumed(Match[] matches, int startIndex) {
		int index = startIndex;
		while (index < matches.length && matches[index].consumed) {
			index++;
		}
		return index;
	}

	private int indexOf(Object element, Object[] array, Match[] matches) {
		for (int i = 0; i < array.length; i++)
			if (matches[i] == null && model.equal(element, array[i]))
				return i;
		return -1;
	}
	
	private int indexOfSimilar(Object element, Object[] candidates, Match[] matches) {
		for (int i = 0; i < candidates.length; i++)
			if (matches[i] == null && model.correspond(element, candidates[i]))
				return i;
		return -1;
	}
	
	private String locator(Object[] array, int index) {
		return parentLocator + model.subPath(array, index);
	}
	
	
	static class Match {
		public int i1, i2;
		public int type;
		public boolean consumed;
		
		public Match(int i1, int i2, int type) {
			this.i1 = i1;
			this.i2 = i2;
			this.type = type;
			this.consumed = false;
		}
		
		void consume() {
			this.consumed = true;
		}
		
		@Override
		public String toString() {
			return type + " " + (i1 >= 0 ? String.valueOf(i1) : "") + " " + (i2 >= 0 ? String.valueOf(i2) : "");
		}
		
	}
	
}
