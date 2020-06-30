package com.jinsit.kmec.DM.DM;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimePickerStartEndActivity extends Activity implements View.OnClickListener {
    private Context context;
    private TextView tv_dm05_start_dt;
    private TextView tv_dm05_end_dt;
    private TimePicker tp_timePicker_start;
    private TimePicker tp_timePicker_end;
    private Button btn_timepicker_ok;
    private DatePickerDialog.OnDateSetListener mDataSetListener_start, mDataSetListener_end;

    private String selectDt, base_limit_mi, job_fr, job_to;
    private int fromHour;
    private int fromMinute;
    private int toHour;
    private int toMinute;
    String date_diff;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker_start_end);
        activityInit();
    }

    protected void activityInit() {
        getInstances();
        context = this;

    }

    private void getInstances() {
        this.tp_timePicker_start = (TimePicker) findViewById(R.id.tp_timePicker_start);
        this.tp_timePicker_end = (TimePicker) findViewById(R.id.tp_timePicker_end);
        this.btn_timepicker_ok = (Button) findViewById(R.id.btn_timepicker_ok);
        tv_dm05_start_dt = (TextView) findViewById(R.id.tv_dm05_start_dt);
        tv_dm05_end_dt = (TextView) findViewById(R.id.tv_dm05_end_dt);
        setEvents();
    }

    private void setEvents() {
        if (getIntent().getExtras() != null) {

            String workTp = getIntent().getExtras().getString("workTp");
            if (workTp.equals("10")) {
                timePickerInterval = 10;
            } else {
                timePickerInterval = 30;
            }
            isEarlyWork = getIntent().getExtras().getBoolean("isEarlyWork", false);

            selectDt = getIntent().getExtras().getString("selectDt");
            base_limit_mi = getIntent().getExtras().getString("base_limit_mi");
            job_fr = getIntent().getExtras().getString("job_fr");
            job_to = getIntent().getExtras().getString("job_to");
            fromHour = getIntent().getExtras().getInt("fromHour", 0);
            fromMinute = getIntent().getExtras().getInt("fromMinute", 0);
            toHour = getIntent().getExtras().getInt("toHour", 0);
            toMinute = getIntent().getExtras().getInt("toMinute", 0);
            tp_timePicker_start.setCurrentHour(fromHour);
            tp_timePicker_start.setCurrentMinute(fromMinute);
            tp_timePicker_end.setCurrentHour(toHour);
            tp_timePicker_end.setCurrentMinute(toMinute);
        }
        this.btn_timepicker_ok.setOnClickListener(this);
        if (job_fr.equals("") && job_to.equals("")) {
            tv_dm05_start_dt.setText(selectDt);
            tv_dm05_end_dt.setText(selectDt);
        } else {
            tv_dm05_start_dt.setText(job_fr.substring(0, 10));
            tv_dm05_end_dt.setText(job_to.substring(0, 10));
        }
        dateClick();

    }

    boolean isEarlyWork = false;
    private static final int EARLY_WORK_HOUR = 8;
    private static final int EARLY_WORK_MINITE = 30;

    private void dateClick() {
        final Calendar minCal = Calendar.getInstance();
        final Calendar maxCal = Calendar.getInstance();
        Date selectDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            selectDate = dateFormat.parse(selectDt);
            minCal.setTime(selectDate);
            maxCal.setTime(selectDate);
            maxCal.add(Calendar.DATE, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // month 는 API 반환 Index + 1
        tv_dm05_start_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDt = tv_dm05_start_dt.getText().toString();
                int year, month, day;
                year = Integer.parseInt(startDt.split("-")[0]);
                month = Integer.parseInt(startDt.split("-")[1]) - 1;
                day = Integer.parseInt(startDt.split("-")[2]);
                DatePickerDialog dialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, mDataSetListener_start, year, month, day);
                dialog.getDatePicker().setMinDate(minCal.getTime().getTime());
                dialog.getDatePicker().setMaxDate(maxCal.getTime().getTime());
                dialog.getDatePicker().setOnFocusChangeListener(null);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        tv_dm05_end_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endDt = tv_dm05_end_dt.getText().toString();
                int year, month, day;
                year = Integer.parseInt(endDt.split("-")[0]);
                month = Integer.parseInt(endDt.split("-")[1]) - 1;
                day = Integer.parseInt(endDt.split("-")[2]);
                DatePickerDialog dialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, mDataSetListener_end, year, month, day);
                dialog.getDatePicker().setMinDate(minCal.getTime().getTime());
                dialog.getDatePicker().setMaxDate(maxCal.getTime().getTime());
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListener_start = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String s_year, s_month, s_day;
                s_year = String.valueOf(year);
                s_month = String.valueOf(month + 1);
                s_day = String.valueOf(day);
                if (month < 9) {
                    s_month = "0" + String.valueOf(month + 1);
                }
                if (day < 10) {
                    s_day = "0" + String.valueOf(s_day);
                }
                String date = String.valueOf(s_year) + "-" + String.valueOf(s_month) + "-" + String.valueOf(s_day);
                tv_dm05_start_dt.setText(date);
            }
        };
        mDataSetListener_end = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String s_year, s_month, s_day;
                s_year = String.valueOf(year);
                s_month = String.valueOf(month + 1);
                s_day = String.valueOf(day);
                if (month < 9) {
                    s_month = "0" + String.valueOf(month + 1);
                }
                if (day < 10) {
                    s_day = "0" + String.valueOf(s_day);
                }
                String date = String.valueOf(s_year) + "-" + String.valueOf(s_month) + "-" + String.valueOf(s_day);
                tv_dm05_end_dt.setText(date);
            }
        };
    }


    private void setTimePickerResult() {
        fromHour = tp_timePicker_start.getCurrentHour();
        fromMinute = Integer.parseInt(displayedValues.get(tp_timePicker_start.getCurrentMinute()));
        toHour = tp_timePicker_end.getCurrentHour();
        toMinute = Integer.parseInt(displayedValues.get(tp_timePicker_end.getCurrentMinute()));

        date_diff = "";
        // 시작시간보다 종료시간이 이전일 경우, 검사.
        String frDt = tv_dm05_start_dt.getText().toString() + " " + fromHour + ":" + fromMinute;
        String toDt = tv_dm05_end_dt.getText().toString() + " " + toHour + ":" + toMinute;
        try {
            SimpleDateFormat dset = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date FromDate = dset.parse(frDt);           // 시작 시간
            Date ToDate = dset.parse(toDt);             // 종료 시간
            long diff = (ToDate.getTime() - FromDate.getTime()) / 1000 / 60;

            if (diff < 0) {
                AlertView.showAlert("시작시간 이전으로 종료시간을 설정할 수 없습니다.", this);
                return;
            }

            // 고장수리지원, 조기출근은 base_limit_mi가 ""로 input -> 기존 작업시간이 없으므로 조정 제한 없음.
            if(!job_fr.equals("") && !job_to.equals("")) {
                Date JobFrDate = dset.parse(job_fr);             // 기존 시작 시간
                Date JobToDate = dset.parse(job_to);             // 기존 종료 시간
                long job_fr_diff = Math.abs(JobFrDate.getTime() - FromDate.getTime()) / 1000 / 60;
                long job_to_diff = Math.abs(JobToDate.getTime() - ToDate.getTime()) / 1000 / 60;
                if (job_fr_diff > Long.parseLong(base_limit_mi) || job_to_diff > Long.parseLong(base_limit_mi)) {
                    AlertView.showAlert("조정 제한은 기존 작업시간의 ±" + base_limit_mi +"분입니다.", this);
                    return;
                }
            }

            String earlyWorkTime = selectDt + " " + EARLY_WORK_HOUR + ":" + EARLY_WORK_MINITE;
            Date earlyWorkDate = dset.parse(earlyWorkTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(earlyWorkDate);
            if (isEarlyWork) {
                calendar.add(Calendar.DATE, 1);     // 선택한 날짜의 다음날
                earlyWorkDate = calendar.getTime();         // 익일 오전 8:30분 까지만 설정할 수 있으므로.
                long fromDiff = (earlyWorkDate.getTime() - FromDate.getTime()) / 1000 / 60;
                long toDiff = (earlyWorkDate.getTime() - ToDate.getTime()) / 1000 / 60;
                if (fromDiff < 0 || toDiff < 0) {
                    AlertView.showAlert("조기출근은 익일 8:30분 이후로 설정할 수 없습니다.", this);
                    return;
                }
            } else {
                earlyWorkDate = calendar.getTime();         // 선택일자 오전 8:30분
                long fromDiff = (FromDate.getTime() - earlyWorkDate.getTime()) / 1000 / 60;
                long toDiff = (ToDate.getTime() - earlyWorkDate.getTime()) / 1000 / 60;
                if (fromDiff < 0 || toDiff < 0) {
                    AlertView.showAlert("오전 8:30분 이전으로 설정할 수 없습니다.", this);
                    return;
                }
            }

            date_diff = "(" + diff + "분)";

        } catch (ParseException e) {
            e.getMessage();
        }

        Bundle b = new Bundle();
        b.putInt("fromHour", fromHour);
        b.putInt("fromMinute", fromMinute);
        b.putInt("toHour", toHour);
        b.putInt("toMinute", toMinute);
        b.putString("startDt", tv_dm05_start_dt.getText().toString());
        b.putString("endDt", tv_dm05_end_dt.getText().toString());
        b.putString("date_diff", date_diff);
        // Add the set of extended data to the intent and start it
        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_timepicker_ok:
                setTimePickerResult();
                break;
        }
    }

    private int timePickerInterval = 10;
    List<String> displayedValues = new ArrayList<String>();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Class<?> classForid = Class.forName("com.android.internal.R$id");
            Field timePickerField = classForid.getField("timePicker");
            //this.tp_timePicker_start = (TimePicker) findViewById(timePickerField.getInt(null));

            Field hourField = classForid.getField("hour");
            Field minuteField = classForid.getField("minute");

            NumberPicker mHourSpinner = (NumberPicker) tp_timePicker_start.findViewById(hourField.getInt(null));
            NumberPicker mHourSpinner2 = (NumberPicker) tp_timePicker_end.findViewById(hourField.getInt(null));
            NumberPicker mMinuteSpinner = (NumberPicker) tp_timePicker_start.findViewById(minuteField.getInt(null));
            NumberPicker mMinuteSpinner2 = (NumberPicker) tp_timePicker_end.findViewById(minuteField.getInt(null));

            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / timePickerInterval) - 1);
            mMinuteSpinner2.setMinValue(0);
            mMinuteSpinner2.setMaxValue((60 / timePickerInterval) - 1);

            int fromDefaultIndex = 0;
            int toDefaultInex = 0;
            displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += timePickerInterval) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[0]));
            mMinuteSpinner2.setDisplayedValues(displayedValues.toArray(new String[0]));

            for (int i = 0; i < displayedValues.size(); i++) {
                if (String.format("%02d", fromMinute).equals(displayedValues.get(i))) {
                    fromDefaultIndex = i;
                }
                if (String.format("%02d", toMinute).equals(displayedValues.get(i))) {
                    toDefaultInex = i;
                }
            }

            tp_timePicker_start.setCurrentMinute(fromDefaultIndex);
            tp_timePicker_end.setCurrentMinute(toDefaultInex);
            mHourSpinner.setOnValueChangedListener(null);
            mHourSpinner2.setOnValueChangedListener(null);
            mMinuteSpinner.setOnValueChangedListener(null);
            mMinuteSpinner2.setOnValueChangedListener(null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
