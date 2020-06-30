package com.jinsit.kmec.WO;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.ComparisonChain;
import com.jinsit.kmec.CM.CM_SelectInspector;
import com.jinsit.kmec.CM.SelectPendCode;
import com.jinsit.kmec.GK.LO.GK_LO00_R00;
import com.jinsit.kmec.HM.MP.AttendanceTagActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_CheckAttendance;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R00;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R01;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R02;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R03;
import com.jinsit.kmec.WO.WT.MW.WT_MW00_R04;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog;
import com.jinsit.kmec.WO.WT.RI.SimpleYesNoDialog.btnClickListener;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R01;
import com.jinsit.kmec.WO.WT.RJ.WT_RJ00_R00;
import com.jinsit.kmec.WO.WT.TS.WO_TS00_R00;
import com.jinsit.kmec.comm.CallService;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.PreferenceUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;

import static android.app.Activity.RESULT_OK;
import static com.jinsit.kmec.Constants.WORKOUT_TIME;

public class WO_WT00_R00 extends Fragment implements OnItemClickListener,OnClickListener, android.widget.AdapterView.OnItemLongClickListener {

	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;
	private ArrayList<WO_WT00_R00_ITEM00> workListData;
	private WO_WT00_R00_Adapter00 workListAdapter;
	private WO_WT00_R00_Adapter01 wO_WT00_R01F_Adapter01;
	private EasyJsonList ejl;
	private boolean isLocalDb = false;
	
	private String upQ030,upQ210,upQ213;
	
	private boolean isSynchronized = false;
	private String updateWorkDt, updateJobNo;
	
	CommonSession cs;
	private String jobNo;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
//			GPSService gps = new GPSService(activity);
//			boolean isgps = gps.isGetLocation();
//			if(isgps){
//				Location loc=null;
//				
//				loc = gps.getLocation();
//				double lat = loc.getLatitude();
//				double lng = loc.getLongitude();
//				Toast.makeText(activity,"gpsTrue  = " + "lat  = " +lat +"lng" + lng, 10000).show();
//				Log.e("LatLng", "LatLng = "  + lat  +   "   lng = " + lng);
//			}else{
//				Toast.makeText(activity,"gpsFalse", 2000).show();
//			}
		
	
	}

	private void setActionbarSort(String sort){
		JActionbar jActionbar = new JActionbar();
		jActionbar.setActionBarSort(activity, activity.getActionBar(), sort, new JActionbar.OnOrderByClickListener() {
			@Override
			public void onOrderByClick(boolean isAsc) {
				setSortJobList(getCurrentSortType());
			}
		});

	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		activity.getMenuInflater().inflate(R.menu.menu_ip_wo_sort, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_ip_wo_sort_job) {
			setJobSort(SORT_TYPE.JOB);
			setSortJobList(SORT_TYPE.JOB);
			return true;
		} else if (id == R.id.menu_ip_wo_sort_car) {
			setJobSort(SORT_TYPE.CAR);
			setSortJobList(SORT_TYPE.CAR);
			return true;
		} else if (id == R.id.menu_ip_wo_sort_bldg) {
			setJobSort(SORT_TYPE.BLDG);
			setSortJobList(SORT_TYPE.BLDG);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setJobSort(SORT_TYPE sort) {
		PreferenceUtil.instance(activity).putJobSort(sort.toString());
	}

	private String getJobSort() {
		return PreferenceUtil.instance(activity).getJobSort();
	}
	private SORT_TYPE getCurrentSortType(){
		String sort = getJobSort();
		SORT_TYPE sortType = SORT_TYPE.JOB;
		if(sort == null || sort.equals("")){
			sortType = SORT_TYPE.JOB;
		}else{
			if(SORT_TYPE.BLDG.toString().equals(sort)){
				sortType = SORT_TYPE.BLDG;
			}else if(SORT_TYPE.CAR.toString().equals(sort)){
				sortType = SORT_TYPE.CAR;
			}else if(SORT_TYPE.JOB.toString().equals(sort)){
				sortType = SORT_TYPE.JOB;
			}
		}
		return sortType;
	}

	private enum SORT_TYPE {
		JOB,
		CAR,
		BLDG
	}
	private void setSortJobList(final SORT_TYPE type){

		String sortName;
		Comparator<WO_WT00_R00_ITEM00> noDesc = new Comparator<WO_WT00_R00_ITEM00>() {
			@Override
			public int compare(WO_WT00_R00_ITEM00 item1, WO_WT00_R00_ITEM00 item2) {
				if(type == SORT_TYPE.CAR){
					return item2.getCAR_NO().compareTo(item1.getCAR_NO());
				}else if(type == SORT_TYPE.BLDG){
					return item2.getBLDG_NM().compareTo(item1.getBLDG_NM());
				}else{
					/*int result = item2.getWORK_CD().compareTo(item1.getWORK_CD());
					if(result != 0){
						return result;
					}*/
					return item2.getJOB_NO().compareTo(item1.getJOB_NO());
				}
			}
		};

		Collections.sort(workListData, noDesc);
		if(PreferenceUtil.instance(activity).getOrderByASC()){
            Collections.reverse(workListData);
        }
        if(type == SORT_TYPE.JOB){
            Comparator<WO_WT00_R00_ITEM00> workcd = new Comparator<WO_WT00_R00_ITEM00>() {
                @Override
                public int compare(WO_WT00_R00_ITEM00 item1, WO_WT00_R00_ITEM00 item2) {
                    return item1.getWORK_CD().compareTo(item2.getWORK_CD());
                }
            };
            Collections.sort(workListData, workcd);
        }
		//workListAdapter.notifyDataSetChanged();   //왜 안먹는거니?
		workListAdapter = new WO_WT00_R00_Adapter00(activity,workListData);
		lv_workTarget.setAdapter(workListAdapter);
		if(type == SORT_TYPE.CAR){
			sortName = "호기별";
		}else if(type == SORT_TYPE.BLDG){
			sortName = "빌딩별";
		}else{
			sortName = "작업별";
		}
		setActionbarSort("정렬 :" + sortName);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("workTarget: ", key);
			}
		}
		setHasOptionsMenu(true);
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
		lv_workTarget.setOnItemLongClickListener(this);
		cs = new CommonSession(activity);
		Log.e("commSe" , "Commss" + cs.getEmpId());
		//initialize(); onResume에서 이니셜라이즈를 한다.
		
	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
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
			
			String query = new DatabaseRawQuery().selectJobList(cs.getEmpId(),cs.getWorkDt());
	
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
		
//			  workListAdapter = new WO_WT00_R01F_Adapter00(activity,workListData);
//			  lv_workTarget.setAdapter(workListAdapter);
		}
		
				
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		if(isLocalDb){

			String workCd = workListData.get(position).getWORK_CD();
			
			if(workListData.get(position).getWORK_CD().equals(WorkCD.정기점검)){
				if(masterDataCheck(workListData.get(position).getCS_EMP_ID(),workListData.get(position).getJOB_NO(),workListData.get(position).getWORK_DT())){
					isSynchronized = true; ///정기점검으로 이동한 후 서버와 동기화를 하기위해 트루로 한다.
					Intent intent = new Intent(activity,WO_WT00_R01.class);
					intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
					intent.putExtra("workDt",workListData.get(position).getWORK_DT());
					
					updateWorkDt=workListData.get(position).getWORK_DT();
					updateJobNo=workListData.get(position).getJOB_NO();
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
				Intent intent = new Intent(activity,WO_TS00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	

			}else if(workListData.get(position).getWORK_CD().equals(WorkCD.수리공사)
					|workCd.equals(WorkCD.유상부품교체)
					|workCd.equals(WorkCD.무상부품교체)
					|workCd.equals(WorkCD.Claim)
					|workCd.equals(WorkCD.특수작업)){
				Intent intent = new Intent(activity,WT_RJ00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
				
			}else if(workCd.equals(WorkCD.상주근무)
					|workCd.equals(WorkCD.저녁식사)
					|workCd.equals(WorkCD.저녁간식)
					|workCd.equals(WorkCD.점심식사)
					|workCd.equals(WorkCD.철야식사)
					|workCd.equals(WorkCD.고장대기)
					|workCd.equals(WorkCD.작업준비)
					|workCd.equals(WorkCD.자택복귀)){
				Intent intent = new Intent(activity,WT_MW00_R01.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
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
				Intent intent = new Intent(activity,WT_MW00_R00.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
				
			}else if(workCd.equals(WorkCD.정기검사입회)){
				Intent intent = new Intent(activity,WT_MW00_R02.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
				
			}else if(workCd.equals(WorkCD.회의교육)
					|workCd.equals(WorkCD.사무정리)){
				Intent intent = new Intent(activity,WT_MW00_R03.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			}else if(workCd.equals(WorkCD.사무실복귀)){
				Intent intent = new Intent(activity, WT_MW00_R04.class);
				intent.putExtra("jobNo", workListData.get(position).getJOB_NO());
				intent.putExtra("workDt",workListData.get(position).getWORK_DT());
				activity.startActivity(intent);	
			}
			
		
			
			
			
			//여기는 로컬 디비
		}else{
			Cursor cursor = wO_WT00_R01F_Adapter01.getCursor();
			Intent intent = new Intent(activity, WO_WT00_R01.class);
			intent.putExtra("jobNo",
					cursor.getString(cursor.getColumnIndex("JOB_NO")));
			intent.putExtra("workDt",
					cursor.getString(cursor.getColumnIndex("WORK_DT")));
			startActivity(intent);
		}
		
		
	}
	
	private boolean masterDataCheck(String csEmpId, String jobNo, String workDt){
		boolean isCount = true;
		
			int count = 0;
			KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
					DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
			SQLiteDatabase db = dbHelper.getWritableDatabase();

			Cursor c = db.rawQuery("SELECT count() from WORK_TBL where CS_EMP_ID = '" + csEmpId + "' and job_no ='" + jobNo + "' and"
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


	private class WorkTargetAsync extends AsyncTask<Void, String, Void> {
		
		@Override
		protected void onPostExecute(Void result) {
			  WO_WT00_R00.this.ProgressDialog.dismiss();

					setSortJobList(getCurrentSortType());
					//workListAdapter = new WO_WT00_R00_Adapter00(activity,workListData);
				  	//lv_workTarget.setAdapter(workListAdapter);
				  try {
					  /**
					   * isAttended가 올바르지 않아서 아직 안됨
					   * preference Attended 처리함
					   */
					  if (lv_workTarget.getAdapter().getCount() < 1 && cs.isAttended() && isResultCancel == false) {

						  long workOutTime = DateUtil.getSecond(WORKOUT_TIME);
						  long time = DateUtil.getSecond(DateUtil.nowDateFormat("HH:mm"));
						  isResultCancel = false;
						  if (workOutTime < time) {
								  SimpleYesNoDialog simpleYesNoDialog = new SimpleYesNoDialog(activity,
										  "작업이 없습니다. 퇴근하시겠습니까?", new btnClickListener() {
									  @Override
									  public void onButtonClick() {
										  // TODO Auto-generated method stub
										  Intent intent = new Intent(activity, AttendanceTagActivity.class);
										  intent.putExtra("attendance", "퇴근");
										  startActivityForResult(intent, 3);
									  }
								  });
							  if (!simpleYesNoDialog.isShowing()) {
								  simpleYesNoDialog.setCancelable(false);
								  simpleYesNoDialog.show();
							  }
						  }

					  }
				  } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
			  
		}		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    WO_WT00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
		    		activity,"작업대상","작업대상목록 불러오는중");	
		}
		@Override
		protected Void doInBackground(Void... params) {

			try {
				workListData = new ArrayList<WO_WT00_R00_ITEM00>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()+"ip/selectWorkTarget.do";
				
			
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("selTp", "1"));
				arguments.add(new BasicNameValuePair("csEmpId",cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
				

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		}
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("synchroniszied" , "Sync?? = " + isSynchronized);
		if(isSynchronized){
			//upLoadDatabase();
			initialize();
		}else{
			initialize();
		}
		Log.w("synchroniszied" , "Sync?? = " + isSynchronized);
	}
	private void upLoadDatabase() {
		// TODO Auto-generated method stub
		isSynchronized = false;
		 StringBuffer sb = updateWorkTable();
		 upQ030= sb.toString();
		 StringBuffer sb2 = update210Table();
		 upQ210= sb2.toString();
		 StringBuffer sb3 = update213Table();
		 upQ213 = sb3.toString();
			
		new UploadDatabaseAsync().execute();
	}

	StringBuffer updateWorkTable(){
	
		StringBuffer inputParams = new StringBuffer();
		String query = "SELECT A.CS_EMP_ID, A.WORK_DT, A.JOB_NO, "
				+ " A.CS_FR, '', A.JOB_ST, '', A.MOVE_TM, A.ARRIVE_TM, A.COMPLETE_TM, A.CS_EMP_ID, "
				+ " B.CS_TP, B.SELCHK_USID, B.SELCHK_USID_NM, B.SUB_SELCHK_USID, B.SUB_SELCHK_USID_NM "
				+ " FROM WORK_TBL A "
				+ " LEFT JOIN TCSQ030 B ON A.CS_EMP_ID = B.CS_EMP_ID AND A.WORK_DT = B.WORK_DT AND A.JOB_NO = B.JOB_NO "
				+ " WHERE A.WORK_DT = '" + updateWorkDt + "' AND A.JOB_NO = '" +updateJobNo +  "'";
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고 
		mCursor.moveToFirst();
		if(mCursor !=null&&mCursor.getCount()!=0){
		
			do{
				for(int i=0;i<mCursor.getColumnCount();i++){
					
					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
		}while(mCursor.moveToNext());
	}
		mCursor.close();
		db.close();
		dbHelper.close();
		return inputParams;
	
		
	}
	
	
	StringBuffer update210Table(){
		//value = mCursor.getString(mCursor.getColumnIndex("CS_EMP_ID"));
		 
		
		StringBuffer inputParams = new StringBuffer();
		  
		String query = "SELECT CS_EMP_ID, WORK_DT, JOB_NO,NFC_PLC,CS_TM_FR, CS_TM_TO,JOB_ST,CS_EMP_ID  FROM TCSQ210 "
				+ " WHERE WORK_DT = '" + updateWorkDt + "' AND JOB_NO = '" +updateJobNo +  "'";
			
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고 
		mCursor.moveToFirst();
		
		if(mCursor !=null&&mCursor.getCount()!=0){
		
			do{
				for(int i=0;i<mCursor.getColumnCount();i++){
					
					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
	
		}while(mCursor.moveToNext());
	}
		mCursor.close();
		db.close();
		dbHelper.close();
		return inputParams;
	
	}
	
	StringBuffer update213Table() {
		StringBuffer inputParams = new StringBuffer();
		  
		String query = "select CS_EMP_ID,WORK_DT,JOB_NO,NFC_PLC,CS_ITEM_CD,INPUT_TP,"
				+ "INPUT_TP1,INPUT_TP3,INPUT_TP7,OVER_MONTH,MONTH_CHK,MONTH_CHK_IF,INPUT_RMK,DEF_VAL_ST,CS_EMP_ID from TCSQ213 "
				+ " WHERE WORK_DT = '" + updateWorkDt + "' AND JOB_NO = '" +updateJobNo +  "'";
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query,
					null); // 쿼리 날리고 
		mCursor.moveToFirst();
		
		if(mCursor !=null&&mCursor.getCount()!=0){
		
			do{
				for(int i=0;i<mCursor.getColumnCount();i++){
					
					if(i==mCursor.getColumnCount()-1){
						inputParams.append(mCursor.getString(i)).append("|");
					}else{
						inputParams.append(mCursor.getString(i)).append("^");
					}
				}
		}while(mCursor.moveToNext());
	}
		mCursor.close();
		db.close();
		dbHelper.close();
		return inputParams;
	}
	
	
	private class UploadDatabaseAsync extends AsyncTask<Void, String, Boolean> {
		private String updateRet = "";
		private EasyJsonMap  msgMap;
		
		private String exceptionMsg;
		@Override
		protected void onPostExecute(Boolean result) {
			 super.onPostExecute(result);
			 WO_WT00_R00.this.ProgressDialog.dismiss();
			
			if(result){
				try {
					boolean isError = msgMap.getValue("errCd").equals("0") ? true:false;//0이면 정상
					if(isError){
						
						if(updateRet.equals("1")){
							//정상 동기화 정상동장
							initialize();
							stringInit();
						
						}else{
							//실패 : 점검항목 생성 X
							Toast.makeText(activity, "동기화를 실패하였습니다..", 2000).show();
							
						}
						
					}else{
						alert(msgMap.getValue("errMsg"), activity);
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
				if(exceptionMsg != null){
					String errMsg = "";
					
						try {
							if(msgMap != null) errMsg = msgMap.getValue("errMsg");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					alert(exceptionMsg + errMsg, activity);
				}
			}
			
		/*	if (updateRet.equals("1")) {
				// 동기화가 정상적으로 됐을 경우 이니셜라이즈
				initialize();
				stringInit();
			} else {
				Toast.makeText(activity, "동기화를 실패하였습니다..", 2000).show();
			}*/
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			   WO_WT00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			    		activity,"동기화","서버와 동기화 중입니다...");	
			//HM_MP00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			    		//activity,"동기화","서버와 동기화 중입니다...");	
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
			
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/dbUploadTBL.do";	//20170922 yowonsm 테스트끝나면 바꿈
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("upQ030", upQ030));
				arguments.add(new BasicNameValuePair("upQ210", upQ210));
				arguments.add(new BasicNameValuePair("upQ213", upQ213));


				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					msgMap = new EasyJsonMap(returnJson.getJSONObject("msgMap"));
					updateRet = returnJson.getString("dataString");
					
					Log.v("updateRet", "UpdateRet = " + updateRet);


				} catch (JSONException e) {
					e.printStackTrace();
					exceptionMsg = e.toString();
					return false;
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				exceptionMsg = ex.toString();
				return false;
			}
			return true;

		}
	}
	
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	/*private class UploadDatabaseAsync extends AsyncTask<Void, String, Void> {
		private String updateRet = "";
		@Override
		protected void onPostExecute(Void result) {
			 WO_WT00_R00.this.ProgressDialog.dismiss();
			
			 if(updateRet.equals("1")){
			initialize();
			stringInit();
		 }else{
			 Toast.makeText(activity, "동기화가 제대로 되지 않았습니다.", 2000).show();
		 }
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			   WO_WT00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			    		activity,"동기화","서버와 동기화 중입니다...");	
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
			
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/dbUploadTBL.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("upQ030", upQ030));
				arguments.add(new BasicNameValuePair("upQ210", upQ210));
				arguments.add(new BasicNameValuePair("upQ213", upQ213));


				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {

					updateRet = returnJson.getString("dataString");
					Log.v("updateRet", "UpdateRet = " + updateRet);


				} catch (JSONException e) {
					e.printStackTrace();
				}

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;

		}
	}
	*/
	private void stringInit(){
		upQ030 = "";
		upQ210 ="";
		upQ213 ="";
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		
		
		if(isLocalDb){
			
			String workNm = workListData.get(position).getWORK_NM();
			jobNo = workListData.get(position).getJOB_NO();
			
			SimpleYesNoDialog ynDialog = new SimpleYesNoDialog(activity,
					"선택한 작업을 삭제하시겠습니까?",
					new btnClickListener() {

						@Override
						public void onButtonClick() {
							// TODO Auto-generated method stub
							new DeleteWorkAsync().execute();
						}
					});
			ynDialog.show();
			
			//여기는 로컬 디비
		}else{
//			Cursor cursor = wO_WT00_R01F_Adapter01.getCursor();
//			Intent intent = new Intent(activity, WO_WT00_R01.class);
//			intent.putExtra("jobNo",
//					cursor.getString(cursor.getColumnIndex("JOB_NO")));
//			intent.putExtra("workDt",
//					cursor.getString(cursor.getColumnIndex("WORK_DT")));
//			startActivity(intent);
		}
		return true;
	}
	
	private class DeleteWorkAsync extends AsyncTask<Void, String, Void> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		EasyJsonMap ej01;
		 String ret = "";
		 String retDesc = "";
	
		
		@Override
		protected Void doInBackground(Void... params) {
			// 1. bagicWorkTime

			try {

				String param_url = WebServerInfo.getUrl() + "ip/deleteWork.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("empId", cs.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo", jobNo));
				returnJson01 = http.getPost(param_url, arguments, true);

				ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));

			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
			}
			return null;
	
		}
		
		@Override
		protected void onPostExecute(Void result) {
			 WO_WT00_R00.this.ProgressDialog.dismiss();
			
			 try {
				 ret = ej01.getValue("RTN");
				 retDesc = ej01.getValue("RTN_DESC");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			 if(ret.equals("1")){
				 Toast.makeText(activity, retDesc, 2000).show();
				 if(isSynchronized){
						//upLoadDatabase();
					}else{
						initialize();
					}
			 }else if(ret.equals("2")){
					SimpleDialog sm01 = new SimpleDialog(activity, "알림",retDesc);
					sm01.show();
			 }else if(ret.equals("0")){
				 SimpleDialog sm01 = new SimpleDialog(activity, "알림",retDesc);
				 sm01.show();
			 }
			 
		
			 
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			   WO_WT00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
			    		activity,"작업삭제","삭제 중입니다...");	
		}
	}

	private boolean isResultCancel = false;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

				case 3:
				if(resultCode == RESULT_OK)
				{
					Bundle bundle = data.getExtras();
					this.commuteResultProcess(bundle);
					break;
				}else{
					isResultCancel = true;
				}

				break;


		}
	}

	private void commuteResultProcess(Bundle bundle){
		String commute = bundle.getString("commute");
		String rtnCode = bundle.getString("rtnCode");
		String commuteTime = bundle.getString("commuteTime");

		if(rtnCode.equals("1")){

			alert(this.cs.getEmpNm() + "님 " + commuteTime +  " 퇴근 처리되었습니다.", activity);
			Toast.makeText(activity, this.cs.getEmpNm() + "님 " +commuteTime +
					" 퇴근 처리되었습니다." , 2000).show();

			cs.setIsAttended(false);
			//20150126 퇴근후에 로그아웃 시킨다.
			CallService.startGPSService(activity);
			attendAndLogout();
		}else if(rtnCode.equals("0")){
			alert("퇴근 미처리입니다.", activity);
		}else if(rtnCode.equals("2")){
			alert("이미 퇴근상태입니다.", activity);
		}else if(rtnCode.equals("3")){
			//퇴근하려는데 '진행중','계획' 상태인 작업이 있을 경우
			alert("완료 안된 작업이 있습니다. 확인해 주세요.", activity);
		}
	}

	private void attendAndLogout(){
		GK_LO00_R00 logout = new GK_LO00_R00(activity);
		logout.attendAndLogout();
	}

}
