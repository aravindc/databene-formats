/*
 * (c) Copyright 2007-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.format.PadFormat;
import org.databene.formats.DataIterator;
import org.databene.formats.util.AbstractDataSource;

import java.io.IOException;

/**
 * Creates Iterators that iterate through the lines of a flat file and returns each line as array of Strings.<br/>
 * <br/>
 * Created: 27.08.2007 19:16:26
 * @author Volker Bergmann
 */
public class FixedWidthLineSource extends AbstractDataSource<String[]> {

    private String uri;
    private PadFormat[] formats;
    private boolean ignoreEmptyLines;
    private String encoding;
    private String lineFilter;

    public FixedWidthLineSource(String uri, PadFormat[] formats, boolean ignoreEmptyLines, String encoding, String lineFilter) {
    	super(String[].class);
        this.uri = uri;
        this.formats = formats.clone();
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.encoding = encoding;
        this.lineFilter = lineFilter;
    }

    @Override
	public DataIterator<String[]> iterator() {
        try {
            return new FixedWidthLineIterator(uri, formats, ignoreEmptyLines, encoding, lineFilter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
