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
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;
import com.jinsit.kmec.DB.TableTCSQ213;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class RoutineCheckTableFragment extends Fragment implements
		OnItemClickListener, OnClickListener {

	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;
	private ArrayList<PartCheckListData> partCheckListData;
	TextView etc;
	private ArrayList<TableTCSQ213> tableTCSQ213;
	private ArrayList<WT_RI02_R00_ITEM00> wT_RI02_R00_ITEM00;
	
	ArrayList<String> groupData;
	
	private EasyJsonList ejl;
	//ArrayList<RoutineCheckListData> routineCheckListData;
	///private RoutineCheckExpandListAdapter routineCheckExpandListAdapter = null;
	private WT_RI02_R01_ExAdapter wT_RI02_R01_ExAdapter = null;
	private ExpandableListView lv_wo_checkTableListParent;
	
	private String selTp="1";
	
	CommonSession commonSession;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		commonSession = new CommonSession(activity);
	}

	private int mPageNumber;
	private String buildingNm,buildingNumber, carNumber,refControlNumber;
	private ArrayList<String> arrCar;
	public static RoutineCheckTableFragment create(int pageNumber, String bldgNm, 
			String bldgNo, String refContrNo,ArrayList<String> car) {
		RoutineCheckTableFragment fragment = new RoutineCheckTableFragment();
		Bundle args = new Bundle();
		args.putInt("page", pageNumber);
		args.putString("bldgNm", bldgNm);
		args.putString("bldgNo", bldgNo);
		args.putString("refContrNo", refContrNo);
		args.putStringArrayList("arrCar", car);
		fragment.setArguments(args);
		return fragment;
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
		 //etc = (TextView)linear2.findViewById(R.id.tv_wt_ri_etc);
		etc.setOnClickListener(this);
		lv_wo_checkTableListParent = (ExpandableListView) view
				.findViewById(R.id.lv_wo_checkTableList);
		lv_wo_checkTableListParent.addHeaderView(linear);
		lv_wo_checkTableListParent.addFooterView(linear2);
		
		lv_wo_checkTableListParent.setOnItemClickListener(this);
		
		checkTableData();
		//groupData = new ArrayList<String>();
//		routineCheckExpandListAdapter = new RoutineCheckExpandListAdapter(activity,
//				mappingItem);
//		
//		lv_wo_checkTableListParent.setAdapter(routineCheckExpandListAdapter);
		
		
		//initialize();

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
		new RoutineCheckListAsync().execute();
		// new ProgressDlgTest(activity).execute(100);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
	}

	private class RoutineCheckListAsync extends AsyncTask<Void, String, Void> {
		
		
		@Override
		protected void onPostExecute(Void result) {
			RoutineCheckTableFragment.this.ProgressDialog.dismiss();
			
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
//			ArrayList<PartCheckTableItem> mappingItem = new ArrayList<PartCheckTableItem>();
//			for (int i = 0; i < groupData.size(); i++) {
//				mappingItem.add(new PartCheckTableItem(groupData.get(i),
//						(ArrayList<PartCheckListData>)obj.get(i)));
//			}
//			
//			
//			routineCheckExpandListAdapter = new RoutineCheckExpandListAdapter(activity,
//					mappingItem);
//			
//			lv_wo_checkTableListParent.setAdapter(routineCheckExpandListAdapter);
//			
//			
			//new PartCheckListAsync().execute();
		

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			RoutineCheckTableFragment.this.ProgressDialog = android.app.ProgressDialog
					.show(activity, "점검항목", "점검항목 조회중");
		}

		@Override
		protected Void doInBackground(Void... params) {
		

			partCheckListData = new ArrayList<PartCheckListData>();
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()
					+ "ip/selectRoutineCheckTable.do";
		
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
		
			
			arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
			arguments.add(new BasicNameValuePair("bldgNo", buildingNumber));
			arguments.add(new BasicNameValuePair("carNo", carNumber));
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
							ejl.getValue(i,"NFC_PLC_NM")));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}

	public class ProgressDlgTest extends AsyncTask<Integer// excute()실행시 넘겨줄
															// 데이터타입
			, String// 진행정보 데이터 타입 publishProgress(), onProgressUpdate()의 인수
			, Integer// doInBackground() 종료시 리턴될 데이터 타입 onPostExecute()의 인수
			> {
		// ProgressDialog를 멤버로 하나 넣어줌
		private ProgressDialog mDlg;
		private Context mContext;

		public ProgressDlgTest(Context context) {
			mContext = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(mContext);
			mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDlg.setMessage("작업 시작");
			mDlg.show();

			super.onPreExecute();
		}

		// doInBackground 함수는 excute() 실행시 실행됨
		// 여기서 인수로는 작업개수를 넘겨주었다.
		@Override
		protected Integer doInBackground(Integer... params) {

			final int taskCnt = params[0];
			// 넘겨받은 작업개수를 ProgressDialog의 맥스값으로 세팅하기 위해 publishProgress()로 데이터를
			// 넘겨준다.
			// publishProgress()로 넘기면 onProgressUpdate()함수가 실행된다.
			publishProgress("max", Integer.toString(taskCnt));

			// 작업 진행, 여기선 넘겨준 작업개수 * 100 만큼 sleep() 걸어줌
			for (int i = 0; i < taskCnt; ++i) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// 작업 진행 마다 진행률을 갱신하기 위해 진행된 개수와 설명을 publishProgress() 로 넘겨줌.
				publishProgress("progress", Integer.toString(i), "작업 번호 "
						+ Integer.toString(i) + "번 수행중");
			}

			// 작업이 끝나고 작업된 개수를 리턴 . onPostExecute()함수의 인수가 됨
			return taskCnt;
		}

		// onProgressUpdate() 함수는 publishProgress() 함수로 넘겨준 데이터들을 받아옴
		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress[0].equals("progress")) {
				mDlg.setProgress(Integer.parseInt(progress[1]));
				mDlg.setMessage(progress[2]);
			} else if (progress[0].equals("max")) {
				mDlg.setMax(Integer.parseInt(progress[1]));
			}
		}

		// onPostExecute() 함수는 doInBackground() 함수가 종료되면 실행됨
		@Override
		protected void onPostExecute(Integer result) {
			mDlg.dismiss();
			Toast.makeText(mContext, Integer.toString(result) + "개의 작업 완료",
					Toast.LENGTH_SHORT).show();
		}
	}

	
	private class PartCheckListAsync extends AsyncTask<Void, String, Void> {

		@Override
		protected void onPostExecute(Void result) {
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				partCheckListData = new ArrayList<PartCheckListData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ip/selectPartCheckList.do";
			
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();

				arguments.add(new BasicNameValuePair("csEmpId", commonSession.getEmpId()));
				arguments.add(new BasicNameValuePair("workDt", commonSession.getWorkDt()));
				arguments.add(new BasicNameValuePair("jobNo", "1"));
				arguments.add(new BasicNameValuePair("nfcPlc", "10"));
				//arguments.add(new BasicNameValuePair("nfcPlc", String.valueOf(mPageNumber)));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);
				
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
								ejl.getValue(i, "CS_LOW_NM"),
								ejl.getValue(i, "SMART_DESC"),
								ejl.getValue(i, "MNG_DESC"),
								ejl.getValue(i, "CS_TOOLS"),
								ejl.getValue(i, "STD_ST"),
								ejl.getValue(i, "INPUT_TP"),
								ejl.getValue(i, "INPUT_TP1"),
								ejl.getValue(i, "INPUT_TP3"),
								ejl.getValue(i, "INPUT_TP7"),
								ejl.getValue(i, "INPUT_RMK"),
								ejl.getValue(i, "OVER_MONTH"),
								ejl.getValue(i, "MONTH_CHK_IF"),
								ejl.getValue(i,"MONTH_CHK")));
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
		switch (v.getId()) {
//	case R.id.tv_wt_ri_etc:
//		etcInput();
//		break;
		
		}

	}
	/**
	 * 특기사항 입력 푸터가 빠지면서 일단은 사용하지 않는다
	 * 
	 */
	EditText et;
	private void etcInput() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout dial = (LinearLayout) inflater.inflate(
				R.layout.wt_mw00_r02_dialog_edittext, null);
		TextView dTitle = (TextView) dial
				.findViewById(R.id.dialTitle);
		dTitle.setText("특기사항을 입력하세요.");
		 et = (EditText) dial
				.findViewById(R.id.et_mw_r02FailureInput);
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

}
