package com.jinsit.kmec.IR.CI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.jinsit.kmec.R;
import com.jinsit.kmec.IR.PI.IR_PI04_R00_Item;
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.AlertView;
import com.jinsit.kmec.comm.jinLib.IntentActionUtils;
import com.jinsit.kmec.comm.jinLib.SysUtil;

public class IR_CI04_R02P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;
	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	///////////
	private IR_PI04_R00_Item item;


	private TextView tv_ci_itemNo;
	private TextView tv_ci_itemNm;
	private TextView tv_ci_size;
	private TextView tv_ci_unitPrc;
	private TextView btn_ci_image;
	private Bitmap bitmap;

	public IR_CI04_R02P(Context context, IR_PI04_R00_Item item,Bitmap bm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.item = item;
		this.context = context;
		this.bitmap = bm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_ci04_r02p);
		activityInit();
	}

	protected void activityInit() {

		getInstances();
		this.tv01_popTitle.setText("부품판가상세");
		this.tv_ci_itemNo.setText(this.item.getItemNo());
		this.tv_ci_itemNm.setText(this.item.getItemNm());
		this.tv_ci_size.setText(this.item.getSize());
		this.tv_ci_unitPrc.setText(SysUtil.makeStringWithComma(this.item.getUnitPrc(),true));
		if(this.item.getImgChk().equals("1")){
			//이미지 있음
			btn_ci_image.setText("사진보기");
		}else{
			//이미지 없음
			btn_ci_image.setText("사진없음");
		}
	}

	protected void getInstances() {
		tv01_popTitle= (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);

		tv_ci_itemNo = (TextView) findViewById(R.id.tv_ci_itemNo);
		tv_ci_itemNm = (TextView) findViewById(R.id.tv_ci_itemNm);
		tv_ci_size = (TextView) findViewById(R.id.tv_ci_size);
		tv_ci_unitPrc = (TextView) findViewById(R.id.tv_ci_unitPrc);
		btn_ci_image = (TextView)findViewById(R.id.btn_ci_image);


		setEvents();
	}

	protected void setEvents() {
		btn_popClose.setOnClickListener(this);
		btn_ci_image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				this.dismiss();
				break;
			case R.id.btn_ci_image:
				if(bitmap!=null){
					detailImg();
				}else{
					AlertView.showError("이미지가 없습니다.", context);
				}
				break;

			default:
				break;
		}
	}

	void detailImg(){
		try {

			File file = new File(AbsoluteFilePath.PDF_PATH + "jintemp.jpg");
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, outStream);

			// Stream 객체를 모두 닫는다.
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		Uri uri = Uri.parse(AbsoluteFilePath.PDF_PATH + "jintemp.jpg");
		IntentActionUtils.actionImage(context, uri);
	}

}