package com.container;

import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

import com.api.model.VideoDownloadRequest;
import com.api.util.MediaUtil;
import com.configuration.ServerConfigation;

public class Container extends HttpServlet {
	public Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public void init() {
		synchronized(this)
		{
			try {
				ServerConfigation.IPprocess();
//				VideoDownloadRequest request = new VideoDownloadRequest();
//				request.setDownloadURL("https://www.youtube.com/watch?v=j51D1Cuf5EI");
//				request.setSaveURI("C:\\Users\\Admin\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\logs");
//				MediaUtil.videoDownloadProcess(null, request, null);
//				log.info("end of process");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
