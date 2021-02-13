package com.common.util.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.common.util.FileOutputStreamUtil;
import com.global.GlobalException;

public class FileOutputStreamUtilTest {

	public static void main(String[] arg) throws IOException, GlobalException {
		Student st = new Student();
		st.id = "";
		st.name = "Ming";
		
		File f = new File("C:\\Users\\Admin\\Documents\\A\\output\\output.txt");
		
		FileOutputStreamUtil outputUtil = new FileOutputStreamUtil(Charset.forName("Utf-8"), false);
		outputUtil.printTabFile(f.getPath(), st);
	}
}
