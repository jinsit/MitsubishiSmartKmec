package com.jinsit.kmec.CM;

import java.io.File;
import java.io.FileOutputStream;
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
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyImageView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.ImageResize;
import com.jinsit.kmec.comm.jinLib.IntentActionUtils;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.JActionbar;

public class CM_SaveReadPicture extends Activity implements OnClickListener,
		OnDismissListener {

	Context context;
	private LinearLayout lin_ts_picturePage;
	private TextView btn_ts_upload;

	private ArrayList<EasyImageView> imageViewList;
	private static int GALLERY_INTENT = 1;
	private static int GALLERY_CHOOSER_INTENT = 2;
	private static int PICK_FROM_CAMERA = 3;
	private static int CROP_FROM_CAMERA = 4;

	private static String WOTS = "1";
	private static String WORJ = "2";

	private int imageId;

	private ProgressDialog progress;
	private CommonSession commonSession;
	private String jobNo;
	private String workDt;
	private String refContrNo;
	private String selTp;
	private List<CM_SaveReadPicture_ITEM01> itemList01;
	private EasyJsonMap ej01;
	private Uri imageCaptureUri;
	private String filePath = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cm_savereadpicture);
		activityInit();
	}

	protected void activityInit() {
		context = this;
		getInstances();
		imageViewList = new ArrayList<EasyImageView>();
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("고장전후 사진");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		commonSession = new CommonSession(context);

		jobNo = getIntent().getExtras().getString("jobNo");
		workDt = getIntent().getExtras().getString("workDt");
		refContrNo = getIntent().getExtras().getString("refContrNo");
		selTp = getIntent().getExtras().getString("selTp");

		if(selTp.equals("1")){
			filePath = AbsoluteFilePath.FTP_FOLDER_WOTS_PATH;
		}else{
			filePath = AbsoluteFilePath.FTP_FOLDER_WORJ_PATH;
		}
		itemList01 = new ArrayList<CM_SaveReadPicture_ITEM01>();
		addPictureTable();

		progress(true);
		new selectPictures().execute("bagicWorkTime");

	}

	protected void getInstances() {
		lin_ts_picturePage = (LinearLayout) findViewById(R.id.lin_ts_picturePage);
		btn_ts_upload = (TextView) findViewById(R.id.btn_ts_upload);
		setEvents();
	}

	protected void setEvents() {
		btn_ts_upload.setOnClickListener(this);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_ts_upload:
				progress(true);
				new FtpPictureDataUpLoadAsync(context).execute(1);
				break;
			default:

				final ImageView imageView = (ImageView) v;
				imageId = Integer.valueOf(imageView.getTag().toString());

				String item[] = { "사진찍기", "갤러리에서 선택", "전체 보기", "사진 삭제" };

				new AlertDialog.Builder(this).setTitle("사진 가져오기")
						.setItems(item, new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {

								switch (which) {
									case 0:
										actionCamera();
										break;
									case 1:
										actionGallery();
										break;
									case 2:
										if( imageViewList.get(imageId).isImage()){
											imageFullScreen();
										}else{
											AlertView.showAlert("이미지가 없습니다.", context);
										}
										break;
									case 3:
										if( imageViewList.get(imageId).isImage()){
											deleteImage();
										}else{
											AlertView.showAlert("이미지가 없습니다.", context);
										}
										break;
								}
							}
						}).show();

				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 1:
				if (resultCode == RESULT_OK) {
					String filepath = getGalleryImagePath(data);
					if(filepath == null || filepath.equals("")){
						AlertView.showAlert("이미지를 가져올 수 없습니다.", context);
						return;
					}
					if (!new File(filepath).exists()) {
						AlertView.showAlert("이미지를 가져올 수 없습니다.", context);
					} else {
						ImageResize imgResize = new ImageResize();
						Bitmap img = imgResize.bitmapResize(filepath);
						imageViewList.get(imageId).setImageBitmap(img);
						imageViewList.get(imageId).setBackground(null);
						imageViewList.get(imageId).setIsImage(true);
						if (imageViewList.size() - 2 <= imageId) {
							addPictureTable();
						}
					}
				}
				break;
			case 3:
				if (resultCode == RESULT_OK) {
					// 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
					// 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(imageCaptureUri, "image/*");

//				intent.putExtra("aspectX", 1);
//				intent.putExtra("aspectY", 1);
					intent.putExtra("scale", true);
					intent.putExtra("return-data", true);
					startActivityForResult(intent, CROP_FROM_CAMERA);

				}
				break;
			case 4:
				if (resultCode == RESULT_OK) {
					// 크롭이 된 이후의 이미지를 넘겨 받습니다. 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
					// 임시 파일을 삭제합니다.
					final Bundle extras = data.getExtras(); // mImageCaptureUri =
					// data.getData();
					if (extras != null) {
						// ImageResize imgResize = new ImageResize();
						Bitmap img = extras.getParcelable("data");
						imageViewList.get(imageId).setImageBitmap(img);
						imageViewList.get(imageId).setIsImage(true);
						imageViewList.get(imageId).setBackground(null);
						if (imageViewList.size() - 2 <= imageId) {
							addPictureTable();
						}
					}
					// 임시 파일 삭제
					File f = new File(imageCaptureUri.getPath());
					if (f.exists()) {
						f.delete();
					}
				}
				break;
			default:
				break;
		}
	}

	private void imageFullScreen() {
		progress(true);
		new viewImageFile().execute("bagicWorkTime");

	}

	private void deleteImage() {
		imageViewList.get(imageId).setImageDrawable(null);
		imageViewList.get(imageId).setIsImage(false);
		imageViewList.get(imageId).setBackgroundResource(R.drawable.btn_nopicture);
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
					BitmapDrawable d = (BitmapDrawable) imageViewList.get(
							imageId).getDrawable();
					if (d != null) {
						Bitmap bitmap = d.getBitmap();
						File file = new File(AbsoluteFilePath.TEMP_PATH
								+ "temp.jpg");
						try {
							FileOutputStream out = new FileOutputStream(
									AbsoluteFilePath.TEMP_PATH + "temp.jpg");
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
									out);
						} catch (Exception ex) {
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
					Uri uri = Uri.parse(AbsoluteFilePath.TEMP_PATH + "temp.jpg");
					IntentActionUtils.actionImage(context, uri);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {

			}
		}
	}// end of SelectData inner-class

	public void removeFiles() {
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

	private void actionGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, GALLERY_INTENT);
	}

	private void actionCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String url = AbsoluteFilePath.TEMP_PATH + "cameraImage.jpg";

		imageCaptureUri = Uri.fromFile(new File(url));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				imageCaptureUri);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PICK_FROM_CAMERA);
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

	public String getGalleryImagePath(Intent data) {
		Uri imgUri = data.getData();
		String filePath = "";
		if (data.getType() == null) {
			// For getting images from default gallery app.

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(imgUri, filePathColumn,
					null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		} else if (data.getType().equals("image/jpeg")
				|| data.getType().equals("image/png")) {
			// For getting images from dropbox or any other gallery apps.
			filePath = imgUri.getPath();
		}
		return filePath;
	}

	private void addPictureTable() {

		if (selTp.equals(WOTS)) {
			if (imageViewList.size() > 19) {
				return;
			}
		} else if (selTp.equals(WORJ)) {
			if (imageViewList.size() > 19) {
				return;
			}
		}

		LinearLayout lin_ts_picture = new LinearLayout(context);
		LinearLayout.LayoutParams llinLayoutParam = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lin_ts_picture.setLayoutParams(llinLayoutParam);

		EasyImageView imageView01 = new EasyImageView(context);
		EasyImageView imageView02 = new EasyImageView(context);
		LinearLayout.LayoutParams llinLayoutParam1 = new LinearLayout.LayoutParams(
				0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

		llinLayoutParam1.topMargin = 10;

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
	}

	private class FtpPictureDataUpLoadAsync extends
			AsyncTask<Integer, String, Integer> {

		private String retMsg = "";
		GetHttp http;
		JSONObject returnJson01;
		JSONObject returnJson02;
		public int taskCnt;

		private ProgressDialog mDlg;
		private Context context;

		public FtpPictureDataUpLoadAsync(Context context) {
			this.context = context;
		}

		// onPreExecute 함수는 이름대로 excute()로 실행 시 doInBackground() 실행 전에 호출되는 함수
		// 여기서 ProgressDialog 생성 및 기본 세팅하고 show()
		@Override
		protected void onPreExecute() {
			mDlg = new ProgressDialog(context);
			mDlg.setCancelable(false);
			mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDlg.setMessage("이미지 업로드 중");
			mDlg.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			// 1. bagicWorkTime
			if (params[0].equals(1)) {
				String param_url = WebServerInfo.getUrl()
						+ "comm/setFileToFTP.do";

				String param_url1 = WebServerInfo.getUrl()
						+ "comm/deleteFilesFromFtp.do";
				http = new GetHttp();
				int j = 0;
				List<NameValuePair> arguments1 = new ArrayList<NameValuePair>();
				for (CM_SaveReadPicture_ITEM01 item : itemList01) {
					if (!item.getFileNm().equals("")
							&& item.getFileNm() != null) {
						arguments1.add(new BasicNameValuePair("file" + j,
								filePath + "&&"
										+ item.getFileNm()));
						j++;
					}

				}

				returnJson02 = http.getPost(param_url1, arguments1, true);
				Log.v("삭제성공여부", returnJson02.toString());
				taskCnt = 0;
				for (EasyImageView imageView : imageViewList) {
					if (imageView.isImage()) {
						taskCnt++;
					}
				}

				publishProgress("max", Integer.toString(taskCnt));

				itemList01.clear();
				ArrayList<NameValuePair> arguments;
				EasyJsonMap ejm;
				String fileName;
				String picSeq;
				String picTp;
				try {
					int i = -1;
					int completeWorkCnt = 0;
					for (ImageView imageView : imageViewList) {
						i++;
						if (i % 2 == 0) {
							picTp = "B";
						} else {
							picTp = "A";
						}
						picSeq = String.valueOf((i / 2) + 1);
						fileName = getaImageFileName(picTp, picSeq);

						BitmapDrawable d = (BitmapDrawable) imageView
								.getDrawable();
						if (d == null) {
							itemList01.add(new CM_SaveReadPicture_ITEM01(picTp,
									picSeq, ""));
							continue;
						}
						Bitmap bitmap = d.getBitmap();

						arguments = new ArrayList<NameValuePair>();
						arguments.add(new BasicNameValuePair("folderToAccess",
								filePath));
						arguments.add(new BasicNameValuePair("fileName",
								fileName));
						arguments.add(new BasicNameValuePair("IMG1",
								DataConvertor.BitmapBase64(bitmap)));

						returnJson01 = http.getPost(param_url, arguments, true);
						ejm = new EasyJsonMap(
								returnJson01.getJSONObject("dataMap"));
						System.out.println(returnJson01);
						retMsg = ejm.getValue("message01");
						if (retMsg.equals("true")) {
							itemList01.add(new CM_SaveReadPicture_ITEM01(picTp,
									picSeq, fileName));
							completeWorkCnt++;
							publishProgress("progress", Integer.toString(i),
									Integer.toString(completeWorkCnt)
											+ "번째 이미지 업로드");
							Log.v("사진등록완료", String.valueOf(i) + " " + fileName);
						} else {
							Log.v("사진등록실패", String.valueOf(i) + " " + fileName);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					Log.v("비트맵 업로드 에러", e.getMessage());
					return 0;
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
			if (itemList01.size() > 0) {
				new registerPicture().execute("bagicWorkTime");
			} else {
				// //등록된 사진이 없습니다.
				progress(false);
			}

		}
	}

	private class registerPicture extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		String returnValue;

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime

			if (params[0].equals("bagicWorkTime")) {
				try {

					String param_url = WebServerInfo.getUrl()
							+ "ip/registerPicture.do";

					for (CM_SaveReadPicture_ITEM01 item : itemList01) {
						List<NameValuePair> arguments = new ArrayList<NameValuePair>();
						arguments.add(new BasicNameValuePair("empId",
								commonSession.getEmpId()));
						arguments.add(new BasicNameValuePair("workDt", workDt));
						arguments.add(new BasicNameValuePair("jobNo", jobNo));
						arguments.add(new BasicNameValuePair("picTp", item
								.getPicTp()));
						arguments.add(new BasicNameValuePair("picSeq", item
								.getPicSeq()));
						arguments.add(new BasicNameValuePair("fileNm", item
								.getFileNm()));
						arguments.add(new BasicNameValuePair("usrId",
								commonSession.getEmpId()));
						returnJson01 = http.getPost(param_url, arguments, true);
						returnValue = returnJson01.getString("dataString");
						if (returnValue.equals("1")) {

						} else {
							return "Error";
						}
					}

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
			if (result.equals("Error")) {
				// /롤백
				progress(false);
				AlertView.showAlert("등록 실패 했습니다.", context);
			} else if (result.equals("bagicWorkTime")) {

				progress(false);
				AlertView.showAlert("등록 완료 되었습니다.", context);
				finish();
			}
		}
	}

	private class selectPictures extends AsyncTask<String, Integer, String> {
		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {
			// 1. bagicWorkTime

			if (params[0].equals("bagicWorkTime")) {
				try {

					String param_url = WebServerInfo.getUrl()
							+ "ip/selectPictures.do";
					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("empId", commonSession
							.getEmpId()));
					arguments.add(new BasicNameValuePair("workDt", workDt));
					arguments.add(new BasicNameValuePair("jobNo", jobNo));
					returnJson01 = http.getPost(param_url, arguments, true);

					ej01 = new EasyJsonMap(
							returnJson01.getJSONObject("dataMap"));

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
				// ej01.getValue("REF_CONTR_NO")
				itemList01 = new ArrayList<CM_SaveReadPicture_ITEM01>();
				try {
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "1", ej01
							.getValue("BEFORE_PIC_01")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "1", ej01
							.getValue("AFTER_PIC_01")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "2", ej01
							.getValue("BEFORE_PIC_02")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "2", ej01
							.getValue("AFTER_PIC_02")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "3", ej01
							.getValue("BEFORE_PIC_03")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "3", ej01
							.getValue("AFTER_PIC_03")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "4", ej01
							.getValue("BEFORE_PIC_04")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "4", ej01
							.getValue("AFTER_PIC_04")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "5", ej01
							.getValue("BEFORE_PIC_05")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "5", ej01
							.getValue("AFTER_PIC_05")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "6", ej01
							.getValue("BEFORE_PIC_06")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "6", ej01
							.getValue("AFTER_PIC_06")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "7", ej01
							.getValue("BEFORE_PIC_07")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "7", ej01
							.getValue("AFTER_PIC_07")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "8", ej01
							.getValue("BEFORE_PIC_08")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "8", ej01
							.getValue("AFTER_PIC_08")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "9", ej01
							.getValue("BEFORE_PIC_09")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "9", ej01
							.getValue("AFTER_PIC_09")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("B", "10",
							ej01.getValue("BEFORE_PIC_10")));
					itemList01.add(new CM_SaveReadPicture_ITEM01("A", "10",
							ej01.getValue("AFTER_PIC_10")));
					progress(false);
					new selectImageFiles(context).execute(1);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progress(false);
				}
			} else {
				progress(false);
			}
		}
	}

	public class selectImageFiles extends AsyncTask<Integer, String, Integer> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;
		List<Bitmap> bitmapList;

		String returnValue;
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
					// 출력할 파일명과 읽어들일 파일명을지정한다.

					for (CM_SaveReadPicture_ITEM01 item : itemList01) {
						if (!item.getFileNm().equals("")) {
							taskCnt++;
						}
					}

					publishProgress("max", Integer.toString(taskCnt));

					Bitmap bmp;
					int i = 0;
					bitmapList = new ArrayList<Bitmap>();
					for (CM_SaveReadPicture_ITEM01 item : itemList01) {
						if (!item.getFileNm().equals("")) {
							bmp = EasyDownLoad.getImage(
									filePath,
									item.getFileNm(), commonSession.getEmpId());
							bitmapList.add(bmp);
							completeWorkCnt++;
							publishProgress("progress", Integer.toString(i),
									Integer.toString(completeWorkCnt)
											+ "번째 이미지 다운로드");
						} else {
							bitmapList.add(null);
						}
						i++;
					}
				} catch (Exception e) {
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
					int i = 0;
					for (Bitmap bmp : bitmapList) {
						if (bmp != null) {
							imageViewList.get(i).setImageBitmap(bmp);
							imageViewList.get(i).setBackground(null);
							imageViewList.get(i).setIsImage(true);
							if (imageViewList.size() - 2 <= i) {
								addPictureTable();
							}
						}
						i++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				bitmapList.clear();
			} else {
			}

		}
	}// end of SelectData inner-class

	private String getaImageFileName(String picTp, String picSeq) {
		String str = "";
		DateUtil du = new DateUtil();
		String ymd = du.getCurrentShortDate();
		str = this.refContrNo + "_" + ymd + "_" + picTp + "_" + picSeq + ".jpg";
		return str;

	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			CM_SaveReadPicture.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			CM_SaveReadPicture.this.progress.dismiss();
		}
	}
}
