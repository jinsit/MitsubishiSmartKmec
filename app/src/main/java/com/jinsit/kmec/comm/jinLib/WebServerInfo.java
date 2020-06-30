package com.jinsit.kmec.comm.jinLib;

public class WebServerInfo {
//	private final static String WEB_SERVER_URL = "http://192.168.0.13:9999/abc/";
//    private final static String WEB_IMAGE_URL = "http://192.168.0.215:18080/images/";
//	private final static String WEB_SERVER_URL = "http://192.168.0.215:18080/abc/";

	private final static String FTP_IMAGE_URL = "ftp://211.117.25.201:5500/ERP_DATA/PCSZ630M/";

//	private final static String WEB_SERVER_URL = "http://210.217.179.135:12000/kmecSvc/";
	//private final static String WEB_IMAGE_URL = "http://210.217.179.135:12000/images/";
	//프록시 적용된 URL
	private final static String WEB_SERVER_URL = "http://s.mitsubishielevator.co.kr:12000/kmecSvc/";
	private final static String WEB_IMAGE_URL = "http://s.mitsubishielevator.co.kr:12000/images/";

	//TEST서버
//	private final static String WEB_SERVER_URL = "http://211.117.25.210:12000/kmecSvc/";
//	private final static String WEB_IMAGE_URL = "http://211.117.25.210:12000/images/";

	//Getter
	public static String getUrl(){
		return WEB_SERVER_URL;
	}
	public static String getImageUrl(){
		return WEB_IMAGE_URL;
	}
	
	public static String getFtpImageUrl(){
		return FTP_IMAGE_URL;
	}
	
}
