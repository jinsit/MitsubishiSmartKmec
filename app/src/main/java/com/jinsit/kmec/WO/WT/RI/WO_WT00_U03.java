package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_Sign;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class WO_WT00_U03 extends Activity implements
		android.view.View.OnClickListener {
	Context context;
	private static final String FORMAT = ".jpg";
	private static final String ABS = "_S";
	private static final String FTP_FOLDER_PATH = "PCSD200S";

	private static final int REQUEST_CODE = 0;
	ImageView iv_wo_sign;
	EditText et_wo_consumerNm;
	Button btn_wo_confirm;
	String fileName;
	String refContrNo;
	String bldgNo;
	String signString="";
	boolean isSignData = false;
	CommonSession commonSession;
	CommentPreference pref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_u03);
		activityInit();
	}
	String comment;
	private void activityInit() {
		context = this;
		// TODO Auto-generated method stub
		commonSession = new CommonSession(context);
		pref = new CommentPreference(context);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("점검완료 확인서");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		
		RoutineCheckListData mData = (RoutineCheckListData) this.getIntent()
				.getExtras().getSerializable("obj");
		comment = getIntent().getExtras().getString("comment");
		refContrNo = mData.getREF_CONTR_NO();
		bldgNo = mData.getBLDG_NO();
		iv_wo_sign = (ImageView) findViewById(R.id.iv_wo_sign);
		iv_wo_sign.setOnClickListener(this);
		et_wo_consumerNm = (EditText) findViewById(R.id.et_wo_consumerNm);
		btn_wo_confirm = (Button) findViewById(R.id.btn_wo_confirm);
		btn_wo_confirm.setOnClickListener(this);
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}
	
	private String getFileName() {
		String str = "";
		DateUtil du = new DateUtil();
		String ymd = du.getCurrentShortDate();
		str = refContrNo + "_" + ymd + ABS + FORMAT;

		return str;

	}

	private void goSign() {
		// TODO Auto-generated method stub
		final CM_Sign cm01 = new CM_Sign(context);
		cm01.show();
		cm01.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				signString = cm01.getSignString();
				if(signString == ""){
					SimpleDialog sm01 = new SimpleDialog(context, "알림","서명저장을 실패하였습니다.");
					sm01.show();
				
				}
				else
				{
					Bitmap signBitmap = DataConvertor.Base64Bitmap(signString);
					iv_wo_sign.setImageBitmap(signBitmap);
					isSignData = true;
				}
				
			}

		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_wo_sign:
			goSign();
			//goSignActivity();
			break;
		case R.id.btn_wo_confirm:

			new AlertDialog.Builder(context)

					.setTitle("작업을 완료하시겠습니까?")
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									return;
								}
							})
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// new StartCheckListAsync().execute();
									if(et_wo_consumerNm.getText().toString().equals("")){
										SimpleDialog sm01 = new SimpleDialog(context, "알림","관리자 성함을 입력해주세요.");
										sm01.show();
									}else if(!isSignData){
										SimpleDialog sm01 = new SimpleDialog(context, "알림","싸인을 입력해주세요.");
										sm01.show();
									}else{
										new FtpSignDataUpLoadAsync().execute();
									}
									
									
								}
							}).show();
			break;
		default:
			break;
		}
	}


	private void goSignActivity() {
		Intent intent = new Intent(WO_WT00_U03.this,
				com.jinsit.kmec.comm.jinLib.SignActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
		// this.Tx_PaymentcardActivity_ResultText.setText("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				String sign = data.getExtras().getString("sign");
				Bitmap signBitmap = StringToBitMap(sign);
				iv_wo_sign.setImageBitmap(signBitmap);
			} else if (resultCode == RESULT_CANCELED) {

			}
			break;

		}
	}

	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString.getBytes(),
					Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	private class CheckCompleteAsync extends AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@Override
		protected void onPostExecute(Void result) {
			if (retMsg.equals("1")) {
				Toast.makeText(context, "정상등록완료.", 2000).show();
				setResult(RESULT_OK);
				finish();
			
			} else {
				Toast.makeText(context, "정상등록이 안됐습니다.", 2000).show();
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
						+ "ip/updateConsumerConfirm.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
				arguments.add(new BasicNameValuePair("bldgNo", bldgNo));//빌딩넘버추가
				arguments.add(new BasicNameValuePair("custSign", getFileName()));
				arguments.add(new BasicNameValuePair("clientId", ""));
				arguments.add(new BasicNameValuePair("clientNm",et_wo_consumerNm.getText().toString()));
				arguments.add(new BasicNameValuePair("mRmk", comment)); //작업자 의견
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

	private class SignDataUpLoadAsync extends AsyncTask<Void, String, Void> {
		private String retMsg = "";

		@Override
		protected void onPostExecute(Void result) {
			if (retMsg.equals("1")) {

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
						+ "ip/upLoadSignData.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				arguments.add(new BasicNameValuePair("empId", "301283"));
				arguments.add(new BasicNameValuePair("workDt", "2014-12-08"));
				arguments.add(new BasicNameValuePair("jobNo", "1"));
				arguments.add(new BasicNameValuePair("picTp", "S"));
				arguments.add(new BasicNameValuePair("picSeq", ""));
				arguments.add(new BasicNameValuePair("fileNm", getFileName()));
				arguments.add(new BasicNameValuePair("usrId", "301283"));

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

	private class FtpSignDataUpLoadAsync extends
			AsyncTask<String, Integer, String> {
		private String retMsg = "";

		@Override
		protected String doInBackground(String... params) {

			GetHttp http = null;
			String param_url = WebServerInfo.getUrl() + "comm/setFileToFTP.do";

			ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("folderToAccess",
					FTP_FOLDER_PATH));
			arguments.add(new BasicNameValuePair("fileName", getFileName()));
			arguments.add(new BasicNameValuePair("IMG1", DataConvertor
					.BitmapBase64(getImageViewBitmap(iv_wo_sign))));
			http = new GetHttp();
			JSONObject returnJson = http.getPost(param_url, arguments, true);

			try {

				EasyJsonMap ejm = new EasyJsonMap(
						returnJson.getJSONObject("dataMap"));
				System.out.println(returnJson);

				retMsg = ejm.getValue("message01");
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (retMsg.equals("true")) {
				new CheckCompleteAsync().execute();
			} else {
				Toast.makeText(context, "사진등록 실패", 2000).show();
			}

		}

	}

	private Bitmap getImageViewBitmap(ImageView iv) {
		BitmapDrawable d = (BitmapDrawable) iv.getDrawable();
		Bitmap b = d.getBitmap();
		return b;
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
		public void onResume(){
			super.onResume();
			naviPref = new HomeNaviPreference(context);
			navigationInit();
		}
		//----------------------내비게이션 영역--------------------------------------//
		
}
