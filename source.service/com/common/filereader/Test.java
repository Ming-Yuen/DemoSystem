package com.common.filereader;

import java.io.IOException;
import java.util.ArrayList;

import com.global.GlobalException;

public class Test {

	public static void main(String[] arg) throws GlobalException, IOException {
		StreamingReaderXlsx read = new StreamingReaderXlsx();
		ArrayList<ArrayList<ArrayList<Object>>> xlsx = read.precessXLSXFile("C:\\Users\\Admin\\Documents\\test\\excel.xlsx");
		System.out.print(xlsx);
	}
}
