package com.jinsit.kmec.IR.NM;

public class NFCRegistrationData {

	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String DONG_CAR_NO;
	public String NFC_PLC;
	public String NFC_PLC_NM;
	public String GOOD_NM;
	public String NFC_TAG;
	
	public NFCRegistrationData(String bLDG_NO, String bLDG_NM, String cAR_NO,
			String dONG_CAR_NO, String nFC_PLC,
			String nFC_PLC_NM, String gOOD_NM, String nFC_TAG) {
		super();
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		DONG_CAR_NO = dONG_CAR_NO;
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = nFC_PLC_NM;
		GOOD_NM = gOOD_NM;
		NFC_TAG = nFC_TAG;
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

	public String getDONG_CAR_NO() {
		return DONG_CAR_NO;
	}

	public void setDONG_CAR_NO(String dONG_CAR_NO) {
		DONG_CAR_NO = dONG_CAR_NO;
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

	public String getGOOD_NM() {
		return GOOD_NM;
	}

	public void setGOOD_NM(String gOOD_NM) {
		GOOD_NM = gOOD_NM;
	}

	public String getNFC_TAG() {
		return NFC_TAG;
	}

	public void setNFC_TAG(String nFC_TAG) {
		NFC_TAG = nFC_TAG;
	}
	
}
