package com.jinsit.kmec.DB;


/**
 * -- 1 : LOCAL DB DATA DOWN  => 점검계획 TCSQ030
 * PROCEDURE [dbo].[SPDC104M]
 * @author 원성민
 *
 */
public class TableTCSQ030 {

	public String CS_EMP_ID; // 점검사원
	public String WORK_DT; // 작업일자
	public String JOB_NO; // 작업번호
	public String WORK_CD; // 작업코드
	public String CS_TM_FR; // 점검시간FR
	public String CS_TM_TO; // 점검시간TO
	public String CS_TM; // 점검시간(분)
	public String BLDG_NO; // 건물관리번호
	public String CAR_NO; // 호기
	public String REF_CONTR_NO; // 관련계약번호
	public String SUPPORT_CD; // 지원부서
	public String JOB_ST; // 작업상태(BC:C0491)
	public String ENG_ST; // 기사상태(BC:C0490)
	public String YET_REASON; // 미완료사유
	public String PLAN_YYMM; // 계획년월
	public String PLAN_SQ; // 계획NO
	public String WK_ORDER; // 주차
	public String WK_DAY; // 요일
	public String CS_DEPT_CD; // 점검부서
	public String INSP_TIME_BC; // 점검시간대
	public String RMK; // 비고
	public String ISRT_USR_ID; // 최초등록자
	public String ISRT_DT; // 최초등록일
	public String UPDT_USR_ID; // 최종수정자
	public String UPDT_DT; // 최종수정일

	public String CS_TP; //1인 점검('1') OR 2인 점검(주점검자 ID존재 시)('2')
	public String SELCHK_USID; //주점검자ID
	public String SELCHK_USID_NM; // 주점검자 이름
	public String SUB_SELCHK_USID; // 보조점검자 ID
	public String SUB_SELCHK_USID_NM; // 보조점검자 이름


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

	public String getWORK_CD() {
		return WORK_CD;
	}

	public void setWORK_CD(String wORK_CD) {
		WORK_CD = wORK_CD;
	}

	public String getCS_TM_FR() {
		return CS_TM_FR;
	}

	public void setCS_TM_FR(String cS_TM_FR) {
		CS_TM_FR = cS_TM_FR;
	}

	public String getCS_TM_TO() {
		return CS_TM_TO;
	}

	public void setCS_TM_TO(String cS_TM_TO) {
		CS_TM_TO = cS_TM_TO;
	}

	public String getCS_TM() {
		return CS_TM;
	}

	public void setCS_TM(String cS_TM) {
		CS_TM = cS_TM;
	}

	public String getBLDG_NO() {
		return BLDG_NO;
	}

	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
	}

	public String getCAR_NO() {
		return CAR_NO;
	}

	public void setCAR_NO(String cAR_NO) {
		CAR_NO = cAR_NO;
	}

	public String getREF_CONTR_NO() {
		return REF_CONTR_NO;
	}

	public void setREF_CONTR_NO(String rEF_CONTR_NO) {
		REF_CONTR_NO = rEF_CONTR_NO;
	}

	public String getSUPPORT_CD() {
		return SUPPORT_CD;
	}

	public void setSUPPORT_CD(String sUPPORT_CD) {
		SUPPORT_CD = sUPPORT_CD;
	}

	public String getJOB_ST() {
		return JOB_ST;
	}

	public void setJOB_ST(String jOB_ST) {
		JOB_ST = jOB_ST;
	}

	public String getENG_ST() {
		return ENG_ST;
	}

	public void setENG_ST(String eNG_ST) {
		ENG_ST = eNG_ST;
	}

	public String getYET_REASON() {
		return YET_REASON;
	}

	public void setYET_REASON(String yET_REASON) {
		YET_REASON = yET_REASON;
	}

	public String getPLAN_YYMM() {
		return PLAN_YYMM;
	}

	public void setPLAN_YYMM(String pLAN_YYMM) {
		PLAN_YYMM = pLAN_YYMM;
	}

	public String getPLAN_SQ() {
		return PLAN_SQ;
	}

	public void setPLAN_SQ(String pLAN_SQ) {
		PLAN_SQ = pLAN_SQ;
	}

	public String getWK_ORDER() {
		return WK_ORDER;
	}

	public void setWK_ORDER(String wK_ORDER) {
		WK_ORDER = wK_ORDER;
	}

	public String getWK_DAY() {
		return WK_DAY;
	}

	public void setWK_DAY(String wK_DAY) {
		WK_DAY = wK_DAY;
	}

	public String getCS_DEPT_CD() {
		return CS_DEPT_CD;
	}

	public void setCS_DEPT_CD(String cS_DEPT_CD) {
		CS_DEPT_CD = cS_DEPT_CD;
	}

	public String getINSP_TIME_BC() {
		return INSP_TIME_BC;
	}

	public void setINSP_TIME_BC(String iNSP_TIME_BC) {
		INSP_TIME_BC = iNSP_TIME_BC;
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

	public TableTCSQ030(String cS_EMP_ID, String wORK_DT, String jOB_NO,
						String wORK_CD, String cS_TM_FR, String cS_TM_TO, String cS_TM,
						String bLDG_NO, String cAR_NO, String rEF_CONTR_NO,
						String sUPPORT_CD, String jOB_ST, String eNG_ST, String yET_REASON,
						String pLAN_YYMM, String pLAN_SQ, String wK_ORDER, String wK_DAY,
						String cS_DEPT_CD, String iNSP_TIME_BC, String rMK,
						String iSRT_USR_ID, String iSRT_DT, String uPDT_USR_ID,
						String uPDT_DT) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		WORK_CD = wORK_CD;
		CS_TM_FR = cS_TM_FR;
		CS_TM_TO = cS_TM_TO;
		CS_TM = cS_TM;
		BLDG_NO = bLDG_NO;
		CAR_NO = cAR_NO;
		REF_CONTR_NO = rEF_CONTR_NO;
		SUPPORT_CD = sUPPORT_CD;
		JOB_ST = jOB_ST;
		ENG_ST = eNG_ST;
		YET_REASON = yET_REASON;
		PLAN_YYMM = pLAN_YYMM;
		PLAN_SQ = pLAN_SQ;
		WK_ORDER = wK_ORDER;
		WK_DAY = wK_DAY;
		CS_DEPT_CD = cS_DEPT_CD;
		INSP_TIME_BC = iNSP_TIME_BC;
		RMK = rMK;
		ISRT_USR_ID = iSRT_USR_ID;
		ISRT_DT = iSRT_DT;
		UPDT_USR_ID = uPDT_USR_ID;
		UPDT_DT = uPDT_DT;
	}

	public TableTCSQ030(String cS_EMP_ID, String wORK_DT, String jOB_NO,
						String wORK_CD, String cS_TM_FR, String cS_TM_TO, String cS_TM,
						String bLDG_NO, String cAR_NO, String rEF_CONTR_NO,
						String sUPPORT_CD, String jOB_ST, String eNG_ST, String yET_REASON,
						String pLAN_YYMM, String pLAN_SQ, String wK_ORDER, String wK_DAY,
						String cS_DEPT_CD, String iNSP_TIME_BC, String rMK,
						String iSRT_USR_ID, String iSRT_DT, String uPDT_USR_ID,
						String uPDT_DT, String csTp, String selChkUsId, String selChkUsIdNm,String subSelChkUsId,String subSelChkUsIdNm)
	{
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		WORK_CD = wORK_CD;
		CS_TM_FR = cS_TM_FR;
		CS_TM_TO = cS_TM_TO;
		CS_TM = cS_TM;
		BLDG_NO = bLDG_NO;
		CAR_NO = cAR_NO;
		REF_CONTR_NO = rEF_CONTR_NO;
		SUPPORT_CD = sUPPORT_CD;
		JOB_ST = jOB_ST;
		ENG_ST = eNG_ST;
		YET_REASON = yET_REASON;
		PLAN_YYMM = pLAN_YYMM;
		PLAN_SQ = pLAN_SQ;
		WK_ORDER = wK_ORDER;
		WK_DAY = wK_DAY;
		CS_DEPT_CD = cS_DEPT_CD;
		INSP_TIME_BC = iNSP_TIME_BC;
		RMK = rMK;
		ISRT_USR_ID = iSRT_USR_ID;
		ISRT_DT = iSRT_DT;
		UPDT_USR_ID = uPDT_USR_ID;
		UPDT_DT = uPDT_DT;
		CS_TP = csTp;
		SELCHK_USID = selChkUsId;
		SELCHK_USID_NM = selChkUsIdNm;
		SUB_SELCHK_USID = subSelChkUsId;
		SUB_SELCHK_USID_NM = subSelChkUsIdNm;
	}

	public TableTCSQ030() {
		super();
	}

}
