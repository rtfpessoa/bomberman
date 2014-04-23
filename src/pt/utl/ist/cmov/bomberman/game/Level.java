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
}
