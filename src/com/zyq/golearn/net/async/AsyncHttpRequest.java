package com.zyq.golearn.net.async;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

/**
 * 异步http请求
 * @author zyq
 *
 */

public class AsyncHttpRequest {

	Context mContext = null;
	ProgressDialog mProgDialog = null;
	String mWaitMsg = "正在执行";
	String mContent = null;
	String response = "";
	String whichYemian = "";
	AsyncHttpRequestListener mListener = null;
	
	public class HttpGetTask extends AsyncTask<String, Integer, Void> {
		
		protected void onPostExecute(Void result) {
			if (mProgDialog != null)
				mProgDialog.dismiss();
			if (mListener != null && mContent != null)
			{
					mListener.onResult(mContent);
			}
		}
		
		protected void onPreExecute() {
			if (mProgDialog == null)
				mProgDialog = new ProgressDialog(mContext);
			mProgDialog.setMessage(mWaitMsg);
			mProgDialog.show();
			mProgDialog.setCancelable(true);
		}
		
		protected Void doInBackground(String... params) {
			Exception exception = null;
			String reason = null;
			try {
				mContent = httpGet(params[0]);
				response = new String(mContent);				
			} 
			catch(ClientProtocolException e) {
				reason = String.format("HTTP协议异常(%s)", e.toString());
				exception = e;
			}
			catch(UnknownHostException e) {
				reason = "网络无法连接";
				exception = e;
			}
			catch (ConnectTimeoutException e) {
				reason = "网络无法连接";
				exception = e;
			}
			catch (SocketTimeoutException e) {
				reason = "网络无法连接";
				exception = e;
			}
			catch (SocketException e) {
				reason = "网络无法连接";
				exception = e;
			}
			catch(Exception e) {
				reason = String.format("操作发生错误(%s)", e.toString());
				exception = e;
			}
			if (exception != null && mListener != null) {
				if (mProgDialog != null)
					mProgDialog.dismiss();
				mListener.onError(exception, reason);
			}
			return null;
		}
	}
	
	public class HttpPostTask extends AsyncTask<String, Integer, Void> {
		List<NameValuePair> mPostData;
		Exception mException;
		
		public HttpPostTask(List<NameValuePair> data) {
			mPostData = data;
		}
		
		protected void onPostExecute(Void result) {
			if (mProgDialog != null)
				mProgDialog.dismiss();
			if (mListener != null && mException == null)
				mListener.onResult(mContent);
		}
		
		protected void onPreExecute() {
			if (mProgDialog == null)
				mProgDialog = new ProgressDialog(mContext);
			mProgDialog.setMessage(mWaitMsg);
			mProgDialog.show();
			mProgDialog.setCancelable(true);
		}
		
		protected Void doInBackground(String... params) {
			String reason = null;
			try {
				mContent = httpPost(params[0], mPostData);
				response = new String(mContent);	
			} 
			catch(ClientProtocolException e) {
				reason = String.format("HTTP协议异常(%s)", e.toString());
				mException = e;
			}
			catch(UnknownHostException e) {
				reason = "没有可用的网络连接";
				mException = e;
			}
			catch (IOException e) {
				reason = String.format("网络错误(%s)", e.toString());
				mException = e;
			}
			catch(Exception e) {
				reason = String.format("操作发生错误(%s)", e.toString());
				mException = e;
			}
			if (mException != null && mListener != null) {
				if (mProgDialog != null)
					mProgDialog.dismiss();
				mListener.onError(mException, reason);
			}
			return null;
		}
	}
	
	public AsyncHttpRequest(Context context, String waitMsg) {
		mContext = context;
		mWaitMsg = waitMsg;
	}
	
	public AsyncHttpRequest(Context context,String waitMsg,String str){
		mContext = context;
		mWaitMsg = waitMsg;
		whichYemian = str;
	}

	public void get(String url) {
		Date begin = new Date();
		Log.d("get", "url: " + url + " begin time: " + begin.toLocaleString());
		new HttpGetTask().execute(url);
		Date end = new Date();
		Log.d("get", "url: " + url + " end time: " + end.toLocaleString() + " total:" + (end.getTime() - begin.getTime()));
	}
	
	public void post(String url,List<NameValuePair> data) {
		Date begin = new Date();
		Log.d("post", "url: " + url + " begin time: " + begin.toLocaleString());
		new HttpPostTask(data).execute(url);
		Date end = new Date();
		Log.d("post", "url: " + url + " end time: " + end.toLocaleString() + " total:" + (end.getTime() - begin.getTime()));
	}
	
	public static DefaultHttpClient setupHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 8000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		return new DefaultHttpClient(httpParameters);
	}
	
	public static String httpPost(String url, List<NameValuePair> data) throws Exception {
		
		HttpPost httppost = new HttpPost(url);
		httppost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
		BufferedReader reader = null;
		String result = null;
		HttpResponse response = setupHttpClient()
			      .execute(httppost);
		try {    	
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {    
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));    
				StringBuilder sb = new StringBuilder();    
				for (String s = reader.readLine(); 
						s != null; 
						s = reader.readLine()) {    
					sb.append(s);    
				}
				result = sb.toString();    
			}
		}
		finally {    
			try {    
				if (reader != null) {    
					reader.close();    
				}    
			} catch (Exception e) {}    
		}
		
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) { 
			throw new Exception(String.format("数据提交失败(%s)", 
					response.getStatusLine().toString()));
		}
		return result;
	}
	
	public static String httpGet(String url) throws ClientProtocolException, IOException {
		BufferedReader reader = null;
		String str = null;
		try {    
			HttpResponse response = setupHttpClient()
			      .execute(new HttpGet(url));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { 
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"GB2312"));    
				StringBuilder sb = new StringBuilder();    
				for (String s = reader.readLine(); 
						s != null; 
						s = reader.readLine()) {    
					sb.append(s);    
				}
				str = sb.toString();    
			}
		}
		finally {    
			try {    
				if (reader != null) {    
					reader.close();    
				}    
			} catch (Exception e) {}    
		}
		return str;
	}
	
	public AsyncHttpRequest setListener(AsyncHttpRequestListener listener) {
		mListener = listener;
		return this;
	}
}
