/*
 * (c) Copyright 2013 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.util;

/**
 * Provides general utility methods for data files.<br/><br/>
 * Created: 05.06.2013 18:17:20
 * @since 0.6.17
 * @author Volker Bergmann
 */

public class DataFileUtil {
	
	public static boolean isPlainTextDocument(String fileName) {
		return hasSuffixIgnoreCase(".txt", fileName);
	}

	public static boolean isDbUnitDocument(String fileName) {
		return hasSuffixIgnoreCase(".dbunit.xml", fileName);
	}
	
	public static boolean isXmlDocument(String fileName) {
		return hasSuffixIgnoreCase(".xml", fileName);
	}
	
	public static boolean isExcelOrCsvDocument(String fileName) {
		return isExcelDocument(fileName) || isCsvDocument(fileName);
	}
	
	public static boolean isExcelDocument(String fileName) {
		return isBinaryExcelDocument(fileName) || isXmlExcelDocument(fileName);
	}
	
	public static boolean isBinaryExcelDocument(String fileName) {
		return hasSuffixIgnoreCase(".xls", fileName);
	}

	public static boolean isXmlExcelDocument(String fileName) {
		return hasSuffixIgnoreCase(".xlsx", fileName);
	}
	
	public static boolean isCsvDocument(String fileName) {
		return hasSuffixIgnoreCase(".csv", fileName);
	}
	
	public static boolean isFixedColumnWidthFile(String fileName) {
		return hasSuffixIgnoreCase(".fcw", fileName);
	}

	public static boolean hasSuffixIgnoreCase(String suffix, String fileName) {
		return fileName.toLowerCase().endsWith(suffix);
	}

}
