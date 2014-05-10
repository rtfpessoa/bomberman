package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.GameServer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import pt.utl.ist.cmov.bomberman.handlers.channels.FakeCommunicationChannel;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.handlers.managers.ServerCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends FullScreenActivity {

	private static Context context;

	private MainGamePanel gamePanel;
	private GameServer gameServer;
	private GameClient gameClient;

	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;

	private TextView timeLeft;
	private TextView playerName;
	private TextView score;
	private TextView playerNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		String levelName = getIntent().getExtras().getString(
				LevelChoiceActivity.LEVEL_MESSAGE);

		Level level = LevelManager.loadLevel(context, context.getAssets(),
				levelName);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		this.gameServer = new GameServer(level);
		// TODO: replace username
		this.gameClient = new GameClient("USERNAME", gamePanel);

		ServerCommunicationManager serverManager = new ServerCommunicationManager(
				this.gameServer);
		ClientCommunicationManager clientManager = new ClientCommunicationManager(
				this.gameClient);

		gameServer.setGameClient(serverManager);
		gameClient.setGameServer(clientManager);

		clientManager
				.setCommChannel(new FakeCommunicationChannel(serverManager));
		serverManager
				.addCommChannel(new FakeCommunicationChannel(clientManager));

		this.gamePanel.setGameClient(this.gameClient);

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
}
