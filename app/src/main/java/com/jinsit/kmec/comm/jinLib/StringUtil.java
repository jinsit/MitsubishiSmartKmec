package com.jinsit.kmec.comm.jinLib;


public class StringUtil {

	

	public static String padLeft( String buttonTitle, String contents) {
		int length = 40;
		String text = buttonTitle;
		for (int i = contents.length(); i < length; i++) {
			text = text + " ";
		}
		return text + contents;
	}
	public static String padLeft( String buttonTitle, String contents, int len) {
		int length = len;
		String text = buttonTitle;
		for (int i = contents.length(); i < length; i++) {
			text = text + " ";
		}
		return text + contents;
	}
}
