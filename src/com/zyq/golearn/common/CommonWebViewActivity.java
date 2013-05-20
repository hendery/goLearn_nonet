package com.zyq.golearn.common;

import com.zyq.golearn.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 通用webview 
 * 尚未完成
 * @author zyq
 *
 */

public class CommonWebViewActivity extends Activity{

	protected WebView webview;
	protected ProgressBar mProgressBar;
	protected TextView title;
	
	//bundle获取从过来的标题、URL、通用参数
	protected Bundle bundle = null;
	protected String titletext = "";
	protected String url = "";
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vip_common_webview);
		bundle = this.getIntent().getExtras();
		initValiable();
		initWebView();
		webview.loadUrl("http://www.icugb.com");
		
	}
	
	protected void initValiable() {
		// TODO Auto-generated method stub
		//title = (TextView) findViewById(R.id.commontitle);
		webview = (WebView) findViewById(R.id.commonwebview);
		mProgressBar = (ProgressBar) findViewById(R.id.commonprogress);
	}
    
	
	protected void initContent() {
		// TODO Auto-generated method stub
		title.setText("");
	}
	public void initWebView(){
		WebSettings ws = webview.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setDomStorageEnabled(true);
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);
		webview.setBackgroundColor(Color.TRANSPARENT);
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webview.setVerticalScrollBarEnabled(false);		
		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new WebChromeClient());
		
	}
		


	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (webview != null && webview.canGoBack()) {
				webview.goBack();                      
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
	 public class MyWebViewClient extends WebViewClient {
		 
			
			
			public boolean shouldOverrideUrlLoading(WebView view, String url) {    
				if(url.contains("&rwxzsafari=1"))
				{
			        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));        
			        view.getContext().startActivity(intent);
			        return false;
				}
				
				view.loadUrl(url); 
				return true;
			} 
			
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				CommonWebViewActivity.this.onPageStarted(view,url);
			}
			
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
			}
			
			public void onReceivedError(WebView view, int errorCode, 
					String description, String failingUrl) {
			} 
		}
		public void onPageStarted(WebView view, String url) {
			// TODO Auto-generated method stub
		}
}
