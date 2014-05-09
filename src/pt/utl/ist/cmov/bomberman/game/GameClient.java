package pt.utl.ist.cmov.bomberman.game;

import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.graphics.Canvas;

public class GameClient {

	private BombermanPlayer player;

	private MainGamePanel gamePanel;
	protected IGameServer gameServerProxy;
	private HashMap<Integer, Drawing> drawings;

	public GameClient(String username, MainGamePanel gamePanel) {
		super();
		this.player = new BombermanPlayer(username);
		this.gamePanel = gamePanel;
		this.drawings = new HashMap<Integer, Drawing>();
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

	public void draw(Canvas canvas) {
		for (Drawing d : drawings.values()) {
			d.draw(canvas);
		}
	}

}
