package com.net.util;

import com.global.Global;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class HttpConnection {
	private final Mandatory mandatory;
	private final Selective selective;

	private final Response response;

	private StringBuilder content = new StringBuilder();

	public HttpConnection(Mandatory mandatoryField, Selective selectiveField) {
		this.mandatory = mandatoryField;
		this.selective = (selectiveField == null) ? new Selective() : selectiveField;
		this.response = new Response();
		selective.procName = this.getClass().getSimpleName();
	}

	public HttpConnection connect() throws IOException {
		content = new StringBuilder();
		URL url = new URL(mandatory.apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(mandatory.method);
		conn.setRequestProperty("Content-Type", mandatory.mediaType);
		conn.setRequestProperty("charset", selective.charset.displayName());
		conn.setConnectTimeout(selective.timeOut);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.connect();
		if (!selective.body.isEmpty()) {
			try(OutputStream os = conn.getOutputStream()){
				try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))){
					writer.write(selective.body);
					writer.flush();
				}
				Global.getLogger.info(selective.procName, "Body : " + os.toString());
			}
		}
		try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream()))){
			String input;
			while ((input = br.readLine()) != null) {
				this.content.append(input);
			}
			Global.getLogger.info(selective.procName, "Response : " + content);
		}
		this.response.setResponseCode(conn.getResponseCode());
		return this;
	}

	public Response getResponse() {
		return this.response;
	}

	public <T> T toJsonRecord(Class<T> clazz) {
		Gson gson = new Gson();
		return (T) gson.fromJson(getContent(), clazz);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T toXMLRecord(Class<T> clazz) throws JAXBException {
		if (clazz == null) {
			throw new NullPointerException("Export XML class cannot null");
		}
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
		Object xmlObj = jaxbUnmarshaller.unmarshal(new StringReader(getContent()));
		return (T) xmlObj;
	}

	public String getContent() {
		return new String(content);
	}

	public static class Mandatory {
		private String apiUrl;
		private String method;
		private String mediaType;

		public Mandatory setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
			return this;
		}

		public Mandatory setMethod(String HTTPMethod) {
			this.method = HTTPMethod;
			return this;
		}

		public Mandatory setContentType(String MediaType) {
			this.mediaType = MediaType;
			return this;
		}
	}

	public static class Selective {
		private String body = "";
		private Charset charset = StandardCharsets.UTF_8;
		private Integer timeOut = 30000;
		private String procName = "";

		public void setTextBody(String body) {
			this.body = body;
		}

		public Selective putJsonBody(JsonElement record) {
			Gson gson = new Gson();
			this.body = gson.toJson(record);
			return this;
		}

		public Selective putXMLBody(Object record) throws Exception {
			if(record == null) {
				throw new Exception("record cannot null");
			}
			JAXBContext context = JAXBContext.newInstance(record.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty("jaxb.formatted.output", Boolean.TRUE);
			StringWriter sw = new StringWriter();
			m.marshal(record, sw);
			this.body = sw.toString();
			return this;
		}

		public void setCharset(Charset charset) {
			this.charset = charset;
		}

		public void setTimeOut(Integer timeOut) {
			this.timeOut = timeOut;
		}
		
		public void setProName(String procName) {
			this.procName = procName;
		}
		
	}

	public class Response {
		private Integer responseCode;

		public Integer getResponseCode() {
			return this.responseCode;
		}

		public void setResponseCode(Integer responseCode) {
			this.responseCode = responseCode;
		}
	}
}
