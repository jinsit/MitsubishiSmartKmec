package com.jinsit.kmec.DM.DM;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jinsit.kmec.R;

import java.util.List;

public class DM_DM04_R00_Adapter02 extends BaseAdapter {

    Context context;
    int layout;
    private List<DM_DM04_R00_ITEM02> dataList = null;
    private View.OnClickListener onClickListener;

    public DM_DM04_R00_Adapter02() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DM_DM04_R00_Adapter02(Context context, int layout,
                                 List<DM_DM04_R00_ITEM02> dataList, View.OnClickListener onClickListener) {
        super();
        this.context = context;
        this.layout = layout;
        this.dataList = dataList;
        this.onClickListener = onClickListener;
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
        private TextView tv_dm_adapter_name;
        private TextView tv_dm_adapter_1week_sum;
        private TextView tv_dm_adapter_1week_gaol;
        private TextView tv_dm_adapter_1week_statutory;
        private TextView tv_dm_adapter_2week_sum;
        private TextView tv_dm_adapter_2week_gaol;
        private TextView tv_dm_adapter_2week_statutory;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DM_DM04_R00_ITEM02 item = dataList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.tv_dm_adapter_name = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_name);
            holder.tv_dm_adapter_1week_sum = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_1week_sum);
            holder.tv_dm_adapter_1week_gaol = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_1week_gaol);
            holder.tv_dm_adapter_1week_statutory = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_1week_statutory);
            holder.tv_dm_adapter_2week_sum = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_2week_sum);
            holder.tv_dm_adapter_2week_gaol = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_2week_gaol);
            holder.tv_dm_adapter_2week_statutory = (TextView) convertView
                    .findViewById(R.id.tv_dm_adapter_2week_statutory);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_dm_adapter_name.setText(item.getEMP_NM());
        holder.tv_dm_adapter_1week_sum.setText(item.getWEEK_WORK_HR_TOT_1());
        holder.tv_dm_adapter_1week_gaol.setText(item.getWEEK_WORK_ACH_1());
        holder.tv_dm_adapter_1week_statutory.setText(item.getWEEK_WORK_ACH_LAW_1());
        holder.tv_dm_adapter_2week_sum.setText(item.getWEEK_WORK_HR_TOT_2());
        holder.tv_dm_adapter_2week_gaol.setText(item.getWEEK_WORK_ACH_2());
        holder.tv_dm_adapter_2week_statutory.setText(item.getWEEK_WORK_ACH_LAW_2());


        return convertView;
    }

}