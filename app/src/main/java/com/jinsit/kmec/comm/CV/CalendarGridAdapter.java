package com.jinsit.kmec.comm.CV;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;


/**
 * BaseAdapter를 상속받아 구현한 CalendarAdapter
 *
 * @author croute
 * @since 2011.03.08
 */
public class CalendarGridAdapter extends BaseAdapter
{
	private ArrayList<DayInfo> mDayList;
	private Context mContext;
	private int mResource;
	private LayoutInflater mLiInflater;

	/**
	 * Adpater 생성자
	 *
	 * @param context
	 *            컨텍스트
	 * @param textResource
	 *            레이아웃 리소스
	 * @param dayList
	 *            날짜정보가 들어있는 리스트
	 * @param iphData
	 */

	private ArrayList<InspectionPlanHistoryData> mIphData;
	public CalendarGridAdapter(Context context, int textResource, ArrayList<DayInfo> dayList, ArrayList<InspectionPlanHistoryData> iphData)
	{
		this.mContext = context;
		this.mDayList = dayList;
		this.mIphData = iphData;
		this.mResource = textResource;
		this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mDayList.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		System.out.println("mDayList : " + mDayList.get(position));
		return mDayList.get(position);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final DayInfo day = mDayList.get(position);
		InspectionPlanHistoryData iphd = null;
		if(mIphData.size()>position){
			iphd =mIphData.get(position);
		}



		DayViewHolde dayViewHolder;

		if(convertView == null)
		{
			convertView = mLiInflater.inflate(mResource, null);

		/*	if(position % 7 == 6)
			{
				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP()+getRestCellWidthDP(), getCellHeightDP()));
			}
			else
			{

				convertView.setLayoutParams(new GridView.LayoutParams(getCellWidthDP(), getCellHeightDP()));
			}
			*/

			dayViewHolder = new DayViewHolde();

			dayViewHolder.llBackground = (LinearLayout)convertView.findViewById(R.id.day_cell_ll_background);
			dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.day_cell_tv_day);
			dayViewHolder.tv_calDayNm = (TextView) convertView.findViewById(R.id.tv_calDayNm);
			dayViewHolder.tv_cal_normalWork = (TextView) convertView.findViewById(R.id.tv_cal_normalWork);
			dayViewHolder.tv_cal_hardWork = (TextView) convertView.findViewById(R.id.tv_cal_hardWork);

			convertView.setTag(dayViewHolder);
		}
		else
		{
			dayViewHolder = (DayViewHolde) convertView.getTag();
		}

		if(day != null)
		{
			dayViewHolder.tvDay.setText(day.getDay());
			//System.out.println("tvDay :: "+ day.getDay());
			if(day.isInMonth())
			{

				if(mIphData.get(Integer.valueOf(day.getDay())).getEMP_ID()!=null){

					dayViewHolder.tv_calDayNm .setText(mIphData.get(position).getDAY_NM2());
					dayViewHolder.tv_cal_normalWork.setText(mIphData.get(position).getD_CNT());
					dayViewHolder.tv_cal_hardWork.setText(mIphData.get(position).getN_CNT());
				}


				if(position % 7 == 0)
				{
					dayViewHolder.tvDay.setTextColor(Color.RED);
				}
				else if(position % 7 == 6)
				{
					dayViewHolder.tvDay.setTextColor(Color.BLUE);
				}
				else
				{
					dayViewHolder.tvDay.setTextColor(Color.BLACK);
				}
			}
			else
			{
				dayViewHolder.tvDay.setTextColor(Color.GRAY);
				///밑에 셋팅안해주면 view재사용때문에 다른값이 들어간다
				dayViewHolder.tv_calDayNm .setText("");
				dayViewHolder.tv_cal_normalWork.setText("");
				dayViewHolder.tv_cal_hardWork.setText("");
			}
		}
		return convertView;
	}

	public class DayViewHolde
	{
		public LinearLayout llBackground;
		public TextView tvDay;
		public TextView tv_calDayNm;
		public TextView tv_cal_normalWork;
		public TextView tv_cal_hardWork;

	}

}
