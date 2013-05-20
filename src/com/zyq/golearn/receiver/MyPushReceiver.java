package com.zyq.golearn.receiver;

import java.util.logging.Logger;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 推送信息的receiver -- 来自JPush极光推送
 * @author zyq
 *
 */
public class MyPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
     
    private NotificationManager nm;
     
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
         
        Bundle bundle = intent.getExtras();
        //Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));
         
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d(TAG, "JPush用户注册成功");
             
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的自定义消息");
                     
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "接受到推送下来的通知");
     
            receivingNotification(context,bundle);
 
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            //Toast.makeText(context, "用户点击了通知", Toast.LENGTH_LONG).show();
        
           openNotification(context,bundle);
 
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
 
   private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.d(TAG, "extras : " + extras);
    } 
 
   private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = ""; 
        try {
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.getString("webUrl");
        } catch (Exception e) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        if (myValue != null && (!myValue.equals("")) ) {
        	//Toast.makeText(context,myValue,Toast.LENGTH_LONG).show();
            Intent mIntent = new Intent(context, MyWebView.class);
            Bundle b = new Bundle();
            b.putString("webUrl", myValue);
            mIntent.putExtras(b);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mIntent);
        } else {
        	Toast.makeText(context,"获取推送内容失败",Toast.LENGTH_LONG).show();
        }
    }
}
