package com.zyq.golearn;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import cn.jpush.android.api.JPushInterface;

import com.zyq.golearn.common.CommonWebViewActivity;
import com.zyq.golearn.db.DBHelper;
import com.zyq.golearn.hole.PostHoleActivity;
import com.zyq.golearn.utils.DateUtils;
import com.zyq.golearn.utils.StringUtils;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 主界面
 * tip：查询数据中存在大量冗余
 * @author zyq
 * 
 */

public class GoLearnActivity extends Activity {

	public static Application m_application;

	private Spinner mSnFloor = null;
	private Spinner mSnWeek = null;
	private Spinner mSnDay = null;
	private Spinner mSnClass = null;
	private Button mBtnSubmit = null;
	private TextView mTvResult = null;
	private TextView mTvTitle = null;
	private ImageView mIvGoBack = null;
	private SlidingDrawer mSdResult = null;

	private String[] builderCode = null;
	
	private int mWeek;
	private int mDay;
	private int mClass;
	private int mFloorCode;
	
	private boolean isExit = false;
	
	String tip = "可供您享用的自习室有：\n";
	String baseSql = "select emptyRooms from room where id =";
	
	
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };


	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		builderCode = getResources().getStringArray(R.array.floor_value);
		m_application = this.getApplication();
		setContentView(R.layout.main);
		try {
			initContent();
			initVariable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化界面元素
	 */
	public void initContent() throws Exception {

		mSnFloor = (Spinner) findViewById(R.id.sn_builder);
		mSnWeek = (Spinner) findViewById(R.id.sn_week);
		mSnDay = (Spinner) findViewById(R.id.sn_day);
		mSnClass = (Spinner) findViewById(R.id.sn_class);
		mBtnSubmit = (Button) findViewById(R.id.submit);	
		mTvTitle = (TextView)findViewById(R.id.head_text);
		mIvGoBack = (ImageView)findViewById(R.id.go_back);
		mSdResult = (SlidingDrawer)findViewById(R.id.drawer);
		mTvResult = (TextView)mSdResult.findViewById(R.id.tvcontent);
		
		setSpinner(mSnFloor, R.array.floor);
		setSpinner(mSnWeek, R.array.week);
		setSpinner(mSnDay, R.array.day);
		setSpinner(mSnClass, R.array.time);
		
		/*Button shuDong = (Button)findViewById(R.id.shudong);
		shuDong.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(GoLearn_nonetActivity.this,PostHoleActivity.class);
				startActivity(in);
			}
		});
		*/
		// 查询按钮点击事件
		mBtnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if(mSnClass.getSelectedItemPosition() > 6){
						getAllDay();
					}else if(mSnClass.getSelectedItemPosition() > 4){
						getAmOrPm();
					} else {
						getRoomsByClass();
					}
					
					if(!mSdResult.isOpened()){
						mSdResult.animateOpen();
					}	
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		
		/**
		 * 设置drawer是否可点击
		 */
		mSdResult.setOnDrawerCloseListener(new OnDrawerCloseListener(){

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				mSdResult.setClickable(false);
			}
			
		});
		
		mSdResult.setOnDrawerOpenListener(new OnDrawerOpenListener(){

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				mSdResult.setClickable(true);
			}
			
		});
			
		mIvGoBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 初始化各控件的值
	 * @throws ParseException 
	 */
	public void initVariable() throws ParseException{

		setTitle();  //初始化标题
		
		mSnWeek.setSelection((int)DateUtils.getWeeksToNow() - 1);   //初始化第几周
		
		//初始化周几
		int thisDay = DateUtils.getIntDayOfWeek();
		if(thisDay == 1){
			mSnDay.setSelection(6);  //周日
		} else {
			mSnDay.setSelection(thisDay - 2);
		}
		
	}

	/**
	 * 初始化spinner
	 * @param spinner
	 * @param data
	 */
	public void setSpinner(Spinner spinner, int data) {

		final String[] dataArray = getResources().getStringArray(data);
		ArrayAdapter<String> floorAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, dataArray);

		spinner.setAdapter(floorAdapter);
	}
	
	/**
	 * 算出 数据库中对应数据的id
	 */
	protected int calculateId(){
		mWeek = mSnWeek.getSelectedItemPosition() ;
		mDay = mSnDay.getSelectedItemPosition() ;
		mClass = mSnClass.getSelectedItemPosition() ;
		mFloorCode = mSnFloor.getSelectedItemPosition() + 1;
		int id = mWeek * 210 + mDay * 30 + mClass * 6 + mFloorCode;
		return id;
	}
	
	/**
	 * 得到title
	 */
	public int getTitleViewId(){
		return R.layout.common_title;
	}
	
	/**
	 * 设置title 周数和星期
	 * @throws ParseException 
	 */
	public void setTitle() throws ParseException{
		int thisWeek = (int) DateUtils.getWeeksToNow();
		if(thisWeek < 0){
			mTvTitle.setText("时光倒流了？~_~");
			return ;
		}
		if(thisWeek > 17){
			mTvTitle.setText("目测时间已超过17周");
		}
		StringBuilder title = new StringBuilder("今天为第" + thisWeek + "周" + DateUtils.getStrDayOfWeek());
		mTvTitle.setText(title.toString());
	}
	
	/**
	 * 得到查询结果并显示
	 */
	public void getRoomsByClass(){
		int id = calculateId();
		String sql  = baseSql + id + ";";
		String result = DBHelper.getInstance().executeStringResult(sql);
		showResult(result);		
	}
	
	/**
	 * 得到整上午或整下午空教室
	 */
	public void getAmOrPm(){
		int id1 = calculateId() - 5 * 6;
		int id2 = calculateId() - 4 * 6;
		String sql1 = baseSql + id1 + ";";
		String sql2 = baseSql + id2 + ";";
		String result1 = DBHelper.getInstance().executeStringResult(sql1);
		String result2 = DBHelper.getInstance().executeStringResult(sql2);
		if(result1 == null || result1.length() ==0 || result2 == null || result2.length() == 0){
			mTvResult.setText("次奥，居然没有自习室！");
			return ;
		}
		String[] result = StringUtils.intersect(result1, result2, "-");
		if(result == null ||result.length == 0){
			mTvResult.setText("次奥，居然没有自习室！");
			return ;
		} else {
			String resultStr = StringUtils.connStr(result);
			mTvResult.setText(tip + resultStr);
			return ;
		}		
		
	}
	
	/**
	 * 得到全天均无课的教室
	 */
	public void getAllDay(){
	int[] id = new int[4];
		String sql1,sql2;
		String resultStr[] = new String[2];
		id[0] = calculateId() - 7 * 6;
		id[1]= calculateId() - 6 * 6;
		id[2] = calculateId() - 5 * 6;
		id[3]= calculateId() - 4 * 6;
		for(int i = 0; i < 4; i ++){
			sql1 = baseSql + id[i++] + ";";
			sql2 = baseSql + id[i] + ";";
			String result1 = DBHelper.getInstance().executeStringResult(sql1);
			String result2 =  DBHelper.getInstance().executeStringResult(sql2);
			if(StringUtils.hasIntersection(result1, result2)){
			    String[] result = StringUtils.intersect(result1, result2, "-"); 
			    resultStr[i / 2] = StringUtils.connStr(result);
			} else {
				mTvResult.setText("次奥，居然没有自习室！");
				return ;
			}	   
		}
		if(StringUtils.hasIntersection(resultStr[0], resultStr[1])){
		    String[] result = StringUtils.intersect(resultStr[0], resultStr[1], "-"); 
		    mTvResult.setText(tip + StringUtils.connStr(result));
		} else {
			mTvResult.setText("次奥，居然没有自习室！");
			return ;
		}	   
		
	}
	
	//
	public void showResult(String result){
		if(result == null || ("").equals(result)){
			mTvResult.setText("次奥，居然没有自习室！");
		} else {
			mTvResult.setText(tip + result);
		}			
	}
	
	
	/**
	 * 覆盖返回按钮事件 ------------------------------------
	 */
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
	
	public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }
	
	//------------------------------------------------------------------

}