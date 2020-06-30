package com.jinsit.kmec.WO.WT.RI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jinsit.kmec.R;

/**
 * 정기점검 항목 (네비게이션드로우)
 * @author 원성민
 *
 */
public class RoutineCheckList extends Fragment implements OnItemClickListener,OnClickListener {

	private ListView lv_workTarget = null;
	private Activity activity = null;
	private ProgressDialog ProgressDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("workTarget: ", key);
			}
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.wo_wt00_r01f, container, false);
		onCreateViewInit(view);
		return view;

	}

	private void onCreateViewInit(View view) {
		// TODO Auto-generated method stub
	
	
		//lv_workTarget = (ListView) view.findViewById(R.id.lv_workTarget);
		initialize();
		
	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			for (String key : savedInstanceState.keySet()) {
				Log.v("DogPlus list savedInstanceState key: ", key);
			}
		}
		super.onActivityCreated(savedInstanceState);
		System.out.println("SaleListFragmentActivity.onActivityCrated");

	}

	private void initialize() {
		// TODO Auto-generated method stub
		new WorkTargetAsync().execute();
				
	
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
	}
	
	private class WorkTargetAsync extends AsyncTask<Void, String, Void> {
  
		@Override
		protected void onPostExecute(Void result) {
			  RoutineCheckList.this.ProgressDialog.dismiss();
			  
		}

		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		    RoutineCheckList.this.ProgressDialog = android.app.ProgressDialog.show(
		    		activity,"작업대상","작업대상목록 불러오는중");
		    		
		}

		
		
		@Override
		protected Void doInBackground(Void... params) {
		
			return null;
		}
		
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
	
		}
		
	}
	

}
