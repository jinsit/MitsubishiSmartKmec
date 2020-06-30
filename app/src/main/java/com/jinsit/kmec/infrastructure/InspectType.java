package com.jinsit.kmec.infrastructure;

public enum InspectType {
	Start(1) ,	//점검시작: 1
	End(2),	//점검종료: 2
	Pause(3);   //작업보류: 3


	private int value;
	private InspectType(int value){
		this.value = value;
	}

	public int getValue(){
		return this.value;
	}

}
