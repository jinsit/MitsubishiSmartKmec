package com.jinsit.kmec.WO.WT.RI;

public class PartCheckListData {
	public String CS_EMP_ID;
	public String WORK_DT;
	public String JOB_NO;
	public String NFC_PLC;
	public String CS_ITEM_CD;
	public String CS_LOW_NM;//
	public String SMART_DESC;
	public String MNG_DESC;//
	public String CS_TOOLS;
	public String STD_ST;
	public String INPUT_TP;
	public String INPUT_TP1;
	public String INPUT_TP3;
	public String INPUT_TP7;
	public String INPUT_RMK;
	public String OVER_MONTH;
	public String MONTH_CHK_IF;
	public String MONTH_CHK;
	
	
	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String CAR_NO_TO;
	public String NFC_PLC_NM;
	private String HEADER_IF;

	public String getHEADER_IF() {
		return HEADER_IF;
	}

	public void setHEADER_IF(String HEADER_IF) {
		this.HEADER_IF = HEADER_IF;
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
	public String getCAR_NO_TO() {
		return CAR_NO_TO;
	}
	public void setCAR_NO_TO(String cAR_NO_TO) {
		CAR_NO_TO = cAR_NO_TO;
	}
	public String getNFC_PLC_NM() {
		return NFC_PLC_NM;
	}
	public void setNFC_PLC_NM(String nFC_PLC_NM) {
		NFC_PLC_NM = nFC_PLC_NM;
	}


	public PartCheckListData(String cS_EMP_ID, String wORK_DT, String jOB_NO,
							 String nFC_PLC, String cS_ITEM_CD, String sMART_DESC,
							 String cS_TOOLS, String sTD_ST, String iNPUT_TP, String iNPUT_TP1,
							 String iNPUT_TP3, String iNPUT_TP7, String iNPUT_RMK,
							 String oVER_MONTH, String mONTH_CHK_IF, String mONTH_CHK,
							 String bLDG_NO, String bLDG_NM, String cAR_NO, String cAR_NO_TO,
							 String nFC_PLC_NM, String headerIf) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		CS_ITEM_CD = cS_ITEM_CD;
		SMART_DESC = sMART_DESC;
		CS_TOOLS = cS_TOOLS;
		STD_ST = sTD_ST;
		INPUT_TP = iNPUT_TP;
		INPUT_TP1 = iNPUT_TP1;
		INPUT_TP3 = iNPUT_TP3;
		INPUT_TP7 = iNPUT_TP7;
		INPUT_RMK = iNPUT_RMK;
		OVER_MONTH = oVER_MONTH;
		MONTH_CHK_IF = mONTH_CHK_IF;
		MONTH_CHK = mONTH_CHK;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		CAR_NO_TO = cAR_NO_TO;
		NFC_PLC_NM = nFC_PLC_NM;
		HEADER_IF = headerIf;
	}

	public PartCheckListData(String cS_EMP_ID, String wORK_DT, String jOB_NO,
			String nFC_PLC, String cS_ITEM_CD, String sMART_DESC,
			String cS_TOOLS, String sTD_ST, String iNPUT_TP, String iNPUT_TP1,
			String iNPUT_TP3, String iNPUT_TP7, String iNPUT_RMK,
			String oVER_MONTH, String mONTH_CHK_IF, String mONTH_CHK,
			String bLDG_NO, String bLDG_NM, String cAR_NO, String cAR_NO_TO,
			String nFC_PLC_NM) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		CS_ITEM_CD = cS_ITEM_CD;
		SMART_DESC = sMART_DESC;
		CS_TOOLS = cS_TOOLS;
		STD_ST = sTD_ST;
		INPUT_TP = iNPUT_TP;
		INPUT_TP1 = iNPUT_TP1;
		INPUT_TP3 = iNPUT_TP3;
		INPUT_TP7 = iNPUT_TP7;
		INPUT_RMK = iNPUT_RMK;
		OVER_MONTH = oVER_MONTH;
		MONTH_CHK_IF = mONTH_CHK_IF;
		MONTH_CHK = mONTH_CHK;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		CAR_NO_TO = cAR_NO_TO;
		NFC_PLC_NM = nFC_PLC_NM;
	}
	public PartCheckListData() {
		super();
	}
	public PartCheckListData(String cS_EMP_ID, String wORK_DT, String jOB_NO,
			String nFC_PLC, String cS_ITEM_CD, String cS_LOW_NM,
			String sMART_DESC, String mNG_DESC, String cS_TOOLS, String sTD_ST,
			String iNPUT_TP, String iNPUT_TP1, String iNPUT_TP3,
			String iNPUT_TP7, String iNPUT_RMK, String oVER_MONTH,
			String mONTH_CHK_IF, String mONTH_CHK) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		NFC_PLC = nFC_PLC;
		CS_ITEM_CD = cS_ITEM_CD;
		CS_LOW_NM = cS_LOW_NM;
		SMART_DESC = sMART_DESC;
		MNG_DESC = mNG_DESC;
		CS_TOOLS = cS_TOOLS;
		STD_ST = sTD_ST;
		INPUT_TP = iNPUT_TP;
		INPUT_TP1 = iNPUT_TP1;
		INPUT_TP3 = iNPUT_TP3;
		INPUT_TP7 = iNPUT_TP7;
		INPUT_RMK = iNPUT_RMK;
		OVER_MONTH = oVER_MONTH;
		MONTH_CHK_IF = mONTH_CHK_IF;
		MONTH_CHK = mONTH_CHK;
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
	public String getMONTH_CHK_IF() {
		return MONTH_CHK_IF;
	}
	public void setMONTH_CHK_IF(String mONTH_CHK_IF) {
		MONTH_CHK_IF = mONTH_CHK_IF;
	}
	public String getMONTH_CHK() {
		return MONTH_CHK;
	}
	public void setMONTH_CHK(String mONTH_CHK) {
		MONTH_CHK = mONTH_CHK;
	}
	
}
