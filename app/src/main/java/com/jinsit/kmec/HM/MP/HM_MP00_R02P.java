package com.jinsit.kmec.HM.MP;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.PreferenceUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 안전운전에 대한 팝업
 * @author 원성민
 *
 */
public class HM_MP00_R02P extends AlertDialog
		implements android.view.View.OnClickListener{

	//uiInstances
	private Context context;

	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private TextView tv_safeDriveRmk;
	private ImageView iv_saveDrive;
	private Button btn_safeDriveConfirm;

	//http
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private JSONObject returnJson;

	//utils

	private ProgressDialog progress;
	private List<HM_MP00_R02_Item01> itemList01;
	private CommonSession session;
	private String path;



	public HM_MP00_R02P(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hm_mp00_r02p);
		getInstances();
	}

	private void getInstances(){

		session				   = new CommonSession(context);
		itemList01			   = new ArrayList<HM_MP00_R02_Item01>();
		tv01_popTitle		   = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose	  	   = (TextView) findViewById(R.id.btn_popClose);
		btn_popClose.setVisibility(View.INVISIBLE);

		tv_safeDriveRmk = (TextView) findViewById(R.id.tv_safeDriveRmk);
		iv_saveDrive = (ImageView) findViewById(R.id.iv_saveDrive);
		btn_safeDriveConfirm = (Button) findViewById(R.id.btn_safeDriveConfirm);

		this.setCancelable(false);

		setEvents();
	}
	private void setEvents(){
		btn_safeDriveConfirm.setOnClickListener(this);
		setConfig();
	}

	private void setConfig(){
		tv01_popTitle.setText("안전운전 공지사항");
		new Database().execute();
	}

	private void setData(HM_MP00_R02_Item01 item, Bitmap bitmap){
		tv01_popTitle.setText(item.getRMK2());
		tv_safeDriveRmk.setText(item.getRMK());
		iv_saveDrive.setImageBitmap(bitmap);
		if(item.getS_NO() == null || item.getS_NO().equals("")){
			this.setSafeDriveNoticeCount(this.getSafeDriveNoticeCount());
			return;
		}
		this.setSafeDriveNoticeCount(Integer.valueOf(item.getS_NO()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.btn_safeDriveConfirm:
				dismiss();
				break;

			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}

	}


	private class Database extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			try{

				GetHttp http = new GetHttp();
				//String argUrl = "http://192.168.12.219:9999/kmecSvc/hm/selectSafeDrivingNotice.do";
				String argUrl = WebServerInfo.getUrl() + "/hm/selectSafeDrivingNotice.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.clear();
				arguments.add(new BasicNameValuePair("type", "1"));

				returnJson = http.getPost(argUrl, arguments, true);

				try {
					ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
					ejl01 = new EasyJsonList( returnJson.getJSONArray("dataList") );
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}

			}catch (Exception ex){
				return false;
			}
			return true;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){


				try {
					boolean isError =  ejm01.getValue("errCd").equals("1") ? true : false;
					if(!isError){

						itemList01.clear();
						int jsonSize = returnJson.getJSONArray("dataList").length();
						for(int i=0; i<jsonSize; i++){
							itemList01.add(new HM_MP00_R02_Item01(  ejl01.getValue(i, "S_NO")
									, ejl01.getValue(i, "FILE_NM"), ejl01.getValue(i, "RMK2"),
									ejl01.getValue(i, "RMK") ) );
						}


						HM_MP00_R02_Item01 currentItem = new HM_MP00_R02_Item01();
						int noticeCount = getSafeDriveNoticeCount();
						//프리퍼런스에 들어있는 안전관리 카운트보다 조회해온 데이터가 적다면 초기화 해줘야 함
						//하루에 한번씩 공지사항 뿌려주는데  한 싸이클 돌았다는 얘기임
						if(itemList01.size() < noticeCount )noticeCount = 1;

						for(HM_MP00_R02_Item01 item : itemList01){

							if(item.getS_NO().equals(String.valueOf(noticeCount))){
								currentItem = item;
								break;
							}
						}


						new selectImageFiles(currentItem).execute();

					}else if(isError){
						alert(ejm01.getValue("errMsg"), context);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}else{

			}

		}
	}



	public class selectImageFiles extends AsyncTask<Void, Void, Boolean> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		Bitmap bmp;
		HM_MP00_R02_Item01 currentItem;

		public selectImageFiles(HM_MP00_R02_Item01 item) {
			this.currentItem = item;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(context);
			progress.setCancelable(false);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setMessage("이미지 다운로드 중");
			progress.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				if(this.currentItem != null){

					path = this.currentItem.getFILE_NM();
					bmp = EasyDownLoad.getImage(AbsoluteFilePath.FTP_FOLDER_SAFEDRIVE_PATH, path, session.getEmpId());

				}else{

				}

			}catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}


		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			progress.dismiss();
			// 1. bagicWorkTime
			if (result)
			{
				setData(currentItem, bmp);
			}

			else
			{
			}

		}
	}// end of SelectData inner-class



	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

	private int getSafeDriveNoticeCount() {
		return PreferenceUtil.instance(context).getSafeDriveNoticeCount();
	}

	private void setSafeDriveNoticeCount(int count){
		PreferenceUtil.instance(context).putSafeDriveNoticeCount(count);
	}
};
