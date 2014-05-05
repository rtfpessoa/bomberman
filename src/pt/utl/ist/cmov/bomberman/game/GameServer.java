package pt.utl.ist.cmov.bomberman.game;

import android.util.Log;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

public class GameServer extends Game {

	public GameServer(Level level, MainGamePanel gamePanel) {
		super(level, gamePanel);
	}

	@Override
	public void moveBomberman(Direction dir) {
		if (this.canMoveBomberman(dir)) {
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

			this.level.getMap().move(this.bombermanPos, newPos);
			this.gamePanel.move(this.bombermanPos, newPos);
			this.bombermanPos = newPos;
		}
	}

}
