package com.whitesmith.homesweethome;

import org.apache.http.Header;
import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HTTPPost {

	private static Context _context;
	private final static String REQUEST_URL = "https://mx3d6g426bxe.runscope.net";

	public static void postIn(Context context, String date){
		RequestParams params = new RequestParams();
		params.put("state", "IN");
		params.put("date", date);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(context, REQUEST_URL, params, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

			}});
	}
	
	public static void postOut(Context context, String date){
		_context = context;
		RequestParams params = new RequestParams();
		params.put("state", "OUT");
		params.put("date", date);
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(context, REQUEST_URL, params, new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				SharedPreferences settings = _context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(MainActivity.DATE, "");
				editor.commit();
			}});
	}
}
