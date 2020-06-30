package com.jinsit.kmec.comm.jinLib;

import java.text.DecimalFormat;

public class SysUtil {

	public static String NewLine = "\n";

	/**
	 * 문자열에 통화적용을 위해 컴마를 표기한다.
	 * @param string 통화적용을 위한 문자열
	 * @param ignoreZero 값이 0일 경우 공백을 리턴한다.
	 * @return 통화적용이 된 문자열
	 */
	public static String makeStringWithComma(String string, boolean ignoreZero) {
		if (string.length() == 0) {
			return "";
		}
		try {
			if (string.indexOf(".") >= 0) {
				return string;
			} else {
				long value = Long.parseLong(string);
				if (ignoreZero && value == 0) {
					return "";
				}
				DecimalFormat format = new DecimalFormat("###,###");
				return format.format(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return string;
	}


}


