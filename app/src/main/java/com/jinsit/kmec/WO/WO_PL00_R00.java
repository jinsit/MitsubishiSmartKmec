package com.jinsit.kmec.WO;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;



public class WO_PL00_R00 extends Fragment implements OnItemClickListener,OnClickListener {

	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;
	private ArrayList<WO_WT00_R00_ITEM00> pendListData;
	private WO_WT00_R00_Adapter00 workListAdapter;
	private EasyJsonList ejl;
	private String jobNoYet, empId, workDt, workDtYet;
	CommonSession commonSession;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
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

		View view = inflater.inflate(R.layout.wo_wt00_r01f, null);
		onCreateViewInit(view);
		return view;

	}

	private void onCreateViewInit(View view) {
		// TODO Auto-generated method stub
	
	
		lv_workTarget = (ListView) view.findViewById(R.id.lv_workTarget);
		lv_workTarget.setOnItemClickListener(this);
		commonSession = new CommonSession(activity);
		initialize();
		
	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("DogPlus list savedInstanceState key: ", key);
			}
		}
		super.onActivityCreated(savedInstanceState);
		System.out.println("SaleListFragmentActivity.onActivityCrated");

	}

	private void initialize() {
		// TODO Auto-generated method stub
		new PendListAsync().execute();
				
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		//미처리된 작업 다시시작
		//jobNoYet, empId, workDt, workDtYet;
		jobNoYet = pendListData.get(position).getJOB_NO();
		workDtYet = pendListData.get(position).getWORK_DT();
		restartWork();
		
	}
	
	private void restartWork() {
		// TODO Auto-generated method stub
		
		SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(activity,
				"작업을 재시작 하시겠습니까?", new btnClickListener() {
					@Override
					public void onButtonClick() {
						// TODO Auto-generated method stub

						new WorkRestartAsync().execute();
					}
				});
		ynDialog.show();
		
	}


	private class PendListAsync extends AsyncTask<Void, String, Void> {
  
		@Override
		protected void onPostExecute(Void result) {
			  WO_PL00_R00.this.ProgressDialog.dismiss();
			  workListAdapter = new WO_WT00_R00_Adapter00(activity,pendListData);
			  lv_workTarget.setAdapter(workListAdapter);
			  
		}		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    WO_PL00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
		    		activity,"미처리목록","미처리목록을 불러오는 중입니다.");	
		}
		@Override
		protected Void doInBackground(Void... params) {

			try {
				pendListData = new ArrayList<WO_WT00_R00_ITEM00>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/selectWorkTarget.do";
				
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", "2"));
				arguments.add(new BasicNameValuePair("csEmpId",commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
				
				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					pendListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						pendListData.add(new WO_WT00_R00_ITEM00(ejl.getValue(i,"CS_EMP_ID"),
								ejl.getValue(i, "WORK_DT"),
								ejl.getValue(i, "JOB_NO"),
								ejl.getValue(i,	"WORK_CD"),
								ejl.getValue(i,"WORK_NM")
								, ejl.getValue(i, "ST")
								,ejl.getValue(i, "CS_DT")
								, ejl.getValue(i, "CS_FR")
								, ejl.getValue(i, "BLDG_NM")
								, ejl.getValue(i, "CAR_NO"),
								ejl.getValue(i, "RESERV_ST"),
								ejl.getValue(i, "Y_CNT"),
								ejl.getValue(i, "T_CNT")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			
			return null;
		}
		
		
		
	}
	
	private class WorkRestartAsync extends AsyncTask<Void, String, Void> {
  
		@Override
		protected void onPostExecute(Void result) {
			  WO_PL00_R00.this.ProgressDialog.dismiss();
			  new PendListAsync().execute();
			  
		}		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    WO_PL00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
		    		activity,"작업대상","작업대상목록 불러오는중");	
		}
		@Override
		protected Void doInBackground(Void... params) {

			try {
				pendListData = new ArrayList<WO_WT00_R00_ITEM00>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/updateWorkRestart.do";
				
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
				arguments.add(new BasicNameValuePair("workDtYet", workDtYet));
				arguments.add(new BasicNameValuePair("jobNoYet", jobNoYet));
				arguments.add(new BasicNameValuePair("usrId", commonSession.getEmpId()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
				System.out.println(returnJson + "");
			/*	
				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					workListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						workListData.add(new WorkListData(ejl.getValue(i,"CS_EMP_ID"),
								ejl.getValue(i, "WORK_DT"),
								ejl.getValue(i, "JOB_NO"),
								ejl.getValue(i,"WORK_NM")
								, ejl.getValue(i, "ST")
								,ejl.getValue(i, "CS_DT")
								, ejl.getValue(i, "CS_FR")
								, ejl.getValue(i, "BLDG_NM")
								, ejl.getValue(i, "CAR_NO"),
								ejl.getValue(i, "RESERV_ST")));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}*/
				
				
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			
			return null;
		}
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		}
		
	}
	

}
