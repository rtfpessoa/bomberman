package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class ObstacleModel extends Model {

	public ObstacleModel(Level level, Integer id, Position pos) {
		super(level, Level.OBSTACLE, id, pos);
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

}
