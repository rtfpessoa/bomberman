package pt.utl.ist.cmov.bomberman.activities.views;

import pt.utl.ist.cmov.bomberman.game.Game;
import pt.utl.ist.cmov.bomberman.game.GameMap;
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

	private Game game;

	private GameMap map;

	public MainGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

	public GameMap getMap() {
		return this.map;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		boolean isNew = thread == null;

		thread = new MainLoopThread(getHolder(), this);

		if (isNew) {
			MapMeasurements.updateMapMeasurements(this.getWidth(),
					this.getHeight(), this.map.getWidth(), this.map.getHeight());
			this.map.parseMap();
			this.game.init();		}

		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		destroy();
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public void destroy() {
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
		// fills the canvas with black
		canvas.drawColor(Color.rgb(16, 120, 48));

		map.draw(canvas);
	}

}
