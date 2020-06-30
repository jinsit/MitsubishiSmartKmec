package com.jinsit.kmec.CM;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.PDFViewActivity;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyImageView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 고장수리
 */
public class CM_ReadPicture extends Dialog  implements OnClickListener, OnDismissListener{

	public CM_ReadPicture(Context context, String jobNo, String workDt, String selTp) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.jobNo = jobNo;
		this.workDt = workDt;
		if(selTp.equals("1")){
			filePath = AbsoluteFilePath.FTP_FOLDER_WOTS_PATH;
		}else{
			filePath = AbsoluteFilePath.FTP_FOLDER_WORJ_PATH;
		}
	}


	private Context context;
	private String jobNo;
	private String workDt;
	private String filePath;

	private LinearLayout lin_ts_picturePage;
	private TextView tv01_popTitle;
	private TextView btn_popClose;

	private ArrayList<EasyImageView> imageViewList;
	private int imageId;

	private ProgressDialog progress;
	private CommonSession commonSession;

	private List<CM_SaveReadPicture_ITEM01> itemList01;
	private EasyJsonMap ej01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cm_readpicture);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		tv01_popTitle.setText("작업전후 사진");
		imageViewList = new ArrayList<EasyImageView>();


		commonSession = new CommonSession(context);

		itemList01 = new ArrayList<CM_SaveReadPicture_ITEM01>();
		addPictureTable();



	}

	protected void getInstances() {
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		tv01_popTitle = (TextView)findViewById(R.id.tv01_popTitle);
		lin_ts_picturePage = (LinearLayout) findViewById(R.id.lin_ts_picturePage);
		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismiss();
				break;
			default:

				final ImageView imageView = (ImageView) v;
				imageId = Integer.valueOf(imageView.getTag().toString());
				if( imageViewList.get(imageId).isImage()){
					imageFullScreen();
				}else{
					AlertView.showAlert("이미지가 없습니다.", context);
				}
				break;
		}

	}



	public void inqueryImages(){
		progress(true);
		new selectPictures().execute("bagicWorkTime");
	}
	private void imageFullScreen(){
		progress(true);
		new viewImageFile().execute("bagicWorkTime");

	}


	public class viewImageFile extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					removeFiles();
					BitmapDrawable d = (BitmapDrawable)imageViewList.get(imageId).getDrawable();
					if(d != null){
						Bitmap bitmap = d.getBitmap();
						File file = new File(AbsoluteFilePath.TEMP_PATH+"temp.jpg");
						try{
							FileOutputStream out = new FileOutputStream(AbsoluteFilePath.TEMP_PATH+"temp.jpg");
							bitmap.compress(Bitmap.CompressFormat.JPEG,100, out);
						}
						catch(Exception ex){
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return params[0];
			}
			return "None";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {

				progress(false);
				try {
					Uri uri = Uri.parse(AbsoluteFilePath.TEMP_PATH+"temp.jpg");
					Intent intent = new Intent(context, PDFViewActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(uri);
					intent.putExtra("isPdf", false);
					context.startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{

			}
		}
	}// end of SelectData inner-class



	public void removeFiles()
	{
		File dir = new File(AbsoluteFilePath.PDF_PATH);
		String[] fileNames = dir.list();
		if (fileNames != null) {
			for (String fileName : fileNames) {
				File f = new File(AbsoluteFilePath.PDF_PATH + fileName);
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}


	/*
	 *
	 * Using the Intent data that the Gallery app returns, this method will
	 *
	 * retrive the filepath location of the image that the user have
	 *
	 * selected.
	 *
	 *
	 *
	 * Known issues: This does not work for getting images from Google Drive and
	 *
	 * Pisca.
	 */



	private View addPictureTable() {
		LinearLayout lin_ts_picture = new LinearLayout(context);
		LinearLayout.LayoutParams llinLayoutParam = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lin_ts_picture.setLayoutParams(llinLayoutParam);

		EasyImageView imageView01 = new EasyImageView(context);
		EasyImageView imageView02 = new EasyImageView(context);
		LinearLayout.LayoutParams llinLayoutParam1 = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

		llinLayoutParam1.leftMargin = 10;
		llinLayoutParam1.rightMargin = 5;

		imageView01.setLayoutParams(llinLayoutParam1);
		imageView01.setBackgroundResource(R.drawable.btn_nopicture);
		imageView01.setScaleType(ScaleType.FIT_XY);
		imageView01.setOnClickListener(this);
		imageView01.setTag(imageViewList.size());


		llinLayoutParam1.leftMargin = 5;
		llinLayoutParam1.rightMargin = 10;
		imageView02.setLayoutParams(llinLayoutParam1);
		imageView02.setBackgroundResource(R.drawable.btn_nopicture);
		imageView02.setScaleType(ScaleType.FIT_XY);
		imageView02.setOnClickListener(this);
		imageView02.setTag(imageViewList.size() + 1);
		lin_ts_picture.setBackgroundResource(R.drawable.bg_photo_area);
		lin_ts_picture.addView(imageView01);
		lin_ts_picture.addView(imageView02);
		imageViewList.add(imageView01);
		imageViewList.add(imageView02);
		lin_ts_picturePage.addView(lin_ts_picture);
		return lin_ts_picture;
	}




	private class selectPictures extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;



		@Override
		protected String  doInBackground(String... params) {
			// 1. bagicWorkTime

			if (params[0].equals("bagicWorkTime")) {
				try {

					String param_url = WebServerInfo.getUrl()
							+ "ip/selectPictures.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson01 = http.getPost(param_url, arguments,true);

					ej01 = new EasyJsonMap(returnJson01.getJSONObject("dataMap"));

				} catch (Exception ex) {
					return "Error";
				}
				return params[0];
			}
			return "None";

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// 1. bagicWorkTime
			if (result.equals("bagicWorkTime")) {
				//ej01.getValue("REF_CONTR_NO")
				itemList01 = new ArrayList<CM_SaveReadPicture_ITEM01>();
				try {
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","1",ej01.getValue("BEFORE_PIC_01")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","1",ej01.getValue("AFTER_PIC_01")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","2",ej01.getValue("BEFORE_PIC_02")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","2",ej01.getValue("AFTER_PIC_02")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","3",ej01.getValue("BEFORE_PIC_03")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","3",ej01.getValue("AFTER_PIC_03")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","4",ej01.getValue("BEFORE_PIC_04")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","4",ej01.getValue("AFTER_PIC_04")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","5",ej01.getValue("BEFORE_PIC_05")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","5",ej01.getValue("AFTER_PIC_05")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","6",ej01.getValue("BEFORE_PIC_06")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","6",ej01.getValue("AFTER_PIC_06")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","7",ej01.getValue("BEFORE_PIC_07")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","7",ej01.getValue("AFTER_PIC_07")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","8",ej01.getValue("BEFORE_PIC_08")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","8",ej01.getValue("AFTER_PIC_08")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","9",ej01.getValue("BEFORE_PIC_09")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","9",ej01.getValue("AFTER_PIC_09")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B","10",ej01.getValue("BEFORE_PIC_10")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A","10",ej01.getValue("AFTER_PIC_10")));
					progress(false);
					new selectImageFiles(context).execute(1);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progress(false);
				}
			}
			else{
				progress(false);
			}
		}
	}


	public class selectImageFiles extends AsyncTask<Integer, String, Integer> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		List<Bitmap> bitmapList;


		public int taskCnt;

		private ProgressDialog mDlg;
		private Context context;

		public selectImageFiles(Context context) {
			this.context = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(context);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDlg.setCancelable(false);
			mDlg.setMessage("이미지 다운로드 중");
			mDlg.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {

			int completeWorkCnt = 0;
			// 1. bagicWorkTime
			if (params[0].equals(1)) {
				try {

					for(CM_SaveReadPicture_ITEM01 item : itemList01){
						if(!item.getFileNm().equals("")){
							taskCnt++;
						}
					}
					publishProgress("max", Integer.toString(taskCnt));

					// 출력할 파일명과 읽어들일 파일명을지정한다.
					Bitmap bmp;
					int i = 0;
					bitmapList = new ArrayList<Bitmap>();
					for(CM_SaveReadPicture_ITEM01 item : itemList01){
						if(!item.getFileNm().equals("")){
							bmp = EasyDownLoad.getImage(filePath, item.getFileNm(), commonSession.getEmpId());
							bitmapList.add(bmp);
							completeWorkCnt++;
							publishProgress("progress", Integer.toString(i), Integer.toString(completeWorkCnt) + "번째 이미지 업로드");

						}
						else{
							bitmapList.add(null);
						}
						i++;
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				return params[0];
			}
			return 0;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			if (progress[0].equals("progress")) {
				mDlg.setProgress(Integer.parseInt(progress[1]));
				mDlg.setMessage(progress[2]);
			} else if (progress[0].equals("max")) {
				mDlg.setMax(Integer.parseInt(progress[1]));
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			mDlg.dismiss();
			// 1. bagicWorkTime
			if (result.equals(1)) {
				try {
					int i= 0;
					View view = null;
					for(Bitmap bmp : bitmapList){
						if(bmp != null){
							imageViewList.get(i).setImageBitmap(bmp);
							imageViewList.get(i).setIsImage(true);
							imageViewList.get(i).setBackground(null);
							if (imageViewList.size() - 2 <= i) {
								view = addPictureTable();
							}
						}
						i++;
					}
					if(view != null){
						lin_ts_picturePage.removeView(view);
					}else{
						lin_ts_picturePage.removeAllViews();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				bitmapList.clear();
			}
			else{
			}

		}
	}// end of SelectData inner-class



	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_ReadPicture.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_ReadPicture.this.progress.dismiss();
		}
	}
}
