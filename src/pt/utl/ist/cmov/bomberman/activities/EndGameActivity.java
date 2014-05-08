package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import android.os.Bundle;
import android.widget.TextView;

public class EndGameActivity extends FullScreenActivity {

	public static final String INTENT_WINNER = ".pt.utl.ist.cmov.bomberman.activities.EndGameAcitivty.WINNER";

	public static final String INTENT_WINNER_POINTS = ".pt.utl.ist.cmov.bomberman.activities.EndGameAcitivty.WINNER_POINTS";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_end_game);

		String winner = getIntent().getExtras().getString(
				EndGameActivity.INTENT_WINNER);

		String winnerPoints = getIntent().getExtras().getString(
				EndGameActivity.INTENT_WINNER_POINTS);

		TextView winnerTextView = (TextView) findViewById(R.id.game_end_message);
		winnerTextView.setText("Congratulations, " + winner + " won :D");

		TextView pointsTextView = (TextView) findViewById(R.id.game_end_points);
		pointsTextView.setText(winnerPoints + " points");
	}
}
