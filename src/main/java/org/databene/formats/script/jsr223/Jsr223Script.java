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

package org.databene.formats.script.jsr223;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.script.ScriptEngine;

import org.databene.commons.Assert;
import org.databene.commons.Context;
import org.databene.formats.script.Script;
import org.databene.formats.script.ScriptException;

/**
 * Provides {@link Script} functionality based on JSR 227: Scripting for the Java platform.<br/>
 * <br/>
 * Created at 23.12.2008 07:19:54
 * @since 0.4.7
 * @author Volker Bergmann
 */

public class Jsr223Script implements Script {
	
	private ScriptEngine engine;

	private String text;

	public Jsr223Script(String text, ScriptEngine engine) {
		Assert.notEmpty(text, "text");
		Assert.notNull(engine, "engine");
		this.text = text;
		this.engine = engine;
	}

	@Override
	public Object evaluate(Context context) throws ScriptException {
		try {
			engine.put("benerator", context);
			for (Map.Entry<String, Object> entry : context.entrySet())
				engine.put(entry.getKey(), entry.getValue());
			return engine.eval(text);
		} catch (javax.script.ScriptException e) {
			throw new ScriptException("Error in evaluating script", e);
		}
	}

	@Override
	public void execute(Context context, Writer out) throws ScriptException, IOException {
		out.write(String.valueOf(evaluate(context)));
	}

	@Override
	public String toString() {
		return text;
	}
}
