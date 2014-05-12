package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanModel extends Model {

	private Integer bombermanId;
	private Boolean isPaused;

	public BombermanModel(Level level, Integer id, Position pos,
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
	public boolean canMoveOver(Model model) {
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
