package com.common.filereader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.global.GlobalException;
import com.monitorjbl.xlsx.StreamingReader;

public class StreamingReaderXlsx {
	private final int oneTimeRowIndex = 100;

	public ArrayList<ArrayList<ArrayList<Object>>> precessXLSXFile(String filePath) throws GlobalException, IOException {
		InputStream is = new FileInputStream(new File(filePath));
		Workbook workbook = StreamingReader.builder().rowCacheSize(oneTimeRowIndex).bufferSize(4096).open(is);
		ArrayList<ArrayList<ArrayList<Object>>> record = new ArrayList<ArrayList<ArrayList<Object>>>();

		for (Sheet sheet : workbook) {
			ArrayList<ArrayList<Object>> sheetRecord = new ArrayList<ArrayList<Object>>();
			for (Row row : sheet) {
				ArrayList<Object> rowRecord = new ArrayList<Object>();
				for (Cell cell : row) {
					switch (cell.getCellTypeEnum()) {
					case BOOLEAN:
						rowRecord.add(cell.getBooleanCellValue());
						break;
					case NUMERIC:
						rowRecord.add(cell.getNumericCellValue());
						break;
					case STRING:
						rowRecord.add(cell.getStringCellValue());
						break;
					case BLANK:
						rowRecord.add("");
						break;
					case FORMULA:
						rowRecord.add(cell.getCellFormula());
						break;
					case _NONE:
						rowRecord.add(null);
						break;
					case ERROR:
						throw new GlobalException("Excel file data type error");
					}
				}
				sheetRecord.add(rowRecord);
			}
			record.add(sheetRecord);
		}
		workbook.close();
		is.close();
		return record;
	}

	public static void getRow(Row row, ArrayList<Object> rowOfIndex) throws Exception {
		for (Cell cell : row) {
			switch (cell.getCellTypeEnum()) {
			case BOOLEAN:
				rowOfIndex.add(cell.getBooleanCellValue());
				break;
			case NUMERIC:
				rowOfIndex.add(cell.getNumericCellValue());
				break;
			case STRING:
				rowOfIndex.add(cell.getStringCellValue());
				break;
			case BLANK:
				rowOfIndex.add("");
				break;
			case FORMULA:
				rowOfIndex.add(cell.getCellFormula());
				break;
			case _NONE:
				rowOfIndex.add(null);
				break;
			case ERROR:
				throw new Exception("Excel file data type error");
			}
		}
	}
}
