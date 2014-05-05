package pt.utl.ist.cmov.bomberman.activities.views;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.GameMap;
import pt.utl.ist.cmov.bomberman.game.MainLoopThread;
import pt.utl.ist.cmov.bomberman.game.models.BombModel;
import pt.utl.ist.cmov.bomberman.game.models.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.models.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.models.Model;
import pt.utl.ist.cmov.bomberman.game.models.ObstacleModel;
import pt.utl.ist.cmov.bomberman.game.models.RobotModel;
import pt.utl.ist.cmov.bomberman.game.models.WallModel;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
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

	private GameMap map;

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

	public void setMap(GameMap map) {
		this.map = map;
	}

	public void putEmpty(Position pos) {
		this.modelsMap.get(pos.y).set(pos.x, new EmptyModel());
	}

	public void putBomb(Position pos) {
		this.modelsMap.get(pos.y).set(
				pos.x,
				new BombModel(this.context, MapMeasurements.POSITION_HEIGHT,
						MapMeasurements.POSITION_WIDTH,
						MapMeasurements.SIDE_PADDING
								+ MapMeasurements.POSITION_WIDTH * pos.x,
						MapMeasurements.UP_PADDING
								+ MapMeasurements.POSITION_HEIGHT * pos.y));
	}

	public void move(Position orig, Position dest) {
		Model modelDest = this.modelsMap.get(dest.y).get(dest.x);
		Model modelOrig = this.modelsMap.get(orig.y).get(orig.x);
		modelDest.setPos(new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * orig.x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* orig.y));
		modelOrig.setPos(new Position(MapMeasurements.SIDE_PADDING
				+ MapMeasurements.POSITION_WIDTH * dest.x,
				MapMeasurements.UP_PADDING + MapMeasurements.POSITION_HEIGHT
						* dest.y));
		this.modelsMap.get(dest.y).set(dest.x, modelOrig);
		this.modelsMap.get(orig.y).set(orig.x, modelDest);
	}

	private void parseMap(int viewWidth, int viewHeight) {

		int mapWidth = this.map.getWidth();
		int mapHeight = this.map.getHeight();

		MapMeasurements.updateMapMeasurements(viewWidth, viewHeight, mapWidth,
				mapHeight);

		this.modelsMap = new ArrayList<List<Model>>();

		for (Integer y = 0; y < mapHeight; y++) {
			this.modelsMap.add(new ArrayList<Model>());

			for (Integer x = 0; x < mapWidth; x++) {
				List<Model> line = this.modelsMap.get(y);
				Character content = this.map.getContent(x, y);

				if (content == GameMap.WALL) {
					line.add(new WallModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.OBSTACLE) {
					line.add(new ObstacleModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.ROBOT) {
					line.add(new RobotModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y));
				} else if (content == GameMap.EMPTY) {
					line.add(new EmptyModel());
				} else {
					line.add(new BombermanModel(context,
							MapMeasurements.POSITION_HEIGHT,
							MapMeasurements.POSITION_WIDTH,
							MapMeasurements.SIDE_PADDING
									+ MapMeasurements.POSITION_WIDTH * x,
							MapMeasurements.UP_PADDING
									+ MapMeasurements.POSITION_HEIGHT * y,
							Character.getNumericValue(content)));
				}
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		this.parseMap(this.getWidth(), this.getHeight());
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

//		for (List<Model> l : this.modelsMap) {
//			for (Model m : l)
//				m.draw(canvas);
//		}
	}

}
