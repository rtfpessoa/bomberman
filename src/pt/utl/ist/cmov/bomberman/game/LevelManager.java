package pt.utl.ist.cmov.bomberman.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.content.Context;
import android.content.res.AssetManager;

public class LevelManager {

	private static final String levelsPath = "levels";

	public static String[] listLevels(AssetManager assetManager) {
		try {
			return assetManager.list(levelsPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static List<List<Character>> readMap(Scanner levelInfo,
			Map<Integer, Position> bombermans) {
		List<List<Character>> map = new ArrayList<List<Character>>();

		int y = 0;
		while (levelInfo.hasNextLine()) {
			String line = levelInfo.nextLine();

			List<Character> mapLine = new ArrayList<Character>();

			for (int x = 0; x < line.length(); x++) {
				if (line.charAt(x) != GameMap.WALL
						&& line.charAt(x) != GameMap.EMPTY
						&& line.charAt(x) != GameMap.OBSTACLE
						&& line.charAt(x) != GameMap.ROBOT) {
					bombermans.put(Character.getNumericValue(line.charAt(x)),
							new Position(x, y));
					mapLine.add(GameMap.EMPTY);
				} else
					mapLine.add(line.charAt(x));
			}

			map.add(mapLine);
			y++;
		}

		return map;
	}

	public static Level loadLevel(Context ctx, AssetManager assetManager, String levelName) {
		try {
			Scanner levelInfo = new Scanner(assetManager.open(levelsPath + "/"
					+ levelName));
			Integer gameDuration = levelInfo.nextInt();
			Integer explosionTimeout = levelInfo.nextInt();
			Integer explosionDuration = levelInfo.nextInt();
			Integer explosionRange = levelInfo.nextInt();
			Integer robotSpeed = levelInfo.nextInt();
			Integer pointsRobot = levelInfo.nextInt();
			Integer pointsOpponent = levelInfo.nextInt();
			levelInfo.nextLine();
			Map<Integer, Position> bombermans = new HashMap<Integer, Position>();
			GameMap map = new GameMap(ctx, readMap(levelInfo, bombermans));
			levelInfo.close();
			return new Level(gameDuration, explosionTimeout, explosionDuration,
					explosionRange, robotSpeed, pointsRobot, pointsOpponent,
					bombermans, map);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
