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

import java.text.ParseException;
import java.text.ParsePosition;

import org.databene.commons.ArrayBuilder;
import org.databene.commons.BeanUtil;
import org.databene.commons.SyntaxError;
import org.databene.commons.accessor.GraphAccessor;
import org.databene.commons.mutator.AnyMutator;

/**
 * Row type support for fixed-width files: formatting, parsing and verification 
 * for array- and bean-type data.<br/><br/>
 * Created: 28.03.2014 15:18:23
 * @since 0.7.3
 * @author Volker Bergmann
 */

public class FixedWidthRowTypeDescriptor {
	
	private String name;
	private FixedWidthColumnDescriptor[] columnDescriptors;
	private int rowLength;
	
	public FixedWidthRowTypeDescriptor(String name, FixedWidthColumnDescriptor[] columnDescriptors) {
		this.name = name;
		this.columnDescriptors = columnDescriptors;
		this.rowLength = totalLength(columnDescriptors);
	}
	
	public String getName() {
		return name;
	}
	
	public FixedWidthColumnDescriptor[] getColumnDescriptors() {
		return columnDescriptors;
	}
	
	public String formatBean(Object rowBean) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < columnDescriptors.length; i++) {
			String path = columnDescriptors[i].getName();
			Object value = GraphAccessor.getValue(path, rowBean);
			builder.append(columnDescriptors[i].format(value));
		}
		return builder.toString();
	}
	
	public String formatArray(Object... columnValues) {
		if (columnValues.length != columnDescriptors.length)
			throw new IllegalArgumentException("Row type '" + name + "' expects " + columnValues.length + " array elements " +
					", but found: " + columnValues.length);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < columnDescriptors.length; i++)
			builder.append(columnDescriptors[i].format(columnValues[i]));
		return builder.toString();
	}
	
	public Object[] parseAsArray(String row) {
		if (row.length() != rowLength)
			throw new SyntaxError("Row of type '" + name + "' has illegal length. " +
					"Expected: " + rowLength + ", found: " + row.length(), "'" + row + "'");
		ArrayBuilder<Object> builder = new ArrayBuilder<Object>(Object.class);
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < columnDescriptors.length; i++) {
			FixedWidthColumnDescriptor columnDescriptor = columnDescriptors[i];
			int endIndex = pos.getIndex() + columnDescriptor.getWidth();
			String cellContent = row.substring(pos.getIndex(), endIndex);
			try {
				Object cellObject = columnDescriptor.parse(cellContent);
				builder.add(cellObject);
			} catch (ParseException e) {
				throw new SyntaxError("Error parsing column '" + descriptorName(columnDescriptor, i) + "'. " + e.getMessage(), cellContent);
			}
			pos.setIndex(endIndex);
		}
		return builder.toArray();
	}
	
	public <T> T parseAsBean(String row, Class<T> beanClass) {
		if (row.length() != rowLength)
			throw new SyntaxError("Row of type '" + name + "' has illegal length. " +
					"Expected: " + rowLength + ", found: " + row.length(), "'" + row + "'");
		T bean = BeanUtil.newInstance(beanClass);
		ParsePosition pos = new ParsePosition(0);
		for (FixedWidthColumnDescriptor columnDescriptor : columnDescriptors) {
			int endIndex = pos.getIndex() + columnDescriptor.getWidth();
			String cellContent = row.substring(pos.getIndex(), endIndex);
			try {
				Object cellObject = columnDescriptor.parse(cellContent);
				AnyMutator.setValue(bean, columnDescriptor.getName(), cellObject, true, true);
			} catch (ParseException e) {
				throw new SyntaxError("Error parsing column '" + columnDescriptor + "'. " + e.getMessage(), cellContent);
			}
			pos.setIndex(endIndex);
		}
		return bean;
	}
	
	
	// private helpers -------------------------------------------------------------------------------------------------
	
	private static String descriptorName(FixedWidthColumnDescriptor descriptor, int i) {
		return (descriptor.getName() != null ? descriptor.getName() : String.valueOf(i));
	}
	
	private static int totalLength(FixedWidthColumnDescriptor[] descriptors) {
		int result = 0;
		for (FixedWidthColumnDescriptor descriptor : descriptors)
			result += descriptor.getWidth();
		return result;
	}
	
}
