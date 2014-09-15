package com.whitesmith.homesweethome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
	
	private static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mmZ",Locale.getDefault());
	
	public NetworkReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {	
		SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		if(connectedToWiFi(activeNetwork)){	
			
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			String current_network = wifiInfo.getSSID();
			String previous_date = settings.getString(MainActivity.DATE, "");
			if(!previous_date.isEmpty()){
				HTTPPost.postOut(context, previous_date);			
			}
			
			if(current_network.equals(getHomeNetwork(settings))){
				//Toast.makeText(context, "AT HOME", Toast.LENGTH_SHORT).show();
				HTTPPost.postIn(context, date_format.format(new Date()));
				setWasAtHome(editor, true);
			}
			else{
				setWasAtHome(editor, false);
			}
		}  
		else{
			if(getWasAtHome(settings)){
				String previous_date = getDateToPost(settings);
				if(previous_date.isEmpty()){
					setDateToPost(editor, date_format.format(new Date()));		
					//Toast.makeText(context, "OUTSIDE", Toast.LENGTH_SHORT).show();
				}	
			} else{
				setDateToPost(editor, "");
			}
				
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

}
