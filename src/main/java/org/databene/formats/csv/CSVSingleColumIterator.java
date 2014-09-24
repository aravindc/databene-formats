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
package org.databene.formats.csv;

import java.io.IOException;

import org.databene.commons.Encodings;
import org.databene.commons.HeavyweightIterator;
import org.databene.commons.StringUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.util.DataIteratorAdapter;
import org.databene.formats.util.ThreadLocalDataContainer;

/**
 * {@link HeavyweightIterator} that iterates through all cells of a single CSV column.<br/><br/>
 * Created: 14.10.2009 11:42:49
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class CSVSingleColumIterator extends DataIteratorAdapter<String[], String> {

	private static final char DEFAULT_SEPARATOR = ',';
	
	private int columnIndex;
	ThreadLocalDataContainer<String[]> rowContainer = new ThreadLocalDataContainer<String[]>();
	
	public CSVSingleColumIterator(String uri, int columnIndex) throws IOException {
		this(uri, columnIndex, DEFAULT_SEPARATOR, false, Encodings.UTF_8);
    }
	
	public CSVSingleColumIterator(String uri, int columnIndex, char separator, boolean ignoreEmptyLines, String encoding) throws IOException {
		super(new CSVLineIterator(uri, separator, ignoreEmptyLines, encoding));
		if (StringUtil.isEmpty(uri))
			throw new IllegalArgumentException("URI is empty");
		if (columnIndex < 0)
			throw new IllegalArgumentException("Negative column index: " + columnIndex);
		this.columnIndex = columnIndex;
    }
	
	@Override
	public Class<String> getType() {
		return String.class;
	}
	
	@Override
	public DataContainer<String> next(DataContainer<String> wrapper) {
		DataContainer<String[]> tmp = source.next(rowContainer.get());
		if (tmp == null)
			return null;
		String[] nextRow = tmp.getData();
		return wrapper.setData(columnIndex < nextRow.length ? nextRow[columnIndex] : null);
	}

}
