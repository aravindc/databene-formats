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

import java.util.ArrayList;
import java.util.List;

import org.databene.commons.CollectionUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * {@link List}-based implementation of the {@link DataIterator} interface.<br/><br/>
 * Created: 08.12.2011 14:36:08
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class ListDataIterator<E> implements DataIterator<E> {
	
	private Class<E> type;
	private List<E> data;
	private int cursor;

	public ListDataIterator(Class<E> type, E... data) {
		this(type, CollectionUtil.toList(data));
	}

	public ListDataIterator(Class<E> type, List<E> data) {
		this.type = type;
		this.data = (data != null ? data : new ArrayList<E>());
		this.cursor = 0;
	}

	@Override
	public Class<E> getType() {
		return type;
	}

	@Override
	public DataContainer<E> next(DataContainer<E> container) {
		if (cursor >= data.size())
			return null;
		return container.setData(data.get(cursor++));
	}

	@Override
	public void close() {
		// nothing to do
	}

}
