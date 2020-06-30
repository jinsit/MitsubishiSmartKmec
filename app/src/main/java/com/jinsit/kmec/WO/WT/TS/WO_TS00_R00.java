package com.jinsit.kmec.WO.WT.TS;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_DateTime;
import com.jinsit.kmec.CM.CM_SaveReadPicture;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SelectInspector;
import com.jinsit.kmec.CM.SearchAdminInfo;
import com.jinsit.kmec.CM.SelectPendCode;
import com.jinsit.kmec.IR.NM.NFCRegistrationData;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.GPSService;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.StringUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 * 고장수리
 */
@SuppressLint("NewApi")
public class WO_TS00_R00 extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	private Context context;
	private EasyJsonMap ej01;
	private EasyJsonMap ej02;
	private WO_TS00_R00_ITEM03 workTargetData;
	private NFCRegistrationData nFCRegistrationData;
	private WO_TS00_R00_ITEM01 item01;
	private WO_TS00_R00_ITEM02 item02;
	private WO_TS00_R00_ITEM04 item04;
	
	private ProgressDialog progress;

	private TextView tv_fr_date;
	private TextView tv_fr_workNm;
	private TextView tv_fr_bldgInfo;
	private TextView tv_fr_addr;
	private TextView tv_fr_csDeptNm;
	private TextView tv_fr_empNm1;
	private TextView tv_fr_empHp1;
	private TextView tv_fr_empNm2;
	private TextView tv_fr_empHp2;
	private TextView tv_fr_notifyNm;
	private TextView tv_fr_recevDesc;
	private TextView tv_fr_specialDesc;

	private TextView btn_fr_adminInfo;
	private TextView btn_fr_reserverTime;

	private TextView tv_fr_travelTime;
	private TextView tv_fr_arrivalTime;
	private TextView tv_fr_repaireCompletionTime;
	private TextView tv_fr_completionTime;

	private TextView btn_fr_troublePicture;
	private CheckBox cb_fr_respond;
	private CheckBox cb_fr_confined;
	private TextView tv_fr_rescuetime;
	private TextView btn_fr_aSProcCode;
	
	private LinearLayout lin_fr_cpErrorCode;
	private TextView tv_fr_cpErrorCode;
	
	private TextView btn_fr_aSProcReport;
	private TextView btn_completework;
	
	private TextView tv_fr_st;
	private TextView tv_fr_csfr;
	
	private LinearLayout lin_aSProcCode;
	
	private TextView tv_fr_aSProcCode;
	private TextView tv_fr_CbsNm1;
	private TextView tv_fr_CbsNm2;
	private TextView tv_fr_CbsNm3;
	private TextView tv_fr_falutNm;
	private TextView tv_fr_procNm;
	private TextView tv_fr_dutyNm;

	
	private EditText et_fr_orderDesc;
	
	// /nfc
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private boolean progressWorking;
	private boolean onPopUp;
	// ////

	private final static String TRAVEL_TIME = "11";
	private final static String ARRIVAL_TIME = "31";
	private final static String COMPLETION_TIME = "39";

	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;
	
	private CommonSession commonSession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_ts00_r00);
		activityInit();
	}
	private boolean isAsRegistration;
	@Override
	protected void onRestart() {
		super.onRestart();
		if(!isAsRegistration)searchDetail();
	};

	@Override
	protected void onResume() {
		super.onResume();
		enableTagWriteMode();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableTagWriteMode();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if(!workTargetData.getRepairTm().equals("") && workTargetData.getRepairTm() != null){
			Toast.makeText(context, "현재모드에서는 NFC TAG를 읽을 수 없습니다", Toast.LENGTH_LONG);
			return;
			
		}
		if(progressWorking){
			Toast.makeText(context, "현재모드에서는 NFC TAG를 읽을 수 없습니다", Toast.LENGTH_LONG);
			return;
		}
		if(onPopUp){
			Toast.makeText(context, "현재모드에서는 NFC TAG를 읽을 수 없습니다", Toast.LENGTH_LONG);
			return;
		}

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
				progress(true);
				new NfcCheckAsync().execute("bagicWorkTime");
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

	
	protected void activityInit() {
		context = this;
		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
		

		commonSession = new CommonSession(context);
		
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		searchDetail();

		// /nfc
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		ndefMsg = "";
		tagUid = "";
		// /

		lin_aSProcCode.setVisibility(View.GONE);
		
		this.selectedItem03 = null;
		this.selectedItemList04 = new ArrayList<WO_TS00_R04P_ITEM01>();
		this.selectedItem07 = null;
		this.selectedItem08 = null;
		this.selectedItem09 = null;
		this.cpErrorCodeList = new ArrayList<String>();
		progressWorking = false;
		onPopUp =false;
	}
	

	protected void getInstances() {
		tv_fr_date = (TextView) findViewById(R.id.tv_fr_date);
		tv_fr_workNm = (TextView) findViewById(R.id.tv_fr_workNm);
		tv_fr_bldgInfo = (TextView) findViewById(R.id.tv_fr_bldgInfo);
		tv_fr_addr = (TextView) findViewById(R.id.tv_fr_addr);
		tv_fr_csDeptNm = (TextView) findViewById(R.id.tv_fr_csDeptNm);
		tv_fr_empNm1 = (TextView) findViewById(R.id.tv_fr_empNm1);
		tv_fr_empHp1 = (TextView) findViewById(R.id.tv_fr_empHp1);
		tv_fr_empNm2 = (TextView) findViewById(R.id.tv_fr_empNm2);
		tv_fr_empHp2 = (TextView) findViewById(R.id.tv_fr_empHp2);
		tv_fr_notifyNm = (TextView) findViewById(R.id.tv_fr_notifyNm);
		tv_fr_recevDesc = (TextView) findViewById(R.id.tv_fr_recevDesc);
		tv_fr_specialDesc = (TextView)findViewById(R.id.tv_fr_specialDesc);
		btn_fr_adminInfo = (TextView) findViewById(R.id.btn_fr_adminInfo);
		btn_fr_reserverTime = (TextView) findViewById(R.id.btn_fr_reserverTime);

		tv_fr_travelTime = (TextView) findViewById(R.id.tv_fr_travelTime);
		tv_fr_arrivalTime = (TextView) findViewById(R.id.tv_fr_arrivalTime);
		tv_fr_repaireCompletionTime = (TextView) findViewById(R.id.tv_fr_repaireCompletionTime);
		tv_fr_completionTime = (TextView) findViewById(R.id.tv_fr_completionTime);

		btn_fr_troublePicture = (TextView) findViewById(R.id.btn_fr_troublePicture);

		cb_fr_respond = (CheckBox) findViewById(R.id.cb_fr_respond);
		cb_fr_confined = (CheckBox) findViewById(R.id.cb_fr_confined);

		tv_fr_rescuetime = (TextView) findViewById(R.id.tv_fr_rescuetime);
		btn_fr_aSProcCode = (TextView) findViewById(R.id.btn_fr_aSProcCode);
		
		lin_fr_cpErrorCode = (LinearLayout) findViewById(R.id.lin_fr_cpErrorCode);
		tv_fr_cpErrorCode = (TextView) findViewById(R.id.tv_fr_cpErrorCode);
		
		btn_fr_aSProcReport = (TextView) findViewById(R.id.btn_fr_aSProcReport);
		
		btn_completework = (TextView) findViewById(R.id.btn_completework);
		
		tv_fr_st = (TextView) findViewById(R.id.tv_fr_st);
		tv_fr_csfr = (TextView) findViewById(R.id.tv_fr_csfr);
		
		lin_aSProcCode= (LinearLayout)findViewById(R.id.lin_aSProcCode);
		
		tv_fr_aSProcCode = (TextView) findViewById(R.id.tv_fr_aSProcCode);
		tv_fr_CbsNm1 = (TextView) findViewById(R.id.tv_fr_CbsNm1);
		tv_fr_CbsNm2 = (TextView) findViewById(R.id.tv_fr_CbsNm2);
		tv_fr_CbsNm3= (TextView) findViewById(R.id.tv_fr_CbsNm3);
		tv_fr_falutNm = (TextView) findViewById(R.id.tv_fr_falutNm);
		tv_fr_procNm = (TextView) findViewById(R.id.tv_fr_procNm);
		tv_fr_dutyNm = (TextView) findViewById(R.id.tv_fr_dutyNm);
		
		et_fr_orderDesc = (EditText)findViewById(R.id.et_fr_orderDesc);
		

		setEvents();
	}

	protected void setEvents() {
		btn_fr_adminInfo.setOnClickListener(this);
		btn_fr_reserverTime.setOnClickListener(this);

		tv_fr_rescuetime.setOnClickListener(this);
		btn_fr_aSProcCode.setOnClickListener(this);
		lin_fr_cpErrorCode.setOnClickListener(this);
		btn_fr_aSProcReport.setOnClickListener(this);
		btn_completework.setOnClickListener(this);
		btn_fr_troublePicture.setOnClickListener(this);

		tv_fr_travelTime.setOnClickListener(this);
		tv_fr_arrivalTime.setOnClickListener(this);
		tv_fr_repaireCompletionTime.setOnClickListener(this);
		
		cb_fr_confined.setOnCheckedChangeListener(this);
		cb_fr_respond.setOnCheckedChangeListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ip_wo_r00, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// String[] param = {empId, WO_Test_Data.getDate(), jobNo};
		String[] param = { commonSession.getEmpId() , workTargetData.getWorkDt(), workTargetData.getJobNO() };
		if (id == R.id.menu_ip_wo_pendWork) {
			// /미처리

			if(!workTargetData.getRepairTm().equals("") && workTargetData.getRepairTm() != null){
				showAlert("수리를 완료 후에는 미처리를 할 수 없습니다.");
				return false;
			}
			SelectPendCode selectPendCode = new SelectPendCode(context, param, this);
			selectPendCode.show();

			return true;
		} else if (id == R.id.menu_ip_wo_transferWork) {
			// 이관
			if(!workTargetData.getRepairTm().equals("") && workTargetData.getRepairTm() != null){
				showAlert("수리를 완료 후에는 이관을 할 수 없습니다.");
				return false;
			}
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_TRANSFER, param, this);
			cM_SelectInspector.show();
			return true;
		} else if (id == R.id.menu_ip_wo_helpWork) {
			// 지원요청
			if(!workTargetData.getRepairTm().equals("") && workTargetData.getRepairTm() != null){
				showAlert("수리를 완료 후에는 지원 요청을 할 수 없습니다.");
				return false;
			}
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_HELP, param, this);
			cM_SelectInspector.show();

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_fr_adminInfo:
			SearchAdminInfo searchAdminInfo = new SearchAdminInfo(context,
					workTargetData.getBldgNo());
			searchAdminInfo.show();
			break;
		case R.id.btn_fr_reserverTime:
			if(workTargetData.getSt().equals("계획")){
				final CM_DateTime dateTimeDlg = new CM_DateTime(context,"예약시간 설정",  reserveYear,reserveMonth,reserveDay,reserverHour,reserverMinute);
				dateTimeDlg.show();
				dateTimeDlg.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						String dateTime = dateTimeDlg.getDateTimeString();
						if (dateTime == "" || dateTime == null) {
						} else {
							reservationTimeRegistration(dateTime);
						}
					}

				});
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				AlertView.showAlert("계획모드에서만 예약을 할 수 있습니다.", context);
			}
			break;
		case R.id.tv_fr_travelTime:
			if(workTargetData.getSt().equals("계획") || workTargetData.getSt().equals("예약")){
				timeRegistration(TRAVEL_TIME);
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				showAlert("계획 또는 예약 모드에서만 이동 할 수 있습니다.");
			}
			break;
		case R.id.tv_fr_arrivalTime:
			if(workTargetData.getMoveTm().equals("") || workTargetData.getMoveTm() == null ){
				showAlert("이동을 먼저 해야 합니다.");
				return;
			}
			if(workTargetData.getSt().equals("이동")){
				timeRegistration(ARRIVAL_TIME);
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				showAlert("이동 모드에서만 도착 할 수 있습니다.");
			}
			break;
		case R.id.tv_fr_repaireCompletionTime:
			if(workTargetData.getArriveTm().equals("") || workTargetData.getArriveTm() == null ){
				showAlert("도착을 먼저 해야 합니다.");
				return;
			}
			if(workTargetData.getSt().equals("작업")){
				repaireTimeRegistration();
			}
			else{
				////계획모드에서만 예약을 할 수 있습니다.
				showAlert("작업 모드에서만 수리를 완료 할 수 있습니다.");
			}
			break;
		case R.id.tv_fr_rescuetime:
			final CM_DateTime rescueDateTimeDlg = new CM_DateTime(context,"구출시간 설정",  rescueYear,rescueMonth,rescueDay,rescueHour,rescueMinute);
			rescueDateTimeDlg.show();
			rescueDateTimeDlg.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					String dateTime = rescueDateTimeDlg.getDateTimeString();
					if (dateTime == "" || dateTime == null) {
					} else {
					}
					setRescueDateTime(dateTime);
				}

			});
			break;
		case R.id.btn_fr_aSProcCode:
			if(workTargetData.getRepairTm().equals("")|| workTargetData.getRepairTm() == null)
			{
				showAlert("수리 완료시간을 넣어주세요");
				return;	
			}
			final WO_TS00_R02P wo1 = new WO_TS00_R02P(context,selectedItem03, selectedItemList04, selectedItem07,selectedItem08,selectedItem09);
			wo1.show();
			wo1.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					selCode = wo1.getSelCode();
					WO_TS00_R03P_ITEM01 item03 = wo1.getSelectedItem03();
				    List<WO_TS00_R04P_ITEM01> itemList04 = wo1.getSelectedItemList04();
				    WO_TS00_R07P_ITEM01 item07  = wo1.getSelectedItem07();
					WO_TS00_R08P_ITEM01 item08 = wo1.getSelectedItem08();
					WO_TS00_R09P_ITEM01 item09 = wo1.getSelectedItem09();
					if(item03 !=null){
						setSelectedItem03(item03);
						lin_aSProcCode.setVisibility(View.VISIBLE);
					}
					if(itemList04 == null){
						
					}
					else if(itemList04.size() > 0){
						setSelectedItemList4(itemList04);
						lin_aSProcCode.setVisibility(View.VISIBLE);
					}
					if(item07 != null){
						setSelectedItem07(item07);
						lin_aSProcCode.setVisibility(View.VISIBLE);
					}
					if(item08 != null){
						setSelectedItem08(item08);
						lin_aSProcCode.setVisibility(View.VISIBLE);
					}
					if(item09 != null){
						setSelectedItem09(item09);
						lin_aSProcCode.setVisibility(View.VISIBLE);
					}
				}

			});
			break;
		case R.id.lin_fr_cpErrorCode:
			if(workTargetData.getRepairTm().equals("")|| workTargetData.getRepairTm() == null)
			{
				showAlert("수리를 완료시간을 넣어주세요");
				return;	
			}
			final WO_TS00_M10P wo10 = new WO_TS00_M10P(context);
			wo10.show();
			wo10.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					List<String> cpErrorCodeList = wo10.getCpErrorCodeList();
					if (cpErrorCodeList.size() > 0) {
						setCpErrorCodeList(cpErrorCodeList);
					} 
				}

			});
			break;
		case R.id.btn_fr_aSProcReport:
			if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
				
				final WO_TS00_R11P wo11 = new WO_TS00_R11P(context, workTargetData, selectedItem03, selectedItemList04, 
						selectedItem07,selectedItem08,selectedItem09, item04);
				wo11.show();
				wo11.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						if(wo11.getResult()){
							searchDetail();
						}
					}

				});
				return;
			}
			if(workTargetData.getRepairTm().equals("")|| workTargetData.getRepairTm() == null)
			{
				showAlert("수리를 완료시간을 넣어주세요");
				return;	
			}
			else if(this.selectedItem03 == null || this.selectedItemList04.size() != 3 || this.selectedItem07 == null || this.selectedItem08 == null || this.selectedItem09 == null){
				showAlert("미처리된 내용이 있습니다.");
				return;	
			}
			workTargetData.setRescueTm(rescueDateTime);
			final WO_TS00_R11P wo11 = new WO_TS00_R11P(context, workTargetData, selectedItem03, selectedItemList04, selectedItem07,selectedItem08,selectedItem09, item04);
			wo11.show();
			wo11.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if(wo11.getResult()){
						//여기서 재조회를 하면 업는데이터가 나온다
						//작업완료를 등록하지는 않고  사인, 고객확인 ftp파일 전송등만 한다.
						//workTargetData 재조회하면 대응협의 시간등은 없는게 맞다.
						//searchDetail을 하는게 아니라 R11P에서 보냈던workTragetData를 다시 가져와서 셋팅을 하던지 
						//as처리보고서 텍스트뷰에 '등록'을 추가해서 작업완료가 가능토록 해야할 것 같다. 
						//일단 재조회 하면 초기화된 데이터가 나오는건 맞다.
						isAsRegistration = true;
						workTargetData = wo11.getWorkTargetData();
						btn_fr_aSProcReport.setText(StringUtil.padLeft("A/S 처리 보고서", "등록"));
						//searchDetail();
					}
				}

			});
			break;
		case R.id.btn_completework:
			Log.v("et_fr_orderDesc.getText()", et_fr_orderDesc.getText().toString());
			
			///gps활성화 체크
			GPSService gs = new GPSService(context);
			if(!gs.isGetLocation()){
				gpsActivation();
				return;
			}
			if(workTargetData.getRepairTm().equals("")|| workTargetData.getRepairTm() == null)
			{
				showAlert("수리를 완료시간을 넣어주세요");
				return;	
			}
			else if(this.selectedItem03 == null || this.selectedItemList04.size() != 3 || this.selectedItem07 == null || this.selectedItem08 == null || this.selectedItem09 == null){
				showAlert("A/S 처리코드를 입력해주세요");
				return;	
			}
			else if(et_fr_orderDesc.getText().length() ==0){
				showAlert("상세내용을 입력해주세요.");
				return;	
			}else if(item04 == null){
				showAlert("A/S 처리 보고서를 등록 해 주세요");
				return;	
			}
			android.content.DialogInterface.OnClickListener ocl = new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					onPopUp = false;
					progress(true);
					new registerASWork().execute("bagicWorkTime");
				}};
			showAlertYn("작업을 완료 하시겠습니까?",ocl );
			break;
		case R.id.btn_fr_troublePicture:
			if(workTargetData.getArriveTm().equals("")|| workTargetData.getArriveTm() == null)
			{
				showAlert("도착 시간을 넣어주세요");
				return;	
			}
			
			if (workTargetData.getRepairTm() != null && !workTargetData.getRepairTm().equals("")) {
				Intent intent = new Intent(WO_TS00_R00.this, CM_SaveReadPicture.class);
				intent.putExtra("jobNo", workTargetData.getJobNO());
				intent.putExtra("workDt", workTargetData.getWorkDt());
				intent.putExtra("refContrNo", workTargetData.getRefContrNo());
				intent.putExtra("selTp", "1");
				startActivity(intent);;
			}
			else{
				showAlert("수리 완료 시간을 넣어주세요");
				return;	
			}
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.cb_fr_confined:
			workTargetData.setStatus(isChecked);
			if(workTargetData.isStatus()){
				this.tv_fr_rescuetime.setVisibility(View.VISIBLE);
				workTargetData.setStatus(isChecked);
			}
			else
			{
				this.tv_fr_rescuetime.setVisibility(View.GONE);
				tv_fr_rescuetime.setText(StringUtil.padLeft(WO_TS00_R00.this
						.getString(R.string.btnstr_rescueTime), ""));
				rescueDateTime = "";
				workTargetData.setStatus(isChecked);
			}
			break;
			
		case R.id.cb_fr_respond:
			workTargetData.setContact(isChecked);
			if(workTargetData.isContact()){
				workTargetData.setContact(isChecked);
				//workTargetData.setco
			}else{
				workTargetData.setContact(isChecked);
			}
			
			break;
			
		default:
			break;
		}
	}

	private class registerASWork extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/registerASWork.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJobNO()));
//				arguments.add(new BasicNameValuePair("contactCd", workTargetData.getContactCd()));
//				arguments.add(new BasicNameValuePair("rescueTmG", workTargetData.getRescueTm()));
				arguments.add(new BasicNameValuePair("contactCd", workTargetData.getContactCd()));
				arguments.add(new BasicNameValuePair("rescueTmG", rescueDateTime));
				
				arguments.add(new BasicNameValuePair("codeSt", selCode));
				arguments.add(new BasicNameValuePair("codeCd", selectedItem03.getCodeCd()));
				arguments.add(new BasicNameValuePair("cbsCd", selectedItemList04.get(2).getAssyCd()));
				arguments.add(new BasicNameValuePair("faultCd", selectedItem07.getFaultCd()));
				arguments.add(new BasicNameValuePair("procCd", selectedItem08.getProcCd()));
				arguments.add(new BasicNameValuePair("dutyCd", selectedItem09.getDutyCd()));
				
				for(int i = cpErrorCodeList.size(); i < 5 ; i++){
					cpErrorCodeList.add("");
				}
				arguments.add(new BasicNameValuePair("errorCd1",cpErrorCodeList.get(0)));
				arguments.add(new BasicNameValuePair("errorCd2",cpErrorCodeList.get(1)));
				arguments.add(new BasicNameValuePair("errorCd3",cpErrorCodeList.get(2)));
				arguments.add(new BasicNameValuePair("errorCd4",cpErrorCodeList.get(3)));
				arguments.add(new BasicNameValuePair("errorCd5",cpErrorCodeList.get(4)));
				
				arguments.add(new BasicNameValuePair("orderDesc",et_fr_orderDesc.getText().toString()));
				
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				
				returnJson01 = http.getPost(param_url, arguments,
						true);

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				
				String returnValue = "N";
				try {
					returnValue = returnJson01.getString("dataString");
					if(returnValue.equals("1")){
						CallService.startGPSService(context);
						finish();
						progress(false);
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}catch (Exception ex){
					ex.printStackTrace();
				}
				progress(false);
				showAlert( "등록실패했습니다.");
			}
		}
	}
	
	public class workTargetDetailAsync extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		JSONObject returnJson02;

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url = WebServerInfo.getUrl()
							+ "ip/selectWorkTargetDetail.do";
					String param_url02 = WebServerInfo.getUrl()
							+ "ip/selectASProcReportMail.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();

					arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson01 = http.getPost(param_url, arguments, true);
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
					
					arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson02 = http.getPost(param_url02, arguments, true);
					ej02 = new EasyJsonMap(returnJson02.getJSONObject("dataMap"));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return params[0];

			}
			return "None";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				workTargetData = null;
				try {
					workTargetData = new WO_TS00_R00_ITEM03(ej01.getValue("CS_EMP_ID"),
							ej01.getValue("WORK_DT"), ej01.getValue("JOB_NO"),
							ej01.getValue("WORK_NM"), ej01.getValue("ST"),ej01.getValue("JOB_ST"),
							ej01.getValue("CS_DT"), ej01.getValue("CS_FR"),
							ej01.getValue("BLDG_NO"), ej01.getValue("BLDG_NM"),
							ej01.getValue("CAR_NO"),
							ej01.getValue("RESERV_ST"), ej01.getValue("ADDR"),
							ej01.getValue("MAIN_EMP_NM"),
							ej01.getValue("MAIN_EMP_PHONE"),
							ej01.getValue("SUB_EMP_NM"),
							ej01.getValue("SUB_EMP_PHONE"),
							ej01.getValue("CS_DEPT_NM"),
							ej01.getValue("NOTIFY_NM"),
							ej01.getValue("NOTIFY_PHONE"),
							ej01.getValue("RECEV_DESC"),
							ej01.getValue("RECEV_TM"),
							ej01.getValue("RESERV_TM"),
							ej01.getValue("REPAIR_TM"),
							ej01.getValue("MOVE_TM"),
							ej01.getValue("ARRIVE_TM"),
							ej01.getValue("COMPLETE_TM"),
							ej01.getValue("RESCUE_TM"),
							ej01.getValue("START_TM"),
							ej01.getValue("CONTACT_CD"),
							ej01.getValue("STATUS_CD"),
							ej01.getValue("REF_CONTR_NO"),
							ej01.getValue("PARTS_NO"),
							ej01.getValue("MODEL_NM"),
							ej01.getValue("RECEV_NO"),
							ej01.getValue("CHECK_YN"), ej01.getValue("CBS_YN"),ej01.getValue("SPECIAL_DESC"));

				} catch (JSONException e) {
					e.printStackTrace();
					workTargetData = null;
				}
				item04 = null;
				try {
					item04 = new WO_TS00_R00_ITEM04(ej02.getValue("CS_EMP_ID"),
							ej02.getValue("WORK_DT"), ej02.getValue("JOB_NO"),
							ej02.getValue("MAILID1"), ej02.getValue("MAILID2"),ej02.getValue("SEND_ST"),
							ej02.getValue("BLDG_NO"), ej02.getValue("BLDG_NM"),
							ej02.getValue("CAR_NO"),  ej02.getValue("CAR_CD"),
							ej02.getValue("RECEV_NO"), ej02.getValue("RECEV_TM"),
							ej02.getValue("RESERV_TM"),
							ej02.getValue("MOVE_TM"),
							ej02.getValue("ARRIVE_TM"),
							ej02.getValue("COMPLETE_TM"),
							ej02.getValue("RESCUE_TM"),
							ej02.getValue("001"),
							ej02.getValue("EMP_NM"),
							ej02.getValue("CS_EMP_NM"),
							ej02.getValue("PHONE_1"),
							ej02.getValue("1"),
							ej02.getValue("EX_ORDER_CD"),
							ej02.getValue("STATUS_CD"),
							ej02.getValue("CBS_CD_1"),
							ej02.getValue("CBS_CD_2"),
							ej02.getValue("CBS_CD_3"),
							ej02.getValue("FAULT_CD"),
							ej02.getValue("PROC_CD"),
							ej02.getValue("DUTY_CD"),
							ej02.getValue("AS_CD_1"),
							ej02.getValue("AS_CD_2"),
							ej02.getValue("AS_RMK"),
							ej02.getValue("CUSTOMER"), ej02.getValue("CUST_SIGN"));

				} catch (JSONException e) {
					e.printStackTrace();
					item04 = null;
				}
				progress(false);
				if (workTargetData != null) {
					setDetailView();
				}
			}
		}
	}

	private class NfcCheckAsync extends AsyncTask<String, Integer, String> {
		EasyJsonMap ejmMsg, ejmData;
		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		
		

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
			try {
				
				CommonSession cs = new CommonSession(context);
				String param_url = WebServerInfo.getUrl()
						+ "sm/checkUserOfficeAttendance.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", DateUtil.nowDate()));
				arguments.add(new BasicNameValuePair("nfcTag", tagUid));
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {
					ejmMsg = new EasyJsonMap( returnJson01.getJSONObject("msgMap") );
					ejmData = new EasyJsonMap( returnJson01.getJSONObject("dataMap") );
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				try {
					boolean isError = ejmMsg.getValue("errCd").equals("1") ? true
							: false;
					if (!isError) {
						String rtn = ejmData.getValue("RTN");
						if (rtn.equals("0")) {
							// nfcTagEvent(ndefMsg);
							progress(false);
							showAlert("등록된 NFC TAG가 없습니다. 태그를 등록하시고 진행하세요.");
						} else if (rtn.equals("1")) {
							// 사무실 태그
							progress(false);
							if (workTargetData.getSt().equals("계획")
									|| workTargetData.getSt().equals("예약")) {
								timeRegistration(TRAVEL_TIME);
							} else if (workTargetData.getSt().equals("이동")) {
								showAlert("사무실 NFC TAG로는 도착할 수 없습니다.");
							} else if (workTargetData.getSt().equals("작업")) {
								showAlert("사무실 NFC TAG로는 수리 완료할 수 없습니다.");
							}
						} else if (rtn.equals("2")) {
							// 회의실 태그
							progress(false);
							if (workTargetData.getSt().equals("계획")
									|| workTargetData.getSt().equals("예약")) {
								timeRegistration(TRAVEL_TIME);
							} else if (workTargetData.getSt().equals("이동")) {
								showAlert("회의실 NFC TAG로는 도착할 수 없습니다.");
							} else if (workTargetData.getSt().equals("작업")) {
								showAlert("회의실 NFC TAG로는 수리 완료할 수 없습니다.");
							}

						} else if (rtn.equals("3")) {
							// 빌딩테그
							if (workTargetData.getSt().equals("계획")
									|| workTargetData.getSt().equals("예약")) {
								progress(false);
								timeRegistration(TRAVEL_TIME);
							} else if (workTargetData.getSt().equals("이동")
									|| workTargetData.getSt().equals("작업")) {
								new selectNfcInfo().execute("bagicWorkTime");
							}
						}

					} else if (isError) {
						progress(false);
						showAlert(ejmMsg.getValue("errMsg"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					progress(false);
					showAlert("Nfc Tag 조회 에러. "+e.getMessage());
				}
			}
		}
	}
	
	public class selectNfcInfo extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectNfcInfo.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("nfcTagNo", tagUid));
					
					returnJson01 = http.getPost(param_url_01, arguments, true);
					String bldgNo = "";
					try {
						Log.v("returnJson01",returnJson01.toString());
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				nFCRegistrationData = null;
				try {
					nFCRegistrationData = new NFCRegistrationData(ej01.getValue("BLDG_NO"),
							ej01.getValue("BLDG_NM"), ej01.getValue("CAR_NO"),
							ej01.getValue("DONG_CAR_NO"), 
							ej01.getValue("NFC_PLC"),
							ej01.getValue("NFC_PLC_NM"),
							ej01.getValue("GOOD_NM"), ej01.getValue("NFC_TAG"));
				} catch (JSONException e) {
					e.printStackTrace();
					nFCRegistrationData = null;
				}
				progress(false);
				
				if (nFCRegistrationData != null){
					Log.v("bldgNo",nFCRegistrationData.getBLDG_NO());
					Log.v("bldgNo1",workTargetData.getBldgNo());
					Log.v("bldgNo",nFCRegistrationData.getCAR_NO());
					Log.v("carNo1",workTargetData.getCarNo());
					Log.v("tag", tagUid);
					if ( !nFCRegistrationData.getBLDG_NO().equals(workTargetData.getBldgNo()) || !nFCRegistrationData.getCAR_NO().equals(workTargetData.getCarNo())){
						showAlert("현재 빌딩정보와 Nfc Tag 빌딩정보가 틀립니다.");
					}
					else if(workTargetData.getSt().equals("이동")){
						timeRegistration(ARRIVAL_TIME);
					}
					else if(workTargetData.getSt().equals("작업")){
						repaireTimeRegistration();
					}
				}
			}
		}
	}// end of SelectData inner-class

	
	private class registerReservationTime extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/registerReservationTime.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJobNO()));
				arguments.add(new BasicNameValuePair("reservTmG", reserveDateTime));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {

					Log.v("returnJson01", returnJson01.toString());
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				item01 = null;
				try {
					item01 = new WO_TS00_R00_ITEM01(ej01.getValue("JOB_NO"),
							ej01.getValue("JOB_ST_NM"),
							ej01.getValue("AS_TP"),
							reserveDateTime);
				} catch (JSONException e) {
					e.printStackTrace();
					item01 = null;
				}

				progress(false);
				if(item01 != null){
					tv_fr_st.setText(item01.getJobStNm());
					tv_fr_csfr.setText(item01.getCsTmFr());
					workTargetData.setReservTm(item01.getCsTmFr());
					workTargetData.setSt(item01.getJobStNm());
					workTargetData.setCsFr(item01.getCsTmFr());
					setReserverDateTime(workTargetData.getReservTm());
				}
			}
		}
	}
	
	private void reservationTimeRegistration(String dateTime){
		reserveDateTime = dateTime;
		String dMsg = reserveDateTime + " " + this.getString(R.string.msg_reservationTimeRegistration);
		AlertView.confirm(context, "알림", dMsg,  new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			
				progress(true);
				new registerReservationTime().execute("bagicWorkTime");
			}
				});
	}
	
	private class TimeStatusChangeAsync extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

	
		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/updateWorkStatusChanged.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJobNO()));
				arguments.add(new BasicNameValuePair("jobAct", jobAct));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
					Log.v("returnJson01", returnJson01.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				item01 = null;
				try {
					item01 = new WO_TS00_R00_ITEM01(ej01.getValue("JOB_NO"),
							ej01.getValue("JOB_ST_NM"),
							ej01.getValue("AS_TP"),
							ej01.getValue("CS_TM_TO"));
					
				} catch (JSONException e) {
					e.printStackTrace();
					item01 = null;
				}

				progress(false);
				if(item01 != null){
					if(item01 != null){
						if(item01.getAsTp().equals("00")){
						String dateTime = DateUtil.nowDate() + " " + item01.getCsTmFr();
						if(jobAct.equals(TRAVEL_TIME)){
							workTargetData.setMoveTm(dateTime);
							tv_fr_travelTime.setText(dateTime);
							CallService.startTimerService(context);
						}else if(jobAct.equals(ARRIVAL_TIME)){
							workTargetData.setArriveTm(dateTime);
							tv_fr_arrivalTime.setText(dateTime);
							CallService.stopTimerService(context);
							CallService.startGPSService(context);
						}else if(jobAct.equals(COMPLETION_TIME)){
							workTargetData.setCompleteTm(dateTime);
							tv_fr_completionTime.setText(dateTime);
							
						}
						tv_fr_st.setText(item01.getJobStNm());
						tv_fr_csfr.setText(dateTime);
						workTargetData.setSt(item01.getJobStNm());
						workTargetData.setCsFr(dateTime);
						}else if(item01.getAsTp().equals("04")){
							showAlert("출동처리자가 없습니다.");
						}else{
							showAlert("작업을 처리할 수 없습니다.");
						}
					}
				}
			}
		}
	}
	private void timeRegistration(String jobAct) {
		
		String dMsg = "";
		onPopUp = true;
		
		///gps활성화 체크
				GPSService gs = new GPSService(context);
				if(!gs.isGetLocation()){
					gpsActivation();
					return;
				}
		
		if (jobAct.equals(TRAVEL_TIME)) {
			dMsg = this.getString(R.string.msg_travelTimeRegistration);
		} else if (jobAct.equals(ARRIVAL_TIME)) {
			dMsg = this.getString(R.string.msg_arrivalTimeRegistration);
		} else if (jobAct.equals(COMPLETION_TIME)) {
			dMsg = this.getString(R.string.msg_completionTimeRegistration);
		}
		this.jobAct = jobAct;
		android.content.DialogInterface.OnClickListener ocl = new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onPopUp = false;
				progress(true);
				new TimeStatusChangeAsync().execute("bagicWorkTime");
			}};
		showAlertYn(dMsg,ocl );
		
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
				.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						onPopUp = false;
					}
				})
				.show();
	}
	private class registerRepairCompletionTime extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

	
		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {
				String param_url = WebServerInfo.getUrl()
						+ "ip/registerRepairCompletionTime.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workTargetData.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  workTargetData.getJobNO()));
				arguments.add(new BasicNameValuePair("repairTmG", DateUtil.nowDateTime()));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				
				returnJson01 = http.getPost(param_url, arguments,
						true);

				try {
					Log.v("returnJson01", returnJson01.toString());
					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
					

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return params[0];
		}
		return "None";

		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				item01 = null;
				try {
					item01 = new WO_TS00_R00_ITEM01(ej01.getValue("JOB_NO"),
							ej01.getValue("JOB_ST_NM"),
							ej01.getValue("AS_TP"),
							ej01.getValue("REPAIR_TM"));
				} catch (JSONException e) {
					e.printStackTrace();
					item01 = null;
				}

				progress(false);
				if(item01 != null){
					tv_fr_csfr.setText(item01.getCsTmFr());
					workTargetData.setRepairTm(item01.getCsTmFr());
					workTargetData.setCsFr(item01.getCsTmFr());
					tv_fr_repaireCompletionTime.setText(item01.getCsTmFr());
					cb_fr_confined.setEnabled(true);
					cb_fr_respond.setEnabled(true);
					CallService.startGPSService(context);
				}
			}
		}
	}
	
	
	private void repaireTimeRegistration() {
		String dMsg = "";
		dMsg = this.getString(R.string.msg_repaireCompletionTimeRegistration);
		android.content.DialogInterface.OnClickListener ocl = new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onPopUp = false;
				progress(true);
				new registerRepairCompletionTime()
						.execute("bagicWorkTime");
			}};
		showAlertYn(dMsg,ocl );
	}

	private void searchDetail() {
		progress(true);
		new workTargetDetailAsync().execute("bagicWorkTime");
	}

	private void initDetailView() {
		tv_fr_date.setText("");
		tv_fr_workNm.setText("");
		tv_fr_bldgInfo.setText("");
		tv_fr_addr.setText("");
		tv_fr_csDeptNm.setText("");
		tv_fr_empNm1.setText("");
		tv_fr_empHp1.setText("");
		tv_fr_empNm2.setText("");
		tv_fr_empHp2.setText("");
		tv_fr_notifyNm.setText("");
		tv_fr_recevDesc.setText("");
		tv_fr_specialDesc.setText("");
		setReserverDateTime("");

		
		
		this.tv_fr_rescuetime.setVisibility(View.GONE);
		this.tv_fr_rescuetime.setText("");
		
		tv_fr_travelTime.setText("");
		tv_fr_arrivalTime.setText("");
		tv_fr_repaireCompletionTime.setText("");
		
		this.cb_fr_confined.setEnabled(false);
		this.cb_fr_respond.setEnabled(false);
		
		tv_fr_completionTime.setText("");
		tv_fr_st.setText("");
		tv_fr_csfr.setText("");
	}

	private void setDetailView() {
		initDetailView();
		tv_fr_date.setText(workTargetData.getWorkDt());
		tv_fr_workNm.setText(workTargetData.getWorkNm());
		tv_fr_bldgInfo.setText(workTargetData.getBldgNm() + " " + workTargetData.getCarNo());
		tv_fr_addr.setText(workTargetData.getAddr());
		tv_fr_csDeptNm.setText(workTargetData.getCsDetpNm());
		tv_fr_empNm1.setText(workTargetData.getMainEmpNm());
		tv_fr_empHp1.setText(workTargetData.getMainEmpPhone());
		tv_fr_empNm2.setText(workTargetData.getSubEmpNm());
		tv_fr_empHp2.setText(workTargetData.getSubEmpPhone());
		tv_fr_notifyNm.setText(workTargetData.getNotifyNm());
		tv_fr_recevDesc.setText(workTargetData.getRecevDesc());
		
		tv_fr_specialDesc.setText(workTargetData.getSpecialDesc());
		tv_fr_st.setText(workTargetData.getSt());
		tv_fr_csfr.setText(workTargetData.getCsFr());
		
		setReserverDateTime(workTargetData.getReservTm());

		tv_fr_travelTime.setText(workTargetData.getMoveTm());
		tv_fr_arrivalTime.setText(workTargetData.getArriveTm());
		
		tv_fr_repaireCompletionTime.setText(workTargetData.getRepairTm());
		
		if (workTargetData.getRepairTm() != null && !workTargetData.getRepairTm().equals("")) {
			this.cb_fr_confined.setEnabled(true);
			this.cb_fr_respond.setEnabled(true);
		}
		else{
			this.cb_fr_confined.setEnabled(false);
			this.cb_fr_respond.setEnabled(false);
		}
		
		tv_fr_completionTime.setText(workTargetData.getCompleteTm());
		this.cb_fr_confined.setChecked(workTargetData.isStatus());
		
		setRescueDateTime(workTargetData.getRescueTm());
		
		if(workTargetData.isStatus()){
			this.tv_fr_rescuetime.setVisibility(View.VISIBLE);
		}
		else
		{
			this.tv_fr_rescuetime.setVisibility(View.GONE);
		}
		
		this.cb_fr_respond.setChecked(workTargetData.isContact());
		if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
			this.btn_fr_aSProcReport.setText(StringUtil.padLeft("A/S 처리 보고서", "등록"));
		}
		else{
			this.btn_fr_aSProcReport.setText("A/S 처리 보고서");
		}

	}

	private void progress(Boolean isActivated) {
		progressWorking = isActivated;
		if (isActivated) {
			WO_TS00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
			
		} else {
			WO_TS00_R00.this.progress.dismiss();
		}
	}

	private String jobNo;
	private String workDt;
	private String jobAct;
	
	private int reserverHour;
	private int reserverMinute;
	private int reserveYear;
	private int reserveMonth;
	private int reserveDay;
	private String reserveDateTime;
	private void setReserverDateTime(String value){
		reserveDateTime = value;
		btn_fr_reserverTime.setText(StringUtil.padLeft(WO_TS00_R00.this
				.getString(R.string.btnstr_reserverTime), value));
		
		if (value.equals(null) || value.equals("")) {
			value = DateUtil.nowDateTime();
			Log.v("currnetDateTime", value);
		}
		
		reserveYear = DateUtil.getYear(value);
		reserveMonth = DateUtil.getMonth(value);
		reserveDay = DateUtil.getDay(value);
		reserverHour = DateUtil.getHour(value);
		reserverMinute = DateUtil.getMinute(value);
		Log.v("reserveMonth",String.valueOf(reserveMonth));
	}

	private int rescueHour;
	private int rescueMinute;
	private int rescueYear;
	private int rescueMonth;
	private int rescueDay;
	private String rescueDateTime;
	private void setRescueDateTime(String value){
		rescueDateTime = value;
		tv_fr_rescuetime.setText(StringUtil.padLeft(WO_TS00_R00.this
				.getString(R.string.btnstr_rescueTime), value));
		
		if (value.equals(null) || value.equals("")) {
			value = DateUtil.nowDateTime();
			Log.v("currnetDateTime", value);
		}
		
		rescueYear = DateUtil.getYear(value);
		rescueMonth = DateUtil.getMonth(value);
		rescueDay = DateUtil.getDay(value);
		rescueHour = DateUtil.getHour(value);
		rescueMinute = DateUtil.getMinute(value);
		Log.v("reserveMonth",String.valueOf(reserveMonth));
	}

	public CM_SearchBldgInfo_ITEM01 currentSelectedItem01;

	
	private WO_TS00_R03P_ITEM01 selectedItem03;
	private void setSelectedItem03(WO_TS00_R03P_ITEM01 item){
		this.selectedItem03 = item;
		tv_fr_aSProcCode.setText(this.selectedItem03.getCodeNm());
	}
	private List<WO_TS00_R04P_ITEM01> selectedItemList04;
	private void setSelectedItemList4(List<WO_TS00_R04P_ITEM01> itemList){
		selectedItemList04 = itemList;
		this.tv_fr_CbsNm1.setText(itemList.get(0).getAssyNm());
		this.tv_fr_CbsNm2.setText(itemList.get(1).getAssyNm());
		this.tv_fr_CbsNm3.setText(itemList.get(1).getAssyNm());
	}
	private WO_TS00_R07P_ITEM01 selectedItem07;
	private void setSelectedItem07(WO_TS00_R07P_ITEM01 item){
		this.selectedItem07 = item;
		tv_fr_falutNm.setText(this.selectedItem07.getFaultNm());
	}
	
	private WO_TS00_R08P_ITEM01 selectedItem08;
	private void setSelectedItem08(WO_TS00_R08P_ITEM01 item){
		this.selectedItem08 = item;
		tv_fr_procNm.setText(this.selectedItem08.getProcNm());
	}
	private WO_TS00_R09P_ITEM01 selectedItem09;
	private void setSelectedItem09(WO_TS00_R09P_ITEM01 item){
		this.selectedItem09 = item;
		tv_fr_dutyNm.setText(this.selectedItem09.getDutyNm());
	}
	
	private List<String> cpErrorCodeList;
	private void setCpErrorCodeList(List<String> itemList){
		this.cpErrorCodeList  = itemList;
		if(this.cpErrorCodeList.size() > 0){
			this.tv_fr_cpErrorCode.setText("등록");
		}
		else
		{
			this.tv_fr_cpErrorCode.setText("");
		}
	}
	
	private String selCode;
	
	
	public void showAlert(String message)
	{
		if(progressWorking){
			return;
		}
		
		if(!onPopUp){
			onPopUp = true;
		AlertView.showAlert(message, context,new android.content.DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					onPopUp = false;
				}
			});
		}
	}
	public void showAlertYn(String message, android.content.DialogInterface.OnClickListener ocl){
		if(progressWorking){
			return;
		}
		AlertView.confirmYN(context, "알림", message, ocl , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				onPopUp = false;
			}});
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
			
}
