package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanModel extends Model {

	private Integer bombermanId;
	private Boolean isPaused;
	private BombermanPlayer player;
	private Boolean isDead;

	public BombermanModel(Level level, Integer id, Position pos,
			Integer bombermanId) {
		super(level, Level.BOMBERMAN, id, pos);

		this.bombermanId = bombermanId;
		this.isPaused = false;
		this.player = null;
		this.isDead = false;
	}

	public Integer getBombermanId() {
		return this.bombermanId;
	}

	public Boolean isPaused() {
		return this.isPaused;
	}

	public void setIsPaused(Boolean value) {
		this.isPaused = value;
	}

	public BombermanPlayer getPlayer() {
		return player;
	}

	public Boolean isDead() {
		return isDead;
	}

	public void putDead() {
		level.decrRemainingBomberman();
		this.isDead = true;
	}
	
	@Override
	public void putKillingZone() {
		this.putDead();
		this.level.putEmpty(this.pos);
	}

	public void setPlayer(BombermanPlayer player) {
		this.player = player;
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

	@Override
	public void moveAction(Model model) {
		if (model.getType() == Level.EXPLODING
				|| (model.getType() == Level.EMPTY && model.isKillingZone())) {
			this.putDead();
			this.level.putEmpty(this.pos);
			return;
		}
	}

	public void pause() {
		if (isDead) {
			return;
		}

		if (isPaused) {
			this.isPaused = false;
			this.level.putBomberman(this);
		} else {
			this.isPaused = true;
			this.level.putEmpty(pos);
		}
	}

}
