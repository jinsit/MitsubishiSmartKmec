package com.jinsit.kmec.WO.WT.RI;

/**
 * WT_RI00_R01_ITEM02
 * 
 * @discription NFC TagId select
 * @author 원성민
 *
 */
public class WT_RI00_R01_ITEM02 {
	public String CS_EMP_ID = "";
	public String WORK_DT = "";
	public String JOB_NO = "";
	public String NFC_PLC = "";
	public String NFC_PLC_NM = "";
	public String BLDG_NO = "";
	public String BLDG_NM = "";
	public String CAR_NO = "";

	public WT_RI00_R01_ITEM02() {
		super();
	}
	
	

	public WT_RI00_R01_ITEM02(String cS_EMP_ID, String wORK_DT, String jOB_NO,
			String nFC_PLC, String nFC_PLC_NM, String bLDG_NO, String bLDG_NM,
			String cAR_NO) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = nFC_PLC_NM;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
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

	public String getNFC_PLC_NM() {
		return NFC_PLC_NM;
	}

	public void setNFC_PLC_NM(String nFC_PLC_NM) {
		NFC_PLC_NM = nFC_PLC_NM;
	}

	public String getBLDG_NO() {
		return BLDG_NO;
	}

	public void setBLDG_NO(String bLDG_NO) {
		BLDG_NO = bLDG_NO;
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

}
