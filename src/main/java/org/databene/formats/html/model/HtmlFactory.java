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

/**
 * Creates common HTML elements in utility methods.<br/><br/>
 * Created: 06.01.2014 08:11:27
 * @since 0.7.1
 * @author Volker Bergmann
 */

public class HtmlFactory {
	
	private HtmlFactory() { }
	
	public static Bold bold(String content) {
		return new Bold(content);
	}
	
	public static Bold bold(HtmlComponent content) {
		return new Bold(content);
	}
	
	public static Font font(String content) {
		return new Font(content);
	}
	
	public static Font font(HtmlComponent content) {
		return new Font(content);
	}
	
	public static Anchor urlAnchor(String label, String url) {
		return new Anchor(label).withHref(url);
	}
	
	@SuppressWarnings("rawtypes")
	public static HtmlElement<?> br() {
		return new HtmlElement("br", true);
	}
	
}
