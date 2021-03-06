/*
 * Copyright (C) 2011-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.databene.formats.dot;

import java.util.Collection;

import org.databene.commons.StringUtil;

/**
 * Provides utility methods for formatting Fot files.
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
