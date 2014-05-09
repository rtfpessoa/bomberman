package pt.utl.ist.cmov.bomberman.game;

public class BombermanPlayer {

	private String username;
	private Integer score;
	private Integer time;
	private Integer players;

	public BombermanPlayer(String username) {
		this.username = username;
		this.score = 0;
		this.time = 0;
		this.players = 0;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getPlayers() {
		return players;
	}

	public void setPlayers(Integer players) {
		this.players = players;
	}

}
