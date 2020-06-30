package com.jinsit.kmec.comm;

import android.app.TimePickerDialog;
import android.content.Context;

public class TimePicker01 extends TimePickerDialog
{

	public TimePicker01(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		// TODO Auto-generated constructor stub
	}
}