package com.jinsit.kmec.SM.CR;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.NM.NFC.NfcParser;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class SM_CR01_R00 extends Activity implements Serializable{

	private static final long serialVersionUID = 167648375312327973L;

	//uiInstances
	private Context context;
	private TextView tv01_cr01_bldgNm;
	private TextView tv02_cr01_carNo;
	private TextView tv03_cr01_dateToShip;
	private TextView tv04_cr01_dateToEnd;
	private TextView tv05_cr01_serviceType;
	private TextView et01_cr01_dateToOccur;
	//private EditText et01_cr01_dateToOccur;
	//private Button btn01_cr01_callYMD;
	private Spinner sp01_cr01_claimDegree;
	private TextView et02_cr01_dateToFix;
	//private EditText et02_cr01_dateToFix;
	//private Button btn02_cr01_callYMD;
	private EditText et03_cr01_status;
	private EditText et04_cr01_expectedReason;
	private EditText et05_cr01_demands;
	private EditText et06_cr01_sthSpecial;
	private TextView btn03_cr01_attachPhotos;
	private TextView tv03_cr01_attachPhotos;
	private Button btn04_cr01_register;

	//SM_CR01_R01P
	private Bitmap bm;
	private Map<String, Integer> idMap;
	private ImageView iv01; private Bitmap bm1;
	private ImageView iv02; private Bitmap bm2;
	private ImageView iv03; private Bitmap bm3;
	private SM_CR01_R01P d01;

	//http
	private EasyJsonMap ejm;
	private EasyJsonMap ejm03;
	private EasyJsonMap ejm04;
	private EasyJsonList ejl;
	private JSONObject returnJson;
	private JSONObject returnJson02;

	//dto
	private Map<String,String> imgMap;

	//utils
	private int flag;
	private ProgressDialog progress;

	//nfc
	private String mClaimDegree;
	private String mBldgNo;
	private String mCarNo;
	//private Button tempNFC;
	static final String TAG = "TEST_LITE_TAG";
	private String ndefMsg;
	private String tagUid;
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;

	//photo
	private File f;
	private static final int ATTACH_PHOTO = 99;
	private CommonSession cs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cr01_r00);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("Claim 등록");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
		setInitConfig();
		setNFCConfig();
	}

	private void getInstances(){

		context 					= this;
		flag						= 0;
		cs							= new CommonSession(context);
		imgMap						= new HashMap<String, String>();
		tv01_cr01_bldgNm			= (TextView) findViewById(R.id.tv01_cr01_bldgNm_value);
		tv02_cr01_carNo				= (TextView) findViewById(R.id.tv02_cr01_carNo_value);
		tv03_cr01_dateToShip		= (TextView) findViewById(R.id.tv03_cr01_dateToShip_value);
		tv04_cr01_dateToEnd			= (TextView) findViewById(R.id.tv04_cr01_dateToEnd_value);
		tv05_cr01_serviceType		= (TextView) findViewById(R.id.tv05_cr01_svcType);
		et01_cr01_dateToOccur		= (TextView) findViewById(R.id.et01_cr01_dateToOccur_value);
		et01_cr01_dateToOccur.setText(cs.getWorkDt());
		//btn01_cr01_callYMD			= (Button) findViewById(R.id.btn01_cr01_callYMD);
		sp01_cr01_claimDegree		= (Spinner) findViewById(R.id.sp01_cr01_claimDegree);
		et02_cr01_dateToFix			= (TextView) findViewById(R.id.et02_cr01_dateToFix_value);
		et02_cr01_dateToFix.setText(cs.getWorkDt());
		//btn02_cr01_callYMD			= (Button) findViewById(R.id.btn02_cr01_callYMD);
		et03_cr01_status			= (EditText) findViewById(R.id.et03_cr01_status_value);
		this.et03_cr01_status.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int KeyCode, KeyEvent event) {
				if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					et04_cr01_expectedReason.requestFocus();
					return true;
				} else if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					return true;
				}
				return false;
			}
		});

		et04_cr01_expectedReason	= (EditText) findViewById(R.id.et04_cr01_expectedReason_value);
		this.et04_cr01_expectedReason.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int KeyCode, KeyEvent event) {
				if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					et05_cr01_demands.requestFocus();
					return true;
				} else if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					return true;
				}
				return false;
			}
		});
		et05_cr01_demands			= (EditText) findViewById(R.id.et05_cr01_demands_value);
		this.et05_cr01_demands.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int KeyCode, KeyEvent event) {
				if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					et06_cr01_sthSpecial.requestFocus();
					return true;
				} else if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					return true;
				}
				return false;
			}
		});
		et06_cr01_sthSpecial		= (EditText) findViewById(R.id.et06_cr01_sthSpecial_value);
		this.et06_cr01_sthSpecial.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int KeyCode, KeyEvent event) {
				if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					KeyboardUtil.hideKeyboard(et06_cr01_sthSpecial, (InputMethodManager)
							context.getSystemService(context.INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);

					return true;
				} else if (event.getKeyCode() == event.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					return true;
				}
				return false;
			}
		});
		btn03_cr01_attachPhotos		= (TextView) findViewById(R.id.btn03_cr01_attachPhotos);
		tv03_cr01_attachPhotos		= (TextView) findViewById(R.id.tv03_cr01_attachPhotos);
		btn04_cr01_register			= (Button) findViewById(R.id.btn04_cr01_register);
		mClaimDegree				= "";
		//tempNFC  = (Button)  findViewById(R.id.tempNFC);
		setEvents();
	}
	private void setEvents(){
		et01_cr01_dateToOccur.setOnClickListener(listener);
		et02_cr01_dateToFix.setOnClickListener(listener);
		btn03_cr01_attachPhotos.setOnClickListener(listener);
		btn04_cr01_register.setOnClickListener(listener);
		sp01_cr01_claimDegree.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int position, long arg3) {
				mClaimDegree = sp01_cr01_claimDegree.getItemAtPosition(position).toString();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});

		//tempNFC.setOnClickListener(listener);
	}
	private void setInitConfig(){
		et01_cr01_dateToOccur.setFocusable(false);
		et02_cr01_dateToFix.setFocusable(false);
		et03_cr01_status.setFocusable(false);
		et04_cr01_expectedReason.setFocusable(false);
		et05_cr01_demands.setFocusable(false);
		et06_cr01_sthSpecial.setFocusable(false);

		//finally declared keys
		imgMap.put("img1", "");
		imgMap.put("img2", "");
		imgMap.put("img3", "");
		new SelectData().execute("claimDegree");
		ActivityAdmin.getInstance().addActivity(this);
	}

	/*
	 * [setEvent and getIntances of popup, SM_CR01_R01P]
	 *
	 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
				case R.id.btn03_cr01_attachPhotos:

					//to get a intances from the pop
					d01 = new SM_CR01_R01P(context, imgMap);
					d01.show();
					iv01  = d01.getIv01_cr01_r01p();
					iv02  = d01.getIv02_cr01_r01p();
					iv03  = d01.getIv03_cr01_r01p();
					idMap = d01.getIdMap();

					//onClick event
					iv01.setOnClickListener(ocl2);
					iv02.setOnClickListener(ocl2);
					iv03.setOnClickListener(ocl2);

					//onLongClick event
					iv01.setOnLongClickListener(olcl01);
					iv02.setOnLongClickListener(olcl01);
					iv03.setOnLongClickListener(olcl01);

					//onDismiss event
					d01.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {

							int i = 0;
							boolean hasImg1 = imgMap.get("img1").toString().length() > 0;
							boolean hasImg2 = imgMap.get("img2").toString().length() > 0;
							boolean hasImg3 = imgMap.get("img3").toString().length() > 0;

							/*
							 * to set the number of photo to be attached
							 * if there is a img3, img1 and img2 MUST be attached. [major premise]
							 */
							if( hasImg3 ){
								tv03_cr01_attachPhotos.setText("사진첨부 " + "(3/3)");
							}else if( hasImg2 ){
								tv03_cr01_attachPhotos.setText("사진첨부 " + "(2/3)");
							}else if( hasImg1 ){
								tv03_cr01_attachPhotos.setText("사진첨부 " + "(1/3)");
							}else{
								tv03_cr01_attachPhotos.setText("사진첨부 " + "(0/0)");
							}
						}
					});

					//to attach photos again which have been attached before.
					if( !(imgMap.get("img3").isEmpty()) ){
						iv01.setImageBitmap(bm1);
						iv02.setImageBitmap(bm2);
						iv03.setImageBitmap(bm3);
					}else if( !(imgMap.get("img2").isEmpty()) ){
						iv01.setImageBitmap(bm1);
						iv02.setImageBitmap(bm2);
					}else if( !(imgMap.get("img1").isEmpty()) ){
						iv01.setImageBitmap(bm1);
					}

					break;
				case R.id.btn04_cr01_register:
					if( checkParamsInserted() ){
						SM_CR01_R00.this.progress =
								android.app.ProgressDialog.show(context, "알림","저장 중입니다.");
						new SelectData().execute("insertClaim");
					}
					break;
				case R.id.et01_cr01_dateToOccur_value:
					Intent intent01 = new Intent(SM_CR01_R00.this,com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
					startActivityForResult(intent01, 0);
					break;
				case R.id.et02_cr01_dateToFix_value:
					Intent intent02 = new Intent(SM_CR01_R00.this,com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
					startActivityForResult(intent02, 1);
					break;
				//case R.id.tempNFC: //testData
				//new SelectData().execute("selectInfo","04E401C2DE3684");
				//new SelectData().execute("selectInfo",tagUid);
				//new SelectData().execute("claimDegree");
				//break;
				default:
					break;
			}
		}
	};


	/*
	 * [for validation check]
	 * make a user to attach photo in order 1->2->3
	 */
	OnClickListener ocl2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			flag = v.getId();
			if(flag == idMap.get("iv03")){

				boolean hasImg1 = imgMap.get("img1").toString().length() > 0;
				boolean hasImg2 = imgMap.get("img2").toString().length() > 0;
				if(hasImg1 && hasImg2){
					androidActivity("ATTACH_PHOTO",flag);
					return;
				}
				d01.alert("1,2번 사진을 먼저 첨부하세요.", context);

			}else if(flag == idMap.get("iv02")){

				boolean hasImg1 = imgMap.get("img1").toString().length() > 0;
				if(hasImg1){
					androidActivity("ATTACH_PHOTO",flag);
					return;
				}
				d01.alert("1번 사진을 먼저 첨부하세요.", context);

			}else if(flag == idMap.get("iv01")){
				androidActivity("ATTACH_PHOTO",flag);
			}
		}
	};

	//an event to delete a photo attached on the pop
	OnLongClickListener olcl01 = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {

			int clickedId = v.getId();
			boolean hasImg1 = imgMap.get("img1").toString().length() > 0;
			boolean hasImg2 = imgMap.get("img2").toString().length() > 0;
			boolean hasImg3 = imgMap.get("img3").toString().length() > 0;
			if(clickedId == idMap.get("iv03")){

				if(hasImg3){
					if(hasImg2 && hasImg1){
						AlertView.deleteConfirm(context, "알림", "삭제하시겠습니까?", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								imgMap.put("img3","");
								iv03.setImageResource(R.drawable.btn_nopicture);
								bm3 = null;
							}
						});
					}
				}

			}else if(clickedId == idMap.get("iv02")){

				if(hasImg2 && !hasImg3){
					if(hasImg1){
						AlertView.deleteConfirm(context, "알림", "삭제하시겠습니까?", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								imgMap.put("img2","");
								iv02.setImageResource(R.drawable.btn_nopicture);
								bm2 = null;
							}
						});
					}
				}

			}else if(clickedId == idMap.get("iv01")){

				if(hasImg1 && !hasImg2 && !hasImg3){
					AlertView.deleteConfirm(context, "알림", "삭제하시겠습니까?", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							imgMap.put("img1","");
							iv01.setImageResource(R.drawable.btn_nopicture);
							bm1 = null;
						}
					});
				}
			}

			return false;
		}
	};



	//a method to select a photo
	private void androidActivity(String div, int id){

		if(div.equals("ATTACH_PHOTO")){

			Intent intent = new Intent(  Intent.ACTION_PICK//Intent.ACTION_GET_CONTENT
					,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");// 모든 이미지
			intent.putExtra("crop", "true");// Crop기능 활성화
			intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());// 임시파일 생성
			intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
			startActivityForResult(intent, ATTACH_PHOTO);
		}

	}

	//HTTP
	private class SelectData extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {

			//건물명조회 w/ 하위정보
			if(params[0].equals("selectInfo")){
				crud(params[1]);
				//불만정도조회
			}else if(params[0].equals("claimDegree")){
				selectClaimDegree();
				//claim등록
			}else if(params[0].equals("insertClaim")){
				insertClaimContent();
			}

			return params[0];
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			//건물명조회 w/ 하위정보
			if(result.equals("selectInfo")){
				setData(result);
				//불만정도조회
			}else if(result.equals("claimDegree")){
				setData(result);
				//claim 등록 후,
			}else if(result.equals("insertClaim")){
				setData(result);
			}
		}
	}
	private void crud(String tagId){

		GetHttp http = new GetHttp();
		String url = WebServerInfo.getUrl()+"sm/selectInfo.do";

		List<NameValuePair> arguments = new ArrayList<NameValuePair>();
		arguments.clear();
		System.out.println("tagId => " + tagId);
		arguments.add(new BasicNameValuePair("tagId", tagId));
		//arguments.add(new BasicNameValuePair("tagId", "04E401C2DE3684"));

		//Http
		returnJson = http.getPost(url, arguments, true);
		try {
			ejm = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void selectClaimDegree(){

		GetHttp http = new GetHttp();
		String url = WebServerInfo.getUrl()+"sm/selectClaimDegree.do";

		List<NameValuePair> arguments = new ArrayList<NameValuePair>();
		arguments.clear();

		//Http
		returnJson = http.getPost(url, arguments, true);

		try {
			ejl = new EasyJsonList( returnJson.getJSONArray("dataList") );
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void insertClaimContent(){

		String bldgNo = mBldgNo;
		String bldgNm = tv01_cr01_bldgNm.getText().toString();
		String carNo = tv02_cr01_carNo.getText().toString();
		String dateToShip = tv03_cr01_dateToShip.getText().toString();
		String dateToEnd = tv04_cr01_dateToEnd.getText().toString();
		String svcType = tv05_cr01_serviceType.getText().toString();
		String dateToOccur = et01_cr01_dateToOccur.getText().toString();
		String empId = cs.getEmpId();//"301241";
		String claimDegree = mClaimDegree;
		String dateToFix = et02_cr01_dateToFix.getText().toString();
		String status = et03_cr01_status.getText().toString();
		String expectedReason = et04_cr01_expectedReason.getText().toString();
		String demands = et05_cr01_demands.getText().toString();
		String sthSpecial = et06_cr01_sthSpecial.getText().toString();

		GetHttp http = new GetHttp();
		String url = WebServerInfo.getUrl()+"sm/insertClaimContent.do";

		List<NameValuePair> arguments = new ArrayList<NameValuePair>();
		arguments.clear();
		arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
		arguments.add(new BasicNameValuePair("bldgNm", bldgNm));
		arguments.add(new BasicNameValuePair("carNo", carNo));
		arguments.add(new BasicNameValuePair("dateToShip", dateToShip));
		arguments.add(new BasicNameValuePair("dateToEnd", dateToEnd));
		arguments.add(new BasicNameValuePair("dateToOccur", dateToOccur));
		arguments.add(new BasicNameValuePair("empId", empId));
		arguments.add(new BasicNameValuePair("claimDegree", claimDegree));
		arguments.add(new BasicNameValuePair("dateToFix", dateToFix));
		arguments.add(new BasicNameValuePair("svcType", svcType));
		arguments.add(new BasicNameValuePair("status", status));
		arguments.add(new BasicNameValuePair("expectedReason", expectedReason));
		arguments.add(new BasicNameValuePair("demands", demands));
		arguments.add(new BasicNameValuePair("sthSpecial", sthSpecial));

		if(!imgMap.get("img3").isEmpty()){
			arguments.add(new BasicNameValuePair("imgCnt", "3"));
			arguments.add(new BasicNameValuePair("IMG1", imgMap.get("img1")));
			arguments.add(new BasicNameValuePair("IMG1_CHK", "1"));
			arguments.add(new BasicNameValuePair("IMG2", imgMap.get("img2")));
			arguments.add(new BasicNameValuePair("IMG2_CHK", "1"));
			arguments.add(new BasicNameValuePair("IMG3", imgMap.get("img3")));
			arguments.add(new BasicNameValuePair("IMG3_CHK", "1"));
		}else if(!imgMap.get("img2").isEmpty()){
			arguments.add(new BasicNameValuePair("imgCnt", "2"));
			arguments.add(new BasicNameValuePair("IMG1", imgMap.get("img1")));
			arguments.add(new BasicNameValuePair("IMG1_CHK", "1"));
			arguments.add(new BasicNameValuePair("IMG2", imgMap.get("img2")));
			arguments.add(new BasicNameValuePair("IMG2_CHK", "1"));
		}else if(!imgMap.get("img1").isEmpty()){
			arguments.add(new BasicNameValuePair("imgCnt", "1"));
			arguments.add(new BasicNameValuePair("IMG1", imgMap.get("img1")));
			arguments.add(new BasicNameValuePair("IMG1_CHK", "1"));
		}

		returnJson02 = http.getPost(url, arguments, true);
		try {
			ejm04 = new EasyJsonMap( returnJson02.getJSONObject("msgMap") );
			ejm03 = new EasyJsonMap( returnJson02.getJSONObject("dataMap") );
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	android.content.DialogInterface.OnClickListener ocl = new android.content.DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			startActivity(getIntent());
			finish();
		}

	};


	private void setData(String result){

		if(result.equals("selectInfo")){

			try {

				mBldgNo = ejm.getValue("BLDG_NO");
				mCarNo  = ejm.getValue("CAR_NO");
				tv01_cr01_bldgNm.setText( ejm.getValue("BLDG_NM") );
				tv02_cr01_carNo.setText( mCarNo );
				tv03_cr01_dateToShip.setText( ejm.getValue("CUST_PASS_DT") );
				tv04_cr01_dateToEnd.setText( ejm.getValue("CONTR_DT") );
				tv05_cr01_serviceType.setText( ejm.getValue("M7_NM") );

				if(mBldgNo.isEmpty()){
					Log.e("[개발자Msg]","bldgNo is empty");
					alert("필요한 정보를 DB에서 불러오지 못했습니다. \n\n"
							+ "[예상원인]\n"
							+ "등록되지 않은 NFC Tag",context);
				}else if(mCarNo.isEmpty()){
					Log.e("[개발자Msg]","bldgNo is empty");
					alert("필요한 정보를 DB에서 불러오지 못했습니다. \n\n"
							+ "[예상원인]\n"
							+ "등록되지 않은 NFC Tag",context);
				}
				/*
				ejm.getValue("BLDG_NO"); 	 ejm.getValue("BLDG_NM"); 	 ejm.getValue("CAR_NO");
				ejm.getValue("DONG_CAR_NO"); ejm.getValue("PROJECT_NO"); ejm.getValue("CUST_PASS_DT");
				ejm.getValue("CONTR_DT"); 	 ejm.getValue("M7"); 		 ejm.getValue("M7_NM");
				*/
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else if(result.equals("claimDegree")){

			ArrayList<String> codeNm = new ArrayList<String>();
			ArrayList<String> codeCd = new ArrayList<String>();

			codeNm.add(0, "선택");
			codeCd.add(0, "");
			for(int i=0; i<ejl.getLength();i++){
				try {
					codeNm.add( ejl.getValue(i, "CODE_NM") );
					codeCd.add( ejl.getValue(i, "CODE_CD") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, codeNm);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp01_cr01_claimDegree.setAdapter(adapter);
		}else if(result.equals("insertClaim")){

			try {
				boolean isError =  ejm04.getValue("errCd").equals("1") ? true : false;
				if(!isError){
					if(!ejm03.getValue("NG_NO").toString().equals("ER")){

						AlertView.alert(  context
								, "알림", "성공적으로 저장되었습니다."
								, ocl);

					}else if(ejm03.getValue("NG_NO").toString().equals("ER")){
						alert("등록되지 않았습니다.", context);
					}

				}else if(isError){
					alert("<프로시저 Error>\n"+ejm04.getValue("errMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}SM_CR01_R00.this.progress.dismiss();

		}

	}//end of setData



	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					String strDate = bundle.getString("dateSelected");
					et01_cr01_dateToOccur.setText( strDate );
					et01_cr01_dateToOccur.requestFocus();

					break;
				}
			case 1:
				if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					String strDate = bundle.getString("dateSelected");
					et02_cr01_dateToFix.setText( strDate );
					et02_cr01_dateToFix.requestFocus();
					break;
				}
			case ATTACH_PHOTO:

				if (resultCode == RESULT_OK) {
					if (data != null) {
						String filePath = Environment.getExternalStorageDirectory()
								+ "/DCIM/Camera/temp.jpg";
						Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
						bm = Bitmap.createScaledBitmap(selectedImage, 640, 480, true);

						if(flag == idMap.get("iv01")){
							iv01.setImageBitmap(bm);
							imgMap.put("img1", DataConvertor.BitmapBase64(bm)); bm1=bm;
						}else if(flag == idMap.get("iv02")){
							iv02.setImageBitmap(bm);
							imgMap.put("img2", DataConvertor.BitmapBase64(bm)); bm2=bm;
						}else if(flag == idMap.get("iv03")){
							iv03.setImageBitmap(bm);
							imgMap.put("img3", DataConvertor.BitmapBase64(bm)); bm3=bm;
						}
						f.delete();
					}
				}
				break;
		}
	}


	// 임시 저장 파일의 경로를 반환
	private Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}
	// 외장메모리에 임시 이미지 파일을 생성하여 그 파일의 경로를 반환
	private File getTempFile() {
		if (isSDCARDMOUNTED()) {
			f = new File(Environment.getExternalStorageDirectory(), // 외장메모리 경로
					"/DCIM/Camera/temp.jpg");
//            try {
//                f.createNewFile();      // 외장메모리에 temp.jpg 파일 생성
//            } catch (IOException e) {
//            }
			return f;
		} else
			return null;
	}

	// SD카드가 마운트 되어 있는지 확인
	private boolean isSDCARDMOUNTED() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;

		return false;
	}



	private boolean checkParamsInserted(){

		boolean result = true;

		if(mBldgNo == null || mBldgNo.isEmpty()){
			alert("bldgNo를 얻지 못했습니다. ",context);
			result = false;
		}else if(tv02_cr01_carNo.getText().toString().isEmpty()){
			alert("carNo를 얻지 못했습니다. ",context);
			result = false;
		}else if(et01_cr01_dateToOccur.getText().toString().isEmpty()){
			AlertView.alert(context, "알림", "[발생일자] 필수입력값이 누락되었습니다", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//setFocus(et01_cr01_dateToOccur);
				}
			});
			result = false;
		}else if(mClaimDegree.equals("선택")){
			alert("[불만정도] 필수입력값이 누락되었습니다",context);
			sp01_cr01_claimDegree.requestFocus();
			result = false;
		}else if(et02_cr01_dateToFix.getText().toString().isEmpty()){
			AlertView.alert(context, "알림", "[조치요구일] 필수입력값이 누락되었습니다", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//setFocus(et02_cr01_dateToFix);
				}
			});
			result = false;
		}else if(et03_cr01_status.getText().toString().isEmpty()){
			AlertView.alert(context, "알림", "[현상] 필수입력값이 누락되었습니다", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					setFocus(et03_cr01_status);
				}
			});
			result = false;
		}
		return result;
	}


	/*** BEGINNING OF NFC ***/
	private void setNFCConfig(){
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		ndefMsg = "";
		tagUid = "";
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
	private void enableTagWriteMode() {
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
		IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
	}
	private void disableTagWriteMode() {
		mNfcAdapter.disableForegroundDispatch(this);
	}
	@Override
	protected void onNewIntent(final Intent intent) {
		// TODO Auto-generated method stub
		Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] duration = { 50, 100, 200, 300 };
		vib.vibrate(duration, -1);

		releaseFocusLock();

		// Tag writing mode
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			tagUid = NfcParser.getTagId(detectedTag);
			ndefMsg = NfcParser.getTagText(detectedTag);
			//Log.v("tagUid", tagUid);
			//Log.v("ndefMsg", ndefMsg);
			if (!tagUid.isEmpty()) {
				String str[] = ndefMsg.split("&&&");
				new SelectData().execute("selectInfo",tagUid);
			}
		}
	}
	public static final String CHARS = "0123456789ABCDEF";
	public static String toHexString(byte[] data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
					.append(CHARS.charAt(data[i] & 0x0F));
		}
		return sb.toString();
	}
	/*** END OF NFC ***/



	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void setFocus(EditText etObj){
		etObj.setFocusable(true);
		etObj.setFocusableInTouchMode(true);
		etObj.requestFocus();
	}
	private void releaseFocusLock(){
		et01_cr01_dateToOccur.setFocusable(true);
		et02_cr01_dateToFix.setFocusable(true);
		et03_cr01_status.setFocusable(true);
		et04_cr01_expectedReason.setFocusable(true);
		et05_cr01_demands.setFocusable(true);
		et06_cr01_sthSpecial.setFocusable(true);
		et01_cr01_dateToOccur.setFocusableInTouchMode(true);
		et02_cr01_dateToFix.setFocusableInTouchMode(true);
		et03_cr01_status.setFocusableInTouchMode(true);
		et04_cr01_expectedReason.setFocusableInTouchMode(true);
		et05_cr01_demands.setFocusableInTouchMode(true);
		et06_cr01_sthSpecial.setFocusableInTouchMode(true);
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
		navi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				naviPref.setHide(!naviPref.isHide());
				homeNavi.setToggleNavi();

			}
		});
	}

};