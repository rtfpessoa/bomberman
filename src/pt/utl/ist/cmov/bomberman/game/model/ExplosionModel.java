package pt.utl.ist.cmov.bomberman.game.model;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Position;

public class ExplosionModel extends Model {

	private BombModel bomb;

	public ExplosionModel(Level level, Integer id, Position pos, BombModel bomb) {
		super(level, Level.EXPLODING, id, pos);

		this.bomb = bomb;
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

	@Override
	public void moveAction(Model model) {
		if (model.getType() == Level.BOMBERMAN) {
			if (model.getId() != bomb.getBomberman().getId()) {
				bomb.getBomberman().getPlayer()
						.addToScore(this.level.getPointsOpponent());
			}

			return;
		}
		if (model.getType() == Level.ROBOT) {
			((RobotModel) model).stopAll();
			bomb.getBomberman().getPlayer()
					.addToScore(this.level.getPointsRobot());
			return;
		}
	}

}
