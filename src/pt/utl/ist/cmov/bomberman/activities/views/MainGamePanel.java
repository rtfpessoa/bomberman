package pt.utl.ist.cmov.bomberman.activities.views;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.game.GameMap;
import pt.utl.ist.cmov.bomberman.game.MainLoopThread;
import pt.utl.ist.cmov.bomberman.game.models.Robot;
import pt.utl.ist.cmov.bomberman.game.models.Wall;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();

	private static final Integer SIDE_PADDING = 15;
	private static final Integer UP_PADDING = 20;
	private static final Integer POSITION_HEIGHT = 40;
	private static final Integer POSITION_WIDTH = 25;

	private MainLoopThread thread;
	
	private ArrayList<Wall> walls = new ArrayList<Wall>();

	public MainGamePanel(Context context, GameMap initialMap) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		for (Integer y = 0; y < initialMap.getHeight(); y++) {
			for (Integer x = 0; x < initialMap.getWidth(); x++) {
				if (initialMap.getContent(x, y) == GameMap.WALL) {
					this.walls.add(new Wall(context, POSITION_HEIGHT,
							POSITION_WIDTH, SIDE_PADDING + POSITION_WIDTH * x,
							UP_PADDING + POSITION_HEIGHT * y));
				}
			}
		}

		// create the game loop thread
		thread = new MainLoopThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { if
	 * (event.getAction() == MotionEvent.ACTION_DOWN) { // delegating event
	 * handling to the droid droid.handleActionDown((int) event.getX(), (int)
	 * event.getY());
	 * 
	 * // check if in the lower part of the screen we exit if (event.getY() >
	 * getHeight() - 50) { thread.setRunning(false); ((Activity)
	 * getContext()).finish(); } else { Log.d(TAG, "Coords: x=" + event.getX() +
	 * ",y=" + event.getY()); } } if (event.getAction() ==
	 * MotionEvent.ACTION_MOVE) { // the gestures if (droid.isTouched()) { //
	 * the droid was picked up and is being dragged droid.setX((int)
	 * event.getX()); droid.setY((int) event.getY()); } } if (event.getAction()
	 * == MotionEvent.ACTION_UP) { // touch was released if (droid.isTouched())
	 * { droid.setTouched(false); } } return true; }
	 */

	@Override
	public void onDraw(Canvas canvas) {
		// fills the canvas with black
		canvas.drawColor(Color.rgb(16, 120, 48));

		for (Wall w : this.walls) {
			w.draw(canvas);
		}
	}

}
