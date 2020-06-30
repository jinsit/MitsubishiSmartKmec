package com.jinsit.kmec.WO.CL.RI;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.WO.WT.RI.PartCheckListData;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

/**
 *
 * @author 원성민 각 파트별 점검항목을 표시한다
 */

@SuppressLint("ResourceAsColor")
public class CL_RI00_R02 extends FragmentActivity {
	ListView lv_wo_checkList;
	//PartCheckListAdapter partCheckListAdapter;
	CL_RI00_R02_Adapter partCheckCursorAdapter;
	private ArrayList<PartCheckListData> partCheckListData;
	private EasyJsonList ejl;
	Context context;
	String nfcPlc = "";
	String jobNo;
	String workDt;
	String selTp = "1";
	boolean isComplete = true;
	int falsePosition = 0;

	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private String ndefMsg;
	private String tagUid = "";
	private CommonSession commonSession;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r02);
		activityInit();

	}


	///점검항목 파트별 점검완료
	void jobPartEnd(){

		String query = new DatabaseRawQuery().updateEndPartCheck(
				commonSession.getEmpId(), workDt, jobNo,nfcPlc, DateUtil.nowDateFormat("HH:mm"),
				"39");

		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(query);
		db.close();

	}


	private boolean checkListCheck(){
		boolean isCheck = true;
		for (int i = 0; i < partCheckCursorAdapter.widgetData.size(); i++) {

			try {
				if(partCheckCursorAdapter.widgetData.get(i).getInputTp().equals("3")){
					Log.e("getInputTp", "getInputTp =  "+ partCheckCursorAdapter.widgetData.get(i).getInputTp());


					if(partCheckCursorAdapter.widgetData.get(i).getInputTp3().equals("")){
						Log.w("inputTp3", "inputTp3 =  "+ partCheckCursorAdapter.widgetData.get(i)
								.getInputTp3());
						isCheck = false;
						falsePosition = i;
						break;
					}
				}
			} catch (Exception ex) {
				// 로그인이 실패하였습니다 띄어주기
				isCheck = false;
			}
		}// 포문끝


		return isCheck;
	}


	private void activityInit() {
		// TODO Auto-generated method stub
		context = this;
		commonSession = new CommonSession(context);
		nfcPlc = getIntent().getExtras().getString("nfcPlc");
		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		String nfcPlcNm = getIntent().getExtras().getString("nfcPlcNm");
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("점검항목(" + nfcPlcNm + ")");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		lv_wo_checkList = (ListView) findViewById(R.id.lv_wo_checkList);

		Log.e("jobno plc " , "jobNo = " + jobNo + "   nfcPlc =" + nfcPlc);
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String query = new DatabaseRawQuery().selectCheckDetail(commonSession.getEmpId(),workDt, jobNo,nfcPlc);
		Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고
		mCursor.moveToFirst();
		partCheckCursorAdapter =new CL_RI00_R02_Adapter(context,
				R.layout.listitem_wt_checklist, mCursor, new String[] {
				"CS_ITEM_CD", "CS_EMP_ID", "MONTH_CHK_IF" },
				null, 0);
		lv_wo_checkList.setAdapter(partCheckCursorAdapter);

		if(mCursor.getCount()==0){


			SimpleDialog ynDialog = new SimpleDialog(context, "알림","점검항목이 없습니다.",
					new com.jinsit.kmec.comm.jinLib.SimpleDialog.btnClickListener() {

						@Override
						public void onButtonClick() {
							// TODO Auto-generated method stub
							jobPartEnd();
							finish();

						}
					});
			ynDialog.show();

		};
		//new PartCheckListAsync().execute();
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().addActivity(this);
	}

	//----------------------내비게이션 영역--------------------------------------//
	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

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
	protected void onResume() {
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();

	}
	//----------------------내비게이션 영역--------------------------------------//


}
