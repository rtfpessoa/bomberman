package pt.utl.ist.cmov.bomberman.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

	private static ArrayList<ArrayList<Character>> readMap(Scanner levelInfo) {
		ArrayList<ArrayList<Character>> map = new ArrayList<ArrayList<Character>>();

		while (levelInfo.hasNextLine()) {
			String line = levelInfo.nextLine();

			ArrayList<Character> mapLine = new ArrayList<Character>();

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
			Integer explosionRange = levelInfo.nextInt();
			Integer robotSpeed = levelInfo.nextInt();
			Integer pointsRobot = levelInfo.nextInt();
			Integer pointsOpponent = levelInfo.nextInt();
			GameMap map = new GameMap(readMap(levelInfo));
			levelInfo.close();
			return new Level(gameDuration, explosionTimeout, explosionRange,
					robotSpeed, pointsRobot, pointsOpponent, map);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
