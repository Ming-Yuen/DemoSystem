package com.database;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseRecordHelper {
	private enum columnType {
		columnName, columnValues;
	}

	public static synchronized <T extends DatabaseRecordClass> ArrayList<T> queryRecord(Connection dbConn, Class<T> record) throws DatabaseRecordException {
		ArrayList<T> resultRecordList = new ArrayList<T>();
		try {
			DatabaseRecordClass databaseRecordClass = record.newInstance();
			String[] columnLabelArray = getColumnFields(databaseRecordClass, false).get(columnType.columnName).toArray(new String[0]);
			StringBuffer sql = new StringBuffer("SELECT " + String.join(",", columnLabelArray) + " FROM " + databaseRecordClass.getTableName());
			if (databaseRecordClass.getWhereClauseKey() != null && databaseRecordClass.getWhereClauseKey().toString().trim().length() > 0) {
				sql.append(" where " + databaseRecordClass.getWhereClauseKey());
			}
			ArrayList<ArrayList<Object>> databaseHelperResult = DatabaseHelper.query(dbConn, sql.toString().toUpperCase(), databaseRecordClass.getWhereClauseValues());
			for (int indexRow = 0; indexRow < databaseHelperResult.size(); indexRow++) {
				T databaseRecordClass1 = record.newInstance();
				for (int indexColumn = 0; indexColumn < databaseHelperResult.get(indexRow).size(); indexColumn++) {
					Method mtd = databaseRecordClass1.getClass().getMethod("set" + columnLabelArray[indexColumn],new Class[] {(databaseHelperResult.get(indexRow)).get(indexColumn).getClass() });
					mtd.invoke(databaseRecordClass1,new Object[] {databaseHelperResult.get(indexRow).get(indexColumn) });
				}
				resultRecordList.add(databaseRecordClass1);
			}
		} catch (NoSuchMethodException e) {
			throw new DatabaseRecordException(String.valueOf(e.getMessage()) + record + " has not setter method", e);
		} catch (DatabaseHelperException | java.lang.reflect.InvocationTargetException | IllegalAccessException
				| InstantiationException e) {
			throw new DatabaseRecordException(e.getMessage(), e);
		}
		return resultRecordList;
	}

	public static <T extends DatabaseRecordClass> Integer update(Connection dbConn, T record) throws DatabaseRecordException {
		return update(dbConn, Arrays.asList(record));
	}

	public static <T extends DatabaseRecordClass> Integer update(Connection dbConn, List<T> record) throws DatabaseRecordException {
		PreparedStatement stmt = null;
		Date queryStartTime = new Date();
		try {
			for (int index = 0; index < record.size(); index++) {
				DatabaseRecordClass dbRecord = record.get(index);
				StringBuffer sqlStatement = new StringBuffer("UPDATE " + dbRecord.getTableName() + " set ");
				Map<columnType, List<Object>> column = getColumnFields(dbRecord, true);
				String[] columnLabelArray = column.get(columnType.columnName).toArray(new String[0]);
				sqlStatement.append(String.join(" = ?, ", columnLabelArray) + " = ? ");
				if (dbRecord.getWhereClauseKey() != null && dbRecord.getWhereClauseKey().toString().trim().length() > 0) {
					sqlStatement.append(" where " + dbRecord.getWhereClauseKey());
				}
				stmt = dbConn.prepareStatement(sqlStatement.toString());
				List<Object> columnValues = column.get(columnType.columnValues);
				columnValues.addAll(dbRecord.getWhereClauseValues());
				DatabaseHelper.databaseSetValue(stmt, columnValues.toArray());
			}
			int[] updateRows = stmt.executeBatch();
			int ttlUpdateRows = Arrays.stream(updateRows).sum();
			DatabaseHelper.sqlStatementLog(stmt, ttlUpdateRows, new Date().getTime() - queryStartTime.getTime());
			return Integer.valueOf(ttlUpdateRows);
		} catch (SQLException e) {
			throw new DatabaseRecordException(e.getMessage(), e);
		}
	}

	public static <T extends DatabaseRecordClass> Map<columnType, List<Object>> getColumnFields(T record, boolean valuesObtain) throws DatabaseRecordException {
		ArrayList<Object> columnName = new ArrayList<Object>();
		ArrayList<Object> columnValues = new ArrayList<Object>();
		Arrays.asList(record.getClass().getFields()).stream()
						.filter(f -> !(Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers())))
						.forEach(f -> {
							columnName.add(f.getName());
							if (valuesObtain)
								try {
									columnValues.add(f.get(f.getName()));
								} catch (IllegalArgumentException|IllegalAccessException e) {
									throw new RuntimeException(e.getMessage(), e);
								}  
							});
		if (columnName.isEmpty()) {
			throw new DatabaseRecordException(String.valueOf(record.getClass().getName()) + " record class cannot empty field value");
		}
		Map<columnType, List<Object>> columnHashMap = new HashMap<>();
		columnHashMap.put(columnType.columnName, columnName);
		columnHashMap.put(columnType.columnValues, columnValues);
		return columnHashMap;
	}

	public static <T extends DatabaseRecordClass> DatabaseSchema getTableSchema(Connection dbConn, Class<T> record) throws DatabaseRecordException {
		DatabaseSchema sc = new DatabaseSchema();
		try {
			DatabaseRecordClass databaseRecordClass = (DatabaseRecordClass) record.newInstance();
			String[] columnLabelArray = getColumnFields(databaseRecordClass, false).get(columnType.columnName).toArray(new String[0]);
			StringBuffer sql = new StringBuffer("SELECT " + String.join(",", columnLabelArray) + " FROM " + databaseRecordClass.getTableName());
			PreparedStatement stmt = dbConn.prepareStatement(sql.toString());
			ResultSet queryResult = stmt.executeQuery();
			ResultSetMetaData rsmd = queryResult.getMetaData();
			for (int index = 1; index <= rsmd.getColumnCount(); index++) {
				String columnName = rsmd.getColumnName(index);
				String columnType = rsmd.getColumnTypeName(index);
				sc.dataType.put(columnName, columnType);
			}
		} catch (IllegalAccessException | InstantiationException | DatabaseRecordException | SQLException e) {
			throw new DatabaseRecordException(e.getMessage(), e);
		}
		return sc;
	}

	public static class DatabaseSchema {
		public Map<String, String> dataType = new HashMap<>();
	}
}
