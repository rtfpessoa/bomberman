package pt.utl.ist.cmov.bomberman.util;

import java.io.Serializable;

public class Position implements Serializable {

	private static final long serialVersionUID = 3586915855280960564L;

	public Integer x;
	public Integer y;

	public Position() {
		this.x = 0;
		this.y = 0;
	}

	public Position(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}

	public static Position calculateNext(Direction dir, Position pos) {
		switch (dir) {
		case UP:
			return new Position(pos.x, pos.y - 1);
		case DOWN:
			return new Position(pos.x, pos.y + 1);
		case LEFT:
			return new Position(pos.x - 1, pos.y);
		case RIGHT:
			return new Position(pos.x + 1, pos.y);
		}

		return null;
	}

	public static Position calculateUpPosition(Position pos) {
		return new Position(pos.x, pos.y - 1);
	}

	public static Position calculateDownPosition(Position pos) {
		return new Position(pos.x, pos.y + 1);
	}

	public static Position calculateLeftPosition(Position pos) {
		return new Position(pos.x - 1, pos.y);
	}

	public static Position calculateRightPosition(Position pos) {
		return new Position(pos.x + 1, pos.y);
	}

}
