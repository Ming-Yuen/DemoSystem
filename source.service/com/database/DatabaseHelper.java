package com.database;

import com.configuration.Config;
import com.global.Global;
import com.mysql.cj.jdbc.ConnectionImpl;

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
import java.util.List;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;

public class DatabaseHelper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Integer sqlMaxLogResultSize = Integer.valueOf(10000);

	private static final Integer sqlMaxUpdateSize = Integer.valueOf(1000);

	public static Connection getConnection() throws DbException {
		Connection conn = null;
		String dbDriver = Config.getConfigValue("DatabaseClassName");
		String dbConnUrl = Config.getConfigValue("DatabaseConnectionUrl");
		String dbUserName = Config.getConfigValue("DatabaseUserName");
		String dbPassword = Config.getConfigValue("DatabasePassword");
		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DbException(e.getMessage(), e);
		}
		return conn;
	}
	
	public static String getSchema(Connection dbConn) {
		try {
			if(dbConn instanceof ConnectionImpl) {
				return ((ConnectionImpl)dbConn).getDatabase();
			}
		}catch(Exception e) {
			new DbException(e);
		}
		return null;
	}

	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement) throws DbException {
		return query(dbConn, sqlStatement, null, false);
	}

	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement, List<Object> whereCluase) throws DbException {
		return query(dbConn, sqlStatement, whereCluase.toArray(), false);
	}
	
	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement, Object[] whereCluase) throws DbException {
		return query(dbConn, sqlStatement, whereCluase, false);
	}
	
	public static ArrayList<ArrayList<Object>> queryIncludeColumnName(Connection dbConn, String sqlStatement) throws DbException {
		return query(dbConn, sqlStatement, null, true);
	}
	
	public static ArrayList<ArrayList<Object>> queryIncludeColumnName(Connection dbConn, String sqlStatement, Object[] whereCluase) throws DbException {
		return query(dbConn, sqlStatement, whereCluase, true);
	}

	private static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement, Object[] whereCluase, boolean includeColName) throws DbException {
		synchronized (DatabaseHelper.class) {
			ArrayList<ArrayList<Object>> resultSetList = new ArrayList<>();
			PreparedStatement stmt = null;
			ResultSet queryResult = null;
			StringBuffer sqlBuffer = new StringBuffer(sqlStatement);
			try {
				Date queryStartTime = new Date();
				stmt = dbConn.prepareStatement(sqlBuffer.toString());
				databaseSetValue(stmt, whereCluase);
				queryResult = stmt.executeQuery();
				ResultSetMetaData rsmd = queryResult.getMetaData();
				
				boolean isFirstRow = true;
				while (queryResult.next()) {
					ArrayList<Object> colNameRow = new ArrayList<Object>();
					ArrayList<Object> row 		 = new ArrayList<Object>();
					for (int index = 1; index <= rsmd.getColumnCount(); index++) {
						if(includeColName && isFirstRow) {
							colNameRow.add(rsmd.getColumnLabel(index));
						}
						row.add(queryResult.getObject(index));
					}
					if(includeColName && isFirstRow) {
						resultSetList.add(colNameRow);
					}
					resultSetList.add(row);
					isFirstRow = false;
				}
				sqlStatementLog(stmt, resultSetList.size(), (new Date()).getTime() - queryStartTime.getTime());
			} catch (SQLException e) {
				Global.getLogger.debug(DatabaseHelper.class.getName(), sqlBuffer + (whereCluase == null ?	 "" : ", " + Arrays.toString(whereCluase)));
				throw new DbException(e.getMessage(), e);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					if (queryResult != null)
						queryResult.close();
				} catch (SQLException e) {
					throw new DbException(e.getMessage(), e);
				}
			}
			return resultSetList;
		}
	}

	public static void databaseSetValue(PreparedStatement stmt, Object[] whereCluase) throws SQLException {
		if (whereCluase != null && whereCluase.length > 0) {
			for (int index = 1; index <= whereCluase.length; index++) {
				Object value = whereCluase[index - 1];
				if(value instanceof Date) {
					stmt.setTimestamp(index, new Timestamp(((Date)value).getTime()));
				}else {
					stmt.setObject(index, value);
				}
			}
			stmt.addBatch();
		}
	}

	public static int update(Connection dbConn, String sqlStatement, Object[] whereCluaseArr) throws DbException {
		PreparedStatement stmt = null;
		Date queryStartTime = new Date();
		try {
			stmt = dbConn.prepareStatement(sqlStatement);
			if (whereCluaseArr != null) {
				databaseSetValue(stmt, whereCluaseArr);
			}
			int row = stmt.executeUpdate();
			sqlStatementLog(stmt, row, (new Date()).getTime() - queryStartTime.getTime());
			return row;
		} catch (Exception e) {
			throw new DbException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage(), e);
			}
		}
	}

	public static int update(Connection dbConn, String[] sqlStatementArr, Object[][] whereCluaseArr) throws DbException {
		PreparedStatement stmt = null;
		int ttlUpdateRows = 0;
		Date queryStartTime = new Date();
		try {
			for (int index = 0; index <= sqlStatementArr.length; index++) {
				queryStartTime = new Date();
				String sql = sqlStatementArr[index];
				stmt = dbConn.prepareStatement(sql);
				if (whereCluaseArr != null) {
					databaseSetValue(stmt, whereCluaseArr[index]);
				}
				stmt.addBatch();
				if (index % sqlMaxUpdateSize.intValue() == 0) {
					ttlUpdateRows += Arrays.stream(stmt.executeBatch()).sum();
					stmt.clearBatch();
				}
			}
			ttlUpdateRows += Arrays.stream(stmt.executeBatch()).sum();
			sqlStatementLog(stmt, ttlUpdateRows, (new Date()).getTime() - queryStartTime.getTime());
			return ttlUpdateRows;
		} catch (SQLException e) {
			throw new DbException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage(), e);
			}
		}
	}

	public static <T> T getValue(ArrayList<ArrayList<Object>> dbResultList, int row, int column) {
		if (row > dbResultList.size()) {
			throw new IndexOutOfBoundsException("dbResultList max row of " + dbResultList.size() + ", cannot get " + row + " index row");
		}
		Object dbValue = dbResultList.get(row).get(column);
		if (dbValue == null) {
			return null;
		}
		return (T) dbValue;
	}

	public static void sqlStatementLog(Statement jdbcSQLStatment, int updateRows, long processTime) {
		String sqlStatement = jdbcSQLStatment.toString();
//		int sqlLength = sqlStatement.length();
//		int sqlResultLimit = Math.min(sqlMaxLogResultSize, sqlLength);
//		StringBuffer sqlLog = new StringBuffer(sqlStatement.substring(sqlStatement.indexOf(":") + 1, sqlResultLimit));
//		if (sqlLength > sqlResultLimit) {
//			sqlLog.append("...");
//		}
//		sqlLog.append(" [" + updateRows + " rows, " + processTime + "ms]");
		String sqlLog = StringUtils.abbreviate(sqlStatement.substring(sqlStatement.indexOf(":") + 1), sqlMaxLogResultSize) + " [" + updateRows + " rows, " + processTime + "ms]";
		Global.getLogger.debug(DatabaseHelper.class.getName(), sqlLog.toString());
	}
}
