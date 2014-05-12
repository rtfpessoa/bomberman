package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;

public class RobotModel extends Model {

	private Direction direction;
	private Handler robotHandler;
	private Runnable robotRunnable;

	public RobotModel(Level level, Integer id, Position pos) {
		super(level, Level.ROBOT, id, pos);
		direction = Direction.LEFT;
		robotHandler = new Handler();
		this.robotRunnable = new Runnable() {
			@Override
			public void run() {
				move();
				resetMoveRunnable();
			}
		};

		this.robotRunnable.run();
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

	private void move() {
		Direction direction = this.getDirection();

		if (level.move(this, direction) == null) {
			for (Direction newDirection : Direction.values()) {
				if (level.move(this, newDirection) != null) {
					this.setDirection(newDirection);
					return;
				}
			}
		}
	}

	private void resetMoveRunnable() {
		robotHandler.postDelayed(robotRunnable, level.getRobotSpeed());
	}

	public void stopAll() {
		robotHandler.removeCallbacks(this.robotRunnable);
	}

}
