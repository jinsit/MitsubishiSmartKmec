package com.jinsit.kmec.WO.WT.RI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinsit.kmec.CM.CM_SDateTime;
import com.jinsit.kmec.IR.PI.IR_PI06_R00P_Adapter;
import com.jinsit.kmec.IR.PI.IR_PI06_R00_Item;
import com.jinsit.kmec.IR.PI.PartsHeaderItem;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.trxn.TrxnDocument;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 승관원 정보전송 변환 체크표 확인화면 20190122 yowonsm
 */
public class WO_WT00_R04 extends FragmentActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
	Context context;
	String empId, workDt, jobNo;

	private ProgressDialog ProgressDialog;
	private WO_WT00_R04_Header wO_WT00_R04_Header;
	private ArrayList<WO_WT00_R04_Detail> wO_WT00_R04_DetailList;

	private EasyJsonList ejl;
	private EasyJsonList ejlHeader;
	private ListView lv_wo_mngCheckTableList;
	private WO_WT00_R04_Adapter wO_WT00_R04_Adapter;
	private Button btn_complete, btn_save;
	private TextView tvEmpNm, tvselChkId,tvFromTm,tvToTm,tvEvelNo,tvBsBnm;
	private TextView tvParticuls;

	private ArrayList<IR_PI06_R00_Item> IR_PI06_R00_Items;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r04);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("승관원정보전송 체크표");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		init();
		activityInit();
		checkTableDown();

	}
	private void init() {
		// TODO Auto-generated method stub
		context = this;
		empId = getIntent().getExtras().getString("empId");
		workDt = getIntent().getExtras().getString("workDt");
		jobNo = getIntent().getExtras().getString("jobNo");

//		empId = "301933";
//		workDt = "2017-11-24";
//		jobNo = "3";
	}
	private void activityInit() {
		// TODO Auto-generated method stub
		lv_wo_mngCheckTableList = (ListView)findViewById(R.id.lv_wo_mngCheckTableList);
		btn_complete = (Button)findViewById(R.id.btn_complete);
		btn_complete.setOnClickListener(this);
		btn_save = (Button)findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);


		tvEmpNm = (TextView)findViewById(R.id.tvEmpNm);
		tvselChkId = (TextView)findViewById(R.id.tvselChkId);
		tvFromTm = (TextView)findViewById(R.id.tvFromTm);
		tvToTm = (TextView)findViewById(R.id.tvToTm);
		tvEvelNo = (TextView)findViewById(R.id.tvEvelNo);
		tvBsBnm = (TextView)findViewById(R.id.tvBsBnm);
		tvParticuls = (TextView)findViewById(R.id.tvParticuls);

		tvFromTm.setOnClickListener(this);
		tvToTm.setOnClickListener(this);
		tvEvelNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("승관원 관리원번호를 변경하시겠습니까?");
				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
				//etRmk.setInputType(InputType.TYPE_CLASS_NUMBER);
				hdRmk_etNumeric.sendEmptyMessageDelayed(0, 100);
				new AlertDialog.Builder(context)
						.setView(dial)
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								})

						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String data = etRmk.getText().toString();
										tvEvelNo.setText(data);
										wO_WT00_R04_Header.setELEVATOR_NO(data);
									}
								}).show();
			}
		});

		tvselChkId.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("승관원 ID를 변경하시겠습니까?");
				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
				//etRmk.setInputType(InputType.TYPE_CLASS_NUMBER);
				hdRmk_etNumeric.sendEmptyMessageDelayed(0, 100);
				new AlertDialog.Builder(context)
						.setView(dial)
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								})

						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String data = etRmk.getText().toString();
										tvselChkId.setText(data);
										wO_WT00_R04_Header.setSELCHK_USID(data);
									}
								}).show();
			}
		});

		tvParticuls.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final LinearLayout dial = (LinearLayout) inflater.inflate(R.layout.wt_mw00_r02_dialog_edittext, null);
				TextView dTitle = (TextView) dial.findViewById(R.id.dialTitle);
				dTitle.setText("특이사항을 입력하세요.");

				etRmk = (EditText) dial.findViewById(R.id.et_mw_r02FailureInput);
				//etRmk.setInputType(InputType.TYPE_CLASS_NUMBER);
				String particulsText = tvParticuls.getText().toString();
				if(particulsText != null && !particulsText.equals("")){
					etRmk.setText(particulsText);
				}
				hdRmk_etNumeric.sendEmptyMessageDelayed(0, 100);
				new AlertDialog.Builder(context)
						.setView(dial)
						.setNegativeButton("취소",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								})

						.setPositiveButton("확인",
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String data = etRmk.getText().toString();
										tvParticuls.setText(data);
										wO_WT00_R04_Header.setPATICULS(data);
										//wO_WT00_R04_Header.setSELCHK_USID(data);
									}
								}).show();
			}
		});

	}


	private void headerDataInit(){
		if(wO_WT00_R04_Header != null){
			tvEmpNm.setText(wO_WT00_R04_Header.getCS_EMP_NM());
			tvselChkId.setText(wO_WT00_R04_Header.getSELCHK_USID());
			tvFromTm.setText(wO_WT00_R04_Header.getSEL_CHK_ST_DT());
			tvToTm.setText(wO_WT00_R04_Header.getSEL_CHK_END_DT());
			tvEvelNo.setText(wO_WT00_R04_Header.getELEVATOR_NO());
			tvBsBnm.setText(wO_WT00_R04_Header.getBS_BNM());
			tvParticuls.setText(wO_WT00_R04_Header.getPATICULS());
		}
	}

	private void checkTableDown() {
		// TODO Auto-generated method stub
		new RoutineCheckListAsync().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_complete:
				finish();
				break;

			case R.id.btn_save:
				if(wO_WT00_R04_Adapter.isNoInputRemark()){
					AlertView.showAlert("비고란을 입력한 후 완료 하십시오.", context);
					return;
				}
				if(wO_WT00_R04_Header != null){
					if(wO_WT00_R04_Header.getSELCHK_USID() == null || wO_WT00_R04_Header.getSELCHK_USID().equals("")){
						AlertView.showAlert("승관원 ID를 입력하십시오.", context);
						return;
					}
					if(wO_WT00_R04_Header.getELEVATOR_NO() == null || wO_WT00_R04_Header.getELEVATOR_NO().equals("")){
						AlertView.showAlert("승관원 관리번호를 입력 하십시오.", context);
						return;
					}
				}
				saveMngCheckTable();
				break;

			case R.id.tvFromTm:
				isFromTm = true;
				if(wO_WT00_R04_Header == null)return;
				showTimePicker();
				break;
			case R.id.tvToTm:
				isFromTm = false;
				if(wO_WT00_R04_Header == null)return;
				showTimePicker();
				break;
		}
	}

	private void showTimePicker(){
		if(wO_WT00_R04_Header == null)return;
		int hour = 0;
		int minute = 0;
		if(isFromTm){
			hour = Integer.valueOf(wO_WT00_R04_Header.getSEL_CHK_ST_DT().substring(0, 2));
			minute = Integer.valueOf(wO_WT00_R04_Header.getSEL_CHK_ST_DT().substring(3, 5));
		}else{
			hour = Integer.valueOf(wO_WT00_R04_Header.getSEL_CHK_END_DT().substring(0, 2));
			minute = Integer.valueOf(wO_WT00_R04_Header.getSEL_CHK_END_DT().substring(3, 5));
		}

		TimePickerDialog dialog = new TimePickerDialog(this, this, hour, minute, false);
		dialog.show();
	}

	private boolean isFromTm;
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		String hour = String.valueOf(hourOfDay);
		if(hourOfDay < 10){
			hour = "0" + hour;
		}
		String min = String.valueOf(minute);
		if(minute < 10){
			min = "0" + min;
		}
		String dateTime = hour + ":" + min;
		if(isFromTm){
			tvFromTm.setText(dateTime);
			wO_WT00_R04_Header.setSEL_CHK_ST_DT(dateTime);
		}else{
			tvToTm.setText(dateTime);
			wO_WT00_R04_Header.setSEL_CHK_END_DT(dateTime);
		}
	}


	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {


		@Override
		protected void onPostExecute(Void result) {
			WO_WT00_R04.this.ProgressDialog.dismiss();

			new SelectSpinnerItem().execute();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WO_WT00_R04.this.ProgressDialog = android.app.ProgressDialog
					.show(context, "점검항목", "점검항목 조회중");
		}

		@Override
		protected Void doInBackground(Void... params) {

			wO_WT00_R04_Header = new WO_WT00_R04_Header();
			wO_WT00_R04_DetailList = new ArrayList<WO_WT00_R04_Detail>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectMngCheckTable.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("csEmpId", empId));
			arguments.add(new BasicNameValuePair("workDt", workDt));
			arguments.add(new BasicNameValuePair("jobNo", jobNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


			try {
				JSONObject dataList = returnJson.getJSONObject("dataList");
				ejlHeader = new EasyJsonList(dataList.getJSONArray("header"));
				wO_WT00_R04_Header = new WO_WT00_R04_Header(ejlHeader.getValue(0,	"ELEVATOR_NO"),
						ejlHeader.getValue(0, "SELCHK_USID"),
						ejlHeader.getValue(0, "SEL_CHK_ST_DT"),
						ejlHeader.getValue(0, "SEL_CHK_END_DT"),
						ejlHeader.getValue(0, "TM"),
						ejlHeader.getValue(0, "BS_BNM"),
						ejlHeader.getValue(0, "CS_EMP_NM"),
						ejlHeader.getValue(0, "CS_EMP_ID"),
						ejlHeader.getValue(0, "WORK_DT"),
						ejlHeader.getValue(0,"JOB_NO"),
						ejlHeader.getValue(0,"PATICULS"));

				ejl = new EasyJsonList(dataList.getJSONArray("detail"));
				wO_WT00_R04_DetailList.clear();
				int jsonSize = ejl.getLength();
				for (int i = 0; i < jsonSize; i++) {
					wO_WT00_R04_DetailList.add(new WO_WT00_R04_Detail(ejl.getValue(i,
							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"),
							ejl.getValue(i, "JOB_NO"),
							ejl.getValue(i, "SEL_CHK_ITEM_CD"),
							ejl.getValue(i, "PRE_CHK_RESULT"),
							ejl.getValue(i, "SEL_CHK_RESULT"),
							ejl.getValue(i,"REMARK"),
							ejl.getValue(i, "SEL_CHK_ITEM_NM"),
							ejl.getValue(i, "SEL_CHK_RESULT_NM"),
							ejl.getValue(i, "PRE_YM"),
							ejl.getValue(i, "HEADER_IF")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	private String inputXml;
	private void saveMngCheckTable(){
		TrxnDocument document = new TrxnDocument();
		try{

			document.setwO_WT00_R04_Header(wO_WT00_R04_Header);
			document.setwO_WT00_R04_Details(wO_WT00_R04_DetailList);
			inputXml = document.toXmlMngCheck();
		}catch(Exception ex){
			AlertView.showAlert(ex.toString(), context);
			return;
		}
		progress(true);
		new CompleteMngCheckRequest().execute();
	}



	public class SelectSpinnerItem extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				IR_PI06_R00_Items = new ArrayList<IR_PI06_R00_Item>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectPartsRequestPopUp.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("bldgNo", ""));
				arguments.add(new BasicNameValuePair("refTp", ""));
				arguments.add(new BasicNameValuePair("tp", "z"));
				arguments.add(new BasicNameValuePair("userId", ""));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					IR_PI06_R00_Items.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						IR_PI06_R00_Items.add(new IR_PI06_R00_Item(
								ejl.getValue(i,"CD"),
								ejl.getValue(i, "NM"),
								ejl.getValue(i, "TP")));
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
			ProgressDialog.dismiss();
			wO_WT00_R04_Adapter = new WO_WT00_R04_Adapter(context, wO_WT00_R04_DetailList, IR_PI06_R00_Items);
			lv_wo_mngCheckTableList.setAdapter(wO_WT00_R04_Adapter);
			headerDataInit();

		}
	}


	public class CompleteMngCheckRequest extends AsyncTask<Void, Void, Void> {

		private String message = "";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/completeMngCheckRequest.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("inputXml", inputXml));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					int jsonSize = returnJson.getJSONArray("dataList").length();

					if(jsonSize > 0){
						message = ejl.getValue(0, "RETVAL");
					}

				} catch (JSONException e) {
					e.printStackTrace();
					message = e.getMessage().toString();
				}


			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				message = ex.getMessage().toString();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ProgressDialog.dismiss();
			if("OK".equals(message)){
				SimpleDialog sm01 = new SimpleDialog(context, "알림", "전송자료가 정상적으로 등록되었습니다.", new SimpleDialog.btnClickListener() {
					@Override
					public void onButtonClick() {
						finish();
					}
				});
				sm01.setCancelable(false);
				sm01.show();
			}else{
				SimpleDialog sm01 = new SimpleDialog(context, "알림", "전송자료 전송실패.\n 메시지 = " + message , new SimpleDialog.btnClickListener() {
					@Override
					public void onButtonClick() {
					}
				});
				sm01.setCancelable(false);
				sm01.show();
			}
		}
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			this.ProgressDialog =
					android.app.ProgressDialog.show(context, "알림","조회중 입니다.");
		}else{
			this.ProgressDialog.dismiss();
		}
	}

	//region NAVI바
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
	private boolean isFirstHide = false;
	private void navigationInit(){
		testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
		homeNavi = new HomeNavigation(context, null);
		homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		if(!isFirstHide){
			naviPref.setHide(true);
			isFirstHide = true;
		}
		homeNavi.setToggleNavi();
		Button navi = (Button) homeNavi.getBtn_naviHOME();
		navi.setOnClickListener(new View.OnClickListener() {
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
	//endregion
	/**
	 * 특기사항 입력 푸터가 빠지면서 일단은 사용하지 않는다
	 *
	 */
	EditText etRmk;
	private Handler hdRmk_etNumeric = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm;
			imm = (InputMethodManager) getSystemService("input_method");
			imm.showSoftInput(etRmk, 0);
		}
	};

	private void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
