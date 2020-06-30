package com.jinsit.kmec.SM.CS;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 로그인시 패스워드 30일동안 변경 안됐을 경우 변경하라고 뜨는 팝업
 * @author 원성민
 *
 */
public class SM_CS00_M00P extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;


	private ProgressDialog ProgressDialog;
	private EditText et01_presentPwd;
	private EditText et02_newPwd;
	private EditText et03_newPwdAgain;
	private TextView btn01_confirmChangePwd;


	private String empId;

	public SM_CS00_M00P(Context context, String empId) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.empId = empId;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sm_cs00_r00_pop01);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("비밀번호 변경");

	}

	protected void getInstances() {
		// /title 위젯

		btn01_confirmChangePwd = (TextView)findViewById(R.id.btn01_confirmChangePwd);
		btn_popClose           = (TextView)findViewById(R.id.btn_popClose);
		tv01_popTitle          = (TextView)findViewById(R.id.tv01_popTitle);
		et01_presentPwd 	   = (EditText)findViewById(R.id.et01_presentPwd);
		et02_newPwd			   = (EditText)findViewById(R.id.et02_newPwd);
		et03_newPwdAgain 	   = (EditText)findViewById(R.id.et03_newPwdAgain);

		et01_presentPwd.setPrivateImeOptions("defaultInputmode=english;");
		et02_newPwd.setPrivateImeOptions("defaultInputmode=english;");
		et03_newPwdAgain.setPrivateImeOptions("defaultInputmode=english;");
		setEvents();
	}

	protected void setEvents() {
		// /title 위젯
		this.btn_popClose.setOnClickListener(this);
		this.btn01_confirmChangePwd.setOnClickListener(this);

		// //////////////////
	}
	private String presentPwd, newPwd, newPwdAgain;
	private void btn01ConfirmChangePwd(){

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
			alert("현재 비밀번호와 변경할 비밀번호가 같습니다.", context);
			et02_newPwd.requestFocus();
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

	private void resetPwd(){
		et01_presentPwd.setText("");
		et02_newPwd.setText("");
		et03_newPwdAgain.setText("");
		et01_presentPwd.requestFocus();
	}
	public class ChangePassWordAsync extends AsyncTask<Void, Void, Void> {
		String isUpdated="";
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress(true);

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"sm/updatePassword.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("usrId", empId));
				arguments.add(new BasicNameValuePair("usrPwd", presentPwd));
				arguments.add(new BasicNameValuePair("newPwd", newPwdAgain));

				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

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
			progress(false);
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
						dismiss();
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
			}

		}
	}



	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// /title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			// //////////////////
			case R.id.btn01_confirmChangePwd:
				btn01ConfirmChangePwd();
				break;

			default:
				break;
		}
	}


	private boolean progressWorking = false;
	private void progress(Boolean isActivated) {
		progressWorking = isActivated;
		if (isActivated) {
			SM_CS00_M00P.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "알림", "비밀번호 변경 중입니다.");

		} else {
			SM_CS00_M00P.this.ProgressDialog.dismiss();
		}
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
}