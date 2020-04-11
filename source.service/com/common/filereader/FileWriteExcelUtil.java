package com.common.filereader;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.file.annotation.ExcelFieldName;

public class FileWriteExcelUtil {

	public static final Integer bufferSize = 100;
	
	public <T> void writeXlsxExcelRecord(List<T> dataRecordList, String filePath, Class<T> dataSourceType) throws Exception {
		try(Workbook workbook = new SXSSFWorkbook(bufferSize)){
			Sheet sheet = workbook.createSheet();
			printHeader(dataSourceType, sheet);
			Integer hdrIndex = sheet.getPhysicalNumberOfRows();
			for(int index = 0; index < dataRecordList.size(); index++) {
				T dataSource = dataRecordList.get(index);
				Row nextRow = sheet.createRow(index + hdrIndex);
				createCell(dataSource, nextRow);
			}

			try(FileOutputStream os = new FileOutputStream(filePath)){
				workbook.write(os);
			}
		}
	}

	public <T> void printHeader(Class<T> dataSourceType, Sheet sheet) throws InstantiationException, IllegalAccessException {
		Field[] fields = dataSourceType.getFields();
		Row hdrRow = sheet.createRow(0);
		for(int index = 0; index < fields.length; index++) {
			Field field = fields[index];
			ExcelFieldName cellFieldName =  field.getAnnotation(ExcelFieldName.class);
			if(cellFieldName == null) {
				hdrRow.createCell(index).setCellValue("");
			}else {
				String fieldColName = cellFieldName.value();
				hdrRow.createCell(index).setCellValue(fieldColName);
			}
		}
	}
	
	public void createCell(Object dataSource, Row newRow) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = dataSource.getClass().getFields();
		for(int index = 0; index < fields.length; index++) {
			Field field = fields[index];
			Object value = field.get(dataSource);
			Cell cell = newRow.createCell(index);
			setCellValue(cell, value);
		}
	}
	
	public void setCellValue(Cell cell, Object value) {
		if(value instanceof String) {
			cell.setCellValue((String) value);
		}else if(value instanceof Date){
			cell.setCellValue((Date) value);
		}else if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}else if(value instanceof Number) {
			cell.setCellValue((Double) value);
		}else if(value instanceof Boolean){
			cell.setCellValue((Boolean) value);
		}else {
			throw new RuntimeException("Unsupport " + value.getClass() + " field type");
		}
	}
}
