package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PartCheckItemChildListMng {
	
	public PartCheckItemChildListMng(List<PartCheckListDataMng> itemList)
	{
		this.itemList = itemList;
	}
	private List<PartCheckListDataMng> itemList;
	
	public ArrayList<CopyPartCheckListDataMng> getChild()
	{
		
		ArrayList<CopyPartCheckListDataMng> groupList = new ArrayList<CopyPartCheckListDataMng>();
		for(int i=0;i<itemList.size();i++){
			if(i==0){
				groupList.add(itemList.get(i));
			}else{
				if(itemList.get(i).getEL_INFO_MAP().equals(itemList.get(i-1).getEL_INFO_MAP())){
					
					//groupList.add(new PartCheckListDataMng(itemList.groupList.set(i, itemList.get(i-1).)
				}else{
					groupList.add(itemList.get(i));	
				}
				
			}
					

		}
		
//		HashSet hs = new HashSet(groupList);
//		ArrayList<PartCheckListDataMng> hashGroupList = new ArrayList<PartCheckListDataMng>(hs);

		
		
		
		return groupList;
	}
	public ArrayList<PartCheckListDataMng> getChildList(String el)
	{
		ArrayList<PartCheckListDataMng> mdataList = new ArrayList<PartCheckListDataMng>();
		for(PartCheckListDataMng item : itemList)
		{
			if(item.getEL_INFO_MAP().equals(el))
			{
				mdataList.add(item);
			}
		}
		

		HashSet hs = new HashSet(mdataList);
		ArrayList<PartCheckListDataMng> hashGroupList = new ArrayList<PartCheckListDataMng>(hs);

		//return hashGroupList;
		return hashGroupList;
	}

}
