package com.jinsit.kmec.IR.CI;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.PI.IR_PI04_R00_Item;
import com.jinsit.kmec.IR.PI.IR_PI04_R01P;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_CI04_R01P extends AlertDialog implements
		android.view.View.OnClickListener ,OnItemClickListener{

	private Context context;
	// /title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	// /////////

	private ListView lv_ci_constructionDetail;

	private List<IR_CI04_R01P_ITEM01> itemList01;
	private IR_CI04_R01P_Adapter01 adapter01;
	private ProgressDialog progress;
	protected IR_CI04_R01P(Context context, List<IR_CI04_R01P_ITEM01> itemList) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemList01 = itemList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci04_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("공사상세내역");
		// //////////////////
		adapter01 = new IR_CI04_R01P_Adapter01(context,
				R.layout.ir_ci04_r01p_adapter02, this.itemList01);
		lv_ci_constructionDetail.setAdapter(adapter01);
	}

	protected void getInstances() {
		// /title 위젯
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		// //////////////////
		lv_ci_constructionDetail = (ListView) findViewById(R.id.lv_ci_constructionDetail);
		lv_ci_constructionDetail.setOnItemClickListener(this);

		setEvents();
	}

	protected void setEvents() {
		// /title 위젯
		this.btn_popClose.setOnClickListener(this);
		// //////////////////
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			// /title 위젯
			case R.id.btn_popClose:
				this.dismiss();
				break;
			// //////////////////
			default:
				break;
		}
	}



	private EasyJsonList ejl01;
	private List<IR_PI04_R00_Item> itemList;
	public class selectSellingPrice extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectSellingPrice.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selTp","1"));//itemNo로 조회
					arguments.add(new BasicNameValuePair("selText",itemNo));
					arguments.add(new BasicNameValuePair("drawNo",""));

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
				itemList = new ArrayList<IR_PI04_R00_Item>();
				try {
					itemList.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList.add(new IR_PI04_R00_Item(
								ejl01.getValue(i,"ITEM_NO"),
								ejl01.getValue(i, "ITEM_NM"),
								ejl01.getValue(i, "SIZE"),
								ejl01.getValue(i, "APPLY_DT"),
								ejl01.getValue(i, "LIFE_CYCLE"),
								ejl01.getValue(i, "WORKER"),
								ejl01.getValue(i, "WORK_TM"),
								ejl01.getValue(i, "UNIT_PRC"),
								ejl01.getValue(i, "BUY_PRC"),
								ejl01.getValue(i, "LT"),
								ejl01.getValue(i, "RMK"),
								ejl01.getValue(i, "ISRT_USR_ID"),
								ejl01.getValue(i, "UPDT_USR_ID"),
								ejl01.getValue(i, "IMG_CHK")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				if(itemList.size() == 0){
					AlertView.showAlert("조회된 데이타가 없습니다", context);
				}else{

					if(itemList.get(0).getImgChk().equals("1")){

						new selectItemImage().execute("bagicWorkTime");
					}else{
						IR_CI04_R02P ir01 = new IR_CI04_R02P(context,itemList.get(0),null);
						ir01.show();
					}

				}



			}
		}
	}// end of SelectData inner-class

	public class selectItemImage extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectItemImage.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("itemNo",itemNo));
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
				String img = "";
				try {
					img = ejl01.getValue(0, "IMG1");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				try {
					Bitmap bm = DataConvertor.Base64Bitmap(img);
					IR_CI04_R02P ir01 = new IR_CI04_R02P(context,itemList.get(0),bm);
					ir01.show();
				}
				catch(Exception ex){
					Toast.makeText(context, "이미지 불러오기를 실패했습니다.", Toast.LENGTH_SHORT)
							.show();
				}
			}
		}
	}// end of SelectData inner-class

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_CI04_R01P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_CI04_R01P.this.progress.dismiss();
		}
	}
	private String itemNo;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		itemNo = itemList01.get(position).getItemNo();
		progress(true);
		new selectSellingPrice().execute("bagicWorkTime");

	}

}
