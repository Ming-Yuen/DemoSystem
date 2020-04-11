package com.database;

class DatabaseRecordException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public DatabaseRecordException() {}
  
  public DatabaseRecordException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }
  
  public DatabaseRecordException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
  
  public DatabaseRecordException(String arg0) {
    super(arg0);
  }
  
  public DatabaseRecordException(Throwable arg0) {
    super(arg0);
  }
}
