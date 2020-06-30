package com.jinsit.kmec.comm;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.jinsit.kmec.R;
/**
 * 이미지 상세보기
 * @author 원성민
 *
 */
public class ImageDetailView extends AlertDialog  {
	Context context;
	Bitmap bitmap;
	public ImageDetailView(Context c, Bitmap img) {
		super(c);
		// TODO Auto-generated constructor stub
		context = c;
		bitmap = img;
	}

	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		super.setTitle(titleId);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedetailview);
		ImageView img = (ImageView)findViewById(R.id.iv_imageDetail);
		img.setImageBitmap(bitmap);
	}





}
