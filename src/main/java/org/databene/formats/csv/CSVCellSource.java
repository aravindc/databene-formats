/*
 * Copyright (C) 2011-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
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

import org.databene.commons.ConfigurationError;
import org.databene.commons.SystemInfo;
import org.databene.formats.DataIterator;
import org.databene.formats.util.AbstractDataSource;

/**
 * Creates Iterators that iterate through the cells of a CSV file.
 * 
 * Created: 01.09.2007 11:40:30
 * @author Volker Bergmann
 */
public class CSVCellSource extends AbstractDataSource<String> {

    private String uri;
    private char separator;
    private String encoding;

    public CSVCellSource() {
        this(null, ',');
    }

    public CSVCellSource(String uri, char separator) {
        this(uri, separator, SystemInfo.getFileEncoding());
    }
    
    public CSVCellSource(String uri, char separator, String encoding) {
    	super(String.class);
        this.uri = uri;
        this.separator = separator;
        this.encoding = encoding;
    }
    
    public void setUri(String uri) {
		this.uri = uri;
	}

    @Override
	public DataIterator<String> iterator() {
        try {
            return new CSVCellIterator(uri, separator, encoding);
        } catch (Exception e) {
            throw new ConfigurationError(e);
        }
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName() + '[' + uri + ", '" + separator + "']";
    }
}
