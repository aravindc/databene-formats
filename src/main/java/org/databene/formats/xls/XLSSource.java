/*
 * (c) Copyright 2011-2013 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.xls;

import org.databene.formats.DataIterator;
import org.databene.formats.DataSource;
import org.databene.formats.util.OrthogonalArrayIterator;

/**
 * Defined XLSSource as abstraction for XLS row or column data sources.<br/><br/>
 * Created: 08.12.2011 16:51:08
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class XLSSource implements DataSource<Object[]> {

	private String uri;
	private boolean formatted;
	private String emptyMarker;
	private String nullMarker;
	private boolean rowBased;
	
	public XLSSource(String uri, boolean formatted, String emptyMarker, String nullMarker, boolean rowBased) {
		this.uri = uri;
		this.formatted = formatted;
		this.emptyMarker = emptyMarker;
		this.nullMarker = nullMarker;
		this.rowBased = rowBased;
	}

	@Override
	public Class<Object[]> getType() {
		return Object[].class;
	}

	@Override
	public DataIterator<Object[]> iterator() {
		try {
			XLSLineIterator iterator = new XLSLineIterator(uri);
			iterator.setFormatted(formatted);
			if (emptyMarker != null)
				iterator.setEmptyMarker(emptyMarker);
			if (nullMarker != null)
				iterator.setNullMarker(nullMarker);
			if (!rowBased)
				return new OrthogonalArrayIterator<Object>(iterator);
			else
				return iterator;
		} catch (Exception e) {
			throw new RuntimeException("Error creating iterator for " + uri, e);
		}
	}

	@Override
	public void close() {
		// nothing to do here
	}

}
