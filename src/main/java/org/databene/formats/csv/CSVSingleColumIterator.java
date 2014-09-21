/*
 * (c) Copyright 2009-2013 by Volker Bergmann. All rights reserved.
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
