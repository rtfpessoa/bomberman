package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.handlers.PlayerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PlayerActivity extends WifiDirectActivity implements
		PeerListListener, ConnectionInfoListener {

	private MainGamePanel gamePanel;
	private GameClient gameClient;
	private ClientCommunicationManager clientManager;

	private DirectionButtonListener upListener;
	private DirectionButtonListener downListener;
	private DirectionButtonListener leftListener;
	private DirectionButtonListener rightListener;

	private Handler timerHandler;
	private Runnable timerRunnable;

	private TextView timeLeft;
	private TextView playerName;
	private TextView score;
	private TextView playerNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		this.timerHandler = new Handler();

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		String username = getIntent().getExtras().getString(
				MainActivity.INTENT_USERNAME);

		this.gameClient = new GameClient(this, username);

		this.clientManager = new ClientCommunicationManager(this.gameClient);

		this.gameClient.setGameServer((IGameServer) clientManager);

		this.gamePanel.setGameClient(this.gameClient);

		this.wifiP2pGroupOwner = getIntent().getExtras().getParcelable(
				GameDiscoveryActivity.DEVICE_MESSAGE);
		this.wifiDirectController.connect(wifiP2pGroupOwner);

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

		this.timerRunnable = new Runnable() {
			@Override
			public void run() {
				update(gameClient.getPlayer());

				timerHandler.postDelayed(timerRunnable, 1000);
			}
		};
		this.timerRunnable.run();
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

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (!p2pInfo.isGroupOwner) {
			Log.i("BOMBERMAN", "Connected as peer");
			handler = new PlayerSocketHandler(this.clientManager,
					p2pInfo.groupOwnerAddress);
			handler.start();
		} else {
			Log.e("BOMBERMAN", "This device should not be the groupd owner!");
		}
	}

	private void update(BombermanPlayer player) {
		if (player != null) {
			String timeString = player.getTime().toString() + " s";
			timeLeft.setText(timeString);

			String usernameString = player.getUsername();
			playerName.setText(usernameString);

			String scoreString = player.getScore() + " pts";
			score.setText(scoreString);

			String playerString = player.getPlayers() + " players";
			playerNumber.setText(playerString);
		}
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// INFO: this is not needed
	}

	public void startNewServer(ArrayList<ModelDTO> models) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GameActivity.CONNECT_TO_ALL, true);
		intent.putExtra(GameActivity.PREVIOUS_SERVER, this.wifiP2pGroupOwner);
		intent.putExtra(GameActivity.MODELS, models);
		finish();
		startActivity(intent);
	}

	private void stopAll() {
		this.timerHandler.removeCallbacks(this.timerRunnable);
		this.gamePanel.stopAll();
		this.clientManager.close();
		this.upListener.stopAll();
		this.downListener.stopAll();
		this.leftListener.stopAll();
		this.rightListener.stopAll();
	}
}
