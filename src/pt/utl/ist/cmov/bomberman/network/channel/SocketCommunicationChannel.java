package pt.utl.ist.cmov.bomberman.network.channel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import pt.utl.ist.cmov.bomberman.network.proxy.ICommunicationProxy;
import android.util.Log;

public class SocketCommunicationChannel implements ICommunicationChannel,
		Runnable {

	private Socket socket = null;
	private ICommunicationProxy commManager;

	private ObjectInputStream iStream;
	private ObjectOutputStream oStream;
	private static final String TAG = "CommunicationHandler";

	private Object object;

	private Boolean isRunning;

	public SocketCommunicationChannel(Socket socket,
			ICommunicationProxy commManager) {
		this.socket = socket;
		this.commManager = commManager;
		this.isRunning = true;

		setupStreams();
	}

	private void setupStreams() {
		try {
			socket.setTcpNoDelay(true);
			oStream = new ObjectOutputStream(socket.getOutputStream());
			oStream.flush();
			iStream = new ObjectInputStream(socket.getInputStream());

			commManager.addCommChannel(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(Object object) {
		write(object);
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				try {
					object = iStream.readObject();
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

	@Override
	public String getChannelEndpoint() {
		InetAddress address = ((InetSocketAddress) socket
				.getRemoteSocketAddress()).getAddress();
		return address.getHostAddress();
	}

}
