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

import org.databene.commons.BeanUtil;
import org.databene.commons.ConfigurationError;
import org.databene.commons.IOUtil;
import org.databene.commons.mutator.AnyMutator;
import org.databene.commons.mutator.NamedMutator;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;
import org.databene.formats.util.ThreadLocalDataContainer;

import java.io.*;

/**
 * Reads a CSV file and maps its columns to JavaBeans.<br/>
 * <br/>
 * Created: 21.07.2006 15:07:36
 * @author Volker Bergmann
 */
public class CSVToJavaBeanMapper<E> implements DataIterator<E> {

    private CSVLineIterator iterator;
    private Class<E> type;
    private String emptyValue;
    
    private NamedMutator[] mutators;
    private int classIndex;
    private ThreadLocalDataContainer<String[]> dataContainer = new ThreadLocalDataContainer<String[]>();


    // constructors ----------------------------------------------------------------------------------------------------

    public CSVToJavaBeanMapper(Reader reader, Class<E> type) throws IOException {
        this(reader, type, ',', null);
    }

    public CSVToJavaBeanMapper(Reader reader, Class<E> type, char separator, String emptyValue) throws IOException {
    	CSVLineIterator iterator = new CSVLineIterator(reader, separator, true);
        String[] attributeNames = iterator.next(dataContainer.get()).getData();
        init(iterator, type, emptyValue, attributeNames);
    }

    public CSVToJavaBeanMapper(Reader reader, Class<E> type, char separator, String emptyValue, String[] attributeNames) throws IOException {
    	CSVLineIterator iterator = new CSVLineIterator(reader, separator, true);
        init(iterator, type, emptyValue, attributeNames);
    }


    // DataIterator interface implementation ---------------------------------------------------------------------------

    @Override
	public Class<E> getType() {
    	return type;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public DataContainer<E> next(DataContainer<E> wrapper) {
        int i = 0;
        String value = null;
        try {
            DataContainer<String[]> tmp = nextRaw(dataContainer.get());
            if (tmp == null)
            	return null;
			String[] line = tmp.getData();
			if (line.length == 0)
				return null;
			Class<E> beanClass = (classIndex >= 0 ? (Class<E>) BeanUtil.forName(line[classIndex]) : type);
            E bean = BeanUtil.newInstance(beanClass);
            int columns = Math.min(line.length, mutators.length);
            for (i = 0; i < columns; i++) {
            	if (i != classIndex) {
	                value = line[i];
	                if (value != null && value.length() == 0)
	                    value = emptyValue;
	                mutators[i].setValue(bean, value);
            	}
            }
            return wrapper.setData(bean);
        } catch (Exception e) {
            throw new ConfigurationError("Failed to set property '" + 
                    mutators[i].getName() + "' to '" + value + "' on class " + type, e);
        }
    }

    public DataContainer<String[]> nextRaw(DataContainer<String[]> wrapper) {
    	return iterator.next(wrapper);
    }

    @Override
	public void close() {
    	IOUtil.close(iterator);
    }


    // further public methods ------------------------------------------------------------------------------------------

    public void skip() {
    	iterator.next(dataContainer.get());
    }


    // private helpers -------------------------------------------------------------------------------------------------

    private void init(CSVLineIterator iterator, Class<E> type, String emptyValue, String[] attributeNames) {
		this.iterator = iterator;
        this.type = type;
        this.emptyValue = emptyValue;
        this.mutators = new NamedMutator[attributeNames.length];
        this.classIndex = -1;
        for (int i = 0; i < attributeNames.length; i++) {
            String attributeName = attributeNames[i];
            if ("class".equals(attributeName)) {
            	mutators[i] = null;
            	this.classIndex = i;
            } else {
            	mutators[i] = new AnyMutator(attributeName, false, true);
            }
        }
	}

}
