package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class EmptyModel extends Model {

	public EmptyModel(Level level, Integer id, Position pos) {
		super(level, Level.EMPTY, id, pos);
	}

	@Override
	public boolean canMoveOver(Model model) {
		return true;
	}

	@Override
	public void moveAction(Model model) {
		if (model.getType() == Level.BOMBERMAN
				&& this.level.isInDeathZone(this.getPos())) {
			this.level.putEmpty(model.getPos());
			// TODO: Bomberman Killed
			return;
		}
	}
}
