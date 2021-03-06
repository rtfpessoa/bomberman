package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.adapters.GameAdapter;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class GameDiscoveryActivity extends WifiDirectActivity implements
		PeerListListener, ConnectionInfoListener,
		AdapterView.OnItemClickListener {

	public static final String DEVICE_MESSAGE = "pt.utl.ist.cmov.bomberman.DEVICE_MESSAGE";

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_peer_choice);
	}

	public void refreshGames(View view) {
		this.wifiDirectController.discoverPeers();
		Toast.makeText(getApplicationContext(), "Discovering peers...",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {

		peers.clear();
		peers.addAll(peerList.getDeviceList());

		loadPeers(peers);
	}

	public void loadPeers(List<WifiP2pDevice> peers) {
		List<WifiP2pDevice> gameOwners = new ArrayList<WifiP2pDevice>();

		for (WifiP2pDevice device : peers) {
			if (device.isGroupOwner()) {
				gameOwners.add(device);
			}
		}

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

		Intent intent = new Intent(this, PlayerActivity.class);
		intent.putExtra(DEVICE_MESSAGE, device);
		String username = getIntent().getExtras().getString(
				MainActivity.INTENT_USERNAME);
		intent.putExtra(MainActivity.INTENT_USERNAME, username);
		finish();
		startActivity(intent);
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// INFO: this is not needed
	}
}
