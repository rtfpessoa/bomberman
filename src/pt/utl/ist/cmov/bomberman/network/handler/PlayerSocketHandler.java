package pt.utl.ist.cmov.bomberman.network.handler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.network.channel.SocketCommunicationChannel;
import pt.utl.ist.cmov.bomberman.network.proxy.ICommunicationProxy;
import pt.utl.ist.cmov.bomberman.util.Constants;
import android.util.Log;

public class PlayerSocketHandler extends Thread {

	private static final String TAG = "ClientSocketHandler";
	private ICommunicationProxy commManager;
	private SocketCommunicationChannel channel;
	private InetAddress mAddress;

	public PlayerSocketHandler(ICommunicationProxy commManager,
			InetAddress groupOwnerAddress) {
		this.commManager = commManager;
		this.mAddress = groupOwnerAddress;
	}

	@Override
	public void run() {
		Socket socket = new Socket();
		try {
			socket.bind(null);
			socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
					Constants.SERVER_PORT), 5000);
			Log.i(TAG, "Launching the I/O handler");
			channel = new SocketCommunicationChannel(socket, commManager);
			new Thread(channel).start();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}

}
