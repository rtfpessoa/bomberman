package pt.utl.ist.cmov.bomberman.handlers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.util.Constants;
import android.os.Handler;
import android.util.Log;

public class PlayerSocketHandler extends Thread {

	private static final String TAG = "ClientSocketHandler";
	private Handler handler;
	private CommunicationManager chat;
	private InetAddress mAddress;

	public PlayerSocketHandler(Handler handler, InetAddress groupOwnerAddress) {
		this.handler = handler;
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
			chat = new CommunicationManager(socket, handler);
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

	public CommunicationManager getChat() {
		return chat;
	}

}
