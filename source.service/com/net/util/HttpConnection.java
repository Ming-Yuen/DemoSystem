package com.net.util;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class HttpConnection {
	private final HTTPConnMandatoryField mandatoryField;
	public final HTTPConnSelectiveField selectiveField;

	private final HTTPConnResponseField connResponseField;

	private final StringBuilder content = new StringBuilder();

	public HttpConnection(HTTPConnMandatoryField mandatoryField, HTTPConnSelectiveField selectiveField) {
		this.mandatoryField = mandatoryField;
		this.selectiveField = (selectiveField == null) ? new HTTPConnSelectiveField() : selectiveField;
		this.connResponseField = new HTTPConnResponseField();
	}

	public HttpConnection connect() throws IOException {
		BufferedReader br = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		try {
			URL url = new URL(mandatoryField.apiUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(mandatoryField.method);
			con.setRequestProperty("Content-Type", mandatoryField.mediaType);
			con.setRequestProperty("charset", selectiveField.connCharset.displayName());
			con.setConnectTimeout(selectiveField.timeOut.intValue());
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			if (!selectiveField.parameter.isEmpty()) {
				os = con.getOutputStream();
				writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				for (Object obj : selectiveField.parameter) {
					writer.write(obj.toString());
					writer.flush();
				}
				writer.close();
				os.close();
			}
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String input;
			while ((input = br.readLine()) != null)
				this.content.append(input);
			this.connResponseField.setResponseCode(Integer.valueOf(con.getResponseCode()));
			return this;
		} finally {
			if (br != null)
				br.close();
			if (writer != null)
				writer.close();
			if (os != null)
				os.close();
		}
	}

	public <T> String xmlClassConvStr(Class<T> xmlClass) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(xmlClass);
		Marshaller m = context.createMarshaller();
		m.setProperty("jaxb.formatted.output", Boolean.TRUE);
		StringWriter sw = new StringWriter();
		m.marshal(xmlClass, sw);
		return sw.toString();
	}

	public <T> String jsonClassConvStr(T jsonClass) {
		Gson gson = new Gson();
		return gson.toJson(jsonClass);
	}

	@SuppressWarnings("unchecked")
	public <T> T exportAsXmlClass(Class<T> clazz) throws JAXBException {
		if (clazz == null) {
			throw new NullPointerException("Export XML class cannot null");
		}
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
		Object xmlObj = jaxbUnmarshaller.unmarshal(new StringReader(exportAsString()));
		return (T) xmlObj;
	}

	public HTTPConnResponseField getConnResponseField() {
		return this.connResponseField;
	}

	public <T> T exportAsJsonClass(Class<T> clazz) {
		Gson gson = new Gson();
		return (T) gson.fromJson(exportAsString(), clazz);
	}

	public String exportAsString() {
		return this.content.toString();
	}

	public static class HTTPConnMandatoryField {
		private String apiUrl;

		private String method;

		private String mediaType;

		public HTTPConnMandatoryField setApiUrl(String apiUrl) {
			this.apiUrl = apiUrl;
			return this;
		}

		public HTTPConnMandatoryField setMethod(String HTTPMethod) {
			this.method = HTTPMethod;
			return this;
		}

		public HTTPConnMandatoryField setConnContentType(String MediaType) {
			this.mediaType = MediaType;
			return this;
		}
	}

//	public enum HTTPMethod {
//		GET("GET"), POST("POST");//, PUT("PUT"), DELETE("DELETE"), PATCH("PATCH"), HEAD("HEAD"), OPTIONS("OPTIONS");
//
//		private String method;
//
//		HTTPMethod(String method) {
//			this.method = method;
//		}
//
//		public String getHTTPMethod() {
//			return this.method;
//		}
//	}

	public static class HTTPConnSelectiveField {
		public List<Object> parameter = new ArrayList<Object>();

		private Charset connCharset = StandardCharsets.UTF_8;

		private Integer timeOut = 30000;

		public void setParameter(List<Object> connParameter) {
			this.parameter = connParameter;
		}

		public void setCharset(Charset connCharset) {
			this.connCharset = connCharset;
		}

		public void setTimeOut(Integer timeOut) {
			this.timeOut = timeOut;
		}
	}

	public class HTTPConnResponseField {
		private Integer responseCode;

		public HTTPConnResponseField() {
			this.responseCode = Integer.valueOf(0);
		}

		public Integer getResponseCode() {
			return this.responseCode;
		}

		public void setResponseCode(Integer responseCode) {
			this.responseCode = responseCode;
		}
	}
}
