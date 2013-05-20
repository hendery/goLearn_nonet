package com.zyq.golearn.hole;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.zyq.golearn.Constant;
import com.zyq.golearn.R;
import com.zyq.golearn.common.CommonWebViewActivity;
import com.zyq.golearn.net.async.AsyncHttpRequest;
import com.zyq.golearn.net.async.AsyncHttpRequestListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 人人树洞发状态页面
 * @author zyq
 *
 */
public class PostHoleActivity extends Activity implements AsyncHttpRequestListener{
	
	private Button mBtnSubmit;
	private EditText mEtContent;
	private Button mBtnGoHole;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hole_post);
		initContent();
		
	}
	
	public void initContent(){
		mBtnSubmit = (Button)findViewById(R.id.submit);
		mEtContent = (EditText)findViewById(R.id.content);
		mBtnGoHole = (Button)findViewById(R.id.gohole);
		
		
		
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncHttpRequest(PostHoleActivity.this,"正在提交树洞").setListener(
						PostHoleActivity.this).post(
						Constant.HOLE_URL,getParam());
			}
		});
		
		/**
		 * 去树洞
		 */
		mBtnGoHole.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(PostHoleActivity.this,CommonWebViewActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onResult(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onError(Exception e, String errorMsg) {
		// TODO Auto-generated method stub
		Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}
	
	public List<NameValuePair> getParam(){
		List<NameValuePair> list = new ArrayList<NameValuePair>(); 
        NameValuePair floor = new BasicNameValuePair("mes", mEtContent.getText().toString()); 
        NameValuePair week = new BasicNameValuePair("token","755869408"); 
        NameValuePair day = new BasicNameValuePair("fromtype","1");
        list.add(floor); 
        list.add(week); 
        list.add(day);
        return list;
	}

}
