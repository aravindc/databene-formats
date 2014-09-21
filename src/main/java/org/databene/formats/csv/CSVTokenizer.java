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

import static org.databene.formats.csv.CSVTokenType.*;

import org.databene.commons.IOUtil;
import org.databene.commons.SystemInfo;

import java.io.*;

/**
 * Parses a CSV file token by token as specified in RFC 4180.
 * It returns parsed values as CSVTokensType of type CELL, EOL and EOF.
 * The current cell content is accessible by the public attribute 'cell'.<br/>
 * <br/>
 * Created: 26.08.2006 17:19:35
 * @see CSVTokenType
 */
public class CSVTokenizer implements Closeable {

    /** The default separator to use */
    public static final char DEFAULT_SEPARATOR = ',';

	private static final char DEFAULT_LINE_COMMENT = '#';

    /** the source to read from */
    private PushbackReader reader;

    /** the actual separator */
    private char separator;

    /** The token at the cursor position */
    public CSVTokenType ttype;

    public CSVTokenType lastType;
    
    /** String representation of the cell at the cursor position.
     *  If the cursor is at a EOL/EOF position, this is null */
    public String cell;
    
    public int line;
    
    // constructors ----------------------------------------------------------------------------------------------------

    /**
     * Creates a tokenizer that reads from a URL.
     * @param uri the URL to read from
     * @throws IOException
     */
    public CSVTokenizer(String uri) throws IOException {
        this(uri, DEFAULT_SEPARATOR);
    }

    /**
     * Creates a tokenizer that reads from a uri.
     * @param uri       the uri to read from
     * @param separator character used for separating CSV cells
     * @throws IOException
     */
    public CSVTokenizer(String uri, char separator) throws IOException {
        this(uri, separator, SystemInfo.getFileEncoding());
    }

    public CSVTokenizer(String uri, char separator, String encoding) throws IOException {
        this(IOUtil.getReaderForURI(uri, encoding), separator);
    }

    /**
     * Creates a tokenizer that reads from a java.io.Reader.
     * @param reader the reader to use as input
     */
    public CSVTokenizer(Reader reader) {
        this(reader, DEFAULT_SEPARATOR);
    }

    /**
     * Creates a tokenizer that reads from a java.io.Reader.
     * @param reader    the reader to use as input
     * @param separator character used for separating CSV cells
     */
    public CSVTokenizer(Reader reader, char separator) {
        this.reader = new PushbackReader(new BufferedReader(reader));
        this.separator = separator;
        this.line = 1;
    }

    // interface -------------------------------------------------------------------------------------------------------

    /**
     * Returns the next token.
     * @return the next token
     * @throws IOException if source access fails
     */
    public CSVTokenType next() throws IOException {
        this.lastType = this.ttype;
        if (reader == null) // if closed, return EOF
            return setState(EOF, null);
        if (lastType == EOL) { // on line separator increase line count
        	line++;
        }
        int c = read();
        if (c == -1) { // if end of file is reached, close and signal EOF
            close();
            return setState(EOF, null);
        } else if (this.lastType == EOL && c == DEFAULT_LINE_COMMENT) {
        	skipLine();
        	return next();
        } else if (c == separator && lastType == CELL) {
        	c = read();
        }
        if (c == -1) { // if end of file is reached, close and signal EOF
            close();
            return setState(CELL, null);
        }
        if (c == separator) {
        	unread(c);
            return setState(CELL, null);
        } else if (c == '\r') { // handle \r\n or \r
            if ((c = read()) != '\n')
                unread(c);
            return setState(EOL, null);
        } else if (c == '\n') { // handle \n
            return setState(EOL, null);
        } else if (c == '"') {
            unread(c);
            return parseQuotes();
        } else {
            return parseSimpleCell(c);
        }
    }

    public void skipLine() throws IOException {
        int c;
        // go to end of line
        while ((c = read()) != -1 && c != '\r' && c != '\n') {
            // skip EOL characters
        }
        switch (c) {
            case -1 :
                return;
            case '\n' :
                return;
            case '\r' :
                int c2 = read();
                if (c2 != '\n')
                    unread(c2);
                return;
            default   :
                throw new IllegalStateException();
        }
    }

    /** Closes the source */
    @Override
	public void close() {
        if (reader != null)
            IOUtil.close(reader);
        reader = null;
    }

    public CSVTokenType lastTtype() {
        return lastType;
    }

    // private helpers -------------------------------------------------------------------------------------------------

    /**
     * sets the state of the tokenizer to the given tokenType and cell content.
     * @param tokenType the tokenType to use
     * @param cell      the cell content
     * @return the token type
     */
    private CSVTokenType setState(CSVTokenType tokenType, String cell) {
        this.cell = cell;
        this.ttype = tokenType;
        return this.ttype;
    }

	private void unread(int c) throws IOException {
		reader.unread(c);
	}

	private int read() throws IOException {
		int c = reader.read();
		return c;
	}

	private CSVTokenType parseSimpleCell(int c) throws IOException {
		StringBuilder buffer = new StringBuilder().append((char) c);
		boolean escapeMode = false;
		while ((c = read()) != -1 && c != '\r' && c != '\n') {
			if (escapeMode) {
				c = unescape((char) c);
				escapeMode = false;
			} else if (c == '\\') {
				escapeMode = true;
				continue;
			}
			if (c == separator) {
				unread(c);
		        return setState(CELL, buffer.toString());
		    }
		    buffer.append((char) c);
		}
		if (c == '\r' || c == '\n' || c == separator)
		    unread(c);
		return setState(CELL, buffer.toString());
	}

	private static char unescape(char c) { // this is more efficient than StringUtil.unescape(String)
		switch (c) {
			case 't': return '\t';
			case 'r': return '\r';
			case 'n': return '\n';
			default: return c;
		}
	}

	private CSVTokenType parseQuotes() throws IOException {
        read(); // skip leading quote
        StringBuilder buffer = new StringBuilder();
        int c;
        boolean escapeMode = false;
        boolean done;
        do {
            while ((c = read()) != -1 && c != '"') {
            	if (escapeMode) {
            		c = unescape((char) c);
            		escapeMode = false;
            	} else if (c == '\\') {
            		escapeMode = true;
            		continue;
            	}
            	buffer.append((char) c);
            }
            if (c == '"') {
                c = read();
                if (c == '"') {
                    // escaped quote
                    buffer.append('"');
                    done = false;
                } else
                    done = true;
            } else
                done = true;
        } while (!done);
        if (c == '\r' || c == '\n' || c == separator) {
            unread(c);
        }
        return setState(CELL, buffer.toString());
    }

}
