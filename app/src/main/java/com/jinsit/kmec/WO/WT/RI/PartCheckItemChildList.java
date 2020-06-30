package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import java.util.List;

public class PartCheckItemChildList {
	
	public PartCheckItemChildList(List<PartCheckListData> itemList)
	{
		this.itemList = itemList;
	}
	private List<PartCheckListData> itemList;
	
	
	public ArrayList<PartCheckListData> getMData(String nfcPlc)
	{
		ArrayList<PartCheckListData> mdataList = new ArrayList<PartCheckListData>();
		for(PartCheckListData item : itemList)
		{
			if(item.getNFC_PLC().equals(nfcPlc))
			{
				mdataList.add(item);
			}
		}
		return mdataList;
	}

}
