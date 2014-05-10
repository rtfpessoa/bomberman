package pt.utl.ist.cmov.bomberman.handlers.channels;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

	private ObjectInputStream iStream;
	private ObjectOutputStream oStream;
	private static final String TAG = "CommunicationHandler";

	@Override
	public void send(String object) {
		write(object);
	}

	@Override
	public void run() {
		try {
			String object;

			socket.setTcpNoDelay(true);
			oStream = new ObjectOutputStream(socket.getOutputStream());
			oStream.flush();
			iStream = new ObjectInputStream(socket.getInputStream());

			commManager.addCommChannel(this);

			while (true) {
				try {
					object = (String) iStream.readObject();
					if (object != null) {
						commManager.receive(object);
					}
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
			oStream.writeObject(obj);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

}
