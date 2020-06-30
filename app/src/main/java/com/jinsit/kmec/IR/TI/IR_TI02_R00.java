package com.jinsit.kmec.IR.TI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.PDFViewActivity;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.RemoveSDcard;
import com.jinsit.kmec.comm.jinLib.ScreenUtil;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;
import com.jinsit.kmec.widget.HomeNaviPreference;
import com.jinsit.kmec.widget.HomeNavigation;
import com.jinsit.kmec.widget.JActionbar;

public class IR_TI02_R00 extends Activity implements OnItemClickListener, OnClickListener {
	private static final String PDF_FILE_NAME = "jintemp.jin";
	private Context context;

	private EditText et_ti_searchText;
	private TextView btn_ti_searchClass;

	private ListView lv_ti_selectionClass;

	private IR_TI01_R00_ITEM01 item01;
	private List<IR_TI02_R00_ITEM01> itemList01;
	private IR_TI02_R00_Adapter01 adapter01;

	private EasyJsonList ejl01;
	private CommonSession commonSession;

	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ti02_r00);
		activityInit();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onResume() {
		super.onResume();
		RemoveSDcard.removeFiles();
		naviPref = new HomeNaviPreference(context);
		navigationInit();
	}

	protected void activityInit() {
		getInstances();
		context = this;

		// 타이틀 바
		android.app.ActionBar aBar = getActionBar();
		aBar.setTitle("기술 정보");
		aBar.setDisplayShowHomeEnabled(false);
		JActionbar.setActionBar(this, aBar);
		String str = Environment.getExternalStorageState();
		if (str.equals(Environment.MEDIA_MOUNTED)) {
			;
			File file = new File(AbsoluteFilePath.PDF_PATH);
			if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
				file.mkdirs();
		} else {
			AlertView.showAlert("SD Card 인식 실패", context);
		}

		commonSession = new CommonSession(context);
		docuHNm = getIntent().getExtras().getString("docuHNm");
		Object selectedItem = (Object) this.getIntent().getExtras()
				.getSerializable("selectedItem");
		if (selectedItem.getClass().getName()
				.equals("com.jinsit.kmec.IR.TI.IR_TI01_R00_ITEM01")) {
			item01 = (IR_TI01_R00_ITEM01) selectedItem;
		} else {
			item01 = new IR_TI01_R00_ITEM01();
		}

		itemList01 = new ArrayList<IR_TI02_R00_ITEM01>();
		this.currentSelectedItem01 = null;
		inquerySectionClass();

		TextWatcher tWatcher = new TextWatcher(){

			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			public void onTextChanged(CharSequence s, int start, int before,int count) {

			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = et_ti_searchText.getText().toString().toLowerCase(Locale.getDefault());
				adapter01.filter(text);

			}
		};

		et_ti_searchText.addTextChangedListener(tWatcher);

	}

	String docuHNm;

	String getFolderPath() {
		return docuHNm + "/" + item01.getDocuMNm();
	}

	protected void getInstances() {
		et_ti_searchText = (EditText)findViewById(R.id.et_ti_searchText);
		btn_ti_searchClass = (TextView)findViewById(R.id.btn_ti_searchClass);
		lv_ti_selectionClass = (ListView) findViewById(R.id.lv_ti_selectionClass);
		setEvents();
	}

	protected void setEvents() {
		lv_ti_selectionClass.setOnItemClickListener(this);
		btn_ti_searchClass.setOnClickListener(this);
		setConfig();
	}

	private void setConfig() {
		ActivityAdmin.getInstance().addActivity(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub
		if (position > -1) {
			this.currentSelectedItem01 = (IR_TI02_R00_ITEM01) this.adapter01.getItem(position);
			//this.currentSelectedItem01 = itemList01.get(position);
			progress(true);
			new selectPdfFile().execute("basicWorkTime");
//			new pdfClickCheckCalss().execute("basicWorkTime");
		} else {
			this.currentSelectedItem01 = null;
		}
	}

	public class selectPdfFile extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. basicWorkTime
			if (params[0].equals("basicWorkTime")) {
				try {

					RemoveSDcard.removeFiles();
					// 출력할 파일명과 읽어들일 파일명을지정한다.
					// InputStream is = EasyDownLoad.getPDF("PCSZ290M",
					// "2014_05_27_RCP.pdf", commonSession.getEmpId());

					if (currentSelectedItem01.getFileNm().equals("")
							|| currentSelectedItem01.getFileNm() == null) {

						return "None";
					}
					System.out.println("00700 :: "
							+ currentSelectedItem01.getFileNm());
					InputStream is = EasyDownLoad.getPDF(
							getFolderPath()// "BC1"//"PCSZ290M"
							, currentSelectedItem01.getFileNm(),
							commonSession.getEmpId());
					System.out.println("ava " + is.available());
					File file = new File(AbsoluteFilePath.PDF_PATH
							+ PDF_FILE_NAME);
					OutputStream outStream = new FileOutputStream(file);
					// 읽어들일 버퍼크기를 메모리에 생성
					byte[] buf = new byte[1024];
					int len = 0;
					// 끝까지 읽어들이면서 File 객체에 내용들을 쓴다
					while ((len = is.read(buf)) > 0) {
						outStream.write(buf, 0, len);
					}
					// Stream 객체를 모두 닫는다.
					outStream.close();
					is.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
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

			// 1. basicWorkTime
			if (result.equals("basicWorkTime")) {

				progress(false);
				try {
					Uri uri = Uri.parse(AbsoluteFilePath.PDF_PATH + PDF_FILE_NAME);
					Intent intent = new Intent(IR_TI02_R00.this, PDFViewActivity.class);
					intent.setData(uri);
					intent.putExtra("isPdf", true);
					startActivity(intent);
					new pdfClickCheckCalss().execute("basicWorkTime");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				AlertView.showAlert("저장된 PDF 파일이 없습니다", context);
			}
		}
	}// end of SelectData inner-class

	public class selectSectionClass extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. basicWorkTime
			if (params[0].equals("basicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/selectSectionClass.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("docuHCd",
							IR_TI02_R00.this.item01.getDocuHCd()));
					arguments.add(new BasicNameValuePair("docuMCd",
							IR_TI02_R00.this.item01.getDocuMCd()));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
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

			// 1. basicWorkTime
			if (result.equals("basicWorkTime")) {

				try {
					itemList01.clear();
					int jsonSize = returnJson01.getJSONArray("dataList")
							.length();
					for (int i = 0; i < jsonSize; i++) {
						itemList01.add(new IR_TI02_R00_ITEM01(ejl01.getValue(i,
								"DOCU_H_CD"), ejl01.getValue(i, "DOCU_M_CD"),
								ejl01.getValue(i, "DOCU_L_CD"), ejl01.getValue(
								i, "DOCU_L_NM"), ejl01.getValue(i,
								"DOCU_CD"), ejl01
								.getValue(i, "FILE_NM")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				progress(false);
				adapter01 = new IR_TI02_R00_Adapter01(context,
						R.layout.ir_ti02_r00_adapter01, itemList01);
				IR_TI02_R00.this.lv_ti_selectionClass.setAdapter(adapter01);
			}
		}
	}// end of SelectData inner-class

	public class pdfClickCheckCalss extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. basicWorkTime
			if (params[0].equals("basicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "ir/pdfClickCheckCalss.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("docuCd",
							currentSelectedItem01.getDocuCd()));
					arguments.add(new BasicNameValuePair("csEmpId",
							commonSession.getEmpId()));
					returnJson01 = http.getPost(param_url_01, arguments, true);

					try {
						ejl01 = new EasyJsonList(
								returnJson01.getJSONArray("dataList"));
					} catch (JSONException e) {
						e.printStackTrace();
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
			// 1. basicWorkTime
			if (result.equals("basicWorkTime"))
				progress(false);
		}
	}// end of SelectData inner-class


	private void inquerySectionClass() {
		progress(true);
		new selectSectionClass().execute("basicWorkTime");
	}

	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_TI02_R00.this.progress = android.app.ProgressDialog.show(
					context, "알림", "조회 중입니다.");
		} else {
			IR_TI02_R00.this.progress.dismiss();
		}
	}

	private IR_TI02_R00_ITEM01 currentSelectedItem01;

	private HomeNavigation homeNavi;
	private FrameLayout testnavi;
	boolean isHide;
	private HomeNaviPreference naviPref;

	private void setToggleNavi() {
		boolean isHide = naviPref.isHide();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int totalHeight = ScreenUtil.getRealScreenHeight(this);;
		int naviHeight = getResources().getDrawable(R.drawable.home_menu_on)
				.getIntrinsicHeight();
		int viewArea = naviHeight / 6;
		int setPadding = totalHeight - viewArea - naviHeight;
		if (isHide) {
			testnavi.setPadding(0, setPadding, 0, 0);testnavi.setAlpha((float)0.5);
		} else {
			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);
		}
	}

	private void navigationInit() {
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
