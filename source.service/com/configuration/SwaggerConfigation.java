package com.configuration;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerConfigation extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setBasePath("/" + Config.getConfigValue(ConfigurationMenu.projectName)+ "/rest");
		beanConfig.setHost("localhost:"+ServerConfigation.IPv4Port);
		beanConfig.setResourcePackage("com.api");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
//		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setVersion("1.0");
	}
}