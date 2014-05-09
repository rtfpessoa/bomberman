package pt.utl.ist.cmov.bomberman.game.elements;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public abstract class Element {

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
		return type;
	}

	public Position getPos() {
		return pos;
	}

	public abstract boolean canMoveOver(Element element);

	public void moveAction(Element element) {

	}

	public void setPos(Position pos) {
		this.pos = pos;
	}

}
