package pt.utl.ist.cmov.bomberman.controllers;

import android.os.Build;
import android.os.Handler;
import android.view.View;

public class FullScreenController implements
		View.OnSystemUiVisibilityChangeListener {

	private final View rootView;
	final int androidVersion = Build.VERSION.SDK_INT;

	public FullScreenController(View rootView) {
		this.rootView = rootView;
		hideSystemUI();
	}

	@Override
	public void onSystemUiVisibilityChange(int visibility) {
		if (androidVersion < Build.VERSION_CODES.KITKAT) {
			mHideHandler.postDelayed(mHideRunnable, 5000);
		}
	}

	private void hideSystemUI() {
		int flags = 0;

		if (androidVersion >= Build.VERSION_CODES.KITKAT) {
			flags = View.SYSTEM_UI_FLAG_IMMERSIVE
					| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LOW_PROFILE;
		} else if (androidVersion >= Build.VERSION_CODES.JELLY_BEAN) {
			flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LOW_PROFILE;
		} else if (androidVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			flags = View.SYSTEM_UI_FLAG_LOW_PROFILE;
		}

		if (androidVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			rootView.setSystemUiVisibility(flags);
		}
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			hideSystemUI();
		}
	};

}
