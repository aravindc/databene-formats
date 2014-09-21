/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.html;

import java.io.File;
import java.io.IOException;

import org.databene.commons.FileUtil;

/**
 * {@link Anchor} implementation which relates to a {@link File} 
 * and provides the resolution of relative paths.<br/><br/>
 * Created: 13.06.2011 12:29:36
 * @since 0.5.8
 * @author Volker Bergmann
 */
public class FileAnchor extends Anchor {

	public final File file;
	
	public FileAnchor(File file, String label) {
		super(label);
		this.file = file;
	}
	
	public FileAnchor(File file, String label, String target) {
		super(label, target);
		this.file = file;
	}
	
	public static FileAnchor createAnchorForNewWindow(File file, String label) {
		return new FileAnchor(file, label, "_blank");
	}
	
	public String relativeLinkFrom(File referer) {
		return linkForURL(FileUtil.relativePath(referer, file, '/'));
	}
	
	@Override
	public String toString() {
		try {
			return linkForURL("file://" + file.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
			return linkForURL("file://" + file.getAbsolutePath());
		}
	}

	private String linkForURL(String url) {
		return "<a href='" + url + "'" + (target != null ? " target='" + target + "'" : "") + ">" + label + "</a>";
	}
	
}
