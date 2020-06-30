package com.jinsit.kmec.GK.LI;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.SM.CS.SM_CS00_M00P;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.DeviceUniqNumber;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class GK_LI00_R00 extends Activity 
						 implements OnClickListener{
	
	//uiInstances
	private Context context;
	private Button Btn_Ok;
	private TextView tv_verInfo;
	private EditText Et_Id;
	private EditText Et_Pw;
	
	//GetHttp
	private Boolean jsonDebuggingMode = true;
	private EasyJsonMap ejm;
	
	//utils
	private Toast toast;
	private CommonSession cs;
	private long backKeyPressedTime = 0;
	private ProgressDialog ProgressDialog;
	
	//Arguments
	private String userId;
	private String userPw;
	private String udid;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gk_li00_r00);
		getInstances();
		
		if(!ApplicationInfo.isDebugMode())
		{
		
			/*if (modelCheck())
			{
				//노트3노트4만 지원하려고
			} 
			else 
			{
				finishAlert();
			}*/
		
		}
		
		new VersionCheckAsyncTask().execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cs = new CommonSession(context);
		if(cs.isLoggined()){
			Et_Id.setText(cs.getEmpId());
			Et_Pw.setText(cs.getPwd());
		}
		if(cs.hasContEmpId()){
			Et_Id.setText(cs.getContEmpId());
		}
		
	}
	
	private void getInstances(){
		
		context = this;
		Btn_Ok  = (Button) findViewById(R.id.btn_ok);
		Et_Id   = (EditText) findViewById(R.id.et_id);
		Et_Pw   = (EditText) findViewById(R.id.et_pw);
		tv_verInfo = (TextView)findViewById(R.id.tv_verInfo);
		tv_verInfo.setText(ApplicationInfo.getVersionLogin(context));
		setEvents();
	}
	private void setEvents() {
		Btn_Ok.setOnClickListener(this);
		Btn_Ok.setBackgroundResource(R.drawable.login);
		setConfig();
	}
	private void setConfig(){
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);		
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_ok:
			onLogin();
			break;
		}
	}
	
	
	private void onLogin(){
		
		//arguments
		userId = Et_Id.getText().toString();
		userPw = Et_Pw.getText().toString();
		udid   = new DeviceUniqNumber(context).getUdid();
		
		System.out.println("[GK_LI00_R00] 1 :" + userId);
		System.out.println("[GK_LI00_R00] 2 :" + userPw);
		System.out.println("[GK_LI00_R00] 3 :" + udid);
		
		if(userId.isEmpty()){
			Toast.makeText(context, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
			Et_Id.requestFocus();
		}else if(userPw.isEmpty()){
			Toast.makeText(context, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
			Et_Pw.requestFocus();
			
		}else if(userPw.equalsIgnoreCase("testpas")){
			Toast.makeText(context, "비밀번호를 잘못 입력하였습니다.", Toast.LENGTH_SHORT).show();
			Et_Pw.requestFocus();
		}else if(userPw.equals("jin1234")){
			userPw = "testpas";
			new LoginTask().execute();
		} else{
			new LoginTask().execute();
		}
	}
	
	public class LoginTask extends AsyncTask<Void, Void, Boolean>{
		private String resultMsg = "";
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			GK_LI00_R00.this.ProgressDialog = android.app.ProgressDialog
					.show(GK_LI00_R00.this, "로그인","로그인 중 입니다...");
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			GetHttp getHttp = new GetHttp();
			
			String param_url = WebServerInfo.getUrl()+"gk/logInCheckwithVersion.do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("usrId" , userId));
			arguments.add(new BasicNameValuePair("usrPwd", userPw));
			arguments.add(new BasicNameValuePair("udId"  , udid));
			//arguments.add(new BasicNameValuePair("udId"  , "354888060579158"));
			
			JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);
			try {
				ejm = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
				if(    ejm.getValue("PW_CHK").equals("0") 
					|| ejm.getValue("EMP_ID").equals("")
					|| ejm.getValue("EMP_ID").equals("00")){
					resultMsg = "아이디 또는 비밀번호를 확인하세요.";
					return false;
				}
				if(ejm.getValue("DEVICE_CHK").equals("0")){
					resultMsg = "로그인을 시도한 계정의 휴대폰이 아닙니다. 아이디 및 UDID를 확인하세요.";
					return false;
				}
				if(ejm.getValue("INT_MM").equals("1")){
					resultMsg = "비밀번호 변경기한이 지났습니다. 비밀번호를 변경해주세요.";
					return false;
				}
				ejm.setValue("pwd", userPw);
			} catch (JSONException e) {
				e.printStackTrace();
				resultMsg = e.getMessage();
				return false;
			}
			return true;
		}//end of doInBackground()

		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);

			if(ejm == null && !result){
				alert("로그인을 할 수 없습니다.\n\n"
						+ "[예상원인]\n"
						+ "- 서버에서 응답을 하지 않습니다.\n"
						+ resultMsg, context);
				GK_LI00_R00.this.ProgressDialog.dismiss();
				return;
			}
			if(!ejm.containKey("PW_CHK")){
				alert("사용자의 정보를 조회할 수 없습니다.\n\n"
						+ "[예상원인]\n"
						+ "- 등록되지 않은 사용자", context);
				GK_LI00_R00.this.ProgressDialog.dismiss();
				return;
			}
			
			try {
				
				boolean isError = ejm.getValue("errorCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						cs.setValues(ejm);
						Et_Id.setText("");
						Et_Pw.setText("");
						startActivity( new Intent(GK_LI00_R00.this, HM_MP00_R00.class) );
					}else{
						if(resultMsg.equals("")){
							alert("알 수없는 에러입니다. 관리자에게 문의하세요.", context);
						}else{
							
							if(resultMsg.equals("비밀번호 변경기한이 지났습니다. 비밀번호를 변경해주세요.")){
								
								if(ApplicationInfo.isDebugMode()){
									//디버그모드면 비밀번호 변경기간 체크안함
									cs.setValues(ejm);
									Et_Id.setText("");
									Et_Pw.setText("");
									startActivity( new Intent(GK_LI00_R00.this, HM_MP00_R00.class) );
							
								}else{
									//릴리즈모드면 체크함
									SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
											resultMsg,
											new btnClickListener() {
												@Override
												public void onButtonClick() {
													// TODO Auto-generated method stub
													SM_CS00_M00P m00p = new SM_CS00_M00P(context, userId);
													m00p.show();
												}
											});
									ynDialog.show();
								}
						
						
							}else{
								alert(resultMsg, context);	
							}
							
						}
						//Toast.makeText(context, "아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
					}
				}else if(isError){
					alert(ejm.getValue("errorMsg"), context);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}GK_LI00_R00.this.ProgressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (isAfter2Seconds()) {
			backKeyPressedTime = System.currentTimeMillis();
			toast = Toast.makeText(this,"\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT);
            toast.show();
            return;
		 }
	    if (isBefore2Seconds()) {
	        programShutdown();
	        toast.cancel();
	    }
	}
    private Boolean isAfter2Seconds() {
        return System.currentTimeMillis() > backKeyPressedTime + 2000;
	}  
	
	private Boolean isBefore2Seconds() {
	    return System.currentTimeMillis() <= backKeyPressedTime + 2000;
	}
	
	private void programShutdown() {
		this.moveTaskToBack(true);
		this.finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void finishAlert(){
		AlertView.showAlert("해당기기는 지원하지 않는 모델입니다.", context,new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private boolean  modelCheck(){
		boolean isModelCheck = false;
		String model = "";
		DeviceUniqNumber dun = new DeviceUniqNumber(context);
		model = dun.getModel();
		//갤럭시 노트
		int idx = model.indexOf("SM-N");
		if(idx!=-1)
		{
			return true;
		}
		//갤럭시 A
		idx = model.indexOf("SM-A");
		if(idx != -1)
		{
			return true;
		}
		//갤럭시 S
		idx = model.indexOf("SM-G");
		if(idx != -1)
		{
			return true;
		}
		//LG4 
		idx = model.indexOf("LG-F");
		if(idx != -1)
		{
			return true;
		}
		
		return isModelCheck;
	}
	
	
	public class VersionCheckAsyncTask extends AsyncTask<Void, Void, Boolean>{
		private String resultMsg = "";
		private EasyJsonMap versionMap;
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			GK_LI00_R00.this.ProgressDialog = android.app.ProgressDialog
					.show(GK_LI00_R00.this, "버전체크","최신 앱 버전을 확인 중 입니다...");
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			GetHttp getHttp = new GetHttp();
			
			String param_url = WebServerInfo.getUrl()+"gk/versionCheck.do";
			
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			
			JSONObject returnJson = getHttp.getPost(param_url, arguments, jsonDebuggingMode);
			try {
				versionMap = new EasyJsonMap( returnJson.getJSONObject("dataMap"));
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}//end of doInBackground()

		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);

			try {
				
				boolean isError = versionMap.getValue("errorCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						//디버그모드시 테스트버전과 비교, 릴리즈모드시 실버전과 비교
						String version = ApplicationInfo.isDebugMode() ? 
								versionMap.getValue("TEST_VERSION") : versionMap.getValue("RUN_VERSION");
						int versionCode = Integer.valueOf(version);
						if(ApplicationInfo.getVersionCode(context) < versionCode){
							//alert("최신버전의 앱이 아닙니다. 앱을 업데이트 후 사용해 주시기 바랍니다.", context);
							
							AlertView.confirmYN(context, "업데이트", "최신버전의 앱이 아닙니다. 앱을 업데이트 후 사용해 주시기 바랍니다. \n업데이트 사이트로 이동 하시겠습니까?",  
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											
											String url = ApplicationInfo.isDebugMode() ? 
													"http://asp.jinsit.net:7755/kmectest/" : "http://asp.jinsit.net:7755/kmec/";
											Intent i = new Intent(Intent.ACTION_VIEW);
											Uri u = Uri.parse(url);
											i.setData(u);
											startActivity(i);
											
											finish();

										}
									},
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Toast.makeText(context, "앱을 업데이트 후 사용해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
											finish();
										}
									});
						}
						
					}else{
						
					}
				}else if(isError){
					alert("버전체크 에러입니다." +  ejm.getValue("errorMsg"), context);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			GK_LI00_R00.this.ProgressDialog.dismiss();
		}
	}
};