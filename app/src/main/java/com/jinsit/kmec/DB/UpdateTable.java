package com.jinsit.kmec.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UpdateTable {
	Context context;
	public UpdateTable(Context context){
		this.context = context;
	}

	public void updateTable(String yn ,String workDt, String jobNo){

		updateWorkTable(yn, workDt,jobNo);
		update030Table(yn, workDt,jobNo);
		update210Table(yn, workDt,jobNo);
		update213Table(yn, workDt,jobNo);

	}

	//3일 지난 워크 테이블 지우기
	public void updateWorkTable(String yn, String workDt, String jobNo) {
		String query = "";
		query = "UPDATE WORK_TBL SET UPLOAD_YN = '" + yn + "' WHERE JOB_NO = '" + jobNo + "' AND WORK_DT = '" + workDt+ "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();

	}


	public void update030Table(String yn, String workDt, String jobNo) {
		String query = "";
		query = "UPDATE TCSQ030 SET UPLOAD_YN = '" + yn + "' WHERE JOB_NO = '" + jobNo + "' AND WORK_DT = '" + workDt+ "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	public void update210Table(String yn, String workDt, String jobNo) {
		String query = "";
		query = "UPDATE TCSQ210 SET UPLOAD_YN = '" + yn + "' WHERE JOB_NO = '" + jobNo + "' AND WORK_DT = '" + workDt+ "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	public void update213Table(String yn, String workDt, String jobNo) {
		String query = "";
		query = "UPDATE TCSQ213 SET UPLOAD_YN = '" + yn + "' WHERE JOB_NO = '" + jobNo + "' AND WORK_DT = '" + workDt+ "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

}
