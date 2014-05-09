package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;

public class GameClient {

	private BombermanPlayer player;

	private MainGamePanel gamePanel;
	protected IGameServer gameServer;

	public GameClient(String username, MainGamePanel gamePanel) {
		super();
		this.player = new BombermanPlayer(username);

		this.gamePanel = gamePanel;
	}

	public void putBomberman() {
		gameServer.putBomberman(player.getUsername());
	}

	public void putBomb() {
		// TODO: put bomb
	}

	public void move(Direction dir) {
		// TODO: move bomberman
	}

}
