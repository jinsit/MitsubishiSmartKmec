package com.jinsit.kmec.SM.GM;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.SearchDeptResData;
import com.jinsit.kmec.CM.SelectDept;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.DeviceUniqNumber;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.ByteLengthFilter;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class SM_GM00_R00 extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener,
		OnItemClickListener {
	TextView btn_sendMsg;
	ImageView btn_groupSelect;
	EditText et_groupMsgBox;
	ListView lv_groupList;
	//TextView tv_gmByteLength;
	private Spinner callingNumberSpinner;
	ArrayList<String> callingNumberList = new ArrayList<>();
	Context context;
	SM_GM00_R00Adapter sM_GM00_R00Adapter = null;
	private EasyJsonList ejl;
	private EasyJsonMap ej01;
	private static final String GP_TP = "1";
	private String gpCd;
	private String retMsg, retValue;
	int maxByte = 4000;
	int totalMsgLength;
	private int successValue;

	private CommonSession commonSession;
	private String currentCallingNumber;

	public String getCurrentCallingNumber() {
		return currentCallingNumber;
	}

	public void setCurrentCallingNumber(String currentCallingNumber) {
		this.currentCallingNumber = currentCallingNumber;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_groupmessage);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("그룹메시지");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		activityInit();
	}
	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		commonSession = new CommonSession(context);

		//tv_gmByteLength = (TextView)findViewById(R.id.tv_gmByteLength);
		btn_sendMsg = (TextView) findViewById(R.id.btn_sendMsg);
		btn_sendMsg.setOnClickListener(this);
		btn_groupSelect = (ImageView) findViewById(R.id.btn_groupSelect);
		btn_groupSelect.setOnClickListener(this);
		et_groupMsgBox = (EditText) findViewById(R.id.et_groupMsgBox);
		lv_groupList = (ListView) findViewById(R.id.lv_groupList);
		lv_groupList.setOnItemClickListener(this);
		callingNumberSpinner = (Spinner)findViewById(R.id.callingNumberSpinner);
		//스피너 리스트 add
		callingNumberList.add("080-253-2533");
		callingNumberList.add("02-6670-2179");
		callingNumberList.add("02-6670-2161");

		ArrayAdapter<String> callingNumberAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, callingNumberList);
		callingNumberSpinner.setAdapter(callingNumberAdapter);
		callingNumberSpinner.setOnItemSelectedListener(this);

//		et_groupMsgBox.addTextChangedListener(new TextWatcher(){
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				Log.w("onTextChanged", s.toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				Log.w("onTextChanged", s.toString());
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				int nowLength = checkByteLength(et_groupMsgBox.getText().toString());
//				tv_gmByteLength.setText(nowLength + "/4000");
//				Log.w("onTextChanged", s.toString());
//			}
//		});
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_sendMsg:
				sendMsg(et_groupMsgBox.getText().toString());
				break;
			case R.id.btn_groupSelect:
				final SelectDept rbld = new SelectDept(context);
				rbld.show();
				rbld.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						ArrayList<SearchDeptResData> arrI = rbld.selectDepListAdapter
								.getSelectedData();
						sM_GM00_R00Adapter = new SM_GM00_R00Adapter(context, arrI);
						lv_groupList.setAdapter(sM_GM00_R00Adapter);
					}

				});
				break;

		}
	}

	private void sendMsg(String msg) {

		totalMsgLength = checkByteLength(et_groupMsgBox.getText().toString());
		if (totalMsgLength >= 4000) {
			Toast.makeText(context, "4000byte 이상입니다. 내용을 줄여주세요.",
					Toast.LENGTH_SHORT).show();
		} else {
			if (sM_GM00_R00Adapter != null
					&& sM_GM00_R00Adapter.getCount() != 0) {
				new ProgressDlgTest(this).execute(sM_GM00_R00Adapter.getCount());
			} else {
				Toast.makeText(context, "전송할 대상을 선택해주세요.", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(position > -1){
			this.setCurrentCallingNumber(callingNumberList.get(position));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	public class ProgressDlgTest extends AsyncTask<Integer// excute()실행시 넘겨줄
			// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public ProgressDlgTest(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDlg.setMessage("메시지 전송");
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
			successValue = 0;
			for (int i = 0; i < taskCnt; i++) {
				SearchDeptResData mData = (SearchDeptResData) sM_GM00_R00Adapter
						.getItem(i);
				gpCd = mData.getDEPT_CD();
				Log.v("getDetpc", gpCd + "  count= " + i + mData.getDEPT_NM());


				try {

					GetHttp getHttp = new GetHttp();
					String param_url = WebServerInfo.getUrl()
							+ "sm/sendGroupMessage.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("smsTp", "0"));
					arguments.add(new BasicNameValuePair("smsMsg",
							et_groupMsgBox.getText().toString()));
					arguments.add(new BasicNameValuePair("reservDt", getDate()));
					arguments.add(new BasicNameValuePair("reservTm", getTime()));
					arguments.add(new BasicNameValuePair("smsSNo", getCurrentCallingNumber()));
					arguments.add(new BasicNameValuePair("gpTp", GP_TP));
					arguments.add(new BasicNameValuePair("gpCd", gpCd));
					arguments.add(new BasicNameValuePair("smsRNo", ""));
					arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("byteLen", String
							.valueOf(totalMsgLength)));
					JSONObject returnJson = getHttp.getPost(param_url,
							arguments, true);
					System.out.println(returnJson);

					try {
						ej01 = new EasyJsonMap(
								returnJson.getJSONObject("dataMap"));
						retMsg = ej01.getValue("RTN_MESS");
						// retValue = ej01.getValue("RETVAL");
						if (retMsg.equals("OK")){
							successValue = successValue + 1;
						}

						// System.out.println(retMsg + retValue);
					} catch (JSONException e) {
						e.printStackTrace();
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
				publishProgress("progress", Integer.toString(i),"수신자 "
						+ Integer.toString(i) + "번째 전송중");
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
			Toast.makeText(mContext, Integer.toString(result) + "팀 전송 완료",
					Toast.LENGTH_SHORT).show();
		}
	}

	private String getDate() {
		DateUtil date = new DateUtil();
		Log.e("date",
				"date = " + date.changeLongDateFormat(date.gurrentDateTime()));
		return date.changeLongDateFormat(date.gurrentDateTime());
	}

	private String getTime() {
		SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm",
				Locale.KOREA);
		Date date = new Date();
		String time = dataFormat.format(date);
		Log.e("getTime", "getTime = " + time);
		return time;
	}

	private String getPhoneNo() {
		DeviceUniqNumber dun = new DeviceUniqNumber(this);
		String phno = dun.getPhoneNumber();
		Log.e("phone", "phone = " + phno);
		return phno;

	}

	private int checkByteLength(String str) {

		ByteLengthFilter byteLeng = new ByteLengthFilter(maxByte, "KSC5601");
		int leng = byteLeng.getByteLength(str);
		// InputFilter[] filters = new InputFilter[] {new
		// ByteLengthFilter(maxByte, "KSC5601")};
		// et_groupMsgBox.setFilters(filters);
		return leng;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
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
		}else{
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
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
	@Override
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
	//----------------------내비게이션 영역--------------------------------------//

}
