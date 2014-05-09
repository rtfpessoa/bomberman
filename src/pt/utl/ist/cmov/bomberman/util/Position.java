package pt.utl.ist.cmov.bomberman.util;

public class Position {

	public Integer x;
	public Integer y;

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

}
