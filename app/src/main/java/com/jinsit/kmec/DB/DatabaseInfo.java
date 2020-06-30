package com.jinsit.kmec.DB;

public class DatabaseInfo {


	final static String DATABASE_NAME = "kmec.db";
	final static int DATABASE_VERSION = 5;	//업데이트 20170920 3으로 버전-> 20181218 4로 업데이트
	public static String getDBName() {
		return DATABASE_NAME;
	}

	public static int getDBVersion(){
		return DATABASE_VERSION;

	}

}
