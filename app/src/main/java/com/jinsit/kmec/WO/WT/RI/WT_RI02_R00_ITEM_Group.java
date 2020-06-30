package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WT_RI02_R00_ITEM_Group {
	
	public WT_RI02_R00_ITEM_Group(List<WT_RI02_R00_ITEM00> itemList)
	{
		this.itemList = itemList;
	}
	private List<WT_RI02_R00_ITEM00> itemList;
	public ArrayList<String> getGroupList()
	{
		
		ArrayList<String> groupList = new ArrayList<String>();
		for(int i=0;i<itemList.size();i++){
			groupList.add(itemList.get(i).getNFC_PLC_NM());

		}
		
		HashSet hs = new HashSet(groupList);
		ArrayList<String> hashGroupList = new ArrayList<String>(hs);

		return hashGroupList;
	}
	
	
}
