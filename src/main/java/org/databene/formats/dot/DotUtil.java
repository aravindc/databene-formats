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

package org.databene.formats.dot;

import java.util.Collection;

import org.databene.commons.StringUtil;

/**
 * Provides utility methods for formatting Fot files.<br/><br/>
 * Created: 25.05.2014 05:59:44
 * @since 0.8.2
 * @author Volker Bergmann
 */

public class DotUtil {

	public static String normalizeId(String id) {
		if (id.startsWith("\"") && id.startsWith("\""))
			return id;
		else if (id.contains(" ") || id.contains("-"))
			return '"' + id + '"';
		else
			return id;
	}
	
	public static String normalizeColor(String color) {
		if (StringUtil.isEmpty(color))
			return null;
		String baseColor = (color.startsWith("\"") && color.endsWith("\"") ? color.substring(1, color.length() - 1) : color);
		return (baseColor.contains("#") ? '"' + baseColor + '"' : baseColor);
	}
	
	public static String segmentsToLabel(Collection<?> segments, boolean vertical) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		if (segments != null) {
			for (Object segment : segments) {
				if (!first)
					builder.append('|');
				builder.append(segment);
				first = false;
			}
			if (builder.length() == 0)
				return null;
			if (!vertical)
				builder.insert(0, "{").append("}");
			return builder.toString();
		} else {
			return null;
		}
	}
	
	public static String formatLines(String... lines) {
		StringBuilder builder = new StringBuilder();
		for (String line : lines)
			addLine(line, builder);
		return builder.toString();
	}
	
	public static void addLine(String line, StringBuilder builder) {
		builder.append(line).append("\\l");
	}
	
}
