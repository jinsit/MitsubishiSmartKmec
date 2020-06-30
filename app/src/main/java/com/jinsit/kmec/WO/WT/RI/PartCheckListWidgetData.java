package com.jinsit.kmec.WO.WT.RI;

import com.jinsit.kmec.R;

public class PartCheckListWidgetData {
	public String numbericText ="";
	public int radioABC =R.id.rd_wt_a;
	public int radioOX=R.id.rd_wt_o;
	public boolean checkBox=false;
	public boolean isUnChecked =false;
	public String csItemCd = "";
	public String inputTp ="";
	public String inputTp1 ="";
	public String inputTp3 ="";
	public String inputTp7 ="";
	public String inputRmk ="";
	public String overMonth ="N";
	public String monthChkIf ="";
	public String smartDesc ="";

	
	public String getSmartDesc() {
		return smartDesc;
	}

	public void setSmartDesc(String smartDesc) {
		this.smartDesc = smartDesc;
	}

	public String getMonthChkIf() {
		return monthChkIf;
	}

	public void setMonthChkIf(String monthChkIf) {
		this.monthChkIf = monthChkIf;
	}

	public boolean isUnChecked() {
		return isUnChecked;
	}

	public void setUnChecked(boolean isUnChecked) {
		this.isUnChecked = isUnChecked;
	}

	public String getCsItemCd() {
		return csItemCd;
	}

	public void setCsItemCd(String csItemCd) {
		this.csItemCd = csItemCd;
	}

	public String getInputTp() {
		return inputTp;
	}

	public void setInputTp(String inputTp) {
		this.inputTp = inputTp;
	}

	public String getInputRmk() {
		return inputRmk;
	}

	public void setInputRmk(String inputRmk) {
		this.inputRmk = inputRmk;
	}

	public String getOverMonth() {
		return overMonth;
	}

	public void setOverMonth(String overMonth) {
		this.overMonth = overMonth;
	}

	public String getInputTp1() {
		return inputTp1;
	}

	public void setInputTp1(String inputTp1) {
		this.inputTp1 = inputTp1;
	}

	public String getInputTp3() {
		return inputTp3;
	}

	public void setInputTp3(String inputTp3) {
		this.inputTp3 = inputTp3;
	}

	public String getInputTp7() {
		return inputTp7;
	}

	public void setInputTp7(String inputTp7) {
		this.inputTp7 = inputTp7;
	}

	public String getNumbericText() {
		return numbericText;
	}

	public void setNumbericText(String numbericText) {
		this.numbericText = numbericText;
	}

	public int getRadioABC() {
		radioABC = R.id.rd_wt_a;
		if(inputTp1.equals("A")){
			radioABC = R.id.rd_wt_a;
		}else if(inputTp1.equals("B")){
			radioABC = R.id.rd_wt_b;
		}else{
			radioABC = R.id.rd_wt_c;
		}
		return radioABC;
	}

	public void setRadioABC(int radioABC) {
		this.radioABC = radioABC;
	}

	public int getRadioOX() {
		radioOX = R.id.rd_wt_o;
		if(inputTp7.equals("1")){
			radioOX = R.id.rd_wt_o;
		}else if(inputTp7.equals("0")){
			radioOX = R.id.rd_wt_o;
		}
		return radioOX;
	}

	public void setRadioOX(int radioOX) {
		this.radioOX = radioOX;
	}

	public boolean isCheckBox() {
		return checkBox;
	}

	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}

}
