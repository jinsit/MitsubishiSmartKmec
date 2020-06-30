package com.jinsit.kmec.WO.WT.TS;

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
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchAdministratorEmail;
import com.jinsit.kmec.CM.CM_SearchAdministratorEmail_ITEM01;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.RegularExpression;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 고장수리
 */
public class WO_TS00_R13P extends Dialog implements OnClickListener, OnDismissListener{

	public WO_TS00_R13P(Context context, WO_TS00_M12P_ITEM01 item01, String bldgNo) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.item01 = item01;
		this.bldgNo = bldgNo;
	}
	
	private Context context;
	private String bldgNo;
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	
	private EditText et_fr_rmk;
	private EditText et_fr_custNm;
	private EditText et_fr_custEmail1;
	private EditText et_fr_custEmail2;
	private TextView btn_fr_ok;
	private TextView btn_fr_custEmail1;
	private TextView btn_fr_custEmail2;
	
	private ProgressDialog progress;
	
	private CommonSession commonSession;
	
	private WO_TS00_M12P_ITEM01 item01;
	private static final String DEFAULT_EMAIL = "kmecSvc@k-mec.co.kr";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wo_ts00_r13p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("고객 평가/승인");	

		commonSession = new CommonSession(context);
		resultOk = false;
		
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		et_fr_rmk = (EditText) findViewById(R.id.et_fr_rmk);
		et_fr_custNm = (EditText) findViewById(R.id.et_fr_custNm);
		et_fr_custEmail1  = (EditText) findViewById(R.id.et_fr_custEmail1);
		et_fr_custEmail2  = (EditText) findViewById(R.id.et_fr_custEmail2);
		btn_fr_custEmail1 = (TextView) findViewById(R.id.btn_fr_custEmail1);
		btn_fr_custEmail2 = (TextView) findViewById(R.id.btn_fr_custEmail2);
		btn_fr_ok = (TextView) findViewById(R.id.btn_fr_ok);
		
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		btn_fr_ok.setOnClickListener(this);
		btn_fr_custEmail1.setOnClickListener(this);
		btn_fr_custEmail2.setOnClickListener(this);
	
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			resultOk = false;
			dismiss();
			break;
		case R.id.btn_fr_ok:
			if(et_fr_custNm.getText().toString().equals("")){
				AlertView.showAlert( "고객성함을 입력해 주세요.", context);
				return;
			}
			if(!et_fr_custEmail1.getText().toString().equals("")){
				if(!RegularExpression.isValidEmail(et_fr_custEmail1.getText().toString())){
					AlertView.showAlert( "유효하지 않은 이메일 주소입니다. 이메일 주소를 확인해 주세요.", context);
					return;
				}
				}
			if(!et_fr_custEmail2.getText().toString().equals("")){
				if(!RegularExpression.isValidEmail(et_fr_custEmail2.getText().toString())){
					AlertView.showAlert( "유효하지 않은 이메일 주소입니다. 이메일 주소를 확인해 주세요.", context);
					return;
				}
				}
			if(et_fr_custEmail1.getText().toString().equals("")&&
					!et_fr_custEmail2.getText().toString().equals("")){
				AlertView.showAlert( "첫번째 메일부터 먼저 입력해주십시오.", context);
				return;
					}
			item01.setCustomer(et_fr_custNm.getText().toString());
			item01.setAsRmk(et_fr_rmk.getText().toString());
			progress(true);
			new FtpSignDataUpLoadAsync().execute("bagicWorkTime");
			break;
		case R.id.btn_fr_custEmail1:
			final CM_SearchAdministratorEmail cm01 = new CM_SearchAdministratorEmail(context,bldgNo );
			cm01.show();
			cm01.searchAdministratorEmail();
			cm01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					CM_SearchAdministratorEmail_ITEM01 item = cm01.getSelectedItem();
					if(item != null){
					et_fr_custEmail1.setText(item.getMailAddr());
					}
				}

			});
			break;
		case R.id.btn_fr_custEmail2:
			final CM_SearchAdministratorEmail cm02 = new CM_SearchAdministratorEmail(context,bldgNo );
			cm02.show();
			cm02.searchAdministratorEmail();
			cm02.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					CM_SearchAdministratorEmail_ITEM01 item = cm02.getSelectedItem();
					if(item != null){
					et_fr_custEmail2.setText(item.getMailAddr());
					}
				}

			});
			break;
		}
	}


	private class approveASProcReport extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/approveASProcReport.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", item01.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  item01.getJobNo()));
				arguments.add(new BasicNameValuePair("asCd1", item01.getAscCd1()));
				arguments.add(new BasicNameValuePair("asCd2", item01.getAscCd1()));
				arguments.add(new BasicNameValuePair("customer", item01.getCustomer()));
				arguments.add(new BasicNameValuePair("asRmk", item01.getAsRmk()));
				arguments.add(new BasicNameValuePair("custSign", item01.getCustSign()));
				arguments.add(new BasicNameValuePair("usrId", item01.getUsrId()));
				
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
						new sendEmail().execute("bagicWorkTime");
					}
					else{
						AlertView.showAlert("등록실패했습니다. 재등록해주시길 바랍니다.", context);
						progress(false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					progress(false);
				}
			}
		}
	}
	

	private class sendEmail extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/sendEmail.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", item01.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo",  item01.getJobNo()));
				arguments.add(new BasicNameValuePair("sendSt", "99"));
				arguments.add(new BasicNameValuePair("rmk", ""));
				if(et_fr_custEmail1.getText().toString().equals("")&&
						et_fr_custEmail2.getText().toString().equals("")){
					arguments.add(new BasicNameValuePair("emailId1", DEFAULT_EMAIL));
					arguments.add(new BasicNameValuePair("emailId2", et_fr_custEmail2.getText().toString()));
				}else{
					arguments.add(new BasicNameValuePair("emailId1", et_fr_custEmail1.getText().toString()));
					arguments.add(new BasicNameValuePair("emailId2", et_fr_custEmail2.getText().toString()));
				}
				arguments.add(new BasicNameValuePair("usrId", item01.getUsrId()));
				
				returnJson01 = http.getPost(param_url, arguments,
						true);
			} catch (Exception ex) {
				ex.printStackTrace();
				Log.e("sendEmail Error", ex.getMessage());
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

				progress(false);
				String returnValue = "N";
				try {
					returnValue = returnJson01.getString("dataString");
					if(returnValue.equals("1")){
						resultOk = true;
						dismiss();
					}
					else{
						AlertView.showAlert("메일 발송 등록실패했습니다. 재등록해주시길 바랍니다.", context);
						resultOk = false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("sendEmail Error", e.getMessage());
				}

			}
		}
	}
	

	
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	

	private class FtpSignDataUpLoadAsync extends
			AsyncTask<String, Integer, String> {
		
		private String retMsg = "";

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
			GetHttp http = null;
			String param_url = WebServerInfo.getUrl() + "comm/setFileToFTP.do";

			ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("folderToAccess",AbsoluteFilePath.FTP_FOLDER_WOTS_PATH));
			arguments.add(new BasicNameValuePair("fileName", item01.getCustSign()));
			arguments.add(new BasicNameValuePair("IMG1", item01.getCustSignData()));
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
			if (retMsg.equals("true")) {
				new approveASProcReport().execute("bagicWorkTime");
			} else {
				AlertView.showAlert("사인등록 실패", context);
				progress(false);
			}

		}
	}
	
	
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WO_TS00_R13P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WO_TS00_R13P.this.progress.dismiss();
		}
	}

	private boolean resultOk;
	public boolean getResult(){
		return resultOk;
	}
	
}
