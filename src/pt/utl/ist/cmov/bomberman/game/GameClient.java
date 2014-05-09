package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.util.Direction;

public class GameClient {

	private BombermanPlayer player;

	private MainGamePanel gamePanel;
	protected IGameServer gameServerProxy;

	public GameClient(String username, MainGamePanel gamePanel) {
		super();
		this.player = new BombermanPlayer(username);

		this.gamePanel = gamePanel;
	}

	public void setGameServer(IGameServer gameServer) {
		this.gameServerProxy = gameServer;
	}

	public void putBomberman() {
		gameServerProxy.putBomberman(player.getUsername());
	}

	public void putBomb() {
		// TODO: put bomb
	}

	public void move(Direction dir) {
		// TODO: move bomberman
	}

}
