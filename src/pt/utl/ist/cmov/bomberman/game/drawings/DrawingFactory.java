package pt.utl.ist.cmov.bomberman.game.drawings;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import android.content.Context;

public class DrawingFactory {

	public static Drawing create(Context context, Element element) {
		if (element.getType() == Level.BOMB) {
			return new BombDrawing(context, element.getId(), element.getPos());
		}
		if (element.getType() == Level.BOMBERMAN) {
			return new BombermanDrawing(context, element.getId(),
					element.getPos());
		}
		if (element.getType() == Level.EMPTY) {
			return new EmptyDrawing(element.getId(), element.getPos());
		}
		if (element.getType() == Level.EXPLODING) {
			return new ExplosionDrawing(context, element.getId(),
					element.getPos());
		}
		if (element.getType() == Level.OBSTACLE) {
			return new ObstacleDrawing(context, element.getId(),
					element.getPos());
		}
		if (element.getType() == Level.ROBOT) {
			return new RobotDrawing(context, element.getId(), element.getPos());
		}
		if (element.getType() == Level.WALL) {
			return new WallDrawing(context, element.getId(), element.getPos());
		}
		return null;
	}

}
