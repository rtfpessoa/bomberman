package pt.utl.ist.cmov.bomberman.game.drawing;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;

public class DrawingFactory {

	public static Drawing create(ModelDTO model) {
		if (model.getType() == Level.BOMB) {
			return new BombDrawing(model.getId(), model.getPos());
		}
		if (model.getType() == Level.BOMBERMAN) {
			return new BombermanDrawing(model.getId(), model.getPos(),
					model.getBombermanId());
		}
		if (model.getType() == Level.EMPTY) {
			return new EmptyDrawing(model.getId(), model.getPos());
		}
		if (model.getType() == Level.EXPLODING) {
			return new ExplosionDrawing(model.getId(), model.getPos());
		}
		if (model.getType() == Level.OBSTACLE) {
			return new ObstacleDrawing(model.getId(), model.getPos());
		}
		if (model.getType() == Level.ROBOT) {
			return new RobotDrawing(model.getId(), model.getPos());
		}
		if (model.getType() == Level.WALL) {
			return new WallDrawing(model.getId(), model.getPos());
		}
		return null;
	}

}
