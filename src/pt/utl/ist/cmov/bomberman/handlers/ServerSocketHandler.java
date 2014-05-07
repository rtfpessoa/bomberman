package pt.utl.ist.cmov.bomberman.handlers;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pt.utl.ist.cmov.bomberman.activities.interfaces.CommunicationPeer;
import android.util.Log;

public class ServerSocketHandler extends Thread {

	ServerSocket socket = null;
	private final int THREAD_COUNT = 10;
	private CommunicationPeer cPeer;
	private static final String TAG = "GroupOwnerSocketHandler";

	public ServerSocketHandler(CommunicationPeer cPeer) throws IOException {
		try {
			socket = new ServerSocket(4545);
			this.cPeer = cPeer;
			Log.d("GroupOwnerSocketHandler", "Socket Started");
		} catch (IOException e) {
			e.printStackTrace();
			pool.shutdownNow();
			throw e;
		}

	}

	private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
			THREAD_COUNT, THREAD_COUNT, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	@Override
	public void run() {
		while (true) {
			try {
				// A blocking operation. Initiate a ChatManager instance when
				// there is a new connection
				pool.execute(new CommunicationManager(socket.accept(), cPeer));
				Log.d(TAG, "Launching the I/O handler");

			} catch (IOException e) {
				try {
					if (socket != null && !socket.isClosed())
						socket.close();
				} catch (IOException ioe) {

				}
				e.printStackTrace();
				pool.shutdownNow();
				break;
			}
		}
	}

}
