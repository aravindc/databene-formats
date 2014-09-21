/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.fixedwidth;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.databene.commons.Assert;
import org.databene.commons.SystemInfo;

/**
 * Writes data arrays to fixed-width files supporting different row types and formats.<br/><br/>
 * Created: 13.03.2014 12:37:54
 * @since 0.7.2
 * @author Volker Bergmann
 */

public class MultiTypeArrayFixedWidthWriter implements Closeable {
	
	private final Writer out;
	private Map<String, FixedWidthRowTypeDescriptor> rowDescriptors;
	
	public MultiTypeArrayFixedWidthWriter(Writer out) {
		this(out, null);
	}
	
	public MultiTypeArrayFixedWidthWriter(Writer out, List<FixedWidthRowTypeDescriptor> rowDescriptors) {
		Assert.notNull(out, "Writer");
		this.out = out;
		this.rowDescriptors = new HashMap<String, FixedWidthRowTypeDescriptor>();
		if (rowDescriptors != null)
			for (FixedWidthRowTypeDescriptor rowDescriptor : rowDescriptors)
				addRowFormat(rowDescriptor);
	}
	
	public void addRowFormat(FixedWidthRowTypeDescriptor rowDescriptor) {
		this.rowDescriptors.put(rowDescriptor.getName(), rowDescriptor);
	}
	
	public void write(String rowTypeName, Object... values) throws IOException {
		// Check preconditions
		Assert.notNull(values, "array");
		FixedWidthRowTypeDescriptor rowType = rowDescriptors.get(rowTypeName);
		if (rowType == null)
			throw new IllegalArgumentException("Illegal row type: " + rowTypeName);
		// format array
		out.write(rowType.formatArray(values));
		out.write(SystemInfo.getLineSeparator());
	}

	@Override
	public void close() throws IOException {
		this.out.close();
	}
	
}
