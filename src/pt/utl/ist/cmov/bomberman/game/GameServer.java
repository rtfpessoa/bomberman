package pt.utl.ist.cmov.bomberman.game;

import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.elements.BombElement;
import pt.utl.ist.cmov.bomberman.game.elements.BombermanElement;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.os.Handler;

public class GameServer {

	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;
	private Integer remainingTime;

	private Level level;
	// protected CommunicationChannel commChannel;

	private HashMap<String, BombElement> bombsToDraw;

	private HashMap<String, BombermanElement> bombermans;

	public GameServer(Level level/* , CommunicationChannel commChannel */) {
		super();
		this.level = level;
		// this.commChannel = commChannel;
		this.remainingTime = level.getGameDuration();

		this.bombsToDraw = new HashMap<String, BombElement>();
		this.bombermans = new HashMap<String, BombermanElement>();

		timerRunnable = new Runnable() {
			@Override
			public void run() {
				remainingTime--;
				timerHandler.postDelayed(timerRunnable, 1000);
			}
		};
		timerRunnable.run();
	}

	public void putBomberman(String username) {
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

}
