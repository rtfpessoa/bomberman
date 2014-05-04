package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

public class GameLocal extends Game {

	public GameLocal(Level level, MainGamePanel gamePanel) {
		super(level, gamePanel);
	}

	@Override
	public void moveBomberman(Direction dir) {
		if (this.canMoveBomberman(dir)) {
			Position newPos;
			switch (dir) {
			case UP:
				newPos = new Position(this.bombermanPos.x,
						this.bombermanPos.y - 1);
				this.gamePanel.move(this.bombermanPos, newPos);
				this.bombermanPos = newPos;
				break;
			case DOWN:
				newPos = new Position(this.bombermanPos.x,
						this.bombermanPos.y + 1);
				this.gamePanel.move(this.bombermanPos, newPos);
				this.bombermanPos = newPos;
				break;
			case LEFT:
				newPos = new Position(this.bombermanPos.x - 1,
						this.bombermanPos.y);
				this.gamePanel.move(this.bombermanPos, newPos);
				this.bombermanPos = newPos;
				break;
			case RIGHT:
				newPos = new Position(this.bombermanPos.x + 1,
						this.bombermanPos.y);
				this.gamePanel.move(this.bombermanPos, newPos);
				this.bombermanPos = newPos;
				break;
			}
		}
	}

}
