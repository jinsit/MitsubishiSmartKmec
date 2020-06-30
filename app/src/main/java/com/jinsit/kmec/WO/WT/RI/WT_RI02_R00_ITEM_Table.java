package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

public class WT_RI02_R00_ITEM_Table {
	public String group;
	public ArrayList<WT_RI02_R00_ITEM00> child;

	
	public WT_RI02_R00_ITEM_Table(String groupName, ArrayList<WT_RI02_R00_ITEM00> childData) {
		group = groupName;
		child = childData;
	}

}