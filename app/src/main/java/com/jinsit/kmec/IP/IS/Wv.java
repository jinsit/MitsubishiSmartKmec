package com.jinsit.kmec.IP.IS;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinsit.kmec.R;
import com.jinsit.kmec.comm.jinLib.DataConvertor;
import com.jinsit.kmec.comm.jinLib.EasyDownLoad;
import com.jinsit.kmec.comm.jinLib.EasyJsonList;
import com.jinsit.kmec.comm.jinLib.EasyJsonMap;
import com.jinsit.kmec.comm.jinLib.WebServerInfo;
import com.jinsit.kmec.webservice.GetHttp;

public class Wv extends Activity {

	TextView wv_textView1;
	EditText wv_et01;
	
	Button wv_btn01_get;
	Button wv_btn01_set;
	Button wv_btn02_get;
	Button wv_btn02_set;
	ImageView wv_imageView1;
	
	static String returnString999;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wv_image);
		
		wv_textView1 	= (TextView) findViewById(R.id.wv_textView1);
		wv_et01 		= (EditText) findViewById(R.id.wv_et01);
		wv_btn01_get 	= (Button) findViewById(R.id.wv_btn01_get);
		wv_btn01_set 	= (Button) findViewById(R.id.wv_btn01_set);
		wv_btn02_get 	= (Button) findViewById(R.id.wv_btn02_get);
		wv_btn02_set 	= (Button) findViewById(R.id.wv_btn02_set);
		wv_imageView1 	= (ImageView) findViewById(R.id.wv_imageView1);
		
		wv_btn01_get.setOnClickListener(listener);
		wv_btn01_set.setOnClickListener(listener);
		wv_btn02_get.setOnClickListener(listener);
		wv_btn02_set.setOnClickListener(listener);
	}
	
	
	String base64Img;
	OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.wv_btn01_get:
				
				
				String folderToAccess = "PCSZ290M";
				String fileName = wv_et01.getText().toString();
				String empId = "301223";
				Bitmap bm = EasyDownLoad.getImage(folderToAccess, fileName, empId);
				
				if(bm == null){
					System.out.println("bm is null");
				}
				wv_imageView1.setImageBitmap(bm);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				
				String base64Img77 = DataConvertor.ByteBase64(byteArray);
				returnString999 = base64Img77;
				
		        
				break;
			case R.id.wv_btn01_set:
				
				new SetFileToFTP().execute();
				
				break;
			case R.id.wv_btn02_get:
				
				new GetImageFromDB().execute();
				
				break;
			case R.id.wv_btn02_set:
				
				new SetImageToDB().execute();
				
				break;

			}
			
		}
	};
	
	
	
	static class SetFileToFTP extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			
			GetHttp http = null;
			String param_url = WebServerInfo.getUrl()+"comm/setFileToFTP.do";
			
			ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
			arguments.add(new BasicNameValuePair("folderToAccess", "PCSF010S"));
			arguments.add(new BasicNameValuePair("fileName", "tttt.jpg"));
			arguments.add(new BasicNameValuePair("IMG1", returnString999));
			http = new GetHttp();
			returnJson = http.getPost(param_url, arguments, true);
			
			try {
				
				EasyJsonMap ejm = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
		}
		
	}
	
	
	
	
	static JSONObject returnJson = null;
	private class GetImageFromDB extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {

    		GetHttp http = null;
    		String param_url = WebServerInfo.getUrl()+"comm/getImageFromDB.do";
    		
    		ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
    		http = new GetHttp();
    		returnJson = http.getPost(param_url, arguments, true);

    		try {
				
    			EasyJsonList ejl = new EasyJsonList(returnJson.getJSONArray("dataList"));
    			base64Img = ejl.getValue(0, "IMG1");
    			
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			Bitmap bm = DataConvertor.Base64Bitmap(base64Img);
			wv_imageView1.setImageBitmap( bm );
		}
		
	}
	
	
	
	
	private class SetImageToDB extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			
    		GetHttp http = null;
    		String param_url = WebServerInfo.getUrl()+"comm/setImageToDB.do";
    		
    		ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
    		arguments.add(new BasicNameValuePair("IMG1", returnString999));
    		arguments.add(new BasicNameValuePair("IMG2", returnString999));
    		
    		http = new GetHttp();
    		returnJson = http.getPost(param_url, arguments, true);
			
			try {
				EasyJsonMap ejm = new EasyJsonMap(returnJson.getJSONObject("dataMap"));
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
			
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	byte[] bytes_song_byte = null;
	EasyJsonList ejl = null;
	List<NameValuePair> arguments = null;
	String returnString = "0";
	
	byte[] byt = null;
	byte[] fby = null;
	String str = null;
	String a = null;
	public class Render extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			
			
    		GetHttp http = null;
    		String param_url = WebServerInfo.getUrl()+"comm/upLoadImg.do";
    		
    		arguments = new ArrayList<NameValuePair>();
    		
    		http = new GetHttp();
    		returnJson = http.getPost(param_url, arguments, false);
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			try {
				EasyJsonMap ejm = new EasyJsonMap(returnJson.getJSONObject("dataImage"));
				returnString = ejm.getValue("IMG1");
				returnString999 = returnString;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			byte[] bytes = Base64.decode(returnString, Base64.DEFAULT);
			Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			wv_imageView1.setImageBitmap( bm );
			
			//wv_imageView1.setImageBitmap( DataConvertor.Base64Bitmap(returnString) );
		}
		
	}
	
	
	
	private class UploadImgToDB extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {


    		GetHttp http = null;
    		String param_url = WebServerInfo.getUrl()+"comm/uploadImgToDB.do";
    		
    		ArrayList<NameValuePair> arguments = new ArrayList<NameValuePair>();
    		arguments.add(new BasicNameValuePair("IMG1", returnString999));
    		arguments.add(new BasicNameValuePair("IMG2", returnString999));
    		
    		http = new GetHttp();
    		returnJson = http.getPost(param_url, arguments, false);
			
			
			return null;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
	*/

	
	
};
