package com.global;

public class GlobalException extends Exception{

	private static final long serialVersionUID = 1L;

	public GlobalException() {
		super();
	}

	public GlobalException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public GlobalException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public GlobalException(String arg0) {
		super(arg0);
	}

	public GlobalException(Throwable arg0) {
		super(arg0);
	}
	
}
