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
	private final String connUrl;

	private final HTTPConnMandatoryField.HTTPMethod connMethod;

	private final String mediaType;

	private final List<Object> connParameter;

	private final Charset connCharset;

	private final Integer connTimeOut;

	private final HTTPConnResponseField connResponseField;

	private final StringBuilder content = new StringBuilder();

	public HttpConnection(HTTPConnMandatoryField mandatoryField, HTTPConnSelectiveField selectiveField) {
		this.connUrl = mandatoryField.connUrl;
		this.connMethod = mandatoryField.connMethod;
		this.mediaType = mandatoryField.mediaType;
		selectiveField = (selectiveField == null) ? new HTTPConnSelectiveField() : selectiveField;
		this.connParameter = selectiveField.connParameter;
		this.connCharset = selectiveField.connCharset;
		this.connTimeOut = selectiveField.connTimeOut;
		this.connResponseField = new HTTPConnResponseField();
	}

	public HttpConnection connect() throws IOException {
		BufferedReader br = null;
		BufferedWriter writer = null;
		OutputStream os = null;
		try {
			URL url = new URL(this.connUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(this.connMethod.getHTTPMethod());
			con.setRequestProperty("Content-Type", this.mediaType);
			con.setRequestProperty("charset", this.connCharset.displayName());
			con.setConnectTimeout(this.connTimeOut.intValue());
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			if (!this.connParameter.isEmpty()) {
				os = con.getOutputStream();
				writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
				for (Object obj : this.connParameter) {
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

	public <T> String xmlClassConvStr(T xmlClass) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(new Class[] { xmlClass.getClass() });
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

	public <T> T exportAsXmlClass(T clazz) throws JAXBException {
		if (clazz == null)
			throw new NullPointerException("Export XML class cannot null");
		JAXBContext context = JAXBContext.newInstance(new Class[] { clazz.getClass() });
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
		private String connUrl;

		private HTTPMethod connMethod;

		private String mediaType;

		public enum HTTPMethod {
			GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), PATCH("PATCH"), HEAD("HEAD"), OPTIONS("OPTIONS");

			private String method;

			HTTPMethod(String method) {
				this.method = method;
			}

			public String getHTTPMethod() {
				return this.method;
			}
		}

		public HTTPConnMandatoryField setConnUrl(String connUrl) {
			this.connUrl = connUrl;
			return this;
		}

		public HTTPConnMandatoryField setConnMethod(HTTPMethod connMethod) {
			this.connMethod = connMethod;
			return this;
		}

		public HTTPConnMandatoryField setConnContentType(String MediaType) {
			this.mediaType = MediaType;
			return this;
		}
	}

	public enum HTTPMethod {
		GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), PATCH("PATCH"), HEAD("HEAD"), OPTIONS("OPTIONS");

		private String method;

		HTTPMethod(String method) {
			this.method = method;
		}

		public String getHTTPMethod() {
			return this.method;
		}
	}

	public static class HTTPConnSelectiveField {
		public List<Object> connParameter = new ArrayList<Object>();

		private Charset connCharset = StandardCharsets.UTF_8;

		private Integer connTimeOut = Integer.valueOf(30000);

		public void setConnParameter(List<Object> connParameter) {
			this.connParameter = connParameter;
		}

		public void setConnCharset(Charset connCharset) {
			this.connCharset = connCharset;
		}

		public void setConnTimeOut(Integer connTimeOut) {
			this.connTimeOut = connTimeOut;
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
