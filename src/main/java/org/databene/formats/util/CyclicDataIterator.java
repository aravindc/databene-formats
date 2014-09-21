/*
 * (c) Copyright 2012 by Volker Bergmann. All rights reserved.
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

import java.io.IOException;

import org.databene.commons.IOUtil;
import org.databene.formats.DataContainer;
import org.databene.formats.DataIterator;

/**
 * Allows repeated iteration through a {@link DataIterator}.<br/><br/>
 * Created: 22.05.2012 08:58:16
 * @since 0.6.9
 * @author Volker Bergmann
 */
public class CyclicDataIterator<E> extends DataIteratorProxy<E> {
	
	protected Creator<E> creator;

	public CyclicDataIterator(Creator<E> creator) throws IOException {
		super(creator.create());
		this.creator = creator;
	}
	
	@Override
	public synchronized DataContainer<E> next(DataContainer<E> wrapper) {
		DataContainer<E> result = super.next(wrapper);
		if (result == null) {
			reset();
			result = super.next(wrapper);
		}
		return result;
	}

	public synchronized void reset() {
		IOUtil.close(source);
		try {
			source = creator.create();
		} catch (IOException e) {
			throw new RuntimeException("Error creating DataIterator", e);
		}
	}
	
	public interface Creator<E> {
		DataIterator<E> create() throws IOException;
	}
	
}
