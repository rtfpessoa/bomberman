package pt.utl.ist.cmov.bomberman.listeners;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.activities.PeerChoiceActivity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class PeerListener implements PeerListListener {

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

	private PeerChoiceActivity activity;

	public PeerListener(PeerChoiceActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {

		peers.clear();
		peers.addAll(peerList.getDeviceList());

		this.activity.loadPeers(peers);
	}
}
