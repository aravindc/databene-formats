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

package org.databene.formats.script;

import org.databene.commons.Context;
import org.databene.commons.DocumentWriter;
import org.databene.commons.context.DefaultContext;

import java.io.Writer;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * A DocumentWriter that uses {@link Script}s for rendering head, body parts and footer.<br/>
 * <br/>
 * Created: 07.06.2007 11:32:09
 * @author Volker Bergmann
 */
public class ScriptedDocumentWriter<E> implements DocumentWriter<E> {

    private Writer out;
    private Map<String, Object> vars;

    private Script headerScript;
    private Script bodyPartScript;
    private Script footerScript;

    private boolean writeHeader;

    public ScriptedDocumentWriter(Writer out, String headerScriptUrl, String bodyPartScriptUrl, String footerScriptUrl)
            throws IOException {
        this(   out,
                (headerScriptUrl != null ? ScriptUtil.readFile(headerScriptUrl) : null),
                (bodyPartScriptUrl != null ? ScriptUtil.readFile(bodyPartScriptUrl) : null),
                (footerScriptUrl != null ? ScriptUtil.readFile(footerScriptUrl) : null)
        );
    }

    public ScriptedDocumentWriter(Writer out, Script headerScript, Script bodyPartScript, Script footerScript) {
        this.out = out;
        this.headerScript = headerScript;
        this.bodyPartScript = bodyPartScript;
        this.footerScript = footerScript;
        this.vars = new HashMap<String, Object>();
        this.writeHeader = true;
    }

    public Script getHeaderScript() {
        return headerScript;
    }

    public void setHeaderScript(Script headerScript) {
        this.headerScript = headerScript;
    }

    public Script getFooterScript() {
        return footerScript;
    }

    public void setFooterScript(Script footerScript) {
        this.footerScript = footerScript;
    }
    
    public void setWriteHeader(boolean writeHeader) {
		this.writeHeader = writeHeader;
	}

    // Script interface implementation ---------------------------------------------------------------------------------

	@Override
	public void setVariable(String name, Object value) {
        vars.put(name, value);
    }

    @Override
	public void writeElement(E part) throws IOException {
        if (writeHeader) {
            writeHeader();
            writeHeader = false;
        }
        if (bodyPartScript != null) {
            Context context = new DefaultContext();
            context.set("var", vars);
            context.set("part", part);
            bodyPartScript.execute(context, out);
        }
    }

    @Override
	public void close() throws IOException {
        writeFooter();
        out.close();
    }

    // helpers ---------------------------------------------------------------------------------------------------------

    protected void writeHeader() throws IOException {
        if (headerScript != null) {
            Context context = new DefaultContext();
            context.set("var", vars);
            headerScript.execute(context, out);
        }
    }

    protected void writeFooter() throws IOException {
        if (footerScript != null) {
            Context context = new DefaultContext();
            context.set("var", vars);
            footerScript.execute(context, out);
        }
    }
}
