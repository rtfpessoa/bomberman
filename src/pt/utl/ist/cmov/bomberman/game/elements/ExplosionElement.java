package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class ExplosionElement extends Element {
	
	private BombElement bomb;

	public ExplosionElement(Level level, Integer id, Position pos, BombElement bomb) {
		super(level, Level.EXPLODING, id, pos);
		
		this.bomb = bomb;
	}

	@Override
	public boolean canMoveOver(Element element) {
		return true;
	}
	
	@Override
	public void moveAction(Element model) {
		if (model.getType() == Level.BOMBERMAN) {
			// TODO: Bomberman Killed
			// TODO: Bomberman gets PO points

			return;
		}
		if (model.getType() == Level.ROBOT) {
			// TODO: Bomberman gets PR points
			return;
		}
	}

}
