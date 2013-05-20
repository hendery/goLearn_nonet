package com.zyq.golearn;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import java.util.HashMap;

import com.zyq.golearn.net.async.AsyncHttpRequest;
import com.zyq.golearn.net.async.AsyncHttpRequestListener;
import com.zyq.golearn.utils.MobileUtils;

public class SplashActivity extends Activity implements AsyncHttpRequestListener {

	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    JPushInterface.init(this);
	    JPushInterface.setDebugMode(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
				SplashActivity.this.startActivityForResult(mainIntent,0);
				overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
				SplashActivity.this.finish();
			}
		}, 2900); 
		
		SharedPreferences TipMsg = getSharedPreferences("TipMsg", 0);  
		TipMsg.edit().putString("tip", "mylpislyr").commit();  
	}

	@Override
	public void onResult(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Exception e, String errorMsg) {
		// TODO Auto-generated method stub
		
	}
}



