package com.jinsit.kmec.DM.DM;


public class DM_DM04_R00_ITEM01 {
	public String getYEAR_WEEK_DT() {
		return YEAR_WEEK_DT;
	}

	public void setYEAR_WEEK_DT(String YEAR_WEEK_DT) {
		this.YEAR_WEEK_DT = YEAR_WEEK_DT;
	}

	public String getYEAR_WEEK_NM() {
		return YEAR_WEEK_NM;
	}

	public void setYEAR_WEEK_NM(String YEAR_WEEK_NM) {
		this.YEAR_WEEK_NM = YEAR_WEEK_NM;
	}

	public String getCUR_DATE() {
		return CUR_DATE;
	}

	public void setCUR_DATE(String CUR_DATE) {
		this.CUR_DATE = CUR_DATE;
	}

	public String YEAR_WEEK_DT;
	public String YEAR_WEEK_NM;
	public String CUR_DATE;

	public DM_DM04_R00_ITEM01() {
		super();
	}


	public DM_DM04_R00_ITEM01(String YEAR_WEEK_DT, String YEAR_WEEK_NM, String CUR_DATE) {
		super();
		this.YEAR_WEEK_DT = YEAR_WEEK_DT;
		this.YEAR_WEEK_NM = YEAR_WEEK_NM;
		this.CUR_DATE = CUR_DATE;
	};

}
