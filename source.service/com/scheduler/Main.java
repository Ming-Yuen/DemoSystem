package com.scheduler;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public class Main {

	public static void main(String[] arg) {
		try {
			File myObj = new File("C:\\Users\\Admin\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\logs");
			for(File f : myObj.listFiles()) {
				if(!f.getName().startsWith("DemoSystem")) {
					continue;
				}
				Scanner myReader = new Scanner(f);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					if(data.contains("insert into") || data.contains("insert into".toUpperCase())) {
						if(!data.contains("DatabaseHelper")) {
							continue;
						}
						System.out.println(StringUtils.substringBefore(StringUtils.substringAfter(data, "DatabaseHelper"), ") [")+");");
					}
				}
				myReader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
}