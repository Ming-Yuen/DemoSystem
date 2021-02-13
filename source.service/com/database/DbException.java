package com.database;

public class DbException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public DbException() {}
  
  public DbException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }
  
  public DbException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
  
  public DbException(String arg0) {
    super(arg0);
  }
  
  public DbException(Throwable arg0) {
    super(arg0);
  }
}
