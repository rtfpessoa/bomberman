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
		// TODO falta implementar verificação de colisões
		return true;
	}

	public abstract void moveBomberman(Direction dir);

}
