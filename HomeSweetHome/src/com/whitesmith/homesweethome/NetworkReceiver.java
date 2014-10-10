package com.whitesmith.homesweethome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

	private PendingIntent pendingIntent;
	public AlarmManager manager;

	public NetworkReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent alarmIntent = new Intent(context, LoggerReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
		manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		String action = intent.getAction();
		if(action.equals("my.action.STOP_TRACKER")){
			//String state = intent.getExtras().getString("extra");
			Toast.makeText(context, "STOPING TRACKER", Toast.LENGTH_SHORT).show();
			stopTracker();
		}
		else if(action.equals("my.action.START_TRACKER")){
			//String state = intent.getExtras().getString("extra");
			Toast.makeText(context, "FIRING UP TRACKER", Toast.LENGTH_SHORT).show();
			startTracker();
		}
		else{
			Toast.makeText(context, "FIRING UP TRACKER", Toast.LENGTH_SHORT).show();
			startTracker();
		}	
	}

	public void startTracker() {
		int interval = 5000;
		manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
	}

	public void stopTracker() {
		if (manager != null) {
			manager.cancel(pendingIntent);
		}
	}
}
