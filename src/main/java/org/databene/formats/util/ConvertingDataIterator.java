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

import java.io.Closeable;

import org.databene.commons.Converter;
import org.databene.commons.IOUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * {@link DataIterator} proxy which applies a {@link Converter} to the data provided by its source.<br/><br/>
 * Created: 24.07.2011 09:52:35
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class ConvertingDataIterator<S, T> extends DataIteratorAdapter<S, T> {

    protected Converter<S, T> converter;

    public ConvertingDataIterator(DataIterator<S> source, Converter<S, T> converter) {
    	super(source);
        this.converter = converter;
    }
    
    @Override
	public Class<T> getType() {
    	return converter.getTargetType();
    }

	@Override
	public DataContainer<T> next(DataContainer<T> container) {
        DataContainer<S> sourceWrapper = nextOfSource();
        if (sourceWrapper == null)
        	return null;
        return container.setData(converter.convert(sourceWrapper.getData()));
    }

	@Override
	public void close() {
		if (converter instanceof Closeable)
			IOUtil.close((Closeable) converter);
		super.close();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[source:" + source + ", converter:" + converter + "]";
	}
	
}
