package pt.utl.ist.cmov.bomberman.util;

import android.util.Log;

public class MapMeasurements {

	public static Integer SIDE_PADDING;
	public static Integer UP_PADDING;
	public static Integer POSITION_HEIGHT;
	public static Integer POSITION_WIDTH;

	public static void updateMapMeasurements(int screenWidth, int screenHeight,
			int mapWidth, int mapHeight) {
		float positionHeight = (float) ((screenHeight / mapHeight) * (7.0 / 10.0));
		POSITION_HEIGHT = (int) positionHeight;
		POSITION_WIDTH = screenWidth / mapWidth;
		SIDE_PADDING = POSITION_WIDTH / 2;
		UP_PADDING = POSITION_HEIGHT / 2;
		
		Log.d("MapMeasurements", "screenWidth: " + screenWidth);
		Log.d("MapMeasurements", "screenHeigth: " + screenHeight);
		Log.d("MapMeasurements", "SIDE_PADDING: " + SIDE_PADDING);
		Log.d("MapMeasurements", "UP_PADDING: " + UP_PADDING);
		Log.d("MapMeasurements", "POSITION_HEIGHT: " + POSITION_HEIGHT);
		Log.d("MapMeasurements", "POSITION_WIDTH: " + POSITION_WIDTH);
	}

}
