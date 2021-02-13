package com.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

import com.common.tools.Gson;
import com.common.tools.JSON;
import com.global.Global;
import com.global.GlobalException;
import com.global.Logger;

public class FileOutputStreamUtil extends FileUtil{

	private enum OutputFormat{
		Json(null), Tab("\t"), Csv(","), Excel(null);
		
		private String format;
		OutputFormat(String format){
			this.format 	= format;
		}
		
		@Override
		public String toString() {
			return format;
		}
	}
	
	private final	Charset	charset;
	private final	boolean	append;
	private			Logger 	logger;
	
	public FileOutputStreamUtil() {
		this(null, Charset.forName("UTF-8"), true);
	}
	
	public FileOutputStreamUtil(boolean append) {
		this(null, Charset.forName("UTF-8"), append);
	}
	
	public FileOutputStreamUtil(Charset charset, boolean append) {
		this(null, charset, append);
	}
	
	public FileOutputStreamUtil(Logger logger, Charset charset, boolean append) {
		this.logger		= logger;
		this.charset 	= charset;
		this.append		= append;
	}
	
	public void printCsvFile(String filePath, Object record) throws IOException, GlobalException {
		printTextFile(OutputFormat.Csv, filePath, record);
	}
	
	public void printTabFile(String filePath, Object record) throws IOException, GlobalException {
		printTextFile(OutputFormat.Tab, filePath, record);
	}
	
	public void printJson(String filePath, Object record) throws IOException, GlobalException {
		printJson(filePath, false, record);
	}
	
	public void printFormatJson(String filePath, Object record) throws IOException, GlobalException {
		printJson(filePath, true, record);
	}
	
	private void printJson(String filePath, boolean formmat, Object record) throws IOException, GlobalException {
		Objects.requireNonNull(filePath, "FilePath cannot null output file path");
		
		File outputFile = new File(filePath);
		createParentFileIfAbsent(outputFile);

		try(FileOutputStream stream = new FileOutputStream(outputFile, append)){
			FileLock lock = stream.getChannel().tryLock();
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream))){
				String data = null;
				if(record instanceof CharSequence) {
					if(JSON.isJSONValid(record.toString())) {
						data = record.toString();
					}else {
						throw new IllegalArgumentException("Record invalid JSON type " + record);
					}
				}else {
					data = Gson.toJson(record);
				}
				String output = formmat ? JSON.formatJSONStr(data) : data;
				writer.write(output);
				writer.newLine();
				lock.release();
			}
		}
		this.writeLogger().info(this.getClass().getName(), "Print file path : " + filePath);
	}
	
	private void printTextFile(OutputFormat fileFormat, String filePath, Object record) throws IOException, GlobalException {
		Objects.requireNonNull(fileFormat, 	"FileFormat cannot null format output");
		Objects.requireNonNull(filePath, 	"FilePath cannot null output file path");
		
		File outputFile = new File(filePath);
		createParentFileIfAbsent(outputFile);

		try(FileOutputStream stream = new FileOutputStream(outputFile, append)){
			FileLock lock = stream.getChannel().tryLock();
			
			try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset))){
				ArrayList<ArrayList<String>> data = ClassUtil.convToStringValues(record);
				
				for(int index = 0; index < data.size(); index++) {
					writer.write(String.join(fileFormat.format, data.get(index)));
					writer.newLine();
				}
				lock.release();
			}
		}
		this.writeLogger().info(this.getClass().getName(), "Print file path : " + filePath);
	}
	
	public static String tabConvJson(ArrayList<ArrayList<Object>> record) {
		if(record.isEmpty()) {
			throw new IllegalArgumentException("Record cannot null or empty");
		}
		List<Object> keys = record.get(0);

		JSONArray jsonArr = new JSONArray();
		
		//first 1 row is filter header keys name row
		for(int index = 1; index < record.size(); index++) {
			List<Object> row = record.get(index);
			
			JSONObject jo = new JSONObject();
			for(int rowIndex = 0; rowIndex < row.size(); rowIndex++) {
				jo.put(keys.get(rowIndex).toString(), row.get(rowIndex));
			}
			jsonArr.put(jo);
		}
		return jsonArr.toString();
	}
	
	public static void createParentFileIfAbsent(File outputFile) throws IOException {
		if(!outputFile.exists()) {
			File directory = outputFile.getParentFile();
			if(!directory.exists()) {
				if(!directory.mkdirs()) {
					throw new IOException("System cannot create output file directory in " + directory.getCanonicalPath());
				}
			}
			if(!outputFile.createNewFile()){
				throw new IOException("System cannot create output file in " + outputFile.getCanonicalPath());
			}
		}
	}
	
	private Logger writeLogger() {
		if(logger == null) {
			return Global.getLogger;
		}
		return logger;
	}
}
