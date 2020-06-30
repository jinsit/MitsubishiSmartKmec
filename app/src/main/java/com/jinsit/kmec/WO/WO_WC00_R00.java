package com.jinsit.kmec.WO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SearchBldgInfo_ITEM01;
import com.jinsit.kmec.CM.CM_SearchBldg_ExList;
import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.CM.CM_SearchWorkCategory;
import com.jinsit.kmec.CM.CM_SearchWorkCategory_ITEM01;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.ApplicationInfo;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class WO_WC00_R00 extends Fragment implements OnClickListener {

	private Activity activity = null;
	private Context context;
	private TextView btn_wt_workCategory;
	private TextView btn_wt_searchSupportDept;
	private TextView btn_wt_searchBldg;
	private TextView btn_wt_searchClaimNo;
	private TextView btn_wt_searchElev;
	private TextView btn_wt_searchOrderNo;
	private TextView btn_wt_saveCreateWork;
	

	private LinearLayout lin_wt_bldgInfo;
	private TextView tv_wt_bldgNm;
	private TextView tv_wt_addr;
	private TextView tv_wt_csDeptNm;
	private TextView tv_wt_clientDept;
	private TextView tv_wt_clientTel;
	private TextView tv_wt_clientNm;
	private TextView tv_wt_clientHp;
	private TextView tv_wt_empNm1;
	private TextView tv_wt_emp1Hp;
	private TextView tv_wt_empNm2;
	private TextView tv_wt_emp2Hp;

	private LinearLayout lin_wt_carInfo;
	private TextView tv_wt_carNo;
	private TextView tv_wt_modelNm;

	private LinearLayout lin_wt_supportDept;
	private TextView tv_wt_deptNm;

	private LinearLayout lin_wt_orderNoInfo;
	private TextView tv_wt_refContrNo;
	private TextView tv_wt_rmk;
	
	private LinearLayout lin_wt_claimNoInfo;
	private TextView tv_wt_refContrNoByClaim;
	private TextView tv_wt_rmkByClaim;
	
	
	private CommonSession commonSession;

	private CM_SearchWorkCategory_ITEM01 item01;

	private String inspTime ="1";
	private EasyJsonMap ej01;
	
	private ProgressDialog progress;

	private WO_WO00_R00F wo1;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		wo1 = (WO_WO00_R00F)activity;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("workTarget: ", key);
			}
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wo_wc00_r00, container, false);
		onCreateViewInit(view);
		return view;
	}

	private void onCreateViewInit(View view) {
		// TODO Auto-generated method stub
		activityInit(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
			}
		}
		super.onActivityCreated(savedInstanceState);
	}

	private void activityInit(View view) {

		context = this.activity;
		commonSession = new CommonSession(context);
		// 타이틀 바
		android.app.ActionBar aBar = this.activity.getActionBar();
		aBar.setTitle("작업생성");
		aBar.setDisplayShowHomeEnabled(false);
		getInstances(view);
		initWorkInfo();
	}

	private void initWorkInfo() {
		item01 = null;

		this.btn_wt_searchSupportDept.setVisibility(View.GONE);
		this.btn_wt_searchBldg.setVisibility(View.GONE);
		this.btn_wt_searchClaimNo.setVisibility(View.GONE);
		this.btn_wt_searchElev.setVisibility(View.GONE);
		this.btn_wt_searchOrderNo.setVisibility(View.GONE);
		this.btn_wt_saveCreateWork.setVisibility(View.GONE);

		lin_wt_bldgInfo.setVisibility(View.GONE);
		tv_wt_bldgNm.setText("");
		tv_wt_addr.setText("");
		tv_wt_csDeptNm.setText("");
		tv_wt_clientDept.setText("");
		tv_wt_clientTel.setText("");
		tv_wt_clientNm.setText("");
		tv_wt_clientHp.setText("");
		tv_wt_empNm1.setText("");
		tv_wt_emp1Hp.setText("");
		tv_wt_empNm2.setText("");
		tv_wt_emp2Hp.setText("");
		this.currnetSelectedBldgInfo = null;

		lin_wt_carInfo.setVisibility(View.GONE);
		tv_wt_carNo.setText("");
		tv_wt_modelNm.setText("");
		this.carNo = "";

		lin_wt_supportDept.setVisibility(View.GONE);
		tv_wt_deptNm.setText("");

		lin_wt_orderNoInfo.setVisibility(View.GONE);
		tv_wt_refContrNo.setText("");
		tv_wt_rmk.setText("");
		
		lin_wt_claimNoInfo.setVisibility(View.GONE);
		tv_wt_refContrNoByClaim.setText("");
		tv_wt_rmkByClaim.setText("");
		
		this.currnetSelectedItem01 = null;
		this.currnetSelectedItem02 = null;
	}

	protected void getInstances(View view) {
		btn_wt_workCategory = (TextView) view
				.findViewById(R.id.btn_wt_workCategory);
		btn_wt_searchSupportDept = (TextView) view
				.findViewById(R.id.btn_wt_searchSupportDept);
		btn_wt_searchBldg = (TextView) view.findViewById(R.id.btn_wt_searchBldg);
		btn_wt_searchClaimNo = (TextView) view.findViewById(R.id.btn_wt_searchClaimNo);
		
		btn_wt_searchElev = (TextView) view.findViewById(R.id.btn_wt_searchElev);
		btn_wt_searchOrderNo = (TextView) view
				.findViewById(R.id.btn_wt_searchOrderNo);
		btn_wt_saveCreateWork = (TextView) view
				.findViewById(R.id.btn_wt_saveCreateWork);

		lin_wt_bldgInfo = (LinearLayout) view
				.findViewById(R.id.lin_wt_bldgInfo);
		tv_wt_bldgNm = (TextView) view.findViewById(R.id.tv_wt_bldgNm);
		tv_wt_addr = (TextView) view.findViewById(R.id.tv_wt_addr);
		tv_wt_csDeptNm = (TextView) view.findViewById(R.id.tv_wt_csDeptNm);
		tv_wt_clientDept = (TextView) view.findViewById(R.id.tv_wt_clientDept);
		tv_wt_clientTel = (TextView) view.findViewById(R.id.tv_wt_clientTel);
		tv_wt_clientNm = (TextView) view.findViewById(R.id.tv_wt_clientNm);
		tv_wt_clientHp = (TextView) view.findViewById(R.id.tv_wt_clientHp);
		tv_wt_empNm1 = (TextView) view.findViewById(R.id.tv_wt_empNm1);
		tv_wt_emp1Hp = (TextView) view.findViewById(R.id.tv_wt_emp1Hp);
		tv_wt_empNm2 = (TextView) view.findViewById(R.id.tv_wt_empNm2);
		tv_wt_emp2Hp = (TextView) view.findViewById(R.id.tv_wt_emp2Hp);

		
		
		lin_wt_claimNoInfo = (LinearLayout) view
				.findViewById(R.id.lin_wt_claimNoInfo);
		tv_wt_refContrNoByClaim = (TextView) view.findViewById(R.id.tv_wt_refContrNoByClaim);
		tv_wt_rmkByClaim = (TextView) view.findViewById(R.id.tv_wt_rmkByClaim);
		
		
		lin_wt_carInfo = (LinearLayout) view.findViewById(R.id.lin_wt_carInfo);
		tv_wt_carNo = (TextView) view.findViewById(R.id.tv_wt_carNo);
		tv_wt_modelNm = (TextView) view.findViewById(R.id.tv_wt_modelNm);

		lin_wt_supportDept = (LinearLayout) view
				.findViewById(R.id.lin_wt_supportDept);
		tv_wt_deptNm = (TextView) view.findViewById(R.id.tv_wt_deptNm);

		lin_wt_orderNoInfo = (LinearLayout) view
				.findViewById(R.id.lin_wt_orderNoInfo);
		tv_wt_refContrNo = (TextView) view.findViewById(R.id.tv_wt_refContrNo);
		tv_wt_rmk = (TextView) view.findViewById(R.id.tv_wt_rmk);
		
		setEvents();
	}

	protected void setEvents() {
		btn_wt_workCategory.setOnClickListener(this);
		this.btn_wt_searchBldg.setOnClickListener(this);
		this.btn_wt_searchClaimNo.setOnClickListener(this);
		this.btn_wt_searchElev.setOnClickListener(this);
		this.btn_wt_searchOrderNo.setOnClickListener(this);
		this.btn_wt_searchSupportDept.setOnClickListener(this);
		this.btn_wt_saveCreateWork.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_wt_workCategory:
			final CM_SearchWorkCategory cm01 = new CM_SearchWorkCategory(
					activity);
			cm01.show(); 
			cm01.searchWorkCategory();
			cm01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub

					CM_SearchWorkCategory_ITEM01 selectedItem = cm01
							.getSelectedItem();
					if (selectedItem != null) {
						//릴리즈버전이면서 정기점검일 경우
						if(ApplicationInfo.isDebugMode() == false && selectedItem.getWorkCd().equals("CA01"))	//정기점검일 경우
						{
							if(commonSession.getMngUsrId().isEmpty() == true)	//승강원 ID가 없는 경우
							{
								AlertView.showAlert("승강기정보센터 ID가 없는 점검자 입니다. 정기점검 작업을 생성할 수 없습니다.", context);
								return;
							}
						}
						initWorkInfo();
						setSelectedWorkCategoryItem(selectedItem);
					}

				}

			});
			break;
		case R.id.btn_wt_searchSupportDept:
			final WO_WC00_R01P wt01 = new WO_WC00_R01P(activity);
			wt01.show();
			wt01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_WC00_R01P_ITEM01 item = wt01.getSelectedItem();
					if (item != null) {
						setSupportDept(item);
					}
				}

			});
			break;
		case R.id.btn_wt_searchBldg:
			final CM_SearchBldg_ExList cmB01 = new CM_SearchBldg_ExList(
					activity);
			cmB01.show();
			cmB01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					CM_SearchBldgInfo_ITEM01 item = cmB01
							.getCurrentSelectedItem();
					if (item != null) {
						setBldgInfo(item);
					}
				}

			});
			break;
			
		case R.id.btn_wt_searchClaimNo:
			if (this.currnetSelectedBldgInfo == null) {
				AlertView.showAlert(  "건물명을 먼저 조회 해주세요",context);
				return;
			}
			String refContrCdByClaim = "13"; //클레임

			final WO_WC00_R02P wo1ByClaim = new WO_WC00_R02P(activity,
					currnetSelectedBldgInfo.getBldgNo(), refContrCdByClaim);
			wo1ByClaim.show();
			wo1ByClaim.searchOrderNo();
			wo1ByClaim.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_WC00_R02P_ITEM01 item = wo1ByClaim.getSelectedItem();
					if (item != null) {
						setClaimNo(item);
					}
				}
			});
			
			break;
			
		case R.id.btn_wt_searchElev:
			if (this.currnetSelectedBldgInfo == null) {
				Toast.makeText(activity, "건물명을 먼저 조회 해주세요", 2000);
				return;
			}
			final CM_SearchElev cmE01 = new CM_SearchElev(activity,
					this.currnetSelectedBldgInfo.getBldgNo());
			cmE01.show();
			cmE01.elevSearch();
			cmE01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					String returnValue = cmE01.getReturnStr();
					if (returnValue != null && returnValue != "") {
						setElevInfo(cmE01.getReturnStr(),
								cmE01.getReturnModelNm());
					}
				}
			});
			break;
		case R.id.btn_wt_searchOrderNo:
			if (this.currnetSelectedBldgInfo == null) {
				AlertView.showAlert(  "건물명을 먼저 조회 해주세요",context);
				return;
			}
			String refContrCd = "";
			if (this.item01.getWorkCd().equals("CA07")) {     //21.수리공사
				refContrCd = "11";
			} else if (this.item01.getWorkCd().equals("CA11")) {   //33.타부서지원  >>  변경 31. 클레임
				refContrCd = "13";
			} else if (this.item01.getWorkCd().equals("CA05")) {   //18.고장대기
				refContrCd = "15";
			} else if (this.item01.getWorkCd().equals("CA23")) {  //22.유상부품교체
				refContrCd = "17";
			} else if (this.item01.getWorkCd().equals("CA25")) { //23.무상부품교체
				refContrCd = "18";
			}

			final WO_WC00_R02P wo1 = new WO_WC00_R02P(activity,
					currnetSelectedBldgInfo.getBldgNo(), refContrCd);
			wo1.show();
			wo1.searchOrderNo();
			wo1.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					WO_WC00_R02P_ITEM01 item = wo1.getSelectedItem();
					if (item != null) {
						setOrderNo(item);
					}
				}
			});
			break;
		case R.id.btn_wt_saveCreateWork:
			if(this.currnetSelectedItem01 == null){
				if(btn_wt_searchSupportDept.getVisibility() == View.VISIBLE){
					AlertView.showAlert(  "지원부서를 먼저 조회 해주세요",context);
					return;
				}
			}
			if (this.currnetSelectedBldgInfo == null) {
				if(btn_wt_searchBldg.getVisibility() == View.VISIBLE){
					if(!this.item01.getWorkCd().equals("CC01")){    /////CC01 07 작업준비는 건물명을 입력하지 않아도 됩니다.
					AlertView.showAlert(  "건물명을 먼저 조회 해주세요",context);
					return;
					}
				}
			}
			if(carNo.equals("")||carNo.equals(null)){
				if(btn_wt_searchElev.getVisibility() == View.VISIBLE){
					AlertView.showAlert(  "호기를 먼저 조회 해주세요",context);
					return;
				}
			}
			if(currnetSelectedItem02 == null) {
				if(btn_wt_searchOrderNo.getVisibility() == View.VISIBLE){
					AlertView.showAlert(  "주문번호를 먼저 조회 해주세요",context);
					return;
				}
				if(btn_wt_searchClaimNo.getVisibility() == View.VISIBLE){
					AlertView.showAlert("클레임번호를 먼저 조회 해주세요.", context);
					return;
				}
			}
			progress(true);
			new saveCreateWork().execute();
			break;
	
			
		default:
			break;
		}

	}
	
	public class saveCreateWork extends AsyncTask<Void, Void, Boolean> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		
		private EasyJsonMap dataMap;
		private EasyJsonMap msgMap;
		
		private String exceptionMsg = null;
		
		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				String param_url_01 = WebServerInfo.getUrl()
						+ "ip/saveCreateWork.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
					arguments.add(new BasicNameValuePair("workCd", item01.getWorkCd()));
					arguments.add(new BasicNameValuePair("jobNoG", "0"));
				if (currnetSelectedBldgInfo == null) {
					arguments.add(new BasicNameValuePair("bldgNo", ""));
				} else {
					arguments.add(new BasicNameValuePair("bldgNo",currnetSelectedBldgInfo.getBldgNo()));
				}
					arguments.add(new BasicNameValuePair("carNo", carNo));
				if (currnetSelectedItem02 == null) {
					arguments.add(new BasicNameValuePair("refContrNo", ""));
				} else {
					arguments.add(new BasicNameValuePair("refContrNo",currnetSelectedItem02.getRefContrNo()));
				}
				if (currnetSelectedItem01 == null) {
					arguments.add(new BasicNameValuePair("supportCd", ""));
				} else {
					arguments.add(new BasicNameValuePair("supportCd",currnetSelectedItem01.getDeptCd()));
				}
					arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("inspTimeBc", inspTime));

				returnJson01 = http.getPost(param_url_01, arguments, true);
				try {
					msgMap = new EasyJsonMap(returnJson01.getJSONObject("msgMap"));
					dataMap = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));
				
				} catch (JSONException e) {
					e.printStackTrace();
					exceptionMsg = e.toString();
					return false;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				exceptionMsg = e.toString();
				
				return false;
			}
			return true;
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			progress(false);
			if(result){
				try {
					boolean isError = msgMap.getValue("errCd").equals("0") ? true:false;//0이면 정상
					if(isError){
						String returnValue = "N";
						String returnMsg = "";
						try {
							returnValue = dataMap.getValue("RTN");
							returnMsg = dataMap.getValue("RTN_MESS");
						} catch (JSONException e) {
							e.printStackTrace();
						}

					
						if (returnValue.equals("1")) {
							AlertView.alert(context, "", "작업생성 성공 했습니다.", new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									wo1.setBtnTabWT();
								}
							});
						
						} else {
							AlertView.alert(context, "", "작업생성 실패 했습니다. "+ returnMsg, new android.content.DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									wo1.setBtnTabWT();
								}
							});
						}
						
					}else{
						
						AlertView.alert(context, "", msgMap.getValue("errMsg"), new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								wo1.setBtnTabWT();
							}
						});
						
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else{
				if(exceptionMsg != null){
					String errMsg = "";
					try {
						if(msgMap != null)errMsg = msgMap.getValue("errMsg");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					AlertView.alert(context, "", exceptionMsg + errMsg, new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wo1.setBtnTabWT();
						}
					});
				
				}
			}

		}
	}// end of SelectData inner-class
	
	

/*	public class saveCreateWork extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ip/saveCreateWork.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession
							.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
					arguments.add(new BasicNameValuePair("workCd", item01
							.getWorkCd()));
					arguments.add(new BasicNameValuePair("jobNoG", "0"));
					if(currnetSelectedBldgInfo == null){
						arguments.add(new BasicNameValuePair("bldgNo",""));
					}
					else
					{
						arguments.add(new BasicNameValuePair("bldgNo",
								currnetSelectedBldgInfo.getBldgNo()));
					}
					arguments.add(new BasicNameValuePair("carNo", carNo));
					if (currnetSelectedItem02 == null) {
						arguments.add(new BasicNameValuePair("refContrNo", ""));
					} else {
						arguments.add(new BasicNameValuePair("refContrNo",
								currnetSelectedItem02.getRefContrNo()));
					}
					if (currnetSelectedItem01 == null) {
						arguments.add(new BasicNameValuePair("supportCd", ""));
					} else {
						arguments.add(new BasicNameValuePair("supportCd",
								currnetSelectedItem01.getDeptCd()));
					}

					arguments.add(new BasicNameValuePair("usrId", commonSession
							.getEmpId()));
					arguments.add(new BasicNameValuePair("inspTimeBc", inspTime));
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
				Map map = null;
				String returnValue = "N";
				String returnMsg = "";
				try {
					returnValue = ej01.getValue("RTN");
					returnMsg = ej01.getValue("RTN_MESS");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if (returnValue.equals("1")) {
					AlertView.alert(context, "", "작업생성 성공 했습니다.", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wo1.setBtnTabWT();
						}
					});
				
				} else {
					AlertView.alert(context, "", "작업생성 실패 했습니다. "+ returnMsg, new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							wo1.setBtnTabWT();
						}
					});
				}
			}
		}
	}// end of SelectData inner-class
*/
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WO_WC00_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WO_WC00_R00.this.progress.dismiss();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	}

	public void setSelectedWorkCategoryItem(
			CM_SearchWorkCategory_ITEM01 selectedItem) {
		this.item01 = selectedItem;
		String btnStr = this.getString(R.string.btnStr_workCategory);
		for (int i = btnStr.length(); i < 30; i++) {
			btnStr = btnStr + " ";
		}
		this.btn_wt_workCategory.setText(btnStr + item01.getWorkNm());

		if (item01.isSupportDept()) {
			this.btn_wt_searchSupportDept.setVisibility(View.VISIBLE);
		} else {
			this.btn_wt_searchSupportDept.setVisibility(View.GONE);
			lin_wt_supportDept.setVisibility(View.GONE);
		}

		if (item01.isSearchBldg()) {
			this.btn_wt_searchBldg.setVisibility(View.VISIBLE);
		} else {
			this.btn_wt_searchBldg.setVisibility(View.GONE);
			lin_wt_bldgInfo.setVisibility(View.GONE);
		}

		if (item01.isSearchElev()) {
			this.btn_wt_searchElev.setVisibility(View.VISIBLE);
		} else {
			this.btn_wt_searchElev.setVisibility(View.GONE);
			lin_wt_carInfo.setVisibility(View.GONE);
		}

		if (item01.isSearchOrderNo()) {
			this.btn_wt_searchOrderNo.setVisibility(View.VISIBLE);
		} else {
			this.btn_wt_searchOrderNo.setVisibility(View.GONE);
		}
		//yowonsm 클레임번호 추가
		if(item01.isClaimNo()){
			this.btn_wt_searchClaimNo.setVisibility(View.VISIBLE);
			this.btn_wt_searchOrderNo.setVisibility(View.GONE);
		}else{
			this.btn_wt_searchClaimNo.setVisibility(View.GONE);
		}
		
		if (item01.isCreateWork()) {
			this.btn_wt_saveCreateWork.setVisibility(View.VISIBLE);
		} else {
			this.btn_wt_saveCreateWork.setVisibility(View.GONE);
		}

	}

	private CM_SearchBldgInfo_ITEM01 currnetSelectedBldgInfo;

	private void setBldgInfo(CM_SearchBldgInfo_ITEM01 item) {
		this.currnetSelectedBldgInfo = item;
		this.lin_wt_bldgInfo.setVisibility(View.VISIBLE);
		this.tv_wt_bldgNm.setText(this.currnetSelectedBldgInfo.getBldgNm());
		this.tv_wt_addr.setText(this.currnetSelectedBldgInfo.getAddr());
		this.tv_wt_csDeptNm.setText(this.currnetSelectedBldgInfo.getCsDetpNm());
		this.tv_wt_clientDept.setText(this.currnetSelectedBldgInfo
				.getClientDept());
		this.tv_wt_clientTel.setText(this.currnetSelectedBldgInfo
				.getClientTel());
		this.tv_wt_clientNm.setText(this.currnetSelectedBldgInfo.getClientNm());
		this.tv_wt_clientHp.setText(this.currnetSelectedBldgInfo.getClientHp());
		this.tv_wt_empNm1.setText(this.currnetSelectedBldgInfo.getEmpNm1());
		this.tv_wt_emp1Hp.setText(this.currnetSelectedBldgInfo.getEmp1hP());
		this.tv_wt_empNm2.setText(this.currnetSelectedBldgInfo.getEmpNm2());
		this.tv_wt_emp2Hp.setText(this.currnetSelectedBldgInfo.getEmp2hP());
		setElevInfo("","");

	}

	private WO_WC00_R01P_ITEM01 currnetSelectedItem01;
	private WO_WC00_R02P_ITEM01 currnetSelectedItem02;

	private void setSupportDept(WO_WC00_R01P_ITEM01 item) {

		this.currnetSelectedItem01 = item;
		lin_wt_supportDept.setVisibility(View.VISIBLE);
		this.tv_wt_deptNm.setText(item.getDeptNm());
	}

	private void setOrderNo(WO_WC00_R02P_ITEM01 item) {

		this.currnetSelectedItem02 = item;
		lin_wt_orderNoInfo.setVisibility(View.VISIBLE);
		this.tv_wt_refContrNo.setText(item.getRefContrNo());
		this.tv_wt_rmk.setText(item.getRmk());
	}
	
	private void setClaimNo(WO_WC00_R02P_ITEM01 item){
		this.currnetSelectedItem02 = item;
		lin_wt_claimNoInfo.setVisibility(View.VISIBLE);
		this.tv_wt_refContrNoByClaim.setText(item.getRefContrNo());
		this.tv_wt_rmkByClaim.setText(item.getRmk());
	}

	private String carNo;
	private String modelNm;

	private void setElevInfo(String carNo, String modelNm) {
		this.carNo = carNo;
		this.modelNm = modelNm;
		if(this.carNo.equals("") || this.carNo == null){
			this.lin_wt_carInfo.setVisibility(View.GONE);
		}else{
	    	this.lin_wt_carInfo.setVisibility(View.VISIBLE);
		}
		this.tv_wt_carNo.setText(this.carNo);
		this.tv_wt_modelNm.setText(this.modelNm);
	}

}
