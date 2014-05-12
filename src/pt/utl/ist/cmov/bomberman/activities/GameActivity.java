package pt.utl.ist.cmov.bomberman.activities;

import java.io.IOException;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.handlers.ServerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.channels.FakeCommunicationChannel;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.handlers.managers.ServerCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
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

public class GameActivity extends WifiDirectActivity implements
		PeerListListener, ConnectionInfoListener {

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

		Level level = LevelManager.loadLevel(context, context.getAssets(),
				levelName);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		this.gameServer = new GameServer(level);
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
				gameServer.decrementTime();
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
	public void onBackPressed()
	{
	    gameClient.pause();
	}

	private void update(BombermanPlayer player) {
		String timeString = player.getTime().toString() + " s";
		timeLeft.setText(timeString);

		String usernameString = player.getUsername();
		playerName.setText(usernameString);

		String scoreString = player.getScore() + " pts";
		score.setText(scoreString);

		String playerString = player.getPlayers() + " players";
		playerNumber.setText(playerString);
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
		// INFO: this is not needed
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
