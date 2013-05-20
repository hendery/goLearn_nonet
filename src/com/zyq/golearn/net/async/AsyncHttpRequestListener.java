package com.zyq.golearn.net.async;

import java.util.List;


public interface AsyncHttpRequestListener {
	
	public void onResult(String string);
	
	public void onError(Exception e, String errorMsg);

	
}
