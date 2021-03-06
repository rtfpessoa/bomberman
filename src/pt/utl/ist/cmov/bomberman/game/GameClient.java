package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.net.wifi.p2p.WifiP2pDevice;

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

	public ArrayList<BombermanPlayer> getPlayers() {
		ArrayList<BombermanPlayer> playersArray = new ArrayList<BombermanPlayer>();
		playersArray.addAll(this.players.values());
		return playersArray;
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

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (activity instanceof PlayerActivity) {
			activity.finish();
		}
	}

	public void split() {
		gameServerProxy.split(this.username);
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
	public void startServer(String username, String levelName, Integer width,
			Integer height, ArrayList<ModelDTO> models,
			ArrayList<WifiP2pDevice> devices) {
		if (username.equals(activity.getUsername())) {
			activity.startNewServer(username, levelName, width, height, models,
					devices);
		}
	}

	@Override
	public void confirmQuit(String oldServer, String newServer) {
		if (!oldServer.equals(activity.getUsername())
				&& !newServer.equals(activity.getUsername())) {
			activity.disconnect();
		}
	}

	@Override
	public void endGame(HashMap<String, BombermanPlayer> players) {
		ArrayList<BombermanPlayer> playersValues = new ArrayList<BombermanPlayer>(
				players.values());

		Collections.sort(playersValues, new Comparator<BombermanPlayer>() {
			@Override
			public int compare(BombermanPlayer lhs, BombermanPlayer rhs) {
				return rhs.getScore().compareTo(lhs.getScore());
			}
		});

		this.activity.endGame(playersValues);
	}

	public void draw(Context context, Canvas canvas) {
		if (MapMeasurements.isReady()) {
			for (Drawing d : drawings.values()) {
				d.draw(context, canvas);
			}
		}
	}

}
