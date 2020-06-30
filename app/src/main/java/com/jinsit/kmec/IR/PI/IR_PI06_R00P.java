package com.jinsit.kmec.IR.PI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IR_PI06_R00P extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {
	public IR_PI06_R00P(Context c, String tp, String bldgNo, String refTp, String userId) {
		super(c);
		// TODO Auto-generated constructor stub
		//refTp = PartsCD이다.     --7.청구형태 에서 조회해온 값
		context = c;
		this.currentTp = tp;
		if(tp.equals("4")){
			this.bldgNo = bldgNo;
			this.refTp = refTp;
		}else if(tp.equals("7")){

		}else if(tp.equals("8")){
			this.refTp = refTp;
		}else if(tp.equals("9")){

		}
	}
	private android.app.ProgressDialog ProgressDialog;
	ArrayList<IR_PI06_R00_Item> iR_PI06_R00_Item;
	private EasyJsonList ejl;
	Button btn_cancel;
	TextView btn_popClose;

	ListView lv_search_dept;
	IR_PI06_R00P_Adapter iR_PI06_R00P_Adapter;
	Context context;
	String currentTp;
	String bldgNo;
	String refTp;
	String userId;

    IR_PI06_R00_Item currentItem;

    public IR_PI06_R00_Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(IR_PI06_R00_Item currentItem) {
        this.currentItem = currentItem;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_search_dept);
		TextView title = (TextView)findViewById(R.id.tv01_popTitle);
		title.setText("청구구분조회");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_search_dept = (ListView) findViewById(R.id.lv_search_dept);
		lv_search_dept.setOnItemClickListener(this);


		//deptSearch();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_popClose:
				dismiss();
				break;

		}

	}
	private void progress(Boolean isActivated){
		if(isActivated){
			this.ProgressDialog =
					android.app.ProgressDialog.show(getContext(), "알림","조회중 입니다.");
		}else{
			this.ProgressDialog.dismiss();
		}
	}
	public void requestPopupSearch() {
		// TODO Auto-generated method stub
		progress(true);
		new PartsRequestPopUpSearch().execute();
	}


	public class PartsRequestPopUpSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				iR_PI06_R00_Item = new ArrayList<IR_PI06_R00_Item>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ir/selectPartsRequestPopUp.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
				arguments.add(new BasicNameValuePair("refTp", refTp));
				arguments.add(new BasicNameValuePair("tp", currentTp));
				arguments.add(new BasicNameValuePair("userId", userId));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					iR_PI06_R00_Item.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						iR_PI06_R00_Item.add(new IR_PI06_R00_Item(
								ejl.getValue(i,"CD"),
								ejl.getValue(i, "NM"),
								ejl.getValue(i, "TP")));
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
			ProgressDialog.dismiss();
			iR_PI06_R00P_Adapter = new IR_PI06_R00P_Adapter(context, iR_PI06_R00_Item);
			lv_search_dept.setAdapter(iR_PI06_R00P_Adapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		IR_PI06_R00_Item resData = iR_PI06_R00_Item.get(position);
		setCurrentItem(resData);
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}
}