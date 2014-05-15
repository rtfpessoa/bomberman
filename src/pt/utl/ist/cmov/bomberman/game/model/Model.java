package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Model {

	protected Level level;
	protected Position pos;
	protected Integer id;
	private final Character type;
	private Boolean isKillingZone;

	public Model(Level level, Character type, Integer id, Position pos) {
		super();

		this.level = level;
		this.type = type;
		this.id = id;
		this.pos = pos;
		this.isKillingZone = false;
	}

	public Character getType() {
		return this.type;
	}

	public Integer getId() {
		return this.id;
	}

	public Position getPos() {
		return this.pos;
	}

	public Boolean isKillingZone() {
		return this.isKillingZone;
	}

	public void putKillingZone() {
		this.isKillingZone = true;
	}

	public void removeKillingZone() {
		this.isKillingZone = false;
	}

	public abstract boolean canMoveOver(Model model);

	public void moveAction(Model model) {

	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

}
