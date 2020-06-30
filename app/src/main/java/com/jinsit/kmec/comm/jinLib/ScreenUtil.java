package com.jinsit.kmec.comm.jinLib;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;

public class ScreenUtil {

	/**
	 * 스크린의 리얼 Height를 가져온다.갤럭시 8~이상의 내비게이션바가 없을 때 리얼사이즈를 가져옴
	 * @param activity
	 * @return
	 */
	public static int getRealScreenHeight(Activity activity){
		View decorView = activity.getWindow().getDecorView();
		Rect r = new Rect();
		decorView.getWindowVisibleDisplayFrame(r);
		int screenHeight = r.bottom;
		return screenHeight;
	}
}
