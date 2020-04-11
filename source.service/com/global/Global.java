package com.global;

import org.apache.log4j.Logger;

public class Global {
  public static class getLogger {
    public static final Logger log4j = Logger.getLogger("");
    
    public static void all(String procName, String message) {
      log4j.debug("Class:" + procName + "\t" + message);
    }
    
    public static void trace(String procName, String message) {
      log4j.trace("Class:" + procName + "\t" + message);
    }
    
    public static void debug(String procName, String message) {
      log4j.debug("Class:" + procName + "\t" + message);
    }
    
    public static void info(String procName, String message) {
      log4j.info("Class:" + procName + "\t" + message);
    }
    
    public static void info(String procName, String methodsName, String message) {
        log4j.info("Method:" + procName + "." + methodsName + "\t" + message);
      }
    
    public static void warn(String procName, String message) {
      log4j.warn("Class:" + procName + "\t" + message);
    }
    
    public static void error(String procName, String message) {
      log4j.error("Class:" + procName + "\t" + message);
    }
    
    public static void error(String procName, String message, Throwable t) {
      log4j.error("Class:" + procName + "\t" + message, t);
    }
  }
}
