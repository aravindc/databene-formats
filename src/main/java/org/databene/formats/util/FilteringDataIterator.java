/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.Filter;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * {@link DataIterator} proxy which applies a {@link Filter} to the data provides by its source.<br/><br/>
 * Created: 24.07.2011 10:19:41
 * @since 0.6.0
 * @author Volker Bergmann
 */
public class FilteringDataIterator<E> extends DataIteratorProxy<E> {

    protected Filter<E> filter;

    public FilteringDataIterator(DataIterator<E> source, Filter<E> filter) {
        super(source);
        this.filter = filter;
    }

    @Override
	public DataContainer<E> next(DataContainer<E> wrapper) {
    	DataContainer<E> result;
    	do {
    		result = source.next(wrapper);
    	} while (result != null && !filter.accept(result.getData()));
        return result;
    }

}
