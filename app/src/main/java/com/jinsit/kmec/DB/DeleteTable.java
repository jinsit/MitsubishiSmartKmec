package com.jinsit.kmec.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DeleteTable {
	Context context;
	public DeleteTable(Context context){
		this.context = context;
	}

	public void deleteTable(String workDt){

		deleteWorkTable("", workDt);
		delete030Table("", workDt);
		delete210Table("", workDt);
		delete213Table("", workDt);

	}

	//3일 지난 워크 테이블 지우기
	public void deleteWorkTable(String empId, String workDt) {
		String query = "";
		query = "DELETE  FROM WORK_TBL WHERE WORK_DT < (select date('" + workDt
				+ "','-3 day' ))";// + " AND UPLOAD_YN = 'Y'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();

	}


	public void delete030Table(String empId, String workDt) {
		String query = "";
		query = "DELETE  FROM TCSQ030 WHERE WORK_DT < (select date('" + workDt
				+ "','-3 day' ))";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	public void delete210Table(String empId, String workDt) {
		String query = "";
		query = "DELETE  FROM TCSQ210 WHERE WORK_DT < (select date('" + workDt
				+ "','-3 day' ))";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	public void delete213Table(String empId, String workDt) {
		String query = "";
		query = "DELETE  FROM TCSQ213 WHERE WORK_DT < (select date('" + workDt
				+ "','-3 day' ))";// + " AND UPLOAD_YN = 'Y'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

}
