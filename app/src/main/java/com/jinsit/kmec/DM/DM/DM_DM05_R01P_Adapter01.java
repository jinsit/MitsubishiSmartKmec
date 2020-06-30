package com.jinsit.kmec.DM.DM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jinsit.kmec.R;

import java.util.List;

public class DM_DM05_R01P_Adapter01 extends BaseAdapter {

    Context context;
    int layout;
    private boolean[] ischeckedItem;

    private List<DM_DM05_R01P_ITEM01> dataList = null;

    public DM_DM05_R01P_Adapter01(Context context, int layout,
                                  List<DM_DM05_R01P_ITEM01> dataList) {
        super();
        this.context = context;
        this.layout = layout;
        this.dataList = dataList;
        // 체크박스 true로 default
        ischeckedItem = new boolean[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            ischeckedItem[i] = true;
        }
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
        private TextView tv_dm05_r01p_adapter_deptNm;
        private TextView tv_dm05_r01p_adapter_deptcd;
        private TextView tv_dm05_r01p_adapter_workNm;
        private TextView tv_dm05_r01p_adapter_workTime;
        private CheckBox check_dm05_r01p_adapter_select;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DM_DM05_R01P_ITEM01 item = dataList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.tv_dm05_r01p_adapter_deptNm = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_deptNm);
            holder.tv_dm05_r01p_adapter_deptcd = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_deptcd);
            holder.tv_dm05_r01p_adapter_workNm = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workNm);
            holder.tv_dm05_r01p_adapter_workTime = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workTime);
            holder.check_dm05_r01p_adapter_select = (CheckBox) convertView.findViewById(R.id.check_dm05_r01p_adapter_select);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.tv_dm05_r01p_adapter_deptNm.setText(item.getBLDG_NM());
        holder.tv_dm05_r01p_adapter_deptcd.setText(item.getBLDG_NO());
        holder.tv_dm05_r01p_adapter_workNm.setText(item.getWORK_NM());
        holder.tv_dm05_r01p_adapter_workTime.setText(item.getJOB_TM_NM());

        holder.check_dm05_r01p_adapter_select.setClickable(false);
        holder.check_dm05_r01p_adapter_select.setFocusable(false);
        holder.check_dm05_r01p_adapter_select.setChecked(ischeckedItem[position]);
        return convertView;
    }

    public void setChecked(int position) {
        ischeckedItem[position] = !ischeckedItem[position];
    }

    public boolean getChecked(int position) {
        return ischeckedItem[position];
    }
}