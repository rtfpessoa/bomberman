package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class WallModel extends Model {

	public WallModel(Level level, Integer id, Position pos) {
		super(level, Level.WALL, id, pos);
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

}
