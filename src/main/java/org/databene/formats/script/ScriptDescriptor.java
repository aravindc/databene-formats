/*
 * (c) Copyright 2010 by Volker Bergmann. All rights reserved.
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

import org.databene.commons.StringUtil;

/**
 * Describes a script.<br/><br/>
 * Created: 09.08.2010 16:40:50
 * @since 0.5.4
 * @author Volker Bergmann
 */
public class ScriptDescriptor {
	
	public final String scriptEngine;
	public final ScriptLevel level;
	public final String text;

	public ScriptDescriptor(String text) {
		if (text != null && text.startsWith("{") && text.endsWith("}")) {
			text = text.substring(1, text.length() - 1);
	        String[] tokens = StringUtil.splitOnFirstSeparator(text, ':');
	        if (tokens.length > 1 && ScriptUtil.getFactory(tokens[0], false) != null) {
	            this.scriptEngine = tokens[0];
	            this.text = tokens[1];
	        } else {
	        	this.scriptEngine = ScriptUtil.getDefaultScriptEngine();
	        	this.text = text;
	        }
			this.level = ScriptLevel.SCRIPT;
		} else {
			this.scriptEngine = null;
			this.level = ScriptLevel.NONE;
			this.text = text;
		}
    }
	
}
