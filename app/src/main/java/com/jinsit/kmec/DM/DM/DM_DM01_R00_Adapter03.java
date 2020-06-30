package com.jinsit.kmec.DM.DM;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.DM.DM.DM_DM01_R00_Adapter02.RefreshListener;
import com.jinsit.kmec.DM.DM.DM_DM01_R00_Adapter02.upate;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

/**
 * 4. 근태조회
 * @author 원성민
 *
 */
public class DM_DM01_R00_Adapter03 extends BaseAdapter implements OnClickListener {

	Context context;
	int layout;
	List<DM_DM01_R00_ITEM04> dataList = null;

	RefreshListener   listener;
	public interface RefreshListener{
		void onRefresh();
	}
	public DM_DM01_R00_Adapter03() {
		super();
		// TODO Auto-generated constructor stub
	}
	String date;
	public DM_DM01_R00_Adapter03(Context context, int layout,
								 List<DM_DM01_R00_ITEM04> dataList ,String date, RefreshListener refresh) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.date = date;
		this.listener = refresh;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class ViewHolder {
		private TextView tv_dm_adapter_otAttenCdNm;
		private TextView tv_dm_adapter_otAttenDt;
		private TextView tv_dm_adapter_otAttenRmk;  //호기
		private TextView btn_dm_attUdpate;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DM_DM01_R00_ITEM04 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.tv_dm_adapter_otAttenCdNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otAttenCdNm);
			holder.tv_dm_adapter_otAttenDt = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otAttenDt);
			holder.tv_dm_adapter_otAttenRmk = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otAttenRmk);
			holder.btn_dm_attUdpate =  (TextView) convertView
					.findViewById(R.id.btn_dm_attUdpate);
			holder.btn_dm_attUdpate.setTag(position);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_dm_adapter_otAttenDt.setText(item.getOT_WORK_DT());
		holder.tv_dm_adapter_otAttenCdNm.setText(item.getATTEN_CD_NM());
		holder.tv_dm_adapter_otAttenRmk.setText(item.getREP_ST_NM());
		if(item.getREP_ST().equals("0")){
			holder.btn_dm_attUdpate.setVisibility(View.VISIBLE);
			holder.btn_dm_attUdpate.setOnClickListener(this);
		}else {
			holder.btn_dm_attUdpate.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}


	public class upate extends AsyncTask<String, Integer, String> {

		GetHttp http = new GetHttp();
		JSONObject returnJson01;

		@Override
		protected String doInBackground(String... params) {

			// 1. bagicWorkTime
			if (params[0].equals("bagicWorkTime")) {
				try {
					String param_url_01 = WebServerInfo.getUrl()
							+ "dm/updateReportToManager.do";

					List<NameValuePair> arguments = new ArrayList<NameValuePair>();
					arguments.add(new BasicNameValuePair("selSt","1"));
					arguments.add(new BasicNameValuePair("csEmpId",dataList.get(viewTag).getEMP_ID()));
					arguments.add(new BasicNameValuePair("otWorkDt",date));
					arguments.add(new BasicNameValuePair("otNo","0"));
					arguments.add(new BasicNameValuePair("usrId",dataList.get(viewTag).getEMP_ID()));

					returnJson01 = http.getPost(param_url_01, arguments, true);
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
			String resultFg= "0";
			if (result.equals("bagicWorkTime")) {
				try {
					resultFg = returnJson01.getString("dataString");
				}
				catch (JSONException e) {
					e.printStackTrace();
				}

				if(resultFg.equals("1"))
				{
					AlertView.showAlert("상신 성공 했습니다.", context);
					listener.onRefresh();
				}
				else
				{
					AlertView.showAlert("상신 실패 했습니다.", context);
				}

			}
		}
	}// end of SelectData inner-class

	private int viewTag;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_dm_attUdpate:

				viewTag = Integer.valueOf(v.getTag().toString());
				new upate().execute("bagicWorkTime");
				break;

			default:
				break;
		}
	}

}