package com.jinsit.kmec.IR.PI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.jinsit.kmec.comm.jinLib.AbsoluteFilePath;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.IntentActionUtils;

public class IR_PI04_R01P extends AlertDialog implements
		android.view.View.OnClickListener {

	private Context context;

	///title 위젯
	private TextView tv01_popTitle;
	private TextView btn_popClose;
	///////////

	private ProgressDialog progress;

	private ImageView img_pi_detailImage;
	private TextView btn_pi_detailImage;
	private Bitmap bm;

	protected IR_PI04_R01P(Context context, Bitmap bm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.bm = bm;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ir_pi04_r01p);
		activityInit();
	}

	protected void activityInit() {
		getInstances();
		// /title 위젯
		tv01_popTitle.setText("부품");
		// //////////////////

		img_pi_detailImage.setImageBitmap(bm);
	}

	protected void getInstances() {
		// /title 위젯
		tv01_popTitle = (TextView) findViewById(R.id.tv01_popTitle);
		btn_popClose = (TextView) findViewById(R.id.btn_popClose);
		// //////////////////
		img_pi_detailImage = (ImageView) findViewById(R.id.img_pi_detailImage);
		btn_pi_detailImage = (TextView) findViewById(R.id.btn_pi_detailImage);
		setEvents();
	}

	protected void setEvents() {
		// /title 위젯
		this.btn_popClose.setOnClickListener(this);
		// //////////////////
		btn_pi_detailImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_popClose:
				this.dismiss();
				break;
			case R.id.btn_pi_detailImage:
				try {

					//	InputStream is = EasyDownLoad.getPDF("PCSZ290M",
					//			currentSelectedItem01.getFileNm(), commonSession.getEmpId());
					File file = new File(AbsoluteFilePath.PDF_PATH + "jintemp.jpg");
					OutputStream outStream = new FileOutputStream(file);
					bm.compress(CompressFormat.PNG, 100, outStream);

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
				this.dismiss();
				break;
			default:
				break;
		}
	}



	private void progress(Boolean isActivated) {
		if (isActivated) {
			IR_PI04_R01P.this.progress = android.app.ProgressDialog.show(
					context, "알림", "사진을 불러 오는 중입니다.");
		} else {
			IR_PI04_R01P.this.progress.dismiss();
		}
	}

}