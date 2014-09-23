/*
 * (c) Copyright 2008-2014 by Volker Bergmann. All rights reserved.
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

import static org.junit.Assert.*;

import org.databene.commons.Context;
import org.databene.commons.context.DefaultContext;
import org.databene.formats.script.ScriptUtil;
import org.junit.Test;

/**
 * Test the ScriptSupport class.<br/><br/>
 * Created: 27.01.2008 17:38:51
 * @author Volker Bergmann
 */
public class ScriptSupportTest {

	@Test
    public void testRender() {
        assertEquals("xyz", ScriptUtil.evaluate("xyz", null));
        assertEquals("xyz${var}xyz", ScriptUtil.evaluate("xyz${var}xyz", null));
        Context context = new DefaultContext();
        context.set("var", "!!!");
        assertEquals("xyz!!!xyz", ScriptUtil.evaluate("{xyz${var}xyz}", context));
        assertEquals("xyz!!!xyz", ScriptUtil.evaluate("{ftl:xyz${var}xyz}", context));
    }
	
}
