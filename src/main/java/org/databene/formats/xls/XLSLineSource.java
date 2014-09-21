/*
 * (c) Copyright 2011-2014 by Volker Bergmann. All rights reserved.
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

import java.util.Iterator;

import org.databene.formats.DataIterator;
import org.databene.formats.util.AbstractDataSource;
import org.databene.formats.xls.XLSLineIterator;

/**
 * {@link Iterable} implementation which creates {@link Iterator}s 
 * that provide lines of XLS files as array objects.<br/><br/>
 * Created: 19.07.2011 08:36:18
 * @since 0.6.5
 * @author Volker Bergmann
 */
public class XLSLineSource extends AbstractDataSource<Object[]> {
	
	private String uri;
	private String sheetName;
	private boolean formatted;

	public XLSLineSource(String uri) {
		this(uri, null, false);
	}

	public XLSLineSource(String uri, String sheetName, boolean formatted) {
		super(Object[].class);
		this.uri = uri;
		this.sheetName = sheetName;
		this.formatted = formatted;
	}

	@Override
	public DataIterator<Object[]> iterator() {
		try {
			return new XLSLineIterator(uri, sheetName, false, formatted, null);
		} catch (Exception e) {
			throw new RuntimeException("Unable to create iterator for URI " + uri, e);
		}
	}

}
