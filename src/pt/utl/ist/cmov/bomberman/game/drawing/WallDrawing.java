package pt.utl.ist.cmov.bomberman.game.drawing;

import pt.utl.ist.cmov.bomberman.util.Position;

public class WallDrawing extends Drawing {

	private static final long serialVersionUID = -3021060390444833908L;

	public WallDrawing() {
		super();
	}

	public WallDrawing(Integer id, Position pos) {
		super(id, pos, "images/wall.png");
	}

}
