package com.jinsit.kmec.WO.WT.MW;
/**
 * @discription 정기검사입회 데이터
 * @author 원성민
 *
 */
public class WT_MW00_R02_ITEM00 {

	
	public String ROW_NUM;
	public String WORK_DT;
	public String INSP_ST;
	public String INSP_ST_NM;
	public String JOB_ST;
	public String JOB_ST_NM;
	public String DETAIL_RMK;
	
	
	public WT_MW00_R02_ITEM00() {
		super();
	}


	public WT_MW00_R02_ITEM00(String rOW_NUM, String wORK_DT,String iNSP_ST,String iNSP_ST_NM, String jOB_ST,
			String jOB_ST_NM, String dETAIL_RMK) {
		super();
		ROW_NUM = rOW_NUM;
		WORK_DT = wORK_DT;
		INSP_ST = iNSP_ST;
		INSP_ST_NM = iNSP_ST_NM;
		JOB_ST = jOB_ST;
		JOB_ST_NM = jOB_ST_NM;
		DETAIL_RMK = dETAIL_RMK;
	}


	public String getINSP_ST() {
		return INSP_ST;
	}


	public void setINSP_ST(String iNSP_ST) {
		INSP_ST = iNSP_ST;
	}


	public String getINSP_ST_NM() {
		return INSP_ST_NM;
	}


	public void setINSP_ST_NM(String iNSP_ST_NM) {
		INSP_ST_NM = iNSP_ST_NM;
	}


	public String getROW_NUM() {
		return ROW_NUM;
	}


	public void setROW_NUM(String rOW_NUM) {
		ROW_NUM = rOW_NUM;
	}


	public String getWORK_DT() {
		return WORK_DT;
	}


	public void setWORK_DT(String wORK_DT) {
		WORK_DT = wORK_DT;
	}


	public String getJOB_ST() {
		return JOB_ST;
	}


	public void setJOB_ST(String jOB_ST) {
		JOB_ST = jOB_ST;
	}


	public String getJOB_ST_NM() {
		return JOB_ST_NM;
	}


	public void setJOB_ST_NM(String jOB_ST_NM) {
		JOB_ST_NM = jOB_ST_NM;
	}


	public String getDETAIL_RMK() {
		return DETAIL_RMK;
	}


	public void setDETAIL_RMK(String dETAIL_RMK) {
		DETAIL_RMK = dETAIL_RMK;
	}
	
	
	
	
	
}
