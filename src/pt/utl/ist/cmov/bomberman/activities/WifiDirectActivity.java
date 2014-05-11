package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.controllers.WifiDirectController;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;

public class WifiDirectActivity extends FullScreenActivity {

	protected WifiP2pManager manager;
	protected final IntentFilter intentFilter = new IntentFilter();
	protected Channel channel;
	protected WifiDirectController wifiDirectController;
	protected WifiP2pDevice wifiP2pGroupOwner;

	private Boolean intentRegistered = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		this.channel = manager.initialize(this, getMainLooper(), null);
		this.intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		this.intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		this.intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		this.intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		this.wifiDirectController = new WifiDirectController(manager, channel,
				this);

		wifiDirectController.setupWifi();

		wifiDirectController.discoverPeers();
	}

	@Override
	public void onResume() {
		super.onResume();
		prepareIntentReceiver();
	}

	@Override
	public void onPause() {
		super.onPause();
		prepareIntentReceiver();
	}

	private void prepareIntentReceiver() {
		if (!this.intentRegistered) {
			this.wifiDirectController = new WifiDirectController(manager,
					channel, this);
			registerReceiver(wifiDirectController, intentFilter);
			this.intentRegistered = true;
		} else {
			unregisterReceiver(this.wifiDirectController);
			this.intentRegistered = false;
		}
	}

}
