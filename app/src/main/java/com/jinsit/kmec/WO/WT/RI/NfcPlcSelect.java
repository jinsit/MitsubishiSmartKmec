package com.jinsit.kmec.WO.WT.RI;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinsit.kmec.DB.DatabaseInfo;
import com.jinsit.kmec.DB.DatabaseRawQuery;
import com.jinsit.kmec.DB.KMecSQLOpenHelper;

public class NfcPlcSelect {
	public static WT_RI00_R01_ITEM02 selectNfcTagId(Context context, String tagId,
			String jobNo) {
		WT_RI00_R01_ITEM02 nfcItem = new WT_RI00_R01_ITEM02();
		String query = new DatabaseRawQuery().selectNfcUdid(tagId);
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		ArrayList<WT_RI00_R01_ITEM02> arrayWT_RI00_R01_ITEM02 = new ArrayList<WT_RI00_R01_ITEM02>();
		WT_RI00_R01_ITEM02 wT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
		Field[] fields = wT_RI00_R01_ITEM02.getClass().getDeclaredFields();
		String name = "";
		String value = "";

		WT_RI00_R01_ITEM02 insertWT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
		if (mCursor != null && mCursor.getCount() != 0) {
			do {

				insertWT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
				for (Field field : fields) {
					name = field.getName();
					try {

						value = mCursor.getString(mCursor.getColumnIndex(name));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						field.set(insertWT_RI00_R01_ITEM02, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				arrayWT_RI00_R01_ITEM02.add(insertWT_RI00_R01_ITEM02);
			} while (mCursor.moveToNext());

		} else {

		}
		mCursor.close();
		db.close();
		dbHelper.close();

		int size = arrayWT_RI00_R01_ITEM02.size();

		if (size != 0) {
			for (int i = 0; i < size; i++) {
				if (jobNo.equals(arrayWT_RI00_R01_ITEM02.get(i).getJOB_NO())) {
					nfcItem = arrayWT_RI00_R01_ITEM02.get(i);
					break;
				}

			}
		}
		return nfcItem;
	}
	
	public static WT_RI00_R01_ITEM02 selectNfcTagIdDebug(Context context, String tagId,
			String jobNo, String plc, String carNo) {
		WT_RI00_R01_ITEM02 nfcItem = new WT_RI00_R01_ITEM02();
		String query = new DatabaseRawQuery().selectNfcUdidDebug(tagId,carNo, plc);
		KMecSQLOpenHelper dbHelper = new KMecSQLOpenHelper(context,
				DatabaseInfo.getDBName(), null, DatabaseInfo.getDBVersion());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null); // 쿼리 날리고
		mCursor.moveToFirst();

		ArrayList<WT_RI00_R01_ITEM02> arrayWT_RI00_R01_ITEM02 = new ArrayList<WT_RI00_R01_ITEM02>();
		WT_RI00_R01_ITEM02 wT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
		Field[] fields = wT_RI00_R01_ITEM02.getClass().getDeclaredFields();
		String name = "";
		String value = "";

		WT_RI00_R01_ITEM02 insertWT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
		if (mCursor != null && mCursor.getCount() != 0) {
			do {

				insertWT_RI00_R01_ITEM02 = new WT_RI00_R01_ITEM02();
				for (Field field : fields) {
					name = field.getName();
					try {

						value = mCursor.getString(mCursor.getColumnIndex(name));
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						field.set(insertWT_RI00_R01_ITEM02, value);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				arrayWT_RI00_R01_ITEM02.add(insertWT_RI00_R01_ITEM02);
			} while (mCursor.moveToNext());

		} else {

		}
		mCursor.close();
		db.close();
		dbHelper.close();

		int size = arrayWT_RI00_R01_ITEM02.size();

		if (size != 0) {
			for (int i = 0; i < size; i++) {
				if (jobNo.equals(arrayWT_RI00_R01_ITEM02.get(i).getJOB_NO())) {
					nfcItem = arrayWT_RI00_R01_ITEM02.get(i);
					break;
				}

			}
		}
		return nfcItem;
	}
	
}
