package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import pt.utl.ist.cmov.bomberman.activities.GameActivity;
import pt.utl.ist.cmov.bomberman.activities.PlayerActivity;
import pt.utl.ist.cmov.bomberman.game.drawing.Drawing;
import pt.utl.ist.cmov.bomberman.game.drawing.DrawingFactory;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Canvas;

public class GameClient implements IGameClient {

	private GameActivity activity;

	private String username;
	private HashMap<String, BombermanPlayer> players;

	protected IGameServer gameServerProxy;
	private ConcurrentHashMap<Integer, Drawing> drawings;

	public GameClient(GameActivity activity, String username) {
		super();
		this.activity = activity;

		this.username = username;
		this.drawings = new ConcurrentHashMap<Integer, Drawing>();

		this.players = new HashMap<String, BombermanPlayer>();
		this.players.put(username, new BombermanPlayer(-1, username));
	}

	public BombermanPlayer getPlayer() {
		return this.players.get(this.username);
	}

	public void setGameServer(IGameServer gameServer) {
		this.gameServerProxy = gameServer;
	}

	public void init(Integer lines, Integer cols, ArrayList<ModelDTO> models) {
		MapMeasurements.updateMapSize(cols, lines);
		updateScreen(models);
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

	public void pause() {
		gameServerProxy.pause(this.username);
	}

	public void quit() {
		gameServerProxy.quit(this.username);

		// TODO: we need to change this
		if (activity instanceof PlayerActivity) {
			activity.finish();
		}
	}

	@Override
	public void updateScreen(ArrayList<ModelDTO> models) {
		for (ModelDTO model : models) {
			Drawing drawing = DrawingFactory.create(model);
			this.drawings.put(drawing.getId(), drawing);
		}
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		this.players = players;
	}

	@Override
	public void startServer(Integer width, Integer height,
			ArrayList<ModelDTO> models) {
		activity.startNewServer(width, height, models);
	}

	public void draw(Context context, Canvas canvas) {
		if (MapMeasurements.isReady()) {
			for (Drawing d : drawings.values()) {
				d.draw(context, canvas);
			}
		}
	}

}
