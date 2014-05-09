package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;
import android.util.Log;

public class BombElement extends Element {

	private BombermanElement bomberman;

	private Handler bombHandler = new Handler();
	private Runnable bombRunnable;

	public BombElement(Level level, Integer id, Position pos,
			BombermanElement bomberman) {
		super(level, Level.BOMB, id, pos);

		this.bomberman = bomberman;

		this.bombRunnable = new BombExplosion(this);
		bombHandler.postDelayed(bombRunnable, level.getExplosionTimeout());
	}

	public BombermanElement getBomberman() {
		return this.bomberman;
	}

	@Override
	public boolean canMoveOver(Element element) {
		return false;
	}

	private int[] explode(BombElement bomb) {
		Position pos = bomb.getPos();

		boolean upSearchStop = false;
		boolean downSearchStop = false;
		boolean leftSearchStop = false;
		boolean rightSearchStop = false;

		int explosionRange = this.level.getExplosionRange();
		// indexes: 0 = Up; 1 = Down; 2 = Left; 3 = Right
		int[] efectiveRange = { explosionRange, explosionRange, explosionRange,
				explosionRange };

		this.level.putExploding(bomb, bomb.getPos());
		for (int i = 1; i <= explosionRange; i++) {
			if (!upSearchStop
					&& this.level.getContent(pos.x, pos.y - i) != Level.WALL
					&& this.level.getContent(pos.x, pos.y - i) != Level.EXPLODING
					&& this.level.getContent(pos.x, pos.y - i) != Level.BOMB) {
				if (this.level.getContent(pos.x, pos.y - i) != Level.EMPTY) {
					upSearchStop = true;
					efectiveRange[0] = i;
				}
				this.level.putExploding(bomb, new Position(pos.x, pos.y - i));
			} else if (!upSearchStop) {
				upSearchStop = true;
				efectiveRange[0] = i - 1;
			}

			if (!downSearchStop
					&& this.level.getContent(pos.x, pos.y + i) != Level.WALL
					&& this.level.getContent(pos.x, pos.y + i) != Level.EXPLODING
					&& this.level.getContent(pos.x, pos.y + i) != Level.BOMB) {
				if (this.level.getContent(pos.x, pos.y + i) != Level.EMPTY) {
					downSearchStop = true;
					efectiveRange[1] = i;
				}
				this.level.putExploding(bomb, new Position(pos.x, pos.y + i));
			} else if (!downSearchStop) {
				downSearchStop = true;
				efectiveRange[1] = i - 1;
			}

			if (!leftSearchStop
					&& this.level.getContent(pos.x - i, pos.y) != Level.WALL
					&& this.level.getContent(pos.x - i, pos.y) != Level.EXPLODING
					&& this.level.getContent(pos.x - i, pos.y) != Level.BOMB) {
				if (this.level.getContent(pos.x - i, pos.y) != Level.EMPTY) {
					leftSearchStop = true;
					efectiveRange[2] = i;
				}
				this.level.putExploding(bomb, new Position(pos.x - i, pos.y));
			} else if (!leftSearchStop) {
				leftSearchStop = true;
				efectiveRange[2] = i - 1;
			}

			if (!rightSearchStop
					&& this.level.getContent(pos.x + i, pos.y) != Level.WALL
					&& this.level.getContent(pos.x + i, pos.y) != Level.EXPLODING
					&& this.level.getContent(pos.x + i, pos.y) != Level.BOMB) {
				if (this.level.getContent(pos.x + i, pos.y) != Level.EMPTY) {
					rightSearchStop = true;
					efectiveRange[3] = i;
				}
				this.level.putExploding(bomb, new Position(pos.x + i, pos.y));
			} else if (!rightSearchStop) {
				rightSearchStop = true;
				efectiveRange[3] = i - 1;
			}
		}

		return efectiveRange;
	}

	private void finishExplosion(Position pos, int[] efectiveRange) {
		Level map = this.level;

		map.putEmpty(pos);

		// Up
		for (int i = 1; i <= efectiveRange[0]; i++) {
			map.putEmpty(new Position(pos.x, pos.y - i));
		}

		// Down
		for (int i = 1; i <= efectiveRange[1]; i++) {
			map.putEmpty(new Position(pos.x, pos.y + i));
		}

		// Left
		for (int i = 1; i <= efectiveRange[2]; i++) {
			map.putEmpty(new Position(pos.x - i, pos.y));
		}

		// Right
		for (int i = 1; i <= efectiveRange[3]; i++) {
			map.putEmpty(new Position(pos.x + i, pos.y));
		}
	}

	class BombExplosion implements Runnable {

		private BombElement bomb;
		private Boolean hasExploded = false;
		private int[] efectiveRange;

		public BombExplosion(BombElement bomb) {
			super();
			this.bomb = bomb;
		}

		@Override
		public void run() {
			Log.d("BombExplosion", "Explosion");
			if (!hasExploded) {
				efectiveRange = explode(this.bomb);
				bombHandler.postDelayed(bombRunnable,
						level.getExplosionDuration());
			} else {
				finishExplosion(bomb.getPos(), this.efectiveRange);
			}
		}

	}
}