package pt.utl.ist.cmov.bomberman.util;

import android.util.Log;

public class MapMeasurements {

	public static Integer SIDE_PADDING;
	public static Integer UP_PADDING;
	public static Integer POSITION_HEIGHT;
	public static Integer POSITION_WIDTH;

	public static void updateMapMeasurements(int viewWidth, int viewHeight,
			int mapWidth, int mapHeight) {
		POSITION_HEIGHT = viewHeight / mapHeight;
		POSITION_WIDTH = viewWidth / mapWidth;
		SIDE_PADDING = POSITION_WIDTH / 2;
		UP_PADDING = POSITION_HEIGHT / 2;

		Log.i("MapMeasurements", "screenWidth: " + viewWidth);
		Log.i("MapMeasurements", "screenHeigth: " + viewHeight);
		Log.i("MapMeasurements", "SIDE_PADDING: " + SIDE_PADDING);
		Log.i("MapMeasurements", "UP_PADDING: " + UP_PADDING);
		Log.i("MapMeasurements", "POSITION_HEIGHT: " + POSITION_HEIGHT);
		Log.i("MapMeasurements", "POSITION_WIDTH: " + POSITION_WIDTH);
	}

}
