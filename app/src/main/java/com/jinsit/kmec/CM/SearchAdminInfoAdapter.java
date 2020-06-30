package com.jinsit.kmec.CM;

import java.util.ArrayList;

import android.content.Context;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.ActionViewEvent;
import com.jinsit.kmec.comm.jinLib.DateUtil;
import com.jinsit.kmec.comm.jinLib.UnderLineString;



public class SearchAdminInfoAdapter extends BaseAdapter implements OnClickListener{

	private class ViewHolder {
		
		public TextView tv_adminName, tv_adminSP,tv_adminEmail,tv_adminCall;
	}

	private ViewHolder holder;
	private Context mContext;
	public ArrayList<SearchAdminInfoResData> adminInfoResData;;
	private ArrayList<SearchAdminInfoResData> orgAdminInfoResData;;

	public SearchAdminInfoAdapter(Context context,
			ArrayList<SearchAdminInfoResData> resData) {
		super();
		this.mContext = context;
		
		setReceiptListItems(resData);
	}
	

	public void setReceiptListItems(ArrayList<SearchAdminInfoResData> value) {
		this.orgAdminInfoResData = value;
		this.adminInfoResData = new ArrayList<SearchAdminInfoResData>();

		for (SearchAdminInfoResData resData : this.orgAdminInfoResData) {
			// if(receiptListItem.getLost_fg().equals(Integer.toString(ABANDON_FLAG))){
			this.adminInfoResData.add(resData);
			// }

		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return adminInfoResData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return adminInfoResData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listitem_search_admininfo, null);
			holder.tv_adminName = (TextView)convertView.findViewById(R.id.tv_adminName);
			holder.tv_adminSP = (TextView)convertView.findViewById(R.id.tv_adminSP);
			holder.tv_adminEmail = (TextView)convertView.findViewById(R.id.tv_adminEmail);
			holder.tv_adminCall = (TextView)convertView.findViewById(R.id.tv_adminCall);
			holder.tv_adminSP.setOnClickListener(this);
			holder.tv_adminCall.setOnClickListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		final SearchAdminInfoResData mData = adminInfoResData.get(position);
		holder.tv_adminName.setText(mData.getCLIENT_NM());
		holder.tv_adminSP.setText((UnderLineString.getUnderLineString(mData.getMOBILE())));
		holder.tv_adminEmail.setText(mData.getMAIL_ADDR());
		holder.tv_adminCall.setText((UnderLineString.getUnderLineString(mData.getPHONE())));
		return convertView;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_adminSP:
			TextView tvSp = (TextView)v;
			ActionViewEvent.callAction(mContext, tvSp.getText().toString());
			break;

		case R.id.tv_adminCall:
			TextView tvCall = (TextView)v;
			ActionViewEvent.callAction(mContext,tvCall.getText().toString());
			break;
			
		}
	}

	

}
