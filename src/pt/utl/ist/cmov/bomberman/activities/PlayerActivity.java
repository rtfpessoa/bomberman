package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
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

	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;

	private TextView timeLeft;
	private TextView playerName;
	private TextView score;
	private TextView playerNumber;

	private Boolean hasStartedServer = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		// TODO: replace username
		this.gameClient = new GameClient("USERNAME2");

		this.clientManager = new ClientCommunicationManager(this.gameClient);

		this.gameClient.setGameServer((IGameServer) clientManager);

		this.gamePanel.setGameClient(this.gameClient);

		this.wifiP2pGroupOwner = getIntent().getExtras().getParcelable(
				GameDiscoveryActivity.DEVICE_MESSAGE);
		this.wifiDirectController.connect(wifiP2pGroupOwner);

		this.findViewById(R.id.button_up).setOnTouchListener(
				new DirectionButtonListener(Direction.UP, gameClient));

		this.findViewById(R.id.button_down).setOnTouchListener(
				new DirectionButtonListener(Direction.DOWN, gameClient));

		this.findViewById(R.id.button_left).setOnTouchListener(
				new DirectionButtonListener(Direction.LEFT, gameClient));

		this.findViewById(R.id.button_right).setOnTouchListener(
				new DirectionButtonListener(Direction.RIGHT, gameClient));

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

		if (!hasStartedServer && !p2pInfo.isGroupOwner) {
			Log.i("BOMBERMAN", "Connected as peer");
			handler = new PlayerSocketHandler(this.clientManager,
					p2pInfo.groupOwnerAddress);
			handler.start();
			hasStartedServer = true;
		} else {
			Log.e("BOMBERMAN", "This device should not be the groupd owner!");
		}
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
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		// INFO: this is not needed
	}
}
