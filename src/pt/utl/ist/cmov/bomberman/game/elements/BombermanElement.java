package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanElement extends Element {

	private Integer bombermanId;

	public BombermanElement(Level level, Integer id, Position pos,
			Integer bombermanId) {
		super(level, Level.BOMBERMAN, id, pos);

		this.bombermanId = bombermanId;
	}

	public Integer getBombermanId() {
		return this.bombermanId;
	}

	@Override
	public boolean canMoveOver(Element model) {
		if (model.getType() == Level.ROBOT
				|| model.getType() == Level.EXPLODING) {
			return true;
		}

		return false;
	}

}
