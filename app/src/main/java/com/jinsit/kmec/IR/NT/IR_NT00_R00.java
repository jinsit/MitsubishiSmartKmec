package com.jinsit.kmec.IR.NT;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_NT00_R00 extends Activity implements OnItemClickListener {
	private Context context;

	ListView lv_noticeList;
	NoticeListAdapter noticeListAdapter;
	ArrayList<NoticeResponseData> noticeListData;
	private ProgressDialog ProgressDialog;
	private EasyJsonList ejl;
	CommonSession commonSession;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("공지사항");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		activityInit();

	}
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
	public void onResume(){
		super.onResume();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}
	private void activityInit() {
		this.context = this;
		commonSession = new CommonSession(context);
		this.lv_noticeList = ((ListView) findViewById(R.id.lv_notice));
		this.lv_noticeList.setOnItemClickListener(this);
		new NoticeAllTask().execute();
		setConfig();
	}

	private void setConfig(){
		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);
		//ActivityAdmin.setActMovementSys(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		lv_noticeList.setEnabled(false);
		NoticeResponseData mData = noticeListData.get(position);
		IR_NT00_R01P NTD = new IR_NT00_R01P(context, mData);
		NTD.show();
		NTD.sch();
		Handler h = new Handler();
		h.postDelayed(new ClickHandler(),1000);
	}

	class ClickHandler implements Runnable{
		//중복클릭 방지용 핸들러
		@Override
		public void run() {
			// TODO Auto-generated method stub
			lv_noticeList.setEnabled(true);
		}

	}
	public class NoticeAllTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_NT00_R00.this.ProgressDialog = android.app.ProgressDialog.show(
					IR_NT00_R00.this, "공지사항", "불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				noticeListData = new ArrayList<NoticeResponseData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectAllNoticeData.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("noticeDt", ""));
				arguments.add(new BasicNameValuePair("deptCd", commonSession.getDeptCd()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {

					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					noticeListData.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						noticeListData.add(new NoticeResponseData(ejl.getValue(
								i, "NOTICE_DT"), ejl.getValue(i, "NOTICE_TM"),
								ejl.getValue(i, "TITLE"), ejl.getValue(i,
								"SENDER_NM"), ejl.getValue(i,
								"RECIPIENT_NM"), ejl.getValue(i,
								"CONTENT")));
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
			IR_NT00_R00.this.ProgressDialog.dismiss();

			noticeListAdapter = new NoticeListAdapter(context, noticeListData);
			lv_noticeList.setAdapter(noticeListAdapter);

		}
	}

}
