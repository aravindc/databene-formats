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

package org.databene.formats.fixedwidth;

import org.databene.commons.Context;
import org.databene.commons.ConversionException;
import org.databene.commons.SystemInfo;
import org.databene.formats.script.AbstractScript;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;
import org.databene.formats.script.ScriptUtil;
import org.databene.formats.script.ScriptedDocumentWriter;

import java.io.IOException;
import java.io.Writer;

/**
 * Writes arrays as flat file columns.<br/>
 * <br/>
 * Created: 07.06.2007 13:05:38
 * @author Volker Bergmann
 */
public class ArrayFixedWidthWriter<E> extends ScriptedDocumentWriter<E[]> {

    public ArrayFixedWidthWriter(Writer out, FixedWidthRowTypeDescriptor descriptor) {
        this(out, (Script) null, (Script) null, descriptor);
    }

    public ArrayFixedWidthWriter(Writer out, String headerScriptUrl, String footerScriptUrl, FixedWidthRowTypeDescriptor descriptor)
            throws IOException {
        this(
            out,
            (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null),
            (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null),
            descriptor
        );
    }

    public ArrayFixedWidthWriter(Writer out, Script headerScript, Script footerScript, FixedWidthRowTypeDescriptor descriptors) {
        super(
            out,
            headerScript,
            new ArrayFixedWidthScript(descriptors),
            footerScript
        );
    }

    // ArrayFlatFileScript ---------------------------------------------------------------------------------------------

    private static class ArrayFixedWidthScript extends AbstractScript {

        private FixedWidthRowTypeDescriptor descriptor;

        public ArrayFixedWidthScript(FixedWidthRowTypeDescriptor descriptor) {
            this.descriptor = descriptor;
        }

        @Override
        public void execute(Context context, Writer out) throws IOException, ScriptException {
            try {
                Object[] cellsOfCurrentRow = (Object[]) context.get("part");
                out.write(descriptor.formatArray(cellsOfCurrentRow));
                out.write(SystemInfo.getLineSeparator());
            } catch (ConversionException e) {
                throw new ScriptException(e);
            }
        }
    }

}
