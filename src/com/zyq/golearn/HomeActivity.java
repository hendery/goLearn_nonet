package com.zyq.golearn;

import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.zyq.golearn.common.BasicActivity;
import com.zyq.golearn.hole.PostHoleActivity;
import com.zyq.golearn.net.async.AsyncHttpRequest;
import com.zyq.golearn.net.async.AsyncHttpRequestListener;
import com.zyq.golearn.utils.MobileUtils;

/**
 * 主页面
 * @author zyq
 *
 */
public class HomeActivity extends Activity implements AsyncHttpRequestListener{
	
	//private Button mBtnHello;
	private Button mBtnStudy;
	private Button mBtnHole;
	
	protected static SensorManager sensorManager; 
	protected static Vibrator vibrator; 
    private static final int SENSOR_SHAKE = 10; 
    /**
     * 动作执行
     */ 
    Handler handler = new Handler() { 
 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch (msg.what) { 
            case SENSOR_SHAKE: 
            	SharedPreferences TipMsg = getSharedPreferences("TipMsg", 0);  	
            	String tip = TipMsg.getString("tip", "mylpislyr");
            	Toast.makeText(HomeActivity.this,tip, Toast.LENGTH_SHORT).show();
                Log.i("homeactivity", "检测到摇晃，执行操作！"); 
                break; 
            } 
        } 
 
    };
	
	private boolean isExit = false;
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };
    
    Handler errorHandler = new Handler(){
    	 @Override
         public void handleMessage(Message msg) {
             // TODO Auto-generated method stub
             super.handleMessage(msg);
             Toast.makeText(HomeActivity.this,"网络无法连接或超时", Toast.LENGTH_SHORT).show();
         }
    };
	
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initContentView();
		//摇动传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
	}
	
	protected void initContentView(){
		//mBtnHello = (Button)findViewById(R.id.hello);
		mBtnStudy = (Button)findViewById(R.id.learn);
		mBtnHole = (Button)findViewById(R.id.hole);
		
		/*mBtnHello.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//startActivity(new Intent(getContext(),))
				
			}
		});*/
		
		mBtnStudy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(HomeActivity.this,GoLearnActivity.class));
			}
		});
		
		mBtnHole.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MobileUtils.isNetworkAvailable(HomeActivity.this)){
					new AsyncHttpRequest(HomeActivity.this,"正在提交").setListener(
							HomeActivity.this).get("http://zic.icugb.com/hellocugb.php?IMEI=" + MobileUtils.getMobileIMEI(HomeActivity.this));
				} else {
					new  AlertDialog.Builder(HomeActivity.this)    
					   
	                .setTitle("( ⊙o⊙  )" )  
	   
	                .setMessage("您可能尚未连接网络" )  
	  
	                .setPositiveButton("确定" ,  null )  
  
	                .show(); 
				}
			 
			}
		});
	}

	@Override
	public void onResult(String string) {
		// TODO Auto-generated method stub
		if(string.startsWith("<")){
			//String str = string.substring(1);		
			new  AlertDialog.Builder(HomeActivity.this)    		   
	        .setTitle("~_~" )  
	        .setMessage("是不是用校网没有登网关？")  
	        .setPositiveButton("确定" ,  null )  
	        .show(); 
		} else {			
			new  AlertDialog.Builder(HomeActivity.this)    		   
	        .setTitle("起床报告" )  
	        .setMessage(string )  
	        .setPositiveButton("确定" , null )  
	        .show(); 
		}
		
	}

	@Override
	public void onError(Exception e, String errorMsg) {
		// TODO Auto-generated method stub
		 Message msg = new Message(); 
         msg.what = 1;         
         errorHandler.sendMessage(msg);  
	}
	
    /**
     * 重力感应监听
     */ 
    private SensorEventListener sensorEventListener = new SensorEventListener() { 
 
        @Override 
        public void onSensorChanged(SensorEvent event) { 
            // 传感器信息改变时执行该方法 
        	float[] values =event.values; 
            int x =(int)values[0]; // x轴方向的重力加速度，向右为正 
            int y = (int)values[1]; // y轴方向的重力加速度，向前为正 
            int z = (int)values[2]; // z轴方向的重力加速度，向上为正 
            Log.i("HomeActivity", "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z); 
            int medumValue = 18;  
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {            
            	
            	 vibrator.vibrate(200); 
                 Message msg = new Message(); 
                 msg.what = SENSOR_SHAKE; 
                 handler.sendMessage(msg); 
            } 
        } 
 
        @Override 
        public void onAccuracyChanged(Sensor sensor, int accuracy) { 
 
        }
    }; 

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
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
	  @Override 
	    protected void onResume() { 
	        super.onResume(); 
	        if (sensorManager != null) {// 注册监听器 
	            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); 
	            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率 
	        } 
	    } 
    @Override 
    protected void onStop() { 
        super.onStop(); 
        if (sensorManager != null) {// 取消监听器 
            sensorManager.unregisterListener(sensorEventListener); 
        } 
    } 

}
