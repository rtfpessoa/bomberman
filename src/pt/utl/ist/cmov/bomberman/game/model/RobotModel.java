package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;

public class RobotModel extends Model {

	private Direction direction;
	private Handler robotHandler;
	private Runnable robotRunnable;
	private Boolean isRunning;

	public RobotModel(Level level, Integer id, Position pos) {
		super(level, Level.ROBOT, id, pos);
		direction = Direction.LEFT;
		isRunning = true;
		robotHandler = new Handler();
		this.robotRunnable = new Runnable() {
			@Override
			public void run() {
				move();
				if (isRunning) {
					resetMoveRunnable();
				}
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
	
	@Override
	public void moveAction(Model model) {
		if (model.getType() == Level.EXPLODING) {
			this.level.putEmpty(this.pos);
			return;
		}
	}

	private void move() {
		Direction direction = this.getDirection();

		if (!level.move(this, direction)) {
			for (Direction newDirection : Direction.values()) {
				if (level.move(this, newDirection)) {
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
		isRunning = false;
		robotHandler.removeCallbacks(this.robotRunnable);
	}

}
