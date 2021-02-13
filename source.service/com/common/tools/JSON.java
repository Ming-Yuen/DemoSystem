package com.common.tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON {

	public static boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static String formatJSONStr(final String json_str) {
	    final char[] chars = json_str.toCharArray();
	    final String newline = System.lineSeparator();
	    final int indent_width = 4;

	    StringBuilder ret = new StringBuilder();
	    boolean begin_quotes = false;

	    for (int i = 0, indent = 0; i < chars.length; i++) {
	        char c = chars[i];

	        if (c == '\"') {
	        	ret.append(c);
	            begin_quotes = !begin_quotes;
	            continue;
	        }

	        if (!begin_quotes) {
	            switch (c) {
	            case '{':
	            case '[':
	            	ret.append(c + newline + String.format("%" + (indent += indent_width) + "s", ""));
	                continue;
	            case '}':
	            case ']':
	            	ret.append(newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "") + c);
	                continue;
	            case ':':
	            	ret.append(c + " ");
	                continue;
	            case ',':
	            	ret.append(c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : ""));
	                continue;
	            default:
	                if (Character.isWhitespace(c)) continue;
	            }
	        }

	        ret.append(c + (c == '\\' ? "" + chars[++i] : ""));
	    }

	    return ret.toString();
	}
}
