package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.GameClient;
import pt.utl.ist.cmov.bomberman.game.IGameServer;
import pt.utl.ist.cmov.bomberman.handlers.managers.ClientCommunicationManager;
import pt.utl.ist.cmov.bomberman.listeners.DirectionButtonListener;
import pt.utl.ist.cmov.bomberman.util.Direction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PlayerActivity extends FullScreenActivity {

	private static final String TAG = PlayerActivity.class.getSimpleName();

	private static Context context;

	private MainGamePanel gamePanel;
	private GameClient gameClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();

		setContentView(R.layout.activity_game);

		this.gamePanel = (MainGamePanel) findViewById(R.id.game_panel);

		// TODO: replace username
		this.gameClient = new GameClient("USERNAME", gamePanel);

		ClientCommunicationManager clientManager = new ClientCommunicationManager(
				this.gameClient);

		this.gameClient.setGameServer((IGameServer) clientManager);

		this.findViewById(R.id.button_up).setOnTouchListener(
				new DirectionButtonListener(Direction.UP, gameClient));

		this.findViewById(R.id.button_down).setOnTouchListener(
				new DirectionButtonListener(Direction.DOWN, gameClient));

		this.findViewById(R.id.button_left).setOnTouchListener(
				new DirectionButtonListener(Direction.LEFT, gameClient));

		this.findViewById(R.id.button_right).setOnTouchListener(
				new DirectionButtonListener(Direction.RIGHT, gameClient));
	}

	public void bombClick(View view) {
		gameClient.putBomb();
	}

	public void updateTime(Integer remainingTime) {
		if (remainingTime <= 0) {
			endGame();
		}

		TextView timerTextView = (TextView) this.findViewById(R.id.time_left);
		timerTextView.setText(remainingTime.toString() + " s");
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
}
