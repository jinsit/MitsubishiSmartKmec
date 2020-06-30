package com.jinsit.kmec.IR.NT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.BitmapToByteArray;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.ImageResize;
import com.jinsit.kmec.comm.jinLib.IntentActionUtils;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class IR_NT00_R01P extends AlertDialog  {
	Activity context;
	NoticeResponseData noticeData;
	ArrayList<NoticeResponseData> rlid;
	private ProgressDialog progress;
	private EasyJsonList ejl;
	String result;
	ImageView iv_noticeImg1, iv_noticeImg2;
	Bitmap grobalBitmap = null;
	private TextView btn_popClose;
	public IR_NT00_R01P(Context c, NoticeResponseData mData) {
		super(c);
		// TODO Auto-generated constructor stub
		context = (Activity) c;
		noticeData = mData;
	}

	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		super.setTitle(titleId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_detail);

		activityInitialize();

	}

	private void activityInitialize() {
		// TODO Auto-generated method stub
		TextView tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		tv01_popTitle.setText("공지내용");
		TextView tv_NoticeSubject = (TextView) findViewById(R.id.tv_noticeSubject);
		TextView tv_NoticeTime = (TextView) findViewById(R.id.tv_noticeTime);
		TextView tv_NoticeTo = (TextView) findViewById(R.id.tv_noticeTo);
		TextView tv_NoticeFrom = (TextView) findViewById(R.id.tv_noticeFrom);
		TextView tv_NoticeContent = (TextView) findViewById(R.id.tv_noticeContent);
		TextView btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		iv_noticeImg1 = (ImageView) findViewById(R.id.iv_noticeImg1);
		iv_noticeImg2 = (ImageView) findViewById(R.id.iv_noticeImg2);

		iv_noticeImg1.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Bitmap bm = 	StringToBitMap(rlid.get(0).getIMG1());
//				ImageDetailView r02p = new ImageDetailView(context, bm);
//				r02p.show();
				Uri uri = getImageUri(context, getImageViewBitmap(iv_noticeImg1));
				if(uri!=null)iamgeCKUri(context,uri);
			}
		});

		iv_noticeImg2.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Bitmap bm =	StringToBitMap(rlid.get(0).getIMG2());
//				ImageDetailView r02p = new ImageDetailView(context, bm);
//				r02p.show();
				Uri uri = getImageUri(context, getImageViewBitmap(iv_noticeImg2));
				if(uri!=null)iamgeCKUri(context,uri);
			}
		});

		btn_popClose.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		tv_NoticeSubject.setText(noticeData.getTITLE());
		tv_NoticeTime.setText(noticeData.getNOTICE_DT());
		tv_NoticeTo.setText(noticeData.getSENDER_NM());
		tv_NoticeFrom.setText(noticeData.getRECIPIENT_NM());
		tv_NoticeContent.setText(noticeData.getCONTENT());
	}

	public class NoticeImageTask extends AsyncTask<Void, Void, Void> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//IR_NT00_R01P.this.ProgressDialog = android.app.ProgressDialog
			// .show(context, "공지사항","불러오는중");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				rlid = new ArrayList<NoticeResponseData>();
				GetHttp getHttp = new GetHttp();
				String param_url = WebServerInfo.getUrl()
						+ "ir/selectImgOfNotice.do";
				List<NameValuePair> arguments = new ArrayList<NameValuePair>();
				arguments.add(new BasicNameValuePair("noticeDt", noticeData
						.getNOTICE_DT()));
				arguments.add(new BasicNameValuePair("noticeTm", noticeData
						.getNOTICE_TM()));
				JSONObject returnJson = getHttp.getPost(param_url, arguments,
						true);

				try {
					ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
					rlid.clear();
					int jsonSize = returnJson.getJSONArray("dataList").length();
					for (int i = 0; i < jsonSize; i++) {
						rlid.add(new NoticeResponseData(
								ejl.getValue(i, "IMG1"), ejl
								.getValue(i, "IMG2")));
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
			progress(false);
			//IR_NT00_R01P.this.ProgressDialog.dismiss();
			if (rlid.size() != 0) {
				if(rlid.get(0).getIMG1().equals("[]")&&rlid.get(0).getIMG2().equals("[]")){
					//이미지가 둘다 없으면 이미지영역을 없애버린다
					iv_noticeImg1.setBackground(null);
					iv_noticeImg1.setVisibility(View.GONE);
					iv_noticeImg2.setBackground(null);
					iv_noticeImg2.setVisibility(View.GONE);
					return;
				}

				if (!rlid.get(0).getIMG1().equals("")){

					//iv_noticeImg1.setImageBitmap(ImageResize.thumbnailResize(StringToBitMap(rlid.get(0).getIMG1())));
					iv_noticeImg1.setImageBitmap((StringToBitMap(rlid.get(0).getIMG1())));
					iv_noticeImg1.setBackground(null);
				}else{
					iv_noticeImg1.setBackground(null);
					iv_noticeImg1.setVisibility(View.GONE);
				}

				if (!rlid.get(0).getIMG2().equals("")){
					//iv_noticeImg2.setImageBitmap(ImageResize.thumbnailResize(StringToBitMap(rlid.get(0).getIMG2())));
					iv_noticeImg2.setImageBitmap((StringToBitMap(rlid.get(0).getIMG2())));
					iv_noticeImg2.setBackground(null);
				}else{
					iv_noticeImg2.setBackground(null);
					iv_noticeImg2.setVisibility(View.GONE);
				}
			}else{
				iv_noticeImg1.setBackground(null);
				iv_noticeImg1.setVisibility(View.GONE);
				iv_noticeImg2.setBackground(null);
				iv_noticeImg2.setVisibility(View.GONE);
			}
		}
	}

	public String BitMapToString(Bitmap bitmap) {
		/*
		 * ByteArrayOutputStream baos=new ByteArrayOutputStream();
		 * bitmap.compress(Bitmap.CompressFormat.PNG,100, baos); byte []
		 * b=baos.toByteArray(); String temp=Base64.encodeToString(b,
		 * Base64.DEFAULT); Log.v("basTem", "log=" + temp);
		 */

		BitmapToByteArray btb = new BitmapToByteArray();
		byte[] b = btb.bitmapToByteArray(bitmap);
		// String temp=Base64.encodeToString(b, Base64.DEFAULT);
		String temp = b.toString();
		Log.v("basTem", "log=" + temp);
		return temp;
	}

	public Bitmap StringToBitMap(String encodedString) {
		try {
			Log.e("basTem", "encodedString=" + encodedString);
			byte[] encodeByte = Base64.decode(encodedString.getBytes(),
					Base64.DEFAULT);
			// byte [] encodeByte=encodedString.getBytes();
			Log.e("basTem", "log=" + encodeByte);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}


	public Uri getImageUri(Context inContext, Bitmap inImage) {
//		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//		  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//		  String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//		  	String str = Environment.getExternalStorageState();
		if(inImage==null)return null;
		String str = Environment.getExternalStorageState();
		if (str.equals(Environment.MEDIA_MOUNTED)) {
			;
			File file = new File(AbsoluteFilePath.PDF_PATH);
			if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
				file.mkdirs();
		} else{
			AlertView.showAlert("SD Card 인식 실패", context);
		}
		File file = new File(AbsoluteFilePath.PDF_PATH + "notice.jpg");
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inImage.compress(CompressFormat.PNG, 100, outStream);

		Uri uri = Uri.parse(AbsoluteFilePath.PDF_PATH + "notice.jpg");
		return uri;
	}


	public  static void iamgeCKUri(Context context, Uri uri) {
		// TODO Auto-generated method stub

		IntentActionUtils.actionImage(context, uri);


//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//		Uri uri = Uri.parse(imageNo);
//		intent.setDataAndType(uri, "image/*");
//		context.startActivity(intent);

	}

	private Bitmap getImageViewBitmap(ImageView iv){
		BitmapDrawable d = (BitmapDrawable)iv.getDrawable();
		Bitmap b = d.getBitmap();
		return b;
	}


	private void detailImage(){


		Uri uri = Uri.parse(AbsoluteFilePath.PDF_PATH + "jintemp.jpg");
		IntentActionUtils.actionImage(context, uri);
		this.dismiss();
	}

	public void sch(){
		progress(true);
		new NoticeImageTask().execute();
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			IR_NT00_R01P.this.progress =
					android.app.ProgressDialog.show(getContext(), "알림","사진을 불러오고 있습니다...");
		}else{
			IR_NT00_R01P.this.progress.dismiss();
		}
	}

}
