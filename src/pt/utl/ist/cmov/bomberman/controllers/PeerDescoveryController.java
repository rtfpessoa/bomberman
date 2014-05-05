package pt.utl.ist.cmov.bomberman.controllers;

import pt.utl.ist.cmov.bomberman.activities.PeerChoiceActivity;
import pt.utl.ist.cmov.bomberman.listeners.PeerListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class PeerDescoveryController extends BroadcastReceiver {

	private WifiP2pManager mManager;

	private Channel mChannel;

	private PeerListener peerListListener;

	private PeerChoiceActivity activity;

	public PeerDescoveryController(WifiP2pManager manager, Channel channel,
			PeerChoiceActivity activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.activity = activity;
		this.peerListListener = new PeerListener(activity);
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
				mManager.requestPeers(mChannel, peerListListener);
			}
		}
	}

	public void discoverPeers() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				Log.d("RTFPESSOA", "Discovered peers successfully!");
			}

			@Override
			public void onFailure(int reasonCode) {
				Log.e("RTFPESSOA", "Failed to discovered peers!");
			}
		});
	}
}