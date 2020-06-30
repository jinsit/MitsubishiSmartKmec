package com.jinsit.kmec.webservice;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.jinsit.kmec.comm.JSONObjectAndException;

import android.os.AsyncTask;


public class GetHttp extends AsyncTask {

	Boolean debuggingMode = false;

	public JSONObject getPost(String param_url, Boolean debugging){
		this.debuggingMode = debugging;
		try {

			HttpClient client = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(param_url);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				String result =  EntityUtils.toString(resEntity);
				JSONObject resultJson = new JSONObject(result);
				if(debugging == true){
					System.out.println("[ getPost() ] :: resultJson is as below"+ "\n" + resultJson);
				}
				return resultJson;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자 Message] getPost() Exception 발생");
		}
		return new JSONObject();

	}// end of getPost()


	public JSONObject getPost(String argUrl, List arguments, Boolean debugging){
		this.debuggingMode = debugging;
		try {

			HttpClient client = new DefaultHttpClient();
//			client.getParams().setParameter("http.protocol.expect-continue", false);
//			client.getParams().setParameter("http.connection.timeout", 5000);
//			client.getParams().setParameter("http.socket.timeout", 5000);
//
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, 30000);

			HttpPost postRequest = new HttpPost(argUrl);

			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(arguments, HTTP.UTF_8);
			postRequest.setEntity(entry);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				String result =  EntityUtils.toString(resEntity);
				JSONObject resultJson = new JSONObject(result);
				if(debugging == true){
					System.out.println("[ getPost() ] :: resultJson is as below"+ "\n" + resultJson);
				}
				return resultJson;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자 Message] getPost() Exception 발생");
			return new JSONObject();
		}
		return new JSONObject();

	}// end of getPost()


	public JSONObject getPostWithTimeout(String argUrl, List arguments, Boolean debugging, int timeout){
		this.debuggingMode = debugging;
		try {

			HttpClient client = new DefaultHttpClient();
//			client.getParams().setParameter("http.protocol.expect-continue", false);
//			client.getParams().setParameter("http.connection.timeout", 5000);
//			client.getParams().setParameter("http.socket.timeout", 5000);
//
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 10000);
			HttpConnectionParams.setSoTimeout(params, timeout);

			HttpPost postRequest = new HttpPost(argUrl);

			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(arguments, HTTP.UTF_8);
			postRequest.setEntity(entry);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				String result =  EntityUtils.toString(resEntity);
				JSONObject resultJson = new JSONObject(result);
				if(debugging == true){
					System.out.println("[ getPost() ] :: resultJson is as below"+ "\n" + resultJson);
				}
				return resultJson;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자 Message] getPost() Exception 발생");
			return new JSONObject();
		}
		return new JSONObject();

	}// end of getPost()


	public JSONObjectAndException getPostByRI(String argUrl, List arguments, Boolean debugging){
		this.debuggingMode = debugging;
		JSONObjectAndException jSONObjectAndException = new JSONObjectAndException();

		try {

			HttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 10000);

			HttpPost postRequest = new HttpPost(argUrl);
			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(arguments, HTTP.UTF_8);
			postRequest.setEntity(entry);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				String result =  EntityUtils.toString(resEntity);
				JSONObject resultJson = new JSONObject(result);

				jSONObjectAndException.setjSONObj(resultJson);
				if(debugging == true){
					System.out.println("[ getPost() ] :: resultJson is as below"+ "\n" + resultJson);
				}
				return jSONObjectAndException;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[개발자 Message] getPost() Exception 발생");
			jSONObjectAndException.setException(e);
			return jSONObjectAndException;
		}

		return jSONObjectAndException;

	}


	public byte[] getPostByte(String param_url, List arguments){

		byte[] bytes = null;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(param_url);

			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(arguments, HTTP.UTF_8);
			postRequest.setEntity(entry);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				bytes = EntityUtils.toByteArray(resEntity);

				//String result =  EntityUtils.toString(resEntity);	
				//JSONObject resultJson = new JSONObject(result);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return bytes;
	}




	public String getPostStr(String param_url, List arguments, Boolean debugging){

		String str = null;
		this.debuggingMode = debugging;

		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(param_url);

			UrlEncodedFormEntity entry = new UrlEncodedFormEntity(arguments, HTTP.UTF_8);
			postRequest.setEntity(entry);
			HttpResponse response = client.execute(postRequest);
			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {
				str = EntityUtils.toString(resEntity);
				if(debugging == true){
					System.out.println("[ getPost() ] :: resultJson is as below"+ "\n" + str);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return new String();
	}


	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

};
