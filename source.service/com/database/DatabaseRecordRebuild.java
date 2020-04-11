package com.database;

import com.global.Global;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class DatabaseRecordRebuild {
	public static final Integer limitSize = Integer.valueOf(100);

	public static final String DatabaseDoc = String.valueOf(System.getProperty("catalina.base")) + File.separator
			+ "wtpwebapps" + File.separator + "DemoSystemBackEnd" + File.separator + "DatabaseDoc";

	public static void rebuildDatabase(Connection dbConn) {
		FileInputStream fis = null;
		BufferedReader reader = null;
		ArrayList<String> sqlList = new ArrayList<>();
		StringBuffer sqlStatement = new StringBuffer();
		try {
			WildcardFileFilter wildcardFileFilter = new WildcardFileFilter("*.sql");
			File f = new File(DatabaseDoc);
			File[] fileArr = f.listFiles((FileFilter) wildcardFileFilter);
			for (int indexFile = 0; indexFile < fileArr.length; indexFile++) {
				fis = new FileInputStream(fileArr[indexFile]);
				reader = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				boolean endQuotationMark = true;
				while ((line = reader.readLine()) != null) {
					if (line.trim().isEmpty())
						continue;
					char[] charArr = line.toCharArray();
					char lasterChar = Character.MIN_VALUE;
					byte b;
					int i;
					char[] arrayOfChar1;
					for (i = (arrayOfChar1 = charArr).length, b = 0; b < i;) {
						char c = arrayOfChar1[b];
						if (lasterChar != '\\' && c == '\'') {
							endQuotationMark = !endQuotationMark;
							sqlStatement.append(c);
						} else if (c == ';' && endQuotationMark) {
							sqlList.add(sqlStatement.toString());
							sqlStatement.setLength(0);
						} else {
							sqlStatement.append(c);
							lasterChar = c;
						}
						b++;
					}
					if (sqlList.size() >= limitSize.intValue()) {
						List<String> sqlSubList = sqlList.subList(0, limitSize.intValue());
						DatabaseHelper.update(dbConn, sqlSubList.<String>toArray(new String[sqlSubList.size()]),
								(Object[][]) null);
						sqlList.clear();
					}
				}
			}
			DatabaseHelper.update(dbConn, sqlList.<String>toArray(new String[sqlList.size()]), (Object[][]) null);
		} catch (Exception e) {
			Global.getLogger.error(DatabaseRecordRebuild.class.getName(), e.getMessage(), e);
		}
	}
}
