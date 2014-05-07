package pt.utl.ist.cmov.bomberman.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import pt.utl.ist.cmov.bomberman.util.Constants;
import android.os.Handler;
import android.util.Log;

public class CommunicationManager implements Runnable {

	private Socket socket = null;
	private Handler handler;

	public CommunicationManager(Socket socket, Handler handler) {
		this.socket = socket;
		this.handler = handler;
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
			handler.obtainMessage(Constants.MESSAGE_HANDLE, this)
					.sendToTarget();

			while (true) {
				try {
					DataInputStream in = new DataInputStream(iStream);
					String str = in.readUTF();

					handler.obtainMessage(Constants.MESSAGE_READ, -1, -1, str)
							.sendToTarget();
				} catch (IOException e) {
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

	public void write(String str) {
		try {
			DataOutputStream out = new DataOutputStream(oStream);
			out.writeUTF(str);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

}
