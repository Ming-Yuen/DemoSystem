package com.media.utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.ws.rs.HttpMethod;

import com.api.std.DocUtil;
import com.common.util.FileUtil;
import com.configuration.Config;
import com.configuration.ConfigurationMenu;
import com.net.util.HttpConnection;

public class MediaUtil extends DocUtil {
	public static void videoDownloadProcess(String downloadUrl, String savePath) throws IOException, InterruptedException {
		final String exeLocation = System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "youtube-dl.exe";
		String[] command = { "cmd", };
		Process p;
		youtubeUpdate(exeLocation);
		p = Runtime.getRuntime().exec(command);
		new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
		new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
		PrintWriter stdin = new PrintWriter(p.getOutputStream());
		stdin.println("cd \"" + savePath + "\"");
		stdin.println(new File(exeLocation).getParent() + "\\youtube-dl " + downloadUrl + " --write-all-thumbnails");
		stdin.close();
		p.waitFor();
	}
	
	public static void youtubeUpdate(String exeLocation) throws IOException {
		final String youtubeDlUrl = "https://yt-dl.org/";
		final String downloadUrl = "https://yt-dl.org/downloads/latest/youtube-dl.exe";

		HttpConnection.Mandatory mandatory = new HttpConnection.Mandatory();
	    mandatory.setApiUrl(youtubeDlUrl).setMethod(HttpMethod.GET).setContentType("application/json");
	    HttpConnection conn = new HttpConnection(mandatory, null);
	    String response = conn.connect().getContent();
		int strIndex = response.indexOf("<div><a href=\"latest\">Latest</a>");
		int endIndex = response.indexOf("</div>", strIndex);
		String version = response.substring(strIndex, endIndex + 1);
		int versionStrIndex = version.indexOf("(");
		int versionEndIndex = version.indexOf(")");
		version = version.substring(versionStrIndex +1, versionEndIndex);
		
		if(!new File(exeLocation).exists()) {
			FileUtil.downloadFileByURL(downloadUrl, exeLocation);
			Config.writeConfigValue(ConfigurationMenu.YouTube_dlVersion, version);
			return;
		}
		if( !Config.getConfigValue(ConfigurationMenu.YouTube_dlVersion).equals(version) ) {
			FileUtil.downloadFileByURL(downloadUrl, exeLocation);
			Config.writeConfigValue(ConfigurationMenu.YouTube_dlVersion, version);
		}
	}
}
class SyncPipe implements Runnable {
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
		istrm_ = istrm;
		ostrm_ = ostrm;
	}

	public void run() {
		try {
			final byte[] buffer = new byte[10240];
			for (int length = 0; (length = istrm_.read(buffer)) != -1;) {
				ostrm_.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final OutputStream ostrm_;
	private final InputStream istrm_;
}