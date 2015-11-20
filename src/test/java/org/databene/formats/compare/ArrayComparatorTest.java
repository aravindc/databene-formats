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

import static org.junit.Assert.*;

import java.util.List;

import org.databene.commons.CollectionUtil;
import org.databene.formats.compare.ArrayComparator;
import org.databene.formats.compare.ArrayComparisonResult;
import org.databene.formats.compare.ComparisonModel;
import org.databene.formats.compare.Diff;
import org.databene.formats.compare.DiffFactory;
import org.junit.Test;

/**
 * Tests the {@link ArrayComparator}.<br/><br/>
 * Created: 20.11.2013 19:08:28
 * @since 1.0
 * @author Volker Bergmann
 */

@SuppressWarnings("unchecked")
public class ArrayComparatorTest {
	
	@Test
	public void testIdenticalLists() {
		String[] l1 = new String[] { "A", "B", "C" };
		String[] l2 = new String[] { "A", "B", "C" };
		ArrayComparisonResult<String> result = ArrayComparator.compare(l1, l2, new StringComparisonModel(), "");
		assertTrue(result.identical());
	}
	
	@Test
	public void testEmptyLists() {
		String[] l1 = new String[] { };
		String[] l2 = new String[] { };
		ArrayComparisonResult<String> result = ArrayComparator.compare(l1, l2, new StringComparisonModel(), "");
		assertTrue(result.identical());
	}
	
	@Test
	public void testRemovedLast() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "A", "B" }, 
			DiffFactory.missing("C", "list element", "[2]")
		);
	}
	
	@Test
	public void testRemovedMiddle() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "A", "C" }, 
			DiffFactory.missing("B", "list element", "[1]")
		);
	}
	
	@Test
	public void testRemovedFirst() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "B", "C" }, 
			DiffFactory.missing("A", "list element", "[0]")
		);
	}
	
	@Test
	public void testAddedEnd() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "A", "B", "C", "X" }, 
			DiffFactory.unexpected("X", "list element", "[3]")
		);
	}
	
	@Test
	public void testAddedInBetween() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "A", "X", "B", "C" }, 
			DiffFactory.unexpected("X", "list element", "[1]")
		);
	}
	
	@Test
	public void testAddedBeginning() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "X", "A", "B", "C" }, 
			DiffFactory.unexpected("X", "list element", "[0]")
		);
	}
	
	@Test
	public void testSwappedNeighbours() {
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "A", "C", "B" }, 
			DiffFactory.moved("B", "list element", "[1]", "[2]")
		);
	}
	
	@Test
	public void testSwappedEnds() {
		String[] l1 = new String[] { "A", "B", "C" };
		String[] l2 = new String[] { "C", "B", "A" };
		ArrayComparisonResult<String> result = ArrayComparator.compare(l1, l2, new StringComparisonModel(), "");
		assertFalse(result.identical());
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "C", "B", "A" }, 
			DiffFactory.moved("A", "list element", "[0]", "[2]")
		);
	}
	
	@Test
	public void testRingChange() {
		String[] l1 = new String[] { "A", "B", "C" };
		String[] l2 = new String[] { "B", "C", "A" };
		ArrayComparisonResult<String> result = ArrayComparator.compare(l1, l2, new StringComparisonModel(), "");
		assertFalse(result.identical());
		check(
			new String[] { "A", "B", "C" }, 
			new String[] { "C", "B", "A" }, 
			DiffFactory.moved("A", "list element", "[0]", "[2]")
		);
	}
	
	@Test
	public void testChanged() {
		check(
			new String[] { "A", "B",  "C" }, 
			new String[] { "A", "B2", "C" }, 
			DiffFactory.different("B", "B2", "list element", "[1]")
		);
	}
	
	@Test
	public void testRemovedAndAdded() {
		check(
			new String[] { "A", "B",  "C" }, 
			new String[] { "A", "X", "C" }, 
			DiffFactory.missing("B", "list element", "[1]"),
			DiffFactory.unexpected("X", "list element", "[1]")
		);
	}
	
	@Test
	public void testMovedAndChanged() {
		check(
			new String[] { "A", "B",  "C" }, 
			new String[] { "A", "C", "B2" }, 
			DiffFactory.moved("B", "list element", "[1]", "[2]"),
			DiffFactory.different("B", "B2", "list element", "[1]")
		);
	}
	
	@Test
	public void testAllChangeTypes() {
		check(
				new String[] { "A", "B", "C", "D",  "E" }, 
				new String[] { "A", "X", "B", "D2", "C" }, 
				DiffFactory.unexpected("X", "list element", "[1]"),
				DiffFactory.moved("C", "list element", "[2]", "[4]"),
				DiffFactory.different("D", "D2", "list element", "[3]"),
				DiffFactory.missing("E", "list element", "[4]")
			);
	}
	
	
	// private helpers -------------------------------------------------------------------------------------------------
	
	private static void check(String[] list1, String[] list2, Diff<String>... expectedDiffs) {
		ArrayComparisonResult<String> result = ArrayComparator.compare(list1, list2, new StringComparisonModel(), "");
		if (expectedDiffs.length > 0)
			assertFalse(result.identical());
		else
			assertTrue(result.identical());
		List<Diff<String>> actualDiffs = result.getDiffs();
		for (Diff<?> diff : actualDiffs)
			System.out.println(diff);
		assertArrayEquals(expectedDiffs, CollectionUtil.toArray(actualDiffs));
	}
	
	static class StringComparisonModel implements ComparisonModel<String> {

		@Override
		public boolean equal(String o1, String o2) {
			return o1.equals(o2);
		}

		@Override
		public boolean correspond(String o1, String o2) {
			return (o1.charAt(0) == o2.charAt(0));
		}

		@Override
		public String subPath(String[] array, int index) {
			return "[" + index + "]";
		}
		
	}
	
}
