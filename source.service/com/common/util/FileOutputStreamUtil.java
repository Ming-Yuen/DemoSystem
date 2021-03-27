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
import com.global.GlobalException;

public class FileOutputStreamUtil{

	private enum OutputFormat{
		Json(null), Tab("\t"), Csv(","), Xlsx(null), Xls(null);
		
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
	
	public FileOutputStreamUtil() {
		this(Charset.forName("UTF-8"), true);
	}
	
	public FileOutputStreamUtil(boolean append) {
		this(Charset.forName("UTF-8"), append);
	}
	
	public FileOutputStreamUtil(Charset charset, boolean append) {
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
		print(filePath, output);
	}
	
	private void printTextFile(OutputFormat fileFormat, String filePath, Object record) throws IOException, GlobalException {
		Objects.requireNonNull(fileFormat, 	"FileFormat cannot null format output");
		Objects.requireNonNull(filePath, 	"FilePath cannot null output file path");
		
		File outputFile = new File(filePath);
		createParentFileIfAbsent(outputFile);

		ArrayList<ArrayList<String>> data = ClassUtil.convToStringValues(record);
		
		StringBuilder output = new StringBuilder();
		for(int index = 0; index < data.size(); index++) {
			output.append(String.join(fileFormat.format, data.get(index)))
				  .append(System.lineSeparator());
		}
		print(filePath, output);
	}
	
	private void print(String filePath, CharSequence output) throws IOException {
		valusIfFalseThrowException(Charset.isSupported(charset.name()), "Unsupport charset " + charset.name());
		
		try(FileOutputStream stream = new FileOutputStream(filePath, append)){
			FileLock lock = stream.getChannel().tryLock();
			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset))){
				writer.write(output.toString());
				writer.newLine();
				lock.release();
			}
		}
	}
	
	public static String tabConvJson(ArrayList<ArrayList<Object>> record) {
		if(record.isEmpty()) {
			throw new IllegalArgumentException("Record cannot null or empty");
		}
		//First row JSON key
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
	
	private void valusIfFalseThrowException(boolean obj, String error) {
		if(!obj) {
			throw new IllegalArgumentException(error);
		}
	}
	
	private void createParentFileIfAbsent(File outputFile) throws IOException {
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
}
