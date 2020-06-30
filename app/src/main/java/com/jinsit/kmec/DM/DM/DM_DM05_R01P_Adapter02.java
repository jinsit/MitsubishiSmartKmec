package com.jinsit.kmec.DM.DM;

import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;

import java.util.List;

public class DM_DM05_R01P_Adapter02 extends BaseAdapter {

    Context context;
    int layout;
    private List<DM_DM05_R01P_ITEM02> dataList = null;
    View.OnClickListener onClickListener;

    public DM_DM05_R01P_Adapter02() {
        super();
        // TODO Auto-generated constructor stub
    }

    public DM_DM05_R01P_Adapter02(Context context, int layout,
                                  List<DM_DM05_R01P_ITEM02> dataList, View.OnClickListener onClickListener) {
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
        private TextView tv_dm05_r01p_adapter_deptNm2;
        private TextView tv_dm05_r01p_adapter_deptcd2;
        private TextView tv_dm05_r01p_adapter_workNm2;
        private TextView tv_dm05_r01p_adapter_workTime_fr;
        private TextView tv_dm05_r01p_adapter_workTime_interval;
        private TextView tv_dm05_r01p_adapter_workTime_to;
        private TextView tv_dm05_r01p_adapter_workTime_sum;
        private TextView btn_dm05_r01p_adapter_delete;

        private LinearLayout ln_dm05_r01p_adapter_workTime;
        private LinearLayout ln_dm05_r01p_adapter;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final DM_DM05_R01P_ITEM02 item = dataList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.tv_dm05_r01p_adapter_deptNm2 = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_deptNm2);
            holder.tv_dm05_r01p_adapter_deptcd2 = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_deptcd2);
            holder.tv_dm05_r01p_adapter_workNm2 = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workNm2);
            holder.tv_dm05_r01p_adapter_workTime_fr = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workTime_fr);
            holder.tv_dm05_r01p_adapter_workTime_interval = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workTime_interval);
            holder.tv_dm05_r01p_adapter_workTime_to = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workTime_to);
            holder.tv_dm05_r01p_adapter_workTime_sum = (TextView) convertView
                    .findViewById(R.id.tv_dm05_r01p_adapter_workTime_sum);
            holder.btn_dm05_r01p_adapter_delete = (TextView) convertView.findViewById(R.id.btn_dm05_r01p_adapter_delete);
            holder.ln_dm05_r01p_adapter_workTime = (LinearLayout) convertView.findViewById(R.id.ln_dm05_r01p_adapter_workTime);
            holder.ln_dm05_r01p_adapter = (LinearLayout) convertView.findViewById(R.id.ln_dm05_r01p_adapter);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_dm05_r01p_adapter_deptNm2.setText(item.getBLDG_NM());
        holder.tv_dm05_r01p_adapter_deptcd2.setText(item.getBLDG_NO());
        holder.tv_dm05_r01p_adapter_workNm2.setText(item.getWORK_NM());

        holder.tv_dm05_r01p_adapter_workTime_fr.setText(item.getCS_TM_FR());
        holder.tv_dm05_r01p_adapter_workTime_to.setText(item.getCS_TM_TO());
        holder.tv_dm05_r01p_adapter_workTime_sum.setText(item.getCS_TM());
        if (!item.getCS_TM_FR().equals("") && !item.getCS_TM_TO().equals("")) {
            holder.tv_dm05_r01p_adapter_workTime_interval.setText("~");
        } else {
            holder.tv_dm05_r01p_adapter_workTime_interval.setText(null);
        }

        if (item.getDEL_CHK().equals("1")) {
            holder.btn_dm05_r01p_adapter_delete.setVisibility(View.VISIBLE);
        } else {
            holder.btn_dm05_r01p_adapter_delete.setVisibility(View.INVISIBLE);
        }
        if (onClickListener != null) {
            holder.btn_dm05_r01p_adapter_delete.setTag(R.id.btn_dm05_r01p_adapter_delete, position);
            holder.btn_dm05_r01p_adapter_delete.setOnClickListener(onClickListener);
            if (dataList.get(position).getWORK_TP().equals("10"))  {
                holder.tv_dm05_r01p_adapter_workNm2.setTextColor(context.getResources().getColor(R.color.primary_text_default_material_light));
                holder.ln_dm05_r01p_adapter_workTime.setTag(R.id.ln_dm05_r01p_adapter_workTime, position);
                holder.ln_dm05_r01p_adapter_workTime.setOnClickListener(onClickListener);
                holder.ln_dm05_r01p_adapter_workTime.setBackgroundResource(R.drawable.border);
            } else {;
                holder.tv_dm05_r01p_adapter_workNm2.setTextColor(context.getResources().getColor(R.color.blue));
                holder.ln_dm05_r01p_adapter_workTime.setBackgroundResource(R.color.translucent);
                holder.ln_dm05_r01p_adapter_workTime.setOnClickListener(null);
                if (dataList.get(position).getREQUIRED_CHK_TM().equals("1")) {
                    holder.ln_dm05_r01p_adapter_workTime.setTag(R.id.ln_dm05_r01p_adapter_workTime, position);
                    holder.ln_dm05_r01p_adapter_workTime.setOnClickListener(onClickListener);
                    holder.ln_dm05_r01p_adapter_workTime.setBackgroundResource(R.drawable.border);
                }
            }
        }
        return convertView;
    }



}