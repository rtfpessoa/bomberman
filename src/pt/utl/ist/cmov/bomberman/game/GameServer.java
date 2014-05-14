package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.activities.ServerActivity;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.game.model.BombModel;
import pt.utl.ist.cmov.bomberman.game.model.BombermanModel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.Handler;

public class GameServer implements IGameServer {

	private ServerActivity activity;

	private HashMap<String, BombermanPlayer> players;

	private Level level;
	private IGameClient gameClientProxy;

	private HashMap<String, BombModel> bombsToDraw;

	private HashMap<String, BombermanModel> bombermans;

	private Handler refreshHandler;
	private Runnable refreshRunnable;
	private Integer remainingTime;

	public GameServer(ServerActivity activity, Level level,
			ArrayList<BombermanPlayer> initialPlayers) {
		super();
		this.activity = activity;
		this.level = level;
		this.remainingTime = level.getGameDuration();

		this.players = new HashMap<String, BombermanPlayer>();
		this.bombsToDraw = new HashMap<String, BombModel>();
		this.bombermans = new HashMap<String, BombermanModel>();

		if (initialPlayers != null) {
			HashMap<Integer, BombermanModel> bombermanModels = this.level
					.getBombermanModels();
			for (BombermanPlayer player : initialPlayers) {
				this.players.put(player.getUsername(), player);
				this.bombermans.put(player.getUsername(),
						bombermanModels.get(player.getId()));
			}
		}

		this.refreshHandler = new Handler();
		this.refreshRunnable = new Runnable() {
			@Override
			public void run() {
				if (gameClientProxy != null) {
					updateScreen();
				}

				refreshHandler.postDelayed(refreshRunnable, 200);
			}
		};
		this.refreshRunnable.run();
	}

	public void initClient() {
		gameClientProxy.init(this.level.getHeight(), this.level.getWidth(),
				this.level.getMapDTO());
	}

	public void setGameClient(IGameClient gameClientProxy) {
		this.gameClientProxy = gameClientProxy;
	}

	public void putBomberman(String username) {
		if (!players.containsKey(username)) {
			BombermanModel model = this.level.putBomberman();
			bombermans.put(username, model);
			players.put(username, new BombermanPlayer(model.getBombermanId(),
					username));
		}

		/* On new player update devices list */
		this.activity.updateDevices();
	}

	public void putBomb(String username) {
		BombermanModel bomberman = bombermans.get(username);

		if (bomberman.isPaused()) {
			return;
		}

		BombModel model = this.level.createBomb(bomberman);
		bombsToDraw.put(username, model);
	}

	public void move(String username, Direction dir) {
		BombermanModel bomberman = bombermans.get(username);

		if (bomberman.isPaused()) {
			return;
		}

		level.move(bomberman, dir);

		if (bombsToDraw.containsKey(username)) {
			BombModel bomb = bombsToDraw.get(username);
			this.level.putBomb(bomb);
			bombsToDraw.remove(username);
		}
	}

	@Override
	public void pause(String username) {
		BombermanModel bomberman = bombermans.get(username);
		bomberman.pause();
	}

	@Override
	public void quit(String username) {
		players.remove(username);
		bombermans.remove(username);

		if (bombermans.size() > 0 && activity.getUsername().equals(username)) {
			// pauseGame
			for (BombermanModel b : bombermans.values()) {
				b.pause();
			}
			this.stopAll();
			// chooseNextServer
			BombermanPlayer newServer = players.values().iterator().next();
			// startServer
			this.updateScreen();
			this.gameClientProxy.updatePlayers(players);
			this.gameClientProxy.startServer(newServer.getUsername(),
					this.level.getLevelName(), this.level.getWidth(),
					this.level.getHeight(), this.level.getMapDTO(),
					this.activity.getDevices());

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			activity.finish();
		} else if (bombermans.size() == 0) {
			activity.finish();
		}
	}

	private void updateScreen() {
		ArrayList<ModelDTO> changedModels = level.getLatestUpdates();
		this.gameClientProxy.updateScreen(changedModels);
	}

	public void decrementTime() {
		this.remainingTime--;

		for (BombermanPlayer player : this.players.values()) {
			player.setTime(this.remainingTime);
			player.setPlayers(players.size());
		}

		this.gameClientProxy.updatePlayers(players);
	}

	public void stopAll() {
		refreshHandler.removeCallbacks(this.refreshRunnable);
		level.stopAll();
	}

}
