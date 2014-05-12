package pt.utl.ist.cmov.bomberman.activities.views;

import java.nio.channels.FileChannel.MapMode;

import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.MainLoopThread;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private MainLoopThread thread;

	private GameClient gameClient;

	private Context context;

	public MainGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);

		this.context = context;
	}

	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		MapMeasurements.updateViewSize(getWidth(), getHeight());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		MapMeasurements.updateViewSize(getWidth(), getHeight());

		thread = new MainLoopThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Surface is being destroyed");
		stopAll();
		Log.i(TAG, "Thread was shut down cleanly");
	}

	public void stopAll() {
		if (thread != null) {
			thread.setRunning(false);

			boolean retry = true;
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
					// try again shutting down the thread
				}
			}
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(16, 120, 48));
		gameClient.draw(context, canvas);
	}

}
