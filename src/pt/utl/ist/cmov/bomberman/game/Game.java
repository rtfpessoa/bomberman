package pt.utl.ist.cmov.bomberman.game;

import android.os.Handler;
import android.util.Log;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Game {

	protected Level level;
	protected MainGamePanel gamePanel;
	protected Position bombermanPos;
	protected boolean bombToDraw;
	protected Handler explosionTimeoutHandler;
	protected Handler explosionDurationHandler;
	protected Handler moveRobotsHandler;


	public Game(Level level, MainGamePanel gamePanel, Integer bombermanId) {
		super();
		this.level = level;
		this.gamePanel = gamePanel;
		this.gamePanel.setGame(this);
		this.bombToDraw = false;
		this.explosionTimeoutHandler = new Handler();
		this.explosionDurationHandler = new Handler();
		this.bombermanPos = this.level.getBombermanInitialPos(bombermanId);
		this.level.getMap().putBomberman(this.bombermanPos, bombermanId);
		this.moveRobotsHandler = new Handler();
	}

	protected class BombExplosion implements Runnable {

		private Position bombPos;

		public BombExplosion(Position bombPos) {
			super();
			this.bombPos = bombPos;
		}

		@Override
		public void run() {
			Log.d("BombExplosion", "Explosion");
			int[] efectiveRange = explode(this.bombPos);
			explosionDurationHandler.postDelayed(new BombExplosionFinish(
					this.bombPos, efectiveRange), level.getExplosionDuration());
		}

	}

	protected class BombExplosionFinish implements Runnable {

		private Position bombPos;
		private int[] efectiveRange;

		public BombExplosionFinish(Position bombPos, int[] efectiveRange) {
			super();
			this.bombPos = bombPos;
			this.efectiveRange = efectiveRange;
		}

		@Override
		public void run() {
			Log.d("BombExplosionFinish", "Finishing explosion");
			finishExplosion(bombPos, this.efectiveRange);
		}

	}

	protected class MoveRobots implements Runnable {

		public MoveRobots() {
			super();
		}

		@Override
		public void run() {
			moveRobots();
			moveRobotsHandler.postDelayed(this, 800);
		}

	}
	protected boolean canMove(Direction dir, Position current) {
		Position newPos = MapMeasurements.calculateNextPosition(dir, current);

		if (this.level.getMap().getContent(newPos) == GameMap.EMPTY
				|| this.level.getMap().getContent(newPos) == GameMap.EXPLODING)
			return true;
		else
			return false;
	}

	public abstract void init();
	
	public abstract void moveBomberman(Direction dir);
	
	public abstract void moveRobots();

	public abstract void putBomb();

	public abstract int[] explode(Position pos);

	public abstract void finishExplosion(Position pos, int[] efectiveRange);

}
