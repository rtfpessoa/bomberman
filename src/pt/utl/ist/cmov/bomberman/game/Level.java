package pt.utl.ist.cmov.bomberman.game;

public class Level {

	private Integer gameDuration;
	private Integer explosionTimeout;
	private Integer explosionRange;
	private Integer robotSpeed;
	private Integer pointsRobot;
	private Integer pointsOpponent;
	private String map;

	public Level(Integer gameDuration, Integer explosionTimeout,
			Integer explosionRange, Integer robotSpeed, Integer pointsRobot,
			Integer pointsOpponent, String map) {
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
