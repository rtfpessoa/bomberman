package pt.utl.ist.cmov.bomberman.game.elements;

import java.io.Serializable;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Element implements Serializable {

	private static final long serialVersionUID = -6958193175944759916L;

	protected Level level;
	protected Position pos;
	protected Integer id;
	private final Character type;

	public Element(Level level, Character type, Integer id, Position pos) {
		super();

		this.level = level;
		this.type = type;
		this.id = id;
		this.pos = pos;
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

	public abstract boolean canMoveOver(Element element);

	public void moveAction(Element element) {

	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

}
