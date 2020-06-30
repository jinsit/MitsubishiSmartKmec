package com.jinsit.kmec.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.TableTCSQ050;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 이 서비스는 로칼 디비 TCSQ050에 있는것을 체크하여 있으면 서버로 날려준다~
 * @author 원성민
 *
 */
public class RealTimeJobHistoryService extends Service{

	private Context context;
	private CommonSession commonSession;

	private Handler realTimeHandler = new Handler();
	private int realTimeTimerCount = 1000 * 10 * 1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("RealTimeJobHistoryService", "onCreate = RealTimeJobHistoryService");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		Log.w("RealTimeJobHistoryService", "onStartCommand = RealTimeJobHistoryService");
		this.context = this;
		this.commonSession = new CommonSession(context);
		//new RealTimeJobHistoryAsync().execute();
		this.realTimeHandler.postDelayed(this.realTimeRunnable, this.realTimeTimerCount);
		return START_STICKY; // 강제로그아웃 유무는 항상 체크해야 하기 때문에 스티키로 한다. "좀비써비스"
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	private Runnable realTimeRunnable = new Runnable() {
		public void run() {

			DatabaseRawQuery databaseRawQuery = new DatabaseRawQuery();
			int count = databaseRawQuery.selectTCSQ050rawCount(context);
			Log.i("RealTimeJobHistoryService", "realTimeRunnable = count = " + count);
			if(count > 0){
				new RealTimeJobHistoryAsync().execute();
				realTimeHandler.postDelayed(realTimeRunnable,realTimeTimerCount);
				Log.i("RealTimeJobHistoryService", "realTimeRunnable = count = " + count);

			}else{
				stopSelf();
			}

		}
	};
	/**
	 * 정기점검 로칼DB TCSQ050 업데이트(서버로)
	 * @author 원성민
	 */
	public class RealTimeJobHistoryAsync extends AsyncTask<Void, Void, Boolean> {
		private TableTCSQ050 tcsq050 = null;
		private EasyJsonMap msgMap;
		private EasyJsonMap dataMap;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.e("RealTimeJobHistoryService", "doInBackground = RealTimeJobHistoryService");
			commonSession = new CommonSession(context);

			try{

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/insertTCSQ050.do";
				tcsq050 = getTCSQ050();
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", tcsq050.getCS_EMP_ID()));
				arguments.add(new BasicNameValuePair("workDt", tcsq050.getWORK_DT()));
				arguments.add(new BasicNameValuePair("actNo", "0"));//서버로 날릴때 actNo는 0으로  체번알아서 하게
				arguments.add(new BasicNameValuePair("jobNo", tcsq050.getJOB_NO()));
				arguments.add(new BasicNameValuePair("workCd", tcsq050.getWORK_CD()));
				arguments.add(new BasicNameValuePair("jobTm", tcsq050.getJOB_TM()));
				arguments.add(new BasicNameValuePair("jobAct", tcsq050.getJOB_ACT()));
				arguments.add(new BasicNameValuePair("jobSt", tcsq050.getJOB_ST()));
				arguments.add(new BasicNameValuePair("bldgNo", tcsq050.getBLDG_NO()));
				arguments.add(new BasicNameValuePair("carNo", tcsq050.getCAR_NO()));
				arguments.add(new BasicNameValuePair("refContrNo", tcsq050.getREF_CONTR_NO()));
				arguments.add(new BasicNameValuePair("supportCd", tcsq050.getSUPPORT_CD()));
				arguments.add(new BasicNameValuePair("engSt", tcsq050.getENG_ST()));
				arguments.add(new BasicNameValuePair("jobCoordX", tcsq050.getJOB_COORD_X()));
				arguments.add(new BasicNameValuePair("jobCoordY", tcsq050.getJOB_COORD_Y()));
				arguments.add(new BasicNameValuePair("deviceNo", tcsq050.getDEVICE_NO()));
				arguments.add(new BasicNameValuePair("cancelIf", tcsq050.getCANCEL_IF()));
				arguments.add(new BasicNameValuePair("cancelDt", tcsq050.getCANCEL_DT()));
				arguments.add(new BasicNameValuePair("remark", tcsq050.getRMK()));


				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
					dataMap = new EasyJsonMap(
							returnJson.getJSONObject("dataMap"));
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}

			}catch (Exception ex){
				return false;
			}
			return true;
		}// end of doInBackground()

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.w("RealTimeJobHistoryService", "onPostExecute = RealTimeJobHistoryService");
			if(result){

				try {
					//정상적으로 TCSQ050에 인서트되면(서버쪽에)
					DatabaseRawQuery databaseRawQuery = new DatabaseRawQuery();
					databaseRawQuery.deleteRawTCSQ050(context, tcsq050);


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{

			}

		}
	}


	private TableTCSQ050 getTCSQ050(){
		String query = new DatabaseRawQuery().selectTCSQ050(commonSession.getEmpId(), commonSession.getWorkDt());

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		TableTCSQ050 wO_WT00_R01_ITEM00 = new TableTCSQ050();
		Field[] fields = wO_WT00_R01_ITEM00.getClass().getDeclaredFields();
		String name = "";
		String value = "";
		for (Field field : fields) {
			name = field.getName();
			try {
				value = mCursor.getString(mCursor.getColumnIndex(name));
				// value = field.get(partCheckListData.get(i)).toString();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				field.set(wO_WT00_R01_ITEM00, value);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		TableTCSQ050 workTargetData = wO_WT00_R01_ITEM00;
		mCursor.close();
		db.close();
		dbHelper.close();
		return workTargetData;
	}



}
