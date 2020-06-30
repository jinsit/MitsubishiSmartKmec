package com.jinsit.kmec.DM.DM;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SDateTime;
import com.jinsit.kmec.CM.CM_SearchBldg;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class DM_DM01_R01P extends Dialog implements OnClickListener,
		OnItemSelectedListener {

	protected DM_DM01_R01P(Context context, String csEmpId) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.csEmpId = csEmpId;
	}

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	private DM_DM01_R00_ITEM01 item01;
	private TextView btn_dm_save;
	private TextView btn_dm_bldgNm;
	private TextView tv_dm_bldgNm;

	private TextView btn_dm_carNo;
	private TextView tv_dm_carNo;

	private Spinner sp_dm_otWorkCd;
	private EditText et_dm_Rmk;

	private TextView tv_dm_otTmFr;
	private TextView tv_dm_otTmTo;

	private ProgressDialog progress;
	private CommonSession commonSession;

	private EasyJsonList ejl01;

	private List<DM_DM01_R01_ITEM01> itemList01;
	private DM_DM01_R01_Adapter01 adapter01;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dm_dm01_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();

		// /title 위젯
		tv01_popTitle.setText("특별O/T 작성");
		// //////////////////


		commonSession = new CommonSession(context);
		itemList01 = new ArrayList<DM_DM01_R01_ITEM01>();
		currentSelectedItem = null;

		String value = DateUtil.nowDateTime();
		otStartHour = DateUtil.getHour(value);
		otStartMinute = DateUtil.getMinute(value);
		otEndHour = DateUtil.getHour(value);
		otEndMinute = DateUtil.getMinute(value);

		//초기에 입력된 시간 설정 안함
		//	String starTime = String.format("%02d-%02d-%02d %02d:%02d", onNowYear, onNowMonth, onNowDay, otStartHour,otStartMinute);
		//	String endTime = String.format("%02d-%02d-%02d %02d:%02d", onNowYear, onNowMonth, onNowDay, otEndHour,otEndMinute);

		//	tv_dm_otTmFr.setText(starTime);
		//	tv_dm_otTmTo.setText(endTime);
	}

	protected void getInstances() {
		// /title 위젯
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		// //////////////////
		btn_dm_save = (TextView) findViewById(R.id.btn_dm_save);
		btn_dm_bldgNm = (TextView) findViewById(R.id.btn_dm_bldgNm);
		tv_dm_bldgNm = (TextView) findViewById(R.id.tv_dm_bldgNm);

		sp_dm_otWorkCd = (Spinner) findViewById(R.id.sp_dm_otWorkCd);
		et_dm_Rmk = (EditText) findViewById(R.id.et_dm_Rmk);
		btn_dm_carNo = (TextView) findViewById(R.id.btn_dm_carNo);
		tv_dm_carNo = (TextView) findViewById(R.id.tv_dm_carNo);

		tv_dm_otTmFr = (TextView) findViewById(R.id.tv_dm_otTmFr);
		tv_dm_otTmTo = (TextView) findViewById(R.id.tv_dm_otTmTo);

		setEvents();
	}

	protected void setEvents() {
		// /title 위젯
		this.btn_popClose.setOnClickListener(this);
		// //////////////////

		btn_dm_save.setOnClickListener(this);
		btn_dm_bldgNm.setOnClickListener(this);
		btn_dm_carNo.setOnClickListener(this);
		tv_dm_otTmFr.setOnClickListener(this);
		tv_dm_otTmTo.setOnClickListener(this);
		sp_dm_otWorkCd.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				this.dismiss();
				break;
			case R.id.btn_dm_save:
				if (this.currentSelectedItem == null) {
					AlertView.showAlert("작업구분을 선택하지 않았습니다.", context);
					return;
				}
				if (this.bldgNo == "" || this.bldgNo == null) {
					AlertView.showAlert("건물을 선택하지 않았습니다.", context);
					return;
				}
				if (this.carNo == "" || this.carNo == null) {
					AlertView.showAlert("호기를 선택하지 않았습니다.", context);
					return;
				}
				if (this.tv_dm_otTmFr.getText().toString().length() == 0
						|| this.tv_dm_otTmTo.getText().toString().length() == 0) {
					AlertView.showAlert("OT시간이 선택되지 않았습니다.", context);
					return;
				}
				progress(true);
				new insertOverTimeSchedule().execute("bagicWorkTime");
				break;
			case R.id.btn_dm_bldgNm:
				searchBldg();
				break;
			case R.id.tv_dm_otTmFr:
				btnClickId = R.id.tv_dm_otTmFr;
				final CM_SDateTime otDateTimeDlg = new CM_SDateTime(context,"OT 시작 시간 설정", otStartHour,otStartMinute);
				otDateTimeDlg.show();
				otDateTimeDlg.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						String dateTime = otDateTimeDlg.getDateTimeString();
						if (dateTime == "" || dateTime == null) {
						} else {
							tv_dm_otTmFr.setText(dateTime);
							otStartHour = otDateTimeDlg.getHour();
							otStartMinute = otDateTimeDlg.getMinute();
						}
					}

				});

				break;
			case R.id.tv_dm_otTmTo:
				btnClickId = R.id.tv_dm_otTmTo;
				final CM_SDateTime otEndDateTimeDlg = new CM_SDateTime(context,"OT 끝나는 시간 설정",  otEndHour,otEndMinute);
				otEndDateTimeDlg.show();
				otEndDateTimeDlg.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						String dateTime = otEndDateTimeDlg.getDateTimeString();
						if (dateTime == "" || dateTime == null) {
						} else {
							tv_dm_otTmTo.setText(dateTime);
							otEndHour = otEndDateTimeDlg.getHour();
							otEndMinute = otEndDateTimeDlg.getMinute();
						}
					}

				});
				break;
			case R.id.btn_dm_carNo:
				searchElev();
				break;
			default:
				break;
		}

	}

	public class selectWorkType extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/selectWorkType.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
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
					itemList01.add(new DM_DM01_R01_ITEM01("", "작업구분선택"));
					int jsonSize01 = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize01; i++) {
						itemList01.add(new DM_DM01_R01_ITEM01(ejl01.getValue(i,
								"WORK_CD"), ejl01.getValue(i, "WORK_NM")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter01 = new DM_DM01_R01_Adapter01(context,
						R.layout.dm_dm01_r01_adapter01, itemList01);
				DM_DM01_R01P.this.sp_dm_otWorkCd.setAdapter(adapter01);
			}

		}
	}// end of SelectData inner-class


	public void searchWorkType(){
		progress(true);
		new selectWorkType().execute("bagicWorkTime");
	}
	public class insertOverTimeSchedule extends
			AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/insertOverTimeSchedule.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("stTp", "I")); //I: 입력, U : 수정, D 삭제
					arguments.add(new BasicNameValuePair("csEmpId", csEmpId));
					arguments.add(new BasicNameValuePair("otWorkDt", DateUtil.nowDate()));
					arguments.add(new BasicNameValuePair("otNo", "0"));
					arguments.add(new BasicNameValuePair("otWorkCd",
							currentSelectedItem.getWorkCd()));
					arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
					arguments.add(new BasicNameValuePair("carNo", carNo));
					arguments.add(new BasicNameValuePair("otTmFr", tv_dm_otTmFr
							.getText().toString()));
					arguments.add(new BasicNameValuePair("otTmTo", tv_dm_otTmTo
							.getText().toString()));
					arguments.add(new BasicNameValuePair("otRmk", et_dm_Rmk
							.getText().toString()));
					arguments.add(new BasicNameValuePair("usrId",
							DM_DM01_R01P.this.commonSession.getEmpId()));
					returnJson01 = http.getPost(param_url_01, arguments, true);
				} catch (Exception ex) {
					ex.printStackTrace();
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

			String resultFg = "0";
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				try {
					if(returnJson01 != null){
						resultFg = returnJson01.getString("dataString");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if (resultFg.equals("1")) {
					AlertView.showAlert("OT 등록 성공 했습니다.", context, new android.content.DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							// TODO Auto-generated method stub
							DM_DM01_R01P.this.dismiss();
						}
					});
				} else {
					AlertView.showAlert("OT 등록 실패 했습니다.", context);
				}

			}

		}
	}// end of SelectData inner-class

	private void searchBldg() {
		final CM_SearchBldg sBldg = new CM_SearchBldg(context);
		sBldg.show();
		sBldg.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub

				String no = sBldg.getBldgNo();
				if (no.equals("") || no == null) {
				} else {
					DM_DM01_R01P.this.setBldgNo(sBldg.getBldgNo());
					DM_DM01_R01P.this.setBldgName(sBldg.getBldgNm());
				}
			}

		});

	}

	private void searchElev() {
		final CM_SearchElev elev = new CM_SearchElev(context, bldgNo);
		if (this.bldgNo == "" || this.bldgNo == null) {
			AlertView.showAlert("건물명을 먼저 조회 하세요", context);
			return;
		}
		elev.show();
		elev.elevSearch();
		elev.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				String no = elev.getReturnStr();
				if(no.equals("") || no == null){

				}else{
					DM_DM01_R01P.this.setCarNo(elev.getReturnStr());
				}
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
							   long id) {
		// TODO Auto-generated method stub
		if (position < 1) {
			this.currentSelectedItem = null;
		} else {
			this.currentSelectedItem = itemList01.get(position);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		this.currentSelectedItem = null;
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			DM_DM01_R01P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "OT 등록 중입니다.");
		} else {
			DM_DM01_R01P.this.progress.dismiss();
		}
	}

	private DM_DM01_R01_ITEM01 currentSelectedItem;
	private String bldgNo;
	private String bldgName;
	private String carNo;

	private int otStartHour;
	private int otStartMinute;
	private int otEndHour;
	private int otEndMinute;
	private int btnClickId;
	private String csEmpId;

	public String getBldgNo() {
		return bldgNo;
	}

	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
	}

	public String getBldgName() {
		return bldgName;
	}

	public void setBldgName(String bldgName) {
		if (bldgName.equals("") || bldgName == null) {
		} else {
			this.bldgName = bldgName;
			DM_DM01_R01P.this.tv_dm_bldgNm.setText(bldgName);
			setCarNo("");
		}
	}

	public String getCarNo() {
		return carNo;

	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
		if (carNo == "" || carNo == null) {
			DM_DM01_R01P.this.tv_dm_carNo.setText(DM_DM01_R01P.this.context
					.getString(R.string.search_carno));
		} else {
			tv_dm_carNo.setText(carNo);

		}
	}
}