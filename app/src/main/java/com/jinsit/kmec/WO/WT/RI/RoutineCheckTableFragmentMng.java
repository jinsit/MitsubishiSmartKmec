package com.jinsit.kmec.WO.WT.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.TableTCSQ213;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class RoutineCheckTableFragmentMng extends Fragment implements
		OnItemClickListener, OnClickListener{
	CommentListener mCallback;
	public interface CommentListener{
		public void comment(String comment);
		public String getComment();
	}
	
	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;
	private ArrayList<PartCheckListDataMng> partCheckListDataMng;
	TextView etc;
	ListView lv_wt_ri_rmk;
	private ArrayList<TableTCSQ213> tableTCSQ213;
	private ArrayList<WT_RI02_R00_ITEM00> wT_RI02_R00_ITEM00;
	
	ArrayList<String> groupData;
	
	private EasyJsonList ejl;
	//ArrayList<RoutineCheckListData> routineCheckListData;
	private RoutineCheckExpandListAdapterMng routineCheckExpandListAdapter = null;
	private WT_RI02_R01_ExAdapter wT_RI02_R01_ExAdapter = null;
	private ExpandableListView lv_wo_checkTableListParent;
	
	private String selTp="1";
	
	CommonSession commonSession;
	CommentPreference commentPref;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		
		commonSession = new CommonSession(activity);
		commentPref = new CommentPreference(activity);
		  // container activity 가 interface 를 구현하였는지 확인한다.
        //  그렇지 않다면 exception 을 발생시킨다.
//        try {
//            mCallback = (CommentListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                + " must implement OnHeadlineSelectedListener");
//        }
		
	}

	private int mPageNumber;
	private String buildingNm,buildingNumber, carNumber,refControlNumber;
	private ArrayList<String> arrCar;
	public static RoutineCheckTableFragmentMng create(int pageNumber, String bldgNm, 
			String bldgNo, String refContrNo,ArrayList<String> car, String comment) {
		RoutineCheckTableFragmentMng fragment = new RoutineCheckTableFragmentMng();
		Bundle args = new Bundle();
		args.putInt("page", pageNumber);
		args.putString("bldgNm", bldgNm);
		args.putString("bldgNo", bldgNo);
		args.putString("refContrNo", refContrNo);
		args.putStringArrayList("arrCar", car);
		args.putString("comment", comment);
		
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//etc.setText(engComment);
		Log.i("onresume", "onresume");
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mCallback.getComment();
//		etc.setText(mCallback.getComment());
		Log.w("onPause", "onPause");
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("workTarget: ", key);
			}
		}
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt("page");
		buildingNm = getArguments().getString("bldgNm");
		buildingNumber = getArguments().getString("bldgNo");
		refControlNumber= getArguments().getString("refContrNo");
		arrCar = getArguments().getStringArrayList("arrCar");
		engComment= getArguments().getString("comment");
		carNumber = arrCar.get(mPageNumber);
		//carNumber = getCarNo();
	
	}
	String getCarNo(){
		int count = mPageNumber +1;
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = "select CAR_NO from WORK_TBL where BLDG_NO ='" + buildingNumber +"'";
//						 + " and REF_CONTR_NO = '" + refControlNumber +"'";
					Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
			String carNo = 	mCursor.getString(0);
	
		
		return carNo;
		
	}
	
	void checkTableData(){
		
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(activity,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				String query = new DatabaseRawQuery().selectCheckTableConfirm
						(commonSession.getEmpId(), commonSession.getWorkDt(),"1",buildingNumber,carNumber);
					Cursor mCursor = db.rawQuery(query,null); // 쿼리 날리고 
				mCursor.moveToFirst();
			tableDataSetting(mCursor);
	}
	
	
	void tableDataSetting(Cursor cursor){
		
		wT_RI02_R00_ITEM00 = new ArrayList<WT_RI02_R00_ITEM00>();
		WT_RI02_R00_ITEM00 mWT_RI02_R00_ITEM00 = new WT_RI02_R00_ITEM00();
		Field[] fields = mWT_RI02_R00_ITEM00.getClass().getDeclaredFields();
		String name = "";
		String value = "";
		
		WT_RI02_R00_ITEM00 insertTableItem= new WT_RI02_R00_ITEM00();
		if(cursor !=null&&cursor.getCount()!=0){
		do{
			
			insertTableItem = new WT_RI02_R00_ITEM00();
			for (Field field : fields) {
				name = field.getName();
				try {
					
					value = cursor.getString(cursor.getColumnIndex(name));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					field.set(insertTableItem, value);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			wT_RI02_R00_ITEM00.add(insertTableItem);
		}while(cursor.moveToNext());
		
	}else{
	}
		
		groupChildeView();
	}
	
	
	void groupChildeView(){
		WT_RI02_R00_ITEM_Group group = new WT_RI02_R00_ITEM_Group(wT_RI02_R00_ITEM00);
		groupData = group.getGroupList();
		//그룹데이터 초기화
		
		ArrayList<Object> obj = new ArrayList<Object>();
		for (int i = 0; i < groupData.size(); i++) {
			ArrayList<WT_RI02_R00_ITEM00> clsd = new ArrayList<WT_RI02_R00_ITEM00>();
			for (int j = 0; j < wT_RI02_R00_ITEM00.size(); j++) {

				if (groupData.get(i).equals(wT_RI02_R00_ITEM00.get(j).getNFC_PLC_NM())) {
					clsd.add(wT_RI02_R00_ITEM00.get(j));
					// clsd.add(object)
				}
			}
			obj.add(clsd);

		}
		
		ArrayList<WT_RI02_R00_ITEM_Table> mappingItem = new ArrayList<WT_RI02_R00_ITEM_Table>();
		for (int i = 0; i < groupData.size(); i++) {
			mappingItem.add(new WT_RI02_R00_ITEM_Table(groupData.get(i),
					(ArrayList<WT_RI02_R00_ITEM00>)obj.get(i)));
		}
		
		
		wT_RI02_R01_ExAdapter = new WT_RI02_R01_ExAdapter(activity,
				mappingItem);
		
		lv_wo_checkTableListParent.setAdapter(wT_RI02_R01_ExAdapter);
		
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.wo_wt00_r03_pager, container,
				false);
	
		onCreateViewInit(view);
		return view;

	}
	
	JinSimpleArrayAdapter adapter;
	private void onCreateViewInit(View view) {
		// TODO Auto-generated method stub
		LayoutInflater li = null;
		li = LayoutInflater.from(activity);
		 LinearLayout linear = (LinearLayout) li.inflate(R.layout.wt_ri02_r01_listitem_header, null);
		 TextView headTitle = (TextView)linear.findViewById(R.id.tv_ri02_r01HeadTitle);
		 TextView subTitle = (TextView)linear.findViewById(R.id.tv_ri02_r01SubTitle);
		// int carNo = mPageNumber+1;
		 headTitle.setText(buildingNm );
		 subTitle.setText(carNumber + "호기" );
		 
		 /**
		  * 20150121 특기사항/부품조회내역이 쓸모가 없다. 
		  * 저장되는곳도 없기 때문에 일단 주석처리함
		  * 
		  */
		 LayoutInflater li2 = null;
		 li2 = LayoutInflater.from(activity);
		LinearLayout linear2 = (LinearLayout) li2.inflate(R.layout.wt_ri02_r01_listitem_footer, null);
		 etc = (TextView)linear2.findViewById(R.id.tv_wt_ri_comment);
		 etc.setHint("점검자 의견을 보려면 클릭하세요");
		 //etc.setText(engComment);
		 lv_wt_ri_rmk = (ListView)linear2.findViewById(R.id.lv_wt_ri_rmk);
		 
		etc.setOnClickListener(this);
		lv_wo_checkTableListParent = (ExpandableListView) view
				.findViewById(R.id.lv_wo_checkTableList);
		lv_wo_checkTableListParent.addHeaderView(linear);
		lv_wo_checkTableListParent.addFooterView(linear2);
		
		lv_wo_checkTableListParent.setOnItemClickListener(this);
		
		//checkTableData();
		//groupData = new ArrayList<String>();
//		routineCheckExpandListAdapter = new RoutineCheckExpandListAdapter(activity,
//				mappingItem);
//		
//		lv_wo_checkTableListParent.setAdapter(routineCheckExpandListAdapter);
		
		
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
		new MngRoutineCheckListAsync().execute();
		// new ProgressDlgTest(activity).execute(100);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
	}

	private class MngRoutineCheckListAsync extends AsyncTask<Void, String, Void> {
		
		
		@Override
		protected void onPostExecute(Void result) {
			RoutineCheckTableFragmentMng.this.ProgressDialog.dismiss();
			getRmk();
			PartCheckItemGroupListMng group = new PartCheckItemGroupListMng(partCheckListDataMng);
			groupData = group.getGroupList();
			
			PartCheckItemChildListMng child = new PartCheckItemChildListMng(partCheckListDataMng);
			ArrayList<CopyPartCheckListDataMng> childData = child.getChild();
			
			//그룹데이터 초기화
			
			ArrayList<Object> obj = new ArrayList<Object>();
			for (int i = 0; i < groupData.size(); i++) {
				ArrayList<PartCheckListDataMng> clsd = new ArrayList<PartCheckListDataMng>();
				for (int j = 0; j < partCheckListDataMng.size(); j++) {

					if (groupData.get(i).equals(partCheckListDataMng.get(j).getNFC_PLC_NM())) {
						clsd.add(partCheckListDataMng.get(j));
						// clsd.add(object)
					}
				}
				obj.add(clsd);

			}
//			for(int i=0;i<partCheckListDataMng.size();i++){
//				
//				for(int j=0;j<childData.size();j++){
//					
//					if(partCheckListDataMng.get(i).getEL_INFO_MAP().equals(childData.get(j).getEL_INFO_MAP())){
//						childData.get(j).appendRmk(partCheckListDataMng.get(i).getINPUT_RMK());
//					}
//			}
//			//childData.get(i).setINPUT_RMK("input");
//		}
			
			ArrayList<Object> obj2 = new ArrayList<Object>();
			for (int i = 0; i < groupData.size(); i++) {
				ArrayList<PartCheckListDataMng> clsd = new ArrayList<PartCheckListDataMng>();
				for (int j = 0; j < childData.size(); j++) {

					if (groupData.get(i).equals(childData.get(j).getNFC_PLC_NM())) {
						clsd.add(addRmk(childData.get(j)));
					}
				}
				obj2.add(clsd);

			}
			
			ArrayList<PartCheckTableItemMng> mappingItem = new ArrayList<PartCheckTableItemMng>();
			for (int i = 0; i < groupData.size(); i++) {
				mappingItem.add(new PartCheckTableItemMng(groupData.get(i),
						(ArrayList<PartCheckListDataMng>)obj.get(i)));
			}

			ArrayList<PartCheckTableItemMng> mappingItem2 = new ArrayList<PartCheckTableItemMng>();
			for (int i = 0; i < groupData.size(); i++) {
				mappingItem2.add(new PartCheckTableItemMng(groupData.get(i),
						(ArrayList<PartCheckListDataMng>)obj2.get(i)));
			}
			
			
			routineCheckExpandListAdapter = new RoutineCheckExpandListAdapterMng(activity,
					mappingItem,mappingItem2);
			
			lv_wo_checkTableListParent.setAdapter(routineCheckExpandListAdapter);
			
			//getRmk();
		

		}
		ArrayList<String> rmkList;
		void getRmk(){
			rmkList = new ArrayList<String>();
			for (int j = 0; j < partCheckListDataMng.size(); j++) {
				if(!partCheckListDataMng.get(j).getINPUT_RMK().equals("")){
					rmkList.add(partCheckListDataMng.get(j).getINPUT_RMK());
				}
				
			}
			
			adapter = new JinSimpleArrayAdapter(activity,  R.layout.jin_simple_listitem, rmkList);
			lv_wt_ri_rmk.setAdapter(adapter);
			listViewHeightSet(adapter,lv_wt_ri_rmk);
		}
		
		private PartCheckListDataMng addRmk (CopyPartCheckListDataMng data){
			for(int i=0;i<partCheckListDataMng.size();i++){
				if(partCheckListDataMng.get(i).getEL_INFO_MAP().equals(data.getEL_INFO_MAP())){
					//Default벨류 더해서 0이상이면~~
						if(!partCheckListDataMng.get(i).getDEF_VAL_ST().equals(""))
						data.addDefaultValue(Integer.valueOf(partCheckListDataMng.get(i).getDEF_VAL_ST()));
						//data.addDefaultValue(1);
						//비고값있는것들 따로 어펜드
						if(!partCheckListDataMng.get(i).getINPUT_RMK().equals("")||
								partCheckListDataMng.get(i).getINPUT_RMK().equals(data.getINPUT_RMK())){
							data.appendRmk(partCheckListDataMng.get(i).getINPUT_RMK());
							
						}
						
				}
		}
			return (PartCheckListDataMng)data;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			RoutineCheckTableFragmentMng.this.ProgressDialog = android.app.ProgressDialog
					.show(activity, "점검항목", "점검항목 조회중");
		}

		@Override
		protected Void doInBackground(Void... params) {
		

			partCheckListDataMng = new ArrayList<PartCheckListDataMng>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRoutineCheckTableMng.do";
		
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
		
			//arguments.add(new BasicNameValuePair("selTp", selTp));
			arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
			arguments.add(new BasicNameValuePair("bldgNo", buildingNumber));
			arguments.add(new BasicNameValuePair("carNo", carNumber));
			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


			try {
				ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
				partCheckListDataMng.clear();
				int jsonSize = returnJson.getJSONArray("dataList").length();
				for (int i = 0; i < jsonSize; i++) {
					
					partCheckListDataMng.add(new PartCheckListDataMng(ejl.getValue(i,
							"CS_EMP_ID"), ejl.getValue(i, "WORK_DT"),
							ejl.getValue(i, "JOB_NO"),
							ejl.getValue(i, "BLDG_NO"),
							ejl.getValue(i, "BLDG_NM"),
							ejl.getValue(i, "CAR_NO"),
							ejl.getValue(i, "CAR_NO_TO"),
							ejl.getValue(i, "NFC_PLC"),
							ejl.getValue(i, "NFC_PLC_NM"),
							ejl.getValue(i, "CS_ITEM_CD"),
							ejl.getValue(i, "EL_INFO_MAP"),
							ejl.getValue(i, "MNG_DESC"),
							ejl.getValue(i, "INPUT_RMK"),
							ejl.getValue(i, "DEF_VAL_ST")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//	case R.id.tv_wt_ri_etc:
//		etcInput("특기사항을 입력하세요");
//		break;
		
	case R.id.tv_wt_ri_comment:
		
		//etc.setText(commentPref.getComment());
		etcInput("점검자의견을 입력하세요");
		
		break;
		
		}

	}
	/**
	 * 특기사항 입력 푸터가 빠지면서 일단은 사용하지 않는다
	 * 
	 */
	private String engComment;
	public void EngComment(String comment){
		engComment = comment;
	}
	
	EditText et;
	private void etcInput(String title) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout dial = (LinearLayout) inflater.inflate(
				R.layout.wt_mw00_r02_dialog_edittext, null);
		TextView dTitle = (TextView) dial
				.findViewById(R.id.dialTitle);
		dTitle.setText(title);
		 et = (EditText) dial
				.findViewById(R.id.et_mw_r02FailureInput);
		 et.setText(commentPref.getComment());
		  hd_etNumeric.sendEmptyMessageDelayed(0, 100);
		new AlertDialog.Builder(activity)
				.setView(dial)
				.setNegativeButton("취소",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								return;
							}
						})

				.setPositiveButton("확인",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String detailEtc = et.getText().toString();
								etc.setText(detailEtc);
								//mCallback.comment(detailEtc);
								commentPref.setComment(detailEtc);
								
								}
						}).show();
	}
	private Handler hd_etNumeric = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			InputMethodManager imm;
			imm = (InputMethodManager) activity.getSystemService("input_method");
			imm.showSoftInput(et, 0);
		}
	};
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
