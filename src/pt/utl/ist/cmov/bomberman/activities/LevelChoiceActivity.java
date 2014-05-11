package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.MainActivity;
import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LevelChoiceActivity extends FullScreenActivity {

	public static final String LEVEL_MESSAGE = "pt.utl.ist.cmov.bomberman.activities.LEVEL";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level_choice);

		final AssetManager assetManager = getApplicationContext().getAssets();

		String[] levels = LevelManager.listLevels(assetManager);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.listview_line, levels);
		ListView listView = (ListView) findViewById(R.id.list_levels);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(listItemClickHandler);
	}

	private AdapterView.OnItemClickListener listItemClickHandler = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ListView list = (ListView) parent;
			levelChoosenAction(list.getItemAtPosition(position));
		}
	};

	private void levelChoosenAction(Object object) {
		String levelName = (String) object;

		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(LEVEL_MESSAGE, levelName);
		String username = getIntent().getExtras().getString(
				MainActivity.INTENT_USERNAME);
		intent.putExtra(MainActivity.INTENT_USERNAME, username);
		startActivity(intent);
	}
}
