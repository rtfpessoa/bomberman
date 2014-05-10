package pt.utl.ist.cmov.bomberman.game;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.game.drawings.DrawingFactory;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import android.graphics.Canvas;
import android.os.Handler;

public class GameClient implements IGameClient {

	private String username;
	private HashMap<String, BombermanPlayer> players;

	private MainGamePanel gamePanel;
	protected IGameServer gameServerProxy;
	private ConcurrentHashMap<Integer, Drawing> drawings;
	private List<List<Element>> initialElements;

	private Handler handler;

	public GameClient(String username, MainGamePanel gamePanel) {
		super();
		this.username = username;
		this.gamePanel = gamePanel;
		this.drawings = new ConcurrentHashMap<Integer, Drawing>();

		this.players = new HashMap<String, BombermanPlayer>();
		this.players.put(username, new BombermanPlayer(username));
	}

	public Handler getHandler() {
		return handler;
	}

	public void setGameServer(IGameServer gameServer) {
		this.gameServerProxy = gameServer;
	}

	public void init(List<List<Element>> elements) {
		initialElements = elements;
	}

	public void init() {
		putBomberman();

		MapMeasurements.updateMapMeasurements(gamePanel.getWidth(),
				gamePanel.getHeight(), initialElements.get(0).size(),
				initialElements.size());

		for (List<Element> line : initialElements) {
			updateScreen(line);
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
	public void updateScreen(List<Element> drawings) {
		for (Element element : drawings) {
			Drawing drawing = DrawingFactory.create(gamePanel.getContext(),
					element);
			this.drawings.put(drawing.getId(), drawing);
		}
	}

	@Override
	public void updatePlayers(HashMap<String, BombermanPlayer> players) {
		this.players = players;
	}

	public void draw(Canvas canvas) {
		for (Drawing d : drawings.values()) {
			d.draw(canvas);
		}
	}

	public BombermanPlayer getPlayer() {
		return this.players.get(this.username);
	}

}
