package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.controllers.WifiDirectController;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WifiDirectActivity extends FullScreenActivity implements
		ChannelListener {

	protected WifiP2pManager manager;
	protected final IntentFilter intentFilter = new IntentFilter();
	protected Channel channel;
	protected WifiDirectController wifiDirectController;
	protected WifiP2pDevice wifiP2pGroupOwner;

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

		wifiDirectController.discoverPeers();
	}

	@Override
	public void onResume() {
		super.onResume();
		this.wifiDirectController = new WifiDirectController(manager, channel,
				this);
		registerReceiver(wifiDirectController, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(this.wifiDirectController);
	}

	@Override
	protected void onStop() {
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
	public void onChannelDisconnected() {
		if (manager != null) {
			Toast.makeText(this, "Channel lost. Trying again",
					Toast.LENGTH_SHORT).show();
			manager.initialize(this, getMainLooper(), this);
		} else {
			Toast.makeText(
					this,
					"Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
					Toast.LENGTH_LONG).show();
		}
	}

}
