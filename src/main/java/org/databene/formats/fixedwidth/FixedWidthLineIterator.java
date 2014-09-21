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

package org.databene.formats.fixedwidth;

import org.databene.commons.IOUtil;
import org.databene.commons.ReaderLineIterator;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.commons.format.PadFormat;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * Tests the FlatFileLineIterator.<br/>
 * <br/>
 * Created: 27.08.2007 06:43:50
 * @author Volker Bergmann
 */
public class FixedWidthLineIterator implements DataIterator<String[]> {

    private boolean ignoreEmptyLines;

    private ReaderLineIterator lineIterator;
    private FixedWidthLineParser parser;
    private int lineCount;
    private Pattern lineFilter;

    // constructors ----------------------------------------------------------------------------------------------------

    public FixedWidthLineIterator(String uri, PadFormat[] formats) throws IOException {
        this(uri, formats, false, SystemInfo.getFileEncoding(), null);
    }

    public FixedWidthLineIterator(String uri, PadFormat[] formats, boolean ignoreEmptyLines, String encoding, 
    		String lineFilter) throws IOException {
        this(IOUtil.getReaderForURI(uri, encoding), formats, ignoreEmptyLines, lineFilter);
    }

    public FixedWidthLineIterator(Reader reader, PadFormat[] formats) {
        this(reader, formats, false, null);
    }

    public FixedWidthLineIterator(Reader reader, PadFormat[] formats, boolean ignoreEmptyLines, String lineFilter) {
        this.lineIterator = new ReaderLineIterator(reader);
        parser = new FixedWidthLineParser(formats);
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.lineCount = 0;
        this.lineFilter = (lineFilter != null ? Pattern.compile(lineFilter) : null);
    }

    // interface -------------------------------------------------------------------------------------------------------

    @Override
	public Class<String[]> getType() {
    	return String[].class;
    }
    
    /**
     * Parses a CSV row into an array of Strings
     * @return an array of Strings that represents a CSV row
     */
	@Override
	public DataContainer<String[]> next(DataContainer<String[]> wrapper) {
        String[] result = fetchNextLine();
        if (result != null) {
            lineCount++;
            return wrapper.setData(result);
        } else 
        	return null;
    }

    /** Closes the source */
    @Override
	public void close() {
        if (lineIterator != null)
            lineIterator.close();
        lineIterator = null;
    }
    
    /** Returns the line count iterated so far. */
    public int lineCount() {
        return lineCount;
    }

    // private helpers -------------------------------------------------------------------------------------------------

    private String[] fetchNextLine() {
        try {
            if (lineIterator == null)
                return null;
            String line = null;
            boolean success = false;
            
            // fetch next appropriate lines skipping empty lines if they shall be ignored
            while (lineIterator.hasNext()) {
                lineCount++;
                line = lineIterator.next();
                if ((line.length() > 0 || !ignoreEmptyLines) 
                		&& (lineFilter == null || lineFilter.matcher(line).matches())) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                close();
                return null;
            } else if (StringUtil.isEmpty(line)) {
                return new String[0];
            } else {
            	return parser.parse(line);
            }
        } catch (ParseException e) {
            throw new RuntimeException("Unexpected error. ", e);
        }
    }

}
