package pt.utl.ist.cmov.bomberman.handlers.channels;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.managers.ICommunicationManager;
import android.util.Log;

public class SocketCommunicationChannel implements ICommunicationChannel,
		Runnable {

	private Socket socket = null;
	private ICommunicationManager commManager;

	public SocketCommunicationChannel(Socket socket,
			ICommunicationManager commManager) {
		this.socket = socket;
		this.commManager = commManager;
	}

	private InputStream iStream;
	private OutputStream oStream;
	private static final String TAG = "CommunicationHandler";

	@Override
	public void send(CommunicationObject object) {
		write(object);
	}

	@Override
	public void run() {
		try {

			socket.setTcpNoDelay(true);
			iStream = socket.getInputStream();
			oStream = socket.getOutputStream();

			commManager.addCommChannel(this);

			while (true) {
				try {
					ObjectInputStream in = new ObjectInputStream(iStream);
					CommunicationObject object = (CommunicationObject) in
							.readObject();

					commManager.receive(object);
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

	private void write(Object obj) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(oStream);
			out.writeObject(obj);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

}
