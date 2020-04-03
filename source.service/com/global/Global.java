package com.global;

import org.apache.log4j.Logger;

public class Global {
  public static class getLogger {
    public static final Logger log4j = Logger.getLogger("");
    
    public static void all(String processName, String message) {
      log4j.debug(processName + "\t" + message);
    }
    
    public static void trace(String processName, String message) {
      log4j.trace(processName + "\t" + message);
    }
    
    public static void debug(String processName, String message) {
      log4j.debug(processName + "\t" + message);
    }
    
    public static void info(String processName, String message) {
      log4j.info(processName + "\t" + message);
    }
    
    public static void warn(String processName, String message) {
      log4j.warn(processName + "\t" + message);
    }
    
    public static void error(String processName, String message) {
      log4j.error(processName + "\t" + message);
    }
    
    public static void error(String processName, String message, Throwable t) {
      log4j.error(processName + "\t" + message, t);
    }
  }
}
