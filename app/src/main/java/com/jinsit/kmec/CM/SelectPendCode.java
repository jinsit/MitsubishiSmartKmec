package com.jinsit.kmec.CM;

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
import android.content.DialogInterface.OnDismissListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01_ITEM01;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SelectPendCode extends Dialog implements OnClickListener, OnItemClickListener, OnDismissListener{

	public SelectPendCode(Context c, String[] param, Activity activity, ArrayList<WO_WT00_R01_ITEM01> workStatusData) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
		this.workParam = param;
		this.activity = activity;
		this.workStatusData = workStatusData;
	}


	public SelectPendCode(Context c, String[] param, Activity activity) {
		super(c);
		// TODO Auto-generated constructor stub
		this.context = c;
		this.workParam = param;
		this.activity = activity;
	}

	private ArrayList<WO_WT00_R01_ITEM01> workStatusData;
	private ProgressDialog ProgressDialog;
	ArrayList<SelectPendCodeData> pendCodeData;
	private EasyJsonList ejl;
	Button btn_cancel;
	Activity activity;
	TextView btn_popClose;
	ListView lv_search_pendCode;
	SelectPendCodeAdapter PendCodeAdapter;
	Context context;
	public boolean isPended=false;

	private String[] workParam;
	public String reasonNm = "";
	public String getreasonNm() {
		return reasonNm;
	}

	public void setreasonNm(String reasonNm) {
		this.reasonNm = reasonNm;
	}
	public String reasonCd = "";

	public String getreasonCd() {
		return reasonCd;
	}

	public void setreasonCd(String depCd) {
		this.reasonCd = depCd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_search_dept);

		TextView title = (TextView)findViewById(R.id.tv01_popTitle);
		title.setText("미처리");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_search_pendCode = (ListView) findViewById(R.id.lv_search_dept);
		lv_search_pendCode.setOnItemClickListener(this);

		deptSearch();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_popClose:
				cancel();
				break;

		}

	}

	private void deptSearch() {
		// TODO Auto-generated method stub
		new PendCodeSearch().execute();
	}

	public class PendCodeSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			SelectPendCode.this.ProgressDialog =
					android.app.ProgressDialog.show(context, "미처리사유","미처리사유를 불러오는 중입니다.");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				pendCodeData = new ArrayList<SelectPendCodeData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/selectPendCode.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					pendCodeData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						pendCodeData.add(new SelectPendCodeData(ejl.getValue(i,"YET_REASON_CD"),
								ejl.getValue(i, "YET_REASON_NM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			SelectPendCode.this.ProgressDialog.dismiss();
			PendCodeAdapter = new SelectPendCodeAdapter(context, pendCodeData);
			lv_search_pendCode.setAdapter(PendCodeAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		SelectPendCodeData resData = pendCodeData.get(position);
		reasonCd = resData.getYET_REASON_CD();
		reasonNm = resData.getYET_REASON_NM();
		pendWork();
		//dismiss();
	}
	private void pendWork() {
		// TODO Auto-generated method stub

		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
				"미처리 하시겠습니까?", new btnClickListener() {
			@Override
			public void onButtonClick() {
				// TODO Auto-generated method stub

				new UpdatePendWork().execute();

			}
		});
		ynDialog.show();
	}


	public class UpdatePendWork extends AsyncTask<Void, Void, Void> {
		EasyJsonMap ejm01;
		String resultStr;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "cm/updatePendWorkMap.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId",workParam[0]));
				arguments.add(new BasicNameValuePair("workDt",workParam[1] ));
				arguments.add(new BasicNameValuePair("jobNo", workParam[2]));
				arguments.add(new BasicNameValuePair("yetReason", reasonCd));
				arguments.add(new BasicNameValuePair("usrId",workParam[0]));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);


				try {

					ejm01 = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
					resultStr = returnJson.getString("dataString");
					Log.v("resultStr", "resultStr = " + resultStr);
					if(resultStr.equals("1")){
						isPended = true;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress(false);

			try {
				boolean isError = ejm01.getValue("errCd").equals("1") ? true : false;
				if (!isError)
				{
					String rtn = resultStr;

					if (rtn.equals("1")) {

						Toast.makeText(context, "미처리 하였습니다.", Toast.LENGTH_SHORT).show();
						if(workStatusData != null)	//정기점검이 아닌경우 로칼 디비 업데이트 처리할 필요없기 때문에 20180308 yowonsm
						{
							isJobStatusCheck();
							updateJobComplete();//로칼디비 미처리 변경해주기
						}
						activity.finish();
						dismiss();
					}

				} else if (isError) {
					alert(ejm01.getValue("errMsg"), activity);
					Toast.makeText(context, "미처리 되지 않았습니다.", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


		/*	if(isPended){
				Toast.makeText(context, "미처리 하였습니다.", Toast.LENGTH_SHORT).show();
				isJobStatusCheck();
				updateJobComplete();//로칼디비 미처리 변경해주기
				activity.finish();
				dismiss();
			}else{

				Toast.makeText(context, "미처리 되지 않았습니다.", Toast.LENGTH_SHORT).show();
			}
		*/


		}
	}
	///미처리 43번 , 이관 45번
	private void updateJobComplete(){
		String query = new DatabaseRawQuery().updateJobStChange
				(workParam[0], workParam[1], workParam[2], DateUtil.nowDateFormat("HH:mm"), "43", "미처리");

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	private void updatePendPart(String nfcPlc){
		String query = new DatabaseRawQuery().updateStartPartCheck
				(workParam[0], workParam[1], workParam[2], nfcPlc, "", "00");

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();

	}


	private void isJobStatusCheck(){

		for (int i = 0; i < workStatusData.size(); i++) {
			if (!workStatusData.get(i).getJOB_ST().equals("00") && !workStatusData.get(i).getJOB_ST().equals("39")) {
				this.updatePendPart(workStatusData.get(i).getNFC_PLC());
			}
		}

	}


	private void progress(Boolean isActivated) {
		if (isActivated) {
			SelectPendCode.this.ProgressDialog = android.app.ProgressDialog.show(
					context, "미처리", "처리 중입니다.");
		} else {
			SelectPendCode.this.ProgressDialog.dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		this.setreasonCd(reasonCd);
		this.setreasonNm(reasonNm);
	}
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

}
