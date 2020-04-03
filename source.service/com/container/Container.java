package com.container;

import com.configuration.ServerConfigation;
import com.global.Global;
import com.net.util.HttpConnection;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;

public class Container extends HttpServlet {
	private static final long serialVersionUID = 1963256997966895234L;

	public void init() {
		synchronized (this) {
			Global.getLogger.debug(getClass().getName(), "service start");
			try {
				ServerConfigation.IPprocess();
				HttpConnection.HTTPConnMandatoryField mandatory = new HttpConnection.HTTPConnMandatoryField();
				mandatory.setConnUrl("http://yahoo.com.hk")
						.setConnMethod(HttpConnection.HTTPConnMandatoryField.HTTPMethod.GET)
						.setConnContentType("application/json");
				HttpConnection.HTTPConnSelectiveField selective = new HttpConnection.HTTPConnSelectiveField();
				HttpConnection conn = new HttpConnection(mandatory, selective);
				conn.connect().exportAsString();
			} catch (Exception e) {
				Global.getLogger.error(getClass().getName(), e.getMessage(), e);
			} finally {
				Global.getLogger.debug(getClass().getName(), "service end");
			}
		}
	}

	public void dbProcess() throws SQLException {
		Connection dbConn = null;
		boolean isProcessSuccess = false;
		try {
			isProcessSuccess = true;
		} catch (Exception e) {
			Global.getLogger.error(getClass().getName(), e.getMessage(), e);
		} finally {
			if (isProcessSuccess)
				dbConn.commit();
		}
	}
}
