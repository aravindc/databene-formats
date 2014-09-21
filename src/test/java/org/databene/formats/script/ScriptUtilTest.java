/*
 * (c) Copyright 2010-2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.script;

import static org.junit.Assert.*;

import org.databene.commons.ParseException;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptDescriptor;
import org.databene.formats.script.ScriptFactory;
import org.databene.formats.script.ScriptLevel;
import org.databene.formats.script.ScriptUtil;
import org.junit.Test;

/**
 * Tests the {@link ScriptUtil} class.<br/><br/>
 * Created: 09.08.2010 16:11:36
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class ScriptUtilTest {

	@Test
	public void testCombineScriptableParts() {
		ScriptUtil.addFactory("xyz", new XyzScriptFactory());
		assertEquals("", ScriptUtil.combineScriptableParts());
		assertEquals("ABCDEF",   ScriptUtil.combineScriptableParts("AB", "CD", "EF"));
		assertEquals("{ABCDEF}", ScriptUtil.combineScriptableParts("{ABC}", "DEF"));
		assertEquals("{ABCDEF}", ScriptUtil.combineScriptableParts("{ABC}", "{DEF}"));
		assertEquals("{ABCDEF}", ScriptUtil.combineScriptableParts("ABC", "{DEF}"));
		assertEquals("{ABCDEF}", ScriptUtil.combineScriptableParts("ABC", "{ftl:DEF}"));
		assertEquals("{SELECT * FROM TT WHERE ID = $n}", 
				ScriptUtil.combineScriptableParts("SELECT * FROM TT", "{ftl: WHERE ID = $n}"));
		assertEquals("{xyz:'SELECT * FROM TT' + ' WHERE ID = ' + n}", 
				ScriptUtil.combineScriptableParts("SELECT * FROM TT", "{xyz:' WHERE ID = ' + n}"));
	}
	
	@Test
	public void testDescribe() {
		ScriptDescriptor[] descriptors = ScriptUtil.describe("alpha", "{ftl:${n}}");
		assertEquals(2, descriptors.length);
		checkDescriptor(descriptors[0], null, "alpha", ScriptLevel.NONE);
		checkDescriptor(descriptors[1], "ftl", "${n}", ScriptLevel.SCRIPT);
	}
	
	@Test
	public void testGetCommonScriptEngine() {
		assertNull(ScriptUtil.getCommonScriptEngine("alpha", "123", "\r\n"));
		assertEquals("ftl", ScriptUtil.getCommonScriptEngine("alpha", "{ftl:${n}}", "\r\n"));
		assertEquals("ftl", ScriptUtil.getCommonScriptEngine("{ftl:alpha}", "{ftl:${n}}", "{ftl:\r\n}"));
	}
	
	@Test
	public void testIsScript() {
		assertTrue(ScriptUtil.isScript("{''}"));
		assertTrue(ScriptUtil.isScript(" { '' } "));
		assertFalse(ScriptUtil.isScript("{sdfw"));
		assertFalse(ScriptUtil.isScript("sdfw}"));
	}
	
    public class XyzScriptFactory implements ScriptFactory {
		@Override
		public Script parseText(String text) throws ParseException { return null; }
		
	    @Override
		public Script readFile(String uri) throws ParseException { return null; }
    }

	private static void checkDescriptor(ScriptDescriptor scriptDescriptor, 
			String scriptEngine, String text, ScriptLevel level) {
		assertEquals(scriptEngine, scriptDescriptor.scriptEngine);
		assertEquals(text, scriptDescriptor.text);
		assertEquals(level, scriptDescriptor.level);
    }

}
