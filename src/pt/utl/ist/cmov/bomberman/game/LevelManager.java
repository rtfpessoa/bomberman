package pt.utl.ist.cmov.bomberman.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.content.res.AssetManager;
import android.util.Log;

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

	private static List<List<Character>> readMap(Scanner levelInfo) {
		List<List<Character>> map = new ArrayList<List<Character>>();

		while (levelInfo.hasNextLine()) {
			String line = levelInfo.nextLine();

			List<Character> mapLine = new ArrayList<Character>();

			for (int i = 0; i < line.length(); i++) {
				mapLine.add(line.charAt(i));
			}

			map.add(mapLine);
		}

		return map;
	}

	public static Level loadLevel(AssetManager assetManager, String levelName) {
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
			GameMap map = new GameMap(readMap(levelInfo));
			levelInfo.close();
			return new Level(gameDuration, explosionTimeout, explosionDuration,
					explosionRange, robotSpeed, pointsRobot, pointsOpponent,
					map);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
