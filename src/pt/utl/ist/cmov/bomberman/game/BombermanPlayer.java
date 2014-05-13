package pt.utl.ist.cmov.bomberman.game;

import java.io.Serializable;

public class BombermanPlayer implements Serializable {

	private static final long serialVersionUID = -1116953100184613328L;

	private Integer id;
	private String username;
	private Integer score;
	private Integer time;
	private Integer players;

	public BombermanPlayer(Integer id, String username) {
		this.id = id;
		this.username = username;
		this.score = 0;
		this.time = 0;
		this.players = 0;
	}

	public Integer getId() {
		return id;
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
