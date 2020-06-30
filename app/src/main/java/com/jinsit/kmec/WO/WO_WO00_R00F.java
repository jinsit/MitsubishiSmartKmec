package com.jinsit.kmec.WO;

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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.RoutineCheckListData;
import com.jinsit.kmec.WO.WT.RI.WO_WT00_R03_MNG;
import com.jinsit.kmec.WO.WT.RI.WT_RI01_R00_Adapter00_W;
import com.jinsit.kmec.WO.WT.RI.WT_RI01_R00_Adapter01;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;
import com.jinsit.kmec.widget.WorkObjectTabWidget;
import com.jinsit.kmec.widget.WorkObjectTabWidget.OnTabPositionListener;


public class WO_WO00_R00F extends FragmentActivity implements
		OnClickListener, OnTabPositionListener {
	final String TAG = "WO_WT00_R00";
	int mCurrentFragmentIndex;
	private ProgressDialog ProgressDialog;
	private EasyJsonList ejl;
	public final static int FRAGMENT_WORK_TARGET_LIST = 0;
	public final static int FRAGMENT_PENDING_LIST = 1;
	public final static int FRAGMENT_COMPLETED_LIST = 2;
	public final static int FRAGMENT_WORK_CREATE = 3;
	public final static int FRAGMENT_CHECK_STATUS = 4;

	private Context context = null;

	WorkObjectTabWidget workObjectTabWidget;
	private  Button btn_tabWT,btn_tabPL,btn_tabCL,btn_tabWC;
	ArrayList<RoutineCheckListData> routineCheckListData;
	private WT_RI01_R00_Adapter00_W routineCheckListAdapter = null;
	private CommonSession commonSession;
	
	private WT_RI01_R00_Adapter01 wT_RI01_R00_Adapter01 = null;
	// Within which the entire activity is enclosed	
		DrawerLayout mDrawerLayout;
		// ListView represents Navigation Drawer
		ListView mDrawerList;
		// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the action bar
		ActionBarDrawerToggle mDrawerToggle;	
		// Title of the action bar
		
		TextView tv_listItemHeader;
		LinearLayout linear ;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r00);
		getActionBar().setDisplayShowHomeEnabled(false);//액션바 아이콘 없애기
		context = this;
		commonSession = new CommonSession(context);
		LayoutInflater li = null;
		li = LayoutInflater.from(context);
		 linear = (LinearLayout) li.inflate(R.layout.listitem_header, null);
		tv_listItemHeader = (TextView)linear.findViewById(R.id.tv_listItemHeader); 
		//tv_listItemHeader.setText(DateUtil.nowDate());
		tv_listItemHeader.setText(commonSession.getWorkDt());
		workObjectTabWidget = new WorkObjectTabWidget(context, null);
		workObjectTabWidget = (WorkObjectTabWidget) findViewById(R.id.tab_workObject);
		
		/*int buttonHeight = getResources().getDrawable(R.drawable.wo_tab_menu01_off).getIntrinsicHeight();
		int tabHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, tabHeight);
		workObjectTabWidget.setLayoutParams(param);*/
		
		btn_tabWT = (Button) workObjectTabWidget.findViewById(R.id.btn_tabWT);
		btn_tabPL = (Button) workObjectTabWidget.findViewById(R.id.btn_tabPL);
		btn_tabCL = (Button) workObjectTabWidget.findViewById(R.id.btn_tabCL);
		btn_tabWC = (Button) workObjectTabWidget.findViewById(R.id.btn_tabWC);
		btn_tabWT.setOnClickListener(this);
		btn_tabPL.setOnClickListener(this);
		btn_tabCL.setOnClickListener(this);
		btn_tabWC.setOnClickListener(this);
		int defaultFragment=0;
		if(getIntent().getExtras()!=null)defaultFragment = getIntent().getExtras().getInt("fragment",0);
		mCurrentFragmentIndex  = defaultFragment;
		fragmentReplace(mCurrentFragmentIndex);
		btnSelect(mCurrentFragmentIndex);
		
		
			
		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		mDrawerList.addHeaderView(linear);
		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(	this, 
													mDrawerLayout, 
													R.drawable.ic_launcher, 
													R.string.hello_world){
			
			/** Called when drawer is closed */
            public void onDrawerClosed(View view) {
            
            	setActionBarTitle(mCurrentFragmentIndex);
            	invalidateOptionsMenu();
                
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
            	setActionBarTitle(FRAGMENT_CHECK_STATUS);
                invalidateOptionsMenu();
            }
			
		};
		
		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);
		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		JActionbar.setActionBar(this, getActionBar());
		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,
							View view,
							int position,
							long id) {		
				
				Log.v("listPosition", "listPos  =" + position);
				
				if(NetworkStates.isNetworkStatus(context)){
					// 온라인인 경우
					if(position!=0){
					int tCnt = Integer.valueOf(routineCheckListData.get(position-1).getT_CNT());
					int iCnt = Integer.valueOf(routineCheckListData.get(position-1).getI_CNT());
					if(tCnt-iCnt==0){
						mDrawerLayout.closeDrawer(mDrawerList);		

					
						RoutineCheckListData mData = routineCheckListData.get(position-1);
						Bundle extras = new Bundle();
						
						//Intent intent = new Intent(WO_WO00_R00F.this, WO_WT00_R03.class);
						Intent intent = new Intent(WO_WO00_R00F.this, WO_WT00_R03_MNG.class);
						extras.putSerializable("obj", mData);
						intent.putExtras(extras);
						intent.putExtra("pagerCount", tCnt);
						startActivity(intent);
					}else{
						Toast.makeText(context, "작업이 완료되지 않았습니다", 2000).show();
						
				}
					}//if 문 if(position!=0){ 끝
						
				}else{
					Toast.makeText(context, "인터넷 연결이 안되어있습니다.", 2000).show();
					///로칼인 경우
//				Cursor cursor = wT_RI01_R00_Adapter01.getCursor();
//				String WORK_DT = cursor.getString(cursor.getColumnIndex("WORK_DT"));
//				String BLDG_NO = cursor.getString(cursor.getColumnIndex("BLDG_NO"));
//				String BLDG_NM = cursor.getString(cursor.getColumnIndex("BLDG_NM"));
//				String I_CNT = cursor.getString(cursor.getColumnIndex("I_CNT"));
//				String T_CNT = cursor.getString(cursor.getColumnIndex("T_CNT"));
//				String REF_CONTR_NO = cursor.getString(cursor.getColumnIndex("REF_CONTR_NO"));
//				String E_TEXT = cursor.getString(cursor.getColumnIndex("E_TEXT"));
//			//드로우아이템 클릭시 해당 빌딩넘버를 가지고  정기점검표화면으로 이동한다.
//				if(position!=0){
//					int tCnt = Integer.valueOf(T_CNT);
//					int iCnt = Integer.valueOf(I_CNT);
//					if(tCnt-iCnt==0){
//						mDrawerLayout.closeDrawer(mDrawerList);	
//						
//						RoutineCheckListData mData =new RoutineCheckListData();
//						mData.setWORK_DT(WORK_DT);
//						mData.setBLDG_NO(BLDG_NO);
//						mData.setBLDG_NM(BLDG_NM);
//						mData.setI_CNT(I_CNT);
//						mData.setT_CNT(T_CNT);
//						mData.setE_TEXT(E_TEXT);
//						
//						Bundle extras = new Bundle();
//						
//						Intent intent = new Intent(WO_WO00_R00F.this, WO_WT00_R03_MNG.class);
//						extras.putSerializable("obj", mData);
//						intent.putExtras(extras);
//						intent.putExtra("pagerCount", tCnt);
//						startActivity(intent);
//						
//				}else{
//					Toast.makeText(context, "작업이 완료되지 않았습니다", 2000).show();
//				}
//					
//				}
//				
				}//if 문 끝
				
				}
		});
		setConfig();
	}
	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
	}

	public void fragmentReplace(int reqNewFragmentIndex) {
		Fragment newFragment = null;
		Log.d(TAG, "fragmentReplace " + reqNewFragmentIndex);
		newFragment = getFragment(reqNewFragmentIndex);
		// replace fragment
		final FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.ll_wo_parent, newFragment);
		// Commit the transaction
		transaction.commit();

	}


	private Fragment getFragment(int idx) {
		Fragment newFragment = null;

		switch (idx) {
		case FRAGMENT_WORK_TARGET_LIST:
			setActionBarTitle(FRAGMENT_WORK_TARGET_LIST);
			
			newFragment = new WO_WT00_R00();
			break;
		case FRAGMENT_PENDING_LIST:
			setActionBarTitle(FRAGMENT_PENDING_LIST);
			newFragment = new WO_PL00_R00();
			break;
	
		case FRAGMENT_COMPLETED_LIST:
			setActionBarTitle(FRAGMENT_COMPLETED_LIST);
			newFragment = new WO_CL00_R00();
			break;
		case FRAGMENT_WORK_CREATE:
			setActionBarTitle(FRAGMENT_WORK_CREATE);
			newFragment = new WO_WC00_R00();
			break;
		default:
			break;
		}

		return newFragment;
	}
	
	private void setActionBarTitle(int index) {
		// TODO Auto-generated method stub
		String title="";
		switch(index){
		case 0:
			title="작업대상목록";
			break;
		case 1:
			title="미처리목록";
			break;
		case 2:
			title="완료목록";
			break;
		case 3:
			title="작업생성";
			break;
		case 4:
			title="정기점검현황";
			break;
		}
		getActionBar().setTitle(title);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub


		switch (v.getId()) {
		case R.id.btn_tabWT:
			setBtnTabWT();
			break;

		case R.id.btn_tabPL:
			mCurrentFragmentIndex = FRAGMENT_PENDING_LIST;
			btnSelect(mCurrentFragmentIndex);
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.btn_tabCL:
			mCurrentFragmentIndex = FRAGMENT_COMPLETED_LIST;
			btnSelect(mCurrentFragmentIndex);
			fragmentReplace(mCurrentFragmentIndex);
			break;
		case R.id.btn_tabWC:
			mCurrentFragmentIndex = FRAGMENT_WORK_CREATE;
			btnSelect(mCurrentFragmentIndex);
			fragmentReplace(mCurrentFragmentIndex);
			break;


		default:
			break;

		}
	}
	public void setBtnTabWT(){
		mCurrentFragmentIndex = FRAGMENT_WORK_TARGET_LIST;
		btnSelect(mCurrentFragmentIndex);
		fragmentReplace(mCurrentFragmentIndex);
	}
	
	private void btnSelect(int idx) {

		btn_tabWT.setBackgroundResource(R.drawable.wo_tab_menu01_off);
		btn_tabPL.setBackgroundResource(R.drawable.wo_tab_menu02_off);
		btn_tabCL.setBackgroundResource(R.drawable.wo_tab_menu03_off);
		btn_tabWC.setBackgroundResource(R.drawable.wo_tab_menu04_off);
		btn_tabWT.setEnabled(true);
		btn_tabPL.setEnabled(true);
		btn_tabCL.setEnabled(true);
		btn_tabWC.setEnabled(true);
		switch(idx){
		case 0:
			btn_tabWT.setBackgroundResource(R.drawable.wo_tab_menu01_on);
			btn_tabWT.setEnabled(false);
			break;
		case 1:
			btn_tabPL.setBackgroundResource(R.drawable.wo_tab_menu02_on);
			btn_tabPL.setEnabled(false);
			break;
		case 2:
			btn_tabCL.setBackgroundResource(R.drawable.wo_tab_menu03_on);
			btn_tabCL.setEnabled(false);
			break;
		case 3:
			btn_tabWC.setBackgroundResource(R.drawable.wo_tab_menu04_on);
			btn_tabWC.setEnabled(false);
			break;
		}
	}
	 @Override
	 protected void onPostCreate(Bundle savedInstanceState) {
		 super.onPostCreate(savedInstanceState);	     
	     mDrawerToggle.syncState();	
	 }
	
	/** Handling the touch event of app icon */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {     
        if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
	/** Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the drawer is open, hide action items related to the content view
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        
       // menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		if(NetworkStates.isNetworkStatus(context)){
			new RoutineCheckListAsync().execute();		
		}else{
			selectRoutineCheckTable();	
		}
	
		
		return true;
	}
	
	
	@Override
	public void onTabPosition(int position) {
		// TODO Auto-generated method stub
		btnSelect(position);
	}
	
	
	private void selectRoutineCheckTable(){
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = new DatabaseRawQuery().selectCheckTableList(commonSession.getEmpId(),commonSession.getWorkDt());
				Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
				wT_RI01_R00_Adapter01 =new WT_RI01_R00_Adapter01(context,
						R.layout.listitem_routine_checklist, mCursor, new String[] {
								"BLDG_NM", "E_TEXT", "I_CNT" },
						null, 0);
				mDrawerList.setAdapter(wT_RI01_R00_Adapter01);
		
	}
	
	
	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {
		  
		@Override
		protected void onPostExecute(Void result) {
			WO_WO00_R00F.this.ProgressDialog.dismiss();
			
			routineCheckListAdapter = new WT_RI01_R00_Adapter00_W(context,
					routineCheckListData);
			mDrawerList.setAdapter(routineCheckListAdapter);
			
			  
		}

		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    WO_WO00_R00F.this.ProgressDialog = android.app.ProgressDialog.show(
		    		WO_WO00_R00F.this,"점검항목","점검항목 조회중");
		    		
		}@Override
		protected Void doInBackground(Void... params) {
			routineCheckListData = new ArrayList<RoutineCheckListData>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRoutineCheckList.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
	
			arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
			//arguments.add(new BasicNameValuePair("workDt", DateUtil.nowDate()));
			arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
			JSONObject returnJson = getHttp.getPost(param_url, arguments,
					true);

			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				routineCheckListData.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					routineCheckListData.add(new RoutineCheckListData(ejl.getValue(i, "WORK_DT"),
							ejl.getValue(i, "BLDG_NO"),
							ejl.getValue(i, "BLDG_NM"),
							ejl.getValue(i, "E_TEXT"),
							ejl.getValue(i, "I_CNT"),
							ejl.getValue(i, "T_CNT"),
							ejl.getValue(i, "REF_CONTR_NO")));
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}
		
		
		
	}
	
	//----------------------내비게이션 영역--------------------------------------//
		private HomeNavigation homeNavi;
		private FrameLayout testnavi;
		boolean isHide;
		private HomeNaviPreference naviPref;
		private boolean isFirstHide = false;
		private void setToggleNavi(){
			boolean isHide = naviPref.isHide();
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int totalHeight = ScreenUtil.getRealScreenHeight(this);;
			int naviHeight = getResources().getDrawable(R.drawable.home_menu_on).getIntrinsicHeight();
			int viewArea = naviHeight/6;
			int setPadding = totalHeight-viewArea-naviHeight;
			 if(isHide){
					testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
					Log.e("isHide", "naviHide = " +isHide );
					workObjectTabWidget.setVisibility(View.VISIBLE);
				}else{
					testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
					Log.e("isHide", "naviHide = " +naviPref.isHide() );
					workObjectTabWidget.setVisibility(View.INVISIBLE);
				}
		}
		
		private void navigationInit(){
			testnavi = (FrameLayout) findViewById(R.id.fl_naviBar);
			homeNavi = new HomeNavigation(context, null);
			homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
			
			//탭이있는 화면이라 액티비티 시작일 때 체크한다. 
			if(!isFirstHide){
				naviPref.setHide(true);
				isFirstHide = true;
			}
			homeNavi.setToggleNavi();
			Button navi = (Button) homeNavi.getBtn_naviHOME();
			navi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					naviPref.setHide(!naviPref.isHide());
					homeNavi.setToggleNavi();
					
				}
			});
		}
		@Override
		public void onResume(){
			super.onResume();
		
			naviPref = new HomeNaviPreference(context);
			navigationInit();
		}
		//----------------------내비게이션 영역--------------------------------------//
		
}
