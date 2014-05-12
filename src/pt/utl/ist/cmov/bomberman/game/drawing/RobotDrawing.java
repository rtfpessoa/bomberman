package pt.utl.ist.cmov.bomberman.game.drawing;

import pt.utl.ist.cmov.bomberman.util.Position;

public class RobotDrawing extends Drawing {

	private static final long serialVersionUID = -1573822000954051199L;

	public RobotDrawing() {
		super();
	}

	public RobotDrawing(Integer id, Position pos) {
		super(id, pos, "images/robot.png");
	}

}
