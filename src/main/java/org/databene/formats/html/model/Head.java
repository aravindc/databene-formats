/*
 * (c) Copyright 2014 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.html.model;

import java.io.IOException;

import org.databene.commons.IOUtil;

/**
 * Represents an HTML &lt;head&gt;.<br/><br/>
 * Created: 16.06.2014 11:06:23
 * @since 0.8.3
 * @author Volker Bergmann
 */

public class Head extends HtmlElement<Head> {
	
	public Head() {
		super("head", false);
	}
	
	public Head withTitle(String title) {
		return this.withAttribute("title", title);
	}

	public Head withCssStyleSheet(String cssPath) {
		Link link = new Link().withRel("stylesheet").withType("text/css").withHref(cssPath);
		return this.addComponent(link);
	}
	
	public Head withInlineCssStyleSheet(String cssFilePath) {
		try {
			String content = IOUtil.getContentOfURI(cssFilePath);
			CssStyle style = new CssStyle().withRawTextContent(content);
			return this.addComponent(style);
		} catch (IOException e) {
			throw new RuntimeException("Error inlining css file: " + cssFilePath, e);
		}
	}
	
}
