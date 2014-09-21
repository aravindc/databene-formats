/*
 * (c) Copyright 2009-2011 by Volker Bergmann. All rights reserved.
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

package org.databene.formats.xls;

import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.databene.commons.ConfigurationError;
import org.databene.commons.Converter;
import org.databene.commons.MathUtil;
import org.databene.commons.converter.ToStringConverter;

/**
 * Provides utility methods for HSSF (POI).<br/>
 * <br/>
 * Created at 09.08.2009 07:47:52
 * @since 0.5.0
 * @author Volker Bergmann
 */

public class XLSUtil {

	private XLSUtil() { }
	
	public static Object resolveCellValue(Cell cell) {
		return resolveCellValue(cell, "'", null, null);
	}
	
	public static Object resolveCellValue(Cell cell, String emptyMarker, String nullMarker, Converter<String, ?> stringPreprocessor) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING: return convertString(cell, emptyMarker, nullMarker, stringPreprocessor);
			case Cell.CELL_TYPE_NUMERIC: 
				if (HSSFDateUtil.isCellDateFormatted(cell))
					return cell.getDateCellValue();
				else
					return mapNumberType(cell.getNumericCellValue());
			case Cell.CELL_TYPE_BOOLEAN: return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_BLANK: 
			case Cell.CELL_TYPE_ERROR: return cell.getRichStringCellValue().getString();
			case Cell.CELL_TYPE_FORMULA:
				FormulaEvaluator evaluator = createFormulaEvaluator(cell); 
				CellValue cellValue = evaluator.evaluate(cell);
				switch (cellValue.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING: return convertString(cellValue, emptyMarker, stringPreprocessor);
				    case HSSFCell.CELL_TYPE_NUMERIC:
				    	if (HSSFDateUtil.isCellDateFormatted(cell))
				    		return HSSFDateUtil.getJavaDate(cellValue.getNumberValue());
				    	else
				    		return mapNumberType(cellValue.getNumberValue());
				    case Cell.CELL_TYPE_BOOLEAN: return cellValue.getBooleanValue();
				    case HSSFCell.CELL_TYPE_BLANK:
				    case HSSFCell.CELL_TYPE_ERROR: return null;
				    default: throw new IllegalStateException("Unexpected cell type: " + cellValue.getCellType());
				    	// CELL_TYPE_FORMULA is not supposed to be encountered here
				}	
			default: throw new ConfigurationError("Not a supported cell type: " + cell.getCellType());
		}
	}
	
	/** Resolves a formula or a normal cell and formats the result as it would be displayed in Excel */
	public static String resolveCellValueAsString(Cell cell) {
		return resolveCellValueAsString(cell, "'", null, null);
	}
	
	/** Resolves a formula or a normal cell and formats the result as it would be displayed in Excel */
	public static String resolveCellValueAsString(Cell cell, String emptyMarker, String nullMarker, Converter<String, ?> stringPreprocessor) {
		if (cell == null)
			return null;
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
	    	String content = cell.getRichStringCellValue().getString();
	    	if (content != null) {
		    	if (content.equals(emptyMarker) || content.equals("'"))
		    		content = "";
		    	else if (content.equals(nullMarker))
		    		content = null;
	    	}
	    	if (stringPreprocessor != null)
	    		content = ToStringConverter.convert(stringPreprocessor.convert(content), null);
	    	return content;
		} else {
			DataFormatter formatter = new DataFormatter();
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
				return formatter.formatCellValue(cell, createFormulaEvaluator(cell));
			else
				return formatter.formatCellValue(cell);
		}
	}

	public static void autoSizeColumns(Workbook workbook) {
		int sheetCount = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetCount; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			int firstRowNum = sheet.getFirstRowNum();
			if (firstRowNum >= 0) {
				Row firstRow = sheet.getRow(firstRowNum);
				for (int cellnum = firstRow.getFirstCellNum(); cellnum < firstRow.getLastCellNum(); cellnum++)
					sheet.autoSizeColumn(cellnum);
			}
		}
	}
	
	public static boolean isEmpty(Row row) {
		if (row == null)
			return true;
		for (int i = 0; i < row.getLastCellNum(); i++)
			if (!isEmpty(row.getCell(i)))
				return false;
		return true;
	}
	
	public static boolean isEmpty(Cell cell) {
		if (cell == null)
			return true;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
			return true;
		if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
			return cell.getStringCellValue().isEmpty();
		return false;
	}
	
	
	// private helpers -------------------------------------------------------------------------------------------------
	
	private static FormulaEvaluator createFormulaEvaluator(Cell cell) {
		return cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
	}

	private static Number mapNumberType(double numericCellValue) {
		if (MathUtil.isIntegralValue(numericCellValue))
			return ((Double) numericCellValue).longValue();
		return numericCellValue;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object convertString(CellValue cellValue, String emptyMarker, Converter<?, ?> stringPreprocessor) {
    	String content = cellValue.getStringValue();
    	if (content != null && (content.equals(emptyMarker) || content.equals("'")))
    		content = "";
    	return (stringPreprocessor != null ? ((Converter) stringPreprocessor).convert(content) : content);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object convertString(Cell cell, String emptyMarker, String nullMarker, Converter<?, ?> stringPreprocessor) {
    	String content = cell.getRichStringCellValue().getString();
    	if (content != null) {
	    	if (content.equals(emptyMarker) || content.equals("'"))
	    		content = "";
	    	if (content.equals(nullMarker))
	    		content = null;
    	}
    	return (stringPreprocessor != null ? ((Converter) stringPreprocessor).convert(content) : content);
    }

	public static int getColumnCount(Sheet sheet) {
		int columnCount = 0;
		Iterator<Row> rowIterator = sheet.rowIterator();
		while (rowIterator.hasNext())
			columnCount = Math.max(columnCount, rowIterator.next().getLastCellNum());
		return columnCount;
	}

}
