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
