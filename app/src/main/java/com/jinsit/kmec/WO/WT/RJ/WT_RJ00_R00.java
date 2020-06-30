package com.jinsit.kmec.WO.WT.RJ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SaveReadPicture;
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
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.StringUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class WT_RJ00_R00 extends Activity implements OnClickListener, OnDismissListener{

	//uiInstances
	private Context context;
	private EasyJsonMap ej01;
	
	private TextView tv01_rj_r00_date;
	private TextView tv02_rj_r00_workNm;
	private TextView tv03_rj_r00_carNo;
	private TextView tv04_rj_r00_addr;
	private TextView tv05_rj_r00_csDeptNm;
	private TextView tv06_rj_r00_majorEng_key;
	private TextView tv07_rj_r00_majorEngNm;
	private TextView tv08_rj_r00_majorEngPhone;
	private TextView tv09_rj_r00_minorEng_key;
	private TextView tv10_rj_r00_minorEngNm;
	private TextView tv11_rj_r00_minorEngPhone;
	private TextView tv12_rj_r00_orderNo_key;
	private TextView tv13_rj_r00_orderNo_value;
	private TextView tv14_rj_r00_status;
	private TextView tv15_rj_r00_timeToMove;
	private TextView tv17_rj_r00_timeToArrive;
	private TextView tv19_rj_r00_timeToComplete;
	private TextView tv20_rj_r00_beforeAfterPhoto;
	private TextView tv21_rj_r00_makeConfirmationSheet;
	private TextView btn01_rj_r00_adminInfo;
	private TextView btn02_rj_r00_register;	
	private TextView tv15_rj_r00_csfr;
	
	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonMap ejm02;
	private EasyJsonMap ejm03;
	
	//dto
	private Map<String, String> dbMap;
	private String engSt;
	private String engNm;

	//utils
	private ProgressDialog progress;
	private static final String READY 	   = "00";
	private static final String MOVING 	   = "11";
	private static final String ARRIVAL    = "31";
	private static final String COMPLETION = "39";
	
	
	private static final int REGISTER_PEND = 0;
	private static final int REGISTER_TRANSFER = 1;
	private static final int REGISTER_HELP = 2;


	private CommonSession commonSession;
	private NFCRegistrationData nFCRegistrationData;

	// /nfc
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private boolean progressWorking;
	private boolean onPopUp;
	// ////
	
	private String custSign;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("작업대상정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		setContentView(R.layout.wt_rj00_r00);
		getInstances();
	}
	@Override
	protected void onNewIntent(Intent intent) {
		
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
	private void getInstances(){
		context								= this;
		dbMap								= new HashMap<String, String>();
		tv01_rj_r00_date					= (TextView) findViewById(R.id.tv01_rj_r00_date);
		tv02_rj_r00_workNm					= (TextView) findViewById(R.id.tv02_rj_r00_workNm);
		tv03_rj_r00_carNo					= (TextView) findViewById(R.id.tv03_rj_r00_carNo);
		tv04_rj_r00_addr					= (TextView) findViewById(R.id.tv04_rj_r00_addr);
		tv05_rj_r00_csDeptNm				= (TextView) findViewById(R.id.tv05_rj_r00_csDeptNm);
		tv06_rj_r00_majorEng_key			= (TextView) findViewById(R.id.tv06_rj_r00_majorEng_key);
		tv07_rj_r00_majorEngNm				= (TextView) findViewById(R.id.tv07_rj_r00_majorEngNm);
		tv08_rj_r00_majorEngPhone			= (TextView) findViewById(R.id.tv08_rj_r00_majorEngPhone);
		tv09_rj_r00_minorEng_key			= (TextView) findViewById(R.id.tv09_rj_r00_minorEng_key);
		tv10_rj_r00_minorEngNm				= (TextView) findViewById(R.id.tv10_rj_r00_minorEngNm);
		tv11_rj_r00_minorEngPhone			= (TextView) findViewById(R.id.tv11_rj_r00_minorEngPhone);
		tv12_rj_r00_orderNo_key				= (TextView) findViewById(R.id.tv12_rj_r00_orderNo_key);
		tv13_rj_r00_orderNo_value			= (TextView) findViewById(R.id.tv13_rj_r00_orderNo_value);
		tv14_rj_r00_status					= (TextView) findViewById(R.id.tv14_rj_r00_status);
		tv15_rj_r00_csfr                    = (TextView) findViewById(R.id.tv15_rj_r00_csfr);
		btn01_rj_r00_adminInfo				= (TextView) findViewById(R.id.btn01_rj_r00_adminInfo);
		tv15_rj_r00_timeToMove				= (TextView) findViewById(R.id.tv15_rj_r00_timeToMove);
		tv17_rj_r00_timeToArrive			= (TextView) findViewById(R.id.tv17_rj_r00_timeToArrive);
		tv19_rj_r00_timeToComplete			= (TextView) findViewById(R.id.tv19_rj_r00_timeToComplete);
		tv20_rj_r00_beforeAfterPhoto		= (TextView) findViewById(R.id.tv20_rj_r00_beforeAfterPhoto);
		tv21_rj_r00_makeConfirmationSheet	= (TextView) findViewById(R.id.tv21_rj_r00_makeConfirmationSheet);
		btn02_rj_r00_register				= (TextView) findViewById(R.id.btn02_rj_r00_register);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		ndefMsg = "";
		tagUid = "";
		// /
		progressWorking = false;
		onPopUp =false;
		setEvents();
	}

	private void setEvents(){
		
		btn01_rj_r00_adminInfo.setOnClickListener(this);
		tv15_rj_r00_timeToMove.setOnClickListener(this);
		tv17_rj_r00_timeToArrive.setOnClickListener(this);
		tv19_rj_r00_timeToComplete.setOnClickListener(this);
		tv20_rj_r00_beforeAfterPhoto.setOnClickListener(this);
		tv21_rj_r00_makeConfirmationSheet.setOnClickListener(this);
		btn02_rj_r00_register.setOnClickListener(this);		
		setConfig();
	}
	private void setConfig(){
		dbMap.put("jobNo" , getIntent().getExtras().getString("jobNo") );
		dbMap.put("workDt", getIntent().getExtras().getString("workDt") );
		dbMap.put("bldgNo", "");
		dbMap.put("jobAct", "");
		
		commonSession = new CommonSession(this);
		setTitle("작업대상정보");
//		WT_RJ00_R00.this.progress = 
//				  android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		progress(true);
		new Database().execute("selectDetailsForOverhaul");
		ActivityAdmin.getInstance().addActivity(this);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_ip_wo_r00, menu);
		menu.removeItem(R.id.menu_ip_wo_helpWork);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		// String[] param = {empId, WO_Test_Data.getDate(), jobNo};
		String[] param = { commonSession.getEmpId() , dbMap.get("workDt").toString() , dbMap.get("jobNo").toString() };
		if (id == R.id.menu_ip_wo_pendWork) {
			// /미처리

			SelectPendCode selectPendCode = new SelectPendCode(context, param, this);
			selectPendCode.show();

			return true;
		} else if (id == R.id.menu_ip_wo_transferWork) {
			
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_TRANSFER, param, this);
			cM_SelectInspector.show();
			return true;
		} else if (id == R.id.menu_ip_wo_helpWork) {
			// 지원요청
			CM_SelectInspector cM_SelectInspector = new CM_SelectInspector(
					context, REGISTER_HELP, param, this);
			cM_SelectInspector.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {	
		switch (v.getId()) {
		//관리자정보
		
		case R.id.btn01_rj_r00_adminInfo:
			showAdminInfo();
			break;
		//이동시간
		case R.id.tv15_rj_r00_timeToMove:
//			if(!gs.isGetLocation()){
//				gpsActivation();
//				return;
//			}
			setTimeToMove();
			break;
			
		//도착시간
		case R.id.tv17_rj_r00_timeToArrive:
			setTimeToArrive();
			break;
			
		//완료시간
		case R.id.tv19_rj_r00_timeToComplete:
			
			break;
		//작업 전후 사진
		case R.id.tv20_rj_r00_beforeAfterPhoto:
			if(engSt.equals(ARRIVAL)){
			showBeforeAfterPhotos();
			}else{
				showAlert("도착 시간을 넣어주세요");
			}
			break;
		//작업완료 확인서 발행
		case R.id.tv21_rj_r00_makeConfirmationSheet:
			makeConfirmationSheet();
			break;
		//등록
		case R.id.btn02_rj_r00_register:
			registerJobAsCompleted();
			break;
		default:
			Log.e("[개발자Msg]", "out of case");
			break;
		}
		
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
				.setOnDismissListener(this)
				.show();
	}

	private void setTimeToMove(){
		if (manageEngStatus(MOVING)) {
			String dMsg = "";
			dMsg = this.getString(R.string.msg_travelTimeRegistration);
			showAlertYn(dMsg,
					new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
//					WT_RJ00_R00.this.progress = android.app.ProgressDialog
//							.show(context, "알림", "변경 중입니다...");
					progress(true);
					dbMap.put("jobAct", MOVING);
					new Database().execute("setEngineerStatus");
				}
			});

		}
	}
	private void setTimeToArrive(){
		if (manageEngStatus(ARRIVAL)) {
			String dMsg = "";
			dMsg = this.getString(R.string.msg_arrivalTimeRegistration);
			showAlertYn(dMsg,new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
//							WT_RJ00_R00.this.progress = android.app.ProgressDialog
//									.show(context, "알림", "변경 중입니다...");
							progress(true);
							dbMap.put("jobAct", ARRIVAL);
							new Database().execute("setEngineerStatus");
						}
						});
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
				arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
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
							
								if(tv15_rj_r00_timeToMove.getText().toString().equals("")){
									//이동
									setTimeToMove();
								}else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
										tv17_rj_r00_timeToArrive.getText().toString().equals("")) {
									//도착
									showAlert("사무실 NFC TAG로는 도착할 수 없습니다.");
								
								}else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
										!tv17_rj_r00_timeToArrive.getText().toString().equals("")&&
										tv19_rj_r00_timeToComplete.getText().toString().equals("")){
									showAlert("사무실 NFC TAG로는 작업을 완료할 수 없습니다.");
									
								}
								else{
									////계획모드에서만 예약을 할 수 있습니다.
									SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
									sm01.show();
								}
							// 사무실 태그
							/*progress(false);
							if (manageEngStatus(MOVING)) {
								setTimeToMove();
							} else if (manageEngStatus(ARRIVAL)) {
								showAlert("사무실 NFC TAG로는 도착할 수 없습니다.");
							} */
						} else if (rtn.equals("2")) {
							// 회의실 태그
							progress(false);
							
							if(tv15_rj_r00_timeToMove.getText().toString().equals("")){
								//이동
								setTimeToMove();
							}else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
									tv17_rj_r00_timeToArrive.getText().toString().equals("")) {
								//도착
								showAlert("회의실 NFC TAG로는 도착할 수 없습니다.");
							
							}else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
									!tv17_rj_r00_timeToArrive.getText().toString().equals("")&&
									tv19_rj_r00_timeToComplete.getText().toString().equals("")){
								showAlert("회의실 NFC TAG로는 작업을 완료할 수 없습니다.");
								
							}
							else{
								////계획모드에서만 예약을 할 수 있습니다.
								SimpleDialog sm01 = new SimpleDialog(context, "알림","시작을 먼저 하십시오.");
								sm01.show();
							}
							
							/*progress(false);
							if (manageEngStatus(MOVING)) {
								setTimeToMove();
							} else if (manageEngStatus(ARRIVAL)) {
								showAlert("회의실 NFC TAG로는 수리 완료할 수 없습니다.");
							}*/

						} else if (rtn.equals("3")) {
							// 빌딩테그
							if (tv15_rj_r00_timeToMove.getText().toString().equals("")){
								progress(false);
								setTimeToMove();
							} else if (!tv15_rj_r00_timeToMove.getText().toString().equals("")||
									tv17_rj_r00_timeToArrive.getText().toString().equals("")) {
								new selectNfcInfo().execute("bagicWorkTime");
							}
							
							
							/*progress(false);
							if (manageEngStatus(MOVING)) {
								setTimeToMove();
							} else if (manageEngStatus(ARRIVAL)) {
								new selectNfcInfo().execute("bagicWorkTime");
							}*/
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
							ej01.getValue("BLDG_NM"), 
							ej01.getValue("CAR_NO"),
							ej01.getValue("DONG_CAR_NO"), 
							ej01.getValue("NFC_PLC"),
							ej01.getValue("NFC_PLC_NM"),
							ej01.getValue("GOOD_NM"), 
							ej01.getValue("NFC_TAG"));
				} catch (JSONException e) {
					e.printStackTrace();
					nFCRegistrationData = null;
				}
				progress(false);
				
				if (nFCRegistrationData != null){
					Log.v("bldgNo",nFCRegistrationData.getBLDG_NO());
					Log.v("bldgNo1",dbMap.get("bldgNo"));
					Log.v("bldgNo",nFCRegistrationData.getCAR_NO());
					Log.v("carNo1",tv03_rj_r00_carNo.getText().toString());
					Log.v("tag", tagUid);
					if ( !nFCRegistrationData.getBLDG_NO().equals(dbMap.get("bldgNo")) || !nFCRegistrationData.getCAR_NO().equals(tv03_rj_r00_carNo.getText().toString())){
						showAlert("현재 빌딩정보와 Nfc Tag 빌딩정보가 틀립니다.");
					}
				
					else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
							tv17_rj_r00_timeToArrive.getText().toString().equals("")){
						setTimeToArrive();
					}
					else if(!tv15_rj_r00_timeToMove.getText().toString().equals("")&&
							!tv17_rj_r00_timeToArrive.getText().toString().equals("")){

						registerJobAsCompleted();
					}
					
					/*else if (manageEngStatus(ARRIVAL)){
						 setTimeToArrive();
					}*/
				}
			}
		}
	}// end of SelectData inner-class

	
	private void showAdminInfo(){
		SearchAdminInfo sai = new SearchAdminInfo( context
			   									  ,dbMap.get("bldgNo").toString());
		sai.show();
	}
	private void showBeforeAfterPhotos(){
		Intent intent = new Intent(WT_RJ00_R00.this, CM_SaveReadPicture.class);
		intent.putExtra("jobNo",dbMap.get("jobNo").toString());
		intent.putExtra("workDt", dbMap.get("workDt").toString());
		intent.putExtra("refContrNo", dbMap.get( "refContrNo").toString());
		intent.putExtra("selTp", "2");
		startActivity(intent);;
		
	}
	private void makeConfirmationSheet(){
		
		if(engSt.equals(ARRIVAL)){
			
			Intent i01 =  new Intent(context, WT_RJ01_R00.class);
			try {
				i01.putExtra("workDt", ejm02.getValue("WORK_DT"));
				i01.putExtra("jobNo" , ejm02.getValue("JOB_NO"));
				i01.putExtra("workNm" , ejm02.getValue("WORK_NM"));
				i01.putExtra("jobSt" , engSt);
				
				i01.putExtra("custSign" , custSign);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("[개발자Msg]", "WT_RJ00_R00 \n makeConfirmationSheet() Occured");
			}
			startActivityForResult(i01, 0);
			//startActivity(i01);
			
		}else{
			showAlert("작업이 완료되지 않았습니다.");
		}
	}
	
	/**
	 * @author 위의 작업완료 확인서 발행시 화면을 갱신해줘야함
	 * 작업완료 확인서가 발생됐으면 '발행'이라고 표시해 줘야되니
	 * 
	 */
	private boolean isConfirmCode = false;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				boolean isConfirm = bundle.getBoolean("confirmCode");
				isConfirmCode = isConfirm;
				if(isConfirm){
					refresh();
				}
				
				break;

			}

		}

	}
	private void refresh() {
		// TODO Auto-generated method stub
		progress(true);
		new Database().execute("selectDetailsForOverhaul");
	}
	private void registerJobAsCompleted(){
		
		if(engSt.equals(ARRIVAL)&&
				tv21_rj_r00_makeConfirmationSheet.getText().toString().contains("등록")){
			String dMsg = "";
			dMsg = this.getString(R.string.msg_completionTimeRegistration);
			AlertView.confirm(context, "알림", dMsg,
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
//							WT_RJ00_R00.this.progress = android.app.ProgressDialog
//									.show(context, "알림", "변경 중입니다...");
							progress(true);
							dbMap.put("jobAct", COMPLETION);
							new Database().execute("setEngineerStatus");
						}
					});

		}else{
			
			if(!engSt.equals(ARRIVAL)){
				showAlert("도착을 먼저 하십시오.");	
				return;
			}else {
				showAlert("작업완료 확인서를 발행하십시오.");
			}
			
		}		
	}
	
	
	
	private boolean manageEngStatus(String nextStep){
		
		//moving
		if(nextStep.equals("11")){
			if(engSt.equals("11")){
				showAlert("이미 이동상태입니다.");
				return false;
			}else if(!engSt.equals("00")){
				showAlert("작업자상태가 준비가 아닙니다.");
				return false;
			}
		//arrival
		}else if(nextStep.equals("31")){
			if(engSt.equals("31")){
				showAlert("이미 도착상태입니다.");
				return false;
			}else if(!engSt.equals("11")){
				showAlert("작업자상태가 이동이 아닙니다.");
				return false;
			}			
		//completion
		}else if(nextStep.equals("39")){
			if(engSt.equals("39")){
				showAlert("이미 완료상태입니다.");
				return false;
			}else if(!engSt.equals("31")){
				showAlert("작업자상태가 도착이 아닙니다.");
				return false;
			}
		}
		///gps활성화 체크
		GPSService gs = new GPSService(context);
		if(!gs.isGetLocation()){
			gpsActivation();
			return false;
		}
		return true;
	}
	
	
	private class Database extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}
	private void crud(String div){
		
		if(div.equals("selectDetailsForOverhaul")){
			
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/"+div+".do";
			
			CommonSession cs = new CommonSession(context);
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.clear();
				arguments.add(new BasicNameValuePair("csEmpId", cs.getEmpId() ));
				arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
				arguments.add(new BasicNameValuePair("jobNo", dbMap.get("jobNo") ));
				
			returnJson = http.getPost(argUrl, arguments, true);
			
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			argUrl = WebServerInfo.getUrl()+"ip/selectCustomerApproval.do";
			arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("empId", cs.getEmpId() ));
			arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt").toString()));
			arguments.add(new BasicNameValuePair("jobNo", dbMap.get("jobNo") ));
			returnJson = http.getPost(argUrl, arguments, true);
			try {
				custSign = "";
				ejm03 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				custSign = ejm03.getValue("CUST_SIGN");
			} catch (JSONException e) {
				e.printStackTrace();
				custSign = "";
			}
			
		}else if(div.equals("setEngineerStatus")){
			
			GetHttp http = new GetHttp();
			String argUrl = WebServerInfo.getUrl()+"ip/"+div+".do";
			
			CommonSession cs = new CommonSession(context);
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.clear();
				arguments.add(new BasicNameValuePair("empId" , cs.getEmpId() ));
				arguments.add(new BasicNameValuePair("workDt", dbMap.get("workDt")));
				arguments.add(new BasicNameValuePair("jobNo" , dbMap.get("jobNo") ));
				arguments.add(new BasicNameValuePair("jobAct", dbMap.get("jobAct") ));
				arguments.add(new BasicNameValuePair("usrId" , cs.getEmpId() ));
				
			returnJson = http.getPost(argUrl, arguments, true);
			
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejm02 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}else if(div.equals("setTimeToArrive")){
			
			
		}else if(div.equals("setTimeToComplete")){
			
			
		}
		
	}
	private void setData(String div){
		
		if(div.equals("selectDetailsForOverhaul")){
			progress(false);
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					tv01_rj_r00_date.setText(ejm02.getValue("WORK_DT"));
					tv02_rj_r00_workNm.setText(ejm02.getValue("WORK_NM"));
					tv03_rj_r00_carNo.setText(ejm02.getValue("CAR_NO"));
					tv04_rj_r00_addr.setText(ejm02.getValue("ADDR"));
					tv05_rj_r00_csDeptNm.setText(ejm02.getValue("CS_DETP_NM"));
					tv07_rj_r00_majorEngNm.setText(ejm02.getValue("MAIN_EMP_NM"));
					tv08_rj_r00_majorEngPhone.setText(ejm02.getValue("MAIN_EMP_PHONE"));
					tv10_rj_r00_minorEngNm.setText(ejm02.getValue("SUB_EMP_NM"));
					tv11_rj_r00_minorEngPhone.setText(ejm02.getValue("SUB_EMP_PHONE"));
					
					if(tv02_rj_r00_workNm.getText().toString().equals("Claim")){ //클레이임이면 refcontr넘버
						tv13_rj_r00_orderNo_value.setText(ejm02.getValue("REF_CONTR_NO"));	
					}else{//나머지는 그대로 파츠넘버
						tv13_rj_r00_orderNo_value.setText(ejm02.getValue("PARTS_NO"));	
					}
					
					tv14_rj_r00_status.setText(ejm02.getValue("ST"));
					tv15_rj_r00_csfr.setText(ejm02.getValue("CS_FR"));
					engSt = ejm02.getValue("JOB_ST");
					engNm = ejm02.getValue("ST");
					dbMap.put( "bldgNo", ejm02.getValue("BLDG_NO") );
					dbMap.put( "refContrNo", ejm02.getValue("REF_CONTR_NO") );
					
					tv15_rj_r00_timeToMove.setText( ejm02.getValue("MOVE_TM") );
					tv17_rj_r00_timeToArrive.setText( ejm02.getValue("ARRIVE_TM") );
					tv19_rj_r00_timeToComplete.setText( ejm02.getValue("COMPLETE_TM") );

					if(tv02_rj_r00_workNm.getText().toString().equals("수리공사")){
						//수리공사일때 
					if(custSign != null && !custSign.equals("")){
						tv21_rj_r00_makeConfirmationSheet.setText(StringUtil.padLeft("작업완료 확인서 발행" , "등록") );
					}
					else{
						tv21_rj_r00_makeConfirmationSheet.setText("작업완료 확인서 발행");
					}
					
					}else{//수리공사가 아니면
						if(isConfirmCode){
							tv21_rj_r00_makeConfirmationSheet.setText(StringUtil.padLeft("작업완료 확인서 발행" , "등록") );
						}
						else{
							tv21_rj_r00_makeConfirmationSheet.setText("작업완료 확인서 발행");
						}
					}
					
				}else if(isError){
					showAlert(ejm01.getValue("errMsg"));
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if(div.equals("setEngineerStatus")){
			progress(false);
			try {
				boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					//dbMap.put("jobAct", COMPLETION);
					
					if(dbMap.get("jobAct").equals(COMPLETION)){
						//showAlert("작업이 완료되었습니다.");
						CallService.startGPSService(context);
						CallService.allStopService(context);
						Toast.makeText(context, "작업이 완료되었습니다.", 2000).show();
						finish();
					}else{
						if(dbMap.get("jobAct").equals(MOVING)){
							CallService.startTimerService(context);
						}else if(dbMap.get("jobAct").equals(ARRIVAL)){
							CallService.stopTimerService(context);
							CallService.startGPSService(context);
						}
						progress(true);
						new Database().execute("selectDetailsForOverhaul");	
					}
					
				}else if(isError){
					showAlert("<Procedure Error>\n"+ejm01.getValue("errMsg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void progress(Boolean isActivated) {
		progressWorking = isActivated;
		if (isActivated) {
			WT_RJ00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
			
		} else {
			WT_RJ00_R00.this.progress.dismiss();
		}
	}
	
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
		@Override
		public void onDismiss(DialogInterface dialog) {
			// TODO Auto-generated method stub
			onPopUp=false;
		}
		//----------------------내비게이션 영역--------------------------------------//
		
};