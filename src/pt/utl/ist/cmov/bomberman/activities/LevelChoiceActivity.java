package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.R;
import pt.utl.ist.cmov.bomberman.game.LevelManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LevelChoiceActivity extends FullScreenActivity {

	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_level_choice);

		context = getApplicationContext();

		final AssetManager assetManager = context.getAssets();

		String[] levels = LevelManager.listLevels(assetManager);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, levels);
		ListView listView = (ListView) findViewById(R.id.list_levels);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(mMessageClickedHandler);
	}

	private static OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Toast.makeText(context, "CLICKED[" + position + "]",
					Toast.LENGTH_SHORT).show();
		}
	};
}
