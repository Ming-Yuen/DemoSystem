package com.api.std;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.database.DatabaseHelper;

public class Entry {

	protected Logger logger = Logger.getLogger(this.getClass().getSimpleName());
	protected Connection dbConn = null;
	
	public void error(Throwable t) {
		logger.error(t.getMessage(), t);
	}
	
	public void getConnection() throws SQLException {
		if(dbConn != null) {
			dbConn = DatabaseHelper.getConnection();
		}
	}
	
	public void finish() {
		try {
			if(dbConn != null && !dbConn.isClosed()) {
				dbConn.close();
			}
			logger.info("end of process");
		}catch(SQLException e) {
			logger.error("1", e);
		}
	}
}
