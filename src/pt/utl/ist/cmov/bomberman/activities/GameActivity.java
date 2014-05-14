package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.listener.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.network.proxy.ClientCommunicationProxy;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public abstract class GameActivity extends WifiDirectActivity implements
		PeerListListener, ConnectionInfoListener {

	public static final String CURRENT_DEVICES = "pt.utl.ist.cmov.bomberman.CURRENT_DEVICES";
	public static final String MODELS = "pt.utl.ist.cmov.bomberman.MODELS";
	public static final String WIDTH = "pt.utl.ist.cmov.bomberman.WIDTH";
	public static final String HEIGHT = "pt.utl.ist.cmov.bomberman.HEIGHT";
	public static final String PLAYERS = "pt.utl.ist.cmov.bomberman.PLAYERS";

	protected MainGamePanel gamePanel;
	protected GameClient gameClient;
	protected ClientCommunicationProxy clientManager;

	private DirectionButtonListener upListener;
	private DirectionButtonListener downListener;
	private DirectionButtonListener leftListener;
	private DirectionButtonListener rightListener;

	private Handler updateScreenHandler;
	private Runnable updateScreenRunnable;

	private TextView timeLeft;
	private TextView playerName;
	private TextView score;
	private TextView playerNumber;

	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		username = getIntent().getExtras().getString(
				MainActivity.INTENT_USERNAME);

		this.gameClient = new GameClient(this, username);

		this.clientManager = new ClientCommunicationProxy(this.gameClient);

		this.gameClient.setGameServer(clientManager);

		this.gamePanel.setGameClient(this.gameClient);

		this.updateScreenHandler = new Handler();

		upListener = new DirectionButtonListener(Direction.UP, gameClient);
		this.findViewById(R.id.button_up).setOnTouchListener(upListener);

		downListener = new DirectionButtonListener(Direction.DOWN, gameClient);
		this.findViewById(R.id.button_down).setOnTouchListener(downListener);

		leftListener = new DirectionButtonListener(Direction.LEFT, gameClient);
		this.findViewById(R.id.button_left).setOnTouchListener(leftListener);

		rightListener = new DirectionButtonListener(Direction.RIGHT, gameClient);
		this.findViewById(R.id.button_right).setOnTouchListener(rightListener);

		timeLeft = (TextView) this.findViewById(R.id.time_left);
		playerName = (TextView) this.findViewById(R.id.player_name);
		score = (TextView) this.findViewById(R.id.player_score);
		playerNumber = (TextView) this.findViewById(R.id.player_number);

		this.updateScreenRunnable = new Runnable() {
			@Override
			public void run() {
				update(gameClient.getPlayer());

				updateScreenHandler.postDelayed(updateScreenRunnable, 1000);
			}
		};
		this.updateScreenRunnable.run();
	}

	public void bombClick(View view) {
		gameClient.putBomb();
	}

	public void pauseClick(View view) {
		gameClient.pause();
	}

	public void quitClick(View view) {
		gameClient.quit();
	}

	public void endGame() {
		Intent intent = new Intent(this, EndGameActivity.class);

		// TODO: get winner and points
		intent.putExtra(EndGameActivity.INTENT_WINNER, "rtfpessoa");
		intent.putExtra(EndGameActivity.INTENT_WINNER_POINTS, "69");
		finish();
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		gamePanel.stopAll();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopAll();
	}

	@Override
	public void onBackPressed() {
		gameClient.pause();
	}

	private void update(BombermanPlayer player) {
		if (player == null) {
			return;
		}
		String timeString = player.getTime().toString() + " s";
		timeLeft.setText(timeString);

		String usernameString = player.getUsername();
		playerName.setText(usernameString);

		String scoreString = player.getScore() + " pts";
		score.setText(scoreString);

		String playerString = player.getPlayers() + " players";
		playerNumber.setText(playerString);
	}

	protected void stopAll() {
		this.updateScreenHandler.removeCallbacks(this.updateScreenRunnable);
		this.gamePanel.stopAll();
		this.clientManager.close();
		this.upListener.stopAll();
		this.downListener.stopAll();
		this.leftListener.stopAll();
		this.rightListener.stopAll();
	}

	public void startNewServer(String username, String levelName,
			Integer width, Integer height, ArrayList<ModelDTO> models,
			ArrayList<WifiP2pDevice> devices) {
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
	}

	public void disconnect() {
		this.wifiDirectController.removeGroup();
	}
}
