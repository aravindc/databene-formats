/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.StringUtil;
import org.databene.commons.format.PadFormat;

/**
 * Parses a line of a flat file.<br/><br/>
 * Created: 22.02.2010 08:06:41
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class FixedWidthLineParser {

	private PadFormat[] formats;

    public FixedWidthLineParser(PadFormat[] formats) {
	    this.formats = formats.clone();
    }

    public String[] parse(String line) throws ParseException {
        String[] cells = new String[formats.length];
        int offset = 0;
        if (StringUtil.isEmpty(line))
            return new String[0];
        else {
            for (int i = 0; i < formats.length; i++) {
                PadFormat format = formats[i];
                String cell = line.substring(offset, Math.min(offset + format.getLength(), line.length()));
                cells[i] = (String) format.parseObject(cell);
                offset += format.getLength();
            }
            return cells;
        }
    }
    
}
