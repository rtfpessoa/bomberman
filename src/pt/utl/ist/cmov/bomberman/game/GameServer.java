package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.elements.BombElement;
import pt.utl.ist.cmov.bomberman.game.elements.BombermanElement;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.util.Direction;
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

	public GameServer(Level level) {
		super();
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
		gameClientProxy.init(this.level.getMap());
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

		level.move(bomberman, dir);
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
		ArrayList<Element> elements = new ArrayList<Element>();

		for (List<Element> line : this.level.getMap()) {
			for (Element element : line) {
				elements.add(element);
			}
		}

		this.gameClientProxy.updateScreen(elements);
	}

	public void decrementTime() {
		this.remainingTime--;

		for (BombermanPlayer player : this.players.values()) {
			player.setTime(this.remainingTime);
			player.setPlayers(players.size());
		}
	}

}
