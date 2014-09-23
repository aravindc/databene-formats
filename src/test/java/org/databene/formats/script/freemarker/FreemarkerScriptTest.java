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

package org.databene.formats.script.freemarker;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

import org.databene.commons.Context;
import org.databene.commons.context.DefaultContext;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;
import org.databene.formats.script.freemarker.FreeMarkerScript;
import org.junit.Test;

import freemarker.template.Configuration;

/**
 * Tests the {@link FreeMarkerScript}.<br/><br/>
 * Created: 12.06.2007 17:36:30
 * @since 0.1
 * @author Volker Bergmann
 */
public class FreemarkerScriptTest {

	@Test
    public void testScriptGetInstance() throws IOException, ScriptException {
        Script script = new FreeMarkerScript("src/test/resources/org/databene/formats/script/freemarker/test.ftl", new Configuration());
        Context context = new DefaultContext();
        context.set("var_dings", "XYZ");
        StringWriter writer = new StringWriter();
        script.execute(context, writer);
        assertEquals("TestXYZTest", writer.toString());
    }
	
}
