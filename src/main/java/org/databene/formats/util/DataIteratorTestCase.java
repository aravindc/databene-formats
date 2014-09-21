/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.formats.util;

import java.util.HashSet;

import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Abstract parent class for tests that check {@link DataIterator}.<br/><br/>
 * Created: 24.07.2011 10:56:23
 * @since 0.6.0
 * @author Volker Bergmann
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class DataIteratorTestCase  {

    public static <T> void checkUniqueIteration(DataIterator<T> iterator, int count) {
    	HashSet<T> items = new HashSet<T>(count);
        for (int i = 0; i < count; i++) {
            T item = iterator.next(new DataContainer<T>()).getData();
            assert items.contains(item); // uniqueness check
            items.add(item);
        }
    }

	public static <T> NextHelper expectNextElements(DataIterator<?> iterator, T... expectedValues) {
		for (T expectedValue : expectedValues) {
			Object actualValue = iterator.next(new DataContainer()).getData();
			assert expectedValue.equals(actualValue);
		}
		return new NextHelper(iterator);
	}
	
	protected static void expectUnavailable(DataIterator<?> iterator) {
		assert iterator.next(new DataContainer()) == null;
	}

	public static class NextHelper {
		
		DataIterator<?> iterator;

		public NextHelper(DataIterator<?> iterator) {
			this.iterator = iterator;
		}
		
		public void withNext() {
			assert iterator.next(new DataContainer()) != null;
		}
		
		public void withNoNext() {
			expectUnavailable(iterator);
		}

	}
	
}
