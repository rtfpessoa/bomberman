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
	public void explode(Position pos) {
		boolean upWallFinded = false;
		boolean downWallFinded = false;
		boolean leftWallFinded = false;
		boolean rightWallFinded = false;

		this.putExploding(pos);
		for (int i = 1; i <= this.level.getExplosionRange(); i++) {
			if (!upWallFinded
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.WALL) {
				this.putExploding(new Position(pos.x, pos.y - i));
			} else
				upWallFinded = true;

			if (!downWallFinded
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.WALL) {
				this.putExploding(new Position(pos.x, pos.y - i));
			} else
				downWallFinded = true;

			if (!leftWallFinded
					&& this.level.getMap().getContent(pos.x, pos.y) != GameMap.WALL) {
				this.putExploding(new Position(pos.x + i, pos.y));
			} else
				leftWallFinded = true;

			if (!rightWallFinded
					&& this.level.getMap().getContent(pos.x, pos.y) != GameMap.WALL) {
				this.putExploding(new Position(pos.x + i, pos.y));
			} else
				rightWallFinded = true;
		}
	}

	@Override
	public void finishExplosion(Position pos) {
		GameMap map = this.level.getMap();

		this.gamePanel.putEmpty(pos);
		map.putEmpty(pos);
		for (int i = 1; i <= this.level.getExplosionRange(); i++) {
			if (this.level.getMap().getContent(pos.x, pos.y - i) == GameMap.EXPLODING) {
				this.gamePanel.putEmpty(new Position(pos.x, pos.y - i));
				map.putEmpty(pos.x, pos.y - i);
			}

			if (this.level.getMap().getContent(pos.x, pos.y + i) == GameMap.EXPLODING) {
				this.gamePanel.putEmpty(new Position(pos.x, pos.y + i));
				map.putEmpty(pos.x, pos.y - i);
			}

			if (this.level.getMap().getContent(pos.x, pos.y - i) == GameMap.EXPLODING) {
				this.gamePanel.putEmpty(new Position(pos.x - i, pos.y));
				map.putEmpty(pos.x, pos.y - i);
			}

			if (this.level.getMap().getContent(pos.x, pos.y + i) == GameMap.EXPLODING) {
				this.gamePanel.putEmpty(new Position(pos.x + i, pos.y));
				map.putEmpty(pos.x, pos.y - i);
			}
		}
	}

}
