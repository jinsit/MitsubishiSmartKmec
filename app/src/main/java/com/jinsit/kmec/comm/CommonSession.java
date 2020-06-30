package com.jinsit.kmec.comm;

import java.util.Map;

import org.json.JSONException;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

import com.jinsit.kmec.comm.jinLib.EasyJsonMap;

public class CommonSession extends ContextWrapper {

	//Field
	private SharedPreferences sessionManager;
	private SharedPreferences constantSession;
	private SharedPreferences commuteManager;	//출퇴근 저장

	private EasyJsonMap ejm;
	private Map mp;
	private Map mp02;
	private Map commuteMap;

	//Constructor
	public CommonSession(Context context) {
		super(context);
		sessionManager  = getSharedPreferences("sessionManager" , MODE_PRIVATE);
		constantSession = getSharedPreferences("constantSession", MODE_PRIVATE);
		commuteManager = getSharedPreferences("commuteManager", MODE_PRIVATE);

		mp   = sessionManager.getAll();
		mp02 = constantSession.getAll();
		commuteMap = commuteManager.getAll();

	}

	//Setter
	public void setValues(EasyJsonMap ejm){
		this.ejm = ejm;
		createLoginSession();
		createConstantSession();
	}

	//Getter
	public String getEmpNm() {
		return mp.get("empNm").toString();
	}
	public String getEmpId() {
		return mp.get("empId").toString();
	}
	public String getPwd() {
		return mp.get("pwd").toString();
	}
	public String getPhone() {
		return mp.get("phone").toString();
	}
	public String getDeptCd() {
		return mp.get("deptCd").toString();
	}
	public String getCollectTerm() {
		return mp.get("collectTerm").toString();
	}
	public String getIntMm() {
		return mp.get("intMm").toString();
	}
	public String getWorkDt() {
		return mp.get("workDt").toString();
	}
	public String getPwChk() {
		return mp.get("pwChk").toString();
	}
	public String getDeviceChk() {
		return mp.get("deviceChk").toString();
	}
	public String getMngUsrId(){
		return mp.get("mngUsrId").toString();
	}

	public boolean isLoggined(){
		boolean result = mp.containsKey("isLoggined");
		if(result){
			return (Boolean) mp.get("isLoggined");
		}else if(!result){
			return false;
		}
		return false;
	}
	public boolean isAttended(){
		boolean result = mp.containsKey("isAttended");
		if(result){
			return (Boolean) mp.get("isAttended");
		}else if(!result){
			return false;
		}
		return false;
	}
	public void setIsAttended(boolean paramBoolean){
		sessionManager = getSharedPreferences("sessionManager", MODE_PRIVATE);
		SharedPreferences.Editor editor = sessionManager.edit();
		editor.putBoolean("isAttended", paramBoolean);
		editor.commit();
		mp.put("isAttended", paramBoolean);

	}

	public void setLogout(){
		sessionManager = getSharedPreferences("sessionManager", MODE_PRIVATE);
		SharedPreferences.Editor editor = sessionManager.edit();
			/*editor.putString("empNm"		, ejm.getValue("EMP_NM"));
			editor.putString("empId"		, ejm.getValue("EMP_ID"));
			editor.putString("pwd"			, ejm.getValue("pwd").toString());
			editor.putString("phone"		, ejm.getValue("PHONE"));
			editor.putString("deptCd"		, ejm.getValue("DEPT_CD"));
			editor.putString("collectTerm"	, ejm.getValue("COLLECT_TERM"));
			editor.putString("intMm"		, ejm.getValue("INT_MM"));
			editor.putString("workDt"		, ejm.getValue("WORK_DT"));
			editor.putString("pwChk"		, ejm.getValue("PW_CHK"));
			editor.putString("deviceChk"	, ejm.getValue("DEVICE_CHK"));*/
		editor.putBoolean("isLoggined"	, false);
		editor.commit();

	}

	public void createLoginSession(){

		sessionManager = getSharedPreferences("sessionManager", MODE_PRIVATE);
		SharedPreferences.Editor editor = sessionManager.edit();
		try {
			editor.putString("empNm"		, ejm.getValue("EMP_NM"));
			editor.putString("empId"		, ejm.getValue("EMP_ID"));
			editor.putString("pwd"			, ejm.getValue("pwd").toString());
			editor.putString("phone"		, ejm.getValue("PHONE"));
			editor.putString("deptCd"		, ejm.getValue("DEPT_CD"));
			editor.putString("collectTerm"	, ejm.getValue("COLLECT_TERM"));
			editor.putString("intMm"		, ejm.getValue("INT_MM"));
			editor.putString("workDt"		, ejm.getValue("WORK_DT"));
			editor.putString("pwChk"		, ejm.getValue("PW_CHK"));
			editor.putString("deviceChk"	, ejm.getValue("DEVICE_CHK"));
			editor.putString("mngUsrId"	, ejm.getValue("MNG_USR_ID"));	//승강원 ID추가 20170920 yowonsm
			editor.putBoolean("isLoggined"	, true);
			editor.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void logout(String empId){
	/*	SharedPreferences.Editor editor = sessionManager.edit();
		editor.clear();
		editor.commit();
		mp.clear();*/
		this.setLogout();

	}



	//ConstantSession
	private void createConstantSession(){
		constantSession = getSharedPreferences("constantSession", MODE_PRIVATE);
		SharedPreferences.Editor editor02 = constantSession.edit();
		try {
			editor02.putString("empId", ejm.getValue("EMP_ID"));
			editor02.commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getContEmpId(){
		return mp02.get("empId").toString();
	}
	public boolean hasContEmpId(){
		if(mp02.containsKey("empId")){
			return true;
		}
		return false;
	}



	public void createCommute(String empId, String empNm, String workDt, String commuteTime, String status,
							  String lat, String lng, String officeName, boolean isFailed)
	{
		commuteManager = getSharedPreferences("commuteManager", MODE_PRIVATE);
		SharedPreferences.Editor editor = commuteManager.edit();
		try
		{
			editor.putString("commuteEmpNm"		, empNm);
			editor.putString("commuteEmpId"		, empId);
			editor.putString("commuteWorkDt"		, workDt);
			editor.putString("commuteTime"		, commuteTime);
			editor.putString("status"		, status);
			editor.putString("latitude"		, lat);
			editor.putString("longitude"		, lng);
			editor.putString("officeName", officeName);
			editor.putBoolean("isFailed"	, isFailed);

			editor.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//Setter
	public void setCommute(String empId, String empNm, String workDt, String commuteTime, String status,
						   String lat, String lng, String officeName, boolean isFailed)
	{
		createCommute(empId, empNm, workDt, commuteTime, status, lat, lng, officeName, isFailed);
	}

	public String getCommuteEmpNm(){
		return commuteMap.get("commuteEmpNm").toString();
	}
	public String getCommuteEmpId(){
		return commuteMap.get("commuteEmpId").toString();
	}
	public String getCommuteWorkDt(){
		return commuteMap.get("commuteWorkDt").toString();
	}
	public String getCommuteTime(){
		if(commuteMap.containsKey("commuteTime") == true)
		{
			return commuteMap.get("commuteTime").toString();
		}
		else
		{
			return "";
		}
	}
	public String getCommuteStatus(){
		return commuteMap.get("status").toString();
	}
	public String getLatitude(){
		return commuteMap.get("latitude").toString();
	}
	public String getLongitude(){
		return commuteMap.get("longitude").toString();
	}

	public String getOfficeName(){
		return commuteMap.get("officeName").toString();
	}

	public boolean isCommuteFailed(){
		return commuteManager.getBoolean("isFailed", false);
//		boolean result = commuteMap.containsKey("isFailed");
//		if(result == true)
//		{
//			return (Boolean) commuteMap.get("isFailed");
//		}
//		else 
//		{
//			return false;
//		}
	}

	public boolean getCommuteSuccess(){

		boolean result = commuteMap.containsKey("success");
		if(result == true)
		{
			return (Boolean) commuteMap.get("success");
		}
		else
		{
			return false;
		}
	}


};