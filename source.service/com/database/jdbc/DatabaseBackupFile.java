package com.database.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.common.util.DateUtils;
import com.common.util.FileOutputStreamUtil;
import com.container.Container;
import com.global.Global;
import com.global.GlobalException;

public class DatabaseBackupFile {

	public static void backupSchema(Connection dbConn) throws DbException, IOException, GlobalException {
		String schema = DatabaseHelper.getSchema(dbConn);
		ArrayList<ArrayList<Object>> tableNameList = DatabaseHelper.query(dbConn, "select table_name from information_schema.tables where table_schema = ?", new Object[] {schema});
		if(tableNameList.isEmpty()) {
			Global.getLogger.info(DatabaseBackupFile.class.getName(), String.format("Database schema : %s empty table will not backup", schema));
			return;
		}else {
			HashMap<String, String> backup = new HashMap<String, String>(); 
			FileOutputStreamUtil writer = new FileOutputStreamUtil(false);
			for(int tableIndex = 0; tableIndex < tableNameList.size(); tableIndex++) {
				String tableName = DatabaseHelper.<String>getValue(tableNameList, tableIndex, 0);
				ArrayList<ArrayList<Object>> tableAllRow = DatabaseHelper.queryIncludeColumnName(dbConn, "select * from " + tableName);
				if(tableAllRow.isEmpty()) {
					continue;
				}
				String fileName = Container.catalinaLogPath + tableName + DateUtils.convDateToString(new Date(), "yyyyMMdd") + ".txt";
				writer.printFormatJson(fileName, FileOutputStreamUtil.tabConvJson(tableAllRow));
				backup.put(tableName, fileName);
				
			}
			if(!backup.isEmpty()) {
				Global.getLogger.info(String.format("System backup schema %s completed, location by %s", schema, Container.catalinaLogPath));
			}
		}
	}
}
