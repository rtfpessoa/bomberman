package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanDrawing extends Drawing {

	private static final long serialVersionUID = -1384653994352488443L;

	public BombermanDrawing() {
		super();
	}

	public BombermanDrawing(Integer id, Position pos, Integer bombermanId) {
		super(id, pos, "images/bomberman" + bombermanId + ".png");
	}

}
