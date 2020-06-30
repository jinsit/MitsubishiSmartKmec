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
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class DM_DM01_R00_Adapter02 extends BaseAdapter implements OnClickListener {

	Context context;
	int layout;
	List<DM_DM01_R00_ITEM03> dataList = null;

	RefreshListener   listener;
	public interface RefreshListener{
		void onRefresh();
	}
	public DM_DM01_R00_Adapter02(Context context, int layout,
								 List<DM_DM01_R00_ITEM03> dataList, RefreshListener refresh) {
		super();
		this.context = context;
		this.layout = layout;
		this.dataList = dataList;
		this.listener = refresh;
	}


	private class ViewHolder {
		private TextView otWorkCd;
		private TextView bldgNm;
		private TextView carNo;  //호기
		private TextView otTm;   //OT
		private TextView otRmk;  //비고
		private TextView btn_dm_otUdpate;  //비고

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final DM_DM01_R00_ITEM03 item = dataList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, layout, null);
			holder = new ViewHolder();
			holder.otWorkCd = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otWorkCd);
			holder.bldgNm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_bldgNm);
			holder.carNo = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_carNo);
			holder.otTm = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otTm);
			/*holder.otRmk = (TextView) convertView
					.findViewById(R.id.tv_dm_adapter_otRmk);*/
			holder.btn_dm_otUdpate = (TextView) convertView
					.findViewById(R.id.btn_dm_otUdpate);
			holder.btn_dm_otUdpate.setTag(position);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.otWorkCd.setText(item.getOtWorkCd());
		holder.bldgNm.setText(item.getBldgNm());
		holder.carNo.setText(item.getRepStNm());
		holder.otTm.setText(item.getOtTm());
		if(item.getRepSt().equals("0")){
			holder.btn_dm_otUdpate.setVisibility(View.VISIBLE);
			holder.btn_dm_otUdpate.setOnClickListener(this);
		}else{
			holder.btn_dm_otUdpate.setVisibility(View.INVISIBLE);
		}
		/*if(!item.getOtRemark().equals("") && item.getOtRemark() != null){
			holder.otRmk.setText("읽기");
		}else{
			holder.otRmk.setText("");
		}*/

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
					arguments.add(new BasicNameValuePair("selSt","2"));
					arguments.add(new BasicNameValuePair("csEmpId",dataList.get(viewTag).getCsEmpId()));
					arguments.add(new BasicNameValuePair("otWorkDt",dataList.get(viewTag).getOtWorkDt()));
					arguments.add(new BasicNameValuePair("otNo",dataList.get(viewTag).getOtNo()));
					arguments.add(new BasicNameValuePair("usrId",dataList.get(viewTag).getCsEmpId()));

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
			case R.id.btn_dm_otUdpate:

				viewTag = Integer.valueOf(v.getTag().toString());
				new upate().execute("bagicWorkTime");
				break;

			default:
				break;
		}
	}


}