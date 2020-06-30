package com.jinsit.kmec.DM.DM;


public class DM_DM02_R01_ITEM02 {

	public String OT_NO;
	public String OT_TM;
	public String TOTAL_OT_TM;
	public String BLDG_NM;
	public String REP_ST_NM;

	public String getOT_NO() {
		return OT_NO;
	}

	public void setOT_NO(String oT_NO) {
		OT_NO = oT_NO;
	}

	public String getOT_TM() {
		return OT_TM;
	}

	public void setOT_TM(String oT_TM) {
		OT_TM = oT_TM;
	}

	public String getTOTAL_OT_TM() {
		return TOTAL_OT_TM;
	}

	public void setTOTAL_OT_TM(String tOTAL_OT_TM) {
		TOTAL_OT_TM = tOTAL_OT_TM;
	}

	public String getBLDG_NM() {
		return BLDG_NM;
	}

	public void setBLDG_NM(String bLDG_NM) {
		BLDG_NM = bLDG_NM;
	}

	public String getREP_ST_NM() {
		return REP_ST_NM;
	}

	public void setREP_ST_NM(String rEP_ST_NM) {
		REP_ST_NM = rEP_ST_NM;
	}

	public DM_DM02_R01_ITEM02(String oT_NO, String oT_TM, String tOTAL_OT_TM,
			String bLDG_NM, String rEP_ST_NM) {
		super();
		OT_NO = oT_NO;
		OT_TM = oT_TM;
		TOTAL_OT_TM = tOTAL_OT_TM;
		BLDG_NM = bLDG_NM;
		REP_ST_NM = rEP_ST_NM;
	}

	public DM_DM02_R01_ITEM02() {
		super();
	}

}
