package com.jinsit.kmec.WO;

public class WO_WT00_R00_ITEM00 {

	public String CS_EMP_ID;
	public String WORK_DT;
	public String JOB_NO;
	private String WORK_CD;		//WORK_CD 추가 20190128 yowonsm
	public String WORK_NM;
	public String ST;
	public String CS_DT;
	public String CS_FR;
	public String BLDG_NM;
	public String CAR_NO;
	public String RESERV_ST;
	public String START_TM;
	public String ARRIVE_TM;
	public String END_TM;
	public String Y_CNT;
	public String T_CNT;
	

	public WO_WT00_R00_ITEM00(String cS_EMP_ID, String wORK_DT, String jOB_NO,
			String wORK_NM, String sT, String cS_DT, String cS_FR,
			String bLDG_NM, String cAR_NO, String rESERV_ST, String sTART_TM,
			String aRRIVE_TM, String eND_TM,String y_CNT, String t_CNT) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		WORK_NM = wORK_NM;
		ST = sT;
		CS_DT = cS_DT;
		CS_FR = cS_FR;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		RESERV_ST = rESERV_ST;
		START_TM = sTART_TM;
		ARRIVE_TM = aRRIVE_TM;
		END_TM = eND_TM;
		Y_CNT = y_CNT;
		T_CNT = t_CNT;
	}
	public WO_WT00_R00_ITEM00(String cS_EMP_ID, String wORK_DT, String jOB_NO, String workCd,
			String wORK_NM, String sT, String cS_DT, String cS_FR,
			String bLDG_NM, String cAR_NO, String rESERV_ST,String y_CNT,String t_CNT) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		WORK_CD = workCd;
		WORK_NM = wORK_NM;
		ST = sT;
		CS_DT = cS_DT;
		CS_FR = cS_FR;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		RESERV_ST = rESERV_ST;
		Y_CNT = y_CNT;
		T_CNT = t_CNT;
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
	public String getWORK_NM() {
		return WORK_NM;
	}
	public void setWORK_NM(String wORK_NM) {
		WORK_NM = wORK_NM;
	}
	public String getST() {
		return ST;
	}
	public void setST(String sT) {
		ST = sT;
	}
	public String getCS_DT() {
		return CS_DT;
	}
	public void setCS_DT(String cS_DT) {
		CS_DT = cS_DT;
	}
	public String getCS_FR() {
		return CS_FR;
	}
	public void setCS_FR(String cS_FR) {
		CS_FR = cS_FR;
	}
	public String getBLDG_NM() {
		return BLDG_NM;
	}
	public void setBLDG_NM(String bLDG_NM) {
		BLDG_NM = bLDG_NM;
	}
	public String getCAR_NO() {
		return CAR_NO;
	}
	public void setCAR_NO(String cAR_NO) {
		CAR_NO = cAR_NO;
	}
	public String getRESERV_ST() {
		return RESERV_ST;
	}
	public void setRESERV_ST(String rESERV_ST) {
		RESERV_ST = rESERV_ST;
	}
	public String getSTART_TM() {
		return START_TM;
	}
	public void setSTART_TM(String sTART_TM) {
		START_TM = sTART_TM;
	}
	public String getARRIVE_TM() {
		return ARRIVE_TM;
	}
	public void setARRIVE_TM(String aRRIVE_TM) {
		ARRIVE_TM = aRRIVE_TM;
	}
	public String getEND_TM() {
		return END_TM;
	}
	public void setEND_TM(String eND_TM) {
		END_TM = eND_TM;
	}
	
	
	public String getY_CNT() {
		return Y_CNT;
	}
	public void setY_CNT(String y_CNT) {
		Y_CNT = y_CNT;
	}
	public String getT_CNT() {
		return T_CNT;
	}
	public void setT_CNT(String t_CNT) {
		T_CNT = t_CNT;
	}

	public String getWORK_CD() {
		return WORK_CD;
	}

	public void setWORK_CD(String WORK_CD) {
		this.WORK_CD = WORK_CD;
	}
}
