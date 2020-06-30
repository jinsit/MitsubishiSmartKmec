package com.jinsit.kmec.IR.CI;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.IP.IS.IR_CI03_R02_ExpandListAdapter;
import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WT.RI.JinSimpleArrayAdapter;
import com.jinsit.kmec.WO.WT.RI.PartCheckItemGroupList;
import com.jinsit.kmec.WO.WT.RI.PartCheckListData;
import com.jinsit.kmec.WO.WT.RI.PartCheckTableItem;
import com.jinsit.kmec.WO.WT.RI.RoutineCheckExpandListAdapter;
import com.jinsit.kmec.WO.WT.RI.RoutineCheckListData;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;
/**
 * 고객정보 정기점검표

 * @author 원성민
 *
 */
public class IR_CI03_R02 extends FragmentActivity {

	Context context;
	ViewPager vp_wo_checkTable;
	int pagerCount;
	String empId, workDt, bldgNm, bldgNo, carNo,refContrNo;
	private boolean isCI = false;

	private ProgressDialog ProgressDialog;
	private PagerAdapter mPagerAdapter;
	RoutineCheckListData mData;
	private ArrayList<PartCheckListData> partCheckListData;
	private ArrayList<String> groupData;

	private EasyJsonList ejl;
	private IR_CI03_R02_ExpandListAdapter iR_CI03_R02_ExpandListAdapter = null;
	private RoutineCheckExpandListAdapter routineCheckExpandListAdapter = null;
	private ExpandableListView lv_wo_checkTableListParent;

	private String selTp="1";
	private String nowCarNo="";
	private boolean isFirst=true;
	ArrayList<String> car ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo_wt00_r03_pager);
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("정기점검표");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		init();
		activityInit();
		checkTableDown();

	}
	private void init() {
		// TODO Auto-generated method stub
		context = this;
		empId = getIntent().getExtras().getString("empId");
		workDt = getIntent().getExtras().getString("workDt");
		carNo = getIntent().getExtras().getString("carNo");
		bldgNm = getIntent().getExtras().getString("bldgNm");
		bldgNo = getIntent().getExtras().getString("bldgNo");
		isCI = getIntent().getExtras().getBoolean("isCI", false);
	}
	private void activityInit() {
		// TODO Auto-generated method stub

		LayoutInflater li = null;
		li = LayoutInflater.from(context);
		LinearLayout linear = (LinearLayout) li.inflate(R.layout.wt_ri02_r01_listitem_header, null);
		TextView headTitle = (TextView)linear.findViewById(R.id.tv_ri02_r01HeadTitle);
		TextView subTitle = (TextView)linear.findViewById(R.id.tv_ri02_r01SubTitle);
		headTitle.setText(bldgNm );
		subTitle.setText(carNo + "호기" );

		/**
		 * 20150121 특기사항/부품조회내역이 쓸모가 없다.
		 * 저장되는곳도 없기 때문에 일단 주석처리함
		 *
		 */
		LayoutInflater li2 = null;
		li2 = LayoutInflater.from(context);
		LinearLayout linear2 = (LinearLayout) li2.inflate(R.layout.wt_ri02_r01_listitem_footer, null);

		LinearLayout ll_wt_ri_comment = (LinearLayout)linear2.findViewById(R.id.ll_wt_ri_comment);
		ll_wt_ri_comment.setVisibility(View.GONE);
		lv_wt_ri_rmk = (ListView)linear2.findViewById(R.id.lv_wt_ri_rmk);

		//etc = (TextView)linear2.findViewById(R.id.tv_wt_ri_etc);
		//etc.setOnClickListener(this);
		lv_wo_checkTableListParent = (ExpandableListView)findViewById(R.id.lv_wo_checkTableList);
		lv_wo_checkTableListParent.addHeaderView(linear);
		lv_wo_checkTableListParent.addFooterView(linear2);

		//lv_wo_checkTableListParent.setOnItemClickListener(this);

	}


	private void checkTableDown() {
		// TODO Auto-generated method stub
		new RoutineCheckListAsync().execute();
	}


	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {


		@Override
		protected void onPostExecute(Void result) {
			IR_CI03_R02.this.ProgressDialog.dismiss();


			PartCheckItemGroupList group = new PartCheckItemGroupList(partCheckListData);
			groupData = group.getGroupList();
			//그룹데이터 초기화

			ArrayList<Object> obj = new ArrayList<Object>();
			for (int i = 0; i < groupData.size(); i++) {
				ArrayList<PartCheckListData> clsd = new ArrayList<PartCheckListData>();
				for (int j = 0; j < partCheckListData.size(); j++) {

					if (groupData.get(i).equals(partCheckListData.get(j).getNFC_PLC_NM())) {
						clsd.add(partCheckListData.get(j));
						// clsd.add(object)
					}
				}
				obj.add(clsd);

			}

			ArrayList<PartCheckTableItem> mappingItem = new ArrayList<PartCheckTableItem>();
			for (int i = 0; i < groupData.size(); i++) {
				mappingItem.add(new PartCheckTableItem(groupData.get(i),
						(ArrayList<PartCheckListData>)obj.get(i)));
			}



			if(isCI){
				//고객정보에서 왔으면 ABC, OX 값 표시
				routineCheckExpandListAdapter = new RoutineCheckExpandListAdapter(context, mappingItem);
				lv_wo_checkTableListParent.setAdapter(routineCheckExpandListAdapter);
			}else{
				//점검계획에서 왔으면 수치, 상태, 유무 등 텍스트로 표시
				iR_CI03_R02_ExpandListAdapter = new IR_CI03_R02_ExpandListAdapter(context, mappingItem);
				lv_wo_checkTableListParent.setAdapter(iR_CI03_R02_ExpandListAdapter);
			}

			getRmk();


		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			IR_CI03_R02.this.ProgressDialog = android.app.ProgressDialog
					.show(context, "점검항목", "점검항목 조회중");
		}

		@Override
		protected Void doInBackground(Void... params) {


			partCheckListData = new ArrayList<PartCheckListData>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRoutineCheckTable.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();

			arguments.add(new BasicNameValuePair("empId", empId));
			arguments.add(new BasicNameValuePair("workDt", workDt));
			arguments.add(new BasicNameValuePair("bldgNo", bldgNo));
			arguments.add(new BasicNameValuePair("carNo", carNo));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				partCheckListData.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					partCheckListData.add(new PartCheckListData(ejl.getValue(i,
							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"),
							ejl.getValue(i, "JOB_NO"),
							ejl.getValue(i, "NFC_PLC"),
							ejl.getValue(i, "CS_ITEM_CD"),
							ejl.getValue(i, "SMART_DESC"),
							ejl.getValue(i, "CS_TOOLS"),
							ejl.getValue(i, "STD_ST"),
							ejl.getValue(i, "INPUT_TP"),
							ejl.getValue(i, "INPUT_TP1"),
							ejl.getValue(i, "INPUT_TP3"),
							ejl.getValue(i, "INPUT_TP7"),
							ejl.getValue(i, "INPUT_RMK"),
							ejl.getValue(i, "OVER_MONTH"),
							ejl.getValue(i, "MONTH_CHK_IF"),
							ejl.getValue(i, "MONTH_CHK"),
							ejl.getValue(i, "BLDG_NO"),
							ejl.getValue(i, "BLDG_NM"),
							ejl.getValue(i, "CAR_NO"),
							ejl.getValue(i, "CAR_NO_TO"),
							ejl.getValue(i,"NFC_PLC_NM"),
							ejl.getValue(i, "HEADER_IF")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}
	JinSimpleArrayAdapter adapter;
	ArrayList<String> rmkList;
	ListView lv_wt_ri_rmk;
	void getRmk(){
		rmkList = new ArrayList<String>();
		for (int j = 0; j < partCheckListData.size(); j++) {
			if(!partCheckListData.get(j).getINPUT_RMK().equals("")){
				rmkList.add(partCheckListData.get(j).getINPUT_RMK());
			}

		}

		adapter = new JinSimpleArrayAdapter(context,  R.layout.jin_simple_listitem, rmkList);
		lv_wt_ri_rmk.setAdapter(adapter);
		listViewHeightSet(adapter,lv_wt_ri_rmk);
	}
	private void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
