package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class ObstacleElement extends Element {

	public ObstacleElement(Level level, Integer id, Position pos) {
		super(level, Level.OBSTACLE, id, pos);
	}

	@Override
	public boolean canMoveOver(Element element) {
		return false;
	}

}
