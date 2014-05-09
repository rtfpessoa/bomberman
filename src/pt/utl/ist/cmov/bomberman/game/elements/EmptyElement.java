package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class EmptyElement extends Element {

	public EmptyElement(Level level, Integer id, Position pos) {
		super(level, Level.EMPTY, id, pos);
	}

	@Override
	public boolean canMoveOver(Element element) {
		return true;
	}

	@Override
	public void moveAction(Element model) {
		if (model.getType() == Level.BOMBERMAN
				&& this.level.isInDeathZone(this.getPos())) {
			this.level.putEmpty(model.getPos());
			// TODO: Bomberman Killed
			return;
		}
	}
}
