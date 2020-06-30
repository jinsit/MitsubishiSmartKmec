package com.jinsit.kmec.HM.MP;

import java.io.Serializable;

public class HM_MP00_R01_Item01 implements Serializable {

	//Field
	private String toolNm;
	private static final long serialVersionUID = -4346645799756667003L;

	//Constructor
	public HM_MP00_R01_Item01(String toolNm) {
		super();
		this.toolNm = toolNm;
	}
	
	//Getter&Setter
	public String getToolNm() {
		return toolNm;
	}
	public void setToolNm(String toolNm) {
		this.toolNm = toolNm;
	}
	
	@Override
	public String toString() {
		return "HM_MP00_R00_Item01 [tools=" + toolNm + "]";
	}
	
};
