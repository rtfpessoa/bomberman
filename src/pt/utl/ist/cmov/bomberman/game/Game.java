package pt.utl.ist.cmov.bomberman.game;

import android.os.Handler;
import android.util.Log;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Game {

	protected Level level;
	protected MainGamePanel gamePanel;
	protected Position bombermanPos;
	protected boolean bombToDraw;
	protected Handler explosionTimeoutHandler;
	protected Handler explosionDurationHandler;

	public Game(Level level, MainGamePanel gamePanel) {
		super();
		this.level = level;
		this.gamePanel = gamePanel;
		this.bombToDraw = false;
		this.explosionTimeoutHandler = new Handler();
		this.explosionDurationHandler = new Handler();

		mapSearch: for (Integer y = 0; y < this.level.getMap().getHeight(); y++) {
			for (Integer x = 0; x < this.level.getMap().getWidth(); x++) {
				if (this.level.getMap().getContent(x, y) == '1') {
					this.bombermanPos = new Position(x, y);
					break mapSearch;
				}
			}

		}

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

	protected boolean canMoveBomberman(Direction dir) {
		Position newPos = null;

		switch (dir) {
		case UP:
			newPos = new Position(this.bombermanPos.x, this.bombermanPos.y - 1);
			break;
		case DOWN:
			newPos = new Position(this.bombermanPos.x, this.bombermanPos.y + 1);
			break;
		case LEFT:
			newPos = new Position(this.bombermanPos.x - 1, this.bombermanPos.y);
			break;
		case RIGHT:
			newPos = new Position(this.bombermanPos.x + 1, this.bombermanPos.y);
			break;
		}

		if (this.level.getMap().getContent(newPos) == GameMap.EMPTY
				|| this.level.getMap().getContent(newPos) == GameMap.EXPLODING)
			return true;
		else
			return false;
	}

	public abstract void moveBomberman(Direction dir);

	public abstract void putBomb();

	public abstract int[] explode(Position pos);

	public abstract void finishExplosion(Position pos, int[] efectiveRange);

}
