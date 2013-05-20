package com.zyq.golearn.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.zyq.golearn.GoLearnActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static String dbFile = "";
	private static final int VERSION = 3;
	public static final long FAILED = -1111111111;
	
	private static DBHelper dBHelper = null;

	public static synchronized DBHelper getInstance()
	{
		if(dBHelper == null)
		{
			copyDB();
			dBHelper = new DBHelper();
		}
		
		return dBHelper;
	}
	
	public DBHelper()
	{
		this(GoLearnActivity.m_application.getApplicationContext(), dbFile, null, VERSION);
	}
	
	public DBHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}
	public static void copyDB() {        
        try {
        	String path = "/data/data/" + GoLearnActivity.m_application.getPackageName() + "/databases/";
        	 File file = new File(path);
        	 if(!file.exists())
        	 {
        		 file.mkdirs();
        	 }
        	dbFile = path + "rooms.db";
            InputStream myInput = GoLearnActivity.m_application.getResources().getAssets().open("rooms.db");
            OutputStream myOutput = new FileOutputStream(dbFile);
            byte[] buffer = new byte[20240];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
     }
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	} 
	//鎵ц涓嶉渶瑕佽繑鍥炵殑璇彞锛屼緥濡俰nsert, update
	public boolean executeNoResult(String sql)
	{
		boolean result = false;
		try
		{
			getWritableDatabase().execSQL(sql);
			result = true;
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	
	//鎵ц闇�杩斿洖涓�釜瀛楃涓插�鐨勮鍙�
	public String executeStringResult(String sql)
	{
		String result = "";
		try
		{
			Cursor cursor = getReadableDatabase().rawQuery(sql, null);
			if(cursor.moveToNext())
			{
				//byte[] val = cursor.getBlob(cursor.getColumnIndex("question"));
			    //result = new String(val,"GBK");
			    result = cursor.getString(0);
				
			}
			cursor.close();
		}
		catch(Exception e)
		{
			
		}
		return result;
	}	

	//鎵ц闇�杩斿洖涓�釜long鍊肩殑璇彞
	public long executeLongResult(String sql)
	{
		long result = FAILED;
		try
		{
			Cursor cursor = getReadableDatabase().rawQuery(sql, null);
			if(cursor.moveToNext())
			{
				result = cursor.getLong(0);
			}
			cursor.close();
		}
		catch(Exception e)
		{
			
		}
		return result;
	}
	
	//杩斿洖鎵�湁鏁版嵁 浠rrayList鐨勫舰寮�
	public ArrayList getAllData(){
		
		ArrayList arrayList = new ArrayList();
		
		try{
			Cursor cursor = getReadableDatabase().rawQuery("select * from voice", null);
			while(cursor.moveToNext()){
				HashMap item = new HashMap();
			/*	item.put("_id", cursor.getInt(cursor.getColumnIndex("id")));
				item.put("question",utf2Gbk(cursor,"question"));
				item.put("answer", utf2Gbk(cursor,"answer"));*/
				//item.put("Q_length", cursor.getInt(cursor.getColumnIndex(")));
				
				item.put("_id",cursor.getInt(cursor.getColumnIndex("id")));
				item.put("question", cursor.getString(cursor.getColumnIndex("question")));
				item.put("answer", cursor.getString(cursor.getColumnIndex("answer")));
				
				arrayList.add(item);
				
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return arrayList;
	}
	
	//灏哢TF-8鏍煎紡鐨勫瓧绗﹁浆鎹负GBK鏍煎紡
	public String utf2Gbk(Cursor cursor,String index){
		byte[] val = cursor.getBlob(cursor.getColumnIndex(index));
		String result = "";
		try {
			result = new String(val,"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return result;
	}
}
