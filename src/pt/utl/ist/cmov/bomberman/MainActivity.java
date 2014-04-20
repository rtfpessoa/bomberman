package pt.utl.ist.cmov.bomberman;

import pt.utl.ist.cmov.bomberman.activities.FullScreenActivity;
import android.os.Bundle;

public class MainActivity extends FullScreenActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
	}
}
