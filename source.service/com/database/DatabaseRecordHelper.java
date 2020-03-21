package com.database;

import java.sql.Connection;

public class DatabaseRecordHelper {

	public <T extends DatabaseRecordClass> void queryRecord( Connection dbConn, T record, String whereClause ) {
		String sql = "SELCT * FROM " + record.getTableName();
	}
}
