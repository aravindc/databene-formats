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

package org.databene.formats.fixedwidth;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

import org.databene.commons.BeanUtil;
import org.databene.commons.format.PadFormat;
import org.databene.formats.DataContainer;
import org.databene.formats.util.DataIteratorAdapter;

/**
 * Iterates fixed-width column files.<br/><br/>
 * Created: 20.12.2011 18:13:28
 * @since 0.6.6
 * @author Volker Bergmann
 */
public class FixedWidthBeanIterator<E> extends DataIteratorAdapter<String[], E> {

	private Class<E> beanClass;
	private FixedWidthColumnDescriptor[] columnDescriptors;
	private Locale locale;
	
	public FixedWidthBeanIterator(String uri, String encoding, Class<E> beanClass, String columnFormats) 
			throws IOException, ParseException {
		this(uri, encoding, beanClass, columnFormats, "");
	}

	public FixedWidthBeanIterator(String uri, String encoding, Class<E> beanClass, String columnFormats, String nullString) 
			throws IOException, ParseException {
		super(null);
		this.locale = Locale.US;
		this.beanClass = beanClass;
		FixedWidthRowTypeDescriptor rowDescriptor = FixedWidthUtil.parseBeanColumnsSpec(columnFormats, beanClass.getSimpleName(), nullString, locale);
		this.columnDescriptors = rowDescriptor.getColumnDescriptors();
		PadFormat[] formats = BeanUtil.extractProperties(this.columnDescriptors, "format", PadFormat.class);
		source = new FixedWidthLineIterator(uri, formats);
	}

	@Override
	public Class<E> getType() {
		return beanClass;
	}

	@Override
	public DataContainer<E> next(DataContainer<E> container) {
		DataContainer<String[]> wrapper = nextOfSource();
		if (wrapper == null)
			return null;
		String[] cells = wrapper.getData();
		E result = BeanUtil.newInstance(beanClass);
		for (int i = 0; i < columnDescriptors.length; i++)
			BeanUtil.setPropertyValue(result, columnDescriptors[i].getName(), cells[i], true, true);
		return container.setData(result);
	}

}
