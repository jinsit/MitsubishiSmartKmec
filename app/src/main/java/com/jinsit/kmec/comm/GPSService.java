package com.jinsit.kmec.comm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.jinsit.kmec.comm.jinLib.ActivityManagerUtil;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.DialogActivity;
import com.jinsit.kmec.comm.jinLib.LocationGeocoder;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class GPSService extends Service implements LocationListener {

	private Context mContext = null;

	// 현재 GPS 사용유무
	boolean isGPSEnabled = false;

	// 네트워크 사용유무
	boolean isNetworkEnabled = false;

	// GPS 상태값
	boolean isGetLocation = false;

	Location location;
	double lat; // 위도
	double lon; // 경도

	// 최소 GPS 정보 업데이트 거리 10미터
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5000;

	// 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
	private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1;

	protected LocationManager locationManager;
	private String deviceTp = "1";
	private CommonSession commonSession;
	public GPSService(Context context) {
		this.mContext = context;
		this.location = getLocation();

	}


	public GPSService() {
		super();
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// mContext = this;
		// registerAlarm();
		Log.e("onCreate", "onCreate = GPSService");
        this.commonSession = new CommonSession(this);
	}


	private void turnOnGps(){
		Intent intent = new Intent(this.mContext, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("ACTION.GPSService");
		this.mContext.startActivity(intent);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.w("onStartCommand", "onStartCommand = GPSService");
		this.mContext = this;
		this.location = getLocation();
		if (location == null) {
			Log.v("location==null", "location==null = GPSService");
			Log.e("DailogActivity", "getPackageName =" + ActivityManagerUtil.getPackageName(mContext));
			Log.e("DailogActivity", "getClassName =" + ActivityManagerUtil.getClassName(mContext));
			if(ActivityManagerUtil.getPackageName(mContext).equals("com.jinsit.kmec")){
				this.turnOnGps();
				Log.i("onStartCommand", "GPSService  turn on ");
			}else{
				Log.i("onStartCommand", "GPSService not turn on ");
			}

		} else {
			Log.v("location!=null", "location!=null = GPSService");
			new UpdateEngineerLocationAsync().execute();
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public Location getLocation() {
		Location location = null;
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// GPS 정보 가져오기
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// 현재 네트워크 상태 값 알아오기
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			Log.i("gpsService", "isGps = " + isGPSEnabled +  " isNetwork = " + isNetworkEnabled);
			if (!isGPSEnabled && !isNetworkEnabled) {
				// GPS 와 네트워크사용이 가능하지 않을때 소스 구현

			} else {
				this.isGetLocation = true;
				// 네트워크 정보로 부터 위치값 가져오기
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							// 위도 경도 저장
							lat = location.getLatitude();
							lon = location.getLongitude();
						}
					}
					Log.w("gpsService",  " in isNetwork = " + isNetworkEnabled);
				}

				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lon = location.getLongitude();
							}
						}
					}
					Log.e("gpsService",  " in isGPS = " + isGPSEnabled);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	/**
	 * GPS 종료
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSService.this);
		}
	}

	/**
	 * 위도값을 가져옵니다.
	 * */
	public double getLatitude() {
		if (location != null) {
			lat = location.getLatitude();
		}
		return lat;
	}

	/**
	 * 경도값을 가져옵니다.
	 * */
	public double getLongitude() {
		if (location != null) {
			lon = location.getLongitude();
		}
		return lon;
	}

	/**
	 * GPS 나 wife 정보가 켜져있는지 확인합니다.
	 * */
	public boolean isGetLocation() {
		return this.isGetLocation;
	}

	/**
	 * GPS 정보를 가져오지 못했을때 설정값으로 갈지 물어보는 alert 창
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		alertDialog.setTitle("GPS 사용유무셋팅");
		alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");

		// OK 를 누르게 되면 설정창으로 이동합니다.
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});
		// Cancle 하면 종료 합니다.
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	private class UpdateEngineerLocationAsync extends
			AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "comm/updateEngineerPlaceWithEmpId.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("collectDtS", DateUtil
						.nowDateFormat("yyyyMMddHHmmss")));
				arguments.add(new BasicNameValuePair("deviceTp", getDeviceType()));
				Log.w("deviceTp","getDepviceTp = " +deviceTp);
				arguments.add(new BasicNameValuePair("localCoordX", String
						.valueOf(location.getLatitude())));

				Log.w("deviceTp","localCoordX = " +String
						.valueOf(location.getLatitude()));
				arguments.add(new BasicNameValuePair("localCoordY", String
						.valueOf(location.getLongitude())));
				Log.w("deviceTp","localCoordY = " +String
						.valueOf(location.getLongitude()));
				arguments.add(new BasicNameValuePair("addr", getAddr()));
				arguments.add(new BasicNameValuePair("udId",new DeviceUniqNumber(mContext).getUdid()));

				arguments.add(new BasicNameValuePair("deviceSt", "00"));
				arguments.add(new BasicNameValuePair("setlliteCnt", String.valueOf(getGpsSatelliteCount())));
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					retMsg = returnJson.getString("dataString");
					Log.v("retMsg", "retMsg = " + retMsg);
				} catch (JSONException e) {
					e.printStackTrace();
					Log.v("JSONException!=", "JSONException!= = e" + e);
				}
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				Log.v("JSONException!=", "JSONException!= = e" + ex);
			}

			return null;
		}

	}
	/**
	 * 주소 획득
	 * @return
	 */
	private String getAddr(){
		String addr="";
		LocationGeocoder lGeo = new LocationGeocoder(mContext);
		addr  = lGeo.getSearchAddress(location.getLatitude(), location.getLongitude());
		Log.w("addr","addr = " +addr);
		return 	addr;

	}
	/**
	 * gps정보인지 network정보인지 획득
	 * @return
	 */
	private String getDeviceType(){
		if(isGPSEnabled&&isNetworkEnabled){
			deviceTp = "1";
		}else if(isGPSEnabled){
			deviceTp = "1";
		}else if(isNetworkEnabled){
			deviceTp ="2";
		}

		Log.w("getDeviceType","getDeviceType = " +deviceTp);
		return deviceTp;

	}


	/**
	 * 위성갯수 얻기
	 * @return
	 */
	private int getGpsSatelliteCount()
	{
		final GpsStatus gs = locationManager.getGpsStatus(null);
		int j = 0;
		int i = 0;
		final Iterator< GpsSatellite > it = gs.getSatellites().iterator();

		while(it.hasNext()) {
			GpsSatellite satellite = it.next();
			// 단순 위성 갯수가 아니라 사용할 수 있게 잡히는 위성의 갯수가 중요하다.
			if (satellite.usedInFix()) {
				j++; // i 값 보다는 이 값이 GPS 위성 사용 여부를 확인하는데 더 중요하다.
			}
			i++;
		}
		Log.w("getGpsSatelliteCount","getGpsSatelliteCount =j " +j +"getGpsSatelliteCount =i " +i );
		return j;
	}
}
