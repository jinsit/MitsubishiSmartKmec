package com.jinsit.kmec.WO.WT.RI;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;
/**
 * 정기점검표 관리원용으로 변환
 
 * @author 원성민
 *
 */
public class WO_WT00_R03_MNG extends FragmentActivity  
 {
	
	Context context;
	ViewPager vp_wo_checkTable;
	int pagerCount;
	String bldgNm, bldgNo, carNo,refContrNo;
	CommonSession commonSession;
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
	public String engComment;
	CommentPreference commentPref;
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
		 commentPref = new CommentPreference(context);
		 commonSession = new CommonSession(context);
		 
	 
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
		 //new RoutineCheckListAsync().execute();
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
			
//				Bundle arguments = new Bundle();
//				arguments.putString("comment", engComment);
//				Fragment fm = manager.getFragment(arguments, "");
//				
//				manager.getFragments();
//				mPagerAdapter.notifyDataSetChanged();
				
				//RoutineCheckTableFragmentMng.create(position, bldgNm,bldgNo,refContrNo,car,commentPref.getComment());
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
	
	FragmentManager manager;
	public class CheckTablePagerAdapter extends FragmentStatePagerAdapter {
		public CheckTablePagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			manager = fm;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Log.v("onPageScrolled", "onPageScrolled position = " + position);
			//return RoutineCheckTableFragment.create(position, bldgNm,bldgNo,refContrNo,car);
			
			return RoutineCheckTableFragmentMng.create(position, bldgNm,bldgNo,refContrNo,car,engComment);

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
				Intent intent = new Intent(WO_WT00_R03_MNG.this,WO_WT00_U03.class);
				extras.putSerializable("obj", mData);
				intent.putExtras(extras);
				intent.putExtra("comment", commentPref.getComment());
				startActivity(intent);	
			}else{
				Toast.makeText(context, "고객승인을 위해선 인터넷이 연결되어 있어야 합니다." , 2000).show();
			}
			
			return true;
		}
		return super.onOptionsItemSelected(item);
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
					}else{
						testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
						Log.e("isHide", "naviHide = " +naviPref.isHide() );
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
