package com.configuration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Config {
	private static final Logger logger = Logger.getLogger(Config.class);
	private static final Properties prop = new Properties();
	private static final String configProperties = "config.properties";

	public static String getConfigValue(String configName) {
		try {
			if (!prop.contains(configName)) {
				try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configProperties);){	
					if (inputStream != null) {
						prop.load(inputStream);
					} else {
						throw new FileNotFoundException("property file '" + configProperties + "' not found in the classpath");
					}
				}
			}
			return prop.getProperty(configName);
		} catch (Exception e) {
			logger.error(Config.class.getName(), e);
		}
		throw new RuntimeException("Config " + configName + " not found");
	}

	public static void writeConfigValue(String configName, String configValue) {
		try {
			try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configProperties);){
				prop.load(inputStream);
			}
			prop.setProperty(configName, configValue);
			try(FileOutputStream fos = new FileOutputStream((Config.class.getResource("/") + configProperties).substring(6));){
				prop.store(fos, null);
			}
			logger.debug("update config" + configName + " success in " + configProperties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}