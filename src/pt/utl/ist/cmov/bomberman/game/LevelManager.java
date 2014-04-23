package pt.utl.ist.cmov.bomberman.game;

import java.io.IOException;
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
			String map = levelInfo.next();
			levelInfo.close();
			return new Level(gameDuration, explosionTimeout, explosionRange,
					robotSpeed, pointsRobot, pointsOpponent, map);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

}
