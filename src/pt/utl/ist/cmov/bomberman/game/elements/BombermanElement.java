package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanElement extends Element {

	private Integer bombermanId;
	private Boolean isPaused;

	public BombermanElement(Level level, Integer id, Position pos,
			Integer bombermanId) {
		super(level, Level.BOMBERMAN, id, pos);

		this.bombermanId = bombermanId;
		this.isPaused = false;
	}

	public Integer getBombermanId() {
		return this.bombermanId;
	}
	
	public Boolean isPaused() {
		return this.isPaused;
	}

	@Override
	public boolean canMoveOver(Element model) {
		if (model.getType() == Level.ROBOT
				|| model.getType() == Level.EXPLODING) {
			return true;
		}

		return false;
	}

	public void pause() {
		if (isPaused) {
			this.isPaused = false;
			this.level.putBomberman(this);
		} else {
			this.isPaused = true;
			this.level.putEmpty(pos);
		}
	}

}
