package com.jinsit.kmec.comm.jinLib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

public class IntentActionUtils {

	public static void callAction(final Context context, final String number){

		new AlertDialog.Builder(context)
				.setMessage("상대방에게 전화를 겁니다.\n통화료가 부과됩니다.")
				.setPositiveButton("예",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub

								try{

									Intent callintent=new Intent(Intent.ACTION_CALL);
									callintent.setData(Uri.parse("tel:" + number));//번호 입력
									context.startActivity(callintent);
								}catch(Exception e){
									Toast.makeText(context,"통화연결에 실패하였습니다.",Toast.LENGTH_SHORT).show();
								}

							}
						})
				.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								return;
							}
						}).show();



	}


	public  static void iamgeCKUri(final Context context,String imageNo) {
		// TODO Auto-generated method stub

		File file = new File(context.getCacheDir(), imageNo);
		Uri uri = FileProvider.getUriForFile(context, "com.jinsit.dogplus", file);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		//Uri uri = Uri.parse(imageNo);
		intent.setDataAndType(uri, "image/*");
		context.startActivity(intent);

	}

	/**
	 * 인텐트 액션으로 이미지 표시 (갤러리, 사진등 선택할 수 있도록 호출)
	 */
	public static void actionImage(Context context, Uri uri){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "image/*");
		context.startActivity(intent);
	}
}
