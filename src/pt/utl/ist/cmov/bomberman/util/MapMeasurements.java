package pt.utl.ist.cmov.bomberman.util;

import android.util.Log;

public class MapMeasurements {

	public static Integer SIDE_PADDING;
	public static Integer UP_PADDING;
	public static Integer POSITION_HEIGHT;
	public static Integer POSITION_WIDTH;

	private static int viewHeight = 0;
	private static int mapHeight = 0;
	private static int viewWidth = 0;
	private static int mapWidth = 0;

	private static boolean isReady = false;

	public static void updateViewSize(int viewWidth, int viewHeight) {
		MapMeasurements.viewWidth = viewWidth;
		MapMeasurements.viewHeight = viewHeight;
		doTheMath();
	}

	public static void updateMapSize(int mapWidth, int mapHeight) {
		MapMeasurements.mapWidth = mapWidth;
		MapMeasurements.mapHeight = mapHeight;
		doTheMath();
	}

	public static Boolean isReady() {
		return isReady;
	}

	private static void doTheMath() {
		if (viewHeight == 0 || mapHeight == 0 || viewWidth == 0
				|| mapWidth == 0) {
			return;
		}

		POSITION_HEIGHT = viewHeight / mapHeight;
		POSITION_WIDTH = viewWidth / mapWidth;
		SIDE_PADDING = POSITION_WIDTH / 2;
		UP_PADDING = POSITION_HEIGHT / 2;

		isReady = true;

		Log.i("MapMeasurements", "screenWidth: " + viewWidth);
		Log.i("MapMeasurements", "screenHeigth: " + viewHeight);
		Log.i("MapMeasurements", "SIDE_PADDING: " + SIDE_PADDING);
		Log.i("MapMeasurements", "UP_PADDING: " + UP_PADDING);
		Log.i("MapMeasurements", "POSITION_HEIGHT: " + POSITION_HEIGHT);
		Log.i("MapMeasurements", "POSITION_WIDTH: " + POSITION_WIDTH);
	}
}
