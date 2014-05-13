package pt.utl.ist.cmov.bomberman.activities;

import java.io.IOException;
import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.handlers.ServerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.channels.FakeCommunicationChannel;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.handlers.managers.ServerCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends WifiDirectActivity implements
		PeerListListener, ConnectionInfoListener {

	public static final String CONNECT_TO_ALL = "pt.utl.ist.cmov.bomberman.CONNECT_TO_ALL";

	public static final String PREVIOUS_SERVER = "pt.utl.ist.cmov.bomberman.PREVIOUS_SERVER";

	public static final String MODELS = "pt.utl.ist.cmov.bomberman.MODELS";

	private static Context context;

	private MainGamePanel gamePanel;
	private GameServer gameServer;
	private GameClient gameClient;
	private ServerCommunicationManager serverManager;
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

	private Boolean hasStartedServer;

	private Boolean connectToAll;
	private WifiP2pDevice previousMaster;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		this.hasStartedServer = false;

		this.wifiDirectController.startGroup();

		String username = getIntent().getExtras().getString(
				MainActivity.INTENT_USERNAME);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		connectToAll = getIntent().getExtras().getBoolean(
				GameActivity.CONNECT_TO_ALL);
		previousMaster = getIntent().getExtras().getParcelable(
				GameActivity.PREVIOUS_SERVER);
		ArrayList<ModelDTO> models = getIntent().getExtras().getParcelable(
				GameActivity.MODELS);

		Level level;
		if (!connectToAll) {
			level = LevelManager.loadLevel(context, context.getAssets(),
					levelName);
		} else {
			level = LevelManager.loadLevel(context, context.getAssets(),
					levelName, null, null, models);
		}

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		this.gameServer = new GameServer(this, level);
		this.gameClient = new GameClient(username);

		this.serverManager = new ServerCommunicationManager(this.gameServer);
		this.clientManager = new ClientCommunicationManager(this.gameClient);

		gameServer.setGameClient(serverManager);
		gameClient.setGameServer(clientManager);

		clientManager
				.setCommChannel(new FakeCommunicationChannel(serverManager));
		serverManager
				.addCommChannel(new FakeCommunicationChannel(clientManager));

		this.gamePanel.setGameClient(this.gameClient);

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

		this.timerHandler = new Handler();
		this.timerRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					gameServer.decrementTime();
					update(gameClient.getPlayer());
				} catch (NullPointerException ignored) {
				}

				timerHandler.postDelayed(timerRunnable, 1000);
			}
		};
		this.timerRunnable.run();

		/* Connect to previous players if any, only after all setup */
		this.wifiDirectController.discoverPeers();
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
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (!hasStartedServer && p2pInfo.isGroupOwner) {
			Log.i("BOMBERMAN", "Connected as GroupOwner");
			try {
				handler = new ServerSocketHandler(this.serverManager);
				handler.start();
				hasStartedServer = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (!p2pInfo.isGroupOwner) {
			Log.e("BOMBERMAN", "This device must be the groupd owner!");
		} else {
			Log.i("BOMBERMAN", "This device already has a server!");
		}
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		if (connectToAll && !peers.getDeviceList().isEmpty()) {
			for (WifiP2pDevice device : peers.getDeviceList()) {
				if (previousMaster == null
						|| !previousMaster.deviceAddress
								.equals(device.deviceAddress)) {
					this.wifiDirectController.connect(device);
				}
			}

			connectToAll = false;
		}
	}

	public void updateDevices() {
		this.wifiDirectController.requestGroupInfo();
	}

	private void stopAll() {
		this.timerHandler.removeCallbacks(this.timerRunnable);
		this.gameServer.stopAll();
		this.gamePanel.stopAll();
		this.clientManager.close();
		this.serverManager.close();
		this.upListener.stopAll();
		this.downListener.stopAll();
		this.leftListener.stopAll();
		this.rightListener.stopAll();
	}
}
