package com.jinsit.kmec.DB;



/**
 * -- 실시간 작업내역  => 실시간 데이터 쌓기 TCSQ050
 * @author 원성민
 */
public class TableTCSQ050{

	public String CS_EMP_ID; // 점검사원
	public String WORK_DT; // 작업일자
	public String JOB_NO; // 작업번호
	public String ACT_NO;  //tcsq050의 row max Count로 하면 될것 같음
	public String WORK_CD; // 작업코드

	public String JOB_TM; // 점검시간(분)
	public String JOB_ACT; //
	public String JOB_ST; // 작업상태(BC:C0491)

	public String BLDG_NO; // 건물관리번호
	public String CAR_NO; // 호기
	public String REF_CONTR_NO; // 관련계약번호
	public String SUPPORT_CD; // 지원부서

	public String ENG_ST; // 기사상태(BC:C0490)


	public String LOCAL_COORD_X; // 미완료사유
	public String LOCAL_COORD_Y; // 계획년월
	public String JOB_COORD_X; // 계획NO
	public String JOB_COORD_Y; // 주차

	public String ADDR; // 요일
	public String DEVICE_NO; // 점검부서
	public String CANCEL_IF; // 점검시간대
	public String CANCEL_DT; // 최초등록자

	public String RMK; // 비고



	public TableTCSQ050(String cS_EMP_ID, String wORK_DT, String jOB_NO,
						String aCT_NO, String wORK_CD, String jOB_TM, String jOB_ACT,
						String jOB_ST, String bLDG_NO, String cAR_NO, String rEF_CONTR_NO,
						String sUPPORT_CD, String eNG_ST, String lOCAL_COORD_X,
						String lOCAL_COORD_Y, String jOB_COORD_X, String jOB_COORD_Y,
						String aDDR, String dEVICE_NO, String cANCEL_IF, String cANCEL_DT,
						String rMK) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		ACT_NO = aCT_NO;
		WORK_CD = wORK_CD;
		JOB_TM = jOB_TM;
		JOB_ACT = jOB_ACT;
		JOB_ST = jOB_ST;
		BLDG_NO = bLDG_NO;
		CAR_NO = cAR_NO;
		REF_CONTR_NO = rEF_CONTR_NO;
		SUPPORT_CD = sUPPORT_CD;
		ENG_ST = eNG_ST;
		LOCAL_COORD_X = lOCAL_COORD_X;
		LOCAL_COORD_Y = lOCAL_COORD_Y;
		JOB_COORD_X = jOB_COORD_X;
		JOB_COORD_Y = jOB_COORD_Y;
		ADDR = aDDR;
		DEVICE_NO = dEVICE_NO;
		CANCEL_IF = cANCEL_IF;
		CANCEL_DT = cANCEL_DT;
		RMK = rMK;
	}



	public TableTCSQ050() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getACT_NO() {
		return ACT_NO;
	}

	public void setACT_NO(String aCT_NO) {
		ACT_NO = aCT_NO;
	}

	public String getWORK_CD() {
		return WORK_CD;
	}

	public void setWORK_CD(String wORK_CD) {
		WORK_CD = wORK_CD;
	}

	public String getJOB_TM() {
		return JOB_TM;
	}

	public void setJOB_TM(String jOB_TM) {
		JOB_TM = jOB_TM;
	}

	public String getJOB_ACT() {
		return JOB_ACT;
	}

	public void setJOB_ACT(String jOB_ACT) {
		JOB_ACT = jOB_ACT;
	}

	public String getJOB_ST() {
		return JOB_ST;
	}

	public void setJOB_ST(String jOB_ST) {
		JOB_ST = jOB_ST;
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

	public String getENG_ST() {
		return ENG_ST;
	}

	public void setENG_ST(String eNG_ST) {
		ENG_ST = eNG_ST;
	}

	public String getLOCAL_COORD_X() {
		return LOCAL_COORD_X;
	}

	public void setLOCAL_COORD_X(String lOCAL_COORD_X) {
		LOCAL_COORD_X = lOCAL_COORD_X;
	}

	public String getLOCAL_COORD_Y() {
		return LOCAL_COORD_Y;
	}

	public void setLOCAL_COORD_Y(String lOCAL_COORD_Y) {
		LOCAL_COORD_Y = lOCAL_COORD_Y;
	}

	public String getJOB_COORD_X() {
		return JOB_COORD_X;
	}

	public void setJOB_COORD_X(String jOB_COORD_X) {
		JOB_COORD_X = jOB_COORD_X;
	}

	public String getJOB_COORD_Y() {
		return JOB_COORD_Y;
	}

	public void setJOB_COORD_Y(String jOB_COORD_Y) {
		JOB_COORD_Y = jOB_COORD_Y;
	}

	public String getADDR() {
		return ADDR;
	}

	public void setADDR(String aDDR) {
		ADDR = aDDR;
	}

	public String getDEVICE_NO() {
		return DEVICE_NO;
	}

	public void setDEVICE_NO(String dEVICE_NO) {
		DEVICE_NO = dEVICE_NO;
	}

	public String getCANCEL_IF() {
		return CANCEL_IF;
	}

	public void setCANCEL_IF(String cANCEL_IF) {
		CANCEL_IF = cANCEL_IF;
	}

	public String getCANCEL_DT() {
		return CANCEL_DT;
	}

	public void setCANCEL_DT(String cANCEL_DT) {
		CANCEL_DT = cANCEL_DT;
	}

	public String getRMK() {
		return RMK;
	}

	public void setRMK(String rMK) {
		RMK = rMK;
	}




}
