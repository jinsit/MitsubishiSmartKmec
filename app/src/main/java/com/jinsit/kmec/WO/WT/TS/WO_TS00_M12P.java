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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_ReadPicture;
import com.jinsit.kmec.CM.CM_SaveReadPicture_ITEM01;
import com.jinsit.kmec.CM.CM_Sign;
import com.jinsit.kmec.IR.PI.IR_PI04_R01P;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 고장수리
 */
public class WO_TS00_M12P extends Dialog implements OnClickListener, OnDismissListener{

	public WO_TS00_M12P(Context context, WO_TS00_R00_ITEM03 workTargetData, WO_TS00_R00_ITEM04 item04) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.workTargetData = workTargetData;
		this.item04 = item04;
	}

	private Context context;
	
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	private ImageView img_fr_sign;
	private TextView btn_fr_approve;
	
	private TextView btn_fr_A1;
	private TextView btn_fr_A2;
	private TextView btn_fr_A3;
	private TextView btn_fr_A4;
	private TextView btn_fr_A5;
	

	private TextView btn_fr_B1;
	private TextView btn_fr_B2;
	private TextView btn_fr_B3;
	private TextView btn_fr_B4;
	private TextView btn_fr_B5;

	private ProgressDialog progress;
	private String custSatisfactionA;
	private String custSatisfactionB;
	private WO_TS00_M12P_ITEM01 item01;
	private WO_TS00_R00_ITEM03 workTargetData;
	private WO_TS00_R00_ITEM04 item04;
	
	private static int A1 = 5;
	private static int A2 = 4;
	private static int A3=3;
	private static int A4=2;
	private static int A5=1;
	
	private static int B1=5;
	private static int B2=4;
	private static int B3=3;
	private static int B4=2;
	private static int B5=1;
	
	private CommonSession commonSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wo_ts00_m12p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("고객 평가/승인");	

		commonSession = new CommonSession(context);
		resultOk = false;
		if( item04.getCustSign() != null && !item04.getCustSign().equals("")){

			sign = null;
			signString ="";
			progress(true);
			new selectItemImage().execute("bagicWorkTime");
			custSatisfactionA = this.item04.getAsCd1();
			custSatisfactionB = this.item04.getAsCd2();
			if(custSatisfactionA.equals("05")){
				btnSelect(5);
			}else if(custSatisfactionA.equals("04")){
				btnSelect(4);
			}else if(custSatisfactionA.equals("03")){
				btnSelect(3);
			}else if(custSatisfactionA.equals("02")){
				btnSelect(2);
			}else if(custSatisfactionA.equals("01")){
				btnSelect(1);
			}
			
			if(custSatisfactionB.equals("05")){
				btnSelectB(5);
			}else if(custSatisfactionB.equals("04")){
				btnSelectB(4);
			}else if(custSatisfactionB.equals("03")){
				btnSelectB(3);
			}else if(custSatisfactionB.equals("02")){
				btnSelectB(2);
			}else if(custSatisfactionB.equals("01")){
				btnSelectB(1);
			}
			
			this.btn_fr_approve.setVisibility(View.GONE);
			
		}
		else{
			custSatisfactionA = "00";
			custSatisfactionB = "00";
			sign = null;
			signString ="";
			btn_fr_A1.setBackgroundResource(R.drawable.tab_contentment01_off);
			btn_fr_A2.setBackgroundResource(R.drawable.tab_contentment02_off);
			btn_fr_A3.setBackgroundResource(R.drawable.tab_contentment03_off);
			btn_fr_A4.setBackgroundResource(R.drawable.tab_contentment04_off);
			btn_fr_A5.setBackgroundResource(R.drawable.tab_contentment05_off);
			btn_fr_B1.setBackgroundResource(R.drawable.tab_contentment01_off);
			btn_fr_B2.setBackgroundResource(R.drawable.tab_contentment02_off);
			btn_fr_B3.setBackgroundResource(R.drawable.tab_contentment03_off);
			btn_fr_B4.setBackgroundResource(R.drawable.tab_contentment04_off);
			btn_fr_B5.setBackgroundResource(R.drawable.tab_contentment05_off);
			
		}
	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		img_fr_sign = (ImageView)findViewById(R.id.img_fr_sign);
		btn_fr_approve = (TextView)findViewById(R.id.btn_fr_approve);
		
		btn_fr_A1 = (TextView)findViewById(R.id.btn_fr_A1);
		btn_fr_A2 = (TextView)findViewById(R.id.btn_fr_A2);
		btn_fr_A3 = (TextView)findViewById(R.id.btn_fr_A3);
		btn_fr_A4 = (TextView)findViewById(R.id.btn_fr_A4);
		btn_fr_A5 = (TextView)findViewById(R.id.btn_fr_A5);
		
		btn_fr_B1 = (TextView)findViewById(R.id.btn_fr_B1);
		btn_fr_B2 = (TextView)findViewById(R.id.btn_fr_B2);
		btn_fr_B3 = (TextView)findViewById(R.id.btn_fr_B3);
		btn_fr_B4 = (TextView)findViewById(R.id.btn_fr_B4);
		btn_fr_B5 = (TextView)findViewById(R.id.btn_fr_B5);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		img_fr_sign.setOnClickListener(this);
		btn_fr_approve.setOnClickListener(this);
		btn_fr_A1.setOnClickListener(this);
		btn_fr_A2.setOnClickListener(this);
		btn_fr_A3.setOnClickListener(this);
		btn_fr_A4.setOnClickListener(this);
		btn_fr_A5.setOnClickListener(this);
		
		btn_fr_B1.setOnClickListener(this);
		btn_fr_B2.setOnClickListener(this);
		btn_fr_B3.setOnClickListener(this);
		btn_fr_B4.setOnClickListener(this);
		btn_fr_B5.setOnClickListener(this);
	}


	private void btnSelect(int idx) {

		btn_fr_A1.setBackgroundResource(R.drawable.tab_contentment01_off);
		btn_fr_A2.setBackgroundResource(R.drawable.tab_contentment02_off);
		btn_fr_A3.setBackgroundResource(R.drawable.tab_contentment03_off);
		btn_fr_A4.setBackgroundResource(R.drawable.tab_contentment04_off);
		btn_fr_A5.setBackgroundResource(R.drawable.tab_contentment05_off);

		btn_fr_A1.setEnabled(true);
		btn_fr_A2.setEnabled(true);
		btn_fr_A3.setEnabled(true);
		btn_fr_A4.setEnabled(true);
		btn_fr_A5.setEnabled(true);
		switch (idx) {
		case 5:
			btn_fr_A1.setBackgroundResource(R.drawable.tab_contentment01_on);
			btn_fr_A1.setEnabled(false);
			custSatisfactionA = "05";
			break;
		case 4:
			btn_fr_A2.setBackgroundResource(R.drawable.tab_contentment02_on);
			btn_fr_A2.setEnabled(false);
			custSatisfactionA = "04";
			break;
		case 3:
			btn_fr_A3.setBackgroundResource(R.drawable.tab_contentment03_on);
			btn_fr_A3.setEnabled(false);	
			custSatisfactionA = "03";
			break;
		case 2:
			btn_fr_A4.setBackgroundResource(R.drawable.tab_contentment04_on);
			btn_fr_A4.setEnabled(false);
			custSatisfactionA = "02";
			break;
		case 1:
			btn_fr_A5.setBackgroundResource(R.drawable.tab_contentment05_on);
			btn_fr_A5.setEnabled(false);
			custSatisfactionA = "01";
			break;
		}
	}
	
	private void btnSelectB(int idx) {

		btn_fr_B1.setBackgroundResource(R.drawable.tab_contentment01_off);
		btn_fr_B2.setBackgroundResource(R.drawable.tab_contentment02_off);
		btn_fr_B3.setBackgroundResource(R.drawable.tab_contentment03_off);
		btn_fr_B4.setBackgroundResource(R.drawable.tab_contentment04_off);
		btn_fr_B5.setBackgroundResource(R.drawable.tab_contentment05_off);

		btn_fr_B1.setEnabled(true);
		btn_fr_B2.setEnabled(true);
		btn_fr_B3.setEnabled(true);
		btn_fr_B4.setEnabled(true);
		btn_fr_B5.setEnabled(true);
		switch (idx) {
		case 5:
			btn_fr_B1.setBackgroundResource(R.drawable.tab_contentment01_on);
			btn_fr_B1.setEnabled(false);
			custSatisfactionB = "05";
			break;
		case 4:
			btn_fr_B2.setBackgroundResource(R.drawable.tab_contentment02_on);
			btn_fr_B2.setEnabled(false);
			custSatisfactionB = "04";
			break;
		case 3:
			btn_fr_B3.setBackgroundResource(R.drawable.tab_contentment03_on);
			btn_fr_B3.setEnabled(false);	
			custSatisfactionB = "03";
			break;
		case 2:
			btn_fr_B4.setBackgroundResource(R.drawable.tab_contentment04_on);
			btn_fr_B4.setEnabled(false);
			custSatisfactionB = "02";
			break;
		case 1:
			btn_fr_B5.setBackgroundResource(R.drawable.tab_contentment05_on);
			btn_fr_B5.setEnabled(false);
			custSatisfactionB = "01";
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_popClose:
			dismiss();
			break;
		case R.id.img_fr_sign:

			if( item04.getCustSign() != null && !item04.getCustSign().equals("")){
				return;
			}
			final CM_Sign cm01 = new CM_Sign(context);
			cm01.show();
			cm01.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					signString = cm01.getSignString();
					if(signString.equals("") ){
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
		case R.id.btn_fr_A1:
			btnSelect(A1);
			break;
		case R.id.btn_fr_A2:
			btnSelect(A2);
			break;
		case R.id.btn_fr_A3:
			btnSelect(A3);
			break;
		case R.id.btn_fr_A4:
			btnSelect(A4);
			break;
		case R.id.btn_fr_A5:
			btnSelect(A5);
			break;
		case R.id.btn_fr_B1:
			btnSelectB(B1);
			break;
		case R.id.btn_fr_B2:
			btnSelectB(B2);
			break;
		case R.id.btn_fr_B3:
			btnSelectB(B3);
			break;
		case R.id.btn_fr_B4:
			btnSelectB(B4);
			break;
		case R.id.btn_fr_B5:
			btnSelectB(B5);
			break;
		case R.id.btn_fr_approve:
			if(custSatisfactionA.equals("00")){
				AlertView.showAlert("고객 평가를 선택해주세요", context);
				return;
			}else if (custSatisfactionB.equals("00")){
				AlertView.showAlert("고객 평가를 선택해주세요", context);
				return;
			}else if(signString.equals("") ){
				AlertView.showAlert("서명을 넣어주세요", context);
				return;
			}
			item01 = new WO_TS00_M12P_ITEM01(commonSession.getEmpId(), workTargetData.getWorkDt(),workTargetData.getJobNO(),custSatisfactionA,custSatisfactionB,"","", getFileName(),commonSession.getEmpId(),signString);
			final WO_TS00_R13P wo13 = new WO_TS00_R13P(context ,  item01, workTargetData.getBldgNo());
			wo13.show();
			wo13.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					resultOk = wo13.getResult();
					if(wo13.getResult()){
						dismiss();
					}else{
						
					}
				}

			});
			break;
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
					sign = EasyDownLoad.getImage(AbsoluteFilePath.FTP_FOLDER_WOTS_PATH ,  item04.getCustSign(), commonSession.getEmpId());
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
	
	

	
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	
	private Bitmap sign;
	private String signString;
	
	private String getFileName() {
		String str = "";
		DateUtil du = new DateUtil();
		String ymd = du.getCurrentShortDate();
		str = workTargetData.getRefContrNo() + "_" + ymd + "_S" + ".jpg";

		return str;

	}
	private void progress(Boolean isActivated) {
		if (isActivated) {
			WO_TS00_M12P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			WO_TS00_M12P.this.progress.dismiss();
		}
	}

	private boolean resultOk;
	public boolean getResult(){
		return resultOk;
	}
}
