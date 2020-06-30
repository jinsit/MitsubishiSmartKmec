package com.jinsit.kmec.DM.DM;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.CV.DayInfo;

import java.util.ArrayList;

public class DM_DM05_R00_Adapter01 extends BaseAdapter {
    private ArrayList<DayInfo> mDayList;
    private Context mContext;
    private int mResource;
    private LayoutInflater mLiInflater;

    /**
     * Adpater 생성자
     *
     * @param context
     * 컨텍스트
     * @param textResource
     * 레이아웃 리소스
     * @param dayList
     * 날짜정보가 들어있는 리스트
     * @param iphData
     */

    private ArrayList<DM_DM05_R00_ITEM01> mIphData;

    public DM_DM05_R00_Adapter01(Context context, int textResource, ArrayList<DayInfo> dayList, ArrayList<DM_DM05_R00_ITEM01> iphData) {
        this.mContext = context;
        this.mDayList = dayList;
        this.mIphData = iphData;
        this.mResource = textResource;
        this.mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public class DayViewHolde {
        public LinearLayout llBackground;
        public TextView tvDay;
        public TextView tv_calDayNm;
        public TextView tv_dm05_totalTime;
        public TextView tv_dm05_sendStatus;

    }

    @Override
    public int getCount() {
        return mDayList.size();
    }

    @Override
    public Object getItem(int position) {
        System.out.println("mDayList : " + mDayList.get(position));
        return mDayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DayInfo day = mDayList.get(position);
        DM_DM05_R00_ITEM01 iphd = null;
        if (mIphData.size() > position) {
            iphd = mIphData.get(position);
        }

        DayViewHolde dayViewHolder;

        if (convertView == null) {
            convertView = mLiInflater.inflate(mResource, null);

            dayViewHolder = new DayViewHolde();

            dayViewHolder.llBackground = (LinearLayout) convertView.findViewById(R.id.day_cell_dm05_background);
            dayViewHolder.tvDay = (TextView) convertView.findViewById(R.id.tv_dm05_daycell);
            dayViewHolder.tv_calDayNm = (TextView) convertView.findViewById(R.id.tv_dm05_calDayNm);
            dayViewHolder.tv_dm05_totalTime = (TextView) convertView.findViewById(R.id.tv_dm05_totalTime);
            dayViewHolder.tv_dm05_sendStatus = (TextView) convertView.findViewById(R.id.tv_dm05_sendStatus);

            convertView.setTag(dayViewHolder);
        } else {
            dayViewHolder = (DayViewHolde) convertView.getTag();
        }
        if (day != null) {
            dayViewHolder.tvDay.setText(day.getDay().toString());
            if (day.isInMonth()) {
//                if(mIphData.get(Integer.valueOf(day.getDay())).getEMP_ID()!=null){
                String workHR = mIphData.get(position).getWORK_HR();
                dayViewHolder.tv_calDayNm.setText(mIphData.get(position).getDAY_NM2());
                dayViewHolder.tv_dm05_totalTime.setText(mIphData.get(position).getWORK_HR());
                dayViewHolder.tv_dm05_sendStatus.setText(mIphData.get(position).getREP_NM());
                if (workHR.length() > 1) {
                    convertView.setBackgroundResource(R.drawable.border_workday);
                } else {
                    convertView.setBackgroundResource(R.drawable.border_workday_non);
                }

//                }

                if (position % 7 == 0) {
                    dayViewHolder.tvDay.setTextColor(Color.RED);
                } else if (position % 7 == 6) {
                    dayViewHolder.tvDay.setTextColor(Color.BLUE);
                } else {
                    dayViewHolder.tvDay.setTextColor(Color.BLACK);
                }
            } else {
                dayViewHolder.tvDay.setTextColor(Color.GRAY);
                ///밑에 셋팅안해주면 view재사용때문에 다른값이 들어간다
                dayViewHolder.tv_calDayNm.setText("");
                dayViewHolder.tv_dm05_totalTime.setText("");
                dayViewHolder.tv_dm05_sendStatus.setText("");
            }
        }
        return convertView;
    }
}
