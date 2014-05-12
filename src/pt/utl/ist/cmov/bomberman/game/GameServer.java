package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.drawing.Drawing;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTOFactory;
import pt.utl.ist.cmov.bomberman.game.model.BombModel;
import pt.utl.ist.cmov.bomberman.game.model.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.model.Model;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.Handler;

public class GameServer implements IGameServer {

	private HashMap<String, BombermanPlayer> players;

	private Level level;
	private IGameClient gameClientProxy;

	private HashMap<String, BombModel> bombsToDraw;

	private HashMap<String, BombermanModel> bombermans;

	private Handler refreshHandler;
	private Runnable refreshRunnable;
	private Integer remainingTime;

	public GameServer(Level level) {
		super();
		this.level = level;
		this.remainingTime = level.getGameDuration();

		this.players = new HashMap<String, BombermanPlayer>();
		this.bombsToDraw = new HashMap<String, BombModel>();
		this.bombermans = new HashMap<String, BombermanModel>();

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
		ArrayList<ArrayList<Model>> models = this.level.getMap();

		ArrayList<ModelDTO> dtos = new ArrayList<ModelDTO>();
		for (List<Model> line : models) {
			for (Model model : line) {
				ModelDTO dto = ModelDTOFactory.create(model);
				dtos.add(dto);
			}
		}

		gameClientProxy.init(models.size(), models.get(0).size(), dtos);
	}

	public void setGameClient(IGameClient gameClientProxy) {
		this.gameClientProxy = gameClientProxy;
	}

	public void putBomberman(String username) {
		this.players.put(username, new BombermanPlayer(username));

		BombermanModel model = this.level.putBomberman();
		bombermans.put(username, model);
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
		// TODO Auto-generated method stub
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
