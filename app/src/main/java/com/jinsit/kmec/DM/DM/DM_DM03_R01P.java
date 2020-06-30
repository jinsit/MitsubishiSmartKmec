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
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 기존소스
 * OT승인 상세
 * @deprecated
 * @author 원성민
 *
 */
public class DM_DM03_R01P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView btn_dm_approve;
	private TextView btn_dm_cancelApproval;

	private TextView tv_dm_empNm;
	private TextView tv_dm_basicOt;
	private TextView tv_dm_onDutyOt;
	private TextView tv_dm_specialOt;
	private TextView tv_dm_totalOt;
	private TextView tv_dm_approvalYnNm;

	private DM_DM03_R00_ITEM02 item;
	private CommonSession commonSession;
	private EasyJsonMap ej01;
	private ProgressDialog progress;

	public interface btnClickListener {
		void onButtonClick();
	}

	btnClickListener inqueryListener;

	public DM_DM03_R01P(Context context, DM_DM03_R00_ITEM02 item,
						btnClickListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item = item;
		this.context = context;
		this.inqueryListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dm_dm03_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();

		tv01_popTitle.setText("상세보기");
		commonSession = new CommonSession(context);

		tv_dm_empNm.setText(item.getEmpNm());
		tv_dm_basicOt.setText(item.getBasicOt());
		tv_dm_onDutyOt.setText(item.getOnDutyOt());
		tv_dm_specialOt.setText(item.getSpecialOt());
		tv_dm_totalOt.setText(item.getTotalOt());
		tv_dm_approvalYnNm.setText(item.getApprovalYnNm());

		if (item.getApprovalYn().equals("1")) {
			this.btn_dm_approve.setVisibility(View.VISIBLE);
			this.btn_dm_cancelApproval.setVisibility(View.GONE);
		} else {
			this.btn_dm_approve.setVisibility(View.GONE);
			this.btn_dm_cancelApproval.setVisibility(View.VISIBLE);
		}
	}

	protected void getInstances() {
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_dm_approve = (TextView) findViewById(R.id.btn_dm_approve);
		btn_dm_cancelApproval = (TextView) findViewById(R.id.btn_dm_cancelApproval);
		tv_dm_empNm = (TextView) findViewById(R.id.tv_dm_empNm);
		tv_dm_basicOt = (TextView) findViewById(R.id.tv_dm_basicOt);
		tv_dm_onDutyOt = (TextView) findViewById(R.id.tv_dm_onDutyOt);
		tv_dm_specialOt = (TextView) findViewById(R.id.tv_dm_specialOt);
		tv_dm_totalOt = (TextView) findViewById(R.id.tv_dm_totalOt);
		tv_dm_approvalYnNm = (TextView) findViewById(R.id.tv_dm_approvalYnNm);

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
						approvalRequestOnOverTime();
						if (inqueryListener != null)
							inqueryListener.onButtonClick();
					}

				});
		btn_dm_cancelApproval
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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
				this.progress(true);
				new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
				break;
			default:
				break;
		}
	}

	private void approvalRequestOnOverTime() {
		this.progress(true);
		new insertApprovalResultOnOverTime().execute("bagicWorkTime");
	}

	private void cancelApprovalRequestOnOverTime() {
		this.progress(true);
		new insertCancelApprovalResultOnOverTime().execute("bagicWorkTime");
	}

	public class insertApprovalResultOnOverTime extends
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
					arguments.add(new BasicNameValuePair("otWorkDt", item
							.getOtWorkDt()));
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
					Toast.makeText(context, "승인 성공 했습니다.", Toast.LENGTH_SHORT)
							.show();
					DM_DM03_R01P.this.dismiss();
				} else {
					Toast.makeText(context, "승인 등록 실패 했습니다.",
							Toast.LENGTH_SHORT).show();
				}
			}

		}
	}// end of SelectData inner-class

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
					arguments.add(new BasicNameValuePair("csEmpId",
							commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("otWorkDt", item
							.getOtWorkDt()));
					arguments.add(new BasicNameValuePair("approvalYn", "N"));
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
					Toast.makeText(context, "승인취소 성공 했습니다.", Toast.LENGTH_SHORT)
							.show();
					DM_DM03_R01P.this.dismiss();

				} else {
					Toast.makeText(context, "승인취소 실패 했습니다.", Toast.LENGTH_SHORT)
							.show();
				}
			}

		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM03_R01P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			DM_DM03_R01P.this.progress.dismiss();
		}
	}

}
	
	
