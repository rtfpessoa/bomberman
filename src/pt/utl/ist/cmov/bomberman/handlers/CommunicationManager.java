package pt.utl.ist.cmov.bomberman.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

			iStream = socket.getInputStream();
			oStream = socket.getOutputStream();
			byte[] buffer = new byte[1024];
			int bytes;
			handler.obtainMessage(Constants.MESSAGE_HANDLE, this)
					.sendToTarget();

			while (true) {
				try {
					// Read from the InputStream
					bytes = iStream.read(buffer);
					if (bytes == -1) {
						break;
					}

					// Send the obtained bytes to the UI Activity
					Log.d(TAG, "Rec:" + String.valueOf(buffer));
					handler.obtainMessage(Constants.MESSAGE_READ, bytes, -1,
							buffer).sendToTarget();
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

	public void write(byte[] buffer) {
		try {
			oStream.write(buffer);
		} catch (IOException e) {
			Log.e(TAG, "Exception during write", e);
		}
	}

}
