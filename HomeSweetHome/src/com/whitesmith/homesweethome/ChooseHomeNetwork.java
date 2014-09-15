package com.whitesmith.homesweethome;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class ChooseHomeNetwork extends Activity{

	private WifiManager wifi;         
	private ListView network_list;  
	private List<WifiConfiguration> results;  
	private EditText editsearch;
	private NetworkListAdapter networkListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_home_network);
		
		getActionBar().setTitle(R.string.choose_network);

		network_list = (ListView) findViewById(R.id.network_list);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);        
		wifi.startScan();
		results= wifi.getConfiguredNetworks();  
		if(results!=null){
			networkListAdapter = new NetworkListAdapter(this, results);
			network_list.setAdapter(networkListAdapter);
			network_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent i = new Intent();
					i.putExtra(MainActivity.NETWORK, networkListAdapter.getNetwork(position).SSID);
					setResult(RESULT_OK, i);
					finish();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search_networks);
		SearchView searchView = (SearchView) menuItem.getActionView();

		int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		editsearch = (EditText) searchView.findViewById(searchPlateId);
		editsearch.addTextChangedListener(textWatcher);
		editsearch.setHint("Network Name");
		return super.onCreateOptionsMenu(menu);
	}
	
	private final TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
			if(networkListAdapter != null){
				networkListAdapter.filter(text);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

	};

	public class NetworkListAdapter extends ArrayAdapter<String> {
		private final Context context;
		private List<WifiConfiguration> networks;
		private List<WifiConfiguration> networks_raw;
		private String searchText;

		public NetworkListAdapter(Context context, List<WifiConfiguration> networks) {
			super(context, R.layout.row_layout);	
			this.context = context;
			this.networks = new ArrayList<WifiConfiguration>(networks);
			this.networks_raw = new ArrayList<WifiConfiguration>(networks);
			this.searchText = "";
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
						
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_layout, parent, false);
			TextView label = (TextView) rowView.findViewById(R.id.label);
			String network_ssid = networks.get(position).SSID.substring(1, networks.get(position).SSID.length()-1);	
			
			String name = network_ssid.toLowerCase(Locale.getDefault());			
			Spannable spanText = Spannable.Factory.getInstance().newSpannable(network_ssid);
			int start = name.indexOf(searchText);
			spanText.setSpan(new BackgroundColorSpan(context.getResources().getColor(android.R.color.holo_blue_light)),start, start+searchText.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			label.setText(spanText);			
			
			return rowView;
		}

		public void filter(String charText) {
			charText = charText.toLowerCase(Locale.getDefault());
			if (charText.length() == 0) {
				searchText = "";
				networks = new ArrayList<WifiConfiguration>(networks_raw);
			}
			else
			{
				searchText = charText;
				networks.clear();
				for (WifiConfiguration config : networks_raw)
				{
					if (config.SSID.toLowerCase(Locale.getDefault()).contains(charText))
					{					
						networks.add(config);
					}
				}
			}
			Log.e(String.valueOf(networks.size()), String.valueOf(networks_raw.size()));

			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return networks.size();
		}

		public WifiConfiguration getNetwork(int position){
			return networks.get(position);
		}
	} 

	
	
}
