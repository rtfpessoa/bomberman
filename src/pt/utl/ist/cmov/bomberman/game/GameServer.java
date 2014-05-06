package pt.utl.ist.cmov.bomberman.game;

import android.util.Log;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

public class GameServer extends Game {

	public GameServer(Level level, MainGamePanel gamePanel) {
		super(level, gamePanel);
	}

	private void bombermanKilled(Integer bombermanId) {
		// TODO falta implementar
		if (bombermanId == 1) {
			this.bombermanPos = null;
		}
	}

	@Override
	public void moveBomberman(Direction dir) {
		if (this.bombermanPos != null && this.canMoveBomberman(dir)) {
			Position newPos = null;
			switch (dir) {
			case UP:
				newPos = new Position(this.bombermanPos.x,
						this.bombermanPos.y - 1);
				break;
			case DOWN:
				newPos = new Position(this.bombermanPos.x,
						this.bombermanPos.y + 1);
				break;
			case LEFT:
				newPos = new Position(this.bombermanPos.x - 1,
						this.bombermanPos.y);
				break;
			case RIGHT:
				newPos = new Position(this.bombermanPos.x + 1,
						this.bombermanPos.y);
				break;
			}

			if (this.level.getMap().getContent(newPos) == GameMap.EXPLODING) {
				this.level.getMap().putEmpty(this.bombermanPos);
				this.gamePanel.putEmpty(this.bombermanPos);
				this.bombermanKilled(1);
				return;
			}

			this.level.getMap().move(this.bombermanPos, newPos);
			this.gamePanel.move(this.bombermanPos, newPos);
			if (bombToDraw) {
				this.level.getMap().putBomb(this.bombermanPos);
				this.gamePanel.putBomb(this.bombermanPos);
				this.bombToDraw = false;
			}
			this.bombermanPos = newPos;
		}
	}

	@Override
	public void putBomb() {
		this.bombToDraw = true;

		this.explosionTimeoutHandler.postDelayed(new BombExplosion(
				this.bombermanPos), this.level.getExplosionTimeout());
	}

	private void putExploding(Position pos) {
		GameMap map = this.level.getMap();

		this.gamePanel.putExploding(pos);
		if (map.hasBomberman(pos)) {
			map.putExploding(pos);
			this.bombermanKilled(Character.getNumericValue(map.getContent(pos)));
			return;
		}
		map.putExploding(pos);
	}

	@Override
	public int[] explode(Position pos) {
		boolean upSearchStop = false;
		boolean downSearchStop = false;
		boolean leftSearchStop = false;
		boolean rightSearchStop = false;

		int explosionRange = this.level.getExplosionRange();
		// indexes: 0 = Up; 1 = Down; 2 = Left; 3 = Right
		int[] efectiveRange = { explosionRange, explosionRange, explosionRange,
				explosionRange };

		this.putExploding(pos);
		for (int i = 1; i <= explosionRange; i++) {
			if (!upSearchStop
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.EMPTY) {
					upSearchStop = true;
					efectiveRange[0] = i;
				}
				this.putExploding(new Position(pos.x, pos.y - i));
			} else if (!upSearchStop) {
				upSearchStop = true;
				efectiveRange[0] = i - 1;
			}

			if (!downSearchStop
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.EMPTY) {
					downSearchStop = true;
					efectiveRange[1] = i;
				}
				this.putExploding(new Position(pos.x, pos.y + i));
			} else if (!downSearchStop) {
				downSearchStop = true;
				efectiveRange[1] = i - 1;
			}

			if (!leftSearchStop
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.EMPTY) {
					leftSearchStop = true;
					efectiveRange[2] = i;
				}
				this.putExploding(new Position(pos.x - i, pos.y));
			} else if (!leftSearchStop) {
				leftSearchStop = true;
				efectiveRange[2] = i - 1;
			}

			if (!rightSearchStop
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.EMPTY) {
					rightSearchStop = true;
					efectiveRange[3] = i;
				}
				this.putExploding(new Position(pos.x + i, pos.y));
			} else if (!rightSearchStop) {
				rightSearchStop = true;
				efectiveRange[3] = i - 1;
			}
		}

		return efectiveRange;
	}

	@Override
	public void finishExplosion(Position pos, int[] efectiveRange) {
		GameMap map = this.level.getMap();

		this.gamePanel.putEmpty(pos);
		map.putEmpty(pos);

		// Up
		for (int i = 1; i <= efectiveRange[0]; i++) {
			this.gamePanel.putEmpty(new Position(pos.x, pos.y - i));
			map.putEmpty(pos.x, pos.y - i);
		}

		// Down
		for (int i = 1; i <= efectiveRange[1]; i++) {
			this.gamePanel.putEmpty(new Position(pos.x, pos.y + i));
			map.putEmpty(pos.x, pos.y + i);
		}

		// Left
		for (int i = 1; i <= efectiveRange[2]; i++) {
			this.gamePanel.putEmpty(new Position(pos.x - i, pos.y));
			map.putEmpty(pos.x - i, pos.y);
		}

		// Right
		for (int i = 1; i <= efectiveRange[3]; i++) {
			this.gamePanel.putEmpty(new Position(pos.x + i, pos.y));
			map.putEmpty(pos.x + i, pos.y);
		}
	}

}
