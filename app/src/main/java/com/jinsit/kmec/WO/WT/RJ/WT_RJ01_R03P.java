package com.jinsit.kmec.WO.WT.RJ;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchAdministratorEmail;
import com.jinsit.kmec.CM.CM_SearchAdministratorEmail_ITEM01;
import com.jinsit.kmec.CM.CM_Sign;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.RegularExpression;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class WT_RJ01_R03P extends Dialog implements android.view.View.OnClickListener , OnDismissListener{

	public WT_RJ01_R03P(Context context, String bldgNo, String refContrNo, String workDt, String jobNo, String clientId, String custSign,Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bldgNo = bldgNo;
		this.refContrNo = refContrNo;
		this.workDt = workDt;
		this.jobNo = jobNo;
		this.clientId = clientId;
		this.custSign = custSign;
		this.activity = activity;
	}

	private Context context;
	private Activity activity;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	private ImageView img_fr_sign;
	
	private EditText et_fr_custNm;
	private EditText et_fr_custEmail1;
	private EditText et_fr_custEmail2;
	private TextView btn_fr_ok;
	private TextView btn_fr_custEmail1;
	private TextView btn_fr_custEmail2;
	private LinearLayout lin_fr_input;
	
	private String bldgNo;
	private String refContrNo;
	private String workDt;
	private String jobNo;
	private String clientId;
	private String custSign;

	private ProgressDialog progress;
	private CommonSession commonSession;
	
	private static final String DEFAULT_EMAIL = "kmecSvc@k-mec.co.kr";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wt_rj01_r03p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("고객 평가/승인");	

		commonSession = new CommonSession(context);
		if(custSign != null && !custSign.equals("")){
			lin_fr_input.setVisibility(View.GONE);
			btn_fr_ok.setVisibility(View.GONE);
			progress(true);
			new selectItemImage().execute("bagicWorkTime");
		}
		else{
			
		}
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		img_fr_sign = (ImageView)findViewById(R.id.img_fr_sign);
		
		et_fr_custNm = (EditText) findViewById(R.id.et_fr_custNm);
		et_fr_custEmail1  = (EditText) findViewById(R.id.et_fr_custEmail1);
		et_fr_custEmail2  = (EditText) findViewById(R.id.et_fr_custEmail2);
		btn_fr_custEmail1 = (TextView) findViewById(R.id.btn_fr_custEmail1);
		btn_fr_custEmail2 = (TextView) findViewById(R.id.btn_fr_custEmail2);
		btn_fr_ok = (TextView) findViewById(R.id.btn_fr_ok);
		lin_fr_input = (LinearLayout)findViewById(R.id.lin_fr_input);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		img_fr_sign.setOnClickListener(this);
		btn_fr_ok.setOnClickListener(this);
		btn_fr_custEmail1.setOnClickListener(this);
		btn_fr_custEmail2.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			dismiss();
			break;
		case R.id.img_fr_sign:
			if(custSign != null && !custSign.equals("")){
				return;
			}
			final CM_Sign cm01 = new CM_Sign(context);
			cm01.show();
			cm01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					signString = cm01.getSignString();
					if(signString == ""){
						AlertView.showAlert("서명 저장을 실패했습니다.", context);
					}
					else
					{
						sign = DataConvertor.Base64Bitmap(signString);
						img_fr_sign.setImageBitmap(sign);
					}
					
				}

			});
			break;
		case R.id.btn_fr_custEmail1:
			final CM_SearchAdministratorEmail cm02 = new CM_SearchAdministratorEmail(context, bldgNo );
			cm02.show();
			cm02.searchAdministratorEmail();
			cm02.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					CM_SearchAdministratorEmail_ITEM01 item = cm02.getSelectedItem();
					if(item != null){
					et_fr_custEmail1.setText(item.getMailAddr());
					}
				}

			});
			break;
		case R.id.btn_fr_custEmail2:
			final CM_SearchAdministratorEmail cm03 = new CM_SearchAdministratorEmail(context, bldgNo );
			cm03.show();
			cm03.searchAdministratorEmail();
			cm03.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					CM_SearchAdministratorEmail_ITEM01 item = cm03.getSelectedItem();
					if(item != null){
					et_fr_custEmail2.setText(item.getMailAddr());
					}
				}

			});
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
			
			progress(true);
			new FtpSignDataUpLoadAsync().execute("bagicWorkTime");
			break;
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
			arguments.add(new BasicNameValuePair("folderToAccess", AbsoluteFilePath.FTP_FOLDER_WORJ_PATH));
			arguments.add(new BasicNameValuePair("fileName", getFileName()));
			arguments.add(new BasicNameValuePair("IMG1", signString));
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
				new registerCustomerApproval().execute("bagicWorkTime");
			} else {
				Toast.makeText(context, "사진등록 실패", 2000).show();
			}

		}
	}
	
	


	private class registerCustomerApproval extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime
			
			if (params[0].equals("bagicWorkTime")) {
			try {

				String param_url = WebServerInfo.getUrl()
						+ "ip/registerCustomerApproval.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo",  jobNo));
				arguments.add(new BasicNameValuePair("custSign", getFileName()));
				arguments.add(new BasicNameValuePair("clientId", clientId));
				arguments.add(new BasicNameValuePair("clientNm", et_fr_custNm.getText().toString()));
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
						new sendEmail().execute("bagicWorkTime");
					}
					else{
						progress(false);
						SimpleDialog sd02 = new SimpleDialog(context, "알림",
								"등록실패했습니다. 재등록해주시길 바랍니다.", new btnClickListener() {
							@Override
							public void onButtonClick() {
							}
						});
						sd02.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					progress(false);
					SimpleDialog sd02 = new SimpleDialog(context, "알림",
							"등록실패했습니다. 재등록해주시길 바랍니다.", new btnClickListener() {
						@Override
						public void onButtonClick() {
						}
					});
					sd02.show();
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
				arguments.add(new BasicNameValuePair("workDt", workDt));
				arguments.add(new BasicNameValuePair("jobNo",  jobNo));
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

				progress(false);
				String returnValue = "N";
				try {
					returnValue = returnJson01.getString("dataString");
					if(returnValue.equals("1")){
						
						Bundle b = new Bundle();
						b.putBoolean("confirmCode", true);
						// Add the set of extended data to the intent and start it
						Intent intent = new Intent();
						intent.putExtras(b);
						activity.setResult(activity.RESULT_OK, intent);
						activity.finish();
						dismiss();
					}
					else{
						SimpleDialog sd = new SimpleDialog(context, "알림",
								"등록실패했습니다. 재등록해주시길 바랍니다.", new btnClickListener() {
							@Override
							public void onButtonClick() {
							}
						});
						sd.show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
			}
		}
	}
	

	
	public class selectItemImage extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					sign = EasyDownLoad.getImage(AbsoluteFilePath.FTP_FOLDER_WORJ_PATH ,  custSign, commonSession.getEmpId());
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
			progress(false);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				try {
					if(sign == null){
						AlertView.showAlert( "이미지 불러오기를 실패했습니다.", context);
					}
					img_fr_sign.setImageBitmap(sign);
				}
				catch(Exception ex){
					AlertView.showAlert( "이미지 불러오기를 실패했습니다.", context);
				}
			}
		}
	}// end of SelectData inner-class
	
	
	private Bitmap sign;
	private String signString;
	
	private String getFileName() {
		String str = "";
		DateUtil du = new DateUtil();
		String ymd = du.getCurrentShortDate();
		str = refContrNo + "_" + ymd + "_S" + ".jpg";

		return str;

	}
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WT_RJ01_R03P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WT_RJ01_R03P.this.progress.dismiss();
		}
	}

}
