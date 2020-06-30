package com.jinsit.kmec.HM.MP;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.DeleteTable;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.DB.TableTCSQ210;
import com.jinsit.kmec.DB.WORK_TBL;
import com.jinsit.kmec.DB.MasterDataDownload.DatabaseDownLoadAsync5;
import com.jinsit.kmec.GK.LO.GK_LO00_R00;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.IR.NT.IR_NT00_R00;
import com.jinsit.kmec.IR.NT.IR_NT00_R01P;
import com.jinsit.kmec.IR.NT.NoticeResponseData;
import com.jinsit.kmec.WO.WO_WO00_R00F;
import com.jinsit.kmec.WO.WO_WT00_R00_ITEM00;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R00;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R01;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R02;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R03;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R04;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R04;
import com.jinsit.kmec.WO.WT.RJ.WT_RJ00_R00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00;
import com.jinsit.kmec.WO.WorkCD;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.DialogActivity;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.ScaledDensity;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

@SuppressLint("NewApi")
public class HM_MP00_R00 extends Activity implements OnClickListener,
		OnItemClickListener {
	// ---widget---///
	private TextView Tv_Notice, Tv_Job, Tv_Date, tv_noticeIn, tv_dateNext,
			tv_datePrev;
	private Button  btn_workStatus;
	private ImageView Btn_Supplies;
	private ListView lv_workTarget;
	// ---widget---///

	// ---variable---///
	private boolean isNaviToggle = false;
	private Date date;
	String strDate;

	private ProgressDialog ProgressDialog;
	private EasyJsonList ejl;
	private EasyJsonList ejl02;
	private NoticeTask noticeTask;
	private static Activity activity = null;
	Context context;

	ArrayList<NoticeResponseData> rlid;
	ArrayList<WO_WT00_R00_ITEM00> workListData;
	ArrayList<TableTCSQ210> tableTCSQ210Data;

	private HomeWorkListAdapter homeWorkListAdapter = null;
	private HomeWorkListAdapter_Local homeWorkListAdapter_local = null;

	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	//in order for 출퇴근
	private Map nextPopMap;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;
	private CommonSession session;
	private TextView tv_onWork;
	private TextView tv_offWork;

	private Timer timer;
	private TimerTask timerTask;
	private int noticePosition = 0;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private boolean isSynchronized = false;
	private boolean isLocalDb = false;

	public static Activity getActivity(){
		return activity;
	}

	private void initialize() {
		// TODO Auto-generated method stub

		isLocalDb = NetworkStates.isNetworkStatus(activity);
		if(isLocalDb)
		{
			new WorkTargetAsync().execute();
		}
		else
		{
			Toast.makeText(activity, "인터넷 연결이 안되어있습니다.", 2000).show();

			String query = new DatabaseRawQuery().selectJobList(session.getEmpId(),strDate);
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고
			mCursor.moveToFirst();
			homeWorkListAdapter_local =
					new HomeWorkListAdapter_Local(activity,
							R.layout.listitem_home_joblist, mCursor, new String[] {
							"WORK_NM", "ST", "BLDG_NM" },
							null, 0);
			lv_workTarget.setAdapter(homeWorkListAdapter_local);
			db.close();
			dbHelper.close();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mNfcAdapter != null)
			enableTagWriteMode();
		naviPref = new HomeNaviPreference(context);
		navigationInit();

		Log.e("synchroniszied" , "Sync?? = " + isSynchronized);
		if(isSynchronized){
			isSynchronized=false;
			//upLoadDatabase();
			initialize();
		}else{
			initialize();
			//new WorkTargetAsync().execute();
		}
		Log.w("synchroniszied" , "Sync?? = " + isSynchronized);


	}

	@Override
	protected void onRestart() {
		super.onRestart();

		Log.e("onRestart", "onRestart  =");
	};
	@Override
	protected void onPause() {
		super.onPause();
		if (mNfcAdapter != null)
			disableTagWriteMode();
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
			if (!tagUid.isEmpty()) {
				//new OfficeNfcCheckAsync().execute();
			} else {
				AlertView.showAlert("알림. 빈 태그입니다.", context);
			}
		}
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
	void nfcInit(){
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		android.app.ActionBar aBar = getActionBar();
		//JActionbar aBar = new JActionbar(this);
		aBar.setTitle("홈");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		/*LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.jactionbar, null);
		TextView titleTextView = (TextView)mCustomView.findViewById(R.id.titleTextView);
		TextView versionTextView = (TextView)mCustomView.findViewById(R.id.versionTextView);
		versionTextView.setText(ApplicationInfo.getVersionLogin(this));
		aBar.setCustomView(mCustomView);
		aBar.setDisplayShowCustomEnabled(true);*/

		activityInit();
		nfcInit();
		setConfig();
	}


	private void setConfig(){

		//ActivityAdmin.getInstance().finishAllActivities();
		ActivityAdmin.getInstance().finishLastMenuActivites();
		//ActivityAdmin.getInstance().addMenuActivity(this);
		//ActivityAdmin.getInstance().addActivity(this);
	}


	private HomeNaviPreference naviPref;

	private void setToggleNavi(){
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int totalHeight = ScreenUtil.getRealScreenHeight(this);
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
		float hardHeight = ScaledDensity.spToPixels(context, (float)100);
		int viewArea = naviHeight/6 ;
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
		/*Button navi = (Button) homeNavi.getBtn_naviHOME();
		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviPref.setHide(!naviPref.isHide());
				homeNavi.setToggleNavi();
			}
		});*/
	}
	/**
	 * 강제로그아웃
	 */
	private void forceLogout(){
		Intent intent = new Intent(this.context, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("ACTION.LoginStatusService");
		startActivity(intent);
	}
	private void activityInit() {
		// TODO Auto-generated method stub
		activity = this;
		context = this;


		nextPopMap = new HashMap();
		session = new CommonSession(context);
		//강제로그아웃 되었는지 체크한다. 스플래쉬 화면에서는 체크후 2초 딜레이때문에 제대로 체크못함
		//여기서 한번더 체크하여 강제로그아웃 시킨다.
		if(!session.isLoggined()){
			this.forceLogout();
			this.finish();
			return;
		}
		openDatabase();// DB Create or Open

		Btn_Supplies = (ImageView) findViewById(R.id.btn_supplies);
		Btn_Supplies.setOnClickListener(this);
		btn_workStatus = (Button) findViewById(R.id.btn_workStatus);
		btn_workStatus.setOnClickListener(this);

		Tv_Notice = (TextView) findViewById(R.id.tv_notice);
		Tv_Notice.setOnClickListener(this);
		tv_noticeIn = (TextView) findViewById(R.id.tv_noticeIn);
		tv_noticeIn.setOnClickListener(this);
		Tv_Date = (TextView) findViewById(R.id.tv_date);
		Tv_Date.setOnClickListener(this);
		Tv_Job = (TextView) findViewById(R.id.tv_joblist);
		Tv_Job.setOnClickListener(this);
		tv_dateNext = (TextView) findViewById(R.id.tv_dateNext);
		tv_dateNext.setOnClickListener(this);
		tv_datePrev = (TextView) findViewById(R.id.tv_datePrev);
		tv_datePrev.setOnClickListener(this);
		lv_workTarget = (ListView) findViewById(R.id.lv_joblist);
		lv_workTarget.setOnItemClickListener(this);

		// strDate = DateUtil.nowDate().toString();
		strDate = session.getWorkDt();
		Tv_Date.setText(strDate);

		new NoticeTask().execute();

		//[nextPopMap] finally declared keys
		nextPopMap.put("workDt","");
		tv_onWork = (TextView) findViewById(R.id.tv_onWork);
		tv_onWork.setOnClickListener(this);
		tv_offWork = (TextView)findViewById(R.id.tv_offWork);
		tv_offWork.setOnClickListener(this);

		setEmpAttendance();
		this.startBackService(context);
	}
	private class OfficeNfcCheckAsync extends AsyncTask<Void, String, Void> {
		EasyJsonMap ejmMsg, ejmData;

		@Override
		protected void onPostExecute(Void result) {
			//HM_MP00_R00.this.ProgressDialog.dismiss();
			try {
				boolean isError =  ejmMsg.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					String rtn = ejmData.getValue("RTN");
					if(rtn.equals("0")){
						alert("등록된 NFC TAG가 없습니다..", context);

					}else if(rtn.equals("1")){
						//사무실 태그

						Log.v("ret1", "사무실태그");
						updateEmpAttendance();
					}else if(rtn.equals("2")){
						//회의실 태그

						alert("회의실 NFC입니다.", context);
					}else if(rtn.equals("3")){
						alert("정기점검용 건물 NFC입니다.", context);
					}

				}else if(isError){
					AlertView.showAlert(ejmMsg.getValue("errMsg"),context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			HM_MP00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
//					HM_MP00_R00.this, "조회중", "태그를 조회중입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				CommonSession cs = new CommonSession(context);
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "sm/checkUserOfficeAttendance.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
				arguments.add(new BasicNameValuePair("nfcTag", tagUid));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejmMsg = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
					ejmData = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

	}
	private class WorkTargetAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {
			//HM_MP00_R00.this.ProgressDialog.dismiss();
			if(progressWorking)progress(false);
			homeWorkListAdapter = new HomeWorkListAdapter(activity,
					workListData);
			lv_workTarget.setAdapter(homeWorkListAdapter);
			if(NetworkStates.isNetworkStatus(context));//new EmpSafeStatusAsync().execute();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!progressWorking)progress(true);
			//HM_MP00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			//HM_MP00_R00.this, "작업대상", "불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				workListData = new ArrayList<WO_WT00_R00_ITEM00>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/selectWorkTarget.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", "1"));
				arguments.add(new BasicNameValuePair("csEmpId", session.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", strDate));
				System.out.println("!-- Log --req");
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				System.out.println("!-- Log --!8798798798797899897979");
				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					workListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						workListData.add(new WO_WT00_R00_ITEM00(ejl.getValue(i,
								"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"), ejl
								.getValue(i, "JOB_NO"), ejl.getValue(i,	"WORK_CD"), ejl.getValue(i,	"WORK_NM"), ejl.getValue(i, "ST"), ejl
								.getValue(i, "CS_DT"),
								ejl.getValue(i, "CS_FR"), ejl.getValue(i,
								"BLDG_NM"), ejl.getValue(i, "CAR_NO"),
								ejl.getValue(i, "RESERV_ST"), ejl.getValue(i,
								"Y_CNT"), ejl.getValue(i, "T_CNT")));
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

	public class NoticeTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				rlid = new ArrayList<NoticeResponseData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "hm/selectNoticeList.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("noticeDt", strDate));
				arguments.add(new BasicNameValuePair("deptCd", session.getDeptCd()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {

					ejl02 = new EasyJsonList(returnJson.getJSONArray("dataList"));
					rlid.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						rlid.add(new NoticeResponseData(ejl02.getValue(i,
								"NOTICE_DT"), ejl02.getValue(i, "NOTICE_TM"), ejl02
								.getValue(i, "TITLE"), ejl02.getValue(i,
								"SENDER_NM"), ejl02.getValue(i, "RECIPIENT_NM"),
								ejl02.getValue(i, "CONTENT")));
					}

				} catch (JSONException e) {
					e.printStackTrace();

				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (rlid.size() == 0) {
				if(NetworkStates.isNetworkStatus(context)){
					Tv_Notice.setText("공지사항이 없습니다.");
				}else{
					Tv_Notice.setText("오프라인 상태입니다. 네트워크를 확인해주세요.");
				}
			} else {
				String str = rlid.get(0).getTITLE();
				//Tv_Notice.setText(str);


				int length = ejl02.getLength();
				String str2 = "";
				for(int i=0; i<length; i++){
					try {
						str2 += ejl02.getValue(i, "TITLE");
						str2 += "                 ";
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				rollingNoticeList();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		// TODO Auto-generated method stub

		if(isLocalDb)
		{
			String workCd = workListData.get(position).getWORK_CD();
			String nowWorkDt = workListData.get(position).getWORK_DT();
			//미쓰비시 조성원대리가 요청 홈화면에서 들어갈 수 있도록 20170123
			//if(nowWorkDt.equals(session.getWorkDt()))	//오늘 작업인지 비교
			//{
			if(workListData.get(position).getWORK_CD().equals(WorkCD.정기점검)){
				if(masterDataCheck(workListData.get(position).getCS_EMP_ID(),workListData.get(position).getJOB_NO(),workListData.get(position).getWORK_DT())){
					isSynchronized = true; ///정기점검으로 이동한 후 서버와 동기화를 하기위해 트루로 한다.
					Intent intent = new Intent(activity,WO_WT00_R01.class);
					intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
					intent.putExtra("workDt",workListData.get(position).getWORK_DT());

					updateWorkDt=workListData.get(position).getWORK_DT();
					updateJobNo=workListData.get(position).getJOB_NO();
					activity.startActivity(intent);
				}
				else
				{
					SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(activity,
							"정기점검 데이터를 다운로드 하지 않았습니다. 다운로드 화면으로 이동하겠습니까?",
							new btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									Intent intent = new Intent(activity, MasterDataDownload.class);
									startActivity(intent);
								}
							});
					ynDialog.show();
				}

			}
			else if(workListData.get(position).getWORK_CD().equals(WorkCD.고장수리)){
				Intent intent = new Intent(activity,WO_TS00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workListData.get(position).getWORK_CD().equals(WorkCD.수리공사)
					|workCd.equals(WorkCD.유상부품교체)
					|workCd.equals(WorkCD.무상부품교체)
					|workCd.equals(WorkCD.Claim)
					|workCd.equals(WorkCD.특수작업)){
				Intent intent = new Intent(activity,WT_RJ00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workCd.equals(WorkCD.상주근무)
					|workCd.equals(WorkCD.저녁식사)
					|workCd.equals(WorkCD.저녁간식)
					|workCd.equals(WorkCD.점심식사)
					|workCd.equals(WorkCD.철야식사)
					|workCd.equals(WorkCD.고장대기)
					|workCd.equals(WorkCD.작업준비)
					|workCd.equals(WorkCD.자택복귀)){
				Intent intent = new Intent(activity,WT_MW00_R01.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workCd.equals(WorkCD.정기점검지원)
					|workCd.equals(WorkCD.고장수리지원)
					|workCd.equals(WorkCD.점검대기)
					|workCd.equals(WorkCD.신설대기)
					|workCd.equals(WorkCD.호텔VIP대기)
					|workCd.equals(WorkCD.고객상담)
					|workCd.equals(WorkCD.고객서비스)
					|workCd.equals(WorkCD.타부서지원)
					|workCd.equals(WorkCD.특별점검)
					|workCd.equals(WorkCD.보수이체검사)){
				Intent intent = new Intent(activity,WT_MW00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workCd.equals(WorkCD.정기검사입회)){
				Intent intent = new Intent(activity,WT_MW00_R02.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workCd.equals(WorkCD.회의교육)
					|workCd.equals(WorkCD.사무정리)){
				Intent intent = new Intent(activity,WT_MW00_R03.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
			else if(workCd.equals(WorkCD.사무실복귀)){
				Intent intent = new Intent(activity,WT_MW00_R04.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);
			}
//			}
//			else
//			{
//				Toast.makeText(context, "금일 작업만 선택하세요.", 2000).show();
//			}


			//여기는 로컬 디비
		}else{
			Cursor cursor = homeWorkListAdapter_local.getCursor();
			Intent intent = new Intent(activity, WO_WT00_R01.class);
			intent.putExtra("jobNo",
					cursor.getString(cursor.getColumnIndex("JOB_NO")));
			intent.putExtra("workDt",
					cursor.getString(cursor.getColumnIndex("WORK_DT")));
			startActivity(intent);
		}


	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.tv_notice:
				// 공지사항 항목 클릭시 공지사항 팝업
				if (rlid.size() != 0) {
					NoticeResponseData mData = rlid.get(noticePosition-1);
					IR_NT00_R01P NTD = new IR_NT00_R01P(context, mData);
					NTD.show();
					NTD.sch();
				}
				break;

			//출퇴근
			case R.id.btn_workStatus:
				updateEmpAttendance();
				break;
			case R.id.tv_onWork:
				if(session.isAttended() == true)//출근상태
				{
					alert("현재 출근상태 입니다.", context);
				}else
				{
					updateEmpAttendance();
				}
				break;
			case R.id.tv_offWork:
				if(session.isAttended() == true)//출근상태
				{
					updateEmpAttendance();
				}else
				{
					alert("현재 퇴근상태 입니다.", context);
				}
				break;

			case R.id.tv_joblist:
				// 작업대상목록 text 클릭 시 [작업대상목록]화면으로 이동
				Intent intent = new Intent(this, WO_WO00_R00F.class);
				startActivity(intent);

				break;
			case R.id.tv_noticeIn:
				Intent i = new Intent(activity, IR_NT00_R00.class);
				startActivity(i);
				break;
			case R.id.tv_dateNext:
				nextDate();
				break;
			case R.id.tv_datePrev:
				prevDate();
				break;
			case R.id.tv_date:
				getCalendar(0);
				break;
			//준비물
			case R.id.btn_supplies:
				//safeDrivingNotice();
				nextPopMap.put("workDt", Tv_Date.getText().toString());
				HM_MP00_R01P d01 = new HM_MP00_R01P(context, nextPopMap);
				d01.show();

				/*Intent intentt = new Intent(activity, WO_WT00_R04.class);
				intentt.putExtra("empId", session.getEmpId());
				intentt.putExtra("workDt",session.getWorkDt());
				intentt.putExtra("jobNo", "902");
				startActivity(intentt);*/
				break;
		}
	}
	private void deleteTable(){
		DeleteTable deleteTable = new DeleteTable(context);
		deleteTable.deleteTable(strDate);

	}


	private void prevDate() {
		// TODO Auto-generated method stub
		Date date = getDate(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		strDate = DateUtil.getFormat(0).format(cal.getTime());
		Tv_Date.setText(DateUtil.getFormat(0).format(cal.getTime()));
		initialize();
		// new WorkTargetAsync().execute();
	}

	private void nextDate() {
		// TODO Auto-generated method stub
		Date date = getDate(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		strDate = DateUtil.getFormat(0).format(cal.getTime());
		Tv_Date.setText(DateUtil.getFormat(0).format(cal.getTime()));
		initialize();
		//new WorkTargetAsync().execute();
	}

	private Date getDate(String strDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
				java.util.Locale.getDefault());
		Date date = null;
		try {
			date = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;

	}



	private void getCalendar(int fromTo) {

		Intent intent = new Intent(HM_MP00_R00.this,
				com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);

	}
	/**
	 * 출퇴근시 NFC태그 하는화면으로 이동
	 */
	private void commuteNfcTag(String commute){
		Intent intent = new Intent(activity, AttendanceTagActivity.class);
		intent.putExtra("attendance", commute);
		startActivityForResult(intent, 3);
	}

	/**
	 * 출퇴근 NFC Activity에서 받아온 결과를 가지고 GPS처리, 출근 안전관리 팝업 등등 처리를 한다.
	 * @param bundle
	 */
	private void commuteResultProcess(Bundle bundle){
		String commute = bundle.getString("commute");
		String rtnCode = bundle.getString("rtnCode");
		String commuteTime = bundle.getString("commuteTime");

		if(commute.equals("출근")){

			if(rtnCode.equals("1")){
				alert(this.session.getEmpNm() + "님 " + commuteTime  +  " 출근 처리되었습니다.", context);
				//Toast.makeText(context, "출근하셨습니다.", 2000).show();
				btn_workStatus.setBackgroundResource(R.drawable.btn_onwork_icon);
				session.setIsAttended(true);

				//출근시 로컬 디비테이블을 지움 3일전 and 업로드 된것만
				deleteTable();
				CallService.startGPSService(context);

				this.safeDrivingNotice();//출근하고 안전운전 공지 띄우기

			}else if(rtnCode.equals("0")){
				alert("출근 미처리입니다.", context);
			}else if(rtnCode.equals("2")){
				alert("오늘의 출퇴근을 모두 하셨습니다.", context);
			}else if(rtnCode.equals("5")){
				//출근이 되면서 오늘 정기점검 항목이 있으므로 데이터다운로드 하라고 한다.
				CallService.startGPSService(context);
				SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
						this.session.getEmpNm() + "님 " + commuteTime +
								" 출근 처리되었습니다. 오늘 정기점검 항목이 있습니다. 다운로드 하시겠습니까?",
						new btnClickListener() {
							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(context, MasterDataDownload.class);
								startActivity(intent);
							}
						});
				ynDialog.show();
				//tv_onWork.setText("출근 ");
//				Drawable img = getResources().getDrawable(R.drawable.btn_onwork_icon);
//				btn_workStatus.setBackground(img);
				btn_workStatus.setBackgroundResource(R.drawable.btn_onwork_icon);
				session.setIsAttended(true);

				//출근시 로컬 디비테이블을 지움 3일전 and 업로드 된것만
				deleteTable();
				this.safeDrivingNotice();//출근하고 안전운전 공지 띄우기
			}


		}else{//퇴근일 경우

			if(rtnCode.equals("1")){

				alert(this.session.getEmpNm() + "님 " + commuteTime +  " 퇴근 처리되었습니다.", context);
				Toast.makeText(context, this.session.getEmpNm() + "님 " +commuteTime +
						" 퇴근 처리되었습니다." , 2000).show();
				//tv_onWork.setText("퇴근 ");
//				Drawable img = getResources().getDrawable(R.drawable.btn_offwork_icon);
//				btn_workStatus.setBackground(img);
				btn_workStatus.setBackgroundResource(R.drawable.btn_offwork_icon);
				session.setIsAttended(false);
				//20150126 퇴근후에 로그아웃 시킨다.
				CallService.startGPSService(context);
				attendAndLogout();
			}else if(rtnCode.equals("0")){
				alert("퇴근 미처리입니다.", context);
			}else if(rtnCode.equals("2")){
				alert("이미 퇴근상태입니다.", context);
			}else if(rtnCode.equals("3")){
				//퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
				alert("완료 안된 작업이 있습니다. 확인해 주세요.", context);
			}

		}

	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					strDate = bundle.getString("dateSelected");
					Tv_Date.setText(strDate);

					break;
				}
				break;

			case 3:
				if(resultCode == RESULT_OK)
				{
					Bundle bundle = data.getExtras();
					this.commuteResultProcess(bundle);
					break;
				}

				break;


		}


	}

	private void openDatabase() {
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.close();

	}

	//a class which accesses to database
	private class Database extends AsyncTask<String, Integer, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress(true);
		}
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress(false);
			setData(result);
		}
	}//end of Database class
	private void crud(String div){

		if(div.equals("getToWork")){

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId"  , session.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt" , session.getWorkDt()));

			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}else if(div.equals("getOffWork")){

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId"  , session.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt" , session.getWorkDt()));

			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}else if(div.equals("checkUserAttendance")){

			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId"  , session.getEmpId() ));
			arguments.add(new BasicNameValuePair("workDt" , session.getWorkDt() ));
			JSONObject returnJson = http.getPost(argUrl, arguments, true);
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}//end of crud()
	private void setData(String div){

		if(div.equals("getToWork")){

			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					String rtn = ejm02.getValue("RTN");
					if(rtn.equals("1")){
						alert("출근하셨습니다.", context);
						//tv_onWork.setText("출근 ");
//						Drawable img = getResources().getDrawable(R.drawable.btn_onwork_icon);
//						btn_workStatus.setBackground(img);
						btn_workStatus.setBackgroundResource(R.drawable.btn_onwork_icon);
						session.setIsAttended(true);

						//출근시 로컬 디비테이블을 지움 3일전 and 업로드 된것만
						deleteTable();
						CallService.startGPSService(context);

						this.safeDrivingNotice();//출근하고 안전운전 공지 띄우기

					}else if(rtn.equals("0")){
						alert("출근 미처리입니다.", context);
					}else if(rtn.equals("2")){
						alert("오늘의 출퇴근을 모두 하셨습니다.", context);
					}else if(rtn.equals("5")){
						//출근이 되면서 오늘 정기점검 항목이 있으므로 데이터다운로드 하라고 한다.
						CallService.startGPSService(context);
						SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
								"출근되었습니다. 오늘 정기점검 항목이 있습니다. 다운로드 하시겠습니까?",
								new btnClickListener() {
									@Override
									public void onButtonClick() {
										// TODO Auto-generated method stub
										Intent intent = new Intent(context, MasterDataDownload.class);
										startActivity(intent);
									}
								});
						ynDialog.show();
						//tv_onWork.setText("출근 ");
//						Drawable img = getResources().getDrawable(R.drawable.btn_onwork_icon);
//						btn_workStatus.setBackground(img);
						btn_workStatus.setBackgroundResource(R.drawable.btn_onwork_icon);
						session.setIsAttended(true);

						//출근시 로컬 디비테이블을 지움 3일전 and 업로드 된것만
						deleteTable();

						this.safeDrivingNotice();//출근하고 안전운전 공지 띄우기
					}

				}else if(isError){
					AlertView.showAlert(ejm01.getValue("errMsg"),context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else if(div.equals("getOffWork")){

			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){

					String rtn = ejm02.getValue("RTN");
					if(rtn.equals("1")){
						//alert("퇴근하셨습니다.", context);
						Toast.makeText(context, "퇴근 되었습니다.", 2000).show();
						//tv_onWork.setText("퇴근 ");
//						Drawable img = getResources().getDrawable(R.drawable.btn_offwork_icon);
//						btn_workStatus.setBackground(img);
						btn_workStatus.setBackgroundResource(R.drawable.btn_offwork_icon);
						session.setIsAttended(false);
						//20150126 퇴근후에 로그아웃 시킨다.
						CallService.startGPSService(context);
						attendAndLogout();
					}else if(rtn.equals("0")){
						alert("퇴근 미처리입니다.", context);
					}else if(rtn.equals("2")){
						alert("이미 퇴근상태입니다.", context);
					}else if(rtn.equals("3")){
						//퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
						alert("완료 안된 작업이 있습니다. 확인해 주세요.", context);
					}

				}else if(isError){

					AlertView.showAlert(ejm01.getValue("errMsg"),context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else if(div.equals("checkUserAttendance")){
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					String result = ejm02.getValue("RTN");
					if(result.equals("1")){
						//tv_onWork.setText("출근 ");
//						Drawable img = getResources().getDrawable(R.drawable.btn_onwork_icon);
//						btn_workStatus.setBackground(img);
						btn_workStatus.setBackgroundResource(R.drawable.btn_onwork_icon);
						session.setIsAttended(true);
					}else if(result.equals("0") || result.equals("3")){
						//tv_onWork.setText("퇴근 ");
//						Drawable img = getResources().getDrawable(R.drawable.btn_offwork_icon);
//						btn_workStatus.setBackground(img);
						btn_workStatus.setBackgroundResource(R.drawable.btn_offwork_icon);
						session.setIsAttended(false);
					}
				}else if(isError){
					AlertView.showAlert(ejm01.getValue("errMsg"),context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}//end of setData()
	private void setEmpAttendance(){
		new Database().execute("checkUserAttendance");
	}
	private void updateEmpAttendance(){
		///gps활성화 체크
		GPSService gs = new GPSService(context);
		if (!gs.isGetLocation()) {
			gpsActivation();
			return;
		}

		if(session.isAttended()){
			//공주임이 빼달라고 함 20150407
//			int uploadCount =masterDataUpLoadCheck();
//			if(uploadCount!=0){
//				//--퇴근하기전에 업로드 안된게 있는지 체크합니다.
//				AlertView.confirm(context, "경고", "오늘 점검하신 " +uploadCount + "개 항목이 업로드 되지 않았습니다. 업로드"
//						+ " 하신 후 퇴근해 주세요.", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						Intent intent = new Intent(activity, MasterDataDownload.class);
//						startActivity(intent);
//					}
//				});
//				return;
//			}
			//---여기까지 추가 쏘스
			AlertView.confirm(context, "알림", "퇴근을 하시면 '홈콜'외에는 작업을 할 수 없습니다. 그래도 퇴근을 하시겠습니까?", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//new Database().execute("getOffWork");
					commuteNfcTag("퇴근");
				}
			});
		}else if(!session.isAttended()){
			AlertView.confirm(context, "알림", "출근을 하시겠습니까?", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//new Database().execute("getToWork");
					commuteNfcTag("출근");
				}
			});
		}
	}
	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}




	private void rollingNoticeList(){
		final int length = ejl02.getLength();
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run(){
				activity.runOnUiThread(new Runnable(){
					public void run(){
						if( noticePosition == length){ noticePosition = 0; }
						try {
							ejl02.getValue(noticePosition, "TITLE");
							Tv_Notice.setText( ejl02.getValue(noticePosition, "TITLE") );
						} catch (JSONException e) {
							e.printStackTrace();
						}noticePosition++;
					}
				});
			}
		};
		timer.schedule(timerTask, 1000, 5000);
	}


	private boolean masterDataCheck(String csEmpId, String jobNo, String workDt){
		boolean isCount = true;

		int count = 0;
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor c = db.rawQuery("SELECT count() from WORK_TBL where CS_EMP_ID = '" + csEmpId + "' and job_no ='" + jobNo + "' and"
				+ " work_dt = '" + workDt  +"'", null);
		c.moveToFirst();
		String getC = c.getString(0);
		c.close();
		db.close();
		dbHelper.close();
		count = Integer.valueOf(getC);
		if(count==0){
			isCount = false;
		}
		return isCount;
	}

	private int masterDataUpLoadCheck(){
		int count = 0;

		String query = "SELECT count() FROM WORK_TBL "
				+ " WHERE WORK_DT = '" + session.getWorkDt() + "' AND UPLOAD_YN = 'N' ";

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor mCursor = db.rawQuery(query,
				null); // 쿼리 날리고
		mCursor.moveToFirst();
		String getC = mCursor.getString(0);
		mCursor.close();
		db.close();
		dbHelper.close();
		count = Integer.valueOf(getC);
		return count;
	}
	private String upQ030, upQ210, upQ213;
	private String updateWorkDt, updateJobNo;
	private void upLoadDatabase() {
		// TODO Auto-generated method stub

		StringBuffer sb = updateWorkTable();
		upQ030= sb.toString();
		StringBuffer sb2 = update210Table();
		upQ210= sb2.toString();
		StringBuffer sb3 = update213Table();
		upQ213 = sb3.toString();

		new UploadDatabaseAsync().execute();
	}

	StringBuffer updateWorkTable(){

		StringBuffer inputParams = new StringBuffer();
		String query = "SELECT A.CS_EMP_ID, A.WORK_DT, A.JOB_NO, "
				+ " A.CS_FR, '', A.JOB_ST, '', A.MOVE_TM, A.ARRIVE_TM, A.COMPLETE_TM, A.CS_EMP_ID, "
				+ " B.CS_TP, B.SELCHK_USID, B.SELCHK_USID_NM, B.SUB_SELCHK_USID, B.SUB_SELCHK_USID_NM "
				+ " FROM WORK_TBL A "
				+ " LEFT JOIN TCSQ030 B ON A.CS_EMP_ID = B.CS_EMP_ID AND A.WORK_DT = B.WORK_DT AND A.JOB_NO = B.JOB_NO "
				+ " WHERE A.WORK_DT = '" + updateWorkDt + "' AND A.JOB_NO = '" +updateJobNo +  "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
				null); // 쿼리 날리고
		mCursor.moveToFirst();
		if(mCursor !=null&&mCursor.getCount()!=0){

			do{
				for(int i=0;i<mCursor.getColumnCount();i++){

					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
			}while(mCursor.moveToNext());
		}
		return inputParams;


	}


	StringBuffer update210Table(){
		//value = mCursor.getString(mCursor.getColumnIndex("CS_EMP_ID"));


		StringBuffer inputParams = new StringBuffer();

		String query = "SELECT CS_EMP_ID, WORK_DT, JOB_NO,NFC_PLC,CS_TM_FR, CS_TM_TO,JOB_ST,CS_EMP_ID  FROM TCSQ210 "
				+ " WHERE WORK_DT = '" + updateWorkDt + "' AND JOB_NO = '" +updateJobNo +  "'";

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
				null); // 쿼리 날리고
		mCursor.moveToFirst();

		if(mCursor !=null&&mCursor.getCount()!=0){

			do{
				for(int i=0;i<mCursor.getColumnCount();i++){

					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}

			}while(mCursor.moveToNext());
		}
		return inputParams;

	}

	StringBuffer update213Table() {
		StringBuffer inputParams = new StringBuffer();

		String query = "select CS_EMP_ID,WORK_DT,JOB_NO,NFC_PLC,CS_ITEM_CD,INPUT_TP,"
				+ "INPUT_TP1,INPUT_TP3,INPUT_TP7,OVER_MONTH,MONTH_CHK,MONTH_CHK_IF,INPUT_RMK,DEF_VAL_ST,CS_EMP_ID from TCSQ213 "
				+ " WHERE WORK_DT = '" + updateWorkDt + "' AND JOB_NO = '" +updateJobNo +  "'";

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
				null); // 쿼리 날리고
		mCursor.moveToFirst();

		if(mCursor !=null&&mCursor.getCount()!=0){

			do{
				for(int i=0;i<mCursor.getColumnCount();i++){

					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
			}while(mCursor.moveToNext());
		}
		return inputParams;
	}



	private class UploadDatabaseAsync extends AsyncTask<Void, String, Boolean> {
		private String updateRet = "";
		private EasyJsonMap  msgMap;

		private String exceptionMsg;

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(progressWorking)progress(false);

			if(result){
				try {
					boolean isError = msgMap.getValue("errCd").equals("0") ? true:false;//0이면 정상
					if(isError){

						if(updateRet.equals("1")){
							//정상 동기화 정상동장
							initialize();
							stringInit();

						}else{
							//실패 : 점검항목 생성 X
							Toast.makeText(activity, "동기화를 실패하였습니다..", 2000).show();

						}

					}else{
						alert(msgMap.getValue("errMsg"), context);
					}


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
				if(exceptionMsg != null){
					String errMsg = "";

					try {
						if(msgMap != null) errMsg = msgMap.getValue("errMsg");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					alert(exceptionMsg + errMsg, context);
				}
			}

		/*	if (updateRet.equals("1")) {
				// 동기화가 정상적으로 됐을 경우 이니셜라이즈
				initialize();
				stringInit();
			} else {
				Toast.makeText(activity, "동기화를 실패하였습니다..", 2000).show();
			}*/
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(!progressWorking)progress(true);
			//HM_MP00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			//activity,"동기화","서버와 동기화 중입니다...");
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/dbUploadTBL.do";	//테스트끝나면 바꾸라
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("upQ030", upQ030));
				arguments.add(new BasicNameValuePair("upQ210", upQ210));
				arguments.add(new BasicNameValuePair("upQ213", upQ213));


				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
					updateRet = returnJson.getString("dataString");
					Log.v("updateRet", "UpdateRet = " + updateRet);


				} catch (JSONException e) {
					e.printStackTrace();
					exceptionMsg = e.toString();
					return false;
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				exceptionMsg = ex.toString();
				return false;
			}
			return true;

		}
	}

	private void stringInit(){
		upQ030 = "";
		upQ210 ="";
		upQ213 ="";
	}


	private boolean progressWorking = false;
	private void progress(Boolean isActivated) {
		progressWorking = isActivated;
		if (isActivated) {
			HM_MP00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");

		} else {
			HM_MP00_R00.this.ProgressDialog.dismiss();
		}
	}

	private void attendAndLogout(){
		GK_LO00_R00 logout = new GK_LO00_R00(context);
		logout.attendAndLogout();
	}



	private class EmpSafeStatusAsync extends AsyncTask<Void, String, Boolean> {

		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			//if(progressWorking)progress(false);

			try {
				boolean isError = msgMap.getValue("errCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						if (dataMap.getValue("RETURN_TP").equals("N")) {

						} else if (dataMap.getValue("RETURN_TP").equals("Y")) {
							employeeSave();
						}

					}else{
					}
				}else if(isError){
					alert(msgMap.getValue("errMsg"), context);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//if(!progressWorking)progress(true);

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "hm/searchEmpSafeStatus.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", session.getEmpId()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					dataMap = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return true;

		}
	}

	private class EmpSafeStatusUpdateAsync extends AsyncTask<Void, String, Boolean> {

		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			//if(progressWorking)progress(false);
			//HM_MP00_R00.this.ProgressDialog.dismiss();
			try {
				boolean isError = msgMap.getValue("errCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						if (dataMap.getValue("RTN").equals("1")) {
							//정상업데이트
							Toast.makeText(context, "갱신 되었습니다.", 2000).show();
						} else if (dataMap.getValue("RTN").equals("0")) {
							//업데이트 안됨
							Toast.makeText(context, "갱신을 실패하였습니다.", 2000).show();
						}
					}else{
					}
				}else if(isError){
					alert(msgMap.getValue("errMsg"), context);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//if(!progressWorking)progress(true);

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "hm/updateEmpStatus.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", session.getEmpId()));

				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					dataMap = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return true;

		}
	}

	private void employeeSave(){
		SimpleYesNoDialog ynd = new SimpleYesNoDialog(context, "작업자 안전관리를 위해 작업자의 상태를 갱신하시겠습니까?",
				new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new EmpSafeStatusUpdateAsync().execute();
					}
				});
		ynd.show();
	}

	@Override
	public void onBackPressed() {
		this.openDialog();
	}

	private void openDialog() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, GK_LO00_R00.class);
		startActivity(intent);
	}

	void gpsActivation() {
		new AlertDialog.Builder(context)
				.setTitle("위치서비스 동의")
				.setMessage("GPS를 켜지 않으면 작업을 시작할 수 없습니다.")
				.setNeutralButton("이동",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								context.startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				})
				.show();
	}

	private void startBackService(Context context){
		CallService.startBackService(context);
	}

	public class WorkTableCheck extends AsyncTask<Integer// excute()실행시
			// 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Boolean// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private Context mContext;
		private String jobNo;
		private String workDt;

		public WorkTableCheck(Context context, String jobNo, String workDt) {
			mContext = context;
			this.jobNo = jobNo;
			this.workDt = workDt;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@SuppressLint("NewApi")
		@Override
		protected Boolean doInBackground(Integer... params) {

			boolean isReturn = false;

			ArrayList<WORK_TBL> tableWORK_TBLData = new ArrayList<WORK_TBL>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/dbDownloadWORK_TBL.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("csEmpId", session.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", this.workDt));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				tableWORK_TBLData.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();

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

			if(tableWORK_TBLData.size() > 0){
				//인서트할게 0보타 크다
				for(WORK_TBL workTbl : tableWORK_TBLData){
					if(workTbl.getWORK_DT().equals(this.workDt) &&
							workTbl.getJOB_NO().equals(this.jobNo)){

						return true;
						//이게 있으면 다운로드 하러가고
					}
				}

			}else{

			}
			return isReturn;
		}



		// onPostExecute() 함수는 doInBackground() 함수가 종료되면 실행됨
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(activity,
						"정기점검 데이터를 다운로드 하지 않았습니다. 다운로드 화면으로 이동하겠습니까?",
						new btnClickListener() {

							@Override
							public void onButtonClick() {
								// TODO Auto-generated method stub
								Intent intent = new Intent(activity, MasterDataDownload.class);
								startActivity(intent);
							}
						});
				ynDialog.show();
			}else{
				alert("다운로드 할 점검 항목이 일치하지 않습니다. 관리자에게 문의 하시기 바랍니다.", mContext);
			}
		}
	}


	private void safeDrivingNotice(){
		HM_MP00_R02P d01 = new HM_MP00_R02P(context);
		d01.show();
	}

};