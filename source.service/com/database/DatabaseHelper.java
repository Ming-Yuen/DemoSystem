package com.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

import com.configuration.Config;
import com.configuration.ConfigurationMenu;

public class DatabaseHelper extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DatabaseHelper.class.getSimpleName());

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		String databaseClassName 		= Config.getConfigValue(ConfigurationMenu.DatabaseClassName);
		String databaseConnectionUrl 	= Config.getConfigValue(ConfigurationMenu.DatabaseConnectionUrl);
		String databaseUserName 		= Config.getConfigValue(ConfigurationMenu.DatabaseUserName);
		String databasePassword 		= Config.getConfigValue(ConfigurationMenu.DatabasePassword);
		try {
			Class.forName(databaseClassName);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword);
		return conn;
	}
	
	public static DatabaseHelperResult query(Connection dbConn, String sqlStatement) throws SQLException {
		return query(dbConn, sqlStatement, null);
	}

	public static DatabaseHelperResult query(Connection dbConn, String sqlStatement, Object[] whereCluase) throws SQLException {
		DatabaseHelperResult dbHelperResult = new DatabaseHelperResult();
		PreparedStatement stmt = null;
		ResultSet queryResult = null;
		try {
			Date queryStartTime = new Date();
			stmt = dbConn.prepareStatement(sqlStatement);
			// SQL where clause
			if(whereCluase != null) {
				for(int paraIndex = 0; paraIndex < whereCluase.length; paraIndex++ ) {
					if (whereCluase[paraIndex] instanceof Date) {
						stmt.setTimestamp(paraIndex, 		new Timestamp(((Date) whereCluase[paraIndex]).getTime()));
				    } else if (whereCluase[paraIndex] instanceof Integer) {
				    	stmt.setInt(paraIndex + 1, 			(Integer) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof Long) {
				    	stmt.setLong(paraIndex + 1, 		(Long) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof Double) {
				    	stmt.setDouble(paraIndex + 1, 		(Double) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof BigDecimal) {
				    	stmt.setBigDecimal(paraIndex + 1, 	(BigDecimal) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof Float) {
				    	stmt.setFloat(paraIndex + 1, 		(Float) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof String){
				    	stmt.setString(paraIndex + 1, 		(String) whereCluase[paraIndex]);
				    } else if (whereCluase[paraIndex] instanceof Boolean){
				    	stmt.setBoolean(paraIndex + 1, 		(Boolean) whereCluase[paraIndex]);
				    }
				}
			}
			queryResult = stmt.executeQuery();
			
			// create column name in ArrayList result
			ResultSetMetaData rsmd = queryResult.getMetaData();
			for (int index = 1; index <= rsmd.getColumnCount(); index++) {
				dbHelperResult.columnNameMap.put(rsmd.getColumnName(index).toUpperCase(), index - 1);
			}

			// create query SQL result in ArrayList result
			while (queryResult.next()) {
				ArrayList<Object> row = new ArrayList<Object>();
				for (int index = 1; index <= dbHelperResult.columnNameMap.size(); index++) {
					row.add(queryResult.getObject(index));
				}
				dbHelperResult.resultSetList.add(row);
			}
			logger.debug(stmt.toString().substring(stmt.toString().lastIndexOf(":") + 1) + "[" + dbHelperResult.resultSetList.size() +" rows, " + (new Date().getTime() - queryStartTime.getTime()) + "ms]");
		}finally {
			if( stmt != null ) { stmt.close(); }
			if( queryResult != null ) { queryResult.close(); }
		}
		return dbHelperResult;
	}
	
	public int update(Connection dbConn, String[] sqlStatement, Object[] whereCluase) throws SQLException {
		Statement stmt = null;
		try {
			stmt = dbConn.createStatement();
			for (String query : sqlStatement) {
				stmt.addBatch(query);
			}
			int[] updateRows = stmt.executeBatch();
			return Arrays.stream(updateRows).sum();
		}finally {
			if( stmt != null ) { stmt.close(); }
		}
	}

	public static class DatabaseHelperResult {
		
		public LinkedHashMap<String, Integer> columnNameMap = new LinkedHashMap<String, Integer>();
		public ArrayList<ArrayList<Object>> resultSetList = new ArrayList<ArrayList<Object>>();
		
		public Object getObject( int index, String columnLabel ) {
			int columnIndex = columnNameMap.get(columnLabel.toUpperCase());
			return resultSetList.get(index).get(columnIndex);
		}
	}
}