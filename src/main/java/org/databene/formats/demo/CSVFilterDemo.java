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

package org.databene.formats.demo;

import org.databene.commons.DocumentWriter;
import org.databene.commons.Filter;
import org.databene.formats.DataContainer;
import org.databene.formats.csv.ArrayCSVWriter;
import org.databene.formats.csv.CSVLineIterator;
import org.databene.formats.util.FilteringDataIterator;

import java.io.*;

/**
 * Parses the rows of a CSV file and extracts the lines that match a {@link Filter} to a target file.<br/>
 * <br/>
 * Created: 12.06.2007 19:32:31
 * @since 0.2
 * @author Volker Bergmann
 */
public class CSVFilterDemo {
    private static final String FILE_NAME = "test.dat";

    public static void main(String[] args) throws IOException {

        // creates a CSV parser for the input file
        Reader reader = new FileReader(FILE_NAME);
        CSVLineIterator src = new CSVLineIterator(reader, '|');

        // sets up a filtered iterator that uses the upper iterator as source
        Filter<String[]> filter = new RowFilter();
        FilteringDataIterator<String[]> iterator = new FilteringDataIterator<String[]>(src, filter);

        // create a CSV writer to save the rows that matched the filter
        Writer out = new BufferedWriter(new FileWriter("matches.csv"));
        DocumentWriter<Object[]> csvWriter = new ArrayCSVWriter(out, '|');

        // initialize counter and timer
        int matchCount = 0;
        System.out.println("Running...");
        long startMillis = System.currentTimeMillis();

        // iterate the entries
        DataContainer<String[]> cells = new DataContainer<String[]>();
        while ((cells = iterator.next(cells)) != null) {
            csvWriter.writeElement(cells.getData());
            matchCount++;
        }

        out.close();
        reader.close();

        // output counter and timer values
        long elapsedTime = System.currentTimeMillis() - startMillis;
        System.out.println("Processed file " + FILE_NAME + " with " + src.lineCount() + " entries " +
                "within " + elapsedTime + "ms (" + (src.lineCount() * 1000L / elapsedTime) + " entries per second)");
        System.out.println("Found " + matchCount + " matches");
    }

    static final class RowFilter implements Filter<String[]> {
        @Override
		public boolean accept(String[] candidate) {
            return candidate.length > 2 && "3023293310905".equals(candidate[1]);
        }
    }
}
