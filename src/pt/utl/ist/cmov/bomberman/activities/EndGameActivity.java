package pt.utl.ist.cmov.bomberman.activities;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EndGameActivity extends FullScreenActivity {

	public static final String INTENT_PLAYERS = ".pt.utl.ist.cmov.bomberman.activities.EndGameAcitivty.WINNER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_end_game);

		ArrayList<BombermanPlayer> players = getIntent().getExtras()
				.getParcelableArrayList(EndGameActivity.INTENT_PLAYERS);

		ArrayList<String> classificationTable = createClassification(players);

		TextView winnerTextView = (TextView) findViewById(R.id.game_end_message);
		winnerTextView.setText("Congratulations, "
				+ players.get(0).getUsername() + " won :D");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.player_table_line, classificationTable);
		ListView listView = (ListView) findViewById(R.id.players_table);

		listView.setAdapter(adapter);
	}

	private ArrayList<String> createClassification(
			ArrayList<BombermanPlayer> players) {
		ArrayList<String> classification = new ArrayList<String>();

		for (int i = 1; i <= players.size(); i++) {
			classification.add(i + " - " + players.get(i - 1).getUsername()
					+ " Score: " + players.get(i - 1).getScore());
		}
		return classification;
	}
}
