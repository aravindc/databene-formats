/*
 * (c) Copyright 2011-2014 by Volker Bergmann. All rights reserved.
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

import java.util.Iterator;

import org.databene.commons.HeavyweightIterator;
import org.databene.commons.IOUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Adapter class that provides Java-SDK-style {@link Iterator} access to a {@link DataIterator}.<br/><br/>
 * Created: 03.08.2011 19:04:58
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class JavaIteratorFromDataIterator<E> implements HeavyweightIterator<E> {
	
	private DataIterator<E> source;
	private DataContainer<E> next;
	private boolean initialized;
	
	public JavaIteratorFromDataIterator(DataIterator<E> source) {
		this.source = source;
		this.next = new DataContainer<E>();
		this.initialized = false;
	}

	@Override
	public boolean hasNext() {
		if (!initialized) {
			next = source.next(next);
			initialized = true;
		}
		return (next != null);
	}

	@Override
	public E next() {
		if (!hasNext())
			throw new IllegalStateException("Not available. Check hasNext() before calling next()");
		E result = next.getData();
		next = source.next(next);
		if (next == null)
			close();
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Iterator<E>.remove() is not supported");
	}

	@Override
	public void close() {
		IOUtil.close(source);
		source = null;
	}
	
	@Override
	public String toString() {
		return source.toString();
	}
	
}
