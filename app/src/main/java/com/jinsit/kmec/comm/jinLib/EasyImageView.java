package com.jinsit.kmec.comm.jinLib;


import android.content.Context;
import android.widget.ImageView;

public class EasyImageView extends ImageView{

	public EasyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.isImage = false;
	}
	
	private boolean isImage;
	
	public boolean isImage() {
		return isImage;
	}
	public void setIsImage(boolean isImage) {
		this.isImage = isImage;
	}
	

}
