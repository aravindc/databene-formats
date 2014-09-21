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

package org.databene.formats;

import static org.junit.Assert.*;

import org.databene.formats.util.ListDataIterator;
import org.databene.formats.util.OrthogonalArrayIterator;
import org.junit.Test;

/**
 * Tests the {@link OrthogonalArrayIterator}.<br/><br/>
 * Created: 08.12.2011 14:33:30
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class OrthogonalArrayIteratorTest {

	@Test
	public void test() {
		DataIterator<Integer[]> source = new ListDataIterator<Integer[]>(Integer[].class, 
				new Integer[] { 1, 2 }, 
				new Integer[] { 3 });
		DataIterator<Integer[]> iterator = new OrthogonalArrayIterator<Integer>(source);
		DataContainer<Integer[]> container = new DataContainer<Integer[]>();
		assertArrayEquals(new Integer[] { 1,    3 }, iterator.next(container).getData());
		assertArrayEquals(new Integer[] { 2, null }, iterator.next(container).getData());
		assertNull(iterator.next(container));
	}
	
}
