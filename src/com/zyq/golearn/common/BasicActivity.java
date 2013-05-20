package com.zyq.golearn.common;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * activity父类
 * @author zyq
 *
 */

public abstract class BasicActivity extends Activity{
	
	//得到内容view
    protected abstract int getContentViewId();
	
    //得到title  view 
	protected abstract int getTitleViewId();
	
	protected Context getContext(){
		return this;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, 
				getTitleViewId());
	
		/*if (findViewById(R.id.buttonHome) != null) {
			cActivityManager.getActivityManager().push(this);
		}	*/
		
		{
			initContentView();
		}
	}
	
	/**
	 * 初始化界面元素
	 */
	protected void initContentView(){
		
	}
}
