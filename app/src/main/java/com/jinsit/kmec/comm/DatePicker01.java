package com.jinsit.kmec.comm;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

/**
 * @class DatePicker01
 * @author 원성민
 * @description 년월일, 년월, 월일 호출가능
 *  arg CHOICE가 0=YMD,1=YM,2=MD
 */
public class DatePicker01 extends DatePickerDialog {
	private static int CHOICE = 0;

	public DatePicker01(Context context, OnDateSetListener callBack, int year,
						int monthOfYear, int dayOfMonth, int choice) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
		CHOICE = choice;
		if (choice == 1)
			ymPicker();
		if (choice == 2)
			mdPicker();

		updateTitle(year, monthOfYear, dayOfMonth, choice);
	}

	@Override
	public void onDateChanged(DatePicker paramDatePicker, int year,
							  int monthOfYear, int dayOfMonth) {
		super.onDateChanged(paramDatePicker, year, monthOfYear, dayOfMonth);
		updateTitle(year, monthOfYear, dayOfMonth, CHOICE);
	}
	/**
	 * MD 월일만 표시함
	 */
	private void mdPicker() {
		try {
			Field[] datePickerDialogFields = DatePickerDialog.class
					.getDeclaredFields();
			for (Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField
							.get(this);
					Field datePickerFields[] = datePickerDialogField.getType()
							.getDeclaredFields();
					for (Field datePickerField : datePickerFields) {
						if ("mYearPicker".equals(datePickerField.getName())
								|| "mYearSpinner".equals(datePickerField
								.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}

			}
		} catch (Exception ex) {
		}
	}
	/**
	 * YM 년월만 표시함
	 */
	private void ymPicker() {
		try {
			Field[] datePickerDialogFields = DatePickerDialog.class
					.getDeclaredFields();
			for (Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField
							.get(this);
					Field datePickerFields[] = datePickerDialogField.getType()
							.getDeclaredFields();
					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName())
								|| "mDaySpinner".equals(datePickerField
								.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}

			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		super.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void updateTitle(int year, int month, int day, int choice) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		setTitle(getFormat(choice).format(c.getTime()));
	}

	@SuppressLint("SimpleDateFormat")
	public SimpleDateFormat getFormat(int paramInt) {
		if (paramInt == 1)
			return new SimpleDateFormat("yyyy-MM");
		if (paramInt == 2)
			return new SimpleDateFormat("MM-dd");
		return new SimpleDateFormat("yyyy-MM-dd");
	}
}