package com.jinsit.kmec.comm.jinLib;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jinsit.kmec.webservice.GetHttp;

public class EasyDownLoad {

	public EasyDownLoad(){}

	public static Bitmap getImage(String folderToAccess, String fileName, String empId){

		Bitmap bm = requestImage(folderToAccess, fileName, empId);
		requestWSDel(folderToAccess, fileName, empId);

		return bm;
	}

	public static InputStream getPDF(String folderToAccess, String fileName, String empId){

		InputStream is = null;
		is = requestPDF(folderToAccess, fileName, empId);
		requestWSDel(folderToAccess, fileName, empId);

		return is;
	}



	private static Bitmap requestImage(String folderToAccess, String imgName, String empId){

		Bitmap bm = null;
		if(requestFTP(folderToAccess, imgName, empId).equals("ready")){
			bm = requestWS_bitMap(folderToAccess, imgName, empId);
		}else{
			System.out.println("[개발자Msg] no file");
		}

		return bm;
	}
	private static InputStream requestPDF(String folderToAccess, String imgName, String empId){

		InputStream is = null;
		if(requestFTP(folderToAccess, imgName, empId).equals("ready")){
			is = requestWS_inputStream(folderToAccess, imgName, empId);
		}else{
			System.out.println("[개발자Msg] no PDF");
		}

		return is;
	}



	private static String requestFTP(String folderToAccess, String imgName, String empId){

		String returnString = "not ready";
		String param_url = WebServerInfo.getUrl()+"comm/getFileFromFTP.do";

		List<NameValuePair> arguments = null;
		arguments = new ArrayList<NameValuePair>();
		arguments.add(new BasicNameValuePair("fileName", imgName));
		arguments.add(new BasicNameValuePair("folderToAccess", folderToAccess));
		arguments.add(new BasicNameValuePair("empId", empId));

		GetHttp http = null;
		http = new GetHttp();
		JSONObject returnJson = null;
		returnJson = http.getPost(param_url, arguments, true);

		EasyJsonMap ejm = null;
		try {
			ejm = new EasyJsonMap( returnJson.getJSONObject("dataList") );
			returnString = ejm.getValue("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return returnString;
	}



	private static Bitmap requestWS_bitMap(String folderToAccess, String imgName, String empId){

		InputStream is = null;
		try {

			URL bitmapUrl = new URL(WebServerInfo.getImageUrl()+empId+imgName);
			HttpGet httpRequest = new HttpGet(bitmapUrl.toURI());
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			is = bufHttpEntity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자Msg] requestWebSever() exception 발생");
		}

		return BitmapFactory.decodeStream(is);
	}
	private static InputStream requestWS_inputStream(String folderToAccess, String imgName, String empId){

		InputStream is = null;
		try {

			URL bitmapUrl = new URL(WebServerInfo.getImageUrl()+empId+imgName);
			HttpPost request = new HttpPost(bitmapUrl.toURI());
			HttpResponse response = new DefaultHttpClient().execute(request);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
			is = bufHttpEntity.getContent();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자Msg] requestWebSever() exception 발생");
		}

		return is;
	}



	private static void requestWSDel(String folderToAccess, String fileName, String empId){

		String param_url = WebServerInfo.getUrl()+"comm/delFile.do";

		List<NameValuePair> arguments = null;
		arguments = new ArrayList<NameValuePair>();
		arguments.add(new BasicNameValuePair("folderToAccess" , folderToAccess));
		arguments.add(new BasicNameValuePair("fileName", fileName));
		arguments.add(new BasicNameValuePair("empId"  , empId));

		GetHttp http = new GetHttp();
		JSONObject returnJson = null;
		returnJson = http.getPost(param_url, arguments, true);

		EasyJsonMap ejm = null;
		try {
			ejm = new EasyJsonMap( returnJson.getJSONObject("dataMap") );
			//System.out.println( ejm.getValue("message") );
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


};
