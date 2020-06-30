package com.jinsit.kmec.IR.PI;

import java.io.Serializable;

public class IR_PI01_R00_Item implements Serializable {	
	
	private static final long serialVersionUID = 2522479877643242259L;
	private String partsDt;
	private String partsNo;
	private String repSt;
	public IR_PI01_R00_Item(){};
	public IR_PI01_R00_Item(  String partsDt
							 , String partsNo
							 , String repSt) {
		super();
		this.partsDt = partsDt;
		this.partsNo  = partsNo;
		this.repSt = repSt;
	}
	
	public String getPartsDt() {
		return partsDt;
	}
	public void setPartsDt(String partsDt) {
		this.partsDt = partsDt;
	}
	public String getPartsNo() {
		return partsNo;
	}
	public void setPartsNo(String partsNo) {
		this.partsNo = partsNo;
	}
	public String getRepSt() {
		return repSt;
	}
	public void setRepSt(String repSt) {
		this.repSt = repSt;
	}
	
	@Override
	public String toString() {
		return "IR_PI01_R01P_ITEM [partsDt=" + partsDt + ", partsNo="
				+ partsNo + ", repSt=" + repSt + "]";
	}	
}
