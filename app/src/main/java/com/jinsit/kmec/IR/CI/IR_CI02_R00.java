package com.jinsit.kmec.IR.CI;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_CI02_R00 extends Fragment implements OnItemClickListener,
		OnClickListener {
	private Context context;
	private Activity activity;
	// / coom_dialog_inquery_startend_date 위젯
	private TextView tv_menutitle;
	private TextView btn_menu;

	private TextView et_inqueryStartDate;
	private TextView btn_startCalendar;
	private TextView et_inqueryEndDate;
	private TextView btn_endCalendar;

	private TextView btn_inquery;
	// /////////////////////////////////////////

	// /2
	private TextView btn_ci02_all;
	private TextView btn_ci02_breakdown;
	private TextView btn_ci02_service;

	private ListView lv_ci02_history;
	private IR_CI02_R00_Adapter01 adapter01;
	private List<IR_CI02_R00_ITEM01> itemList01;
	private IR_CI02_R01P_ITEM01 item02;

	//

	private android.app.ActionBar aBar;

	private CM_SearchBldgInfo_ITEM01 item01;

	private EasyJsonList ejl01;
	private EasyJsonMap ej01;

	private ProgressDialog progress;

	private final static int TAB_ALL =0;
	private final static int TAB_BREAKDOWN =2;
	private final static int TAB_SERVICE =1;


	/**
	 *
	 */
	public IR_CI02_R00(CM_SearchBldgInfo_ITEM01 item) {
		super();
		// TODO Auto-generated constructor stub
		this.item01 = item;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ir_ci02_r00, null);
		activityInit(view);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}


	protected void activityInit(View view) {
		getInstances(view);
		setEvents();
		context = this.activity;

		// 타이틀 바
		aBar = this.activity.getActionBar();
		aBar.setTitle("고장 이력");
		aBar.setDisplayShowHomeEnabled(false);

		itemList01 = new ArrayList<IR_CI02_R00_ITEM01>();
		carNoList = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			carNoList.add("");
		}

		this.setInqueryStartDate(DateUtil.nowDate());
		this.setInqueryEndDate(DateUtil.nowDate());
		btnSelect(selTp);
	}

	protected void getInstances(View view) {
		// //2////
		btn_ci02_all = (TextView) view.findViewById(R.id.btn_ci02_all);
		btn_ci02_breakdown = (TextView) view.findViewById(R.id.btn_ci02_breakdown);
		btn_ci02_service = (TextView) view.findViewById(R.id.btn_ci02_service);
		lv_ci02_history = (ListView) view.findViewById(R.id.lv_ci02_history);
		// ///////////

		// ///공통 조회 부분////
		this.et_inqueryStartDate = (TextView) view.findViewById(R.id.et_inqueryStartDate);
		this.btn_startCalendar = (TextView) view.findViewById(R.id.btn_startCalendar);
		this.et_inqueryEndDate = (TextView) view.findViewById(R.id.et_inqueryEndDate);
		this.btn_endCalendar = (TextView) view.findViewById(R.id.btn_endCalendar);
		this.btn_inquery = (TextView) view.findViewById(R.id.btn_inquery);
		this.btn_menu = (TextView) view.findViewById(R.id.btn_menu);
		this.tv_menutitle = (TextView)view.findViewById(R.id.tv_menutitle);
		// /////
		setEvents();
	}

	protected void setEvents() {

		// ///공통 조회 부분////
		this.btn_startCalendar.setOnClickListener(this);
		this.btn_endCalendar.setOnClickListener(this);
		this.et_inqueryStartDate.setOnClickListener(this);
		this.et_inqueryEndDate.setOnClickListener(this);
		this.btn_inquery.setOnClickListener(this);
		this.btn_menu.setOnClickListener(this);
		// /////

		// ////2
		btn_ci02_all.setOnClickListener(this);
		btn_ci02_breakdown.setOnClickListener(this);
		btn_ci02_service.setOnClickListener(this);
		lv_ci02_history.setOnItemClickListener(this);
		// ////////////

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// / coom_dialog_inquery_startend_date 위젯
			case R.id.btn_menu:
				popupMenu(v, R.menu.menu_ir_ci02);
				break;
			case R.id.btn_startCalendar:
				getCalendar(0);
				break;
			case R.id.btn_endCalendar:
				getCalendar(1);
				break;
			case R.id.et_inqueryStartDate:
				getCalendar(0);
				break;
			case R.id.et_inqueryEndDate:
				getCalendar(1);
				break;
			case R.id.btn_inquery:
				ci02Inquery();
				break;
			case R.id.btn_ci02_all:
				changeCi02Inquery(TAB_ALL);
				btnSelect(TAB_ALL);
				break;
			case R.id.btn_ci02_breakdown:
				changeCi02Inquery(TAB_BREAKDOWN);
				btnSelect(TAB_BREAKDOWN);
				break;
			case R.id.btn_ci02_service:
				changeCi02Inquery(TAB_SERVICE);
				btnSelect(TAB_SERVICE);
				break;
			default:
				break;
		}
	}



	private void btnSelect(int idx) {

		btn_ci02_all.setBackgroundResource(R.drawable.tab_ci02_all_off);
		btn_ci02_breakdown.setBackgroundResource(R.drawable.tab_ci02_breakdown_off);
		btn_ci02_service.setBackgroundResource(R.drawable.tab_ci02_service_off);

		btn_ci02_all.setEnabled(true);
		btn_ci02_breakdown.setEnabled(true);
		btn_ci02_service.setEnabled(true);
		switch (idx) {
			case TAB_ALL:
				btn_ci02_all.setBackgroundResource(R.drawable.tab_ci02_all_on);
				btn_ci02_all.setEnabled(false);
				break;
			case TAB_BREAKDOWN:
				btn_ci02_breakdown.setBackgroundResource(R.drawable.tab_ci02_breakdown_on);
				btn_ci02_breakdown.setEnabled(false);
				break;
			case TAB_SERVICE:
				btn_ci02_service
						.setBackgroundResource(R.drawable.tab_ci02_service_on);
				btn_ci02_service.setEnabled(false);
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {

			this.currentSelectedItem01 = 	this.getCI02Item(selTp).get(position);
			/**
			 * 전역으로 선언된 itemList01은 selTp에 따라 분류가 안되서 데이터가 맞지 않는다.
			 * selTp에 따라 분류된 itemList를 받아와서 Detail을 조회해야 데이터가 맞더라
			 */
			//this.currentSelectedItem01 = itemList01.get(position);
			progress(true);
			new selectTroubleDetail().execute("bagicWorkTime");
		} else {
			this.currentSelectedItem01 = null;
		}
	}

	public class selectTroubleHistory extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectTroubleHistory.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("bldgNo",
							IR_CI02_R00.this.item01.getBldgNo()));
					arguments.add(new BasicNameValuePair("recvDtFr",
							IR_CI02_R00.this.getInqueryStartDate()));
					arguments.add(new BasicNameValuePair("recvDtTo",
							IR_CI02_R00.this.getInqueryEndDate()));
					arguments.add(new BasicNameValuePair("carNo1",
							IR_CI02_R00.this.carNoList.get(0)));
					arguments.add(new BasicNameValuePair("carNo2",
							IR_CI02_R00.this.carNoList.get(1)));
					arguments.add(new BasicNameValuePair("carNo3",
							IR_CI02_R00.this.carNoList.get(2)));
					arguments.add(new BasicNameValuePair("carNo4",
							IR_CI02_R00.this.carNoList.get(3)));
					arguments.add(new BasicNameValuePair("carNo5",
							IR_CI02_R00.this.carNoList.get(4)));
					arguments.add(new BasicNameValuePair("carNo6",
							IR_CI02_R00.this.carNoList.get(5)));
					arguments.add(new BasicNameValuePair("carNo7",
							IR_CI02_R00.this.carNoList.get(6)));
					arguments.add(new BasicNameValuePair("carNo8",
							IR_CI02_R00.this.carNoList.get(7)));
					arguments.add(new BasicNameValuePair("carNo9",
							IR_CI02_R00.this.carNoList.get(8)));
					arguments.add(new BasicNameValuePair("carNo10",
							IR_CI02_R00.this.carNoList.get(9)));
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
						itemList01.add(new IR_CI02_R00_ITEM01(ejl01.getValue(i,
								"ST"), ejl01.getValue(i, "RECEV_DT"), ejl01
								.getValue(i, "ORDER_CD"), ejl01.getValue(i,
								"CAR_NO"), ejl01.getValue(i, "DONG_CAR_NO"),
								ejl01.getValue(i, "RECEV_NO"), ejl01.getValue(
								i, "EX_ORDER_CD"), ejl01.getValue(i,
								"STATUS_CD")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress(false);
				if(itemList01.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}
				changeCi02Inquery(selTp);
			}
		}
	}// end of SelectData inner-class

	public class selectTroubleDetail extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectTroubleDetail.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments
							.add(new BasicNameValuePair("recevNo",
									IR_CI02_R00.this.currentSelectedItem01
											.getRecevNo()));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ej01 = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
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
					item02 = new IR_CI02_R01P_ITEM01(ej01.getValue("CAR_NO"),
							ej01.getValue("MODEL_NM"),
							ej01.getValue("RECEV_DT"),
							ej01.getValue("ARRIVE_DT"),
							ej01.getValue("COMPLETE_DT"),
							ej01.getValue("RESCUE_DT"),
							ej01.getValue("CONTACT_CD"),
							ej01.getValue("RECEV_DESC"),
							ej01.getValue("EX_ORDER_CD"),
							ej01.getValue("STATUS_CD"),
							ej01.getValue("CBS_CD_1"), ej01.getValue("CBS_CD_2"),
							ej01.getValue("CBS_CD_3"),
							ej01.getValue("FAULT_CD"),
							ej01.getValue("PROC_CD"), ej01.getValue("DUTY_CD"),
							ej01.getValue("ORDER_DESC"),
							//고장처리자/연락처 추가 20181130 yowonsm
							ej01.getValue("CS_EMP_NM"),
							ej01.getValue("CS_EMP_MOBILE"),
							//고장접수자/연락처 추가 20190118 yowonsm
							ej01.getValue("NOTIFY"),
							ej01.getValue("NOTIFY_PHONE"));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				IR_CI02_R01P ir01 = new IR_CI02_R01P(context, item02,
						IR_CI02_R00.this.currentSelectedItem01.getRecevNo());
				ir01.show();
			}
		}
	}// end of SelectData inner-class



	public class searchElevatorAll extends AsyncTask<String, Integer, String> {

		JSONObject returnJson01;
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {

					GetHttp getHttp = new GetHttp();
					String param_url = WebServerInfo.getUrl()
							+ "cm/searchElevator.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("pBldgNo",
							IR_CI02_R00.this.item01.getBldgNo()));
					// arguments.add(new
					// BasicNameValuePair("pBldgNo","B07C0350"));
					returnJson01 = getHttp.getPost(param_url, arguments, true);
					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} catch (Exception ex) {
					// 로그인이 실패하였습니다 띄어주기
				}
				return params[0];
			}
			return "None";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				carNoList = new ArrayList<String>();
				try {
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {

						carNoList.add(ejl01.getValue(i, "CAR_NO"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progress(false);
				int i = IR_CI02_R00.this.carNoList.size();
				while (i < 10) {
					IR_CI02_R00.this.carNoList.add("");
					i++;
				}

			}
		}
	}


	private void ci02Inquery() {
		if (this.carNoList.size() == 0 || this.carNoList.get(0).equals("")) {
			AlertView.showAlert("호기를 조회 해주시길 바랍니다.", context);
		} else {
			progress(true);
			new selectTroubleHistory().execute("bagicWorkTime");
		}
	}
	private List<IR_CI02_R00_ITEM01> getCI02Item(int selTp){
		this.selTp = selTp;
		List<IR_CI02_R00_ITEM01> itemList = new ArrayList<IR_CI02_R00_ITEM01>();

		if (selTp == TAB_ALL) {
			itemList = itemList01;
		} else if (selTp == TAB_BREAKDOWN) {
			for (IR_CI02_R00_ITEM01 item : itemList01) {
				if (item.getSt().equals(String.valueOf(TAB_BREAKDOWN))) {
					itemList.add(item);
				}
			}
		} else if (selTp == TAB_SERVICE) {
			for (IR_CI02_R00_ITEM01 item : itemList01) {
				if (item.getSt().equals(String.valueOf(TAB_SERVICE))) {
					itemList.add(item);
				}
			}
		} else {
			itemList = itemList01;
		}

		return itemList;

	}
	private void changeCi02Inquery(int selTp) {
		this.selTp = selTp;
		List<IR_CI02_R00_ITEM01> itemList = new ArrayList<IR_CI02_R00_ITEM01>();

		if (selTp == TAB_ALL) {
			itemList = itemList01;
		} else if (selTp == TAB_BREAKDOWN) {
			for (IR_CI02_R00_ITEM01 item : itemList01) {
				if (item.getSt().equals(String.valueOf(TAB_BREAKDOWN))) {
					itemList.add(item);
				}
			}
		} else if (selTp == TAB_SERVICE) {
			for (IR_CI02_R00_ITEM01 item : itemList01) {
				if (item.getSt().equals(String.valueOf(TAB_SERVICE))) {
					itemList.add(item);
				}
			}
		} else {
			itemList = itemList01;
		}
		adapter01 = new IR_CI02_R00_Adapter01(context,
				R.layout.ir_ci02_r00_adapter01, itemList);
		IR_CI02_R00.this.lv_ci02_history.setAdapter(adapter01);
	}

	private void popupMenu(final View v, final int menu) {
		final PopupMenu pop = new PopupMenu(context, v);
		pop.getMenuInflater().inflate(menu, pop.getMenu());
		pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
					case R.id.menu_ci_all:
						tv_menutitle.setText("전체 조회");
						carNoList.clear();
						progress(true);
						new searchElevatorAll().execute("bagicWorkTime");
						break;
					case R.id.menu_ci_inqueryelev:
						tv_menutitle.setText("호기별 조회");
						final CM_SearchElev cm01 = new CM_SearchElev(context,
								item01.getBldgNo(), 3);
						cm01.show();
						cm01.elevSearch();
						cm01.setOnDismissListener(new OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								// TODO Auto-generated method stub
								IR_CI02_R00.this.carNoList = cm01
										.getReturnStrList();
								int i = IR_CI02_R00.this.carNoList.size();
								if(i== 0){
									tv_menutitle.setText("선 택");
									return;
								}
								while (i < 10) {
									IR_CI02_R00.this.carNoList.add("");
									i++;
								}
							}
						});
						break;
					default:
						break;

				}
				return false;
			}
		});
		pop.show();
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
					this.setInqueryStartDate(bundle.getString("dateSelected"));
					break;
				} else {
					break;
				}

			case 1:
				if (resultCode == -1) {
					Bundle bundle = data.getExtras();
					this.setInqueryEndDate(bundle.getString("dateSelected"));
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
			IR_CI02_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI02_R00.this.progress.dismiss();
		}
	}

	// / coom_dialog_inquery_startend_date 위젯

	public String getInqueryStartDate() {
		return this.et_inqueryStartDate.getText().toString();
	}
	public void setInqueryStartDate(String inqueryStartDate) {
		this.et_inqueryStartDate.setText(inqueryStartDate);
	}

	public String getInqueryEndDate() {
		return this.et_inqueryEndDate.getText().toString();
	}

	public void setInqueryEndDate(String inqueryEndDate) {
		this.et_inqueryEndDate.setText(inqueryEndDate);
	}

	// /////////////////////////
	private IR_CI02_R00_ITEM01 currentSelectedItem01;

	private ArrayList<String> carNoList;
	private int selTp;

}
