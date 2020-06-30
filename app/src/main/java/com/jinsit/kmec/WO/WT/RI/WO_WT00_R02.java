package com.jinsit.kmec.WO.WT.RI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 * 
 * @author 원성민 각 파트별 점검항목을 표시한다
 */

@SuppressLint("ResourceAsColor")
public class WO_WT00_R02 extends FragmentActivity {
	ListView lv_wo_checkList;
	//PartCheckListAdapter partCheckListAdapter;
	PartCheckCursorAdapter partCheckCursorAdapter;
	private ArrayList<PartCheckListData> partCheckListData;
	private EasyJsonList ejl;
	Context context;
	String nfcPlc = "";
	String jobNo;
	String workDt;
	String selTp = "1";
	boolean isComplete = true;
	int falsePosition = 0;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private CommonSession commonSession;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r02);
		activityInit();
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mNfcAdapter!=null)enableTagWriteMode();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mNfcAdapter!=null)disableTagWriteMode();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] duration = { 50, 100, 200, 300 };
		vib.vibrate(duration, -1);

		// Tag writing mode
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			tagUid = NfcParser.getTagId(detectedTag);
			ndefMsg = NfcParser.getTagText(detectedTag);
			Log.e("tagUid", tagUid);
			Log.e("ndefMsg", ndefMsg);

			if (!tagUid.isEmpty()) {
				// progress(true);
				// new selectNfcTag().execute("bagicWorkTime");
				
				nfcTagEventSave(ndefMsg);

			} else {
				// 비어있을 때 작업
			}

		}

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(ApplicationInfo.isDebugMode()){
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}else{
			return false;
		}
		
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		if(ApplicationInfo.isDebugMode()){
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				testNfcTagEventSave();
				return true;
			}
			return super.onOptionsItemSelected(item);	
			
		}else{
			return false;
		}
	
	}
	
	///점검항목 파트별 점검완료
	private void jobPartEnd(){
		
		String query = new DatabaseRawQuery().updateEndPartCheck(commonSession.getEmpId(), workDt, jobNo,nfcPlc, DateUtil.nowDateFormat("HH:mm"),"39");

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		
	}
	
	private void testNfcTagEventSave() {
	
		
				SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
						"점검항목을 저장하시겠습니까?", new btnClickListener() {
							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								if (checkListCheck()) {

									jobPartEnd();
									finish();
									
									} else {
									Toast.makeText(context, "선택안된 점검항목이 있습니다.",
											2000).show();

									partCheckCursorAdapter
											.notifyDataSetChanged();
									lv_wo_checkList.setSelection(falsePosition);
									partCheckCursorAdapter.widgetData.get(
											falsePosition).setUnChecked(true);
								}

							}
						});
				ynDialog.show();
	}
	
	private void nfcTagEventSave(String ndefMessage){
		WT_RI00_R01_ITEM02 nfcItem = NfcPlcSelect.selectNfcTagId(context, tagUid,jobNo);
		
		if(ndefMessage != null && ApplicationInfo.isDebugMode() && ndefMessage.length() > 10 ){
			String nfcPlc = ndefMessage.substring(ndefMessage.length() - 2);
			Log.e("nfcPlc no", "nfcPlc = " + nfcPlc);
			String carNo = ndefMessage.substring(11, 20);
			int indexOOf = ndefMessage.indexOf("&&&");
			Log.w("carNo no", "carNo = " + carNo);
			Log.e("indexOOf no", "indexOOf = " + indexOOf);
			 nfcItem = NfcPlcSelect.selectNfcTagIdDebug(context, tagUid,jobNo, nfcPlc, carNo);
		}
		if (nfcItem.getJOB_NO().equals("")) {
			// d
			SimpleDialog sm01 = new SimpleDialog(context, "알림","현재 작업과 태그한 NFC가 일치하지 않습니다.");
			sm01.show();
			
		} else {
			String tagPlc = nfcItem.getNFC_PLC();
			if(tagPlc.equals(nfcPlc)){
					//&& nfcItem.getNFC_PLC().equals(nfcPlc)) {
				///현재 NFC와 점검항목의 NFC가 일치하는지 확인
				SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
						"점검항목을 저장하시겠습니까?", new btnClickListener() {
							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								if(checkListCheck()){
									
									jobPartEnd();
									finish();
								//	new UpdatePartCheckListProgressAsync(context).execute(partCheckListAdapter.widgetData.size());
								}else{
									Toast.makeText(context, "선택안된 점검항목이 있습니다.", 2000).show();
								
									partCheckCursorAdapter.notifyDataSetChanged();
									lv_wo_checkList.setSelection(falsePosition);
									partCheckCursorAdapter.widgetData.get(falsePosition).setUnChecked(true);
									}
								
							}
						});
				ynDialog.show();
				
			}
			}
	}
	private void nfcTagEvent(String ndefMessage) {
		// TODO Auto-generated method stub
		String tagPlc = ndefMessage.substring(ndefMessage.length() - 2);
		Log.e("nfcPlc no", "nfcPlc = " + tagPlc);
		String carNo = ndefMessage.substring(11, 20);
		int indexOOf = ndefMessage.indexOf("&&&");
		Log.w("carNo no", "carNo = " + carNo);
		Log.e("indexOOf no", "indexOOf = " + indexOOf);
		
		if(tagPlc.equals(nfcPlc)){
			///현재 NFC와 점검항목의 NFC가 일치하는지 확인
			SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
					"점검항목을 저장하시겠습니까?", new btnClickListener() {
						@Override
						public void onButtonClick() {
							// TODO Auto-generated method stub
							if(checkListCheck()){
								
								jobPartEnd();
								finish();
							//	new UpdatePartCheckListProgressAsync(context).execute(partCheckListAdapter.widgetData.size());
							}else{
								Toast.makeText(context, "선택안된 점검항목이 있습니다.", 2000).show();
							
								partCheckCursorAdapter.notifyDataSetChanged();
								lv_wo_checkList.setSelection(falsePosition);
								partCheckCursorAdapter.widgetData.get(falsePosition).setUnChecked(true);
								}
							
						}
					});
			ynDialog.show();
		}else{
			///현재 NFC와 점검항목의 NFC가 한다
			SimpleDialog sm01 = new SimpleDialog(context, "알림","현재 점검중인 위치와 태그가 일치하지 않습니다.");
			sm01.show();
			
		}
	

	}
	
	
	private boolean overMonthCheck(){
		boolean isCheck = true;
		for (int i = 0; i < partCheckCursorAdapter.widgetData.size(); i++) {
			
			try {
				if(partCheckCursorAdapter.widgetData.get(i).getOverMonth().equals("3")){
					Log.e("getInputTp", "getInputTp =  "+ partCheckCursorAdapter.widgetData.get(i).getInputTp());
					
					
					if(partCheckCursorAdapter.widgetData.get(i).getInputTp3().equals("")){
						Log.w("inputTp3", "inputTp3 =  "+ partCheckCursorAdapter.widgetData.get(i)
										.getInputTp3());
						isCheck = false;
						falsePosition = i;
						break;
					}
				}
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				isCheck = false;
			}
		}// 포문끝
		
		
		return isCheck;
	}
	private boolean checkListCheck(){
		boolean isCheck = true;
		for (int i = 0; i < partCheckCursorAdapter.widgetData.size(); i++) {
			
			
			if(partCheckCursorAdapter.widgetData.get(i).getOverMonth().equals("N")&& 
					partCheckCursorAdapter.widgetData.get(i).getInputTp().isEmpty() == false &&
					(partCheckCursorAdapter.widgetData.get(i).getInputTp1().equals("")||
							partCheckCursorAdapter.widgetData.get(i).getInputTp3().equals("")||
							partCheckCursorAdapter.widgetData.get(i).getInputTp7().equals(""))){
				
			}
			
			
			
			try {
				
				if(partCheckCursorAdapter.widgetData.get(i).getOverMonth().equals("N")&&
						partCheckCursorAdapter.widgetData.get(i).getInputTp().isEmpty() == false &&
						(partCheckCursorAdapter.widgetData.get(i).getInputTp1().equals("")&&
								partCheckCursorAdapter.widgetData.get(i).getInputTp3().equals("")&&
								partCheckCursorAdapter.widgetData.get(i).getInputTp7().equals(""))){
					
					isCheck = false;
					falsePosition = i;
					break;
				}
				
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				isCheck = false;
			}
		}// 포문끝
		
		
		return isCheck;
	}

	private void enableTagWriteMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
				mWriteTagFilters, null);
	}

	private void disableTagWriteMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}

	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		commonSession = new CommonSession(context);
		nfcPlc = getIntent().getExtras().getString("nfcPlc");
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		String nfcPlcNm = getIntent().getExtras().getString("nfcPlcNm");
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("점검항목(" + nfcPlcNm + ")");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		lv_wo_checkList = (ListView) findViewById(R.id.lv_wo_checkList);
		
		Log.e("jobno plc " , "jobNo = " + jobNo + "   nfcPlc =" + nfcPlc);
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = new DatabaseRawQuery().selectCheckDetail(commonSession.getEmpId(),workDt, jobNo,nfcPlc);
				Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
							partCheckCursorAdapter =new PartCheckCursorAdapter(context,
						R.layout.listitem_wt_checklist, mCursor, new String[] {
								"CS_ITEM_CD", "CS_EMP_ID", "MONTH_CHK_IF", "HEADER_IF"},
						null, 0);
				lv_wo_checkList.setAdapter(partCheckCursorAdapter);
				//점검완료 버튼 추가하기 위해 footer추가
				TextView view = new TextView(context);
				view.setBackgroundResource(R.drawable.btn);
				view.setTextSize(20);
				view.setGravity(Gravity.CENTER_HORIZONTAL);
				view.setText("점검완료");
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						testNfcTagEventSave();
					}
				});
				///20160722 yowonsm 점검항목에서도 버튼으로 작업완료할 수 있도록
				lv_wo_checkList.addFooterView(view);
			
				if(mCursor.getCount()==0){
					
					
					SimpleDialog ynDialog = new SimpleDialog(context, "알림","점검항목이 없습니다.",
							new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									jobPartEnd();
									finish();
									
								}
							});
					ynDialog.show();
			
				};
		//new PartCheckListAsync().execute();
		setConfig();
	}
	
	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	private class StartCheckListAsync extends AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(Void result) {

			if (retMsg.equals("1")) {
				if (selTp.equals("1")) {
					Toast.makeText(context, "작업이 시작되었습니다", 1000).show();
				} else if (selTp.equals("2")) {
					Toast.makeText(context, "정상등록 되었습니다.", 1000).show();
					finish();
				}

			} else {
				Toast.makeText(context, "정상처리 안되었습니다.", 1000).show();
			}

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
						+ "ip/updatePartCheckState.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", selTp));
				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
				arguments.add(new BasicNameValuePair("wkTm", getTime()));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					retMsg = returnJson.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}

	private class PartCheckListAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {

			
//			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
//					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
//			SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//			ContentValues values = new ContentValues();
//
//			for (int i = 0; i < partCheckListData.size(); i++) {
//
//				PartCheckListData partInsertData = new PartCheckListData();
//				Field[] fields = partInsertData.getClass().getDeclaredFields();
//				String name = "";
//				String value = "";
//				for (Field field : fields) {
//					name = field.getName();
//					try {
//						value = field.get(partCheckListData.get(i)).toString();
//					} catch (IllegalAccessException e) { // TODO Auto-generated
//															// catch block
//						e.printStackTrace();
//					} catch (IllegalArgumentException e) {
//						// TODO Auto-generated catch block 
//						e.printStackTrace();
//						 }
//					
//						values.put(name, value);
//					}
//
//					db.insert("partCheckListData", null, values);
//				}
//				db.close();
//
//				Cursor mCursor = db.rawQuery("select * from partCheckListData",
//						null); // 쿼리 날리고 mCursor.moveToFirst();
//								// partCheckCursorAdapter =
//				new PartCheckCursorAdapter(context,
//						R.layout.listitem_wt_checklist, mCursor, new String[] {
//								"SMART_DESC", "INPUT_TP", "MONTH_CHK_IF" },
//						null, 0);
//				lv_wo_checkList.setAdapter(partCheckCursorAdapter);
//			  
			
			
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
			DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			String query = new DatabaseRawQuery().selectCheckDetail("301167", "2014-12-09", jobNo,nfcPlc);
			Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고 mCursor.moveToFirst();
							// partCheckCursorAdapter =
			new PartCheckCursorAdapter(context,
					R.layout.listitem_wt_checklist, mCursor, new String[] {
							"SMART_DESC", "INPUT_TP", "MONTH_CHK_IF" },
					null, 0);
			lv_wo_checkList.setAdapter(partCheckCursorAdapter);
		
//			partCheckListAdapter = new PartCheckListAdapter(context,
//					partCheckListData);
//			lv_wo_checkList.setAdapter(partCheckListAdapter);
			}

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				partCheckListData = new ArrayList<PartCheckListData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/selectPartCheckList.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					partCheckListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						partCheckListData.add(new PartCheckListData(ejl
								.getValue(i, "CS_EMP_ID"), ejl.getValue(i,
								"WORK_DT"), ejl.getValue(i, "JOB_NO"), ejl
								.getValue(i, "NFC_PLC"), ejl.getValue(i,
								"CS_ITEM_CD"), ejl.getValue(i, "CS_LOW_NM"),
								ejl.getValue(i, "SMART_DESC"), ejl.getValue(i,
										"MNG_DESC"), ejl
										.getValue(i, "CS_TOOLS"), ejl.getValue(
										i, "STD_ST"), ejl.getValue(i,
										"INPUT_TP"), ejl.getValue(i,
										"INPUT_TP1"), ejl.getValue(i,
										"INPUT_TP3"), ejl.getValue(i,
										"INPUT_TP7"), ejl.getValue(i,
										"INPUT_RMK"), ejl.getValue(i,
										"OVER_MONTH"), ejl.getValue(i,
										"MONTH_CHK_IF"), ejl.getValue(i,
										"MONTH_CHK")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}


	
	
	private void popupDialog() {

		new AlertDialog.Builder(context)
		.setTitle("점검을 시작하시겠습니까?")
				.setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						return;
					}
				})
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new StartCheckListAsync().execute();
					}
				}).show();
	}

	private String getTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm",
				Locale.KOREA);
		Date date = new Date();
		String time = dataFormat.format(date);
		Log.e("getTime", "getTime = " + time);
		return time;
	}

	//----------------------내비게이션 영역--------------------------------------//
		private HomeNavigation homeNavi;
		private FrameLayout testnavi;
		boolean isHide;
		private HomeNaviPreference naviPref;

		private void setToggleNavi(){
			boolean isHide = naviPref.isHide();
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int totalHeight = ScreenUtil.getRealScreenHeight(this);;
			int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
			int viewArea = naviHeight/6;
			int setPadding = totalHeight-viewArea-naviHeight;
			 if(isHide){
					testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
					Log.e("isHide", "naviHide = " +isHide );
				}else{
					testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
					Log.e("isHide", "naviHide = " +naviPref.isHide() );
				}
		}
		
		private void navigationInit(){
			testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
			homeNavi = new HomeNavigation(context, null);
			homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
			homeNavi.setToggleNavi();
			Button navi = (Button) homeNavi.getBtn_naviHOME();
			navi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					naviPref.setHide(!naviPref.isHide());
					homeNavi.setToggleNavi();
					
				}
			});
		}
	
		//----------------------내비게이션 영역--------------------------------------//
		
		@Override
		public void onBackPressed() {
			this.openDialog();
		}
		
		private void openDialog() {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(context)
			.setMessage("점검항목을 나가겠습니까?")
			.setPositiveButton("예",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							finish();
						}
					})
			.setNegativeButton("아니오",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							
							return;
						}
					}).show();
		}
		
	
}
