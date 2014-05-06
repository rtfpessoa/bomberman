package pt.utl.ist.cmov.bomberman.game;

import java.util.Map;

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
	private GameMap map;

	public Level(Integer gameDuration, Integer explosionTimeout,
			Integer explosionDuration, Integer explosionRange,
			Integer robotSpeed, Integer pointsRobot, Integer pointsOpponent,
			Map<Integer, Position> bombermansInitialPos, GameMap map) {
		super();
		this.gameDuration = gameDuration;
		this.explosionTimeout = explosionTimeout;
		this.explosionDuration = explosionDuration;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsRobot = pointsRobot;
		this.pointsOpponent = pointsOpponent;
		this.bombermansInitialPos = bombermansInitialPos;
		this.maxBombermans = this.bombermansInitialPos.size();
		this.map = map;
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

	public GameMap getMap() {
		return map;
	}

}
