package com.jinsit.kmec.comm.jinLib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
/**
 * @author  yowonsm
 */
public class NetworkStates {

	public static boolean isNetworkState = true;
	public static boolean isNetworkStatus(Context context){
		boolean isNetWork = true;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean isWifiConn = ni.isConnected();
		ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean isMobileConn = ni.isConnected();

		Log.e("isMobileConn", "isMobileConn  = " + isMobileConn);
		Log.d("isWifiConn", "isWifiConn  = " + isWifiConn);

		if (!isWifiConn && !isMobileConn) {
			//Toast.makeText(context,
			//"Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!",
			//Toast.LENGTH_SHORT).show();
			isNetWork = false;
		}
		return isNetWork;
	}


}
