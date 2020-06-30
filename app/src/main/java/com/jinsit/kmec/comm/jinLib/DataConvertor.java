package com.jinsit.kmec.comm.jinLib;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Base64;

public class DataConvertor {
	
	public static byte[] Base64Byte(String str){
		byte[] bytes = null;
		if(!str.isEmpty()){
			bytes = Base64.decode(str, Base64.DEFAULT);
			return bytes;
		}else if(str.isEmpty()){
			bytes = new byte[0];
		}
		
		return bytes;
	}
	
	public static Bitmap Base64Bitmap(String str){
		Bitmap bitmap = null;
		if(!str.isEmpty()){
			byte[] bytes = null;
			bytes = Base64.decode(str, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		
		return bitmap;
	}
	
	
	public static String ByteBase64(byte[] bytes){
		String str = "";
		if(bytes.length > 0 || bytes != null){
			str = Base64.encodeToString(bytes, Base64.DEFAULT);
		}
		
		return str;
	}
	
	public static byte[] BitmapByte(Bitmap bitmap){
		byte[] bytes = null;
		//if(bitmap != null){
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, stream);
			bytes = stream.toByteArray();
		//}
		
		return bytes; 
	}
	
	public static String BitmapBase64(Bitmap bitmap){
		String str = "";
		//if(bitmap != null){
			str = ByteBase64( BitmapByte(bitmap) );
		//}
		
		return str;
	}
	
};



