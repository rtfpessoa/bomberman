package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class EmptyModel extends Model {

	public EmptyModel(Level level, Integer id, Position pos) {
		super(level, Level.EMPTY, id, pos);
	}

	@Override
	public boolean canMoveOver(Model model) {
		if (isKillingZone() && model.getType() == Level.BOMBERMAN) {
			return false;
		}
		return true;
	}
}
