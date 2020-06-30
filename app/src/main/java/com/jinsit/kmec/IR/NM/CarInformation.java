package com.jinsit.kmec.IR.NM;

public class CarInformation {
	public String DONG_CAR_NO;
	public String MNG_NO;
	public String REPL_CAR_IF;

	public String getDONG_CAR_NO() {
		return DONG_CAR_NO;
	}

	public void setDONG_CAR_NO(String dONG_CAR_NO) {
		DONG_CAR_NO = dONG_CAR_NO;
	}

	public String getMNG_NO() {
		return MNG_NO;
	}

	public void setMNG_NO(String mNG_NO) {
		MNG_NO = mNG_NO;
	}

	public String getREPL_CAR_IF() {
		return REPL_CAR_IF;
	}

	public void setREPL_CAR_IF(String rEPL_CAR_IF) {
		REPL_CAR_IF = rEPL_CAR_IF;
	}

	public CarInformation(String dONG_CAR_NO, String mNG_NO, String rEPL_CAR_IF) {
		super();
		DONG_CAR_NO = dONG_CAR_NO;
		MNG_NO = mNG_NO;
		REPL_CAR_IF = rEPL_CAR_IF;
	}

	public CarInformation() {
		super();
	}

}
