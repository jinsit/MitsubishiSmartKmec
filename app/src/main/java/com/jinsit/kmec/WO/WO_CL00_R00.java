package com.jinsit.kmec.WO;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.WO.CL.MW.CL_MW00_R00;
import com.jinsit.kmec.WO.CL.MW.CL_MW00_R01;
import com.jinsit.kmec.WO.CL.MW.CL_MW00_R02;
import com.jinsit.kmec.WO.CL.RI.CL_RI00_R01;
import com.jinsit.kmec.WO.CL.RJ.CL_RJ00_R00;
import com.jinsit.kmec.WO.CL.TS.CL_TS00_R00;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class WO_CL00_R00 extends Fragment implements OnItemClickListener,OnClickListener {

	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;
	private ArrayList<WO_WT00_R00_ITEM00> workListData;
	private WO_WT00_R00_Adapter00 workListAdapter;
	private WO_WT00_R00_Adapter01 wO_WT00_R01F_Adapter01;
	private EasyJsonList ejl;
	CommonSession commonSession;
	private boolean isLocalDb = false;
	private boolean isSynchronized = false;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		this.commonSession = new CommonSession(activity);
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
		
		isLocalDb = NetworkStates.isNetworkStatus(activity);
		if(isLocalDb){
			new WorkTargetAsync().execute();
		
		}else{
			Toast.makeText(activity, "인터넷 연결이 안되어있습니다.", 2000).show();
			
			String query = new DatabaseRawQuery().selectCompleteJobList(commonSession.getEmpId(),commonSession.getWorkDt());
	
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor mCursor = db.rawQuery(query,
						null); // 쿼리 날리고 
			mCursor.moveToFirst();
			wO_WT00_R01F_Adapter01 =
				new WO_WT00_R00_Adapter01(activity,
						R.layout.listitem_job, mCursor, new String[] {
								"WORK_NM", "ST", "BLDG_NM" },
						null, 0);
			lv_workTarget.setAdapter(wO_WT00_R01F_Adapter01);
			db.close();
			dbHelper.close();		
	
	}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		if(isLocalDb){

			String workCd = workListData.get(position).getWORK_CD();
			
			if(workListData.get(position).getWORK_CD().equals(WorkCD.정기점검)){
				if(masterDataCheck(workListData.get(position).getJOB_NO(),workListData.get(position).getWORK_DT())){
					isSynchronized = true; ///정기점검으로 이동한 후 서버와 동기화를 하기위해 트루로 한다.
					Intent intent = new Intent(activity,CL_RI00_R01.class);
					intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
					intent.putExtra("workDt", workListData.get(position).getWORK_DT());
					activity.startActivity(intent);		
				}else{
					SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(activity,
							"정기점검 데이터를 다운로드 하지 않았습니다. 다운로드 화면으로 이동하겠습니까?",
							new btnClickListener() {

								@Override
								public void onButtonClick() {
									// TODO Auto-generated method stub
									Intent intent = new Intent(activity, MasterDataDownload.class);
									startActivity(intent);
								}
							});
					ynDialog.show();
				}
				
			}else if(workListData.get(position).getWORK_CD().equals(WorkCD.고장수리)){
				Intent intent = new Intent(activity,CL_TS00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt", workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	

			}else if(workListData.get(position).getWORK_CD().equals(WorkCD.수리공사)
					|workCd.equals(WorkCD.유상부품교체)
					|workCd.equals(WorkCD.무상부품교체)
					|workCd.equals(WorkCD.Claim)
					|workCd.equals(WorkCD.특수작업)){
				Intent intent = new Intent(activity,CL_RJ00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt", workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			}else if(workCd.equals(WorkCD.상주근무)
					|workCd.equals(WorkCD.저녁식사)
					|workCd.equals(WorkCD.저녁간식)
					|workCd.equals(WorkCD.점심식사)
					|workCd.equals(WorkCD.철야식사)
					|workCd.equals(WorkCD.고장대기)
					|workCd.equals(WorkCD.작업준비)
					|workCd.equals(WorkCD.자택복귀)
					|workCd.equals(WorkCD.회의교육)
					|workCd.equals(WorkCD.사무정리)
					|workCd.equals(WorkCD.사무실복귀)){
				Intent intent = new Intent(activity,CL_MW00_R01.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt", workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			}
			else if(workCd.equals(WorkCD.정기점검지원)
					|workCd.equals(WorkCD.고장수리지원)
					|workCd.equals(WorkCD.점검대기)
					|workCd.equals(WorkCD.신설대기)
					|workCd.equals(WorkCD.호텔VIP대기)
					|workCd.equals(WorkCD.고객상담)
					|workCd.equals(WorkCD.고객서비스)
					|workCd.equals(WorkCD.타부서지원)
					|workCd.equals(WorkCD.특별점검)
					|workCd.equals(WorkCD.보수이체검사)){
				Intent intent = new Intent(activity,CL_MW00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt", workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			
			}else if(workCd.equals(WorkCD.정기검사입회)){
				Intent intent = new Intent(activity,CL_MW00_R02.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			}
		
			
			
			
			
		}else{
			Cursor cursor = wO_WT00_R01F_Adapter01.getCursor();
			Intent intent = new Intent(activity, CL_RI00_R01.class);
			intent.putExtra("jobNo",
					cursor.getString(cursor.getColumnIndex("JOB_NO")));
			intent.putExtra("workDt",
					cursor.getString(cursor.getColumnIndex("WORK_DT")));
			startActivity(intent);
		}
		
	}
	
	private class WorkTargetAsync extends AsyncTask<Void, String, Void> {
  
		@Override
		protected void onPostExecute(Void result) {
			  WO_CL00_R00.this.ProgressDialog.dismiss();
			  workListAdapter = new WO_WT00_R00_Adapter00(activity,workListData);
			  lv_workTarget.setAdapter(workListAdapter);
			  
		}		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    WO_CL00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
		    		activity,"작업대상","작업대상목록 불러오는중");	
		}
		@Override
		protected Void doInBackground(Void... params) {

			try {
				workListData = new ArrayList<WO_WT00_R00_ITEM00>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/selectWorkTarget.do";
				
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", "3"));
				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
				
				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					workListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						workListData.add(new WO_WT00_R00_ITEM00(ejl.getValue(i,"CS_EMP_ID"),
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
	
	private boolean masterDataCheck(String jobNo, String workDt){
		boolean isCount = true;
		
			int count = 0;
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT count() from WORK_TBL where job_no ='" + jobNo + "' and"
					+ " work_dt = '" + workDt  +"'", null);
			c.moveToFirst();
			String getC = c.getString(0);
			c.close();
			db.close();
			dbHelper.close();
			count = Integer.valueOf(getC);
			if(count==0){
				isCount = false;
			}
		return isCount;
	}
	
	private boolean masterDataCheck(String jobNo){
		boolean isCount = true;
		
			int count = 0;
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT count() from WORK_TBL where job_no ='" + jobNo + "'", null);
			c.moveToFirst();
			String getC = c.getString(0);
			c.close();
			db.close();
			dbHelper.close();
			count = Integer.valueOf(getC);
			if(count==0){
				isCount = false;
			}
		return isCount;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		}
		
	}
	

}
