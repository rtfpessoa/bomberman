package pt.utl.ist.cmov.bomberman;

import java.util.Random;

import pt.utl.ist.cmov.bomberman.activities.FullScreenActivity;
import pt.utl.ist.cmov.bomberman.activities.GameDiscoveryActivity;
import pt.utl.ist.cmov.bomberman.activities.LevelChoiceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends FullScreenActivity {

	public static final String INTENT_USERNAME = "pt.utl.ist.cmov.bomberman.MainAcitivty.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		TextView usernameField = (TextView) findViewById(R.id.username);
		usernameField.setText(generateUsername());
	}

	public void newGameAction(View view) {
		Intent intent = new Intent(this, LevelChoiceActivity.class);
		TextView usernameField = (TextView) findViewById(R.id.username);
		String username = usernameField.getText().toString();
		intent.putExtra(MainActivity.INTENT_USERNAME, username);
		finish();
		startActivity(intent);
	}

	public void multiplayerAction(View view) {
		Intent intent = new Intent(this, GameDiscoveryActivity.class);
		TextView usernameField = (TextView) findViewById(R.id.username);
		String username = usernameField.getText().toString();
		intent.putExtra(MainActivity.INTENT_USERNAME, username);
		finish();
		startActivity(intent);
	}

	private String generateUsername() {
		Random rand = new Random();
		Integer min = 1, max = 99;
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return "Player" + randomNum;
	}
}
