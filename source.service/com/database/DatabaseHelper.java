package com.database;

import com.configuration.Config;
import com.global.Global;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServlet;

public class DatabaseHelper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Integer sqlMaxLogResultSize = Integer.valueOf(100);

	private static final Integer sqlMaxUpdateSize = Integer.valueOf(1000);

	public static Connection getConnection() throws DatabaseHelperException {
		Connection conn = null;
		String databaseClassName = Config.getConfigValue("DatabaseClassName");
		String databaseConnectionUrl = Config.getConfigValue("DatabaseConnectionUrl");
		String databaseUserName = Config.getConfigValue("DatabaseUserName");
		String databasePassword = Config.getConfigValue("DatabasePassword");
		try {
			Class.forName(databaseClassName);
			conn = DriverManager.getConnection(databaseConnectionUrl, databaseUserName, databasePassword);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException e) {
			throw new DatabaseHelperException(e.getMessage(), e);
		}
		return conn;
	}

	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement) throws DatabaseHelperException {
		return query(dbConn, sqlStatement, new Object[] {});
	}

	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement, List<Object> whereCluase) throws DatabaseHelperException {
		return query(dbConn, sqlStatement, whereCluase.toArray());
	}

	public static ArrayList<ArrayList<Object>> query(Connection dbConn, String sqlStatement, Object[] whereCluase) throws DatabaseHelperException {
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
				while (queryResult.next()) {
					ArrayList<Object> row = new ArrayList<Object>();
					for (int index = 1; index <= rsmd.getColumnCount(); index++)
						row.add(queryResult.getObject(index));
					resultSetList.add(row);
				}
				sqlStatementLog(stmt, resultSetList.size(), (new Date()).getTime() - queryStartTime.getTime());
			} catch (SQLException e) {
				Global.getLogger.debug(DatabaseHelper.class.getName(), sqlBuffer + (whereCluase == null ?	 "" : ", " + Arrays.toString(whereCluase)));
				throw new DatabaseHelperException(e.getMessage(), e);
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					if (queryResult != null)
						queryResult.close();
				} catch (SQLException e) {
					throw new DatabaseHelperException(e.getMessage(), e);
				}
			}
			return resultSetList;
		}
	}

	public static void databaseSetValue(PreparedStatement stmt, Object[] whereCluase) throws SQLException {
		if (whereCluase != null && whereCluase.length > 0) {
			for (int index = 1; index <= whereCluase.length; index++) {
				Object value = whereCluase[index - 1];
				stmt.setObject(index, value);
			}
			stmt.addBatch();
		}
	}

	public static int update(Connection dbConn, String sqlStatement, Object[] whereCluaseArr) throws DatabaseHelperException {
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
			throw new DatabaseHelperException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new DatabaseHelperException(e.getMessage(), e);
			}
		}
	}

	public static int update(Connection dbConn, String[] sqlStatementArr, Object[][] whereCluaseArr) throws DatabaseHelperException {
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
			throw new DatabaseHelperException(e.getMessage(), e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				throw new DatabaseHelperException(e.getMessage(), e);
			}
		}
	}

	public static Object getValue(ArrayList<ArrayList<Object>> dbResultList, int row, int column) {
		if (row > dbResultList.size()) {
			throw new IndexOutOfBoundsException("dbResultList max row of " + dbResultList.size() + ", cannot get " + row + " index row");
		}
		Object dbValue = dbResultList.get(row).get(column);
		if (dbValue == null) {
			return null;
		}
		return dbValue;
	}

	public static void sqlStatementLog(Statement jdbcSQLStatment, int updateRows, long processTime) {
		String sqlStatement = jdbcSQLStatment.toString();
		int sqlLength = sqlStatement.length();
		int sqlResultLimit = Math.min(sqlMaxLogResultSize.intValue(), sqlLength);
		StringBuffer sqlLog = new StringBuffer(sqlStatement.substring(sqlStatement.indexOf(":") + 1, sqlResultLimit));
		if (sqlLength > sqlResultLimit) {
			sqlLog.append("...");
		}
		sqlLog.append(" [" + updateRows + " rows, " + processTime + "ms]");
		Global.getLogger.debug(DatabaseHelper.class.getName(), sqlLog.toString());
	}
}
