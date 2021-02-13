package com.common.util.test;

import com.file.annotation.FileFieldName;

public class Student {

	@FileFieldName("Student id")
	public String id;
	
	@FileFieldName("Student name")
	public String name;
}
