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

		Log.d("MapMeasurements", "screenWidth: " + viewWidth);
		Log.d("MapMeasurements", "screenHeigth: " + viewHeight);
		Log.d("MapMeasurements", "SIDE_PADDING: " + SIDE_PADDING);
		Log.d("MapMeasurements", "UP_PADDING: " + UP_PADDING);
		Log.d("MapMeasurements", "POSITION_HEIGHT: " + POSITION_HEIGHT);
		Log.d("MapMeasurements", "POSITION_WIDTH: " + POSITION_WIDTH);
	}

	public static Position calculateNextPosition(Direction dir, Position pos) {
		switch (dir) {
		case UP:
			return new Position(pos.x, pos.y - 1);
		case DOWN:
			return new Position(pos.x, pos.y + 1);
		case LEFT:
			return new Position(pos.x - 1, pos.y);
		case RIGHT:
			return new Position(pos.x + 1, pos.y);
		}

		return null;
	}

}
