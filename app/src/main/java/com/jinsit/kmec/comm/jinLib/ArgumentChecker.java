package com.jinsit.kmec.comm.jinLib;

import java.util.List;

public class ArgumentChecker {

	
	public boolean isEmpty(String str){
		
		Boolean result = false;
		if(str.isEmpty()){
			result = false;
		}else{
			result = true;
		}
		return result;
	}
	
	
	public boolean areEmpties(List list){
		
		Boolean result = false;
		int	listSize = list.size();
		
		for(int i=0;i<listSize;i++){
			
			String param = list.get(i).toString();
			
			if(param.length() == 0){
				result = false;
				continue;
			}
			result = true;
		}
		return result;
	}
	
//	argList.add(presentPwd);
//	argList.add(newPwd);
//	argList.add(newPwdAgain);
//	//argChecker.areEmpties(argList);
//	
//	if( argChecker.areEmpties(argList) )
	
	
	public boolean checkEmptyAlert(String str1, String str2){
		
		Boolean result = false;
		
		return result;
	}
	
	
	
//	public boolean isEmpty(){
//		
//		
//		
//		return false;
//	}
//	
//	public boolean areEmpty(ArrayList arr){
//		
//		System.out.println(arr.size());
//		
//		String result = "";
//		if(arr.size() > 0){
//			
//			int size = arr.size();
//			for(int i=0; i < size; i++){
//				
//				if( ( arr.get(i).toString().length() ) == 0 ){
//					result = "false";
//					continue;
//				}
//				
//				result = "true";
//				
//			}
//			
//			
//		}
//		
//		
//		return false;
//	}
//	
};
