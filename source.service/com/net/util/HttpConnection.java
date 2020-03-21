package com.net.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpConnection {
	public static String connect(String https_url, String parameter) throws IOException {
		StringBuilder content = new StringBuilder();
		URL url = new URL(https_url);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		if (parameter != null && !parameter.isEmpty()) {
			con.setRequestMethod("POST");
		}else {
			con.setRequestMethod("GET");
		}
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("charset", "utf-8");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.connect();
		
		if (parameter != null && !parameter.isEmpty()) {
			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(parameter);
			writer.flush();
			writer.close();
			os.close();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		while ((input = br.readLine()) != null) {
			content.append(input);
		}
		br.close();
		return content.toString();
	}
}
