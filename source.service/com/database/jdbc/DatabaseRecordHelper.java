package com.database.jdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.database.jdbc.annotation.DatabaseColumn;
import com.database.jdbc.annotation.DatabasePrimaryKey;
import com.database.jdbc.annotation.DatabaseTable;

public class DatabaseRecordHelper {
	
	public enum Execute{
		insert, update, updateThenInsert 
	}
	
	private static void checkRecordAnnotation(Class<?> clazz) {
		DatabaseTable type = clazz.getAnnotation(DatabaseTable.class);
		if(type == null) {
			throw new DbException(clazz.getName() + " not found " + DatabaseTable.class.getSimpleName() + " annotation");
		}
		if(type.tableName().isEmpty()) {
			throw new DbException(clazz.getName() + " " + DatabaseTable.class.getSimpleName() + " tableName cannot empty");
		}
		
		//record class no primary key or column name will error 
		boolean primary_key_exists = false;
		HashSet<String> column_name_list = new HashSet<String>();
		for(Field field : clazz.getFields()) {
			// check column, column name cannot empty
			DatabaseColumn column_name = field.getAnnotation(DatabaseColumn.class);
			if(column_name != null) {
				if(column_name.columnName().isEmpty()) {
					throw new DbException(clazz.getName() + field.getName() + " annotation column name cannot empty" );
				}
				if(column_name_list.contains(column_name.columnName().toUpperCase())) {
					throw new DbException(clazz.getName() + " dulipcate sql column name : " + column_name.columnName() );
				}
				column_name_list.add(column_name.columnName().toUpperCase());
			}
			
			// check primary key column, column name cannot empty
			DatabasePrimaryKey column_primary = field.getAnnotation(DatabasePrimaryKey.class);
			if(column_primary != null){
				if(column_name == null) {
					throw new DbException(clazz.getName() + field.getName() + " annotation column name cannot not exists " + DatabaseColumn.class.getSimpleName());
				}
				if(column_name.columnName().isEmpty()) {
					throw new DbException(clazz.getName() + field.getName() + " annotation column name cannot empty" );
				}
				primary_key_exists = true;
			}
		}
		if(!primary_key_exists) {
			throw new DbException(clazz.getName() + " not found primary key annotation, annotation : " + DatabasePrimaryKey.class.getName());
		}
		if(column_name_list.isEmpty()) {
			throw new DbException(clazz.getName() + " not found column annotation, annotation : " + DatabaseColumn.class.getSimpleName());
		}
	}

	public static synchronized <T> ArrayList<T> queryRecord(Connection dbConn, Class<T> record) {
		return queryRecord(dbConn, record, null, null);
	}
	
	public static synchronized <T> ArrayList<T> queryRecord(Connection dbConn, Class<T> clazz, String whereClause, Object[] values) {
		ArrayList<T> sqlRecordList = new ArrayList<T>();
		checkRecordAnnotation(clazz);
		
		ArrayList<String>   sqlFieldList = SqlRecord.getColumnFields(clazz);
		StringBuffer sql = SqlRecord.getSqlQueryStatment(clazz, whereClause);
		
		ArrayList<ArrayList<Object>> sqlResult = DatabaseHelper.query(dbConn, sql.toString().toUpperCase(), values);
		
		try {
			for (int sqlRow = 0; sqlRow < sqlResult.size(); sqlRow++) {
				T sqlRecord = clazz.newInstance();
				for (int sqlColumn = 0; sqlColumn < sqlFieldList.size(); sqlColumn++) {
					Field column = sqlRecord.getClass().getField(sqlFieldList.get(sqlColumn));
					if(column.getType() == String.class && sqlResult.get(sqlRow).get(sqlColumn) != null) {
						column.set(sqlRecord, String.valueOf(sqlResult.get(sqlRow).get(sqlColumn)));
					}else {
						column.set(sqlRecord, sqlResult.get(sqlRow).get(sqlColumn));
					}
				}
				sqlRecordList.add(sqlRecord);
			}
		}catch( InstantiationException | SecurityException |IllegalAccessException | NoSuchFieldException e) {
			throw new DbException(e);
		}
		return sqlRecordList;
	}
	
	public static <T> Integer insert(Connection dbConn, T record) {
		return execute(dbConn, Arrays.asList(record), Execute.insert);
	}

	public static <T> Integer insert(Connection dbConn, List<T> record) {
		return execute(dbConn, record, Execute.insert);
	}
	
	public static <T> Integer update(Connection dbConn, T record) {
		return execute(dbConn, Arrays.asList(record), Execute.update);
	}

	public static <T> Integer update(Connection dbConn, List<T> record) {
		return execute(dbConn, record, Execute.update);
	}

	public static <T> Integer execute(Connection dbConn, T record, Execute execute) {
		return execute(dbConn, Arrays.asList(record), execute);
	}

	public static <T> Integer execute(Connection dbConn, List<T> record, Execute execute) {
		PreparedStatement stmt = null;
		Date queryStartTime = new Date();
		try {
			for (int index = 0; index < record.size(); index++) {
				T dbRecord = record.get(index);
				
				ArrayList<String> sqlFieldList 	   = SqlRecord.getColumnFields(dbRecord.getClass());
				ArrayList<String> sqlPrimaryFields = SqlRecord.getPrimaryFields(dbRecord.getClass());
				
				StringBuffer sqlFields = SqlRecord.getSqlUpdateStatment(dbRecord.getClass(), sqlFieldList, sqlPrimaryFields, execute);
				stmt = dbConn.prepareStatement(sqlFields.toString().toUpperCase());
				List<Object> columnValues = SqlRecord.getSqlFieldValues(dbRecord, sqlFieldList, sqlPrimaryFields, execute);
				DatabaseHelper.databaseSetValue(stmt, columnValues.toArray());
			}
			if(stmt == null) {
				throw new DbException("PreparedStatement is null");
			}
			int[] updateRows = stmt.executeBatch();
			int ttlUpdateRows = Arrays.stream(updateRows).sum();
			DatabaseHelper.sqlStatementLog(stmt, ttlUpdateRows, new Date().getTime() - queryStartTime.getTime());
			return Integer.valueOf(ttlUpdateRows);
		} catch (SQLException e) {
			throw new DbException(e);
		}finally {
			try {
				if(stmt != null) {
					stmt.close();
				}
			}catch(SQLException e){
				throw new DbException(e);
			}
		}
	}

	public static <T extends DatabaseRecordClass> DatabaseSchema getTableSchema(Connection dbConn, Class<T> record) {
		DatabaseSchema sc = new DatabaseSchema();
		try {
			StringBuffer sql = SqlRecord.getSqlQueryStatment(record, null);
			PreparedStatement stmt = dbConn.prepareStatement(sql.toString());
			ResultSet queryResult = stmt.executeQuery();
			ResultSetMetaData rsmd = queryResult.getMetaData();
			for (int index = 1; index <= rsmd.getColumnCount(); index++) {
				String columnName = rsmd.getColumnName(index);
				String columnType = rsmd.getColumnTypeName(index);
				sc.dataType.put(columnName, columnType);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
		return sc;
	}

	public static class DatabaseSchema {
		public Map<String, String> dataType = new HashMap<>();
	}
	
	public static class SqlRecord{
		
		public static String getTableNameFields(Class<?> clazz) {
			DatabaseTable table = clazz.getAnnotation(DatabaseTable.class);
			return table.tableName();
		}
		
		public static ArrayList<String> getPrimaryFields(Class<?> clazz) {
			ArrayList<String> columnName = new ArrayList<String>();
			for(Field field : clazz.getFields()) {
				DatabaseColumn column_name = field.getAnnotation(DatabaseColumn.class);
				DatabasePrimaryKey primary_name = field.getAnnotation(DatabasePrimaryKey.class);
				if(primary_name != null) {
					columnName.add(column_name.columnName());
				}
			}
			return columnName;
		}
		
		public static ArrayList<String> getColumnFields(Class<?> clazz) {
			ArrayList<String> columnName = new ArrayList<String>();
			for(Field field : clazz.getDeclaredFields()) {
				DatabaseColumn column_name = field.getAnnotation(DatabaseColumn.class);
				if(column_name != null) {
					columnName.add(column_name.columnName());
				}
			}
			return columnName;
		}
		
		public static StringBuffer getSqlQueryStatment(Class<?> clazz, String whereClause) {
			String 					sqlTable = SqlRecord.getTableNameFields(clazz);
			ArrayList<String>   sqlFieldList = SqlRecord.getColumnFields(clazz);
			String 					sqlField = String.join(",", sqlFieldList);
			
			StringBuffer sql = new StringBuffer().append("SELECT ").append(sqlField).append(" FROM ").append(sqlTable);
			if(whereClause != null && whereClause.trim().isEmpty()) {
				sql.append(" where ").append(whereClause);
			}
			return sql;
		}
		
		public static StringBuffer getSqlUpdateStatment(Class<?> clazz, ArrayList<String> sqlFields, ArrayList<String> sqlPrimaryFields, Execute execute) {
			String sqlPkField = String.join(" = ? and ", getPrimaryFields(clazz)) + " = ? ";

			StringBuffer sqlStatement = new StringBuffer();
			switch(execute) {
				case update:
					sqlStatement.append("update ").append(SqlRecord.getTableNameFields(clazz))
								.append(" set ").append(String.join(" = ?, ", getColumnFields(clazz))).append(" = ?")
								.append(" where ").append(sqlPkField);
					break;
				case insert:
					ArrayList<String> fieldList = getColumnFields(clazz);
					sqlStatement.append("insert into ").append(SqlRecord.getTableNameFields(clazz))
								.append(" (").append(String.join(", ", fieldList)).append(")")
								.append(" values (").append(StringUtils.repeat("?", ",", fieldList.size())).append(")");
					break;
				default:
					break;
			}
			return sqlStatement;
		}
		
		public static ArrayList<Object> getSqlFieldValues(Object record, ArrayList<String> sqlFields, ArrayList<String> sqlPrimaryFields, Execute execute) {
			ArrayList<Object> values = new ArrayList<Object>();
			
			HashMap<String, Object> annotationCache = new HashMap<String, Object>();
			
			Field[] varField = record.getClass().getFields();
			try {
				for(Field var : varField) {
					DatabaseColumn type = var.getAnnotation(DatabaseColumn.class);
					if(type != null) {
						annotationCache.put(type.columnName(), var.get(record));
					}
				}
			}catch(IllegalAccessException e){
				throw new DbException(e);
			}
			
			for(String sqlField : sqlFields) {
				Object value = annotationCache.get(sqlField);
				values.add(value);
			}
			if(Execute.update == execute) {
				for(String sqlField : sqlPrimaryFields) {
					Object value = annotationCache.get(sqlField);
					values.add(value);
				}
			}
			return values;
		}
	}
}
