package pt.utl.ist.cmov.bomberman.activities.views;

import java.util.List;

import pt.utl.ist.cmov.bomberman.game.MainLoopThread;
import pt.utl.ist.cmov.bomberman.game.models.BombModel;
import pt.utl.ist.cmov.bomberman.game.models.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.models.Model;
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

	private List<List<Model>> modelsMap;
	private Context context;

	public MainGamePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);

		// create the game loop thread
		thread = new MainLoopThread(getHolder(), this);

		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	public void setModelsMap(List<List<Model>> modelsMap) {
		this.modelsMap = modelsMap;
	}

	public void putEmpty(int x, int y) {
		this.modelsMap.get(y).set(x, new EmptyModel());
	}

	public void putBomb(int x, int y) {
		this.modelsMap.get(y).set(
				x,
				new BombModel(this.context, MapMeasurements.POSITION_HEIGHT,
						MapMeasurements.POSITION_WIDTH,
						MapMeasurements.SIDE_PADDING
								+ MapMeasurements.POSITION_WIDTH * x,
						MapMeasurements.UP_PADDING
								+ MapMeasurements.POSITION_HEIGHT * y));
	}

	public void move(int origX, int origY, int destX, int destY) {
		Model model = this.modelsMap.get(destY).get(destX);
		this.modelsMap.get(destY).set(destX,
				this.modelsMap.get(origY).get(origX));
		this.modelsMap.get(origY).set(origX, model);
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

	@Override
	public void onDraw(Canvas canvas) {
		// fills the canvas with black
		canvas.drawColor(Color.rgb(16, 120, 48));

		for (List<Model> l : this.modelsMap) {
			for (Model m : l)
				m.draw(canvas);
		}
	}

}
