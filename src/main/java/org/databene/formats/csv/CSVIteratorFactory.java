/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.formats.DataIterator;

/**
 * Factory for all kinds of CSV iterators.<br/><br/>
 * Created: 14.10.2009 11:40:31
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class CSVIteratorFactory {

	private CSVIteratorFactory() { }
	
	public static DataIterator<String> createCSVCellIterator(
			String uri, char separator, String encoding) throws IOException { 
		return new CSVCellIterator(uri, separator, encoding);
	} 

	public static DataIterator<String> createCSVVellIteratorForColumn(
			String uri, int column, char separator, boolean ignoreEmptyLines, String encoding) throws IOException { 
		return new CSVSingleColumIterator(uri, column, separator, ignoreEmptyLines, encoding);
	} 

	public static DataIterator<String[]> createCSVLineIterator(
			String uri, char separator, boolean ignoreEmptyLines, String encoding) throws IOException { 
		return new CSVLineIterator(uri, separator, ignoreEmptyLines, encoding);
	}

}