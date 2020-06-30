package com.jinsit.kmec.IR.NM;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_NM02_R00 extends Activity implements OnClickListener {

	private TextView tv_nm_nfcNumber;
	private TextView tv_nm_nfcBldg;
	private TextView tv_nm_nfcCarNo;
	private TextView tv_nm_nfcPosition;
	private TextView btn_nm_nfcBldg;
	private TextView btn_nm_nfcCarNo;
	private TextView btn_nm_nfcPosition;


	private EditText et_nm_nfcDonCarNo;
	private EditText et_nm_nfckeSafeNo;
	private Spinner sp_nm_nfcSubstitute;
	TextView btn_save;
	// ---widget---//

	String tagUid = "", bldgNo = "", carNo = "", nfcPlc = "", mngNo = "";
	String ndefMsg;
	Context context;
	ArrayList<PartData> partData;
	CarInformation carInfoData;
	private NFCRegistrationData item01;
	private EasyJsonMap ej01;

	private ProgressDialog progress;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	boolean mWriteMode = false;
	private DialogInterface writeNfcDlg = null;
	private String[] item = {"A","N","S"};
	private String substituteCodeNm,substituteCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_nm02_r00);

		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("NFC 등록");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		activityInit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		tv_nm_nfcNumber = (TextView) findViewById(R.id.tv_nm_nfcNumber);

		tv_nm_nfcBldg = (TextView) findViewById(R.id.tv_nm_nfcBldg);
		btn_nm_nfcBldg = (TextView) findViewById(R.id.btn_nm_nfcBldg);
		btn_nm_nfcBldg.setOnClickListener(this);
		tv_nm_nfcCarNo = (TextView) findViewById(R.id.tv_nm_nfcCarNo);
		btn_nm_nfcCarNo = (TextView) findViewById(R.id.btn_nm_nfcCarNo);
		btn_nm_nfcCarNo.setOnClickListener(this);
		tv_nm_nfcPosition = (TextView) findViewById(R.id.tv_nm_nfcPosition);
		btn_nm_nfcPosition = (TextView) findViewById(R.id.btn_nm_nfcPosition);
		btn_nm_nfcPosition.setOnClickListener(this);

		et_nm_nfcDonCarNo = (EditText)findViewById(R.id.et_nm_nfcDonCarNo);
		et_nm_nfckeSafeNo = (EditText)findViewById(R.id.et_nm_nfckeSafeNo);

		sp_nm_nfcSubstitute = (Spinner)findViewById(R.id.sp_nm_nfcSubstitute);

		btn_save = (TextView) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);

		tagUid = getIntent().getExtras().get("tagUid").toString();
		ndefMsg = getIntent().getExtras().get("ndefMsg").toString();
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		spinnerAdapterInit();
		tv_nm_nfcNumber.setText(tagUid);
		setConfig();
	}

	void spinnerAdapterInit(){
		new ReplaceCarIfAsync().execute();
	}


	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_nm_nfcBldg:
				searchBldg();
				break;
			case R.id.btn_nm_nfcCarNo:
				if(this.bldgNo == null || this.bldgNo.equals(""))
				{
					showAlert("빌딩을 먼저 선택해주십시오");
					return;
				}
				searchElev();
				break;
			case R.id.btn_nm_nfcPosition:
				if(this.bldgNo == null || this.bldgNo.equals(""))
				{
					showAlert("빌딩을 먼저 선택해주십시오");
					return;
				}
				if(this.carNo == null || this.carNo.equals(""))
				{
					showAlert("호기를 선택해주십시오");
					return;
				}
				progress(true);
				new NFCPositionAsync().execute();
				break;

			case R.id.btn_save:
				registrationNFC();
				break;
		}
	}

	private void registrationNFC() {
		if(this.bldgNo == null || this.bldgNo.equals(""))
		{
			showAlert("빌딩을 먼저 선택해주십시오");
			return;
		}
		if(this.carNo == null || this.carNo.equals(""))
		{
			showAlert("호기를 선택해주십시오");
			return;
		}
		if(this.nfcPlc == null  || this.nfcPlc.equals(""))
		{
			showAlert( "위치를 선택해주십시오");
			return;
		}

		writeNFCTag();
	}

	private void writeNFCTag()
	{

		mNfcAdapter = NfcAdapter.getDefaultAdapter(IR_NM02_R00.this);
		mNfcPendingIntent = PendingIntent.getActivity(IR_NM02_R00.this, 0,
				new Intent(IR_NM02_R00.this, IR_NM02_R00.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		enableTagWriteMode();
		AlertDialog.Builder alertDialog;
		alertDialog = new AlertDialog.Builder(IR_NM02_R00.this);
		alertDialog.setTitle("NFC 태그를 가까지 가져가 주십시오");
		alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				disableTagWriteMode();
			}

		});
		alertDialog.create();
		writeNfcDlg = alertDialog.show();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Tag writing mode
		if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String tuId = NfcParser.getTagId(detectedTag);
			if(!tuId.equals(tagUid)){
				showAlert("등록 실패! 다른 NFC 태그입니다. ");
				return;
			}
			String tagText = new DateUtil().getCurrentShortDate() + "&&&" + IR_NM02_R00.this.carNo + "&&&" + IR_NM02_R00.this.nfcPlc;
			NdefRecord record = createTextRecord(tagText, Locale.KOREA, true) ;
			NdefMessage message = new NdefMessage(new NdefRecord[] { record });
			if (writeTag(message, detectedTag)) {
				writeNfcDlg.dismiss();
				progress(true);
				new NFCRegistrationAsync().execute();
			}
		}
	}



	public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
		byte[] textBytes = payload.getBytes(utfEncoding);
		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
		NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], data);
		return record;
	}

	/*
	 * Writes an NdefMessage to a NFC tag
	 */
	public boolean writeTag(NdefMessage message, Tag tag) {
		int size = message.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();
				if (!ndef.isWritable()) {
					Toast.makeText(getApplicationContext(),
							"Error: tag not writable",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				if (ndef.getMaxSize() < size) {
					Toast.makeText(getApplicationContext(),
							"Error: tag too small",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				ndef.writeNdefMessage(message);
				return true;
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);
						return true;
					} catch (IOException e) {
						return false;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
	}

	private void enableTagWriteMode() {
		mWriteMode = true;
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	}

	private void disableTagWriteMode() {
		mWriteMode = false;
		mNfcAdapter.disableForegroundDispatch(this);
	}

	private void searchElev() {

		if(this.bldgNo == null || this.bldgNo.equals(""))
		{
			showAlert("빌딩을 먼저 선택해주십시오");
			return;
		}
		final CM_SearchElev elev = new CM_SearchElev(context, bldgNo);
		elev.show();
		elev.elevSearch();
		elev.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				carNo = elev.getReturnStr();
				tv_nm_nfcCarNo.setText(elev.getReturnStr());
				nfcPlc ="";
				tv_nm_nfcPosition.setText("");

			}
		});
	}

	private void searchBldg() {
		final CM_SearchBldg cmBldg = new CM_SearchBldg(context);
		cmBldg.show();
		cmBldg.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				bldgNo = cmBldg.getBldgNo();
				if (bldgNo.equals("")) {
				} else {
					tv_nm_nfcBldg.setText(cmBldg.getBldgNm());
					carNo = "";
					tv_nm_nfcCarNo.setText("");
					nfcPlc ="";
					tv_nm_nfcPosition.setText("");
				}
			}
		});
	}

	public class NFCPositionAsync extends AsyncTask<String, Integer, String> {


		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				partData = new ArrayList<PartData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectNFCPosition.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("bldgNo",bldgNo));
				arguments.add(new BasicNameValuePair("carNo",carNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				try {
					EasyJsonList ejl = new EasyJsonList(
							returnJson.getJSONArray("dataList"));

					partData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						partData.add(new PartData(ejl.getValue(i, "CODE_CD"),
								ejl.getValue(i, "CODE_NM")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					showAlert("호기 위치정보를 불러오는 것을 실패했습니다. "+e.getMessage());
				}

			} catch (Exception ex) {
				showAlert("호기 위치정보를 불러오는 것을 실패했습니다. "+ex.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			progress(false);
			positionPopUp();

		}
	}

	private ArrayList<ReplaceCarData> replaceCarData;
	public class ReplaceCarIfAsync extends AsyncTask<String, Integer, String> {


		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress(true);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				replaceCarData = new ArrayList<ReplaceCarData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectReplaceCarIf.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				try {
					EasyJsonList ejl = new EasyJsonList(
							returnJson.getJSONArray("dataList"));

					replaceCarData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						replaceCarData.add(new ReplaceCarData(ejl.getValue(i, "REPL_CAR_IF"),
								ejl.getValue(i, "REPL_CAR_IF_NM")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
					showAlert("대체호기 정보를 불러오는 것을 실패했습니다. "+e.getMessage());
				}

			} catch (Exception ex) {
				showAlert("대체호기 정보를 불러오는 것을 실패했습니다. "+ex.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress(false);
			String[] item=null;
			if(replaceCarData.size()!=0){
				item = new String[replaceCarData.size()];
				for(int i=0;i<replaceCarData.size();i++){
					item[i] = replaceCarData.get(i).getREPL_CAR_IF_NM();
				}
				IR_NM02_R00.this.item = item;
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, item);
			sp_nm_nfcSubstitute.setAdapter(adapter);
			sp_nm_nfcSubstitute.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int position, long arg3) {
					substituteCodeNm = sp_nm_nfcSubstitute.getItemAtPosition(position).toString();
					substituteCode = replaceCarData.get(position).getREPL_CAR_IF();
					//String.valueOf(position);
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}
			});
			sp_nm_nfcSubstitute.setSelection(replaceCarData.size()-1);

		}
	}


	/**
	 * 빌딩번호와 카넘버를 가지고 dongCarno , 관리원번호, 대체호기 정보를 가져온다.
	 * @author 원성민
	 *
	 */
	public class CarInformationAsync extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				carInfoData = new CarInformation();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectCarInfo.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("bldgNo",bldgNo));
				arguments.add(new BasicNameValuePair("carNo",carNo));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				try {
					ej01 = new EasyJsonMap( returnJson.getJSONObject("dataMap"));
					carInfoData.setDONG_CAR_NO(ej01.getValue("DONG_CAR_NO"));
					carInfoData.setMNG_NO(ej01.getValue("MNG_NO"));
					carInfoData.setREPL_CAR_IF(ej01.getValue("REPL_CAR_IF"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				//showAlert("호기 위치정보를 불러오는 것을 실패했습니다. "+ex.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress(false);
			et_nm_nfcDonCarNo.setText(carInfoData.getDONG_CAR_NO());
			et_nm_nfckeSafeNo.setText(carInfoData.getMNG_NO());
			int position=0;
			for(int i=0;i<replaceCarData.size();i++){
				if(replaceCarData.get(i).getREPL_CAR_IF().equals(carInfoData.getREPL_CAR_IF())){
					position = i;
					substituteCodeNm = item[i];
					substituteCode = replaceCarData.get(position).getREPL_CAR_IF();
					break;
				}
			}

			sp_nm_nfcSubstitute.setSelection(position);

		}
	}



	public class NFCRegistrationAsync extends AsyncTask<String, Integer, String> {

		JSONObject returnJson01;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/registrationNFC.do";

				String mngNo = et_nm_nfckeSafeNo.getText().toString();
				if(mngNo != null)
				{
					mngNo = mngNo.trim();
				}
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("nfcTagNo", tagUid));
				arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
				arguments.add(new BasicNameValuePair("carNo", carNo));
				arguments.add(new BasicNameValuePair("nfcPlc", nfcPlc));
				arguments.add(new BasicNameValuePair("dongCarNo", et_nm_nfcDonCarNo.getText().toString()));//동카넘버
				arguments.add(new BasicNameValuePair("mngNo", mngNo)); //승강원번호
				arguments.add(new BasicNameValuePair("replCarIf", substituteCode));//대체호기여부
				//mngNo가 승강원 번호인데 일단은 파라미터가 이렇게 되어있으니 나중에 인풋파라미터 추가시 변경하도록 한다.
				//밑의 keSafeNo가 승강원 번호이다.
				//arguments.add(new BasicNameValuePair("dongCarNo", et_nm_nfcDonCarNo.getText().toString()));
				//arguments.add(new BasicNameValuePair("keSafeNo", et_nm_nfckeSafeNo.getText().toString()));
				//arguments.add(new BasicNameValuePair("substituteCodeNm", substituteCodeNm));

				returnJson01 = getHttp.getPost(param_url, arguments,
						true);
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progress(false);
			String retrunValue = "N";
			try {
				retrunValue = returnJson01.getString("dataString");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (retrunValue.equals("1")) {
				AlertView.showAlert("성공적으로 등록이 되었습니다.", context,new android.content.DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						btn_save.setEnabled(false);
						finish();
					}
				});
			} else if (retrunValue.equals("0")) {
				showAlert("등록실패했습니다. 재등록해주시길 바랍니다.");
			} else if (retrunValue.equals("2")) {
				showAlert("이미 등록된 nfc 입니다.");
			}else if(retrunValue.equals("3")){
				showAlert("NFC Tag가 사용된 Tag입니다.");
			}else {
				showAlert("등록실패했습니다. 재등록해주시길 바랍니다.");
			}
		}
	}

	private void positionPopUp() {
		final String pItem[] = new String[partData.size()];
		for (int i = 0; i < partData.size(); i++) {
			pItem[i] = partData.get(i).getCODE_NM();
		}
		new AlertDialog.Builder(context).setTitle("등록위치 선택")
				.setItems(pItem, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						tv_nm_nfcPosition.setText(pItem[which]);
						nfcPlc = partData.get(which).getCODE_CD();

						//빌딩정보와 호기정보가 선택된 후 카정보를 획득해 온다
						//그런 후 mngNo DongCarNO 등을 획득해서 셋팅해준다.
						new CarInformationAsync().execute();
					}
				}).show();
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_NM02_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "nfc 등록 중입니다.");
		} else {
			IR_NM02_R00.this.progress.dismiss();
		}
	}

	public void showAlert(String message)
	{
		AlertView.showAlert(message, context,new android.content.DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
	}
	public void showAlertYn(String message, android.content.DialogInterface.OnClickListener ocl){
		AlertView.confirmYN(context, "알림", message, ocl , new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}});
	}


	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;
	private boolean isFirstHide = false;
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
		//탭이있는 화면이라 액티비티 시작일 때 체크한다.
		if(!isFirstHide){
			naviPref.setHide(true);
			isFirstHide = true;
		}
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

}
