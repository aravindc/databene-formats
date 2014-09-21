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

import org.databene.commons.ArrayUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Uses a {@link DataIterator} which provides data rows and forwards its data in form of columns.<br/><br/>
 * Created: 08.12.2011 13:45:37
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class OrthogonalArrayIterator<E> implements DataIterator<E[]> {

	private DataIterator<E[]> source;
	
	private List<E[]> rows;
	private int columnIndex;

	public OrthogonalArrayIterator(DataIterator<E[]> source) {
		this.source = source;
	}

	@Override
	public Class<E[]> getType() {
		return source.getType();
	}

	@Override
	public DataContainer<E[]> next(DataContainer<E[]> container) {
		beInitialized(container);
		if (rows.size() == 0 || columnIndex >= rows.get(0).length)
			return null;
		@SuppressWarnings("unchecked")
		E[] column = ArrayUtil.newInstance((Class<E>) source.getType().getComponentType(), rows.size());
		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			E[] row = rows.get(rowIndex);
			column[rowIndex] = (columnIndex < row.length ? row[columnIndex] : null);
		}
		columnIndex++;
		return container.setData(column);
	}

	@Override
	public void close() {
		// nothing to do
	}
	
	// private helpers -------------------------------------------------------------------------------------------------
	
	private void beInitialized(DataContainer<E[]> container) {
		if (rows == null) { // initialize on the first invocation
			rows = new ArrayList<E[]>();
			while (source.next(container) != null)
				rows.add(container.getData());
			columnIndex = 0;
		}
	}

}
