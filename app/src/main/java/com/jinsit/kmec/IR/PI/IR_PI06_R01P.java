package com.jinsit.kmec.IR.PI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.CM.CM_SearchElev;
import com.jinsit.kmec.DM.DM.DM_DM01_R01P;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.SwipeDismissListViewTouchListener;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IR_PI06_R01P extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener {
	public IR_PI06_R01P(Context c, String bldgNo, Activity activity) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
		this.bldgNo = bldgNo;
		this.activity = activity;
	}
	private android.app.ProgressDialog ProgressDialog;
	ArrayList<IR_PI06_R00_Item> iR_PI06_R00_Item;
	private EasyJsonList ejl;
	Button btn_cancel;
	TextView btn_popClose;
	private TextView btn_pi06_searchCarNo;
	private TextView btn_pi06_inquery;
	private EditText et_pi06_inqueryText;
	private TextView btn_pi06_savePartsRequest;

	ListView lv_pi06_partsListView;
	IR_PI06_R01P_Adapter iR_PI06_R01P_Adapter;
	Context context;
	private Activity activity;
	String bldgNo;
	String carNo;
	String searchImageString;

	ArrayList<IR_PI06_R01P_Item> iR_PI06_R01P_Items = new ArrayList<>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ir_pi06_r01p);
		TextView title = (TextView)findViewById(R.id.tv01_popTitle);
		title.setText("청구품목");
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setOnClickListener(this);
		lv_pi06_partsListView = (ListView) findViewById(R.id.lv_pi06_partsListView);
		lv_pi06_partsListView.setOnItemClickListener(this);

		btn_pi06_searchCarNo = (TextView) findViewById(R.id.btn_pi06_searchCarNo);
		btn_pi06_searchCarNo.setOnClickListener(this);
		btn_pi06_inquery = (TextView) findViewById(R.id.btn_pi06_inquery);
		btn_pi06_inquery.setOnClickListener(this);
		et_pi06_inqueryText = (EditText) findViewById(R.id.et_pi06_inqueryText);
		btn_pi06_savePartsRequest = (TextView) findViewById(R.id.btn_pi06_savePartsRequest);
		btn_pi06_savePartsRequest.setOnClickListener(this);

		iR_PI06_R01P_Adapter = new IR_PI06_R01P_Adapter(context, iR_PI06_R01P_Items, this);
		lv_pi06_partsListView.setAdapter(iR_PI06_R01P_Adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_cancel:
				dismiss();
				break;
			case R.id.btn_popClose:
			this.iR_PI06_R01P_Items = null;
			dismiss();
			break;

			case R.id.btn_pi06_searchCarNo:
				searchElev();
				break;
			case R.id.btn_pi06_inquery:

				if(btn_pi06_searchCarNo.getText().toString() == null || btn_pi06_searchCarNo.getText().toString().equals("")){
					AlertView.showAlert("호기를 선택하십시오.", context);
					return;
				}

				if(et_pi06_inqueryText.getText().toString() == null || et_pi06_inqueryText.getText().toString().equals("")){
					AlertView.showAlert("아이템명, 아이템번호, 규격 중 하나를 입력하십시오.", context);
					return;
				}
				selectParts();
				break;
			case R.id.btn_pi06_savePartsRequest:
				//등록하시겠습니까?> 얼러트
				setiR_PI06_R01P_Items(iR_PI06_R01P_Items);
				dismiss();
				break;

			case R.id.iv_pi06_partsImage:
				ImageView button = (ImageView) v;
				String imageYN = button.getTag(R.id.tag_ir_pi06_image_yn).toString();
				searchImageString = button.getTag(R.id.tag_ir_pi06_image_no).toString();
				if ("Y".equals(imageYN)) {
					//이미지가 있으면 이미지 보여주기
					progress(true);
					new PartsImageSearch().execute();
				}
				break;
		}
	}

	public ArrayList<IR_PI06_R01P_Item> getiR_PI06_R01P_Items() {
		return iR_PI06_R01P_Items;
	}

	public void setiR_PI06_R01P_Items(ArrayList<IR_PI06_R01P_Item> iR_PI06_R01P_Items) {
		this.iR_PI06_R01P_Items = iR_PI06_R01P_Items;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		btn_pi06_searchCarNo.setText(carNo);
		this.carNo = carNo;
	}

	private void searchElev() {
		if (this.bldgNo == "" || this.bldgNo == null) {
			AlertView.showAlert("건물명을 먼저 조회 하세요", context);
			return;
		}
    	final CM_SearchElev elev = new CM_SearchElev(context, bldgNo);
		elev.show();
		elev.elevSearch();
		elev.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				String no = elev.getReturnStr();
				if(no == null || no.equals("")){

				}else{
					setCarNo(no);
				}
			}
		});
	}
	private void selectParts(){
		final IR_PI06_R02P iR_PI06_R02P = new IR_PI06_R02P(context, et_pi06_inqueryText.getText().toString(), activity);
		iR_PI06_R02P.show();
		iR_PI06_R02P.requestPartsItemSearch();
		iR_PI06_R02P.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
//				if(iR_PI06_R02P.getCurrentItem() == null)return;
//				IR_PI06_R01P_Item item = iR_PI06_R02P.getCurrentItem();
//				iR_PI06_R01P_Items.add(item);
				for (int i = 0; i < iR_PI06_R02P.iR_PI06_R02P_Items.size(); i++) {
					IR_PI06_R01P_Item item = iR_PI06_R02P.iR_PI06_R02P_Items.get(i);
					iR_PI06_R01P_Items.add(item);
				}
				iR_PI06_R01P_Adapter.notifyDataSetChanged();
			}

		});
		SwipeDismissListViewTouchListener touchListener =
				new SwipeDismissListViewTouchListener(lv_pi06_partsListView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
			@Override
			public boolean canDismiss(int position) {
				return true;
			}

			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions) {
				for (int position : reverseSortedPositions) {
					iR_PI06_R01P_Items.remove(iR_PI06_R01P_Items.get(position));
				}
				iR_PI06_R01P_Adapter.notifyDataSetChanged();
			}
		});
		lv_pi06_partsListView.setOnTouchListener(touchListener);
		lv_pi06_partsListView.setOnScrollListener(touchListener.makeScrollListener());
	}

	public class PartsImageSearch extends AsyncTask<Void, Void, Void> {

		private String partsImage;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl() + "ir/selectItemImage.do";

				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("itemNo", searchImageString));        //선택된 ItemNo를 가져와야 한다.
				//arguments.add(new BasicNameValuePair("tp", "2"));				//상품검색, 2는  사진
				JSONObject returnJson = getHttp.getPost(param_url, arguments, true);


				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						try {
							partsImage = ejl.getValue(0, "IMG1");
						} catch (JSONException e) {
							e.printStackTrace();
						}
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
			ProgressDialog.dismiss();
			try {
				Bitmap bm = DataConvertor.Base64Bitmap(partsImage);
				IR_PI04_R01P ir01 = new IR_PI04_R01P(context, bm);
				ir01.show();
			} catch (Exception ex) {
				Toast.makeText(context, "이미지 불러오기를 실패했습니다.", Toast.LENGTH_SHORT)
						.show();
			}

		}
	}


	private void progress(Boolean isActivated){
		if(isActivated){
			this.ProgressDialog =
					android.app.ProgressDialog.show(getContext(), "알림","조회중 입니다.");
		}else{
			this.ProgressDialog.dismiss();
		}
	}
	public void requestPopupSearch() {
		// TODO Auto-generated method stub
		progress(true);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		//dismiss();
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		//this.setDeptCd(deptCd);
		//this.setDeptNm(deptNm);

	}
}