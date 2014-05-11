package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.utl.ist.cmov.bomberman.game.elements.BombElement;
import pt.utl.ist.cmov.bomberman.game.elements.BombermanElement;
import pt.utl.ist.cmov.bomberman.game.elements.Element;
import pt.utl.ist.cmov.bomberman.game.elements.EmptyElement;
import pt.utl.ist.cmov.bomberman.game.elements.ExplosionElement;
import pt.utl.ist.cmov.bomberman.game.elements.ObstacleElement;
import pt.utl.ist.cmov.bomberman.game.elements.RobotElement;
import pt.utl.ist.cmov.bomberman.game.elements.WallElement;
import pt.utl.ist.cmov.bomberman.util.Direction;
import pt.utl.ist.cmov.bomberman.util.Position;

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

	private ArrayList<ArrayList<Element>> modelMap;

	private Integer bombermanIds;
	private Integer elementIds;

	private Boolean isPaused;

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
		this.modelMap = new ArrayList<ArrayList<Element>>();
		this.bombermansInitialPos = bombermansInitialPos;
		this.bombermanIds = 1;
		this.isPaused = false;
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
		return this.modelMap.get(y).get(x).getType();
	}

	public Character getContent(Position pos) {
		return this.modelMap.get(pos.y).get(pos.x).getType();
	}

	public Element getElementContent(Position pos) {
		return this.modelMap.get(pos.y).get(pos.x);
	}

	public Integer getHeight() {
		return this.height;
	}

	public Integer getWidth() {
		return this.width;
	}

	public Boolean move(Element element, Direction direction) {
		if (!this.isPaused) {
			Position newPos = Position.calculateNext(direction,
					element.getPos());

			Element destination = this.getElementContent(newPos);
			if (destination.canMoveOver(element)) {
				element.moveAction(destination);
				destination.moveAction(element);
				this.move(element.getPos(), newPos);
			} else {
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

	private void move(Position orig, Position dest) {
		Element model = this.modelMap.get(dest.y).get(dest.x);
		Element otherModel = this.modelMap.get(orig.y).get(orig.x);

		model.setPos(orig);
		otherModel.setPos(dest);

		this.modelMap.get(dest.y).set(dest.x, otherModel);
		this.modelMap.get(orig.y).set(orig.x, model);
	}

	public void putEmpty(Position pos) {
		Element current = this.modelMap.get(pos.y).get(pos.x);

		this.modelMap.get(pos.y).set(pos.x,
				new EmptyElement(this, current.getId(), pos));
	}

	public void putExploding(BombElement model, Position pos) {
		Element current = this.modelMap.get(pos.y).get(pos.x);

		this.modelMap.get(pos.y).set(pos.x,
				new ExplosionElement(this, current.getId(), pos, model));
	}

	public BombElement createBomb(BombermanElement bomberman) {
		Position pos = bomberman.getPos();

		return new BombElement(this, bomberman.getId(), pos, bomberman);
	}

	public void putBomb(BombElement bomb) {
		Element previous = this.modelMap.get(bomb.getPos().y).get(
				bomb.getPos().x);

		bomb.setId(previous.getId());

		this.modelMap.get(bomb.getPos().y).set(bomb.getPos().x, bomb);
	}

	public BombermanElement putBomberman() {
		Integer id = bombermanIds++;
		Position pos = this.getBombermanInitialPos(id);
		Element current = this.getMap().get(pos.y).get(pos.x);
		BombermanElement bomberman = new BombermanElement(this,
				current.getId(), pos, id);
		this.modelMap.get(pos.y).set(pos.x, bomberman);
		return bomberman;
	}

	public void parseMap(List<List<Character>> initialMap) {
		this.isPaused = true;

		this.height = initialMap.size();
		this.width = initialMap.get(0).size();

		this.maxBombermans = this.bombermansInitialPos.size();
		this.elementIds = maxBombermans + 1;

		for (int y = 0; y < this.height; y++) {
			ArrayList<Element> line = new ArrayList<Element>();

			for (int x = 0; x < this.width; x++) {
				Character c = initialMap.get(y).get(x);
				Integer id = this.elementIds++;

				Position pos = new Position(x, y);

				if (c == WALL)
					line.add(new WallElement(this, id, pos));
				else if (c == OBSTACLE)
					line.add(new ObstacleElement(this, id, pos));
				else if (c == ROBOT)
					line.add(new RobotElement(this, id, pos));
				else if (c == EMPTY)
					line.add(new EmptyElement(this, id, pos));
			}

			this.modelMap.add(line);
		}

		this.isPaused = false;
	}

	public boolean isInDeathZone(Position testPosition) {
		for (Direction direction : Direction.values()) {
			Position nextPosition = Position.calculateNext(direction,
					testPosition);
			if (getElementContent(nextPosition).getType() == Level.ROBOT) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<ArrayList<Element>> getMap() {
		return modelMap;
	}

}
