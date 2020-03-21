package com.common.util;

import java.nio.charset.Charset;

public class StringUtils {
	public static String encodingCharacter(String str, Charset decoder, Charset converter) {
		String decoderStr = new String(str.getBytes(decoder), decoder);
		String encoderStr = new String(decoderStr.getBytes(converter), converter);
		return encoderStr;
	}
}
