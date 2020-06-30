package com.jinsit.kmec.DB;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM00;
import com.jinsit.kmec.comm.DeviceUniqNumber;
import com.jinsit.kmec.comm.jinLib.DateUtil;

public class DatabaseRawQuery {





	public DatabaseRawQuery() {

	}


	public String selectJobList(String empId, String workDt){

		String selectJobList = "SELECT  "
				+ " T1._id,"
				+ "T1.CS_EMP_ID, "
				+ "T1.WORK_DT,"
				+ "T1.JOB_NO,"
				+ "T1.WORK_NM,"
				+ "T1.ST,"
				+ "T1.CS_DT,"
				+ "T1.CS_FR,"
				+ "T1.BLDG_NM,"
				+ "T1.CAR_NO,"
				+ "T1.RESERV_ST,"
				//+ "T1.JOB_ST,"		//테스트용으로 추가했음
				+ "CASE WHEN T1.WORK_CD = 'CA01' THEN  (SELECT COUNT(*) FROM TCSQ210 B WHERE T1.CS_EMP_ID = B.CS_EMP_ID AND T1.WORK_DT = B.WORK_DT AND T1.JOB_NO = B.JOB_NO AND B.JOB_ST = '39') ELSE 0 END Y_CNT,"
				+ " CASE WHEN T1.WORK_CD = 'CA01' THEN (SELECT COUNT(*) FROM TCSQ210 B WHERE T1.CS_EMP_ID = B.CS_EMP_ID AND T1.WORK_DT = B.WORK_DT AND T1.JOB_NO = B.JOB_NO) ELSE 0 END T_CNT"
				+ " FROM	 WORK_TBL AS T1 "
				+ "WHERE T1.CS_EMP_ID= '"+ empId + "'"
				+ "AND T1.WORK_DT = '" + workDt + "'"
				+ "AND NOT T1.JOB_ST	IN ( '39', '43' )" + "ORDER BY CS_FR ";

		return selectJobList;

	}

	/**
	 * 완료목록 리스트 조회르를 위한 쿼리
	 * @param empId
	 * @param workDt
	 * @return
	 */
	public String selectCompleteJobList(String empId, String workDt){

		String selectJobList = "SELECT  "
				+ " T1._id,"
				+ "T1.CS_EMP_ID, "
				+ "T1.WORK_DT,"
				+ "T1.JOB_NO,"
				+ "T1.WORK_NM,"
				+ "T1.ST,"
				+ "T1.CS_DT,"
				+ "T1.CS_FR,"
				+ "T1.BLDG_NM,"
				+ "T1.CAR_NO,"
				+ "T1.RESERV_ST,"
				+ "CASE WHEN T1.WORK_CD = 'CA01' THEN  (SELECT COUNT(*) FROM TCSQ210 B WHERE T1.CS_EMP_ID = B.CS_EMP_ID AND T1.WORK_DT = B.WORK_DT AND T1.JOB_NO = B.JOB_NO AND B.JOB_ST = '39') ELSE 0 END Y_CNT,"
				+ " CASE WHEN T1.WORK_CD = 'CA01' THEN (SELECT COUNT(*) FROM TCSQ210 B WHERE T1.CS_EMP_ID = B.CS_EMP_ID AND T1.WORK_DT = B.WORK_DT AND T1.JOB_NO = B.JOB_NO) ELSE 0 END T_CNT"
				+ " FROM	 WORK_TBL AS T1 "
				+ "WHERE T1.CS_EMP_ID= '"+ empId + "'"
				+ "AND T1.WORK_DT = '" + workDt + "'"
				+ "AND  T1.JOB_ST	IN ( '39' ) " + "ORDER BY CS_FR ";

		return selectJobList;

	}



	public String selectJobDetail(String empId, String workDt,String jobNo){

		String selectJobDetail =	"SELECT	"
				+ " _id,"
				+ "CS_EMP_ID,"
				+"WORK_DT,"
				+"JOB_NO,"
				+"WORK_CD,"
				+"	WORK_NM,"
				+"ST,"
				+"	CS_DT,"
				+"	CS_FR,"
				+"	BLDG_NO,"
				+"	BLDG_NM,"
				+"	CAR_NO,"
				+"	RESERV_ST,"
				+"	ADDR,"
				+"	MAIN_EMP_NM,"
				+"	MAIN_EMP_PHONE,"
				+"	SUB_EMP_NM,"
				+"	SUB_EMP_PHONE,"
				+"	CS_DEPT_NM,"
				+"	NOTIFY_NM,"
				+"	NOTIFY_PHONE,"
				+"	RECEV_DESC,"
				+"	RECEV_TM,"
				+"	RESERV_TM,"
				+"	REPAIR_TM,"
				+"	MOVE_TM,"
				+"	ARRIVE_TM,"
				+"	COMPLETE_TM,"
				+"	RESCUE_TM,"
				+"	START_TM,"
				+"	CONTACT_CD,"
				+"	STATUS_CD,"
				+"	REF_CONTR_NO,"
				+" PARTS_NO,"
				+"	MODEL_NM,"
				+"	RECEV_NO,"
				+"	CHECK_YN,"
				+" CBS_YN "
				+" FROM		WORK_TBL "
				+" WHERE   CS_EMP_ID = '" + empId + "'"
				+" AND		WORK_DT = '" + workDt + "'"
				+" AND		JOB_NO = '" + jobNo + "'";

		return selectJobDetail;

	}
	public String selectCheckListPart(String empId, String workDt, String jobNo){

		String checkListPart = "SELECT "
				+ " T1._id,"
				+"T1.NFC_PLC, "
				+"T1.NFC_PLC_NM,"
				+"T1.JOB_ST,"
				+" CASE WHEN T1.JOB_ST = '00' THEN '미진행' "
				+"	WHEN T1.JOB_ST = '31' THEN '진행중' "
				+" WHEN T1.JOB_ST = '39' THEN '완료' "
				+" WHEN T1.JOB_ST = '41' THEN '보류' "
				+" ELSE '계획' END JOB_ST_NM "
				+" FROM		TCSQ210 T1 "
				+" WHERE	T1.CS_EMP_ID = '" + empId + "'"
				+" AND		T1.WORK_DT = '" + workDt + "'"
				+" AND		T1.JOB_NO = '" + jobNo + "'"
				+" GROUP BY NFC_PLC " //이 distinct 대신에 그룹바이로 묶음 _id때문에 distinct 사용X  (파트별 항목 중복해서 여러개 나오는거 방지)
				+" ORDER BY T1.CS_EMP_ID, "
				+" T1.WORK_DT, "
				+" T1.JOB_NO, "
				+" T1.NFC_PLC ";
		return checkListPart;

	}

	public String selectInspector(String empId, String workDt, String jobNo){
		String query = "SELECT "
				+ " T1._id,"
				+"T1.CS_TP, "
				+"T1.SELCHK_USID, "
				+"T1.SELCHK_USID_NM, "
				+"T1.SUB_SELCHK_USID, "
				+"T1.SUB_SELCHK_USID_NM "
				+" FROM		TCSQ030 T1 "
				+" WHERE	T1.CS_EMP_ID = '" + empId + "'"
				+" AND		T1.WORK_DT = '" + workDt + "'"
				+" AND		T1.JOB_NO = '" + jobNo + "'";
		return query;

	}

	public String selectCheckList(String empId, String workDt, String jobNo, String nfcPlc){
		String query ="";
		query = "SELECT	"
				+ " T1._id,"
				+" T1.CS_EMP_ID,"
				+"T1.WORK_DT,"
				+"T1.JOB_NO,"
				+"T1.NFC_PLC,"
				+"T1.CS_ITEM_CD,"
				+"T1.SMART_DESC,"
				+"T1.INPUT_TP,"
				+"T1.INPUT_TP1,"
				+"T1.INPUT_TP3,"
				+"T1.INPUT_TP7,"
				+"T1.INPUT_RMK,"
				+"T1.OVER_MONTH,"
				+"T1.MONTH_CHK_IF,"
				+"T1.MONTH_CHK "
				+" FROM		TCSQ213 T1 "
				+" WHERE	T1.CS_EMP_ID	= '" + empId + "'"
				+" AND		T1.WORK_DT		= '" + workDt + "'"
				+" AND		T1.JOB_NO		= '"+ jobNo + "'"
				+" AND		T1.NFC_PLC		= '"+ nfcPlc + "'"
				+" ORDER BY T1.CS_ITEM_CD ";


		return query;
	}

	////점검항목상세 조회
	public String selectCheckDetail(String empId, String workDt, String jobNo, String nfcPlc){
		String query = "";
		query = "SELECT	"
				+ " T1._id, "
				+ "T1.CS_EMP_ID, "
				+"T1.WORK_DT,"
				+"T1.JOB_NO, "
				+"T1.NFC_PLC,"
				+"T1.CS_ITEM_CD,"
				+"T1.SMART_DESC,"
				+"T1.INPUT_TP,"
				+"T1.INPUT_TP1,"
				+"T1.INPUT_TP3,"
				+"T1.INPUT_TP7,"
				+"T1.INPUT_RMK,"
				+"T1.OVER_MONTH,"
				+"T1.MONTH_CHK_IF,"
				+"T1.MONTH_CHK ,"
				+"T1.EL_INFO_MAP ,"
				+"T1.DEF_VAL ,"
				+"T1.DEF_VAL_ST, "
				+"T1.PRE_WORK_MM, "
				+"T1.PRE_INPUT_TP, "
				+"T1.STD_ST, "
				+"T1.INSP_METHOD, "
				+"T1.HEADER_IF"
				+" FROM		TCSQ213 T1 "
				+" WHERE	T1.CS_EMP_ID	= '" + empId + "'"
				+" AND		T1.WORK_DT		= '" + workDt + "'"
				+" AND		T1.JOB_NO		= '" + jobNo + "'"
				+" AND		T1.NFC_PLC		= '" + nfcPlc + "'"
				+" ORDER BY T1.MONTH_CHK DESC, T1.CS_ITEM_CD ASC";
		return query;
	}



	////점검항목 저장
	public String updateCheckList(String empId, String workDt, String jobNo, String nfcPlc,
								  String type1,String type3,String type7,String rmk, String ovMonth,String itemCd){
		String query = "";

		query = "UPDATE TCSQ213 "
				+" SET		INPUT_TP1		= '" + type1 + "'"
				+", INPUT_TP3	 		= '" + type3 + "'"
				+", INPUT_TP7			= '" + type7 + "'"
				+", INPUT_RMK			= '" + rmk + "'"
				+", OVER_MONTH		= '" + ovMonth + "'"
				+" WHERE	CS_EMP_ID	= '" + empId + "'"
				+" AND		WORK_DT	= '" + workDt + "'"
				+" AND		JOB_NO		= '" + jobNo + "'"
				+" AND		NFC_PLC			= '" + nfcPlc + "'"
				+" AND		CS_ITEM_CD		= '" + itemCd + "'"	;


		return query;
	}





	//정기점검 항목 파트별 점검시작 31, 작업보류 41,
	public String updateStartPartCheck(String empId, String workDt, String jobNo, String nfcPlc, String csTmFr, String jobSt){

		String query ="";
		query = "UPDATE  TCSQ210  "
				+"  SET		   CS_TM_FR		= '" + csTmFr  + "',"
				+"  JOB_ST    = '" + jobSt + "'"
				+"  WHERE   	CS_EMP_ID	='" + empId + "' "
				+"  AND		 WORK_DT	='" + workDt + "' "
				+"  AND		 JOB_NO		='" + jobNo + "' "
				+"  AND		 NFC_PLC			= '" + nfcPlc + "'"		;
		return query;
	}

	//정기점검 항목 파트별 종료 39
	public String updateEndPartCheck(String empId, String workDt, String jobNo, String nfcPlc, String csTmTo, String jobSt){

		String query ="";
		query = "UPDATE  TCSQ210 "
				+"  SET		 CS_TM_TO		= '" + csTmTo  + "',"
				+"  JOB_ST    = '" + jobSt + "'"
				+"  WHERE 	 CS_EMP_ID	='" + empId + "'"
				+"  AND		 WORK_DT	='" + workDt + "'"
				+"  AND		 JOB_NO		='" + jobNo + "'"
				+"  AND		 NFC_PLC			= '" + nfcPlc + "'"		;
		return query;
	}



	//출발시간등록
	public String updateMoveTime(String empId, String workDt, String jobNo, String moveTm, String csFr){

		String query ="";
		query = "UPDATE  WORK_TBL  "
				+" SET     JOB_ST		= '11',  "
				+" ST    =  '이동', "
				+" CS_FR    = '" + csFr + "', "
				+" MOVE_TM    = '" + moveTm + "' "
				+" WHERE 	CS_EMP_ID	='" + empId + "' "
				+" AND	      WORK_DT	='" + workDt + "'"
				+" AND	 JOB_NO		='" + jobNo + "' "	;
		return query;
	}



	//도착시간 등록

	public String updateArriveTime(String empId, String workDt, String jobNo, String arriveTm, String csFr){

		String query ="";
		query = "UPDATE  WORK_TBL  "
				+" SET     JOB_ST		= '31',  "
				+" ST    =  '작업', "
				+" CS_FR     = '" + csFr + "', "
				+"  ARRIVE_TM   = '" + arriveTm + "'"
				+"  WHERE 	 CS_EMP_ID	 ='" + empId + "'"
				+"  AND		 WORK_DT	 ='" + workDt + "'"
				+"  AND		 JOB_NO	 	='" + jobNo + "'"	;
		return query;
	}



	//작업대상 완료

	public String updateWorkComplete(String empId, String workDt, String jobNo, String completeTm,String csFr){

		String query ="";
		query = "UPDATE   WORK_TBL  "
				+"  SET	 	 JOB_ST		 = '39' ,"
				+" ST    =  '완료', "
				+" CS_FR     = '" + csFr + "', "
				+"  COMPLETE_TM    = '" + completeTm + "'"
				+"  WHERE 	 CS_EMP_ID	='" + empId + "'"
				+"  AND		 WORK_DT	='" + workDt + "'"
				+"  AND		 JOB_NO		='" + jobNo + "'"	;
		return query;
	}



	public String selectCheckTableList(String empId , String workDt){
		String query = "";
		query = "SELECT	  "
				+ "T1._id, "
				+ "T1.WORK_DT , "
				+"  T1.BLDG_NO, "
				+"  T1.BLDG_NM,"
				+"  T1.REF_CONTR_NO,"
				+"  ( CASE WHEN T2.CNT = T1.CNT THEN '전체완료' ELSE '미처리' END ) E_TEXT, "
				+"  T2.CNT I_CNT, "
				+"  T1.CNT T_CNT "
				+"   FROM (SELECT  _id, WORK_DT, BLDG_NO, BLDG_NM, REF_CONTR_NO, COUNT(*)  "
				+ "  CNT FROM WORK_TBL WHERE CS_EMP_ID ='" + empId + "'" + 	" AND WORK_DT ='" + workDt + "'"
				+"   GROUP BY WORK_DT, BLDG_NO,BLDG_NM, REF_CONTR_NO ) T1  "
				+"   LEFT OUTER JOIN ( SELECT WORK_DT, BLDG_NO,BLDG_NM, REF_CONTR_NO, COUNT(*)"
				+" CNT FROM WORK_TBL WHERE CS_EMP_ID='" + empId + "'" + 	" AND WORK_DT ='" + workDt + "'"+"  AND JOB_ST = '39'  "
				+"  GROUP BY WORK_DT, BLDG_NO, BLDG_NM, REF_CONTR_NO ) T2 ON T1.BLDG_NO = T2.BLDG_NO ";

		return query;
	}

	/**
	 * 주/부점검자 업데이트 TCSQ030
	 * @param csTp
	 * @param selChkUsid
	 * @param selChkUsNm
	 * @param subSelChkUsid
	 * @param subSelChkUsNm
	 * @param empId
	 * @param workDt
	 * @param jobNo
	 * @return
	 */
	public String updateInspector(String csTp, String selChkUsid, String selChkUsNm, String subSelChkUsid,
								  String subSelChkUsNm, String empId, String workDt, String jobNo) {

		String query = "";
		query = "UPDATE TCSQ030 SET CS_TP = '" + csTp + "', "
				+ "  SELCHK_USID = '" + selChkUsid + "', "
				+ "  SELCHK_USID_NM = '" + selChkUsNm + "', "
				+ "  SUB_SELCHK_USID = '" + subSelChkUsid + "', "
				+ "  SUB_SELCHK_USID_NM = '" + subSelChkUsNm + "'"
				+ "  WHERE CS_EMP_ID	='" + empId + "'" + "  AND		 WORK_DT	='"
				+ workDt + "'" + "  AND		 JOB_NO		='" + jobNo + "'";
		return query;
	}

	public String selectCheckTableConfirm(String empId, String workDt , String jobNo, String bldgNo, String carNo){
		String query = "";
		query = "SELECT	 T1.CS_EMP_ID, "
				+ " T1.WORK_DT,"
				+ " T1.BLDG_NO, "
				+ " T1.CAR_NO, "
				+ " T2.NFC_PLC, "
				+ " (SELECT NFC_PLC_NM FROM TCSQ210 AS B WHERE B.NFC_PLC = T2.NFC_PLC) NFC_PLC_NM, "
				+ " T3.CS_ITEM_CD,  "
				+ " T3.SMART_DESC,  "
				+ " T3.CS_TOOLS, "
				+ " T3.STD_ST, "
				+ " T3.MONTH_CHK_IF, "
				+ " T3.MONTH_CHK, "
				+ " T3.INPUT_TP, "
				+ " T3.INPUT_TP1, "
				+ " T3.INPUT_TP3, "
				+ " T3.INPUT_TP7, "
				+ " T3.INPUT_RMK, "
				+ " T3.OVER_MONTH "
				+ " FROM TCSQ030 AS T1 "
				+ " INNER JOIN TCSQ210 AS T2 ON T2.CS_EMP_ID = T1.CS_EMP_ID AND T2.WORK_DT = T1.WORK_DT AND T2.JOB_NO = T1.JOB_NO "
				+ " INNER JOIN TCSQ213 AS T3 ON T3.CS_EMP_ID = T2.CS_EMP_ID AND T3.WORK_DT = T2.WORK_DT AND T3.JOB_NO = T2.JOB_NO AND T3.NFC_PLC = T2.NFC_PLC "
				+ " WHERE T1.CS_EMP_ID = '" + empId + "' AND T1.WORK_DT ='" + workDt + "'"
				+ " AND T1.BLDG_NO ='" + bldgNo + "'  AND T1.CAR_NO ='" + carNo + "'";

		return query;

	}

	public String selectNfcUdid(String nfcUdid){
		String query = "";
		query = "  SELECT B1.CS_EMP_ID, B1.WORK_DT, B1.JOB_NO, B1.NFC_PLC, B1.NFC_PLC_NM, A.BLDG_NO, A.BLDG_NM, A.CAR_NO "
				+" FROM TCSQ210 AS B1 INNER JOIN WORK_TBL A ON B1.CS_EMP_ID = A.CS_EMP_ID AND B1.WORK_DT = A.WORK_DT AND B1.JOB_NO = A.JOB_NO "
				+"  WHERE B1.NFC_UDID ='" + nfcUdid + "'";




		return query;
	}


	public String selectNfcUdidDebug(String nfcUdid, String carNo, String plc){
		String query = "";
		query = "  SELECT B1.CS_EMP_ID, B1.WORK_DT, B1.JOB_NO, B1.NFC_PLC, B1.NFC_PLC_NM, A.BLDG_NO, A.BLDG_NM, A.CAR_NO "
				+" FROM TCSQ210 AS B1 INNER JOIN WORK_TBL A ON B1.CS_EMP_ID = A.CS_EMP_ID AND B1.WORK_DT = A.WORK_DT AND B1.JOB_NO = A.JOB_NO "
				+"  WHERE B1.NFC_PLC ='" + plc + "'"
				+"  AND A.CAR_NO ='" + carNo + "'";




		return query;
	}


	//미처리 이관에 대한 잡st 변경해주기
	public String updateJobStChange(String empId, String workDt, String jobNo,String csFr, String jobSt, String stName){

		String query ="";
		query = "UPDATE   WORK_TBL  "
				+"  SET	 	 JOB_ST		 = '" + jobSt+ "' ,"
				+" ST    =  '" + stName + "', "
				+" CS_FR     = '" + csFr + "' "
				+"  WHERE 	 CS_EMP_ID	='" + empId + "'"
				+"  AND		 WORK_DT	='" + workDt + "'"
				+"  AND		 JOB_NO		='" + jobNo + "'"	;
		return query;
	}

	public String selectAdminInfo(String bldgNo){
		String query = "";

		query = "SELECT T1._id, T1.CLIENT_NM, T1.MOBILE, T1.MAIL_ADDR, T1.PHONE "
				+" FROM TCSC010 AS T1 WHERE T1.BLDG_NO = '" + bldgNo + "' ORDER BY T1.CLIENT_ID";
		return query;
	}

	public long insertTCSQ050(Context context, WO_WT00_R01_ITEM00 workTargetData, String jobAct) {

		ContentValues values = this.makeInsertData(context, workTargetData, jobAct);

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long result = db.insert("TCSQ050", null, values);
		db.close();

		return result;
	}

	private ContentValues makeInsertData(Context context, WO_WT00_R01_ITEM00 data, String jobAct){

		ContentValues values = new ContentValues();
		values.put("CS_EMP_ID", data.getCS_EMP_ID());
		values.put("WORK_DT", data.getWORK_DT());
		values.put("JOB_NO", data.getJOB_NO());
		values.put("ACT_NO", this.selectTCSQ050rawCount(context)+1);
		values.put("WORK_CD", data.getWORK_CD());
		values.put("JOB_TM", DateUtil.nowDateTime());
		values.put("JOB_ACT", jobAct);
		values.put("JOB_ST", this.selectJobSt(context, data));
		values.put("BLDG_NO", data.getBLDG_NO());
		values.put("CAR_NO", data.getCAR_NO());
		values.put("REF_CONTR_NO", data.getREF_CONTR_NO());
		values.put("SUPPORT_CD", this.selectSupportCd(context, data));
		values.put("ENG_ST", this.selectEngSt(context,data));
		values.put("LOCAL_COORD_X", "0.0000000000");
		values.put("LOCAL_COORD_Y", "0.0000000000");
		values.put("JOB_COORD_X", "0.0000000000");
		values.put("JOB_COORD_Y", "0.0000000000");
		values.put("ADDR", data.getADDR());
		values.put("DEVICE_NO", new DeviceUniqNumber(context).getPhoneNumber());
		values.put("CANCEL_IF", "");
		values.put("CANCEL_DT", "");
		values.put("RMK", "");

		return values;
	}

	public int selectTCSQ050rawCount(Context context){
		int count = 0;
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor c = db.rawQuery("SELECT count() from TCSQ050 ", null);
		c.moveToFirst();
		String getC = c.getString(0);
		c.close();
		db.close();
		dbHelper.close();
		count = Integer.valueOf(getC);
		return count;
	}

	private String selectEngSt(Context context, WO_WT00_R01_ITEM00 data ){
		String engST ="";
		try{

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT JOB_ST FROM WORK_TBL WHERE CS_EMP_ID = '" + data.getCS_EMP_ID() + "' AND WORK_DT = '" +
					data.getWORK_DT() + "' AND JOB_NO = '" + data.getJOB_NO() + "' AND WORK_CD = '"+ data.getWORK_CD() + "'", null);
			c.moveToFirst();
			String getC = c.getString(0);
			c.close();
			db.close();
			dbHelper.close();
			engST = getC;
		}catch(Exception e){
			engST = "00";
		}

		return engST;
	}

	private String selectJobSt(Context context, WO_WT00_R01_ITEM00 data ){
		String jobSt ="";
		try{

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT JOB_ST FROM WORK_TBL WHERE CS_EMP_ID = '" + data.getCS_EMP_ID() + "' AND WORK_DT = '" +
					data.getWORK_DT() + "' AND JOB_NO = '" + data.getJOB_NO() + "' AND WORK_CD = '"+ data.getWORK_CD() + "'", null);
			c.moveToFirst();
			String getC = c.getString(0);
			c.close();
			db.close();
			dbHelper.close();
			jobSt = getC;
		}catch(Exception e){
			jobSt = "00";
		}

		return jobSt;
	}
	private String selectSupportCd(Context context, WO_WT00_R01_ITEM00 data ){
		String supportCd ="";
		try{

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT SUPPORT_CD FROM TCSQ030 WHERE CS_EMP_ID = '" + data.getCS_EMP_ID() + "' AND WORK_DT = '" +
					data.getWORK_DT() + "' AND JOB_NO = '" + data.getJOB_NO() + "' AND WORK_CD = '"+ data.getWORK_CD() + "'", null);
			c.moveToFirst();
			String getC = c.getString(0);
			c.close();
			db.close();
			dbHelper.close();
			supportCd = getC;
		}catch(Exception e){
			supportCd = "00";
		}

		return supportCd;
	}



	public String selectTCSQ050(String empId, String workDt){

		String query =	"SELECT	"
				+"CS_EMP_ID,"
				+"WORK_DT,"
				+"JOB_NO,"
				+"ACT_NO,"
				+"WORK_CD,"
				+"JOB_TM,"
				+"JOB_ACT,"
				+"JOB_ST,"
				+"BLDG_NO,"
				+"CAR_NO,"
				+"REF_CONTR_NO,"
				+"SUPPORT_CD,"
				+"ENG_ST,"
				+"LOCAL_COORD_X,"
				+"LOCAL_COORD_Y,"
				+"JOB_COORD_X,"
				+"JOB_COORD_Y,"
				+"ADDR,"
				+"DEVICE_NO,"
				+"CANCEL_IF,"
				+"CANCEL_DT,"
				+"RMK"
				+" FROM		TCSQ050 "
				+" WHERE   CS_EMP_ID = '" + empId + "'"
				+" AND		WORK_DT = '" + workDt + "'"
				+" AND		ACT_NO = (SELECT MIN(ACT_NO) FROM TCSQ050 WHERE CS_EMP_ID = '" + empId + "'"+" AND		WORK_DT = '" + workDt + "')";



		return query;

	}


	//TCSQ050 테이블 지우기
	public void deleteRawTCSQ050(Context context, TableTCSQ050 tcsq050) {
		String query = "";
		query = "DELETE  FROM TCSQ050"
				+" WHERE   CS_EMP_ID = '" + tcsq050.getCS_EMP_ID() + "'"
				+" AND		WORK_DT = '" + tcsq050.getWORK_DT() + "'"
				+" AND		ACT_NO = '" + tcsq050.getACT_NO() + "'";

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();

	}


}
