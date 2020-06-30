package com.jinsit.kmec.WO.WT.RI;
/**
 * WO_WT00_R04_Header
 * @discription 승관원 체크테이블 헤더 데이터
 * @author 원성민
 *
 */
public class WO_WT00_R04_Header {
	private String ELEVATOR_NO;
	private String SELCHK_USID;
	private String SEL_CHK_ST_DT;
	private String SEL_CHK_END_DT;
	private String TM;
	private String BS_BNM;
	private String CS_EMP_NM;
	private String CS_EMP_ID;
	private String WORK_DT;
	private String JOB_NO;
	private String PATICULS;

	public WO_WT00_R04_Header() {
	}

	public WO_WT00_R04_Header(String ELEVATOR_NO, String SELCHK_USID, String SEL_CHK_ST_DT, String SEL_CHK_END_DT, String TM, String BS_BNM,
							  String CS_EMP_NM, String CS_EMP_ID, String WORK_DT, String JOB_NO, String paticuls) {
		this.ELEVATOR_NO = ELEVATOR_NO;
		this.SELCHK_USID = SELCHK_USID;
		this.SEL_CHK_ST_DT = SEL_CHK_ST_DT;
		this.SEL_CHK_END_DT = SEL_CHK_END_DT;
		this.TM = TM;
		this.BS_BNM = BS_BNM;
		this.CS_EMP_NM = CS_EMP_NM;
		this.CS_EMP_ID = CS_EMP_ID;
		this.WORK_DT = WORK_DT;
		this.JOB_NO = JOB_NO;
		this.PATICULS = paticuls;
	}

	public String getELEVATOR_NO() {
		return ELEVATOR_NO;
	}

	public void setELEVATOR_NO(String ELEVATOR_NO) {
		this.ELEVATOR_NO = ELEVATOR_NO;
	}

	public String getSELCHK_USID() {
		return SELCHK_USID;
	}

	public void setSELCHK_USID(String SELCHK_USID) {
		this.SELCHK_USID = SELCHK_USID;
	}

	public String getSEL_CHK_ST_DT() {
		return SEL_CHK_ST_DT;
	}

	public void setSEL_CHK_ST_DT(String SEL_CHK_ST_DT) {
		this.SEL_CHK_ST_DT = SEL_CHK_ST_DT;
	}

	public String getSEL_CHK_END_DT() {
		return SEL_CHK_END_DT;
	}

	public void setSEL_CHK_END_DT(String SEL_CHK_END_DT) {
		this.SEL_CHK_END_DT = SEL_CHK_END_DT;
	}

	public String getTM() {
		return TM;
	}

	public void setTM(String TM) {
		this.TM = TM;
	}

	public String getBS_BNM() {
		return BS_BNM;
	}

	public void setBS_BNM(String BS_BNM) {
		this.BS_BNM = BS_BNM;
	}

	public String getCS_EMP_NM() {
		return CS_EMP_NM;
	}

	public void setCS_EMP_NM(String CS_EMP_NM) {
		this.CS_EMP_NM = CS_EMP_NM;
	}

	public String getCS_EMP_ID() {
		return CS_EMP_ID;
	}

	public void setCS_EMP_ID(String CS_EMP_ID) {
		this.CS_EMP_ID = CS_EMP_ID;
	}

	public String getWORK_DT() {
		return WORK_DT;
	}

	public void setWORK_DT(String WORK_DT) {
		this.WORK_DT = WORK_DT;
	}

	public String getJOB_NO() {
		return JOB_NO;
	}

	public void setJOB_NO(String JOB_NO) {
		this.JOB_NO = JOB_NO;
	}

	public String getPATICULS() {
		return PATICULS;
	}

	public void setPATICULS(String PATICULS) {
		this.PATICULS = PATICULS;
	}
}
