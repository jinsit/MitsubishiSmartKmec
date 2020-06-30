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
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * @deprecated 각파트별 점검항목 웹용이라 사용하지 않는다.
 * @author 원성민 각 파트별 점검항목을 표시한다
 */

@SuppressLint("ResourceAsColor")
public class WO_WT00_R02_W extends FragmentActivity {
	ListView lv_wo_checkList;
	PartCheckListAdapter partCheckListAdapter;
	PartCheckCursorAdapter partCheckCursorAdapter;
	private ArrayList<PartCheckListData> partCheckListData;
	private EasyJsonList ejl;
	Context context;
	String nfcPlc = "";
	String jobNo,workDt;
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

			if (!ndefMsg.isEmpty()) {
				// progress(true);
				// new selectNfcTag().execute("bagicWorkTime");
			
				nfcTagEvent(ndefMsg);

			} else {
				// 비어있을 때 작업
			}

		}

	}

	private void nfcTagEvent(String ndefMessage) {
		// TODO Auto-generated method stub

		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
				"점검항목을 저장하시겠습니까?", new btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						if(checkListCheck()){
							new UpdatePartCheckListProgressAsync(context).execute(partCheckListAdapter.widgetData.size());
						}else{
							Toast.makeText(context, "선택안된 점검항목이 있습니다.", 2000).show();
						
							partCheckListAdapter.notifyDataSetChanged();
							//lv_wo_checkList.setSelection(falsePosition);
							lv_wo_checkList.setSelectionFromTop(falsePosition, 0);
							partCheckListAdapter.widgetData.get(falsePosition).setUnChecked(true);
							}
						
					}
				});
		ynDialog.show();

	}
	
	private boolean checkListCheck(){
		boolean isCheck = true;
		for (int i = 0; i < partCheckListAdapter.widgetData.size(); i++) {
			
			try {
				if(partCheckListAdapter.widgetData.get(i).getInputTp().equals("3")){
					Log.e("getInputTp", "getInputTp =  "+ partCheckListAdapter.widgetData.get(i).getInputTp());
					if(partCheckListAdapter.widgetData.get(i).getInputTp3().equals("")){
						Log.w("inputTp3", "inputTp3 =  "+ partCheckListAdapter.widgetData.get(i)
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
								"CS_ITEM_CD", "CS_EMP_ID", "MONTH_CHK_IF" },
						null, 0);
				lv_wo_checkList.setAdapter(partCheckCursorAdapter);
		
		//new PartCheckListAsync().execute();
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

	private class UpdatePartCheckListAsync extends
			AsyncTask<Void, String, Void> {

		@SuppressLint("ResourceAsColor")
		@Override
		protected void onPostExecute(Void result) {

			// partCheckListAdapter = new PartCheckListAdapter(context,
			// partCheckListData);
			// lv_wo_checkList.setAdapter(partCheckListAdapter);
			if (isComplete) {
				Toast.makeText(context, "정상처리되었습니다.", Toast.LENGTH_SHORT)
						.show();
				selTp = "2";
				new StartCheckListAsync().execute();

			} else {
				Toast.makeText(context, "선택 안된부분이 있습니다.", Toast.LENGTH_SHORT)
						.show();
				partCheckListAdapter.notifyDataSetChanged();
				lv_wo_checkList.setSelection(falsePosition);
				partCheckListAdapter.holder.ll_wt_checkList
						.setBackgroundColor(R.color.comm_boldFont_color);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < partCheckListAdapter.widgetData.size(); i++) {
				try {

					GetHttp getHttp = new GetHttp();
					String param_url = WebServerInfo.getUrl()
							+ "ip/updatePartCheckList.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					JSONObject returnJson;
					String retrunValue = "";

					arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
					arguments
							.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
					arguments.add(new BasicNameValuePair("csItemCd",
							partCheckListAdapter.widgetData.get(i)
									.getCsItemCd()));
					Log.e("getCsItemCd", "getCsItemCd =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getCsItemCd());
					arguments
							.add(new BasicNameValuePair("inputTp",
									partCheckListAdapter.widgetData.get(i)
											.getInputTp()));
					Log.e("getInputTp", "getInputTp =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp());
					arguments.add(new BasicNameValuePair("inputTp1",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp1()));
					Log.d("inputTp1", "inputTp1 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp1());
					arguments.add(new BasicNameValuePair("inputTp3",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp3()));
					Log.w("inputTp3", "inputTp3 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp3());
					arguments.add(new BasicNameValuePair("inputTp7",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp7()));
					Log.e("inputTp7", "inputTp7 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp7());
					arguments.add(new BasicNameValuePair("inputRmk",
							partCheckListAdapter.widgetData.get(i)
									.getInputRmk()));
					arguments.add(new BasicNameValuePair("overMonth",
							partCheckListAdapter.widgetData.get(i)
									.getOverMonth()));
					Log.i("overMonth", "overMonth =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getOverMonth());
					returnJson = getHttp.getPost(param_url, arguments, true);

					retrunValue = returnJson.getString("dataString");
					if (!retrunValue.equals("1")) {
						isComplete = false;
						falsePosition = i;
						break;
					}

				} catch (Exception ex) {
					// 로그인이 실패하였습니다 띄어주기

					ex.printStackTrace();
				}

			}
			return null;

		}
	}

	public class UpdatePartCheckListProgressAsync extends AsyncTask<Integer// excute()실행시 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public UpdatePartCheckListProgressAsync(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDlg.setMessage("시작");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			final int taskCnt = params[0];
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.
			publishProgress("max", Integer.toString(taskCnt));

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌
		
			for (int i = 0; i < taskCnt; i++) {
				
				try {

					GetHttp getHttp = new GetHttp();
					String param_url = WebServerInfo.getUrl()
							+ "ip/updatePartCheckList.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					JSONObject returnJson;
					String retrunValue = "";

					arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
					arguments
							.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
					arguments.add(new BasicNameValuePair("csItemCd",
							partCheckListAdapter.widgetData.get(i)
									.getCsItemCd()));
					Log.e("getCsItemCd", "getCsItemCd =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getCsItemCd());
					arguments
							.add(new BasicNameValuePair("inputTp",
									partCheckListAdapter.widgetData.get(i)
											.getInputTp()));
					Log.e("getInputTp", "getInputTp =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp());
					arguments.add(new BasicNameValuePair("inputTp1",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp1()));
					Log.d("inputTp1", "inputTp1 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp1());
					arguments.add(new BasicNameValuePair("inputTp3",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp3()));
					Log.w("inputTp3", "inputTp3 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp3());
					arguments.add(new BasicNameValuePair("inputTp7",
							partCheckListAdapter.widgetData.get(i)
									.getInputTp7()));
					Log.e("inputTp7", "inputTp7 =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getInputTp7());
					arguments.add(new BasicNameValuePair("inputRmk",
							partCheckListAdapter.widgetData.get(i)
									.getInputRmk()));
					arguments.add(new BasicNameValuePair("overMonth",
							partCheckListAdapter.widgetData.get(i)
									.getOverMonth()));
					Log.i("overMonth", "overMonth =  "
							+ partCheckListAdapter.widgetData.get(i)
									.getOverMonth());
					returnJson = getHttp.getPost(param_url, arguments, true);

					retrunValue = returnJson.getString("dataString");
					if (!retrunValue.equals("1")) {
						isComplete = false;
						falsePosition = i;
						break;
					}
				} catch (Exception ex) {
					// 로그인이 실패하였습니다 띄어주기
				}

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "점검항목 번호 "
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝
			return taskCnt;
		}

		// onProgressUpdate() 함수는 publishProgress() 함수로 넘겨준 데이터들을 받아옴
		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress[0].equals("progress")) {
				mDlg.setProgress(Integer.parseInt(progress[1]));
				mDlg.setMessage(progress[2]);
			} else if (progress[0].equals("max")) {
				mDlg.setMax(Integer.parseInt(progress[1]));
			}
		}

		// onPostExecute() 함수는 doInBackground() 함수가 종료되면 실행됨
		@Override
		protected void onPostExecute(Integer result) {
			mDlg.dismiss();
			if (isComplete) {
				Toast.makeText(context,Integer.toString(result) + "개의 작업이 정상처리되었습니다.", Toast.LENGTH_SHORT)
						.show();
				selTp = "2";
				new StartCheckListAsync().execute();

			} else {
				Toast.makeText(context, "선택 안된부분이 있습니다.", Toast.LENGTH_SHORT)
						.show();
				partCheckListAdapter.notifyDataSetChanged();
				lv_wo_checkList.setSelection(falsePosition);
				partCheckListAdapter.holder.ll_wt_checkList
						.setBackgroundColor(R.color.comm_boldFont_color);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	
	public void popupDialog() {

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

}
