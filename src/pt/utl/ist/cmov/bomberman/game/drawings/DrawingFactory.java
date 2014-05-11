package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.elements.BombermanElement;
import pt.utl.ist.cmov.bomberman.game.elements.Element;

public class DrawingFactory {

	public static Drawing create(Element element) {
		if (element.getType() == Level.BOMB) {
			return new BombDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.BOMBERMAN) {
			BombermanElement bomberman = (BombermanElement) element;

			return new BombermanDrawing(element.getId(), element.getPos(),
					bomberman.getBombermanId());
		}
		if (element.getType() == Level.EMPTY) {
			return new EmptyDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.EXPLODING) {
			return new ExplosionDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.OBSTACLE) {
			return new ObstacleDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.ROBOT) {
			return new RobotDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.WALL) {
			return new WallDrawing(element.getId(), element.getPos());
		}
		return null;
	}

}
