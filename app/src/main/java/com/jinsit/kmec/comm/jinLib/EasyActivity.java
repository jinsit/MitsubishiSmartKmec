package com.jinsit.kmec.comm.jinLib;

import com.jinsit.kmec.R;
import com.jinsit.kmec.widget.HomeNavigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class EasyActivity extends Activity{

	protected boolean isNaviToggle = false;
	protected HomeNavigation hn_homeNavi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hn_homeNavi = new HomeNavigation(this, null);
		hn_homeNavi = (HomeNavigation) findViewById(R.id.hn_homeNavi);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			navigationToggle();
			isNaviToggle = !isNaviToggle;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void navigationToggle() {
		hn_homeNavi.setVisibility(isNaviToggle ? View.VISIBLE : View.GONE);
	}

}
