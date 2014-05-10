package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.adapters.GameAdapter;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
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

		Intent intent = new Intent(this, PlayerActivity.class);
		intent.putExtra(DEVICE_MESSAGE, device);
		startActivity(intent);
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		// INFO: this is not needed
	}
}
