package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

public class GameClient implements IGameClient {

	private String username;
	private HashMap<String, BombermanPlayer> players;

	protected IGameServer gameServerProxy;
	private ConcurrentHashMap<Integer, Drawing> drawings;

	public GameClient(String username) {
		super();
		this.username = username;
		this.drawings = new ConcurrentHashMap<Integer, Drawing>();

		this.players = new HashMap<String, BombermanPlayer>();
		this.players.put(username, new BombermanPlayer(username));
	}

	public BombermanPlayer getPlayer() {
		return this.players.get(this.username);
	}

	public void setGameServer(IGameServer gameServer) {
		this.gameServerProxy = gameServer;
	}

	public void init(Integer lines, Integer cols, ArrayList<Drawing> elements) {
		MapMeasurements.updateMapSize(cols, lines);
		updateScreen(elements);
	}

	public void putBomberman() {
		gameServerProxy.putBomberman(this.username);
	}

	public void putBomb() {
		gameServerProxy.putBomb(this.username);
	}

	public void move(Direction direction) {
		gameServerProxy.move(this.username, direction);
	}

	@Override
	public void updateScreen(ArrayList<Drawing> drawings) {
		Log.e("BOMBERMAN", "UpdatedScreen with : " + drawings.size());
		for (Drawing drawing : drawings) {
			this.drawings.put(drawing.getId(), drawing);
		}
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		this.players = players;
	}

	public void draw(Context context, Canvas canvas) {
		if (MapMeasurements.isReady()) {
			for (Drawing d : drawings.values()) {
				d.draw(context, canvas);
			}
		}
	}

}
