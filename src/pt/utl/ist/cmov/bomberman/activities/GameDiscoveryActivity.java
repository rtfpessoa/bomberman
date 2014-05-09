package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.adapters.GameAdapter;
import pt.utl.ist.cmov.bomberman.controllers.WifiDirectController;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class GameDiscoveryActivity extends FullScreenActivity implements
		PeerListListener, AdapterView.OnItemClickListener {

	public static final String DEVICE_MESSAGE = "pt.utl.ist.cmov.bomberman.DEVICE_MESSAGE";

	private WifiP2pManager manager;

	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private WifiDirectController discoveryController = null;

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_peer_choice);

		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);

		discoveryController = new WifiDirectController(manager, channel, this);

		discoveryController.setupWifi();

		discoveryController.discoverPeers();
	}

	public void refreshGames(View view) {
		discoveryController.discoverPeers();
		Toast.makeText(getApplicationContext(), "Discovering peers...",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStop() {
		if (manager != null && channel != null) {
			manager.removeGroup(channel, new ActionListener() {

				@Override
				public void onFailure(int reasonCode) {
					Log.d("BOMBERMAN", "Disconnect failed. Reason :"
							+ reasonCode);
				}

				@Override
				public void onSuccess() {
				}

			});
		}
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
		discoveryController = new WifiDirectController(manager, channel, this);
		registerReceiver(discoveryController, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(discoveryController);
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {

		peers.clear();
		peers.addAll(peerList.getDeviceList());

		loadPeers(peers);
	}

	public void loadPeers(List<WifiP2pDevice> peers) {
		GameAdapter adapter = new GameAdapter(this, peers);
		ListView listView = (ListView) findViewById(R.id.list_levels);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView list = (ListView) parent;

		WifiP2pDevice device = (WifiP2pDevice) list.getItemAtPosition(position);

		discoveryController.connect(device);

		Intent intent = new Intent(this, PlayerActivity.class);
		intent.putExtra(DEVICE_MESSAGE, device);
		startActivity(intent);
	}
}
