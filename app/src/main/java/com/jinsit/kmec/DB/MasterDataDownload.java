package com.jinsit.kmec.DB;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;

public class MasterDataDownload extends Activity implements OnClickListener {
	private ProgressDialog ProgressDialog;
	private EasyJsonList ejl;

	ArrayList<TableTCSQ210> tableTCSQ210Data;
	ArrayList<TableTCSQ030> tableTCSQ030Data;
	ArrayList<TableTCSQ213> tableTCSQ213Data;
	ArrayList<WORK_TBL> tableWORK_TBLData;
	ArrayList<TableTCSC010> tableTCSC010Data;

	ArrayList<String> arrayJobNo;
	Context context;

	String upQ030, upQ210, upQ213;
	private CommonSession commonSession;

	String updateWorkDt = "";
	private boolean isAllDataDownload = false;
	private ArrayList<String> currentJobNoList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.masterdata_download);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("데이터 업로드/다운로드");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		context = this;
		commonSession = new CommonSession(context);
		updateWorkDt = commonSession.getWorkDt();
		Button btn = (Button) findViewById(R.id.button1);

		btn.setOnClickListener(this);

		Button btn2 = (Button) findViewById(R.id.button2);

		btn2.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.button1:
				boolean isNetwork = NetworkStates.isNetworkStatus(context);
				if (isNetwork == true) {
					SimpleYesNoDialog ynD = new SimpleYesNoDialog(
							context,
							"점검항목을 다운로드 하시겠습니까?",
							new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									//새로운 잡이 있으면
									if(isNewJob(updateWorkDt, commonSession.getEmpId())){
										SimpleYesNoDialog ynD = new SimpleYesNoDialog(
												context,
												"이미 점검한 항목이 있습니다.\n 기존 점검항목도 같이 다운로드 하시겠습니까?",
												new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {

													@Override
													public void onButtonClick() {
														// TODO Auto-generated method stub
														isAllDataDownload = true;
														new DatabaseDownLoadAsync(context).execute(0);
													}
												},
												new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {

													@Override
													public void onButtonClick() {
														// TODO Auto-generated method stub
														isAllDataDownload = false;
														currentJobNoList = jobCount();
														new DatabaseDownLoadAsync(context).execute(0);
													}
												}, "모두 받기", "새 항목만 받기");
										ynD.show();
									}else{
										isAllDataDownload = true;
										new DatabaseDownLoadAsync(context).execute(0);
									}


								}
							});
					ynD.show();

				} else {
					Toast.makeText(context, "인터넷이 연결안되었습니다.", Toast.LENGTH_SHORT)
							.show();
				}

				break;

			case R.id.button2:

				ArrayList<String> jobNOArray = jobCount();
				arrayJobNo = jobNOArray;

				if (arrayJobNo.size() == 0) {
					SimpleDialog sd = new SimpleDialog(context, "알림",
							"업로드할 데이터가 없습니다.", new btnClickListener() {
						@Override
						public void onButtonClick() {

						}
					});
					sd.show();
				} else {

					SimpleYesNoDialog ynD = new SimpleYesNoDialog(
							context,
							"점검하신 정기점검항목을 업로드 하시겠습니까?",
							new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									new UploadDatabaseProgressAsync(context)
											.execute(arrayJobNo.size());
								}
							});
					ynD.show();

				}

				break;

		}
	}

	private void upLoadDatabase(String jobNo) {
		// TODO Auto-generated method stub

		StringBuffer sb = updateWorkTable(jobNo);
		upQ030 = sb.toString();
		StringBuffer sb2 = update210Table(jobNo);
		upQ210 = sb2.toString();
		StringBuffer sb3 = update213Table(jobNo);
		upQ213 = sb3.toString();

	}

	private void stringInit() {
		upQ030 = "";
		upQ210 = "";
		upQ213 = "";
	}

	private ArrayList<String> jobCount() {
		ArrayList<String> uploadJobNo = new ArrayList<String>();
		String query = "SELECT JOB_NO FROM WORK_TBL " + " WHERE WORK_DT = '"
				+ updateWorkDt + "'";// AND UPLOAD_YN = 'N' ";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();
		if (mCursor != null && mCursor.getCount() != 0) {

			do {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {
					uploadJobNo.add(mCursor.getString(i));
				}

			} while (mCursor.moveToNext());

		} else {

		}
		mCursor.close();
		db.close();
		dbHelper.close();

		return uploadJobNo;
	}

	private boolean isNewJob(String tableName, String empId){
		boolean isNewJob;
		String query = "SELECT count() FROM WORK_TBL WHERE WORK_DT = '"+ updateWorkDt + "'" + " AND CS_EMP_ID = '" + empId + "'";// AND UPLOAD_YN = 'N' ";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context, DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		String count = mCursor.getString(0);
		int jobCount = Integer.valueOf(count);

		mCursor.close();
		db.close();
		dbHelper.close();

		if (jobCount == 0) {
			isNewJob = false;
		} else {
			isNewJob = true;
		}

		return isNewJob;
	}

	private String updateRet;
	// boolean isComplete = true;
	ArrayList<Boolean> isComplete;
	ArrayList<Integer> falsePosition;

	// int falsePosition = 0;
	public class UploadDatabaseProgressAsync extends AsyncTask<Integer//
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public UploadDatabaseProgressAsync(Context context) {
			mContext = context;
			falsePosition = new ArrayList<Integer>();
			isComplete = new ArrayList<Boolean>();
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("업로드 시작");
			mDlg.setCancelable(false);
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
				stringInit();
				upLoadDatabase(arrayJobNo.get(i));

				try {
					GetHttp getHttp = new GetHttp();
					String param_url = WebServerInfo.getUrl()
							+ "ip/dbUploadTBL.do";	//20170922 yowonsm 테스트끝나면 바꿈
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("upQ030", upQ030));
					arguments.add(new BasicNameValuePair("upQ210", upQ210));
					arguments.add(new BasicNameValuePair("upQ213", upQ213));

					JSONObject returnJson = getHttp.getPost(param_url,
							arguments, true);

					try {

						updateRet = returnJson.getString("dataString");
						Log.v("updateRet", "UpdateRet = " + updateRet);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						publishProgress("progress", Integer.toString(i),
								"업로드 번호 " + Integer.toString(i) + "번 진행중");
						if (updateRet.equals("1")) {
							// /UPLOAD_YN 업데이트 해주기
							// arrayJobNo.get(i);
							updateTable(updateWorkDt, arrayJobNo.get(i));

						} else {
							isComplete.add(false);
							falsePosition.add(i);
							// break;
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} catch (Exception ex) {
					// 로그인이 실패하였습니다 띄어주기
				}

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
			if (isComplete.size() == 0) {
				// 실패내역이 없으면 업로드 완료
				SimpleDialog sd = new SimpleDialog(context, "알림",
						"업로드가 정상처리 되었습니다..", new btnClickListener() {
					@Override
					public void onButtonClick() {
						finish();
					}
				});
				sd.show();
			} else {
				// 실패내역이 있으면 몇개의 어떤작업이 에런지 보여줌
				isComplete.add(false);
				String job = "";
				ArrayList<String> failJob = new ArrayList<String>();

				for (int i = 0; i < falsePosition.size(); i++) {
					job = arrayJobNo.get(falsePosition.get(i));
					failJob.add(failjobName(updateWorkDt, job));
				}

				if (failJob.size() != 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < failJob.size(); i++) {
						sb.append("[").append(failJob.get(i)).append("],");
					}
					String noticeMsg = sb.toString();
					SimpleDialog sd = new SimpleDialog(context, "알림", noticeMsg
							+ "작업이 실패하였습니다.  관리자에게 문의 하십시오.",
							new btnClickListener() {
								@Override
								public void onButtonClick() {
									// finish();
								}
							});
					sd.show();
				}

			}

		}
	}

	private String failjobName(String workDt, String jobNo) {
		String retStr = "";
		StringBuffer inputParams = new StringBuffer();
		String query = "SELECT BLDG_NM, CAR_NO FROM WORK_TBL "
				+ " WHERE WORK_DT = '" + workDt + "' AND JOB_NO = '" + jobNo
				+ "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();
		if (mCursor != null && mCursor.getCount() != 0) {

			do {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {
					inputParams.append(mCursor.getString(i)).append("/");
				}

			} while (mCursor.moveToNext());

		} else {

		}
		mCursor.close();
		db.close();
		dbHelper.close();

		// if(inputParams.equals("")){
		retStr = inputParams.toString();
		// }

		return retStr;
	}

	StringBuffer updateWorkTable(String updateJobNo) {

		StringBuffer inputParams = new StringBuffer();
//		String query = "SELECT CS_EMP_ID, WORK_DT, JOB_NO, "
//				+ " CS_FR, '', JOB_ST, '', MOVE_TM, ARRIVE_TM, COMPLETE_TM, CS_EMP_ID FROM WORK_TBL "
//				+ " WHERE WORK_DT = '" + updateWorkDt + "' AND JOB_NO = '"
//				+ updateJobNo + "'";
		String query = "SELECT A.CS_EMP_ID, A.WORK_DT, A.JOB_NO, "
				+ " A.CS_FR, '', A.JOB_ST, '', A.MOVE_TM, A.ARRIVE_TM, A.COMPLETE_TM, A.CS_EMP_ID, "
				+ " B.CS_TP, B.SELCHK_USID, B.SELCHK_USID_NM, B.SUB_SELCHK_USID, B.SUB_SELCHK_USID_NM "
				+ " FROM WORK_TBL A "
				+ " LEFT JOIN TCSQ030 B ON A.CS_EMP_ID = B.CS_EMP_ID AND A.WORK_DT = B.WORK_DT AND A.JOB_NO = B.JOB_NO "
				+ " WHERE A.WORK_DT = '" + updateWorkDt + "' AND A.JOB_NO = '" +updateJobNo +  "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();
		if (mCursor != null && mCursor.getCount() != 0) {

			do {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {

					if (i == mCursor.getColumnCount() - 1) {
						inputParams.append(mCursor.getString(i)).append("|");
					} else {
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
			} while (mCursor.moveToNext());
		}

		mCursor.close();
		db.close();
		dbHelper.close();

		return inputParams;

	}

	StringBuffer update210Table(String updateJobNo) {
		// value = mCursor.getString(mCursor.getColumnIndex("CS_EMP_ID"));

		StringBuffer inputParams = new StringBuffer();

		String query = "SELECT CS_EMP_ID, WORK_DT, JOB_NO,NFC_PLC,CS_TM_FR, CS_TM_TO,JOB_ST,CS_EMP_ID  FROM TCSQ210 "
				+ " WHERE WORK_DT = '"
				+ updateWorkDt
				+ "' AND JOB_NO = '"
				+ updateJobNo + "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		if (mCursor != null && mCursor.getCount() != 0) {

			do {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {

					if (i == mCursor.getColumnCount() - 1) {
						inputParams.append(mCursor.getString(i)).append("|");
					} else {
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}

			} while (mCursor.moveToNext());
		}

		mCursor.close();
		db.close();
		dbHelper.close();

		return inputParams;

	}

	StringBuffer update213Table(String updateJobNo) {
		StringBuffer inputParams = new StringBuffer();

		String query = "select CS_EMP_ID,WORK_DT,JOB_NO,NFC_PLC,CS_ITEM_CD,INPUT_TP,"
				+ "INPUT_TP1,INPUT_TP3,INPUT_TP7,OVER_MONTH,MONTH_CHK,MONTH_CHK_IF,INPUT_RMK,DEF_VAL_ST,CS_EMP_ID from TCSQ213 "
				+ " WHERE WORK_DT = '"
				+ updateWorkDt
				+ "' AND JOB_NO = '"
				+ updateJobNo + "'";

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		if (mCursor != null && mCursor.getCount() != 0) {

			do {
				for (int i = 0; i < mCursor.getColumnCount(); i++) {

					if (i == mCursor.getColumnCount() - 1) {
						inputParams.append(mCursor.getString(i)).append("|");
					} else {
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
			} while (mCursor.moveToNext());
		}

		mCursor.close();
		db.close();
		dbHelper.close();

		return inputParams;
	}

	private class UploadDatabaseAsync extends AsyncTask<Void, String, Void> {

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
				String param_url = WebServerInfo.getUrl() + "ip/dbUploadTBL.do";	//20170922 yowonsm 테스트끝나면 바꿈

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("upQ030", upQ030));
				arguments.add(new BasicNameValuePair("upQ210", upQ210));
				arguments.add(new BasicNameValuePair("upQ213", upQ213));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));

					int jsonSize = returnJson.getJSONArray("dataList").length();

					System.out.println(returnJson);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}

	private int rawCount(String tableNm) {
		int count = 0;
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor c = db.rawQuery("SELECT count() from   " + tableNm, null);
		c.moveToFirst();
		String getC = c.getString(0);
		c.close();
		db.close();
		dbHelper.close();
		count = Integer.valueOf(getC);
		return count;
	}

	public class DatabaseDownLoadAsync extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public DatabaseDownLoadAsync(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("점검계획 다운로드...");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {
			boolean isInsert = false;
			int taskCnt = params[0];
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

			tableTCSQ210Data = new ArrayList<TableTCSQ210>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/dbDownloadTCSQ210.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableTCSQ210Data.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				taskCnt = jsonSize;
				for (int i = 0; i < jsonSize; i++) {

					tableTCSQ210Data.add(new TableTCSQ210(ejl.getValue(i,"CS_EMP_ID"),
							ejl.getValue(i, "WORK_DT"),
							ejl	.getValue(i, "JOB_NO"),
							ejl.getValue(i, "NFC_PLC"),
							ejl.getValue(i, "NFC_PLC_NM"),
							ejl.getValue(i,	"CS_TM_FR"),
							ejl.getValue(i, "CS_TM_TO"),
							ejl.getValue(i, "CS_TM"),
							ejl.getValue(i, "JOB_ST"),
							ejl.getValue(i, "ENG_ST"),
							ejl.getValue(i,	"REASON_RMK"),
							ejl.getValue(i, "RMK"),
							ejl.getValue(i, "ISRT_USR_ID"),
							ejl.getValue(i, "ISRT_DT"),
							ejl.getValue(i,	"UPDT_USR_ID"),
							ejl.getValue(i, "UPDT_DT"),
							ejl.getValue(i, "NFC_UDID")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			int getCount = rawCount("TCSQ210");
			if (getCount == 0) {
				isInsert = true;
			}

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			publishProgress("max", Integer.toString(taskCnt));
			for (int i = 0; i < taskCnt; i++) {
				ContentValues values = new ContentValues();
				TableTCSQ210 tcsq210Data = new TableTCSQ210();
				// PartCheckListData partInsertData = new PartCheckListData();
				Field[] fields = tcsq210Data.getClass().getDeclaredFields();
				String name = "";
				String value = "";
				for (Field field : fields) {
					name = field.getName();
					try {
						value = field.get(tableTCSQ210Data.get(i)).toString();
					} catch (IllegalAccessException e) { // TODO Auto-generated
						// catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e){
						e.printStackTrace();
						value = null;
					}
					if(value != null)
					values.put(name, value);
				}
				if (isInsert) {
					db.insert("TCSQ210", null, values);
				} else {
					//전체데이터 다운로드 일 경우(기존데이터 업데이트)
					if(isAllDataDownload){

						String where = "CS_EMP_ID = '"
								+ values.getAsString("CS_EMP_ID")
								+ "' AND WORK_DT = '"
								+ values.getAsString("WORK_DT")
								+ "' AND JOB_NO = '" + values.getAsString("JOB_NO")
								+ "' AND NFC_PLC = '"
								+ values.getAsString("NFC_PLC") + "'";
						// 위의 웨어조건에 맞게 업데이트를 먼저 한다.
						// 업데이트 성공이면 1 혹은 그 이상이 될것이고 실패면 0을 리턴한다.
						// 그래서 udpateCount가 1보다 작으면 인서트를 친다.
						int updateCount = db.update("TCSQ210", values, where, null);
						Log.e("mastDataDownload","masterdown1 TCSQ210 updateCount = " + updateCount);
						if (updateCount < 1) {
							long errCd = db.insert("TCSQ210", null, values); // errCd
							// 는
							// 인서트된
							// row의
							// 번호
							// =
							// _id
							Log.i("mastDataDownload", "masterdown1 TCSQ210 errCd= "+ errCd);
						}

					}else{
						boolean isNewJob = currentJobNoList.contains(values.getAsString("JOB_NO"));
						if(!isNewJob){
							long errCd = db.insert("TCSQ210", null, values); // errCd
							Log.i("mastDataDownload", "masterdown1 TCSQ210 errCd= "+ errCd);
						}
					}

				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "점검계획 "
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝

			db.close();
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
			new DatabaseDownLoadAsync2(context).execute(0);
		}
	}

	public class DatabaseDownLoadAsync2 extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public DatabaseDownLoadAsync2(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("점검위치 다운로드...");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			int taskCnt = params[0];
			boolean isInsert = false;
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

			tableTCSQ030Data = new ArrayList<TableTCSQ030>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/dbDownloadTCSQ030.do";	//20170922 yowonsm 테스트끝나면 푼다.

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableTCSQ030Data.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				taskCnt = jsonSize;
				for (int i = 0; i < jsonSize; i++) {

					tableTCSQ030Data
							.add(new TableTCSQ030(ejl.getValue(i, "CS_EMP_ID"),
									ejl.getValue(i, "WORK_DT"), ejl.getValue(i,
									"JOB_NO"), ejl.getValue(i,
									"WORK_CD"), ejl.getValue(i,
									"CS_TM_FR"), ejl.getValue(i,
									"CS_TM_TO"), ejl.getValue(i,
									"CS_TM"), ejl
									.getValue(i, "BLDG_NO"), ejl
									.getValue(i, "CAR_NO"), ejl
									.getValue(i, "REF_CONTR_NO"), ejl
									.getValue(i, "SUPPORT_CD"), ejl
									.getValue(i, "JOB_ST"), ejl
									.getValue(i, "ENG_ST"), ejl
									.getValue(i, "YET_REASON"), ejl
									.getValue(i, "PLAN_YYMM"), ejl
									.getValue(i, "PLAN_SQ"), ejl
									.getValue(i, "WK_ORDER"), ejl
									.getValue(i, "WK_DAY"), ejl
									.getValue(i, "CS_DEPT_CD"), ejl
									.getValue(i, "INSP_TIME_BC"), ejl
									.getValue(i, "RMK"),
									ejl.getValue(i, "ISRT_USR_ID"),
									ejl.getValue(i, "ISRT_DT"),
									ejl.getValue(i, "UPDT_USR_ID"),
									ejl.getValue(i,"UPDT_DT"),
									ejl.getValue(i,"CS_TP"),
									ejl.getValue(i,"SELCHK_USID"),
									ejl.getValue(i,"SELCHK_USID_NM"),
									ejl.getValue(i,"SUB_SELCHK_USID"),
									ejl.getValue(i,"SUB_SELCHK_USID_NM")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			int getCount = rawCount("TCSQ030");
			if (getCount == 0) {
				isInsert = true;
			}

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			publishProgress("max", Integer.toString(taskCnt));
			for (int i = 0; i < taskCnt; i++) {
				ContentValues values = new ContentValues();

				TableTCSQ030 tcsq030Data = new TableTCSQ030();
				// PartCheckListData partInsertData = new PartCheckListData();
				Field[] fields = tcsq030Data.getClass().getDeclaredFields();
				String name = "";
				String value = "";
				for (Field field : fields) {
					name = field.getName();
					try {
						value = field.get(tableTCSQ030Data.get(i)).toString();
					} catch (IllegalAccessException e) { // TODO Auto-generated
						// catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e){
						e.printStackTrace();
						value = null;
					}
					if(value != null)
						values.put(name, value);
				}

				// db.insert("TCSQ030", null, values);
				if (isInsert) {
					db.insert("TCSQ030", null, values);
				} else {
					//전체데이터 다운로드 일 경우(기존데이터 업데이트)
					if(isAllDataDownload){

						String where = "CS_EMP_ID = '"
								+ values.getAsString("CS_EMP_ID")
								+ "' AND WORK_DT = '"
								+ values.getAsString("WORK_DT")
								+ "' AND JOB_NO = '" + values.getAsString("JOB_NO")
								+ "'";
						// 위의 웨어조건에 맞게 업데이트를 먼저 한다.
						// 업데이트 성공이면 1 혹은 그 이상이 될것이고 실패면 0을 리턴한다.
						// 그래서 udpateCount가 1보다 작으면 인서트를 친다.
						int updateCount = db.update("TCSQ030", values, where, null);
						Log.e("mastDataDownload",
								"masterdown2 TCSQ030 updateCount = " + updateCount);
						if (updateCount < 1) {
							long errCd = db.insert("TCSQ030", null, values); // errCd
							Log.i("mastDataDownload", "masterdown2 TCSQ030 errCd= "+ errCd);
						}

					}else{
						boolean isNewJob = currentJobNoList.contains(values.getAsString("JOB_NO"));
						if(!isNewJob){
							long errCd = db.insert("TCSQ030", null, values); // errCd
							Log.i("mastDataDownload", "masterdown2 TCSQ030 errCd= "+ errCd);
						}
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "점검위치 "
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝

			db.close();
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
			new DatabaseDownLoadAsync4(context).execute(0);
		}
	}

	public class DatabaseDownLoadAsync3 extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public DatabaseDownLoadAsync3(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("점검항목 다운로드...");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			int taskCnt = params[0];
			boolean isInsert = false;
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

			tableTCSQ213Data = new ArrayList<TableTCSQ213>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
						+ "ip/dbDownloadTCSQ213.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = getHttp.getPostWithTimeout(param_url,
					arguments, true, 60000);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableTCSQ213Data.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				taskCnt = jsonSize;
				for (int i = 0; i < jsonSize; i++) {

					tableTCSQ213Data.add(new TableTCSQ213(ejl.getValue(i,
							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"), ejl
							.getValue(i, "JOB_NO"), ejl.getValue(i, "NFC_PLC"),
							ejl.getValue(i, "CS_ITEM_CD"), ejl.getValue(i,
							"CS_TOOLS"), ejl.getValue(i, "STD_ST"), ejl
							.getValue(i, "INPUT_TP"), ejl.getValue(i,
							"INPUT_TP1"), ejl.getValue(i, "INPUT_TP3"),
							ejl.getValue(i, "INPUT_TP7"), ejl.getValue(i,
							"INPUT_RMK"),
							ejl.getValue(i, "OVER_MONTH"), ejl.getValue(i,
							"MONTH_CHK"), ejl.getValue(i, "RMK"), ejl
							.getValue(i, "ISRT_USR_ID"), ejl.getValue(
							i, "ISRT_DT"), ejl.getValue(i,
							"UPDT_USR_ID"), ejl.getValue(i, "UPDT_DT"),
							ejl.getValue(i, "CS_LOW_NM"), ejl.getValue(i,
							"SMART_DESC"), ejl.getValue(i, "MNG_DESC"),
							ejl.getValue(i, "MONTH_CHK_IF"), ejl.getValue(i,
							"EL_INFO_MAP"), ejl.getValue(i, "DEF_VAL"),
							ejl.getValue(i, "DEF_VAL_ST"),
							ejl.getValue(i, "PRE_WORK_MM"),
							ejl.getValue(i, "PRE_INPUT_TP"),
							ejl.getValue(i, "HEADER_IF"),
							ejl.getValue(i, "INSP_METHOD")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			int getCount = rawCount("TCSQ213");
			if (getCount == 0) {
				isInsert = true;
			}

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();

			publishProgress("max", Integer.toString(taskCnt));
			for (int i = 0; i < taskCnt; i++) {
				TableTCSQ213 tcsq213Data = new TableTCSQ213();
				// PartCheckListData partInsertData = new PartCheckListData();
				Field[] fields = tcsq213Data.getClass().getDeclaredFields();
				String name = "";
				String value = "";
				for (Field field : fields) {
					name = field.getName();
					try {
						value = field.get(tableTCSQ213Data.get(i)).toString();
					} catch (IllegalAccessException e) { // TODO Auto-generated
						// catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e){
						e.printStackTrace();
						value = null;
					}
					if(value != null)
						values.put(name, value);
				}

				if (isInsert) {
					db.insert("TCSQ213", null, values);
				} else {

					String where = "CS_EMP_ID = '"
							+ values.getAsString("CS_EMP_ID")
							+ "' AND WORK_DT = '"
							+ values.getAsString("WORK_DT")
							+ "' AND JOB_NO = '" + values.getAsString("JOB_NO")
							+ "' AND NFC_PLC = '"
							+ values.getAsString("NFC_PLC") + "'"
							+ " AND CS_ITEM_CD = '"
							+ values.getAsString("CS_ITEM_CD") + "'";
					// 위의 웨어조건에 맞게 업데이트를 먼저 한다.
					// 업데이트 성공이면 1 혹은 그 이상이 될것이고 실패면 0을 리턴한다.
					// 그래서 udpateCount가 1보다 작으면 인서트를 친다.
					int updateCount = db.update("TCSQ213", values, where, null);
					Log.e("mastDataDownload",
							"masterdown3 TCSQ213 updateCount = " + updateCount);
					if (updateCount < 1) {
						long errCd = db.insert("TCSQ213", null, values); // errCd
						// 는
						// 인서트된
						// row의
						// 번호
						// =
						// _id
						Log.i("mastDataDownload", "masterdown3 TCSQ213 errCd= "
								+ errCd);
					}
					/*
					 * int j = i+1; if(j<=getCount){ db.update("TCSQ213",
					 * values, "_id = '" + j+"'",null); }else{
					 * db.insert("TCSQ213", null, values); }
					 */
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "점검항목 "
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝

			db.close();
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
			new DatabaseDownLoadAsync5(context).execute(0);

		}
	}

	public class DatabaseDownLoadAsync4 extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public DatabaseDownLoadAsync4(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("작업상세정보 다운로드...");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			int taskCnt = params[0];
			boolean isInsert = false;
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

			tableWORK_TBLData = new ArrayList<WORK_TBL>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/dbDownloadWORK_TBL.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableWORK_TBLData.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				taskCnt = jsonSize;
				for (int i = 0; i < jsonSize; i++) {

					tableWORK_TBLData.add(new WORK_TBL(ejl.getValue(i,
							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"), ejl
							.getValue(i, "JOB_NO"), ejl.getValue(i, "WORK_CD"),
							ejl.getValue(i, "WORK_NM"), ejl.getValue(i, "ST"),
							ejl.getValue(i, "JOB_ST"),
							ejl.getValue(i, "CS_DT"), ejl.getValue(i, "CS_FR"),
							ejl.getValue(i, "BLDG_NO"), ejl.getValue(i,
							"BLDG_NM"), ejl.getValue(i, "CAR_NO"), ejl
							.getValue(i, "RESERV_ST"), ejl.getValue(i,
							"ADDR"), ejl.getValue(i, "MAIN_EMP_NM"),
							ejl.getValue(i, "MAIN_EMP_PHONE"), ejl.getValue(i,
							"SUB_EMP_NM"), ejl.getValue(i,
							"SUB_EMP_PHONE"), ejl.getValue(i,
							"CS_DEPT_NM"),
							ejl.getValue(i, "NOTIFY_NM"), ejl.getValue(i,
							"NOTIFY_PHONE"), ejl.getValue(i,
							"RECEV_DESC"), ejl.getValue(i, "RECEV_TM"),
							ejl.getValue(i, "RESERV_TM"), ejl.getValue(i,
							"REPAIR_TM"), ejl.getValue(i, "MOVE_TM"),
							ejl.getValue(i, "ARRIVE_TM"), ejl.getValue(i,
							"COMPLETE_TM"), ejl
							.getValue(i, "RESCUE_TM"), ejl.getValue(i,
							"START_TM"), ejl.getValue(i, "CONTACT_CD"),
							ejl.getValue(i, "STATUS_CD"), ejl.getValue(i,
							"REF_CONTR_NO"), ejl
							.getValue(i, "PARTS_NO"), ejl.getValue(i,
							"MODEL_NM"), ejl.getValue(i, "RECEV_NO"),
							ejl.getValue(i, "CHECK_YN"), ejl.getValue(i,
							"CBS_YN")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			int getCount = rawCount("WORK_TBL");
			if (getCount == 0) {
				isInsert = true;
			}

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			publishProgress("max", Integer.toString(taskCnt));
			for (int i = 0; i < taskCnt; i++) {
				ContentValues values = new ContentValues();
				WORK_TBL WORK_TBLData = new WORK_TBL();
				// PartCheckListData partInsertData = new PartCheckListData();
				Field[] fields = WORK_TBLData.getClass().getDeclaredFields();
				String name = "";
				String value = "";
				for (Field field : fields) {
					name = field.getName();
					try {
						value = field.get(tableWORK_TBLData.get(i)).toString();
					} catch (IllegalAccessException e) { // TODO Auto-generated
						// catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e){
						e.printStackTrace();
						value = null;
					}
					if(value != null){
						values.put(name, value);
						values.get("JOB_NO");
						tableWORK_TBLData.get(i).getJOB_NO();
					}

				}

				if (isInsert) {
					db.insert("WORK_TBL", null, values);
				} else {

					//전체데이터 다운로드 일 경우(기존데이터 업데이트)
					if(isAllDataDownload){
						String where = "CS_EMP_ID = '"
								+ values.getAsString("CS_EMP_ID")
								+ "' AND WORK_DT = '"
								+ values.getAsString("WORK_DT")
								+ "' AND JOB_NO = '" + values.getAsString("JOB_NO")
								+ "'";
						// 위의 웨어조건에 맞게 업데이트를 먼저 한다.
						// 업데이트 성공이면 1 혹은 그 이상이 될것이고 실패면 0을 리턴한다.
						// 그래서 udpateCount가 1보다 작으면 인서트를 친다.
						int updateCount = db
								.update("WORK_TBL", values, where, null);
						Log.e("mastDataDownload",
								"masterdown4 workTbl updateCount = " + updateCount);
						if (updateCount < 1) {
							long errCd = db.insert("WORK_TBL", null, values); // errCd는	인서트된row의 번호=	_id
							Log.i("mastDataDownload", "masterdown4 workTbl errCd= "+ errCd);
						}

					}else{
						boolean isNewJob = currentJobNoList.contains(values.getAsString("JOB_NO"));
						if(!isNewJob){
							long errCd = db.insert("WORK_TBL", null, values); // errCd
							Log.i("mastDataDownload", "masterdown4 workTbl errCd= "+ errCd);
						}
					}

				}

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "작업상세정보"
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝

			db.close();
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
			new DatabaseDownLoadAsync6(mContext, tableWORK_TBLData).execute(0);

		}
	}

	public class DatabaseDownLoadAsync5 extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public DatabaseDownLoadAsync5(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("관리자정보 다운 시작");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			int taskCnt = params[0];
			boolean isInsert = false;
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

			tableTCSC010Data = new ArrayList<TableTCSC010>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/dbDownloadTCSC010.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", commonSession
					.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession
					.getWorkDt()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableTCSC010Data.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				taskCnt = jsonSize;
				for (int i = 0; i < jsonSize; i++) {
					tableTCSC010Data.add(new TableTCSC010(ejl.getValue(i,
							"BLDG_NO"), ejl.getValue(i, "CLIENT_ID"), ejl
							.getValue(i, "CLIENT_CD"), ejl.getValue(i,
							"CLIENT_NM"), ejl.getValue(i, "MAIL_ADDR"), ejl
							.getValue(i, "DEPT_NM"),
							ejl.getValue(i, "GRAD_NM"), ejl
							.getValue(i, "PHONE"), ejl.getValue(i,
							"MOBILE"), ejl.getValue(i, "FAX")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			int getCount = rawCount("TCSC010");
			if (getCount == 0) {
				isInsert = true;
			}

			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			ContentValues values = new ContentValues();

			publishProgress("max", Integer.toString(taskCnt));
			for (int i = 0; i < taskCnt; i++) {
				TableTCSC010 tableTcsc010Data = new TableTCSC010();
				// PartCheckListData partInsertData = new PartCheckListData();
				Field[] fields = tableTcsc010Data.getClass()
						.getDeclaredFields();
				String name = "";
				String value = "";
				for (Field field : fields) {
					name = field.getName();
					try {
						value = field.get(tableTCSC010Data.get(i)).toString();
					} catch (IllegalAccessException e) { // TODO Auto-generated
						// catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (NullPointerException e){
						e.printStackTrace();
						value = null;
					}
					if(value != null){
						values.put(name, value);
					}
				}
				if (isInsert) {
					db.insert("TCSC010", null, values);
				} else {
					int j = i + 1;
					if (j <= getCount) {
						db.update("TCSC010", values, "_id = '" + j + "'", null);
					} else {
						db.insert("TCSC010", null, values);
					}
				}
				// db.insert("WORK_TBL", null, values);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				publishProgress("progress", Integer.toString(i), "관리자정보 "
						+ Integer.toString(i) + "번 진행중");
			}// 포문끝

			db.close();
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

			SimpleDialog sd = new SimpleDialog(context, "다운로드 완료",
					"다운로드가 완료되었습니다. 이전페이지로 이동합니다.", new btnClickListener() {
				@Override
				public void onButtonClick() {
					finish();
				}
			});
			sd.show();

		}
	}

	void updateTable(String workDt, String jobNo) {
		UpdateTable updateTable = new UpdateTable(context);
		updateTable.updateTable("Y", workDt, jobNo);

	}

	public class DatabaseDownLoadAsync6 extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;
		private ArrayList<WORK_TBL> workList;

		public DatabaseDownLoadAsync6(Context context, ArrayList<WORK_TBL> workTblList) {
			mContext = context;
			workList = workTblList;

			//데이터 다운로드를 신규생성된 데이터만 다운로드
			if(currentJobNoList != null && currentJobNoList.size() > 0){
				for (int i = 0; i < currentJobNoList.size(); i++){
					String jobNO = currentJobNoList.get(i);
					for(int j = workList.size() -1; j >= 0; j-- ){
						if(jobNO.equals(workList.get(j).getJOB_NO())){
							workList.remove(j);
						}
					}
				}
			}
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDlg.setMessage("점검항목 다운로드...");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {
			int taskCnt = params[0];

			for(int job = 0; job < workList.size(); job++)
			{
				boolean isInsert = false;
				taskCnt = 0;
				// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
				// 넘겨준다.
				// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.

				// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌

				tableTCSQ213Data = new ArrayList<TableTCSQ213>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/dbDownloadTCSQ213Range.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("csEmpId", commonSession
						.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession
						.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo", workList.get(job).getJOB_NO()));

				JSONObject returnJson = getHttp.getPost(param_url,
						arguments, true);
				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					tableTCSQ213Data.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					taskCnt = jsonSize;
					for (int i = 0; i < jsonSize; i++) {

						tableTCSQ213Data.add(new TableTCSQ213(ejl.getValue(i,
								"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"), ejl
								.getValue(i, "JOB_NO"), ejl.getValue(i, "NFC_PLC"),
								ejl.getValue(i, "CS_ITEM_CD"), ejl.getValue(i,
								"CS_TOOLS"), ejl.getValue(i, "STD_ST"), ejl
								.getValue(i, "INPUT_TP"), ejl.getValue(i,
								"INPUT_TP1"), ejl.getValue(i, "INPUT_TP3"),
								ejl.getValue(i, "INPUT_TP7"), ejl.getValue(i,
								"INPUT_RMK"),
								ejl.getValue(i, "OVER_MONTH"), ejl.getValue(i,
								"MONTH_CHK"), ejl.getValue(i, "RMK"), ejl
								.getValue(i, "ISRT_USR_ID"), ejl.getValue(
								i, "ISRT_DT"), ejl.getValue(i,
								"UPDT_USR_ID"), ejl.getValue(i, "UPDT_DT"),
								ejl.getValue(i, "CS_LOW_NM"), ejl.getValue(i,
								"SMART_DESC"), ejl.getValue(i, "MNG_DESC"),
								ejl.getValue(i, "MONTH_CHK_IF"), ejl.getValue(i,
								"EL_INFO_MAP"), ejl.getValue(i, "DEF_VAL"),
								ejl.getValue(i, "DEF_VAL_ST"),
								ejl.getValue(i, "PRE_WORK_MM"),
								ejl.getValue(i, "PRE_INPUT_TP"),
								ejl.getValue(i, "HEADER_IF"),
								ejl.getValue(i, "INSP_METHOD")
								));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				int getCount = rawCount("TCSQ213");
				if (getCount == 0) {
					isInsert = true;
				}

				KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
						DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				ContentValues values = new ContentValues();

				publishProgress("max", Integer.toString(taskCnt));
				for (int i = 0; i < taskCnt; i++) {
					TableTCSQ213 tcsq213Data = new TableTCSQ213();
					// PartCheckListData partInsertData = new PartCheckListData();
					Field[] fields = tcsq213Data.getClass().getDeclaredFields();
					String name = "";
					String value = "";
					for (Field field : fields) {
						name = field.getName();
						try {
							value = field.get(tableTCSQ213Data.get(i)).toString();
						} catch (IllegalAccessException e) { // TODO Auto-generated
							// catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (NullPointerException e){
							e.printStackTrace();
							value = null;
						}
						if(value != null){
							values.put(name, value);
						}
					}

					if (isInsert) {
						db.insert("TCSQ213", null, values);
					} else {
						//전체데이터 다운로드 일 경우(기존데이터 업데이트)
						if(isAllDataDownload){
							String where = "CS_EMP_ID = '"
									+ values.getAsString("CS_EMP_ID")
									+ "' AND WORK_DT = '"
									+ values.getAsString("WORK_DT")
									+ "' AND JOB_NO = '" + values.getAsString("JOB_NO")
									+ "' AND NFC_PLC = '"
									+ values.getAsString("NFC_PLC") + "'"
									+ " AND CS_ITEM_CD = '"
									+ values.getAsString("CS_ITEM_CD") + "'";
							// 위의 웨어조건에 맞게 업데이트를 먼저 한다.
							// 업데이트 성공이면 1 혹은 그 이상이 될것이고 실패면 0을 리턴한다.
							// 그래서 udpateCount가 1보다 작으면 인서트를 친다.
							int updateCount = db.update("TCSQ213", values, where, null);
							Log.e("mastDataDownload",
									"masterdown3 TCSQ213 updateCount = " + updateCount);
							if (updateCount < 1) {
								long errCd = db.insert("TCSQ213", null, values); // errCd
								Log.i("mastDataDownload", "masterdown3 TCSQ213 errCd= " + errCd);
							}

						}else{
							boolean isNewJob = currentJobNoList.contains(values.getAsString("JOB_NO"));
							if(!isNewJob){
								long errCd = db.insert("TCSQ213", null, values); // errCd
								Log.i("mastDataDownload", "masterdown3 TCSQ213 errCd= " + errCd);
							}
						}

					}
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					publishProgress("progress", Integer.toString(i), "점검항목: " + workList.get(job).getJOB_NO() + "\n"
							+ workList.get(job).getBLDG_NM() + " "+ workList.get(job).getCAR_NO() +"\n"
							+ Integer.toString(i) + "번 진행중");
				}// 포문끝

				db.close();

			}
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
			new DatabaseDownLoadAsync5(context).execute(0);

		}
	}

}
