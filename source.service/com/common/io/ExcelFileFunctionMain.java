package com.common.io;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServlet;

import com.common.filereader.StreamingReaderXlsx;
import com.common.util.DateTimeUtils;
import com.common.util.FileUtil;
import com.global.Global;
import com.global.GlobalException;

public class ExcelFileFunctionMain extends HttpServlet {

	private static final long serialVersionUID = 5423673353589454994L;
	private static final String CONVSOURCEFILEURI = System.getProperty("catalina.base") + "/logs/workbook.xls";
	private static final String importSourceFile = System.getProperty("catalina.base") + "/logs/xls.xls";
	
	private static final String importXlsxSourceFile = "C://Users/Admain/eclipse-workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/logs/workbook.xlsx";

	private String dateformet = "yyyy-MM-dd HH:mm:ss";
	private final String status = "import";

	public enum FileConvertFormat {
		XlsxConvXls("xlsx", "xls"), XlsConvXlsx("xls", "xlsx");
		public String inputExtension;
		public String outputExtension;

		FileConvertFormat(String inputExtension, String outputExtension) {

		}
	}
	
	public void init() {
		Date startTime = new Date();
		ArrayList<ArrayList<ArrayList<Object>>> record = null;
		try {
			String fileType = FileUtil.getFileExtension(importXlsxSourceFile);
			switch (fileType) {
			case "xlsx":
				StreamingReaderXlsx stream = new StreamingReaderXlsx();
				record = stream.precessXLSXFile(importXlsxSourceFile);
				break;
			case "xls":

				break;
			default:
				throw new GlobalException("Unsupper fle type :" + new File(importXlsxSourceFile).getName());
			}
		} catch (Exception e) {
			Global.getLogger.error(this.getClass().getSimpleName(), e);
		}
		System.out.println(record);
		SimpleDateFormat formater = new SimpleDateFormat(dateformet);
		Date endTime = new Date();
		Global.getLogger.debug("File path : " + importXlsxSourceFile);
		Global.getLogger.debug("Start time : " + formater.format(startTime));
		Global.getLogger.debug("End time : " + formater.format(endTime));
		Global.getLogger.debug("Total import time : " + DateTimeUtils.getDatePoor(startTime, endTime));
	}

}
