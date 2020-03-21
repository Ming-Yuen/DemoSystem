package com.global;

import org.apache.log4j.Logger;

public class Global{

	public static class getLogger{
		
		public static final Logger log4j = Logger.getLogger(getLogger.class);
		
		public static void all(String message) {
			log4j.debug(message);
		}
		
		public static void trace(String message) {
			log4j.trace(message);
		}

		public static void debug(String message) {
			log4j.debug(message);
		}
		
		public static void info(String message) {
			log4j.info(message);
		}
		
		public static void warn(String message) {
			log4j.warn(message);
		}
		
		public static void error(String message) {
			log4j.error(message);
		}
		
		public static void error(String message, Throwable t) {
			log4j.error(message, t);
		}
	}
}
