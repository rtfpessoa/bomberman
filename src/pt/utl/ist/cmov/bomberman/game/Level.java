package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;

public class Level {

	private Integer gameDuration;
	private Integer explosionTimeout;
	private Integer explosionRange;
	private Integer robotSpeed;
	private Integer pointsRobot;
	private Integer pointsOpponent;
	private ArrayList<ArrayList<Character>> map;

	public Level(Integer gameDuration, Integer explosionTimeout,
			Integer explosionRange, Integer robotSpeed, Integer pointsRobot,
			Integer pointsOpponent, ArrayList<ArrayList<Character>> map) {
		super();
		this.gameDuration = gameDuration;
		this.explosionTimeout = explosionTimeout;
		this.explosionRange = explosionRange;
		this.robotSpeed = robotSpeed;
		this.pointsRobot = pointsRobot;
		this.pointsOpponent = pointsOpponent;
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

	public ArrayList<ArrayList<Character>> getMap() {
		return map;
	}

	public void setMap(ArrayList<ArrayList<Character>> map) {
		this.map = map;
	}

}
