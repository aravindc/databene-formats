/*
 * (c) Copyright 2007-2013 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License.
 *
 * For redistributing this software or a derivative work under a license other
 * than the GPL-compatible Free Software License as defined by the Free
 * Software Foundation or approved by OSI, you must first obtain a commercial
 * license to this software product from Volker Bergmann.
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

import org.databene.commons.IOUtil;
import org.databene.commons.CollectionUtil;
import org.databene.commons.SystemInfo;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Gives access to content of a CSV file by String arrays
 * that represent the CSV rows as specified in RFC 4180.<br/>
 * <br/>
 * @author Volker Bergmann
 */
public class CSVLineIterator implements DataIterator<String[]> {

    /** The default separator to use */
    public static final char DEFAULT_SEPARATOR = ',';

    private String stringRep;
    
    private CSVTokenizer tokenizer;

    private String[] nextLine;

    private boolean ignoreEmptyLines;

    private int lineCount;
    
    private String[] headers;

	private HashMap<String, Integer> headerIndexes;
    
    private boolean eol;

    // constructors ----------------------------------------------------------------------------------------------------

    /**
     * Creates a parser that reads from a uri
     * @param uri the URL to read from
     * @throws IOException if uri access failed
     */
    public CSVLineIterator(String uri) throws IOException {
        this(uri, DEFAULT_SEPARATOR);
    }

    /**
     * Creates a parser that reads from a uri
     * @param uri the URL to read from
     * @param separator
     * @throws IOException
     */
    public CSVLineIterator(String uri, char separator) throws IOException {
        this(uri, separator, false);
    }

    public CSVLineIterator(String uri, char separator, String encoding) throws IOException {
        this(uri, separator, false, encoding);
    }

    public CSVLineIterator(String uri, char separator, boolean ignoreEmptyLines) throws IOException {
        this(uri, separator, ignoreEmptyLines, SystemInfo.getFileEncoding());
    }

    public CSVLineIterator(String uri, char separator, boolean ignoreEmptyLines, String encoding) throws IOException {
        this(IOUtil.getReaderForURI(uri, encoding), separator, ignoreEmptyLines);
        this.stringRep = uri;
    }

    /**
     * Creates a parser that reads from a reader and used a special separator character
     * @param reader the reader to use
     * @param separator the separator character
     */
    public CSVLineIterator(Reader reader, char separator) throws IOException {
        this(reader, separator, false);
    }

    public CSVLineIterator(Reader reader, char separator, boolean ignoreEmptyLines) throws IOException {
        this.tokenizer = new CSVTokenizer(reader, separator);
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.nextLine = parseNextLine();
        this.lineCount = 0;
        this.eol = false;
        this.stringRep = reader.toString();
    }

    // interface -------------------------------------------------------------------------------------------------------

	public void setHeaders(String[] headers) {
		if (headers != null)
			this.headers = headers;
		else
			this.headers = new String[0];
		this.headerIndexes = new HashMap<String, Integer>();
		for (int i = 0; i < this.headers.length; i++)
			this.headerIndexes.put(this.headers[i], i);
	}
    
    @Override
	public Class<String[]> getType() {
    	return String[].class;
    }
    
    /**
     * Parses a CSV row into an array of Strings
     * @return an array of Strings that represents a CSV row
     */
	@Override
	public synchronized DataContainer<String[]> next(DataContainer<String[]> wrapper) {
    	if (nextLine == null)
    		return null;
        try {
            String[] result = nextLine;
            if (tokenizer != null) {
                nextLine = parseNextLine();
                lineCount++;
            } else
                nextLine = null;
            return wrapper.setData(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	public String[] cellsByHeaders(String[] headers, String[] data) {
		String[] result = new String[headers.length];
		for (int i = 0; i < headers.length; i++)
			result[i] = cellByHeader(headers[i], data);
		return result;
	}

	public String cellByHeader(String header, String[] data) {
		Integer index = this.headerIndexes.get(header);
		return (index != null && index < data.length ? data[index] : null);
	}
    
    /** Closes the source */
    @Override
	public synchronized void close() {
        if (tokenizer != null)
            tokenizer.close();
        tokenizer = null;
        nextLine = null;
    }

    public synchronized int lineCount() {
        return lineCount;
    }

    public static void process(String uri, char separator, String encoding, boolean ignoreEmptyLines, CSVLineHandler lineHandler) throws IOException {
        CSVLineIterator iterator = null;
        try {
            iterator = new CSVLineIterator(uri, separator, ignoreEmptyLines, encoding);
            DataContainer<String[]> row = new DataContainer<String[]>();
            while ((row = iterator.next(row)) != null)
                lineHandler.handle(row.getData());
        } finally {
            if (iterator != null)
                iterator.close();
        }
    }
    
    
    
    // private helpers -------------------------------------------------------------------------------------------------

    private String[] parseNextLine() throws IOException {
        if (tokenizer == null)
            return null;
        List<String> list;
        CSVTokenType tokenType;
        do {
            list = new ArrayList<String>();
            while ((tokenType = tokenizer.next()) == CSVTokenType.CELL) {
                list.add(tokenizer.cell);
            }
            if (tokenType == CSVTokenType.EOF)
                close();
        } while (tokenType != CSVTokenType.EOF && (ignoreEmptyLines && list.size() == 0));
        if (list.size() > 0) {
            eol = (tokenType == CSVTokenType.EOL);
            String[] line = CollectionUtil.toArray(list, String.class);
           	checkHeaders(line);
			return line;
        }
        if (eol) {
           	checkHeaders(null);
        	if (!ignoreEmptyLines)
        		return new String[0];
        }
        return null;
    }
    
    private void checkHeaders(String[] line) {
		if (this.headers == null) {
			setHeaders(line);
		}
	}

    
    
    // java.lang.Object overrides --------------------------------------------------------------------------------------

	@Override
    public String toString() {
    	return getClass().getSimpleName() + "[" + stringRep + "]";
    }

}
