package com.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

import com.global.Global;
import com.global.GlobalException;

import antlr.collections.List;

public class ClassUtil {

	public static <T> void referenceFields(T copyRecord, T pasteRecord) throws Exception {
		Objects.requireNonNull(pasteRecord);
		if(copyRecord == null) {
			pasteRecord = null;
			return;
		}
		if(copyRecord.getClass() == pasteRecord.getClass()) {
			throw new Exception("Copy record and paste record data type is different");
		}
		Field[] fields = copyRecord.getClass().getFields();
		for(int index = 0; index < fields.length; index++) {
			Field copyRecordField = fields[index];
			copyRecordField.setAccessible(true);
			Object copyRecordFieldValue = copyRecordField.get(copyRecord);
			
			Field pasteRecordField = pasteRecord.getClass().getField(copyRecordField.getName());
			pasteRecordField.setAccessible(true);
			pasteRecordField.set(pasteRecord, copyRecordFieldValue);
		}
	}

	public static <T> T getFieldValue(Object record, String fieldName) throws Exception {
		Field field = record.getClass().getField(fieldName);
		field.setAccessible(true);
		return (T) field.get(record);
	}
	
	public static ArrayList<ArrayList<String>> convToStringValues(Object recordArray) throws GlobalException{
		return fieldToArrayData(new Object[] { recordArray }, null);
	}
	
	public static ArrayList<ArrayList<String>> convToStringValues(Object[] recordArray) throws GlobalException{
		return fieldToArrayData(recordArray, null);
	}
	
	public static ArrayList<ArrayList<String>> fieldToArrayData(Object[] recordArray, String replaceNullValue) throws GlobalException{
		if(recordArray == null || recordArray.length == 0) {
			throw new IllegalArgumentException("Class field to array data cannot null or empty");
		}
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		Field[] fields = null;
		for(int index = 0; index < recordArray.length; index++) {
			Object record = recordArray[index];
			if(record == null) {
				Global.getLogger.warn(ClassUtil.class.getName(), "ClassUtil Field convert data is null, index : " + index);
				continue;
			}
			if(fields == null) {
				fields = record.getClass().getDeclaredFields();
			}
			
			ArrayList<String> dataRow = new ArrayList<String>();
			for(int indexField = 0; indexField < fields.length; indexField++) {
				Object value = null;
				try {
					value = fields[indexField].get(record);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new GlobalException(e);
				}
				value = value == null ? replaceNullValue : value;
				dataRow.add(value.toString());
			}
			data.add(dataRow);
		}
		return data;
	}
}
