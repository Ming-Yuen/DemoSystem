package com.common.tools;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import com.common.util.DateUtils;
import com.common.util.FileOutputStreamUtil;
import com.container.Container;
import com.database.DatabaseHelper;
import com.database.DbException;
import com.global.Global;
import com.global.GlobalException;

public class DatabaseBackupFile {

	public static void backupAllSchema(Connection dbConn) throws DbException, IOException, GlobalException {
		String schema = DatabaseHelper.getSchema(dbConn);
		ArrayList<ArrayList<Object>> tableNameList = DatabaseHelper.query(dbConn, "select table_name from information_schema.tables where table_schema = ?", new Object[] {schema});
		if(tableNameList.isEmpty()) {
			Global.getLogger.info(DatabaseBackupFile.class.getName(), String.format("Database schema : %s empty table will not backup", schema));
			return;
		}else {
			FileOutputStreamUtil writer = new FileOutputStreamUtil(false);
			for(int tableIndex = 0; tableIndex < tableNameList.size(); tableIndex++) {
				String tableName = DatabaseHelper.<String>getValue(tableNameList, tableIndex, 0);
				Global.getLogger.info(DatabaseBackupFile.class.getName(), String.format("Backup %s start", tableName));
				ArrayList<ArrayList<Object>> tableAllRow = DatabaseHelper.queryIncludeColumnName(dbConn, "select * from " + tableName);
				if(tableAllRow.isEmpty()) {
					Global.getLogger.info(DatabaseBackupFile.class.getName(), String.format("%s data empty will not back file", tableName));
					continue;
				}
//				System.out.println(FileOutputStreamUtil.tabConvJson(tableAllRow));
				writer.printFormatJson(Container.catalinaLogPath + tableName + DateUtils.convDateToString(new Date(), "yyyyMMdd") + ".txt", FileOutputStreamUtil.tabConvJson(tableAllRow));
				Global.getLogger.info(DatabaseBackupFile.class.getName(), String.format("Backup %s finish", tableName));
			}
		}
	}
}
