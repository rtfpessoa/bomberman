package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;

public class GameClient implements IGameClient {

	private String username;
	private HashMap<String, BombermanPlayer> players;

	private MainGamePanel gamePanel;
	protected IGameServer gameServerProxy;
	private ConcurrentHashMap<Integer, Drawing> drawings;
	private ArrayList<Drawing> initialElements;
	private Integer lines;
	private Integer cols;

	private Handler handler;

	private Handler initHandler;
	private Runnable initRunnable;

	public GameClient(String username, MainGamePanel gamePanel) {
		super();
		this.username = username;
		this.gamePanel = gamePanel;
		this.drawings = new ConcurrentHashMap<Integer, Drawing>();

		this.players = new HashMap<String, BombermanPlayer>();
		this.players.put(username, new BombermanPlayer(username));

		this.initHandler = new Handler();
		this.initRunnable = new Runnable() {

			@Override
			public void run() {
				init();
			}
		};
	}

	public Handler getHandler() {
		return handler;
	}

	public void setGameServer(IGameServer gameServer) {
		this.gameServerProxy = gameServer;
	}

	public void init(Integer lines, Integer cols, ArrayList<Drawing> elements) {
		this.initialElements = elements;
		this.lines = lines;
		this.cols = cols;
	}

	public void init() {
		if (initialElements != null) {
			MapMeasurements.updateMapMeasurements(gamePanel.getWidth(),
					gamePanel.getHeight(), cols, lines);

			updateScreen(initialElements);
		} else {
			this.initHandler.postDelayed(this.initRunnable, 1000);
		}
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
		for (Drawing drawing : drawings) {
			this.drawings.put(drawing.getId(), drawing);
		}
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		this.players = players;
	}

	public void draw(Context context, Canvas canvas) {
		for (Drawing d : drawings.values()) {
			d.draw(context, canvas);
		}
	}

	public BombermanPlayer getPlayer() {
		return this.players.get(this.username);
	}

}
