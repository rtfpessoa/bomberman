package pt.utl.ist.cmov.bomberman.game.model;

import java.util.LinkedList;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;
import android.util.Log;

public class BombModel extends Model {

	private BombermanModel bomberman;

	private Handler bombHandler;
	private Runnable bombRunnable;

	private Boolean hasExploded;
	private List<Position> positionsToExplode;

	public BombModel(Level level, Integer id, Position pos,
			BombermanModel bomberman) {
		super(level, Level.BOMB, id, pos);

		this.bombHandler = level.getHandler();
		this.bomberman = bomberman;
		this.hasExploded = false;
		this.positionsToExplode = new LinkedList<Position>();

		this.bombRunnable = new Runnable() {
			@Override
			public void run() {
				bombTimer();
			}
		};
		bombHandler.postDelayed(bombRunnable, level.getExplosionTimeout());
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BombermanModel getBomberman() {
		return this.bomberman;
	}

	@Override
	public boolean canMoveOver(Model model) {
		return false;
	}

	private void explode() {
		Integer explosionRange = this.level.getExplosionRange();

		positionsToExplode.add(this.getPos());

		for (Direction direction : Direction.values()) {
			Position pos = this.getPos();

			for (int i = 1; i <= explosionRange; i++) {
				pos = Position.calculateNext(direction, pos);
				Model model = this.level.getOnMap(pos);

				if (isStopElementType(model.getType())) {
					break;
				} else if (model.getType() != Level.EMPTY) {
					positionsToExplode.add(pos);
					break;
				} else {
					positionsToExplode.add(pos);
				}
			}
		}

		for (Position pos : positionsToExplode) {
			this.level.putExploding(this, pos);
		}
	}

	private boolean isStopElementType(Character type) {
		return type == Level.WALL || type == Level.EXPLODING
				|| type == Level.BOMB;
	}

	private void finishExplosion() {
		for (Position pos : positionsToExplode) {
			this.level.putEmpty(pos);
		}
	}

	private void bombTimer() {
		if (!hasExploded) {
			Log.i("BombExplosion", "Explosion");
			explode();
			this.hasExploded = true;
			bombHandler.postDelayed(bombRunnable, level.getExplosionDuration());
		} else {
			finishExplosion();
		}
	}

}
