package pt.utl.ist.cmov.bomberman.listeners;

import java.net.InetAddress;
import java.net.UnknownHostException;

import pt.utl.ist.cmov.bomberman.activities.PlayerActivity;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;

public class ConnectionListener implements ConnectionInfoListener {

	private PlayerActivity activity;

	public ConnectionListener(PlayerActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onConnectionInfoAvailable(final WifiP2pInfo info) {

		InetAddress groupOwnerAddress;

		try {
			groupOwnerAddress = InetAddress.getAllByName(info.groupOwnerAddress
					.getHostAddress())[0];
		} catch (UnknownHostException e) {
			return;
		}

		// After the group negotiation, we can determine the group owner.
		if (info.groupFormed && info.isGroupOwner) {
			// Do whatever tasks are specific to the group owner.
			// One common case is creating a server thread and accepting
			// incoming connections.
		} else if (info.groupFormed) {
			// The other device acts as the client. In this case,
			// you'll want to create a client thread that connects to the group
			// owner.
		}
		
		activity.printConnectionInfo(info);
	}
}
