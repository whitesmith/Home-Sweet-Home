package com.whitesmith.homesweethome;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "HomeSweetHomePrefs";
	public static final String NETWORK = "network";
	public static final String WAS_AT_HOME = "was_at_home";
	public static final String DATE = "date";
	static final int PICK_NETWORK_REQUEST = 200;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(R.string.app_name);
		
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
		String home_network_name = settings.getString(NETWORK, "");	
        ((TextView) findViewById(R.id.network_ssid)).setText(home_network_name);

		findViewById(R.id.choose_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(),ChooseHomeNetwork.class);
				startActivityForResult(i, PICK_NETWORK_REQUEST);
			}
		});

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
