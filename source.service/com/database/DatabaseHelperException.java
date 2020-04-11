package com.database;

public class DatabaseHelperException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public DatabaseHelperException() {}
  
  public DatabaseHelperException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }
  
  public DatabaseHelperException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
  
  public DatabaseHelperException(String arg0) {
    super(arg0);
  }
  
  public DatabaseHelperException(Throwable arg0) {
    super(arg0);
  }
}
