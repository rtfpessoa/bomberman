package pt.utl.ist.cmov.bomberman.controllers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
				WifiManager wifi = (WifiManager) activity
						.getSystemService(Context.WIFI_SERVICE);
				wifi.setWifiEnabled(true);
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
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
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
		mManager.connect(mChannel, config, new ActionListener() {
			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int reason) {
			}
		});
	}
}