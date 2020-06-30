package com.jinsit.kmec.WO.WT.RI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinsit.kmec.R;
import com.jinsit.kmec.WO.WO_WO00_R00F;

public class NextWorkDialog extends AlertDialog {

	public btnClickListener bntListener;
	public OnItemClickListener listener;
	String msg;
	ListView lv_dialogList;
	ListAdapter ladpater = null;
	Button btn_wo_dialog_nextCar, btn_wo_dialog_cunsumerCounsel,
			btn_wo_dialog_etc;
	boolean isCounsel = false;

	Activity activity;
	Context context;
	int tCount = 0;
	RoutineCheckListData routineData;
	public interface btnClickListener {
		void onButtonClick();
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
		super.setMessage(message);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comm_nextwork_dialog);

		TextView message = (TextView) findViewById(R.id.tv_dialogMessage);
		btn_wo_dialog_nextCar = (Button) findViewById(R.id.btn_wo_dialog_nextCar);
		btn_wo_dialog_cunsumerCounsel = (Button) findViewById(R.id.btn_wo_dialog_cunsumerCounsel);
		btn_wo_dialog_etc = (Button) findViewById(R.id.btn_wo_dialog_etc);
		if (message != null)
			message.setText(msg);

		
		//if(routineData.getI_CNT() == null) routineData.setI_CNT("0");
		//if(routineData.getT_CNT() == null) routineData.setT_CNT("0");
		// 왜 이부분 routineData가 널로 나오는지 먼저 찾아봐야함. 어떤조건에 널이 되는지 봐보도록 하여라
		int tCnt = Integer.valueOf(routineData.getT_CNT());
		int iCnt = Integer.valueOf(routineData.getI_CNT());
		if(tCnt-iCnt==0){
			isCounsel =true;
			tCount = tCnt;
		}
		
		btn_wo_dialog_nextCar
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						activity.finish();
						cancel();
					}

				});

		btn_wo_dialog_cunsumerCounsel
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (isCounsel) {
							
							RoutineCheckListData mData =new RoutineCheckListData();
							mData = routineData;
							Bundle extras = new Bundle();
						
							
//							Intent intent = new Intent(activity,
//									WO_WT00_R03.class);
							Intent intent = new Intent(activity,
									WO_WT00_R03_MNG.class);
							extras.putSerializable("obj", mData);
							intent.putExtras(extras);
							intent.putExtra("pagerCount", tCount);
							activity.startActivity(intent);
							activity.finish();
							cancel();
							
							
						} else {
							Toast.makeText(context, "모든호기의 점검이 완료되지 않았습니다.",
									2000).show();
						}

					}

				});

		btn_wo_dialog_etc
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(activity, WO_WO00_R00F.class);
						intent.putExtra("fragment", 3);
						activity.startActivity(intent);
						activity.finish();
						cancel();
					}

				});
	}

	@Override
	public void setView(View view) {
		// TODO Auto-generated method stub
		super.setView(view);
	}

	public NextWorkDialog(Context context, String message,
			btnClickListener cancelListener, Activity activity,
			RoutineCheckListData mData) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.activity = activity;
		this.bntListener = cancelListener;
		this.msg = message;
		this.routineData = mData;
	}

}
