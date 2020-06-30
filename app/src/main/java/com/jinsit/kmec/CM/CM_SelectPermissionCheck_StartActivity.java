package com.jinsit.kmec.CM;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;

import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;

public class CM_SelectPermissionCheck_StartActivity{

	public CM_SelectPermissionCheck_StartActivity(Context context){
		this.context = context;
	}
	private Context context;

	public void moveActivity(String result, Class className){
		if(result.equals("1")){
			//권한있음
			Intent intent = new Intent(context, className);
			context.startActivity(intent);
		}else if(result.equals("0")){
			//권한없음
			SimpleDialog sm01 = new SimpleDialog(context, "알림","접근권한이 없습니다.");
			sm01.show();
		}else{
			//네트워크에러
			SimpleDialog sm01 = new SimpleDialog(context, "알림","네트워크 연결 에러입니다. WIFI나 LTE를 확인하시고 다시 시도하십시오.");
			sm01.show();
		}
	}

	public String permissionCheck(String menuCd) {

		String result = "";
		if (NetworkStates.isNetworkStatus(context)) {
			CommonSession cs = new CommonSession(context);
			CM_SelectPermissionCheck_ReqestData reqData = new CM_SelectPermissionCheck_ReqestData();
			reqData.setUsrId(cs.getEmpId());
			reqData.setMenuCd(menuCd);
			// 던질 파라미터

			CM_SelectPermissionCheck permissionCheck = new CM_SelectPermissionCheck(context);
			AsyncTask<CM_SelectPermissionCheck_ReqestData, Process, String> permissionResult =
					permissionCheck.execute(reqData);
			try {
				result = permissionResult.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			result = "";
		}
		return result;

	}


}
