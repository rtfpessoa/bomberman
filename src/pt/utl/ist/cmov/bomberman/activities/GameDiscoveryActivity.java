package pt.utl.ist.cmov.bomberman.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.adapters.GameAdapter;
import pt.utl.ist.cmov.bomberman.activities.interfaces.MessageTarget;
import pt.utl.ist.cmov.bomberman.controllers.GameDiscoveryController;
import pt.utl.ist.cmov.bomberman.handlers.CommunicationManager;
import pt.utl.ist.cmov.bomberman.handlers.PlayerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.ServerSocketHandler;
import pt.utl.ist.cmov.bomberman.util.Constants;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class GameDiscoveryActivity extends FullScreenActivity implements
		Handler.Callback, PeerListListener, ConnectionInfoListener,
		MessageTarget, AdapterView.OnItemClickListener {

	public static final String PEER_MESSAGE = "pt.utl.ist.cmov.bomberman.activities.PEER";

	private WifiP2pManager manager;

	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private GameDiscoveryController discoveryController = null;

	private CommunicationManager commManager;

	private Handler handler = new Handler(this);

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

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

		discoveryController = new GameDiscoveryController(manager, channel,
				this);

		discoveryController.discoverPeers();
	}

	public void refreshGames(View view) {
		discoveryController.discoverPeers();

		if (commManager != null) {
			Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT)
					.show();
			commManager.write("isto e uma string");
		} else {
			Toast.makeText(getApplicationContext(), "Not Sent!",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.MESSAGE_READ:
			String readMessage = (String) msg.obj;
			// construct a string from the valid bytes in the buffer
			Log.e("BOMBERMAN-SOCKET", readMessage);
			Toast.makeText(getApplicationContext(), readMessage,
					Toast.LENGTH_LONG).show();
			break;

		case Constants.MESSAGE_HANDLE:
			Object obj = msg.obj;
			commManager = (CommunicationManager) obj;

		}
		return true;
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
		discoveryController = new GameDiscoveryController(manager, channel,
				this);
		registerReceiver(discoveryController, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(discoveryController);
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (p2pInfo.isGroupOwner) {
			Log.d("BOMBERMAN", "Connected as group owner");
			try {
				handler = new ServerSocketHandler(
						((MessageTarget) this).getHandler());
				handler.start();
			} catch (IOException e) {
				Log.d("BOMBERMAN",
						"Failed to create a server thread - " + e.getMessage());
				return;
			}
		} else {
			Log.d("BOMBERMAN", "Connected as peer");
			handler = new PlayerSocketHandler(
					((MessageTarget) this).getHandler(),
					p2pInfo.groupOwnerAddress);
			handler.start();
		}
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

		// Intent intent = new Intent(this, PlayerActivity.class);
		// intent.putExtra(PEER_MESSAGE, device);
		// startActivity(intent);
	}
}
