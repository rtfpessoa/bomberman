package pt.utl.ist.cmov.bomberman.controllers;

import pt.utl.ist.cmov.bomberman.util.AndroidConfigHelper;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;

public class GameDiscoveryController extends BroadcastReceiver {

	private WifiP2pManager mManager;

	private Channel mChannel;

	private Activity activity;

	public GameDiscoveryController(WifiP2pManager manager, Channel channel,
			Activity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int wifiState = intent
					.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
			if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
				AndroidConfigHelper.enableWifi(activity);
			}

			int wifiDirectState = intent.getIntExtra(
					WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (wifiDirectState == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
				AndroidConfigHelper.enableWifiDirect(mManager, mChannel);
			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			if (mManager != null) {
				mManager.requestPeers(mChannel, (PeerListListener) activity);
			}
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			if (mManager == null) {
				return;
			}

			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (networkInfo.isConnected()) {
				mManager.requestConnectionInfo(mChannel,
						(ConnectionInfoListener) activity);
			}
		}
	}

	public void discoverPeers() {
		mManager.discoverPeers(mChannel, new ActionListener() {
			@Override
			public void onSuccess() {
				Log.d("BOMBERMAN", "Discovered peers successfully!");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.e("BOMBERMAN", "Failed to discovered peers!");
			}
		});
	}

	public void connect(WifiP2pDevice device) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = device.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		mManager.connect(mChannel, config, new ActionListener() {
			@Override
			public void onSuccess() {
				Log.d("BOMBERMAN", "Connected successfully!");
			}

			@Override
			public void onFailure(int reason) {
				Log.d("BOMBERMAN", "Failed to connect!");
			}
		});
	}
}