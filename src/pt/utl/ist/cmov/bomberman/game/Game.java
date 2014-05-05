package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Game {

	protected Level level;
	protected MainGamePanel gamePanel;
	protected Position bombermanPos;

	public Game(Level level, MainGamePanel gamePanel) {
		super();
		this.level = level;
		this.gamePanel = gamePanel;

		mapSearch: for (Integer y = 0; y < this.level.getMap().getHeight(); y++) {
			for (Integer x = 0; x < this.level.getMap().getWidth(); x++) {
				if (this.level.getMap().getContent(x, y) == '1') {
					this.bombermanPos = new Position(x, y);
					break mapSearch;
				}
			}

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

		if (this.level.getMap().getContent(newPos) == GameMap.EMPTY)
			return true;
		else
			return false;
	}

	public abstract void moveBomberman(Direction dir);

}
