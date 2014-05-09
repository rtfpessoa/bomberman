package pt.utl.ist.cmov.bomberman.handlers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.activities.interfaces.CommunicationPeer;
import pt.utl.ist.cmov.bomberman.handlers.channels.SocketCommunicationChannel;
import pt.utl.ist.cmov.bomberman.util.Constants;
import android.util.Log;

public class PlayerSocketHandler extends Thread {

	private static final String TAG = "ClientSocketHandler";
	private CommunicationPeer cPeer;
	private SocketCommunicationChannel chat;
	private InetAddress mAddress;

	public PlayerSocketHandler(CommunicationPeer cPeer,
			InetAddress groupOwnerAddress) {
		this.cPeer = cPeer;
		this.mAddress = groupOwnerAddress;
	}

	@Override
	public void run() {
		Socket socket = new Socket();
		try {
			socket.bind(null);
			socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
					Constants.SERVER_PORT), 5000);
			Log.d(TAG, "Launching the I/O handler");
			chat = new SocketCommunicationChannel(socket, cPeer);
			new Thread(chat).start();
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

	public SocketCommunicationChannel getChat() {
		return chat;
	}

}
