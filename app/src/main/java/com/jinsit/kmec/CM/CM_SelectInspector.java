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
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.KeyboardUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class CM_SelectInspector extends Dialog implements OnClickListener,
		OnItemClickListener, OnDismissListener {

	public CM_SelectInspector(Context context, int type, String[] param, Activity activity) {
		super(context);
		// TODO Auto-generated constructor stub
		this.workType = type;
		this.workParam = param;
		this.context = context;
		this.activity = activity;
		if(workType!=1)this.workName ="지원요청";


	}

	private ProgressDialog progress;
	private TextView btn_cm_searchInspector;
	private TextView btn_popClose;
	private EditText et_cm_searchBox;
	private ListView lv_cm_searchList;

	private CM_SelectInspector_Adapter adapter;
	private ArrayList<CM_SelectInspector_ITEM> itemList;
	private EasyJsonList ejl01;

	private Context context;
	private Activity activity;
	private int workType;
	private String[] workParam;
	private String workName = "이관";
	private boolean isUpdateWork = false;
	public boolean isTransfer=false;
	public boolean isHelp=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_selectinspector);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		//this.context = context;
		itemList = new ArrayList<CM_SelectInspector_ITEM>();
	}

	protected void getInstances() {
		TextView title = (TextView)findViewById(R.id.tv01_popTitle);
		title.setText(workName);
		btn_cm_searchInspector = (TextView) findViewById(R.id.btn_cm_selectInspector);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		et_cm_searchBox = (EditText) findViewById(R.id.et_cm_selectBox);

		lv_cm_searchList = (ListView) findViewById(R.id.lv_cm_selectList);
		setEvents();
	}

	protected void setEvents() {
		btn_cm_searchInspector.setOnClickListener(this);
		btn_popClose.setOnClickListener(this);
		lv_cm_searchList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cm_selectInspector:
				inspectorSearch();
				break;
			case R.id.btn_popClose:
				dismiss();
				break;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		currentSelectedItem = itemList.get(position);
		updateWork();
	}

	private void updateWork() {
		// TODO Auto-generated method stub

		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(context,
				"작업을 " + workName+ " 하시겠습니까?", new btnClickListener() {

			@Override
			public void onButtonClick() {
				// TODO Auto-generated method stub
				transferHelpWork();

			}
		});
		ynDialog.show();

	}


	private void transferHelpWork() {
		// TODO Auto-generated method stub
		switch(workType){
			case 1:
				new UpdateTransferWork().execute();
				break;

			case 2:
				new UpdateHelpWork().execute();
				break;
		}
	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	private void inspectorSearch() {
		// TODO Auto-generated method stub
		if (et_cm_searchBox.getText().toString().equals("")) {
			Toast.makeText(context, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
		} else {
			progress(true);
			new SearchInspectorName().execute();
		}

	}


	public class UpdateHelpWork extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				itemList.clear();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "cm/updateHelpWork.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empIdS",workParam[0]));
				arguments.add(new BasicNameValuePair("workDtS",workParam[1] ));
				arguments.add(new BasicNameValuePair("jobNoS", workParam[2]));
				arguments.add(new BasicNameValuePair("empIdG", currentSelectedItem.getCsEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				try {

					String resultStr = returnJson.getString("dataString");
					Log.v("resultStr", "resultStr = " + resultStr);
					if(resultStr.equals("1")){
						isHelp = true;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			/*	try {
					ejl01 = new EasyJsonList(
							returnJson.getJSONArray("dataList"));
					itemList.clear();


					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new CM_SelectInspector_ITEM(ejl01.getValue(i,
								"CS_EMP_ID"), ejl01.getValue(i, "DEPT_NM"), ejl01
								.getValue(i, "EMP_NM"), ejl01.getValue(i,
								"PHONE_1")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}*/

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress(false);

			if(isHelp){
				Toast.makeText(context, "지원요청하였습니다.", Toast.LENGTH_SHORT).show();
				//if(isUpdateWork)
				//activity.finish();
				dismiss();
			}else{
				Toast.makeText(context, "지원요청 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}


//			Toast.makeText(context, "지원요청하였습니다.", Toast.LENGTH_SHORT).show();
//			if(isUpdateWork)activity.finish();
//			dismiss();

		}
	}

	public class UpdateTransferWork extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				itemList.clear();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "cm/updateTransferWork.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empIdS",workParam[0]));
				arguments.add(new BasicNameValuePair("workDtS",workParam[1] ));
				arguments.add(new BasicNameValuePair("jobNoS", workParam[2]));
				arguments.add(new BasicNameValuePair("empIdG", currentSelectedItem.getCsEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);


				try {

					String resultStr = returnJson.getString("dataString");
					Log.v("resultStr", "resultStr = " + resultStr);
					if(resultStr.equals("1")){
						isTransfer = true;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			/*	try {
					ejl01 = new EasyJsonList(
							returnJson.getJSONArray("dataList"));
					itemList.clear();


					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new CM_SelectInspector_ITEM(ejl01.getValue(i,
								"CS_EMP_ID"), ejl01.getValue(i, "DEPT_NM"), ejl01
								.getValue(i, "EMP_NM"), ejl01.getValue(i,
								"PHONE_1")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}*/

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progress(false);

			if(isTransfer){
				Toast.makeText(context, "이관하였습니다.", Toast.LENGTH_SHORT).show();
				updateJobComplete();
				//if(isUpdateWork)
				activity.finish();
				dismiss();
			}else{
				Toast.makeText(context, "이관에 실패하였습니다.", Toast.LENGTH_SHORT).show();
			}
//			Toast.makeText(context, "이관하였습니다.", Toast.LENGTH_SHORT).show();
//			if(isUpdateWork)activity.finish();
//			dismiss();

		}
	}

	///미처리 43번 , 이관 45번
	void updateJobComplete(){
		String query = new DatabaseRawQuery().updateJobStChange
				(workParam[0], workParam[1], workParam[2], DateUtil.nowDateFormat("HH:mm"), "45", "이관");

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();
		dbHelper.close();
	}

	public class SearchInspectorName extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				itemList.clear();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "cm/selectInspector.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empNm", et_cm_searchBox
						.getText().toString()));
				arguments.add(new BasicNameValuePair("deptNm", ""));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl01 = new EasyJsonList(
							returnJson.getJSONArray("dataList"));
					itemList.clear();


					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new CM_SelectInspector_ITEM(ejl01.getValue(i,
								"CS_EMP_ID"), ejl01.getValue(i, "DEPT_NM"), ejl01
								.getValue(i, "EMP_NM"), ejl01.getValue(i,
								"PHONE_1")));
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
			adapter = new CM_SelectInspector_Adapter(context, itemList);
			lv_cm_searchList.setAdapter(adapter);
			KeyboardUtil.hideKeyboard(et_cm_searchBox, (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE)).sendEmptyMessageDelayed(0, 100);

		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SelectInspector.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SelectInspector.this.progress.dismiss();
		}
	}

	private CM_SelectInspector_ITEM currentSelectedItem;

	private String bldgAddr = "";


	public String getCsDeptNm() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getCsEmpId();
	}

	public String getBldgAddr() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getDeptNm();
	}

	public String getBldgNm() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getEmpNm();
	}

	public String getBldgNo() {
		if (currentSelectedItem == null) {
			return "";
		}
		return currentSelectedItem.getPhone1();
	}

}
