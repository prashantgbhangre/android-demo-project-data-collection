package com.prashantb.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebService {

	public static String makeHttpRequest(String url,
			List<NameValuePair> params, String method) {
		Log.d("in post", "in 1");
		InputStream is = null;
		String return_data = null;
		Log.d("Path", url);
		// Making HTTP request
		try {
			// check for request method
			if (method == "POST") {
				Log.d("in post", "in");
				// request method is POST
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				Log.d("Ticket", "input stream is " + is);
			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				// String paramString = URLEncodedUtils.format(params, "utf-8");
				// url += "?" + paramString;
				Log.d("Ticket arg", "" + url);
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("accept", "application/json");
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				Log.d("Ticket", "" + is);
			}
			return_data = inputStreamToString(is).toString();
			Log.d("Ticket Return data is", "" + return_data);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.d("Ticket err 1 ", "" + e);
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.d("Ticket err 2 ", "" + e);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("Ticket err 3 ", "" + e);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Ticket err 4 ", "" + e);
			return null;
		}
		return return_data;
	}

	public static StringBuilder inputStreamToString(InputStream is) {
		try {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			} catch (IOException e) {
			}
			return answer;
		} catch (Exception ex) {
		}
		return null;
	}
}
