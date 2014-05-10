package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.controllers.WifiDirectController;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.handlers.PlayerSocketHandler;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PlayerActivity extends FullScreenActivity implements
		ConnectionInfoListener {

	private MainGamePanel gamePanel;
	private GameClient gameClient;
	private ClientCommunicationManager clientManager;

	private WifiP2pManager manager;
	private final IntentFilter intentFilter = new IntentFilter();
	private Channel channel;
	private WifiDirectController wifiDirectController;

	private WifiP2pDevice wifiP2pManagerDevice;

	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		this.wifiP2pManagerDevice = getIntent().getExtras().getParcelable(
				GameDiscoveryActivity.DEVICE_MESSAGE);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		// TODO: replace username
		this.gameClient = new GameClient("USERNAME", gamePanel);

		this.clientManager = new ClientCommunicationManager(this.gameClient);

		this.gameClient.setGameServer((IGameServer) clientManager);

		this.gamePanel.setGameClient(this.gameClient);

		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(this, getMainLooper(), null);

		this.wifiDirectController = new WifiDirectController(manager, channel,
				this);

		wifiDirectController.setupWifi();

		wifiDirectController.connect(wifiP2pManagerDevice);

		this.findViewById(R.id.button_up).setOnTouchListener(
				new DirectionButtonListener(Direction.UP, gameClient));

		this.findViewById(R.id.button_down).setOnTouchListener(
				new DirectionButtonListener(Direction.DOWN, gameClient));

		this.findViewById(R.id.button_left).setOnTouchListener(
				new DirectionButtonListener(Direction.LEFT, gameClient));

		this.findViewById(R.id.button_right).setOnTouchListener(
				new DirectionButtonListener(Direction.RIGHT, gameClient));

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

	public void endGame() {
		Intent intent = new Intent(this, EndGameActivity.class);

		// TODO: get winner and points
		intent.putExtra(EndGameActivity.INTENT_WINNER, "rtfpessoa");
		intent.putExtra(EndGameActivity.INTENT_WINNER_POINTS, "69");
		gamePanel.destroy();
		finish();
		startActivity(intent);
	}

	@Override
	public void onPause() {
		gamePanel.destroy();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		gamePanel.destroy();
		super.onDestroy();
	}

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo p2pInfo) {
		Thread handler = null;

		if (p2pInfo.isGroupOwner) {
			Log.e("BOMBERMAN", "This device should not be the groupd owner!");
		} else {
			Log.d("BOMBERMAN", "Connected as peer");
			handler = new PlayerSocketHandler(this.clientManager,
					p2pInfo.groupOwnerAddress);
			handler.start();
		}
	}

	private void update(BombermanPlayer player) {
		TextView timeLeft = (TextView) this.findViewById(R.id.time_left);
		timeLeft.setText(player.getTime().toString() + " s");

		TextView playerName = (TextView) this.findViewById(R.id.player_name);
		playerName.setText(player.getUsername());

		TextView score = (TextView) this.findViewById(R.id.player_score);
		score.setText(player.getScore());

		TextView playerNumber = (TextView) this
				.findViewById(R.id.player_number);
		playerNumber.setText(player.getScore());
	}
}
