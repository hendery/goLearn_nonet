package com.zyq.golearn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 有关时间日期的工具类
 * @author zyq
 *
 */

public class DateUtils {
	
	public static final String BeginDate = "2013/3/4"; 
	
	
	/**
	 * 得到现在为第几周
	 * @return
	 * @throws ParseException
	 */
	public static int getWeeksToNow() throws ParseException{
		
		 Date begin = null;   //定义时间类型       
		 SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd"); 
		 begin = inputFormat.parse(BeginDate);
		 
		 Calendar now = Calendar.getInstance();
		 Date date  = now.getTime();
		 
		 long beginTime = begin.getTime();
		 long endTime = date.getTime();
		 long cha = endTime - beginTime;
		 if(cha < 0){
			 return -1;
		 }
		 long zhou = 60 * 60 * 24 * 7 * 1000;   //换算周数
		 return (int) (cha/zhou + 1);
	}
	
	/**
	 * 得到现在为周几  int型   -- 从周日开始
	 */
	public static int getIntDayOfWeek(){
		Calendar now = Calendar.getInstance();
		 return  now.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 得到现在为周几 -- 字符串型
	 */
    public static String getStrDayOfWeek(){
    	int dayOfWeek = getIntDayOfWeek();
		switch (dayOfWeek) {
		case 1:
			return "星期日";
		case 2:
			return "星期一";
		case 3:
			return "星期二";
		case 4:
			return "星期三";
		case 5:
			return "星期四";
		case 6:
			return "星期五";
		case 7:
			return "星期六";
		default:
			return "计算失败";
		}
	}
}
