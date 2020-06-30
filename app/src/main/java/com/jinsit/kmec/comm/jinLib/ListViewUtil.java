package com.jinsit.kmec.comm.jinLib;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;

public class ListViewUtil {

	/**
	 * @name ListViewHeightSet
	 * @param listAdapter
	 * @param listView
	 * @descript 스크롤뷰 안에서 리스트뷰가 동적으로 동작하도록 하는 메소드 리스트뷰의 한 item의 크기와  item 수를 곱해서 listview의 총 height를 구함
	 * 리스트뷰의 height를 설정하고 리스트뷰의 스크롤은 안먹고  스크롤뷰가 먹도록 한다.
	 */
	public static void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
