package com.jinsit.kmec.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.CM.CM_SelectPermissionCheck;
import com.jinsit.kmec.CM.CM_SelectPermissionCheck_ReqestData;
import com.jinsit.kmec.DB.MasterDataDownload;
import com.jinsit.kmec.DM.DM.DM_DM00_R00;
import com.jinsit.kmec.GK.LO.GK_LO00_R00;
import com.jinsit.kmec.HM.MP.HM_MP00_R00;
import com.jinsit.kmec.IP.IS.IP_IS00_R00;
import com.jinsit.kmec.IP.JS.IP_JS00_R00;
import com.jinsit.kmec.IR.CD.IR_CD00_R00;
import com.jinsit.kmec.IR.CI.IR_CI00_R00;
import com.jinsit.kmec.IR.EDS.IR_EDS00_R00;
import com.jinsit.kmec.IR.ES.IR_ES00_R00;
import com.jinsit.kmec.IR.NM.IR_NM00_R00;
import com.jinsit.kmec.IR.NT.IR_NT00_R00;
import com.jinsit.kmec.IR.PI.IR_PI00_R00;
import com.jinsit.kmec.IR.TI.IR_TI00_R00;
import com.jinsit.kmec.SM.CR.SM_CR00_R00;
import com.jinsit.kmec.SM.CS.SM_CS00_R00;
import com.jinsit.kmec.SM.GM.SM_GM00_R00;
import com.jinsit.kmec.WO.WO_WO00_R00F;
import com.jinsit.kmec.comm.CommonSession;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.NetworkStates;
import com.jinsit.kmec.comm.jinLib.SimpleDialog;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;


public class HomeNavigation extends LinearLayout implements OnClickListener, OnLongClickListener, Animation.AnimationListener{
	Context context;
	LinearLayout navigationMenu;
	Button btn_naviIR,btn_naviDM,btn_naviHOME,btn_naviIP,btn_naviSM;
	private FrameLayout widgetFrameLayout;
	private TextView navigationEmptyTextView;

    private HomeNaviPreference naviPref;

    private Animation homeNaviAnimation;

	public HomeNavigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		navigationMenu = (LinearLayout)inflater.inflate(R.layout.widget_home_navigation,  null);
		addView(navigationMenu);

		widgetFrameLayout = (FrameLayout)findViewById(R.id.widgetFrameLayout);
		btn_naviIR = (Button)findViewById(R.id.btn_naviIR);
		btn_naviDM = (Button)findViewById(R.id.btn_naviDM);
		btn_naviHOME = (Button)findViewById(R.id.btn_naviHOME);
		btn_naviIP = (Button)findViewById(R.id.btn_naviIP);
		btn_naviSM = (Button)findViewById(R.id.btn_naviSM);

		navigationEmptyTextView = (TextView)findViewById(R.id.navigationEmptyTextView);
		btn_naviIR.setOnClickListener(this);
		btn_naviDM.setOnClickListener(this);
		btn_naviHOME.setOnClickListener(this);
		btn_naviHOME.setOnLongClickListener(this);
		btn_naviIP.setOnClickListener(this);
		btn_naviSM.setOnClickListener(this);
		navigationEmptyTextView.setOnClickListener(this);

        naviPref = new HomeNaviPreference(context);
        homeNaviAnimation = new AlphaAnimation(0, 1);
        homeNaviAnimation.setDuration(500);
        homeNaviAnimation.setAnimationListener(this);
		setToggleNavi();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//현재 내비상태를 가져와서 숨겨져있으면 팝오버 메뉴가 클릭안되도록 한다.
		if(naviPref.isHide() && v.getId() != R.id.navigationEmptyTextView){
		    return;
        }
		switch(v.getId()){
			case R.id.btn_naviIR:
				loginStatusCheck(false);
				navigationPopUp(v, R.menu.navipop_ir);
				break;
			case R.id.btn_naviDM:
				loginStatusCheck(false);
				navigationPopUp(v, R.menu.navipop_dm);
				break;

			case R.id.btn_naviHOME:
				naviPref.setHide(!naviPref.isHide());
				setToggleNavi();
				break;
			case R.id.navigationEmptyTextView:
				naviPref.setHide(!naviPref.isHide());
				setToggleNavi();
				break;
//s		case R.id.btn_naviHOME:
			//popUp();
			//Intent cv = new Intent(context, WO_WT00_R02.class);
			//context.startActivity(cv);

			//moveHome(); pre-alive
			//testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);

//			FrameLayout testnavi =  (FrameLayout) idMap.get("testnavi");
//			testnavi.setPadding(0, 0, 0, 0); testnavi.setAlpha((float)1);

//s			break;
			case R.id.btn_naviIP:
				loginStatusCheck(false);
				navigationPopUp(v,R.menu.navipop_ip);
				break;
			case R.id.btn_naviSM:
				loginStatusCheck(false);
				navigationPopUp(v,R.menu.navipop_sm);
				break;

		}
	}

	private void moveHome(){
		ActivityManager actManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo>info;
		info = actManager.getRunningTasks(1);
		for(Iterator<RunningTaskInfo> iterator = info.iterator(); iterator.hasNext();) {
			RunningTaskInfo runningTaskInfo = (RunningTaskInfo)iterator.next();

			if(runningTaskInfo.topActivity.getClassName().equals("com.jinsit.kmec.HM.MP.HM_MP00_R00")) {
				//Toast.makeText(context, "현재 홈화면에 있습니다.", 2000).show();
				//홈화면에 있을 때 롱클릭하면 리프레쉬 하려고 한다.
				Intent intent = new Intent(context, HM_MP00_R00.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}else{
				Intent intent = new Intent(context, HM_MP00_R00.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			}


		}
	}
	PopupMenu pop;
	private void navigationPopUp(final View v, final int menu){
		pop = new PopupMenu(context, v);

		pop.getMenuInflater().inflate(menu, pop.getMenu());

		pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch(item.getItemId()){

					//[공지사항]
					case R.id.menu_ir_NT:
						Intent nt = new Intent(context, IR_NT00_R00.class);
						context.startActivity(nt);

						/*PopupMenu pop = new PopupMenu(context, v);
						pop.getMenuInflater().inflate( R.menu.navipop_sm, pop.getMenu());
						pop.show();*/
						//pop.getMenuInflater().inflate( R.menu.navipop_sm, pop.getMenu());
						//pop.show();
						break;

					//[Group Message]
					case R.id.menu_sm_GM:
						moveActivity(permissionCheck("SM_GM00_R00"), SM_GM00_R00.class);
						break;

					//[환경설정]
					case R.id.menu_sm_CS:
						if (NetworkStates.isNetworkStatus(context)) {
							Intent cs = new Intent(context, SM_CS00_R00.class);
							//Intent cs = new Intent(context, MasterDataDownload.class);
							context.startActivity(cs);
						}else{
							SimpleDialog sm01 = new SimpleDialog(context, "알림","네트워크 연결 에러입니다. WIFI나 LTE를 확인하시고 다시 시도하십시오.");
							sm01.show();
						}

						break;

					//[Claim등록조회] - done
					case R.id.menu_sm_CR:

						moveActivity(permissionCheck("SM_CR00_R00"), SM_CR00_R00.class);
//						Intent cr = new Intent(context, SM_CR00_R00.class);
//						context.startActivity(cr);
						break;

					//[로그아웃] - done
					case R.id.menu_sm_LO:
						context.startActivity(new Intent(context,GK_LO00_R00.class));
						break;

					//[CBS Data] - done
					case R.id.menu_ir_CD:
						//moveActivity(permissionCheck("IR_CD00_R00"), IR_CD00_R00.class);
						//cbs data 권한 풀어달라고 함(공성윤주임 20150126)
						Intent cd = new Intent(context,IR_CD00_R00.class);
						context.startActivity(cd);
						break;

					//[작업자현황] - done
					case R.id.menu_ir_ES:
						moveActivity(permissionCheck("IR_ES00_R00"), IR_ES00_R00.class);
//						Intent es = new Intent(context, IR_ES00_R00.class);
//						context.startActivity(es);
						break;


					//[교육이수현황] - done
					case R.id.menu_ir_EDS:
						//moveActivity(permissionCheck("IR_ES00_R00"), IR_ES00_R00.class);
						Intent eds = new Intent(context, IR_EDS00_R00.class);
						context.startActivity(eds);
						break;

					//[NFC등록] - done
					case R.id.menu_ir_NM:
						Intent nm = new Intent(context, IR_NM00_R00.class);
						context.startActivity(nm);
						break;
					//[점검스케줄] - done
					case R.id.menu_ip_IS:
						moveActivity(permissionCheck("IP_IS00_R00"), IP_IS00_R00.class);
//						Intent is = new Intent (context, IP_IS00_R00.class);
//						context.startActivity(is);
						break;

					//[작업일지] - done
					case R.id.menu_ip_WLog:
						moveActivity(permissionCheck("IP_JS00_R00"), IP_JS00_R00.class);
//						Intent wLog = new Intent(context, IP_JS00_R00.class);
//						context.startActivity(wLog);
						break;

					//[작업대상목록] - done
					case R.id.menu_ip_WList:
						//작업대상은 체크안함?
						Intent wList = new Intent(context,WO_WO00_R00F.class);
						context.startActivity(wList);
						break;

					//[부품청구현황] - done
					case R.id.menu_ir_PI:
						Intent pi = new Intent(context,IR_PI00_R00.class);
						context.startActivity(pi);
						break;

					//[고객정보] - done
					case R.id.menu_ir_CI:
						Intent ci = new Intent(context,IR_CI00_R00.class);
						context.startActivity(ci);
						break;

					//[근태관리] - done
					case R.id.menu_dm_DM:
						//AlertView.showAlert( "근태관리 기능은 2019년 7월 16일부터 사용할 수 있습니다.", context);

						Intent dm = new Intent(context, DM_DM00_R00.class);
						context.startActivity(dm);
						break;

					//[기술정보]
					case R.id.menu_ir_TI:
						moveActivity(permissionCheck("IR_TI00_R00"), IR_TI00_R00.class);
//						Intent ti = new Intent(context, IR_TI00_R00.class);
//						context.startActivity(ti);
						break;

				}
				return false;
			}


		});
		pop.show();
	}

	private void moveActivity(String result, Class className){
		if(result.equals("1")){
			//권한있음
			Intent intent = new Intent(context, className);
			context.startActivity(intent);
		}else if(result.equals("0")){
			//권한없음
			SimpleDialog sm01 = new SimpleDialog(context, "알림","접근권한이 없습니다.");
			sm01.show();
		}else{
			//네트워크에러
			SimpleDialog sm01 = new SimpleDialog(context, "알림","네트워크 연결 에러입니다. WIFI나 LTE를 확인하시고 다시 시도하십시오.");
			sm01.show();
		}
	}

	private String permissionCheck(String menuCd) {

		String result = "";
		if (NetworkStates.isNetworkStatus(context)) {
			CommonSession cs = new CommonSession(context);
			CM_SelectPermissionCheck_ReqestData reqData = new CM_SelectPermissionCheck_ReqestData();
			reqData.setUsrId(cs.getEmpId());
			reqData.setMenuCd(menuCd);
			// 던질 파라미터

			CM_SelectPermissionCheck permissionCheck = new CM_SelectPermissionCheck(context);
			AsyncTask<CM_SelectPermissionCheck_ReqestData, Process, String> permissionResult =
					permissionCheck.execute(reqData);

			try {
				result = permissionResult.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			result = "";
		}
		return result;

	}
	private void loginStatusCheck(boolean moveHome){
		new LoginOrOutCheckAsync(moveHome).execute();
	}

	private CommonSession commonSession;
	EasyJsonMap ejm;

    public class LoginOrOutCheckAsync extends AsyncTask<Void, Void, Boolean>{
		private String resultMsg = "";
		private boolean isMoveHome = false;
		public LoginOrOutCheckAsync(boolean moveHome) {
			// TODO Auto-generated constructor stub
			this.isMoveHome = moveHome;
		}
		@Override
		protected void onPreExecute(){
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			commonSession = new CommonSession(context);
			GetHttp getHttp = new GetHttp();
			String param_url = WebServerInfo.getUrl()+"cm/checkIfLoginOrNot.do";

			List<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("empId" , commonSession.getEmpId()));

			JSONObject returnJson = getHttp.getPost(param_url, arguments, true);
			try {
				ejm = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return true;
		}//end of doInBackground()


		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			try {

				boolean isError = ejm.getValue("errorCd").equals("0") ? false : true;
				if(!isError){
					if(result){
						if(ejm.getValue("RTN").equals("1")){
							//로그아웃상태
							if(!isMoveHome)pop.dismiss();//팝업이 뜬상태로 얼러트 띄우고 얼러트 클릭하면 앱을 종료하면
							//팝이 켜져있는상태에서 종료한다고 에러를 띄움 죽지는 않지만 에러때문에 pop을 전역으로 선언하고 여기서 디스미스
							AlertView.showAlert("강제 로그아웃 되었습니다.", context,new OnDismissListener() {
								@Override
								public void onDismiss(DialogInterface dialog) {
									//ActivityAdmin.getInstance().addActivity(HM_MP00_R00.getActivity());
									attendAndLogout();
								}
							});
						}else{
							if(isMoveHome)moveHome();
						}

					}else{

					}
				}else if(isError){
					alert(ejm.getValue("errorMsg"), context);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Button getBtn_naviHOME(){
		return btn_naviHOME;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_naviHOME:
				loginStatusCheck(true);

				break;
		}
		return true;
	}
	//utils
	private void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}

	private void attendAndLogout(){
		GK_LO00_R00 logout = new GK_LO00_R00(context);
		logout.attendAndLogout();
	}

	public void setToggleNavi() {
		boolean isHide = naviPref.isHide();
        widgetFrameLayout.startAnimation(homeNaviAnimation);

	}


	//region  애니메이션 리스너
    @Override
    public void onAnimationStart(Animation animation) {
		boolean isHide = naviPref.isHide();
		if(isHide){
			widgetFrameLayout.setVisibility(View.GONE);
		}else{
			widgetFrameLayout.setVisibility(View.VISIBLE);
		}
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        boolean isHide = naviPref.isHide();
        if(isHide){
            widgetFrameLayout.setVisibility(View.GONE);
        }else{
            widgetFrameLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    //endregion
}