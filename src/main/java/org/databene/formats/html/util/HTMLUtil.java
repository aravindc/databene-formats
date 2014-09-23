/*
 * (c) Copyright 2007-2010 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.html.util;

import org.databene.commons.CollectionUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.SystemInfo;
import org.databene.formats.html.HtmlEntity;

import java.awt.Color;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides utility methods for HTML processing.<br/>
 * <br/>
 * Created: 15.06.2007 19:42:19
 * @author Volker Bergmann
 */
public class HTMLUtil {
	
	private static final String LF = SystemInfo.getLineSeparator();
    private static final Set<String> EMPTY_TAGS = CollectionUtil.toSet("br", "img", "meta", "link");
	public static final String DOCTYPE_401 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";

	static final String[] BROWSERS = { 
		"google-chrome", "firefox", "opera", "epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla", "netscape", "links", "lynx" };
	
	public static void openBrowser(String url) {
		try { 
			// On JDK 1.6+, I can use java.awt.Desktop.getDesktop().browse().
			// On older Java versions, this would not compile. Thus the reflection based invocation
			Class<?> desktopClass = Class.forName("java.awt.Desktop"); 
			Method browseMethod = desktopClass.getDeclaredMethod("browse", new Class[] { URI.class });
			Method getDesktopMethod = desktopClass.getDeclaredMethod("getDesktop");
			Object desktop = getDesktopMethod.invoke(null);
			browseMethod.invoke(desktop, new Object[] { URI.create(url) }); 
		} catch (Exception ignore) { 
			// older JVM version or invocation failed
			// try invoking the browser in OS specific ways
			try { 
				if (SystemInfo.isMacOsx()) { 
					// On Mac OS X, use FileManager.openURL(url)
					Class<?> fileManager = Class.forName("com.apple.eio.FileManager");
					Method openUrlMethod = fileManager.getDeclaredMethod( "openURL", new Class[] { String.class });
					openUrlMethod.invoke(null, new Object[] { url }); 
				} else if (SystemInfo.isWindows()) { 
					// On Windows, call rundll32 url.dll,FileProtocolHandler <url>
					Runtime.getRuntime().exec( "rundll32 url.dll,FileProtocolHandler " + url); 
				} else { 
					// Otherwise, assume Unix or Linux 
					// and try to open the browser on the command line
					String browser = null; 
					for (String candidate : BROWSERS) 
						if (browser == null && Runtime.getRuntime().exec(new String[] { "which", candidate }).getInputStream().read() != -1) {
							browser = candidate;
							Runtime.getRuntime().exec(new String[] { candidate, url }); 
						}
					if (browser == null) 
						throw new RuntimeException("No browser found"); 
					} 
				} catch (Exception e) { 
					throw new RuntimeException("Error opening web browser with URL: " + url, e); 
				}
			}
	}

	public static boolean isEmptyTag(String tagName) {
        return EMPTY_TAGS.contains(tagName.toLowerCase());
    }

	public static String unescape(String text) {
		StringBuilder result = new StringBuilder(text.length());
	    int i;
        while ((i = text.indexOf('&')) >= 0) {
            HtmlEntity entity = HtmlEntity.getEntity(text, i);
            if (entity != null) {
                result.append(text.substring(0, i));
                if ("nbsp".equals(entity.htmlCode))
                	result.append(' ');
                else if ("ndash".equals(entity.htmlCode))
                    	result.append('-');
                    else
                	result.append(entity.character);
                text = text.substring(i + entity.htmlCode.length() + 2);
            } else {
                result.append(text.substring(0, i));
                result.append("&");
                text = text.substring(i + 1);
            }
        }
        result.append(text);
        return result.toString();
    }
	
	public static String escape(String value) {
		if (value == null)
			return "";
		value = value.replace("&", "&amp;"); // must be the first conversion
		value = value.replace("<", "&lt;");
		value = value.replace(">", "&gt;");
		value = value.replace("'", "&#39;"); // IE6 and IE7 do not support &apos;
		value = value.replace("\"", "&quot;");
		// TODO replace diacritic characters
		return value;
	}

	public static Map<String, String> parseCGIParameters(String url) {
		Map<String, String> result = new HashMap<String, String>();
		int qmIndex = url.indexOf('?');
		if (qmIndex >= 0)
			url = url.substring(qmIndex + 1);
		String[] nvPairs = url.split("&");
		for (String nvPair : nvPairs) {
			String[] tokens = nvPair.split("=");
			result.put(tokens[0], tokens[1]);
		}
		return result;
	}
	
	public static String td(String text) {
	    return td(text, null, null);
    }

	public static String td(String text, String alignment) {
		return td(text, alignment);
    }

	public static String td(String text, String alignment, String style) {
		StringBuilder builder = new StringBuilder("<td");
		if (alignment != null)
			builder.append(" align=\"").append(alignment).append('"');
		if (style != null)
			builder.append(" style=\"").append(style).append('"');
		builder.append('>').append(text).append("</td>");
		return builder.toString();
    }

	public static String a(String href, String text) {
	    return "<a href='" + href + "'>" + text + "</a>";
    }

	public static String aNewWindow(String href, String text) {
	    return "<a href='" + href + "' target='_blank'>" + text + "</a>";
    }
	
	public static String hexColorCode(Color color) {
		String colorCode = Integer.toHexString(color.getRGB() & 0xffffff);
		return "000000".substring(colorCode.length()) + colorCode; // pad left to six characters
	}

	public static String convertLineFeeds(String text) {
		List<String> lines = StringUtil.splitLines(text);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lines.size(); i++) {
			if (i > 0)
				builder.append("<br/>").append(LF);
			builder.append(lines.get(i));
		}
		return builder.toString();
	}
	
}
