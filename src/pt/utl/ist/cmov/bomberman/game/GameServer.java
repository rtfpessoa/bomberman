package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.activities.GameActivity;
import pt.utl.ist.cmov.bomberman.activities.views.MainGamePanel;
import pt.utl.ist.cmov.bomberman.game.models.RobotModel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.MapMeasurements;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;

public class GameServer extends Game {

	private GameActivity gameActivity;

	private Handler timerHandler = new Handler();
	private Runnable timerRunnable;
	private int remainingTime;

	public GameServer(GameActivity gameActivity, Level level,
			MainGamePanel gamePanel) {
		super(level, gamePanel, 1);
		this.remainingTime = level.getGameDuration();
		this.gameActivity = gameActivity;
	}

	private void bombermanKilled(Integer bombermanId) {
		// TODO falta implementar
		if (bombermanId == 1) {
			this.bombermanPos = null;
		}
	}

	@Override
	public void init() {
		timerRunnable = new Runnable() {
			@Override
			public void run() {
				gameActivity.updateTime(remainingTime--);
				timerHandler.postDelayed(timerRunnable, 1000);
			}
		};
		timerRunnable.run();

		new MoveRobots().run();
	}

	@Override
	public void moveBomberman(Direction dir) {
		if (this.bombermanPos != null && this.canMove(dir, bombermanPos)) {
			Position newPos = MapMeasurements.calculateNextPosition(dir,
					bombermanPos);

			if (this.level.getMap().getContent(newPos) == GameMap.EXPLODING) {
				this.level.getMap().putEmpty(this.bombermanPos);
				this.bombermanKilled(1);
				return;
			}

			this.level.getMap().move(this.bombermanPos, newPos);
			if (bombToDraw) {
				this.level.getMap().putBomb(this.bombermanPos);
				this.bombToDraw = false;
			}
			this.bombermanPos = newPos;
		}
	}

	@Override
	public void moveRobots() {
		List<Position> positions = new ArrayList<Position>();

		for (int y = 0; y < this.level.getMap().getHeight(); y++) {
			for (int x = 0; x < this.level.getMap().getWidth(); x++) {
				this.level.getMap();
				if (this.level.getMap().getContent(new Position(x, y)) == GameMap.ROBOT) {
					positions.add(new Position(x, y));
					continue;
				}
			}
		}

		for (Position pos : positions) {
			RobotModel robot = (RobotModel) this.level.getMap()
					.getModelContent(pos);
			Direction robotDirection = robot.getDirection();
			if (canMove(robotDirection, pos)) {
				Position newPos = MapMeasurements.calculateNextPosition(
						robotDirection, pos);

				this.level.getMap().move(pos, newPos);
			} else {

				for (Direction direction : Direction.values()) {
					if (canMove(direction, pos)) {
						robot.setDirection(direction);

						Position newPos = MapMeasurements
								.calculateNextPosition(direction, pos);

						this.level.getMap().move(pos, newPos);
					}
				}
			}
		}
	}

	@Override
	public void putBomb() {
		this.bombToDraw = true;

		this.explosionTimeoutHandler.postDelayed(new BombExplosion(
				this.bombermanPos), this.level.getExplosionTimeout());
	}

	private void putExploding(Position pos) {
		GameMap map = this.level.getMap();

		if (map.hasBomberman(pos)) {
			map.putExploding(pos);
			this.bombermanKilled(Character.getNumericValue(map.getContent(pos)));
			return;
		}
		map.putExploding(pos);
	}

	@Override
	public int[] explode(Position pos) {
		boolean upSearchStop = false;
		boolean downSearchStop = false;
		boolean leftSearchStop = false;
		boolean rightSearchStop = false;

		int explosionRange = this.level.getExplosionRange();
		// indexes: 0 = Up; 1 = Down; 2 = Left; 3 = Right
		int[] efectiveRange = { explosionRange, explosionRange, explosionRange,
				explosionRange };

		this.putExploding(pos);
		for (int i = 1; i <= explosionRange; i++) {
			if (!upSearchStop
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x, pos.y - i) != GameMap.EMPTY) {
					upSearchStop = true;
					efectiveRange[0] = i;
				}
				this.putExploding(new Position(pos.x, pos.y - i));
			} else if (!upSearchStop) {
				upSearchStop = true;
				efectiveRange[0] = i - 1;
			}

			if (!downSearchStop
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x, pos.y + i) != GameMap.EMPTY) {
					downSearchStop = true;
					efectiveRange[1] = i;
				}
				this.putExploding(new Position(pos.x, pos.y + i));
			} else if (!downSearchStop) {
				downSearchStop = true;
				efectiveRange[1] = i - 1;
			}

			if (!leftSearchStop
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x - i, pos.y) != GameMap.EMPTY) {
					leftSearchStop = true;
					efectiveRange[2] = i;
				}
				this.putExploding(new Position(pos.x - i, pos.y));
			} else if (!leftSearchStop) {
				leftSearchStop = true;
				efectiveRange[2] = i - 1;
			}

			if (!rightSearchStop
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.WALL
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.EXPLODING
					&& this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.BOMB) {
				if (this.level.getMap().getContent(pos.x + i, pos.y) != GameMap.EMPTY) {
					rightSearchStop = true;
					efectiveRange[3] = i;
				}
				this.putExploding(new Position(pos.x + i, pos.y));
			} else if (!rightSearchStop) {
				rightSearchStop = true;
				efectiveRange[3] = i - 1;
			}
		}

		return efectiveRange;
	}

	@Override
	public void finishExplosion(Position pos, int[] efectiveRange) {
		GameMap map = this.level.getMap();

		map.putEmpty(pos);

		// Up
		for (int i = 1; i <= efectiveRange[0]; i++) {
			map.putEmpty(new Position(pos.x, pos.y - i));
		}

		// Down
		for (int i = 1; i <= efectiveRange[1]; i++) {
			map.putEmpty(new Position(pos.x, pos.y + i));
		}

		// Left
		for (int i = 1; i <= efectiveRange[2]; i++) {
			map.putEmpty(new Position(pos.x - i, pos.y));
		}

		// Right
		for (int i = 1; i <= efectiveRange[3]; i++) {
			map.putEmpty(new Position(pos.x + i, pos.y));
		}
	}

}
