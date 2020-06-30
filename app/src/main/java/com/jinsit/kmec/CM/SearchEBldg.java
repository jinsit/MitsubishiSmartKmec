package com.jinsit.kmec.CM;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SearchEBldg extends Dialog implements OnClickListener,
		OnItemClickListener, OnDismissListener{

	public SearchEBldg(Context c) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
	}
	private ProgressDialog ProgressDialog;
	Button btn_searchBldg, btn_cancel;
	EditText et_searchBox;
	ListView lv_searchList;
	SearchBldgAdapter searchBldgListAdapter;
	ArrayList<SearchBldgResData> bldData ;
	private EasyJsonList ejl;
	Context context;
	public String bldgAddr = "";
	public String bldgNm;
	public String bldgNo;
	public String csDeptNm;

	public String getCsDeptNm() {
		return csDeptNm;
	}
	public void setCsDeptNm(String csDeptNm) {
		this.csDeptNm = csDeptNm;
	}
	public String getBldgAddr() {
		return bldgAddr;
	}
	public void setBldgAddr(String bldgAddr) {
		this.bldgAddr = bldgAddr;
	}
	public String getBldgNm() {
		return bldgNm;
	}
	public void setBldgNm(String bldgNm) {
		this.bldgNm = bldgNm;
	}
	public String getBldgNo() {
		return bldgNo;
	}
	public void setBldgNo(String bldgNo) {
		this.bldgNo = bldgNo;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
   /*     WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lpWindow);*/
		setContentView(R.layout.dialog_search_ebld);

		btn_searchBldg = (Button)findViewById(R.id.btn_searchBldg);
		btn_searchBldg.setOnClickListener(this);
		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		et_searchBox = (EditText)findViewById(R.id.et_searchBox);
		lv_searchList = (ListView)findViewById(R.id.lv_searchList);
		lv_searchList.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.btn_searchBldg:
				bldgSearch();
				break;
			case R.id.btn_cancel:
				dismiss();
				break;


		}

	}
	private void bldgSearch() {
		// TODO Auto-generated method stub
		if(et_searchBox.getText().toString().equals("")){
			Toast.makeText(context, "건물명을 입력하세요", Toast.LENGTH_SHORT).show();
		}else{
			new BuildingSearch().execute();
		}

	}

	public class BuildingSearch extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			SearchEBldg.this.ProgressDialog =
					android.app.ProgressDialog.show(context, "로그인","로그인 중 입니다...");
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try{
				bldData = new ArrayList<SearchBldgResData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"cm/searchBuildingName.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("pBldgNm", et_searchBox.getText().toString()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					bldData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						bldData.add(new SearchBldgResData(ejl.getValue(i,"BLDG_NO"),
								ejl.getValue(i,"BLDG_NM"),
								ejl.getValue(i,"ADDR"),
								ejl.getValue(i, "CS_DEPT_NM")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}catch(Exception ex){
				//로그인이 실패하였습니다 띄어주기
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			SearchEBldg.this.ProgressDialog.dismiss();
			searchBldgListAdapter = new SearchBldgAdapter(context, bldData);
			lv_searchList.setAdapter(searchBldgListAdapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		SearchBldgResData resData = bldData.get(position);
		bldgNm = resData.getBLDG_NM();
		bldgAddr = resData.getADDR();
		bldgNo = resData.getBLDG_NO();
		csDeptNm = resData.getCS_DEPT_NM();
		Log.e("hogi", resData.getBLDG_NM());
		dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		setBldgNm(bldgNm);
		setBldgAddr(bldgAddr);
		setBldgNo(bldgNo);
		setCsDeptNm(csDeptNm);

	}
}
