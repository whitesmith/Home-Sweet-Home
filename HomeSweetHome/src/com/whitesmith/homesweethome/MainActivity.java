package com.whitesmith.homesweethome;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "HomeSweetHomePrefs";
	public static final String NETWORK = "network";
	public static final String WAS_AT_HOME = "was_at_home";
	public static final String DATE = "date";
	static final int PICK_NETWORK_REQUEST = 200;
	
	private Button status_btn;
	private PendingIntent pendingIntent;
	private AlarmManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(R.string.app_name);

		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		String home_network_name = settings.getString(NETWORK, "");	
		((TextView) findViewById(R.id.network_ssid)).setText(home_network_name);

//		Intent alarmIntent = new Intent(this, LoggerReceiver.class);
//		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
		
		status_btn = (Button) findViewById(R.id.status_btn);
		
		AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		if (manager.ELAPSED_REALTIME > 0) {
			setButtonToStop();
		}
		else {
			setButtonToStart();
		}
	}
	
	private void setButtonToStop(){
		status_btn.setText(getResources().getString(R.string.stop_tracking));
		status_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("my.action.STOP_TRACKER");
				//intent.putExtra("extra", phoneNo); \\ phoneNo is the sent Number
				sendBroadcast(intent);
				setButtonToStart();
			}
		});
	}
	
	private void setButtonToStart(){
		status_btn.setText(getResources().getString(R.string.start_tracking));
		status_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent("my.action.START_TRACKER");
				//intent.putExtra("extra", phoneNo); \\ phoneNo is the sent Number
				sendBroadcast(intent);
				setButtonToStop();
			}
		});
	}

	public void pickNetwork(View view){
		Intent i = new Intent(getBaseContext(),ChooseHomeNetwork.class);
		startActivityForResult(i, PICK_NETWORK_REQUEST);
	}

	public void startTracking(View view){
		Intent intent = new Intent("my.action.START_TRACKER");
		//intent.putExtra("extra", phoneNo); \\ phoneNo is the sent Number
		sendBroadcast(intent);
	}

	public void stopTracking(View view){
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_NETWORK_REQUEST) {
			if (resultCode == RESULT_OK) {	
				String home_network_name = data.getExtras().getString(NETWORK);
				((TextView) findViewById(R.id.network_ssid)).setText(home_network_name);
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString(NETWORK, home_network_name);
				editor.commit();
			}
		}
	}
}
