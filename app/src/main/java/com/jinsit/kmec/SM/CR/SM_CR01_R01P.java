package com.jinsit.kmec.SM.CR;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.AlertView;

public class SM_CR01_R01P extends AlertDialog
		implements android.view.View.OnClickListener{

	/**
	 * 등록에서 사진첨부page
	 */
	//uiInstances
	Context context;
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	private ImageView iv01_cr01_r01p;
	private ImageView iv02_cr01_r01p;
	private ImageView iv03_cr01_r01p;
	private Button btn01_endToAttach;

	//dto
	private Map<String, String> imgMap;
	private Map<String, Integer> idMap;
	private ProgressDialog progress;

	//parent
	SM_CR01_R00 parent;

	protected SM_CR01_R01P(Context context, Map imgMap) {
		super(context);
		this.imgMap = imgMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sm_cr01_r01p);
		getInstances();
	}

	private void getInstances(){
		context				= getContext();
		idMap				= new HashMap<String, Integer>();
		tv01_popTitle 		= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose 		= (TextView) findViewById(R.id.btn_popClose);
		iv01_cr01_r01p		= (ImageView) findViewById(R.id.iv01_cr01_r01p);
		iv02_cr01_r01p		= (ImageView) findViewById(R.id.iv02_cr01_r01p);
		iv03_cr01_r01p		= (ImageView) findViewById(R.id.iv03_cr01_r01p);
		btn01_endToAttach 	= (Button) findViewById(R.id.btn01_cr01_r01p);
		getId();
	}
	private void setEvents(){
		btn_popClose.setOnClickListener(this);
		btn01_endToAttach.setOnClickListener(this);
		setConfig();
	}
	private void setConfig(){
		tv01_popTitle.setText("Claim 사진등록");
	}
	private void getId(){
		idMap.put("iv01",R.id.iv01_cr01_r01p);
		idMap.put("iv02",R.id.iv02_cr01_r01p);
		idMap.put("iv03",R.id.iv03_cr01_r01p);
		idMap.put("BtnPopClose",R.id.btn_popClose);
		idMap.put("BtnEndToAttach",R.id.btn01_cr01_r01p);
		setEvents();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_popClose:
				dismissAfterCheck();
				break;
			case R.id.btn01_cr01_r01p:
				dismiss();
				break;
			default:
				Log.e("[개발자Msg]", "out of case");
				break;
		}
	}


	private void dismissAfterCheck(){

		boolean hasImg1 = imgMap.get("img1").toString().length() > 0;
		boolean hasImg2 = imgMap.get("img2").toString().length() > 0;
		boolean hasImg3 = imgMap.get("img3").toString().length() > 0;

		if( hasImg1 || hasImg2 || hasImg3 ){
			AlertView.confirm(context, "경고", "선택하신 사진이 삭제됩니다.\n계속 진행하시겠습니까?", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//image초기화
					imgMap.put("img1","");
					imgMap.put("img2","");
					imgMap.put("img3","");
					dismiss();
				}
			});
		}else{
			dismiss();
		}
	}


	public ImageView getIv01_cr01_r01p() {
		return iv01_cr01_r01p;
	}
	public ImageView getIv02_cr01_r01p() {
		return iv02_cr01_r01p;
	}
	public ImageView getIv03_cr01_r01p() {
		return iv03_cr01_r01p;
	}
	public Map getIdMap() {
		return this.idMap;
	}

	public Button getBtn01_endToAttach() {
		return btn01_endToAttach;
	}


	//utils
	public void alert(String msg, Context context){
		AlertView.showAlert(msg, context);
	}
	private void progress(Boolean isActivated){
		if(isActivated){
			SM_CR01_R01P.this.progress =
					android.app.ProgressDialog.show(context, "알림","조회 중입니다.");
		}else{
			SM_CR01_R01P.this.progress.dismiss();
		}
	}
};
