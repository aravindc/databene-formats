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

import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;
import org.databene.formats.DataSource;

/**
 * {@link DataSource} proxy which provides a subset of the source's data defined by an offset.<br/><br/>
 * Created: 24.07.2011 09:59:24
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class OffsetDataSource<E> extends DataSourceProxy<E> {

	protected int offset;
	
	public OffsetDataSource(DataSource<E> source, int offset) {
		super(source);
		this.offset = offset;
	}

	@Override
	public DataIterator<E> iterator() {
		DataContainer<E> container = new DataContainer<E>();
		DataIterator<E> result = super.iterator();
		for (int i = 0; i < offset; i++)
			result.next(container);
		return result;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + '[' + offset + ", " + source + ']';
	}
	
}
