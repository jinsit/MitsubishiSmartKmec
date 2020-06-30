package com.jinsit.kmec.SM.CR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.IntentActionUtils;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class SM_CR02_R02P extends AlertDialog
		implements android.view.View.OnClickListener{

	//uiInstances

	private Context context;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	//http
	private JSONObject returnJson;
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;

	//parameter
	private Map<String, String> paraMap;

	//utils
	private ProgressDialog progress;


	protected SM_CR02_R02P(Context context, Map paraMap) {
		super(context);
		this.context = context;
		this.paraMap  = paraMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cr02_r02p);
		getInstances();
		setEvents();
		setConfig();
	}

	private void getInstances(){

		tv01_popTitle 				= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose 				= (TextView) findViewById(R.id.btn_popClose);
	}
	private void setEvents(){
		btn_popClose.setOnClickListener(this);
	}
	private void setConfig(){
		tv01_popTitle.setText("사진보기");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}
	}


	private class Database extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}

	private void crud(String div){

		GetHttp http = null;
		String argUrl = "";

		if(div.equals("selectPhotosForClaim")){
			http = new GetHttp();
			argUrl = WebServerInfo.getUrl()+"sm/"+div+".do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("ngNo", paraMap.get("ngNo") ));
			arguments.add(new BasicNameValuePair("ngSr", paraMap.get("ngSr") ));
//				arguments.add(new BasicNameValuePair("ngNo", "C100129-002" ));
//				arguments.add(new BasicNameValuePair("ngSr", "001" ));

			returnJson = http.getPost(argUrl, arguments, true);

			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("msgMap") );
				ejl01 = new EasyJsonList( returnJson.getJSONArray("dataList") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void setData(String div){

		Bitmap bm = null;
		try {
			bm = DataConvertor.Base64Bitmap(ejl01.getValue(0, "IMG1"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* for test
		Integer[] mThumbIds = {
		        R.drawable.test, R.drawable.test,
		        R.drawable.test, R.drawable.test,
		        R.drawable.test
		};
		*/

		LinearLayout layout = (LinearLayout)findViewById(R.id.sm_cr02_mainLayout);
		int imgSize = Integer.parseInt( paraMap.get("imgCnt") );
		for(int i=0;i<imgSize;i++){

			final ImageView iv = new ImageView(getContext());
//			iv.setLayoutParams(new TableLayout.LayoutParams(
//			TableLayout.LayoutParams.WRAP_CONTENT,
//			TableLayout.LayoutParams.WRAP_CONTENT));
			//마진 주기위해 변수생성해서 사용
			TableLayout.LayoutParams tLayout = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT,
					TableLayout.LayoutParams.WRAP_CONTENT);
			tLayout.topMargin = 10;
			iv.setLayoutParams(tLayout);
			iv.setMinimumHeight(768);
			iv.setMinimumWidth(1024);

			try{
				iv.setImageBitmap( DataConvertor.Base64Bitmap( ejl01.getValue(i, "IMG1") ) );
				/*iv.setImageResource(mThumbIds[i]);*/
			}catch(Exception e){
				Log.e("[개발자Msg]", "Image생성실패");
			}
			layout.addView(iv);
			//일단 클릭시 확대사진은 보류
//			iv.setOnClickListener(new android.view.View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Uri uri = getImageUri(context, getImageViewBitmap(iv));
//					iamgeCKUri(context,uri);
//				}
//			});
		}
		progress(false);
	}

	public void sch(){
		progress(true);
		new Database().execute("selectPhotosForClaim");
	}

	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			SM_CR02_R02P.this.progress =
					android.app.ProgressDialog.show(getContext(), "알림","사진을 불러오고 있습니다...");
		}else{
			SM_CR02_R02P.this.progress.dismiss();
		}
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {

		File file = new File(AbsoluteFilePath.PDF_PATH + "picture.jpg");
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inImage.compress(CompressFormat.PNG, 100, outStream);

		Uri uri = Uri.parse(AbsoluteFilePath.PDF_PATH + "picture.jpg");
		return uri;
	}


	public  static void iamgeCKUri(Context context, Uri uri) {
		// TODO Auto-generated method stub

		IntentActionUtils.actionImage(context, uri);
	}

	private Bitmap getImageViewBitmap(ImageView iv){
		BitmapDrawable d = (BitmapDrawable)iv.getDrawable();
		Bitmap b = d.getBitmap();
		return b;
	}

};
