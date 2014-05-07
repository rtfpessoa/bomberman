package pt.utl.ist.cmov.bomberman;

import pt.utl.ist.cmov.bomberman.activities.FullScreenActivity;
import pt.utl.ist.cmov.bomberman.activities.GameDiscoveryActivity;
import pt.utl.ist.cmov.bomberman.activities.LevelChoiceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends FullScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}

	public void newGameAction(View view) {
		Intent intent = new Intent(this, LevelChoiceActivity.class);
		startActivity(intent);
	}

	public void multiplayerAction(View view) {
		Intent intent = new Intent(this, GameDiscoveryActivity.class);
		startActivity(intent);
	}
}
