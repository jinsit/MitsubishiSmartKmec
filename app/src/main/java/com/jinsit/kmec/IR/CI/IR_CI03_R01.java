package com.jinsit.kmec.IR.CI;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jinsit.kmec.IR.CI.IR_CI00_R00_F.FRAGMENT_INSPECTION_HISTORY;

public class IR_CI03_R01 extends Fragment implements OnItemClickListener,
		OnClickListener {
	private Context context;
	private Activity activity;
    private IR_CI00_R00_F parentActivity;
	/// coom_dialog_inquery_startend_date 위젯
	//private TextView tv_menutitle;
	//private TextView btn_menu;

	private TextView et_inqueryStartDate;
	private TextView btn_startCalendar;
	private TextView et_inqueryEndDate;
	private TextView btn_endCalendar;

	private TextView btn_inquery;
	private TextView tab1TextView ;
	private TextView tab2TextView ;
	///////////////////////////////////////////

	///3
	private TextView tv_ci03_modelNm;
	private ListView lv_ci03_inspectionHistory;
	private IR_CI03_R01_Adapter00 adapter01;
	private List<IR_CI03_R01_ITEM00> itemList01;
	//



	private android.app.ActionBar aBar;

	private CM_SearchBldgInfo_ITEM01 item01;


	private EasyJsonList ejl01;
	private EasyJsonList ejl02;


	private ProgressDialog progress;


	/**
	 *
	 */
	public IR_CI03_R01(CM_SearchBldgInfo_ITEM01 item) {
		super();
		// TODO Auto-generated constructor stub
		this.item01 = item;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
        parentActivity = (IR_CI00_R00_F)activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ir_ci03_r01, null);
		activityInit(view);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("SaleListFragmentActivity.onActivityCrated");

	}

	protected void activityInit(View view) {
		getInstances(view);
		setEvents();
		context = this.activity;

		// 타이틀 바
		aBar =  this.activity.getActionBar();
		aBar.setTitle("점검 계획");
		aBar.setDisplayShowHomeEnabled(false);

		this.setInqueryStartDate(DateUtil.nowDate());
		this.setInqueryEndDate(DateUtil.nowDate());
		itemList01 = new ArrayList<IR_CI03_R01_ITEM00>();
		//this.tv_menutitle.setText("호기선택");

	}

	protected void getInstances(View view) {

		////3////
		tv_ci03_modelNm = (TextView)view.findViewById(R.id.tv_ci03_modelNm);
		lv_ci03_inspectionHistory = (ListView)view.findViewById(R.id.lv_ci03_inspectionHistory);
		/////////////

		/////공통 조회 부분////
		this.et_inqueryStartDate = (TextView)view.findViewById(R.id.et_inqueryStartDate);
		this.btn_startCalendar = (TextView)view.findViewById(R.id.btn_startCalendar);
		this.et_inqueryEndDate  = (TextView)view.findViewById(R.id.et_inqueryEndDate);
		this.btn_endCalendar = (TextView)view.findViewById(R.id.btn_endCalendar);
		this.btn_inquery = (TextView)view.findViewById(R.id.btn_inquery);
		//this.btn_menu =  (TextView)view.findViewById(R.id.btn_menu);
		//this.tv_menutitle = (TextView)view.findViewById(R.id.tv_menutitle);
		tab1TextView = (TextView)view.findViewById(R.id.tab1TextView);
		tab2TextView = (TextView)view.findViewById(R.id.tab2TextView);
		///////
		setEvents();
	}

	protected void setEvents() {
		// ///공통 조회 부분////
		this.btn_startCalendar.setOnClickListener(this);
		this.btn_endCalendar.setOnClickListener(this);
		this.btn_inquery.setOnClickListener(this);
		//this.btn_menu.setOnClickListener(this);
		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);
		// /////

		// //3////
		lv_ci03_inspectionHistory.setOnItemClickListener(this);
		tab1TextView.setOnClickListener(this);
		tab2TextView.setOnClickListener(this);
		// ///////////

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Bundle extras;
		switch (v.getId()) {
			// / coom_dialog_inquery_startend_date 위젯

			case R.id.btn_startCalendar:
				getCalendar(0);
				break;
			case R.id.btn_endCalendar:
				getCalendar(1);
				break;
			case R.id.btn_inquery:
				ci03Inquery();
				break;
			case R.id.et_inqueryStartDate:
				getCalendar(0);
				break;
			case R.id.et_inqueryEndDate:
				getCalendar(1);
				break;
			case R.id.tab1TextView:
				parentActivity.fragmentReplace(FRAGMENT_INSPECTION_HISTORY);
				break;

			// ///////////////////////

			default:
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			this.currentSelectedItem01 = itemList01.get(position);


			//Intent intent = new Intent(WO_WO00_R00F.this, WO_WT00_R03.class);
//			Intent intent = new Intent(context,  IR_CI03_R02.class);
//			intent.putExtra("empId", currentSelectedItem01.getCsEmpId());
//			intent.putExtra("workDt", currentSelectedItem01.getWorkDt());
//			intent.putExtra("bldgNo", item01.getBldgNo());
//			intent.putExtra("bldgNm", item01.getBldgNm());
//			intent.putExtra("carNo", currentSelectedItem01.getCarNo());
//			intent.putExtra("isCI", true);
//			startActivity(intent);

				progress(true);
				new CreateInspectionDetail().execute("bagicWorkTime");
		} else {
			this.currentSelectedItem01 = null;
		}
	}

	public class selectInspectionSchedule extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectInspectionSchedule.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("bldgNo",
							IR_CI03_R01.this.item01.getBldgNo()));
					arguments.add(new BasicNameValuePair("baseDtFr",
							IR_CI03_R01.this.getInqueryStartDate()));
					arguments.add(new BasicNameValuePair("baseDtTo",
							IR_CI03_R01.this.getInqueryEndDate()));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
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

				try {
					itemList01.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList01.add(new IR_CI03_R01_ITEM00(ejl01.getValue(i,"WORK_DT"),
								ejl01.getValue(i, "CAR_NO"),
								ejl01.getValue(i, "CS_EMP_NM"),
								ejl01.getValue(i, "CS_EMP_ID"),
								ejl01.getValue(i, "JOB_NO"),
								ejl01.getValue(i, "TP")
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				changeCi03Inquery();
			}
		}
	}// end of SelectData inner-class



	public class CreateInspectionDetail extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		private String exceptionMsg = null;
		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/createInspectionSchedule.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("csEmpId",
							IR_CI03_R01.this.currentSelectedItem01.getCsEmpId()));
					arguments.add(new BasicNameValuePair("workDt",
							IR_CI03_R01.this.currentSelectedItem01.getWorkDt()));
					arguments.add(new BasicNameValuePair("jobNo",
							IR_CI03_R01.this.currentSelectedItem01.getJobNo()));
					arguments.add(new BasicNameValuePair("tp",
							IR_CI03_R01.this.currentSelectedItem01.getTp()));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						dataMap = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
						msgMap = new EasyJsonMap(returnJson01.getJSONObject("msgMap"));
					} catch (JSONException e) {
						e.printStackTrace();
						exceptionMsg = e.toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					exceptionMsg = e.toString();
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
					boolean isError = msgMap.getValue("errCd").equals("0") ? true : false;//0이면 정상
					if (isError) {
						if (dataMap.getValue("VAL").equals("OK")) {
							//정상 : 점검항목 생성됨
							Intent intent = new Intent(context,  IR_CI03_R02.class);
							intent.putExtra("empId", currentSelectedItem01.getCsEmpId());
							intent.putExtra("workDt", currentSelectedItem01.getWorkDt());
							intent.putExtra("bldgNo", item01.getBldgNo());
							intent.putExtra("bldgNm", item01.getBldgNm());
							intent.putExtra("carNo", currentSelectedItem01.getCarNo());
							intent.putExtra("isCI", false);
							startActivity(intent);

						} else {
							//실패 : 점검항목 생성X
							AlertView.showAlert("점검항목을 생성실패 하였습니다. 관리자에게 문의하세요", context);
						}
					} else {
						AlertView.showAlert(msgMap.getValue("errMsg"), context);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}
	}// end of SelectData inner-class

	private void ci03Inquery()
	{
		progress(true);
        new selectInspectionSchedule().execute("bagicWorkTime");
//		if (this.carNo03 != "") {
//			progress(true);
//			new selectInspectionSchedule().execute("bagicWorkTime");
//		}
//		else
//		{
//			AlertView.showAlert("호기를 조회 해주시길 바랍니다.", context);
//		}
	}

	private void changeCi03Inquery()
	{

		if(itemList01.size() == 0){
			AlertView.showAlert("조회된 데이타가 없습니다", context);
			return;
		}
		adapter01 = new IR_CI03_R01_Adapter00(context,
				R.layout.ir_ci03_r01_adapter00, itemList01);
		IR_CI03_R01.this.lv_ci03_inspectionHistory.setAdapter(adapter01);
	}



	private void getCalendar(int fromTo) {
		Intent intent = new Intent(this.activity, com.jinsit.kmec.comm.CalendarGridNoDataActivity.class);
		startActivityForResult(intent, fromTo);
	}

	/**
	 * @author 원성민 캘린더를 호출하여 선택된 날짜를 반환받을 때 사용합니다.
	 *
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == -1) {
					Bundle bundle = data.getExtras();
					this.setInqueryStartDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}

			case 1:
				if (resultCode == -1) {
					Bundle bundle = data.getExtras();
					this.setInqueryEndDate(bundle
							.getString("dateSelected"));
					break;
				} else {
					break;
				}
			default:
				break;
		}
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_CI03_R01.this.progress = ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI03_R01.this.progress.dismiss();
		}
	}

	/// coom_dialog_inquery_startend_date 위젯
	public String getInqueryStartDate() {
		return  this.et_inqueryStartDate.getText().toString();
	}

	public void setInqueryStartDate(String inqueryStartDate) {
		this.et_inqueryStartDate.setText(inqueryStartDate);
	}
	public String getInqueryEndDate() {
		return  this.et_inqueryEndDate.getText().toString();
	}

	public void setInqueryEndDate(String inqueryEndDate) {
		this.et_inqueryEndDate.setText(inqueryEndDate);
	}
	///////////////////////////
	private IR_CI03_R01_ITEM00 currentSelectedItem01;




}
