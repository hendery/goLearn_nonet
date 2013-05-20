package com.zyq.golearn.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 关于手机状态的工具类
 * 是否连接网络
 * 取得IMEI
 * @author zyq
 *
 */
public class MobileUtils {

	static public boolean isNetworkAvailable(Context context) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}

   public static String getMobileIMEI(Context context){
	   final TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
	   return tm.getDeviceId().toString();
   }
}
