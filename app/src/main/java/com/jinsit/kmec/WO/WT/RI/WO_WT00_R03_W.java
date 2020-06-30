package com.jinsit.kmec.WO.WT.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;
/**
 * 정기점검표
 * @deprecated웹용이라 디플레케이션
 * @author 원성민
 *
 */
public class WO_WT00_R03_W extends FragmentActivity {

	Context context;
	ViewPager vp_wo_checkTable;
	int pagerCount;
	String bldgNm, bldgNo, carNo,refContrNo;
	
	private ProgressDialog ProgressDialog;
	private PagerAdapter mPagerAdapter;
	RoutineCheckListData mData;
	private ArrayList<PartCheckListData> partCheckListData;
	private ArrayList<String> groupData;
	
	private EasyJsonList ejl;
	//ArrayList<RoutineCheckListData> routineCheckListData;
	private RoutineCheckExpandListAdapter routineCheckExpandListAdapter = null;
	private ExpandableListView lv_wo_checkTableListParent;
	
	private String selTp="1";
	private String nowCarNo="";
	private boolean isFirst=true;
	ArrayList<String> car ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r03);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("정기점검표");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		activityInit();
		//checkTableDown();
		
	}
	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		pagerCount = getIntent().getExtras().getInt("pagerCount");
		mData = (RoutineCheckListData) this.getIntent()
					.getExtras().getSerializable("obj");
		
		
		bldgNm = mData.getBLDG_NM();
		bldgNo = mData.getBLDG_NO();
		
		refContrNo = mData.getREF_CONTR_NO();
		getCarNo();
		vp_wo_checkTable = (ViewPager) findViewById(R.id.vp_wo_checkTable);
		pagerAdapterSetting();
	}
	
	
	
	ArrayList<String> getCarNo(){
	 car = new ArrayList<String>();
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = "select CAR_NO from WORK_TBL where BLDG_NO ='" + bldgNo +"'";
				// + " and REF_CONTR_NO = '" + refControlNumber +"'";
					Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
				
			//String carNo = 	mCursor.getString(0);
	
			if(mCursor !=null&&mCursor.getCount()!=0){
				
				do{
					car.add(mCursor.getString(0));
			}while(mCursor.moveToNext());
		
			}else{
				
			}
		return car;
		
	}
	
	private void checkTableDown() {
		// TODO Auto-generated method stub
		// new RoutineCheckListAsync().execute();
	}
	private void pagerAdapterSetting(){
		isFirst = false;
		mPagerAdapter = new CheckTablePagerAdapter(getSupportFragmentManager());
		vp_wo_checkTable.setAdapter(mPagerAdapter);

		vp_wo_checkTable.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// /페이지가 옆으로 이동할 때 이벤트
				Log.v("onPageSeleted", "onPageSeleted position = " + position);
			 
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				Log.e("onPageScrolled", "onPageScrolled position = " + position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				Log.w("onPageScrollStateChanged", "onPageScrollStateChanged position = " + state);
			}
		});
		
	}
	

	public class CheckTablePagerAdapter extends FragmentStatePagerAdapter {
		public CheckTablePagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Log.v("onPageScrolled", "onPageScrolled position = " + position);
			return RoutineCheckTableFragment.create(position, bldgNm,bldgNo,refContrNo,car);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pagerCount;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wo_wt00_r03, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_ip_wo_consumerConfirm) {
			// 고객이 서명을 위해 서명화면으로 이동한다
			
			
			if(NetworkStates.isNetworkStatus(context)){
				Bundle extras = new Bundle();
				Intent intent = new Intent(WO_WT00_R03_W.this,WO_WT00_U03.class);
				extras.putSerializable("obj", mData);
				intent.putExtras(extras);
				startActivity(intent);	
			}else{
				Toast.makeText(context, "고객승인을 위해선 인터넷이 연결되어 있어야 합니다." , 2000).show();
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
//	
//	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {
//		
//		
//		@Override
//		protected void onPostExecute(Void result) {
//			WO_WT00_R03.this.ProgressDialog.dismiss();
//			
//			PartCheckItemGroupList group = new PartCheckItemGroupList(partCheckListData);
//			groupData = group.getGroupList();
//			//그룹데이터 초기화
//			
//			ArrayList<Object> obj = new ArrayList<Object>();
//			for (int i = 0; i < groupData.size(); i++) {
//				ArrayList<PartCheckListData> clsd = new ArrayList<PartCheckListData>();
//				for (int j = 0; j < partCheckListData.size(); j++) {
//
//					if (groupData.get(i).equals(partCheckListData.get(j).getNFC_PLC_NM())) {
//						clsd.add(partCheckListData.get(j));
//						// clsd.add(object)
//					}
//				}
//				obj.add(clsd);
//
//			}
//			
//			 mappingItem = new ArrayList<PartCheckTableItem>();
//			for (int i = 0; i < groupData.size(); i++) {
//				mappingItem.add(new PartCheckTableItem(groupData.get(i),
//						(ArrayList<PartCheckListData>)obj.get(i)));
//			}
//			
//			Log.i("RoutineCheckListAsync", "RoutineCheckListAsync position = " + mappingItem.size());
//			if(mappingItem.size()!=0)nowCarNo = mappingItem.get(0).child.get(0).getCAR_NO();
//			if(isFirst)pagerAdapterSetting();
//			
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			WO_WT00_R03.this.ProgressDialog = android.app.ProgressDialog
//					.show(context, "점검항목", "점검항목 조회중");
//		}
//
//		@Override
//		protected Void doInBackground(Void... params) {
//		
//
//			partCheckListData = new ArrayList<PartCheckListData>();
//			GetHttp getHttp = new GetHttp();
//			String param_url = WebServerInfo.getUrl()
//					+ "ip/selectRoutineCheckTable.do";
//		
//			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
//			arguments.add(new BasicNameValuePair("selTp", selTp));
//			arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
//			arguments.add(new BasicNameValuePair("workDt", workDt));
//			arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
//			arguments.add(new BasicNameValuePair("carNo", nowCarNo));
//			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
//
//
//			try {
//				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
//				partCheckListData.clear();
//				int jsonSize = returnJson.getJSONArray("dataList").length();
//				for (int i = 0; i < jsonSize; i++) {
//					partCheckListData.add(new PartCheckListData(ejl.getValue(i,
//							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"),
//							ejl.getValue(i, "JOB_NO"),
//							ejl.getValue(i, "NFC_PLC"),
//							ejl.getValue(i, "CS_ITEM_CD"),
//							ejl.getValue(i, "SMART_DESC"),
//							ejl.getValue(i, "CS_TOOLS"),
//							ejl.getValue(i, "STD_ST"),
//							ejl.getValue(i, "INPUT_TP"),
//							ejl.getValue(i, "INPUT_TP1"),
//							ejl.getValue(i, "INPUT_TP3"),
//							ejl.getValue(i, "INPUT_TP7"),
//							ejl.getValue(i, "INPUT_RMK"),
//							ejl.getValue(i, "OVER_MONTH"),
//							ejl.getValue(i, "MONTH_CHK_IF"),
//							ejl.getValue(i, "MONTH_CHK"),
//							ejl.getValue(i, "BLDG_NO"),
//							ejl.getValue(i, "BLDG_NM"),
//							ejl.getValue(i, "CAR_NO"),
//							ejl.getValue(i, "CAR_NO_TO"),
//							ejl.getValue(i,"NFC_PLC_NM")));
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//	}
}
