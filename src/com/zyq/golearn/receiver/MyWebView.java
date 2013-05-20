package com.zyq.golearn.receiver;

import com.zyq.golearn.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 用于接受来自推送通知的信息要求  -- 一般为url
 * @author zyq
 *
 */
public class MyWebView extends Activity{
	
	private WebView mWebView;
	private ProgressBar pb;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webreceiver);
		Bundle b = this.getIntent().getExtras();
		String url = b.getString("webUrl");
		if(!url.startsWith("http://")){
			url = "http://" + url;
		}
		mWebView = (WebView)findViewById(R.id.webview);
		pb = (ProgressBar) findViewById(R.id.commonprogress);
		mWebView.loadUrl(url);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (mWebView != null && mWebView.canGoBack()) {
	    		mWebView.goBack();                      
			}
			else {
				 Activity parent = getParent();
				 if (parent != null)
					 parent.onBackPressed();
				 else
					 super.onBackPressed();
			}
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public void onBackPressed(){
		finish();
	}

}
