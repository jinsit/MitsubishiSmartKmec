package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PartCheckItemGroupListMng {
	
	public PartCheckItemGroupListMng(List<PartCheckListDataMng> itemList)
	{
		this.itemList = itemList;
	}
	private List<PartCheckListDataMng> itemList;
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
