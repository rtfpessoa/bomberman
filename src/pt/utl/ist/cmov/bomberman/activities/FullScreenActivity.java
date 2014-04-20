package pt.utl.ist.cmov.bomberman.activities;

import pt.utl.ist.cmov.bomberman.controllers.FullScreenController;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FullScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		View decorView = window.getDecorView();
		decorView
				.setOnSystemUiVisibilityChangeListener(new FullScreenController(
						decorView));
	}
}
