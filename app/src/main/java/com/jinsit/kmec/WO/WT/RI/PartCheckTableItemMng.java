package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

public class PartCheckTableItemMng {
	public String group;
	public ArrayList<PartCheckListDataMng> child;

	
	public PartCheckTableItemMng(String groupName, ArrayList<PartCheckListDataMng> childData) {
		group = groupName;
		child = childData;
	}

}