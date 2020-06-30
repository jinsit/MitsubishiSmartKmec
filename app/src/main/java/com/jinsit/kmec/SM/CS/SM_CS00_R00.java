package com.jinsit.kmec.SM.CS;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.IP.IS.Wv;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.ArgumentChecker;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;


/**
 * @Package_Name	: com.jinsit.kmec.SM.CS
 * @Class_Name		: SM_CS00_R00
 * @Date			: 2014. 11. 18
 * @Developer		: 이상원
 * @Description_Kor	: 환경설정
 * @History			:
 */
public class SM_CS00_R00 extends Activity{

	//UI
	private Context context;
	private TextView tv01_changePwd;
	private TextView tv02_verOfSoftware;
	private TextView tv05_cs_dataDown;
	private TextView tv06_cs_empSave;

	//POP
	private Builder ad;
	private DialogInterface mPopupDlg = null;
	private EditText et01_presentPwd;
	private EditText et02_newPwd;
	private EditText et03_newPwdAgain;
	private TextView btn01_confirmChangePwd;
	private TextView btn_popClose;
	private TextView tv01_popTitle;

	//http
	private EasyJsonMap ejm01;

	//utils
	private ArgumentChecker argChecker;
	private List<String> argList;
	private Boolean jsonDebuggingMode = true;
	private CommonSession commonSession;
	private String presentPwd;
	private String newPwd;
	private String newPwdAgain;
	private ProgressDialog progress;


	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cs00_r00);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("환경설정");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		getInstances();
	}

	private void getInstances() {
		context = this;
		commonSession = new CommonSession(context);
		tv01_changePwd = (TextView) findViewById(R.id.tv01_changePwd);
		tv02_verOfSoftware = (TextView) findViewById(R.id.tv02_verOfSoftware);
		tv05_cs_dataDown = (TextView)findViewById(R.id.tv05_cs_dataDown);
		tv06_cs_empSave = (TextView)findViewById(R.id.tv06_cs_empSave);
		argChecker = new ArgumentChecker();
		argList =  new ArrayList();
		setEvents();
	}
	private void setEvents(){
		tv01_changePwd.setOnClickListener(listener);
		tv02_verOfSoftware.setOnClickListener(listener);
		tv05_cs_dataDown.setOnClickListener(listener);
		tv06_cs_empSave.setOnClickListener(listener);
		setConfig();
	}
	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

	//[Event]
	OnClickListener listener = new OnClickListener() {

		GetHttp getHttp = new GetHttp();
		Intent intent;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.tv01_changePwd:
					tv01ChangePwd();
					break;
				case R.id.tv02_verOfSoftware:
					tv02VerOfSoftware();
					break;
				case R.id.tv05_cs_dataDown:
					tv05_cs_dataDown(intent);
					break;
				case R.id.btn01_confirmChangePwd:
					btn01ConfirmChangePwd(getHttp);
					break;
				case R.id.btn_popClose:
					btn02ChangePwdClose();
					break;
				case R.id.tv06_cs_empSave:
					employeeSave();
					break;
			}
		}
	};

	private void employeeSave(){

		SimpleDialog ynDialog = new SimpleDialog(context, "알림","작업자 안전관리를 위해 작업자의 상태를 갱신하시겠습니까?",
				new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new EmpSafeStatusUpdateAsync().execute();
					}
				});
		ynDialog.show();
		/*SimpleYesNoDialog ynd = new SimpleYesNoDialog(context, "작업자 안전관리를 위해 작업자의 상태를 갱신하시겠습니까?",
				new com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub
						new EmpSafeStatusUpdateAsync().execute();
					}
				});
		ynd.show();*/
	}

	private void tv01ChangePwd(){
		openDialog();
	}
	private void tv02VerOfSoftware(){
		AlertView.showAlert("Software Version", ApplicationInfo.getVersion(context), context);
	}

	private void tv04_cs_test(Intent intent){
		intent = new Intent(SM_CS00_R00.this,Wv.class);
		startActivity(intent);
	}
	private void tv05_cs_dataDown(Intent intent) {
		intent = new Intent(this,MasterDataDownload.class);
		startActivity(intent);
	}

	private void btn01ConfirmChangePwd(GetHttp getHttp){

		//arguments
		presentPwd  = et01_presentPwd.getText().toString();
		newPwd 	    = et02_newPwd.getText().toString();
		newPwdAgain = et03_newPwdAgain.getText().toString();

		//for debugging
		System.out.println("presentPwd = > "+presentPwd);
		System.out.println("newPwd = > "+newPwd);
		System.out.println("newPwdAgain = > "+newPwdAgain);

		//to check arguments validation
		if(presentPwd.isEmpty()){
			alert("현재 비밀번호를 입력하세요.", context);
			et01_presentPwd.requestFocus();
			return;
		}else if(newPwd.isEmpty()){
			alert("변경 비밀번호를 입력하세요.", context);
			et02_newPwd.requestFocus();
			return;
		}else if(newPwdAgain.isEmpty()){
			alert("변경 비밀번호 재확인을 입력하세요.", context);
			et03_newPwdAgain.requestFocus();
			return;
		}else if(presentPwd.equals(newPwd)){
			AlertView.alert(context, "알림", "현재 비밀번호와 변경할 비밀번호가 같습니다.", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					et02_newPwd.requestFocus();
					resetPwd();
				}
			});
			return;
		}

		if(newPwd.equals(newPwdAgain)){
			if(RegExp(newPwd)){
				new ChangePassWordAsync().execute();
			}

		}else if(!newPwd.equals(newPwdAgain)){
			alert("변경 Password 와"+"\n"+"변경 Password 재확인의 값이"+"\n"+"서로 다릅니다.",context);
			et03_newPwdAgain.requestFocus();
		}
	}
	private void btn02ChangePwdClose(){
		mPopupDlg.dismiss();
	}

	private boolean RegExp(String value){

		if(value.length()<8){
			alert("8자리 이상의 비밀번호만 가능합니다.", context);
			return false;
		}

		int aCode;
		boolean n = false;
		boolean s = false;
		boolean a = false;
		boolean A = false;
		int inputValueLength = value.length();
		for(int i=0;i<inputValueLength;i++){
			aCode = (int) value.charAt(i);
			if(aCode < 33 && aCode > 126){
				return false;
			}
			if( (aCode > 47 && aCode < 58) ){
				n = true;
			}
			if( (aCode > 96 && aCode < 123) ){
				a = true;
			}
			if( (aCode > 64 && aCode < 91) ){
				A = true;
			}
			if( (aCode > 32 && aCode < 48) || (aCode > 57 && aCode < 65)
					|| (aCode > 90 && aCode < 97) || (aCode > 122 && aCode < 127) ){
				s = true;
			}
		};
		if(n&&a&&A&&s){
			return true;
		}
		alert("비밀번호는\n"
						+ "숫자, 영소문자, 영대문자, 특수기호로 조합된\n"
						+ "8자리 이상만 가능합니다.\n"
				, context);
		return false;
	}

	private void openDialog(){

		String service = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)getBaseContext().getSystemService (service);
		LinearLayout ll =(LinearLayout)li.inflate (R.layout.sm_cs00_r00_pop01, null);

		btn01_confirmChangePwd = (TextView)ll.findViewById(R.id.btn01_confirmChangePwd);
		btn_popClose           = (TextView) ll.findViewById(R.id.btn_popClose);
		tv01_popTitle          = (TextView) ll.findViewById(R.id.tv01_popTitle);
		et01_presentPwd 	   = (EditText) ll.findViewById(R.id.et01_presentPwd);
		et02_newPwd			   = (EditText) ll.findViewById(R.id.et02_newPwd);
		et03_newPwdAgain 	   = (EditText) ll.findViewById(R.id.et03_newPwdAgain);

		btn01_confirmChangePwd.setOnClickListener(listener);
		btn_popClose.setOnClickListener(listener);
		tv01_popTitle.setText("비밀번호 변경");
		ad = new AlertDialog.Builder(this);
		ad.create();

		mPopupDlg  = ad.setView(ll).show();
		et01_presentPwd.setPrivateImeOptions("defaultInputmode=english;");
		et02_newPwd.setPrivateImeOptions("defaultInputmode=english;");
		et03_newPwdAgain.setPrivateImeOptions("defaultInputmode=english;");
	}


	public class ChangePassWordAsync extends AsyncTask<Void, Void, Void> {
		String isUpdated="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SM_CS00_R00.this.progress = android.app.ProgressDialog
					.show(SM_CS00_R00.this, "비밀번호변경","비밀번호 변경 중입니다. ");
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"sm/updatePassword.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("usrPwd", presentPwd));
				arguments.add(new BasicNameValuePair("newPwd", newPwdAgain));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);

				try {

					EasyJsonMap em = new EasyJsonMap( returnJson.getJSONObject("dataList") );
					isUpdated = em.getValue("isUpdated");
					System.out.println("isUpdated  " + isUpdated);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			//when 비번동일
			if(isUpdated.equals("0")){
				resetPwd();
				et01_presentPwd.requestFocus();
				AlertView.alert( context, "알림","현재 비밀번호와 같습니다.", new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						resetPwd();
						et01_presentPwd.requestFocus();
					}
				});

				//when 비번변경성공
			}else if(isUpdated.equals("TRUE")){
				AlertView.alert( context, "알림","비밀번호가 변경되었습니다.", new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mPopupDlg.dismiss();
					}
				});
				//when 비번변경실패
			}else if(isUpdated.equalsIgnoreCase("FALSE")){
				AlertView.alert( context, "알림","현재 비밀번호가 다릅니다.", new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						et01_presentPwd.requestFocus();
					}
				});

			}else{
				alert("비밀번호 변경 에러",context);
			}SM_CS00_R00.this.progress.dismiss();
		}
	}



	public class EmployeeSaveAsync extends AsyncTask<Void, Void, Void> {
		String retStr="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SM_CS00_R00.this.progress = android.app.ProgressDialog
					.show(SM_CS00_R00.this, "알림","작업자상태 갱신중...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"hm/updateEmpStatus.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));


				JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);
				returnJson.getString("dataString");
				try {
					retStr =returnJson.getString("dataString");
					System.out.println("isUpdated  " + retStr);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			SM_CS00_R00.this.progress.dismiss();

			if(retStr.equals("")){
				//업데이트 안됨
				alert("갱신을 실패하였습니다.", context);
			}else if(retStr.equals("1")){
				//업데이트 됨
				alert("갱신 되었습니다.", context);
			}else if(retStr.equals("0")){
				//업데이트 실패
				alert("갱신을 실패하였습니다.", context);
			}

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
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));

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


	/*
	private class Database extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SM_CS00_R00.this.progress = android.app.ProgressDialog
					.show(SM_CS00_R00.this, "유효성검사","비밀번호 유효성 검사 중입니다 ... ");
		}
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
		if(div.equals("compareNewToOldPwd")){

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"gk/"+div+".do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId" , commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("usrPwd", presentPwd));
				arguments.add(new BasicNameValuePair("newPwd", newPwdAgain));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);

				try {
					ejm01 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	private void setData(String div){
		if(div.equals("compareNewToOldPwd")){
			try {
				boolean isError = ejm01.getValue("errorCd").equals("0") ? false : true;
				if(!isError){

				}else if(isError){
					alert(ejm01.getValue("errorMsg"), context);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}SM_CS00_R00.this.progress.dismiss();
		}
	}
	*/
	private void resetPwd(){
		et01_presentPwd.setText("");
		et02_newPwd.setText("");
		et03_newPwdAgain.setText("");
		et01_presentPwd.requestFocus();
	}


	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
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



};
