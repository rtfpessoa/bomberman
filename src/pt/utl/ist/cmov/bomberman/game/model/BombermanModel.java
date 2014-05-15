package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.BombermanPlayer;
import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class BombermanModel extends Model {

	private Integer bombermanId;
	private Boolean isPaused;
	private BombermanPlayer player;

	public BombermanModel(Level level, Integer id, Position pos,
			Integer bombermanId) {
		super(level, Level.BOMBERMAN, id, pos);

		this.bombermanId = bombermanId;
		this.isPaused = false;
		this.player = null;
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

	public void setPlayer(BombermanPlayer player) {
		this.player = player;
	}

	@Override
	public boolean canMoveOver(Model model) {
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
