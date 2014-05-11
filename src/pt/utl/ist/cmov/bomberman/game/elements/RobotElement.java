package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;

public class RobotElement extends Element {

	private Direction direction = Direction.LEFT;
	private Handler robotHandler = new Handler();
	private Runnable robotRunnable;

	public RobotElement(Level level, Integer id, Position pos) {
		super(level, Level.ROBOT, id, pos);
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
	public boolean canMoveOver(Element element) {
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

}
