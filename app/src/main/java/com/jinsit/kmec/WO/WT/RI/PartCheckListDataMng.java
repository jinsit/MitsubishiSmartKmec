package com.jinsit.kmec.WO.WT.RI;

public class PartCheckListDataMng extends CopyPartCheckListDataMng{
	public String CS_EMP_ID;
	public String WORK_DT;
	public String JOB_NO;
	
	public String BLDG_NO;
	public String BLDG_NM;
	public String CAR_NO;
	public String CAR_NO_TO;
	public String NFC_PLC;
	public String NFC_PLC_NM;
	
	public String CS_ITEM_CD;
	
	public String EL_INFO_MAP;////
	public String MNG_DESC;///
	public String INPUT_RMK;
	public String DEF_VAL_ST;
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
	public String getCS_ITEM_CD() {
		return CS_ITEM_CD;
	}
	public void setCS_ITEM_CD(String cS_ITEM_CD) {
		CS_ITEM_CD = cS_ITEM_CD;
	}
	public String getEL_INFO_MAP() {
		return EL_INFO_MAP;
	}
	public void setEL_INFO_MAP(String eL_INFO_MAP) {
		EL_INFO_MAP = eL_INFO_MAP;
	}
	public String getMNG_DESC() {
		return MNG_DESC;
	}
	public void setMNG_DESC(String mNG_DESC) {
		MNG_DESC = mNG_DESC;
	}
	public String getINPUT_RMK() {
		return INPUT_RMK;
	}
	public void setINPUT_RMK(String iNPUT_RMK) {
		INPUT_RMK = iNPUT_RMK;
	}
	public String getDEF_VAL_ST() {
		return DEF_VAL_ST;
	}
	public void setDEF_VAL_ST(String dEF_VAL_ST) {
		DEF_VAL_ST = dEF_VAL_ST;
	}
	public PartCheckListDataMng(String cS_EMP_ID, String wORK_DT,
			String jOB_NO, String bLDG_NO, String bLDG_NM, String cAR_NO,
			String cAR_NO_TO, String nFC_PLC, String nFC_PLC_NM,
			String cS_ITEM_CD, String eL_INFO_MAP, String mNG_DESC,
			String iNPUT_RMK, String dEF_VAL_ST) {
		super();
		CS_EMP_ID = cS_EMP_ID;
		WORK_DT = wORK_DT;
		JOB_NO = jOB_NO;
		BLDG_NO = bLDG_NO;
		BLDG_NM = bLDG_NM;
		CAR_NO = cAR_NO;
		CAR_NO_TO = cAR_NO_TO;
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = nFC_PLC_NM;
		CS_ITEM_CD = cS_ITEM_CD;
		EL_INFO_MAP = eL_INFO_MAP;
		MNG_DESC = mNG_DESC;
		INPUT_RMK = iNPUT_RMK;
		DEF_VAL_ST = dEF_VAL_ST;
	}
	public PartCheckListDataMng() {
		super();
	}
	public PartCheckListDataMng(String nFC_PLC,String plcNm) {
		super();
		NFC_PLC = nFC_PLC;
		NFC_PLC_NM = plcNm;
	}
	@Override
	public void appendRmk(String rmk) {
		// TODO Auto-generated method stub
		super.appendRmk(rmk);
		this.INPUT_RMK = super.getINPUT_RMK();
		
	}
	
	
//	public void appendRmk(String rmk){
//		this.INPUT_RMK = this.INPUT_RMK +", " + rmk;
//	}
//	
//	

	
	
	
}
