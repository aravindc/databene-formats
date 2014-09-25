/*
 * Copyright (C) 2011-2014 Volker Bergmann (volker.bergmann@bergmann-it.de).
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.databene.formats.util;

import org.databene.commons.IOUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Adapter for a {@link DataIterator}.<br/><br/>
 * Created: 24.07.2011 09:53:49
 * @since 0.6.0
 * @author Volker Bergmann
 */
public abstract class DataIteratorAdapter<S, T> implements DataIterator<T> {

    protected DataIterator<S> source;
    private ThreadLocalDataContainer<S> sourceContainerProvider;

    public DataIteratorAdapter(DataIterator<S> source) {
        this.source = source;
        this.sourceContainerProvider = new ThreadLocalDataContainer<S>();
    }
    
    @Override
	public void close() {
        IOUtil.close(source);
    }
    
    protected DataContainer<S> nextOfSource() {
    	return source.next(getSourceContainer());
    }

    protected DataContainer<S> getSourceContainer() {
    	return sourceContainerProvider.get();
    }

}