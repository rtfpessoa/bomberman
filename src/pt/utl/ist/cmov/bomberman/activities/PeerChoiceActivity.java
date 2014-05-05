package pt.utl.ist.cmov.bomberman.activities;

import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.controllers.PeerDescoveryController;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PeerChoiceActivity extends FullScreenActivity {

	public static final String PEER_MESSAGE = "pt.utl.ist.cmov.bomberman.activities.PEER";

	private PeerDescoveryController peerDescoveryController;

	private Channel mChannel;

	private WifiP2pManager mManager;

	private IntentFilter mIntentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_peer_choice);

		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		peerDescoveryController = new PeerDescoveryController(mManager,
				mChannel, this);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

		peerDescoveryController.discoverPeers();
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(peerDescoveryController, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(peerDescoveryController);
	}

	public void refreshPeers(View view) {
		peerDescoveryController.discoverPeers();
	}

	public void loadPeers(List<WifiP2pDevice> peers) {
		ArrayAdapter<WifiP2pDevice> adapter = new ArrayAdapter<WifiP2pDevice>(
				this, R.layout.listview_line, peers);
		ListView listView = (ListView) findViewById(R.id.list_levels);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(listItemClickHandler);
	}

	private AdapterView.OnItemClickListener listItemClickHandler = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView list = (ListView) parent;
			peerChoosenAction(list.getItemAtPosition(position));
		}
	};

	private void peerChoosenAction(Object object) {
		Parcelable peer = (Parcelable) object;

		Intent intent = new Intent(this, PlayerActivity.class);
		intent.putExtra(PEER_MESSAGE, peer);
		startActivity(intent);
	}
}
