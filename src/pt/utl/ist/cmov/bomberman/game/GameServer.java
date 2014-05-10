package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;
import pt.utl.ist.cmov.bomberman.game.drawings.DrawingFactory;
import pt.utl.ist.cmov.bomberman.game.elements.BombElement;
import pt.utl.ist.cmov.bomberman.game.elements.BombermanElement;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.os.Handler;

public class GameServer implements IGameServer {

	private HashMap<String, BombermanPlayer> players;

	private Level level;
	private IGameClient gameClientProxy;

	private HashMap<String, BombElement> bombsToDraw;

	private HashMap<String, BombermanElement> bombermans;

	private Handler refreshHandler = new Handler();
	private Runnable refreshRunnable;
	private Integer remainingTime;

	private Context context;

	public GameServer(Context context, Level level) {
		super();
		this.context = context;
		this.level = level;
		this.remainingTime = level.getGameDuration();

		this.players = new HashMap<String, BombermanPlayer>();
		this.bombsToDraw = new HashMap<String, BombElement>();
		this.bombermans = new HashMap<String, BombermanElement>();

		this.refreshRunnable = new Runnable() {
			@Override
			public void run() {
				if (gameClientProxy != null) {
					updateScreen();
				}

				refreshHandler.postDelayed(refreshRunnable, 100);
			}
		};
		this.refreshRunnable.run();
	}

	public void initClient() {
		ArrayList<ArrayList<Element>> elements = this.level.getMap();

		ArrayList<Drawing> drawings = new ArrayList<Drawing>();
		for (List<Element> line : elements) {
			for (Element element : line) {
				Drawing drawing = DrawingFactory.create(context, element);
				drawings.add(drawing);
			}
		}

		gameClientProxy.init(elements.size(), elements.get(0).size(), drawings);
	}

	public void setGameClient(IGameClient gameClientProxy) {
		this.gameClientProxy = gameClientProxy;
	}

	public void putBomberman(String username) {
		this.players.put(username, new BombermanPlayer(username));

		BombermanElement element = this.level.putBomberman();
		bombermans.put(username, element);
	}

	public void putBomb(String username) {
		BombermanElement bomberman = bombermans.get(username);
		BombElement element = this.level.putBomb(bomberman);
		bombsToDraw.put(username, element);
	}

	public void move(String username, Direction dir) {
		BombermanElement bomberman = bombermans.get(username);
		Position bombPos = bomberman.getPos();

		level.move(bomberman, dir);

		BombElement bomb = bombsToDraw.get(username);
		if (bomb != null) {
			this.level.getMap().get(bombPos.y).set(bombPos.x, bomb);
			bombsToDraw.put(username, null);
		}
	}

	@Override
	public void pause(String username) {
		// TODO Auto-generated method stub
	}

	@Override
	public void quit(String username) {
		// TODO Auto-generated method stub
	}

	private void updateScreen() {
		ArrayList<Drawing> drawings = new ArrayList<Drawing>();

		for (List<Element> line : this.level.getMap()) {
			for (Element element : line) {
				Drawing drawing = DrawingFactory.create(context, element);
				drawings.add(drawing);
			}
		}

		this.gameClientProxy.updateScreen(drawings);
	}

	public void decrementTime() {
		this.remainingTime--;

		for (BombermanPlayer player : this.players.values()) {
			player.setTime(this.remainingTime);
			player.setPlayers(players.size());
		}
	}

}
