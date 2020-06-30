package com.jinsit.kmec.DB;

/**
 * 3 : LOCAL DB DATA DOWN => 점검계획 점검항목 TCSQ213 3 : PROCEDURE [dbo].[SPDC104M];6
 *
 * @author 원성민
 *
 */
public class TableTCSQ213 {

	public String CS_EMP_ID; // 점검사원
	public String WORK_DT; // 작업일자
	public String JOB_NO; // 작업번호
	public String NFC_PLC; // 부착위치
	public String CS_ITEM_CD; // 점검항목코드
	public String CS_TOOLS; // 점검공구
	public String STD_ST; // 점검주기
	public String INPUT_TP; // 입력형식(BC:CS188)
	public String INPUT_TP1; // 상태값(ABC)
	public String INPUT_TP3; // 수치값(....)
	public String INPUT_TP7; // 유무값(OX)
	public String INPUT_RMK; // 검사내역비고
	public String OVER_MONTH; // 이월여부(Y:이월;N:이월아님)
	public String MONTH_CHK; // 중점점검월여부(Y/N)
	public String RMK; // 비고
	public String ISRT_USR_ID; // 최초등록자
	public String ISRT_DT; // 최초등록일
	public String UPDT_USR_ID; // 최종수정자
	public String UPDT_DT; // 최종수정일
	public String CS_LOW_NM; // 점검명(소)
	public String SMART_DESC; // 스마트폰용 DESC.
	public String MNG_DESC; // 관리원용 DESC
	public String MONTH_CHK_IF; // 이월가능여부(1:이월가능)
	public String EL_INFO_MAP; // 승강원분류번호
	public String DEF_VAL; // 기본값
	public String DEF_VAL_ST; // 기본값 변경상태 변경안됨:0 , 변경되면:1
	//20181218 yowonsm 이전점검결과 추가
	public String PRE_WORK_MM;	//이전 점검 월
	public String PRE_INPUT_TP;	//이전점검 결과값
	public String HEADER_IF;	//헤더여부 "1" 인경우 헤더
	public String INSP_METHOD;	//점검방법
	public TableTCSQ213() {
		super();
	}

	public TableTCSQ213(String cS_EMP_ID, String wORK_DT, String jOB_NO,
						String nFC_PLC, String cS_ITEM_CD, String cS_TOOLS, String sTD_ST,
						String iNPUT_TP, String iNPUT_TP1, String iNPUT_TP3,
						String iNPUT_TP7, String iNPUT_RMK, String oVER_MONTH,
						String mONTH_CHK, String rMK, String iSRT_USR_ID, String iSRT_DT,
						String uPDT_USR_ID, String uPDT_DT, String cS_LOW_NM,
						String sMART_DESC, String mNG_DESC, String mONTH_CHK_IF,String elInfoMap, String defVal,String defValSt, String preWorkMM, String preInputTp, String headerIf, String inspMethod) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		CS_ITEM_CD = cS_ITEM_CD;
		CS_TOOLS = cS_TOOLS;
		STD_ST = sTD_ST;
		INPUT_TP = iNPUT_TP;
		INPUT_TP1 = iNPUT_TP1;
		INPUT_TP3 = iNPUT_TP3;
		INPUT_TP7 = iNPUT_TP7;
		INPUT_RMK = iNPUT_RMK;
		OVER_MONTH = oVER_MONTH;
		MONTH_CHK = mONTH_CHK;
		RMK = rMK;
		ISRT_USR_ID = iSRT_USR_ID;
		ISRT_DT = iSRT_DT;
		UPDT_USR_ID = uPDT_USR_ID;
		UPDT_DT = uPDT_DT;
		CS_LOW_NM = cS_LOW_NM;
		SMART_DESC = sMART_DESC;
		MNG_DESC = mNG_DESC;
		MONTH_CHK_IF = mONTH_CHK_IF;
		EL_INFO_MAP = elInfoMap;
		DEF_VAL=defVal;
		DEF_VAL_ST=defValSt;
		PRE_WORK_MM = preWorkMM;
		PRE_INPUT_TP = preInputTp;
		this.HEADER_IF = headerIf;
		INSP_METHOD = inspMethod;
	}

	public String getCS_EMP_ID() {
		return CS_EMP_ID;
	}

	public void setCS_EMP_ID(String cS_EMP_ID) {
		CS_EMP_ID = cS_EMP_ID;
	}

	public String getWORK_DT() {
		return WORK_DT;
	}

	public void setWORK_DT(String wORK_DT) {
		WORK_DT = wORK_DT;
	}

	public String getJOB_NO() {
		return JOB_NO;
	}

	public void setJOB_NO(String jOB_NO) {
		JOB_NO = jOB_NO;
	}

	public String getNFC_PLC() {
		return NFC_PLC;
	}

	public void setNFC_PLC(String nFC_PLC) {
		NFC_PLC = nFC_PLC;
	}

	public String getCS_ITEM_CD() {
		return CS_ITEM_CD;
	}

	public void setCS_ITEM_CD(String cS_ITEM_CD) {
		CS_ITEM_CD = cS_ITEM_CD;
	}

	public String getCS_TOOLS() {
		return CS_TOOLS;
	}

	public void setCS_TOOLS(String cS_TOOLS) {
		CS_TOOLS = cS_TOOLS;
	}

	public String getSTD_ST() {
		return STD_ST;
	}

	public void setSTD_ST(String sTD_ST) {
		STD_ST = sTD_ST;
	}

	public String getINPUT_TP() {
		return INPUT_TP;
	}

	public void setINPUT_TP(String iNPUT_TP) {
		INPUT_TP = iNPUT_TP;
	}

	public String getINPUT_TP1() {
		return INPUT_TP1;
	}

	public void setINPUT_TP1(String iNPUT_TP1) {
		INPUT_TP1 = iNPUT_TP1;
	}

	public String getINPUT_TP3() {
		return INPUT_TP3;
	}

	public void setINPUT_TP3(String iNPUT_TP3) {
		INPUT_TP3 = iNPUT_TP3;
	}

	public String getINPUT_TP7() {
		return INPUT_TP7;
	}

	public void setINPUT_TP7(String iNPUT_TP7) {
		INPUT_TP7 = iNPUT_TP7;
	}

	public String getINPUT_RMK() {
		return INPUT_RMK;
	}

	public void setINPUT_RMK(String iNPUT_RMK) {
		INPUT_RMK = iNPUT_RMK;
	}

	public String getOVER_MONTH() {
		return OVER_MONTH;
	}

	public void setOVER_MONTH(String oVER_MONTH) {
		OVER_MONTH = oVER_MONTH;
	}

	public String getMONTH_CHK() {
		return MONTH_CHK;
	}

	public void setMONTH_CHK(String mONTH_CHK) {
		MONTH_CHK = mONTH_CHK;
	}

	public String getRMK() {
		return RMK;
	}

	public void setRMK(String rMK) {
		RMK = rMK;
	}

	public String getISRT_USR_ID() {
		return ISRT_USR_ID;
	}

	public void setISRT_USR_ID(String iSRT_USR_ID) {
		ISRT_USR_ID = iSRT_USR_ID;
	}

	public String getISRT_DT() {
		return ISRT_DT;
	}

	public void setISRT_DT(String iSRT_DT) {
		ISRT_DT = iSRT_DT;
	}

	public String getUPDT_USR_ID() {
		return UPDT_USR_ID;
	}

	public void setUPDT_USR_ID(String uPDT_USR_ID) {
		UPDT_USR_ID = uPDT_USR_ID;
	}

	public String getUPDT_DT() {
		return UPDT_DT;
	}

	public void setUPDT_DT(String uPDT_DT) {
		UPDT_DT = uPDT_DT;
	}

	public String getCS_LOW_NM() {
		return CS_LOW_NM;
	}

	public void setCS_LOW_NM(String cS_LOW_NM) {
		CS_LOW_NM = cS_LOW_NM;
	}

	public String getSMART_DESC() {
		return SMART_DESC;
	}

	public void setSMART_DESC(String sMART_DESC) {
		SMART_DESC = sMART_DESC;
	}

	public String getMNG_DESC() {
		return MNG_DESC;
	}

	public void setMNG_DESC(String mNG_DESC) {
		MNG_DESC = mNG_DESC;
	}

	public String getMONTH_CHK_IF() {
		return MONTH_CHK_IF;
	}

	public void setMONTH_CHK_IF(String mONTH_CHK_IF) {
		MONTH_CHK_IF = mONTH_CHK_IF;
	}

	public String getEL_INFO_MAP() {
		return EL_INFO_MAP;
	}

	public void setEL_INFO_MAP(String eL_INFO_MAP) {
		EL_INFO_MAP = eL_INFO_MAP;
	}

	public String getDEF_VAL() {
		return DEF_VAL;
	}

	public void setDEF_VAL(String dEF_VAL) {
		DEF_VAL = dEF_VAL;
	}

	public String getDEF_VAL_ST() {
		return DEF_VAL_ST;
	}

	public void setDEF_VAL_ST(String dEF_VAL_ST) {
		DEF_VAL_ST = dEF_VAL_ST;
	}

	public String getPRE_WORK_MM() {
		return PRE_WORK_MM;
	}

	public void setPRE_WORK_MM(String PRE_WORK_MM) {
		this.PRE_WORK_MM = PRE_WORK_MM;
	}

	public String getPRE_INPUT_TP() {
		return PRE_INPUT_TP;
	}

	public void setPRE_INPUT_TP(String PRE_INPUT_TP) {
		this.PRE_INPUT_TP = PRE_INPUT_TP;
	}

	public String getHEADER_IF() {
		return HEADER_IF;
	}

	public void setHEADER_IF(String HEADER_IF) {
		this.HEADER_IF = HEADER_IF;
	}

	public String getINSP_METHOD() {
		return INSP_METHOD;
	}

	public void setINSP_METHOD(String INSP_METHOD) {
		this.INSP_METHOD = INSP_METHOD;
	}
}