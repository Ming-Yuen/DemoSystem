package com.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.log4j.Logger;
import com.global.GlobalException;

public class FileUtil {
	
	private static Logger logger = Logger.getLogger(FileUtil.class.getSimpleName());
	
	public static String getFileExtension(String fileName) throws GlobalException {
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			String extension = fileName.substring(i + 1);
			return extension;
		} else {
			throw new GlobalException("File not fond extension");
		}
	}

	public static void downloadFileByURL(String downloadUrl, String location) throws IOException {
		FileOutputStream outStream = null;
		BufferedInputStream inStream = null;
		try {
			inStream = new BufferedInputStream(new URL(downloadUrl).openStream());
			outStream = new FileOutputStream(location);
			int count = 0;
			byte[] b = new byte[1024];
			while ((count = inStream.read(b)) != -1) {
				outStream.write(b, 0, count);
			}
			logger.debug("download " + downloadUrl + " in " + location + " success");
		} finally {
			if(inStream != null) {inStream.close();}
			if(outStream != null) {outStream.close();}
		}
	}
	
	public static void createParentFileIfAbsent(File file) throws IOException {
		if(!file.exists()) {
			File directory = file.getParentFile();
			if(!directory.exists()) {
				if(!directory.mkdirs()) {
					throw new IOException("System cannot create output file directory in " + directory.getCanonicalPath());
				}
			}
		}
	}
	
	public static void createFileIfAbsent(File file) throws IOException {
		if(!file.exists()) {
			createParentFileIfAbsent(file);
			if(!file.createNewFile()){
				throw new IOException("System cannot create output file in " + file.getCanonicalPath());
			}
		}
	}
}
