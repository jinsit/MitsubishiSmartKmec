package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

public class PartCheckTableItem {
	public String group;
	public ArrayList<PartCheckListData> child;

	
	public PartCheckTableItem(String groupName, ArrayList<PartCheckListData> childData) {
		group = groupName;
		child = childData;
	}

}