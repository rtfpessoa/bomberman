package pt.utl.ist.cmov.bomberman.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.activities.interfaces.CommunicationPeer;
import pt.utl.ist.cmov.bomberman.util.Constants;
import android.util.Log;

public class CommunicationManager implements Runnable {

	private Socket socket = null;
	private CommunicationPeer cPeer;

	public CommunicationManager(Socket socket, CommunicationPeer cPeer) {
		this.socket = socket;
		this.cPeer = cPeer;
	}

	private InputStream iStream;
	private OutputStream oStream;
	private static final String TAG = "CommunicationHandler";

	@Override
	public void run() {
		try {

			socket.setTcpNoDelay(true);
			iStream = socket.getInputStream();
			oStream = socket.getOutputStream();

			cPeer.addCommunicationManager(this);

			while (true) {
				try {
					ObjectInputStream in = new ObjectInputStream(iStream);
					Object obj = in.readObject();

					cPeer.getHandler()
							.obtainMessage(Constants.MESSAGE_READ, -1, -1, obj)
							.sendToTarget();
				} catch (ClassNotFoundException e) {
					Log.e(TAG, "disconnected", e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void write(Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(oStream);
			out.writeObject(obj);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}
}
