/*
 * (c) Copyright 2007-2014 by Volker Bergmann. All rights reserved.
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

import org.databene.formats.csv.CSVTokenType;
import org.databene.formats.csv.CSVTokenizer;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

import static org.databene.formats.csv.CSVTokenType.*;
import static org.junit.Assert.*;

/**
 * Tests the {@link CSVTokenizer}.<br/><br/>
 * Created: 26.08.2006 17:51:14
 * @author Volker Bergmann
 */
public class CSVTokenizerTest {

	@Test
    public void testEmpty() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testA() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A");
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testAB() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A\tv,B");
        assertNextToken(tokenizer, CELL, "A\tv");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testEmptyAndNull() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("\"\",,A,,");
        assertNextToken(tokenizer, CELL, "");
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer,  EOF, null);
        assertNextToken(tokenizer,  EOF, null);
    }

	@Test
    public void testEmptyFirstCell() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer(",,A");
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer,  EOF, null);
        assertNextToken(tokenizer,  EOF, null);
    }

	@Test
    public void testABTab() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A\tB", '\t');
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testABL() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A,B\r\n");
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testABLC() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A,B\r\nC");
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "C");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testABLCL() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("A,B\r\nC\r\n");
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "C");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testQuotes() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("\"A\",B\r\n\"C\"\r\n");
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, "B");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "C");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testQuoteEscaping() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("\"A\"\"A\",\"\"\"B\"\" is B\"\r\n" +
                "\"C was \"\"C\"\"\",\"\"\"D\"\" is \"\"D\"\"\"\r\n");
        assertNextToken(tokenizer, CELL, "A\"A");
        assertNextToken(tokenizer, CELL, "\"B\" is B");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "C was \"C\"");
        assertNextToken(tokenizer, CELL, "\"D\" is \"D\"");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testQuoteThenEmpty() throws IOException {
    	CSVTokenizer tokenizer = createTokenizer("\"A\";;X", ';');
        assertNextToken(tokenizer, CELL, "A");
        assertNextToken(tokenizer, CELL, null);
        assertNextToken(tokenizer, CELL, "X");
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testLFInQuote() throws IOException {
        CSVTokenizer tokenizer = createTokenizer("\"A\r\nB\"");
        assertNextToken(tokenizer, CELL, "A\r\nB");
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }

	@Test
    public void testFile() throws IOException {
        CSVTokenizer tokenizer = new CSVTokenizer("file://org/databene/formats/csv/names.csv", ',');
        assertNextToken(tokenizer, CELL, "Alice");
        assertNextToken(tokenizer, CELL, "Bob");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "Charly");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, CELL, "Dieter");
        assertNextToken(tokenizer, CELL, "Indiana\r\nJones");
        assertNextToken(tokenizer, EOL, null);
        assertNextToken(tokenizer, EOF, null);
        assertNextToken(tokenizer, EOF, null);
    }
    
	@Test
    public void testSkipLine() throws IOException {
    	// testing \r
    	CSVTokenizer tokenizer = createTokenizer("1\r2");
        tokenizer.skipLine();
        assertNextToken(tokenizer, CELL, "2");
        assertNextToken(tokenizer, EOF, null);
    	// testing \n
    	tokenizer = createTokenizer("1\r\n2");
        tokenizer.skipLine();
        assertNextToken(tokenizer, CELL, "2");
        assertNextToken(tokenizer, EOF, null);
    	// testing \r\n
    	tokenizer = createTokenizer("1\n2");
        tokenizer.skipLine();
        assertNextToken(tokenizer, CELL, "2");
        assertNextToken(tokenizer, EOF, null);
    }

    // helpers ---------------------------------------------------------------------------------------------------------

	private static CSVTokenizer createTokenizer(String content) {
		return createTokenizer(content, ',');
	}

	private static CSVTokenizer createTokenizer(String content, char separator) {
		StringReader reader = new StringReader(content);
		return new CSVTokenizer(reader, separator);
	}

    private static void assertNextToken(CSVTokenizer tokenizer, CSVTokenType tokenType, String cell) throws IOException {
        CSVTokenType found = tokenizer.next();
        assertEquals(tokenType, found);
        assertEquals(tokenType, tokenizer.ttype);
        assertEquals(cell, tokenizer.cell);
    }
}
