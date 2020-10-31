package com.global;

import org.apache.log4j.Logger;

public class Global {
  public static class getLogger {
    public static final Logger log4j = Logger.getLogger("");
    
    public static void all(String procName, String message) {
      log4j.debug(procName + "\t" + message);
    }
    
    public static void trace(String procName, String message) {
      log4j.trace(procName + "\t" + message);
    }
    
    public static void debug(String procName, String message) {
      log4j.debug(procName + "\t" + message);
    }
    
    public static void info(String procName, String message) {
      log4j.info(procName + "\t" + message);
    }
    
    public static void info(String procName, String methodsName, String message) {
        log4j.info("Method:" + procName + "." + methodsName + "\t" + message);
      }
    
    public static void warn(String procName, String message) {
      log4j.warn(procName + "\t" + message);
    }
    
    public static void error(String procName, String message) {
      log4j.error(procName + "\t" + message);
    }
    
    public static void error(String procName, String message, Throwable t) {
      log4j.error(procName + "\t" + message, t);
    }
  }
}
