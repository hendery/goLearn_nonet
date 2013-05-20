package com.zyq.golearn.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 字符串处理工具类
 * @author zyq
 *
 */

public class StringUtils {
	
	/**
	 * 判断两个字符串是否有交集
	 */
	public static boolean hasIntersection(String str1,String str2){
		if(str1 == null || str1.length() ==0 || str2 == null || str2.length() == 0){
			return false;
		}
		String[] result = StringUtils.intersect(str1, str2, "-");
		if(result == null ||result.length == 0){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 得到两个字符串数组的交集
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	 public static String[] intersect(String[] arr1, String[] arr2) {   
	        Map<String, Boolean> map = new TreeMap<String, Boolean>();   
	        LinkedList<String> list = new LinkedList<String>();   
	        for (String str : arr1) {   
	            if (!map.containsKey(str)) {   
	                map.put(str, Boolean.FALSE);   
	            }   
	        }   
	        for (String str : arr2) {   
	            if (map.containsKey(str)) {   
	                map.put(str, Boolean.TRUE);   
	            }   
	        }   
	  
	        for (Entry<String, Boolean> e : map.entrySet()) {   
	            if (e.getValue().equals(Boolean.TRUE)) {   
	                list.add(e.getKey());   
	            }   
	        }   
	  
	        String[] result = {};   
	        return list.toArray(result);   
	    }  
	 
	 /**
	  * 两个字符串分割后取交集
	  */
	 public static String[] intersect(String str1,String str2,String splitBy){
		 String[] arrayStr1 = str1.split(splitBy);
		 String[] arrayStr2 = str2.split(splitBy);
		 return intersect(arrayStr1,arrayStr2);
	 }
	 
	 /**
	  * 得到两个字符串数组的差集
	  * @param arr1
	  * @param arr2
	  * @return
	  */
	 public static String[] minus(String[] arr1, String[] arr2) {   
	        LinkedList<String> list = new LinkedList<String>();   
	        LinkedList<String> history = new LinkedList<String>();   
	        String[] longerArr = arr1;   
	        String[] shorterArr = arr2;   
	        //找出较长的数组来减较短的数组   
	        if (arr1.length > arr2.length) {   
	            longerArr = arr2;   
	            shorterArr = arr1;   
	        }   
	        for (String str : longerArr) {   
	            if (!list.contains(str)) {   
	                list.add(str);   
	            }   
	        }   
	        for (String str : shorterArr) {   
	            if (list.contains(str)) {   
	                history.add(str);   
	                list.remove(str);   
	            } else {   
	                if (!history.contains(str)) {   
	                    list.add(str);   
	                }   
	            }   
	        }   
	  
	        String[] result = {};   
	        return list.toArray(result);   
	    } 
	 
	 /**
	  * 将字符串数组元素用"-"连接起来
	  */
	 public static String connStr(String[] arrayStr){
		 StringBuilder result = new StringBuilder();
		 for(int i = 0, j = arrayStr.length; i < j - 1 ; i++){
			 result.append(arrayStr[i]);
			 result.append("-");
		 }
		 result.append(arrayStr[arrayStr.length - 1]);
		 return result.toString();
	 }

}
