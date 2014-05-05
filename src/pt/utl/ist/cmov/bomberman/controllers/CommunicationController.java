package pt.utl.ist.cmov.bomberman.controllers;

import pt.utl.ist.cmov.bomberman.activities.PlayerActivity;
import pt.utl.ist.cmov.bomberman.listeners.ConnectionListener;
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

public class CommunicationController extends BroadcastReceiver {

	private WifiP2pManager mManager;

	private Channel mChannel;

	private PlayerActivity activity;

	public CommunicationController(WifiP2pManager manager, Channel channel,
			PlayerActivity activity) {
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
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			if (mManager == null) {
				return;
			}

			NetworkInfo networkInfo = (NetworkInfo) intent
					.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

			if (networkInfo.isConnected()) {
				mManager.requestConnectionInfo(mChannel,
						new ConnectionListener(this.activity));
			}
		}
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