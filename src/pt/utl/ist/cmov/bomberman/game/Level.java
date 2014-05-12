package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import pt.utl.ist.cmov.bomberman.game.dto.ModelDTOFactory;
import pt.utl.ist.cmov.bomberman.game.model.BombModel;
import pt.utl.ist.cmov.bomberman.game.model.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.model.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.model.ExplosionModel;
import pt.utl.ist.cmov.bomberman.game.model.Model;
import pt.utl.ist.cmov.bomberman.game.model.ObstacleModel;
import pt.utl.ist.cmov.bomberman.game.model.RobotModel;
import pt.utl.ist.cmov.bomberman.game.model.WallModel;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Handler;

public class Level {

	private Integer gameDuration;
	private Integer explosionTimeout;
	private Integer explosionDuration;
	private Integer explosionRange;
	private Integer robotSpeed;
	private Integer pointsRobot;
	private Integer pointsOpponent;
	private Map<Integer, Position> bombermansInitialPos;
	private Integer maxBombermans;

	public static final Character WALL = 'W';
	public static final Character OBSTACLE = 'O';
	public static final Character ROBOT = 'R';
	public static final Character BOMB = 'B';
	public static final Character EMPTY = '-';
	public static final Character EXPLODING = 'E';
	public static final Character BOMBERMAN = 'P';

	private Integer height;
	private Integer width;

	private ArrayList<ArrayList<Model>> modelMap;

	private Integer bombermanIds;
	private Integer modelIds;

	private Boolean isPaused;

	private ArrayList<ModelDTO> updatesBuffer;

	private Handler handler;

	public Level(Integer gameDuration, Integer explosionTimeout,
			Integer explosionDuration, Integer explosionRange,
			Integer robotSpeed, Integer pointsRobot, Integer pointsOpponent,
			Map<Integer, Position> bombermansInitialPos) {
		super();
		this.gameDuration = gameDuration;
		this.explosionTimeout = explosionTimeout;
		this.explosionDuration = explosionDuration;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsRobot = pointsRobot;
		this.pointsOpponent = pointsOpponent;
		this.modelMap = new ArrayList<ArrayList<Model>>();
		this.bombermansInitialPos = bombermansInitialPos;
		this.bombermanIds = 1;
		this.isPaused = false;
		this.updatesBuffer = new ArrayList<ModelDTO>();
		this.handler = new Handler();
	}

	public Handler getHandler() {
		return this.handler;
	}

	public Integer getGameDuration() {
		return gameDuration;
	}

	public void setGameDuration(Integer gameDuration) {
		this.gameDuration = gameDuration;
	}

	public Integer getExplosionTimeout() {
		return explosionTimeout;
	}

	public void setExplosionTimeout(Integer explosionTimeout) {
		this.explosionTimeout = explosionTimeout;
	}

	public Integer getExplosionDuration() {
		return explosionDuration;
	}

	public void setExplosionDuration(Integer explosionDuration) {
		this.explosionDuration = explosionDuration;
	}

	public Integer getExplosionRange() {
		return explosionRange;
	}

	public void setExplosionRange(Integer explosionRange) {
		this.explosionRange = explosionRange;
	}

	public Integer getRobotSpeed() {
		return robotSpeed;
	}

	public void setRobotSpeed(Integer robotSpeed) {
		this.robotSpeed = robotSpeed;
	}

	public Integer getPointsRobot() {
		return pointsRobot;
	}

	public void setPointsRobot(Integer pointsRobot) {
		this.pointsRobot = pointsRobot;
	}

	public Integer getPointsOpponent() {
		return pointsOpponent;
	}

	public void setPointsOpponent(Integer pointsOpponent) {
		this.pointsOpponent = pointsOpponent;
	}

	public Position getBombermanInitialPos(Integer id) {
		return this.bombermansInitialPos.get(id);
	}

	public Integer getMaxBombermans() {
		return maxBombermans;
	}

	public Character getContent(Integer x, Integer y) {
		return getOnMap(new Position(x, y)).getType();
	}

	public Character getContent(Position pos) {
		return getOnMap(pos).getType();
	}

	public Model getOnMap(Position pos) {
		return this.modelMap.get(pos.y).get(pos.x);
	}

	public Integer getHeight() {
		return this.height;
	}

	public Integer getWidth() {
		return this.width;
	}

	public Model move(Model model, Direction direction) {
		if (!this.isPaused) {
			Position newPos = Position.calculateNext(direction, model.getPos());

			Model destination = this.getOnMap(newPos);
			if (destination.canMoveOver(model)) {
				model.moveAction(destination);
				destination.moveAction(model);
				this.move(model.getPos(), newPos);
			} else {
				return null;
			}

			return destination;
		} else {
			return null;
		}
	}

	private void move(Position orig, Position dest) {
		synchronized (this.modelMap) {
			Model model = getOnMap(dest);
			Model otherModel = getOnMap(orig);

			model.setPos(orig);
			otherModel.setPos(dest);

			setOnMap(dest, otherModel);
			setOnMap(orig, model);
		}
	}

	public void putEmpty(Position pos) {
		Model current = getOnMap(pos);

		EmptyModel empty = new EmptyModel(this, current.getId(), pos);
		setOnMap(pos, empty);
	}

	public void putExploding(BombModel model, Position pos) {
		Model current = getOnMap(pos);

		ExplosionModel explosion = new ExplosionModel(this, current.getId(),
				pos, model);
		setOnMap(pos, explosion);
	}

	public BombModel createBomb(BombermanModel bomberman) {
		Position pos = bomberman.getPos();

		return new BombModel(this, bomberman.getId(), pos, bomberman);
	}

	public void putBomb(BombModel bomb) {
		Model previous = getOnMap(bomb.getPos());

		bomb.setId(previous.getId());

		setOnMap(bomb.getPos(), bomb);
	}

	public BombermanModel putBomberman() {
		Integer id = bombermanIds++;
		Position pos = this.getBombermanInitialPos(id);
		Model current = this.getMap().get(pos.y).get(pos.x);
		BombermanModel bomberman = new BombermanModel(this, current.getId(),
				pos, id);
		setOnMap(pos, bomberman);

		return bomberman;
	}

	public void putBomberman(BombermanModel bomberman) {
		setOnMap(bomberman.getPos(), bomberman);
	}

	public void parseMap(List<List<Character>> initialMap) {
		this.isPaused = true;

		this.height = initialMap.size();
		this.width = initialMap.get(0).size();

		this.maxBombermans = this.bombermansInitialPos.size();
		this.modelIds = maxBombermans + 1;

		for (int y = 0; y < this.height; y++) {
			ArrayList<Model> line = new ArrayList<Model>();

			for (int x = 0; x < this.width; x++) {
				Character c = initialMap.get(y).get(x);
				Integer id = this.modelIds++;

				Position pos = new Position(x, y);

				if (c == WALL)
					line.add(new WallModel(this, id, pos));
				else if (c == OBSTACLE)
					line.add(new ObstacleModel(this, id, pos));
				else if (c == ROBOT)
					line.add(new RobotModel(this, id, pos));
				else if (c == EMPTY)
					line.add(new EmptyModel(this, id, pos));
			}

			this.modelMap.add(line);
		}

		this.isPaused = false;
	}

	public boolean isInDeathZone(Position testPosition) {
		for (Direction direction : Direction.values()) {
			Position nextPosition = Position.calculateNext(direction,
					testPosition);
			if (getOnMap(nextPosition).getType() == Level.ROBOT) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<ArrayList<Model>> getMap() {
		return modelMap;
	}

	public ArrayList<ModelDTO> getLatestUpdates() {
		ArrayList<ModelDTO> dtos = new ArrayList<ModelDTO>(updatesBuffer);
		this.updatesBuffer = new ArrayList<ModelDTO>();

		return dtos;
	}

	public void stopAll() {
		for (ArrayList<Model> line : modelMap) {
			for (Model model : line) {
				if (model.getType().equals(ROBOT)) {
					RobotModel robot = (RobotModel) model;
					robot.stopAll();
				}
			}
		}
	}

	private void setOnMap(Position position, Model model) {
		this.modelMap.get(position.y).set(position.x, model);
		updatesBuffer.add(ModelDTOFactory.create(model));
	}
}
