package com.jinsit.kmec.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class KMecSQLOpenHelper extends SQLiteOpenHelper{

	public KMecSQLOpenHelper(Context context, String name,
							 CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL("CREATE TABLE TCSQ030("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"CS_EMP_ID Text NOT NULL,"
				+"WORK_DT datetime NOT NULL,"
				+"JOB_NO numeric(5, 0) NOT NULL,"
				+"WORK_CD Text NOT NULL,"
				+"CS_TM_FR Text NULL,"
				+"CS_TM_TO Text NULL,"
				+"CS_TM numeric(5, 0) NULL,"
				+"BLDG_NO Text NULL,"
				+"CAR_NO Text NULL,"
				+"REF_CONTR_NO Text NULL,"
				+"SUPPORT_CD Text NULL,"
				+"JOB_ST Text NOT NULL,"
				+"ENG_ST Text NOT NULL,"
				+"YET_REASON Text NULL,"
				+"PLAN_YYMM Text NULL,"
				+"PLAN_SQ numeric(3, 0) NULL,"
				+"WK_ORDER int NULL,"
				+"WK_DAY int NULL,"
				+"CS_DEPT_CD Text NULL,"
				+"INSP_TIME_BC int NULL,"
				+"RMK Text NULL,"
				+"ISRT_USR_ID Text NOT NULL,"
				+"ISRT_DT datetime NOT NULL,"
				+"UPDT_USR_ID Text NOT NULL,"
				+"UPDT_DT datetime NOT NULL,"
				+"UPLOAD_YN TEXT DEFAULT 'N',"
				+"CS_TP TEXT NULL,"
				+"SELCHK_USID TEXT NULL,"
				+"SELCHK_USID_NM TEXT NULL, "
				+"SUB_SELCHK_USID TEXT NULL,"
				+"SUB_SELCHK_USID_NM TEXT NULL "
//	+"PRIMARY KEY (_id , CS_EMP_ID, WORK_DT, JOB_NO)"
				+ ");");


		/**
		 * 테이블 2번
		 * 점검계획 점검파트
		 */
		db.execSQL("CREATE TABLE TCSQ210("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"CS_EMP_ID Text NOT NULL,"
				+"WORK_DT datetime NOT NULL,"
				+"JOB_NO numeric(5, 0) NOT NULL,"
				+"NFC_PLC Text NOT NULL,"
				+"NFC_PLC_NM Text NOT NULL,"
				+"CS_TM_FR Text NULL,"
				+"CS_TM_TO Text NULL,"
				+"CS_TM numeric(5, 0) NULL,"
				+"JOB_ST Text NULL,"
				+"ENG_ST Text NULL,"
				+"REASON_RMK Text NULL,"
				+"RMK Text NULL,"
				+"ISRT_USR_ID Text NOT NULL,"
				+"ISRT_DT datetime NOT NULL,"
				+"UPDT_USR_ID Text NOT NULL,"
				+"UPDT_DT datetime NOT NULL,"
				+"NFC_UDID TEXT  NULL,"
				+"UPLOAD_YN TEXT DEFAULT 'N' "
//	+"PRIMARY KEY(_id , CS_EMP_ID, WORK_DT, JOB_NO, NFC_PLC)"
				+ ");");

		db.execSQL("CREATE TABLE TCSQ213("
						+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
						+"CS_EMP_ID Text NOT NULL,"
						+"WORK_DT datetime NOT NULL,"
						+"JOB_NO numeric(5, 0) NOT NULL,"
						+"NFC_PLC Text NOT NULL,"
						+"CS_ITEM_CD Text NOT NULL,"
						+"CS_TOOLS Text NULL,"
						+"STD_ST Text NULL,"
						+"INPUT_TP Text NULL,"
						+"INPUT_TP1 Text NULL,"
						+"INPUT_TP3 Text NULL,"
						+"INPUT_TP7 Text NULL,"
						+"INPUT_RMK Text NULL,"
						+"OVER_MONTH Text NULL,"
						+"MONTH_CHK Text NULL,"
						+"RMK Text NULL,"
						+"ISRT_USR_ID Text NOT NULL,"
						+"ISRT_DT datetime NOT NULL,"
						+"UPDT_USR_ID Text NOT NULL,"
						+"UPDT_DT datetime NOT NULL,"
						+"CS_LOW_NM Text NULL,"
						+"SMART_DESC Text NULL,"
						+"MNG_DESC Text NULL,"
						+"MONTH_CHK_IF Text NULL,"
						+"EL_INFO_MAP Text NULL,"
						+"DEF_VAL Text NULL,"
						+"DEF_VAL_ST Text NULL,"
						+"PRE_WORK_MM Text NULL,"
						+"PRE_INPUT_TP Text NULL,"
						+"UPLOAD_YN TEXT DEFAULT 'N',"
						+"INSP_METHOD TEXT NULL,"
						+"HEADER_IF TEXT NULL "
//	+"PRIMARY KEY(_id , CS_EMP_ID, WORK_DT, JOB_NO, NFC_PLC, CS_ITEM_CD)"
						+ ");"
		);



		db.execSQL("CREATE TABLE WORK_TBL("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "CS_EMP_ID Text NOT NULL,"
				+ "WORK_DT Text NOT NULL,"
				+ "JOB_NO Text NOT NULL,"
				+ "WORK_CD Text NOT NULL,"
				+ "WORK_NM Text NULL,"
				+ "ST Text  NULL,"
				+ "JOB_ST Text  NULL,"
				+ "CS_DT Text NULL,"
				+ "CS_FR Text NULL,"
				+ "BLDG_NO Text NULL,"
				+ "BLDG_NM Text NULL,"
				+ "CAR_NO Text NULL,"
				+ "RESERV_ST Text NULL,"
				+ "ADDR Text NULL,"
				+ "MAIN_EMP_NM Text NULL,"
				+ "MAIN_EMP_PHONE Text NULL,"
				+ "SUB_EMP_NM Text NULL,"
				+ "SUB_EMP_PHONE Text NULL,"

				+ "CS_DEPT_NM Text NULL,"
				+ "NOTIFY_NM Text NULL,"
				+ "NOTIFY_PHONE Text NULL,"
				+ "RECEV_DESC Text NULL,"
				+ "RECEV_TM Text NULL,"
				+ "RESERV_TM Text NULL,"
				+ "REPAIR_TM Text NULL,"

				+ "MOVE_TM Text NULL,"
				+ "ARRIVE_TM Text NULL,"
				+ "COMPLETE_TM Text NULL,"
				+ "RESCUE_TM Text NULL,"
				+ "START_TM Text NULL,"

				+ "CONTACT_CD Text NULL,"
				+ "STATUS_CD Text NULL,"
				+ "REF_CONTR_NO Text NULL,"
				+ "PARTS_NO Text NULL,"
				+ "MODEL_NM Text NULL,"
				+ "RECEV_NO Text NULL,"
				+ "CHECK_YN Text NULL,"
				+ "CBS_YN Text NULL,"
				+"UPLOAD_YN TEXT DEFAULT 'N' "
//				+ "PRIMARY KEY(_id , CS_EMP_ID, WORK_DT, JOB_NO)"
				+ ");");


		db.execSQL("CREATE TABLE TCSC010("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "BLDG_NO Text NOT NULL,"
				+ "CLIENT_ID Text NOT NULL,"
				+ "CLIENT_CD Text NOT NULL,"
				+ "CLIENT_NM Text NOT NULL,"
				+ "MAIL_ADDR Text NULL,"
				+ "DEPT_NM Text  NULL,"
				+ "GRAD_NM Text  NULL,"
				+ "PHONE Text NULL,"
				+ "MOBILE Text NULL,"
				+ "FAX Text NULL "

//				+ "PRIMARY KEY(_id , CS_EMP_ID, WORK_DT, JOB_NO)"
				+ ");");



		db.execSQL("CREATE TABLE TCSQ050 ("
				+ "CS_EMP_ID Text NOT NULL,"
				+ "WORK_DT datetime NOT NULL,"
				+ "JOB_NO numeric(5, 0) NOT NULL,"
				+ "ACT_NO numeric(3, 0) NOT NULL,"
				+ "WORK_CD Text NOT NULL,"
				+ "JOB_TM datetime NOT NULL,"
				+ "JOB_ACT Text NOT NULL,"
				+ "JOB_ST Text NOT NULL,"
				+ "BLDG_NO Text NULL,"
				+ "CAR_NO Text NULL,"
				+ "REF_CONTR_NO Text NULL,"
				+ "SUPPORT_CD Text NULL,"
				+ "ENG_ST Text NOT NULL,"
				+ "LOCAL_COORD_X numeric(16, 10) NOT NULL,"
				+ "LOCAL_COORD_Y numeric(16, 10) NOT NULL,"
				+ "JOB_COORD_X numeric(16, 10) NULL,"
				+ "JOB_COORD_Y numeric(16, 10) NULL,"
				+ "ADDR Text NULL,"
				+ "DEVICE_NO Text NOT NULL,"
				+ "CANCEL_IF Text NULL,"
				+ "CANCEL_DT datetime NULL,"
				+ "RMK Text NULL, "
				+ "PRIMARY KEY(CS_EMP_ID, WORK_DT, JOB_NO,ACT_NO)"
				+ ");");

	}




	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		if(newVersion == 2){
			db.execSQL("alter table TCSQ213 add column EL_INFO_MAP TEXT");
			db.execSQL("alter table TCSQ213 add column DEF_VAL TEXT");
			db.execSQL("alter table TCSQ213 add column DEF_VAL_ST INTEGER DEFAULT 0");

		}else if(newVersion == 3) {

			//TCSQ030 컬럼 추가
			db.execSQL("alter table TCSQ030 add column CS_TP TEXT");
			db.execSQL("alter table TCSQ030 add column SELCHK_USID TEXT");
			db.execSQL("alter table TCSQ030 add column SELCHK_USID_NM TEXT");
			db.execSQL("alter table TCSQ030 add column SUB_SELCHK_USID TEXT");
			db.execSQL("alter table TCSQ030 add column SUB_SELCHK_USID_NM TEXT");
		}else if(newVersion == 4){
			db.execSQL("alter table TCSQ213 add column PRE_WORK_MM TEXT");
			db.execSQL("alter table TCSQ213 add column PRE_INPUT_TP TEXT");
		}
		else if(newVersion == 5){
			db.execSQL("alter table TCSQ213 add column HEADER_IF TEXT");
			db.execSQL("alter table TCSQ213 add column INSP_METHOD TEXT");
		}



	}

}
