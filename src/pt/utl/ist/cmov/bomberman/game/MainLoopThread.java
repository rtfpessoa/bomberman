package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class MainLoopThread extends Thread {

	private static final String TAG = MainLoopThread.class.getSimpleName();

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;
	// The actual view that handles inputs
	// and draws to the surface
	private MainGamePanel gamePanel;

	// flag to hold game state
	private boolean running;

	public void setRunning(boolean running) {
		this.running = running;
	}

	public MainLoopThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting game loop");
		while (running) {
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.onDraw(canvas);
				}
			} finally {
				// in case of an exception the surface is not left in
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			} // end finally
		}
	}

}
