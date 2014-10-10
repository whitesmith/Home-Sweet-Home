package com.whitesmith.homesweethome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class LoggerReceiver extends BroadcastReceiver {
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mmZ",Locale.getDefault());
	private final static String REQUEST_URL = "https://mx3d6g426bxe.runscope.net";
	
	public LoggerReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
		SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if(connectedToWiFi(activeNetwork)){	
			
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String current_network = wifiInfo.getSSID();
			
			if(current_network.equals(getHomeNetwork(settings))){
				Toast.makeText(context, "AT HOME", Toast.LENGTH_SHORT).show();
				//postIn(context, date_format.format(new Date()));
			}
			else{
			}
		}  
		else{
		}
	}
	
	private boolean connectedToWiFi(NetworkInfo activeNetwork){
		if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
			return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		} else{
			return false;
		}	
	}
	
	private boolean getWasAtHome(SharedPreferences settings){
		return settings.getBoolean(MainActivity.WAS_AT_HOME, false);
	}
	
	private void setWasAtHome(SharedPreferences.Editor editor, boolean was_at_home){
		editor.putBoolean(MainActivity.WAS_AT_HOME, was_at_home);
		editor.commit();
	}
	
	private String getDateToPost(SharedPreferences settings){
		return settings.getString(MainActivity.DATE, "");
	}
	
	private void setDateToPost(SharedPreferences.Editor editor, String date){
		editor.putString(MainActivity.DATE, date);
		editor.commit();
	}
	
	private String getHomeNetwork(SharedPreferences settings){
		return settings.getString(MainActivity.NETWORK, "");
	}
	
	private void postIn(Context context, String date){
		RequestParams params = new RequestParams();
		params.put("state", "IN THA HOUSE");
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
}
