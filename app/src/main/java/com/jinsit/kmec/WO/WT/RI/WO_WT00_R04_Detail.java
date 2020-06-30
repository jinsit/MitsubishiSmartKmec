package com.jinsit.kmec.WO.WT.RI;
/**
 * WO_WT00_R04_Header
 * @discription 승관원 체크테이블 헤더 데이터
 * @author 원성민
 *
 */
public class WO_WT00_R04_Detail {
	private String CS_EMP_ID;
	private String WORK_DT;
	private String JOB_NO;
	private String SEL_CHK_ITEM_CD;
	private String PRE_CHK_RESULT;
	private String SEL_CHK_RESULT;
	private String SEL_CHK_RESULT_NM;
	private String REMARK;
	private String SEL_CHK_ITEM_NM;
	private String PRE_YM;
	private String HEADER_IF;

	public WO_WT00_R04_Detail() {
	}

	public WO_WT00_R04_Detail(String CS_EMP_ID, String WORK_DT, String JOB_NO, String SEL_CHK_ITEM_CD, String PRE_CHK_RESULT, String SEL_CHK_RESULT, String REMARK, String selChkItemNm, String selCheckResultNm,
							  String preYm, String headerIf) {
		this.CS_EMP_ID = CS_EMP_ID;
		this.WORK_DT = WORK_DT;
		this.JOB_NO = JOB_NO;
		this.SEL_CHK_ITEM_CD = SEL_CHK_ITEM_CD;
		this.PRE_CHK_RESULT = PRE_CHK_RESULT;
		this.SEL_CHK_RESULT = SEL_CHK_RESULT;
		this.REMARK = REMARK;
		this.SEL_CHK_ITEM_NM = selChkItemNm;
		this.SEL_CHK_RESULT_NM = selCheckResultNm;
		this.PRE_YM = preYm;
		this.HEADER_IF = headerIf;
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

	public String getSEL_CHK_ITEM_CD() {
		return SEL_CHK_ITEM_CD;
	}

	public void setSEL_CHK_ITEM_CD(String SEL_CHK_ITEM_CD) {
		this.SEL_CHK_ITEM_CD = SEL_CHK_ITEM_CD;
	}

	public String getPRE_CHK_RESULT() {
		return PRE_CHK_RESULT;
	}

	public void setPRE_CHK_RESULT(String PRE_CHK_RESULT) {
		this.PRE_CHK_RESULT = PRE_CHK_RESULT;
	}

	public String getSEL_CHK_RESULT() {
		return SEL_CHK_RESULT;
	}

	public void setSEL_CHK_RESULT(String SEL_CHK_RESULT) {
		this.SEL_CHK_RESULT = SEL_CHK_RESULT;
	}

	public String getREMARK() {
		return REMARK;
	}

	public void setREMARK(String REMARK) {
		this.REMARK = REMARK;
	}

	public String getSEL_CHK_ITEM_NM() {
		return SEL_CHK_ITEM_NM;
	}

	public void setSEL_CHK_ITEM_NM(String SEL_CHK_ITEM_NM) {
		this.SEL_CHK_ITEM_NM = SEL_CHK_ITEM_NM;
	}

	public String getSEL_CHK_RESULT_NM() {
		return SEL_CHK_RESULT_NM;
	}

	public void setSEL_CHK_RESULT_NM(String SEL_CHK_RESULT_NM) {
		this.SEL_CHK_RESULT_NM = SEL_CHK_RESULT_NM;
	}

	public String getPRE_YM() {
		return PRE_YM;
	}

	public void setPRE_YM(String PRE_YM) {
		this.PRE_YM = PRE_YM;
	}

	public String getHEADER_IF() {
		return HEADER_IF;
	}

	public void setHEADER_IF(String HEADER_IF) {
		this.HEADER_IF = HEADER_IF;
	}
}

