package com.jinsit.kmec.DM.DM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DM.DM.DM_DM03_R01P.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class DM_DM03_R03P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView btn_dm_approve;
	private TextView btn_dm_cancelApproval,btn_dm_reject;

	private TextView tv_dm0303_empNm;
	private TextView tv_dm0303_bldgNm,tv_dm0303_workNm;

	private TextView tv_dm0303_carNo,tv_dm0303_totalOtTm;
	private TextView tv_dm0303_rmk;


	private DM_DM03_R02_ITEM02 item;
	private CommonSession commonSession;
	private EasyJsonMap ej01;
	private ProgressDialog progress;

	public interface btnClickListener {
		void onButtonClick();
	}

	btnClickListener inqueryListener;
	String date;
	String repSt,repStNm;
	public DM_DM03_R03P(Context context, DM_DM03_R02_ITEM02 item,
						btnClickListener listener, String date) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item = item;
		this.context = context;
		this.inqueryListener = listener;
		this.date = date;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm03_r03p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();

		tv01_popTitle.setText("특별OT 승인");
		commonSession = new CommonSession(context);



		tv_dm0303_empNm.setText(item.getEMP_NM());
		tv_dm0303_bldgNm.setText(item.getBLDG_NM());
		tv_dm0303_carNo.setText(item.getCAR_NO());
		tv_dm0303_totalOtTm.setText(item.getOT_TM() + " / " + item.getTOTAL_OT_TM());
		tv_dm0303_workNm.setText(item.getWORK_NM());
		tv_dm0303_rmk.setText(item.getRMK());

		//  ,REP_ST -- 상신상태 (0:계획(승인취소),1:상신,2:승인,9:REJECT)
		if (item.getREP_ST().equals("1")) {
			this.btn_dm_approve.setVisibility(View.VISIBLE);
			this.btn_dm_cancelApproval.setVisibility(View.GONE);
			this.btn_dm_reject.setVisibility(View.VISIBLE);
		} else if(item.getREP_ST().equals("2")){
			this.btn_dm_approve.setVisibility(View.GONE);
			this.btn_dm_cancelApproval.setVisibility(View.VISIBLE);
			this.btn_dm_reject.setVisibility(View.VISIBLE);
		}else if(item.getREP_ST().equals("9")){
			this.btn_dm_approve.setVisibility(View.GONE);
			this.btn_dm_cancelApproval.setVisibility(View.GONE);
			this.btn_dm_reject.setVisibility(View.GONE);
		}

	}

	protected void getInstances() {
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_dm_approve = (TextView) findViewById(R.id.btn_dm_approve);
		btn_dm_cancelApproval = (TextView) findViewById(R.id.btn_dm_cancelApproval);
		btn_dm_reject = (TextView) findViewById(R.id.btn_dm_reject);
		tv_dm0303_empNm = (TextView) findViewById(R.id.tv_dm0303_empNm);
		tv_dm0303_workNm = (TextView) findViewById(R.id.tv_dm0303_workNm);
		tv_dm0303_bldgNm = (TextView) findViewById(R.id.tv_dm0303_bldgNm);
		tv_dm0303_carNo = (TextView) findViewById(R.id.tv_dm0303_carNo);
		tv_dm0303_totalOtTm = (TextView) findViewById(R.id.tv_dm0303_totalOtTm);
		tv_dm0303_rmk = (TextView) findViewById(R.id.tv_dm0303_rmk);


		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);

		Button btn = (Button) findViewById(R.id.btn_dialogBtn);
		btn_dm_approve
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						repSt = "2";//승인
						repStNm = "승인";
						cancelApprovalRequestOnOverTime();
						if (inqueryListener != null)
							inqueryListener.onButtonClick();
					}

				});
		btn_dm_cancelApproval
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						repSt = "0";//승인취소
						repStNm = "승인취소";
						cancelApprovalRequestOnOverTime();
						if (inqueryListener != null)
							inqueryListener.onButtonClick();
					}
				});

		btn_dm_reject.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				repSt = "9";//거절
				repStNm = "거절";
				cancelApprovalRequestOnOverTime();
				if (inqueryListener != null)
					inqueryListener.onButtonClick();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				this.dismiss();
				break;
			case R.id.btn_dm_cancelApproval:
				repSt = "0";//승인취소
				this.progress(true);
				new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
				break;
			case R.id.btn_dm_reject:
				repSt = "9";//거절
				this.progress(true);
				new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
				break;
			case R.id.btn_dm_approve:
				repSt = "2";//승인
				this.progress(true);
				new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
				break;

			default:
				break;
		}
	}


	private void cancelApprovalRequestOnOverTime() {
		this.progress(true);
		new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
	}

	/*public class insertApprovalResultOnOverTime extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/insertApprovalResultOnOverTime.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("csEmpId",
							commonSession.getEmpId()));
					//arguments.add(new BasicNameValuePair("otWorkDt", item.getOtWorkDt()));
					arguments.add(new BasicNameValuePair("approvalYn", "Y"));
					arguments.add(new BasicNameValuePair("approvalDt", DateUtil
							.nowDate()));
					arguments.add(new BasicNameValuePair("usrId", commonSession
							.getEmpId()));

					returnJson01 = http.getPost(param_url_01, arguments, true);
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

				String resultFg = "";
				try {
					resultFg = returnJson01.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if (resultFg.equals("1")) {
					Toast.makeText(context, repStNm  +" 성공 했습니다.", Toast.LENGTH_SHORT)
							.show();
					DM_DM03_R03P.this.dismiss();
				} else {
					Toast.makeText(context, repStNm  +" 실패 했습니다.",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}// end of SelectData inner-class
*/
	public class insertCancelApprovalResultOnOverTime extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/insertApprovalResultOnOverTime.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selSt","2"));
					arguments.add(new BasicNameValuePair("csEmpId",item.getCS_EMP_ID()));
					arguments.add(new BasicNameValuePair("otWorkDt", date));
					arguments.add(new BasicNameValuePair("otNo", item.getOT_NO()));
					arguments.add(new BasicNameValuePair("repSt", repSt));
					arguments.add(new BasicNameValuePair("apprEmpId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("usrId", item.getCS_EMP_ID()));

					returnJson01 = http.getPost(param_url_01, arguments, true);
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

				String resultFg = "";
				try {
					resultFg = returnJson01.getString("dataString");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if (resultFg.equals("1")) {
					Toast.makeText(context, repStNm  +" 성공 했습니다.", Toast.LENGTH_SHORT)
							.show();
					DM_DM03_R03P.this.dismiss();
				} else {
					Toast.makeText(context, repStNm  +" 실패 했습니다.",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM03_R03P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			DM_DM03_R03P.this.progress.dismiss();
		}
	}

}
	
	
