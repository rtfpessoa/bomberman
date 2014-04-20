package pt.utl.ist.cmov.bomberman.controllers;

import android.os.Build;
import android.os.Handler;
import android.view.View;

public class FullScreenController implements
		View.OnSystemUiVisibilityChangeListener {

	private final View rootView;

	public FullScreenController(View rootView) {
		this.rootView = rootView;
		setImmersiveMode(true);
	}

	@Override
	public void onSystemUiVisibilityChange(int visibility) {
		if (visibility == 0) {
			mHideHandler.postDelayed(mHideRunnable, 2000);
		}
	}

	private void setImmersiveMode(boolean enabled) {
		int flags = 0;
		final int version = Build.VERSION.SDK_INT;

		if (version >= Build.VERSION_CODES.KITKAT) {
			flags = View.SYSTEM_UI_FLAG_IMMERSIVE
					| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		} else if (version >= Build.VERSION_CODES.JELLY_BEAN) {
			flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
					| View.SYSTEM_UI_FLAG_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		} else if (version >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
		}

		if (version >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			rootView.setSystemUiVisibility(flags);
		}
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			setImmersiveMode(true);
		}
	};

}
