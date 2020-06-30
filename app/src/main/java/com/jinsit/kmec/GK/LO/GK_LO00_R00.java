package com.jinsit.kmec.GK.LO;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.ActivityAdmin;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class GK_LO00_R00 extends Activity
		implements OnClickListener{

	//UI
	private Context context;
	private TextView tv01_gk_lo00_r00;
	private TextView tv02_gk_lo00_r00_yes;
	private TextView tv03_gk_lo00_r00_no;

	//Http
	private EasyJsonMap ejm01;
	private EasyJsonList ejl01;
	private JSONObject returnJson;

	//Utils
	private ProgressDialog progress;
	private android.app.ActionBar aBar;

	public GK_LO00_R00(Context context){
		this.context = context;
	}
	public GK_LO00_R00(){

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gk_lo00_r00);
		getInstances();
		setConfig();
	}

	private void getInstances(){

		context				  	= this;
		aBar  					= getActionBar();
		aBar.setTitle("로그아웃");
		aBar.setDisplayShowHomeEnabled(false);
		tv01_gk_lo00_r00 	  	= (TextView) findViewById(R.id.tv01_gk_lo00_r00);
		tv02_gk_lo00_r00_yes 	= (TextView) findViewById(R.id.tv02_gk_lo00_r00_yes);
		tv03_gk_lo00_r00_no  	= (TextView) findViewById(R.id.tv03_gk_lo00_r00_no);
		setEvents();
	}

	private void setEvents(){
		tv02_gk_lo00_r00_yes.setOnClickListener(this);
		tv03_gk_lo00_r00_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv02_gk_lo00_r00_yes:
				new Database().execute("userLogOut");
				break;
			case R.id.tv03_gk_lo00_r00_no:
				//finish();
				startActivity(new Intent(this,HM_MP00_R00.class));
				break;
			default:
				System.out.println("out of case");
				break;
		}
	}

	private void logout() {
		setConfig();
		CommonSession cs = new CommonSession(context);
		cs.logout(cs.getEmpId());
		ActivityAdmin.getInstance().finishAllActivities();
		ActivityAdmin.getInstance().finishLastMenuActivites();

	}

	private void setConfig(){

		ActivityAdmin.getInstance().finishLastMenuActivites();
		ActivityAdmin.getInstance().addMenuActivity(this);
		ActivityAdmin.getInstance().addActivity(this);

		if(HM_MP00_R00.getActivity() != null) HM_MP00_R00.getActivity().finish();//홈이 액티비티가 널이 아니면 피니쉬 하고 널이면 패스

	}
	public void attendAndLogout(){
		new Database().execute("userLogOut");
	}

	/**
	 * 강제로그아웃
	 * 강제로그아웃은 로그아웃 프로시저 호출할 필요없다. 이미 로그아웃 상태이기 때문에
	 */
	public void forceLogout(){
		this.logout();
	}

	public class Database extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			progress = android.app.ProgressDialog
					.show(context, "로그아웃","로그아웃 중 입니다...");
		}
		@Override
		protected String doInBackground(String... params) {
			crud(params[0]);
			return params[0];
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setData(result);
		}
	}
	private void crud(String div){
		if(div.equals("userLogOut")){

			GetHttp http = new GetHttp();
			String url = WebServerInfo.getUrl()+"gk/"+div+".do";

			CommonSession cs = new CommonSession(context);
			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.clear();
			arguments.add(new BasicNameValuePair("usrId", cs.getEmpId()));
			arguments.add(new BasicNameValuePair("workDt", cs.getWorkDt()));

			//Http
			returnJson = http.getPost(url, arguments, true);
			try {
				ejm01 = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
	private void setData(String div){
		if(div.equals("userLogOut")){

			try {

				boolean isError = ejm01.getValue("errorCd").equals("0") ? false : true;
				if(!isError){
					logout();
				}else if(isError){
					alert(ejm01.getValue("errorMsg"), context);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}progress.dismiss();

		}
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this,HM_MP00_R00.class));
	}

	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

};