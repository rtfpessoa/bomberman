package pt.utl.ist.cmov.bomberman.game.drawing;

import pt.utl.ist.cmov.bomberman.util.Position;

public class BombDrawing extends Drawing {

	private static final long serialVersionUID = 3660148771402000957L;

	public BombDrawing() {
		super();
	}

	public BombDrawing(Integer id, Position pos) {
		super(id, pos, "images/bomb.png");
	}

}
